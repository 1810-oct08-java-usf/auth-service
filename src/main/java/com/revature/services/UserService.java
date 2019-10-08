package com.revature.services;

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

import com.revature.models.AppUser;
import com.revature.models.UserPrincipal;
import com.revature.repositories.UserRepository;

/**
 * This class contains methods that should be accessed by the controller to find and edit users.
 * @author Caleb
 *
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
		AppUser tempUser = findUserByUsername(newUser.getUsername());
		if(tempUser != null) return null;
		tempUser = findUserByEmail(newUser.getEmail());
		if(tempUser != null) return null;
		newUser.setRole("ROLE_USER");
		return repo.save(newUser);
	}

	/**
	 * This method will update a user's information
	 * @param user
	 * @return false if the user does not exist
	 * @return true if the user exists and was updated
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean updateUser(AppUser user) {
	      System.out.println("In update user of user service");
		if(user == null) return false;
		repo.save(user);
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
		Optional<AppUser> optUser = repo.findById(id);
		if(optUser.isPresent()) {
			AppUser tempUser = optUser.get();
			repo.delete(tempUser);
			return true;
		}
		else {
			return false;
		}
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
	
}
