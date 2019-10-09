package com.revature.rpm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.exceptions.UserUpdateException;
import com.revature.rpm.entities.AppUser;
import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.repositories.UserRepository;

/*
 * TODO
 * 
 * - encrypt user passwords prior to persistence (should not break security functionality)
 */

/**
 * This class contains methods that should be accessed by the 
 * controller to find and edit users.
 */
@Service
public class UserService implements UserDetailsService {
	
	private BCryptPasswordEncoder encoder;
	private UserRepository repo;
	
	@Autowired
	public UserService(UserRepository repo, BCryptPasswordEncoder encoder) {
		this.repo = repo;
		this.encoder = encoder;
	}
	
	/**
	 * This method will find all users
	 * @return All users
	 */
	@Transactional(readOnly=true, isolation=Isolation.SERIALIZABLE)
	public List<AppUser> findAllUsers(){
		return repo.findAll();
	}
	
	/**
	 * This method will find a user with the specified id
	 * @param id
	 * @return The user with the specified id
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public AppUser findById(int id) {
		Optional<AppUser> optUser = repo.findById(id);
		if(optUser.isPresent()) return optUser.get();
		else return null;
	}
	
	/**
	 * This method will find a user with a given username
	 * @param username
	 * @return null if there is no user with that username
	 * @return The user with the given username
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public AppUser findUserByUsername(String username) {
		return repo.findUserByUsername(username);
	}
	
	/**
	 * This method will find a user with a given username
	 * @param email
	 * @return The user with the given username
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public AppUser findUserByEmail(String email) {
		return repo.findUserByEmail(email);
	}
	
	/**
	 * This method will save a new user to the DB if it does not already exist
	 * @param newUser
	 * @return null if the username or email already exists
	 * @return the new user that was created
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public AppUser addUser(AppUser newUser) {
		
		if(isUsernameAvailable(newUser.getUsername()) || isEmailAddressAvailable(newUser.getUsername())) {
			newUser.setRole("ROLE_USER");
			return repo.save(newUser);
		} else {
			throw new UserCreationException("Username or email address already taken");
		}
		
	}

	/**
	 * This method will update a user's information
	 * @param updatedUser
	 * @return false if the user does not exist
	 * @return true if the user exists and was updated
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean updateUser(AppUser updatedUser, boolean updateRole) {
		
		if(!validateFields(updatedUser)) return false;
		
		if(isUsernameAvailable(updatedUser.getUsername()) || isEmailAddressAvailable(updatedUser.getUsername())) {
			throw new UserCreationException("Username or email address already taken");
		}
		
		Optional<AppUser> _userBeforeUpdate = repo.findById(updatedUser.getId());
		if(!_userBeforeUpdate.isPresent()) {
			throw new UserNotFoundException("No user found with the provided id");
		}
		
		AppUser userBeforeUpdate = _userBeforeUpdate.get();
		
		String expectedUsername = userBeforeUpdate.getUsername();
		String actualUsername = updatedUser.getUsername();
		if(!actualUsername.equals(expectedUsername)) {
			throw new UserUpdateException("Usernames cannot be changed");
		}
		
		String expectedRole = userBeforeUpdate.getRole();
		String actualRole = updatedUser.getRole();
		if(!updateRole && !actualRole.equals(expectedRole)) {
			throw new UserUpdateException("Could not update user role");
		}
		
		repo.save(updatedUser);
		
		return true;
		
	}
	
	/**
	 * This method should delete a user form the DB given an id
	 * @param id
	 * @return true if the user with that id was found and deleted
	 * @return false if the user with the given id was not found
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean deleteUserById(int id) {
		
		Optional<AppUser> _user = repo.findById(id);
		
		if(!_user.isPresent()) {
			throw new UserNotFoundException("No user found with provided id");
		}
		
		repo.delete(_user.get());
		return true;
	}
	
	/**
	 * Returns a user from the DB using the provided username. If no user is found
	 * with the given username, an exception is thrown.
	 * 
	 *  @param username
	 *  	The username of a user.
	 *  
	 *  @return UserDetails
	 *  	Provides core user information.
	 *  
	 *  @throws UsernameNotFoundException
	 *  	Throws this if there is no user found with the given username.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AppUser retrievedUser = repo.findUserByUsername(username);
		
		if(retrievedUser == null) {
			throw new UsernameNotFoundException("Username: " + username + " not found");
		}
		
		String userPw = retrievedUser.getPassword();
		String encodedPw = encoder.encode(userPw);
		String userRole = retrievedUser.getRole();
		retrievedUser.setPassword(encodedPw);
	
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(userRole);

		return new UserPrincipal(retrievedUser, username, encodedPw, grantedAuthorities);

	}
	
	/**
	 * Checks to see if there is a user record with the provided username.
	 * 
	 * @param username
	 * @return true if present, false if not
	 */
	public boolean isUsernameAvailable(String username) {
		if(repo.findUserByUsername(username) == null) return true;
		return false;
	}
	
	/**
	 * Checks to see if there is a user record with the provided email.
	 * 
	 * @param username
	 * @return true if present, false if not
	 */
	public boolean isEmailAddressAvailable(String email) {
		if(repo.findUserByEmail(email) == null) return true;
		return false;
	}
	
	/**
	 * Checks to see if the provided user objcet has valid fields.
	 * 
	 * @param user
	 * @return true if valid, false if invalid
	 */
	public boolean validateFields(AppUser user) {
		if(user == null) return false;
		if(user.getFirstName() == null || user.getFirstName().equals("")) return false;
		if(user.getLastName() == null || user.getLastName().equals("")) return false;
		if(user.getUsername() == null || user.getUsername().equals("")) return false;
		if(user.getPassword() == null || user.getPassword().equals("")) return false;
		if(user.getEmail() == null || user.getEmail().equals("")) return false;
		if(user.getRole() == null || user.getRole().equals("")) return false;
		return true;
	}
}
