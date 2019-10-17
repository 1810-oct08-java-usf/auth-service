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

import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.exceptions.UserUpdateException;
import com.revature.rpm.repositories.UserRepository;

/*
 * TODO
 * 
 * - encrypt user passwords prior to persistence (should not break security functionality)
 */

/**
 * This class contains methods that should be accessed by the controller to find
 * and edit users. Validation is included to ensure requests are proper,
 * otherwise an exception will be thrown detailing the validation rule that was
 * violated.
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
	 * Retrieves a list of all users from the data repository
	 * 
	 * @return a list of all users present in the data source
	 */
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public List<AppUser> findAllUsers() {
		return repo.findAll();
	}

	/**
	 * Retrieves a single user object from the data repository with a match id
	 * 
	 * @param id the id of the sought user
	 * 
	 * @return the user with the specified id
	 * 
	 * @throws BadRequestException   if the id provided is invalid
	 * 
	 * @throws UserNotFoundException if there is no user found with the provided id
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	public AppUser findUserById(int id) {

		if (id <= 0) {
			throw new BadRequestException("Invalid id value provided");
		}

		Optional<AppUser> _user = repo.findById(id);
		if (!_user.isPresent()) {
			throw new UserNotFoundException("No user found with provided id");
		}

		return _user.get();
	}

	/**
	 * Retrieves a single user object from the data repository with a match username
	 * 
	 * @param username the username of the sought user
	 * 
	 * @return the user with the specified username
	 * 
	 * @throws BadRequestException   if the username provided is invalid
	 * 
	 * @throws UserNotFoundException if there is no user found with the provided
	 *                               username
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	public AppUser findUserByUsername(String username) {

		if (username == null || username.equals("")) {
			throw new BadRequestException("Invalid username value provided");
		}

		AppUser retrievedUser = repo.findUserByUsername(username);

		if (retrievedUser == null) {
			throw new UserNotFoundException("No user found with provided username");
		}

		return retrievedUser;

	}

	/**
	 * Retrieves a single user object from the data repository with a match id.
	 * Throws a BadReq
	 * 
	 * @param email the email of the sought user
	 * 
	 * @return the user with the specified email
	 * 
	 * @throws BadRequestException   if the email provided is invalid
	 * 
	 * @throws UserNotFoundException if there is no user found with the provided
	 *                               email
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	public AppUser findUserByEmail(String email) {

		if (email == null || email.equals("")) {
			throw new BadRequestException("Invalid email value provided");
		}

		AppUser retrievedUser = repo.findUserByEmail(email);

		if (retrievedUser == null) {
			throw new UserNotFoundException("No user found with provided email");
		}

		return retrievedUser;

	}

	/**
	 * Attempts to add a new user to the data repository.
	 * 
	 * @param newUser the new user to be added to the data repository
	 * 
	 * @return the persisted user object with its generated id
	 * 
	 * @throws BadRequestException if an invalid user object is provided
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public AppUser addUser(AppUser newUser) {

		if (!validateFields(newUser)) {
			throw new BadRequestException("Invalid user object provided");
		}

		if (!isUsernameAvailable(newUser.getUsername())) {
			throw new UserCreationException("Username already in use");
		}

		if (!isEmailAddressAvailable(newUser.getEmail())) {
			throw new UserCreationException("Email address already in use");
		}

		newUser.setRole("ROLE_USER");
		return repo.save(newUser);

	}

	/**
	 * Attempts to update the user in the data repository.
	 * 
	 * @param updatedUser
	 * 		the user object to be used for updating the data source
	 * 
	 * @param updateRole
	 * 		a flag used to determine if role updating is allowed
	 * 
	 * @return
	 * 		true, if the update was successful; otherwise an exception is thrown
	 * 
	 * @throws BadRequestException
	 * 		if an invalid user object is provided
	 * 
	 * @throws UserUpdateException
	 * 		if the updated username or email is already in use or if a role change
	 * 		is attempted without the proper boolean flag passed to the method
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean updateUser(AppUser updatedUser, AppUser requester) {
		
		if (requester == null) {
			throw new BadRequestException("Invalid requester object provided");
		}
		
		if (!validateFields(updatedUser) || updatedUser.getId() == null) {
			throw new BadRequestException("Invalid user object provided");
		}
		
		if (updatedUser.getId() != requester.getId() && !requester.getRole().equals("ADMIN")) {
			throw new SecurityException("Illegal update request made by " + requester.getUsername());
		}

		AppUser userBeforeUpdate = null;
		
		try {
			 userBeforeUpdate = findUserById(updatedUser.getId());
		} catch (UserNotFoundException unfe) {
			throw new UserUpdateException(unfe.getMessage());
		}
		
		String persistedUsername = userBeforeUpdate.getUsername();
		String updatedUsername = updatedUser.getUsername();
		if (!persistedUsername.equals(updatedUsername)) {
			AppUser u = repo.findUserByUsername(updatedUsername);
			if (u != null) {
				throw new UserUpdateException("Username is already in use");
			}
			
		}
		
		
		String persistedEmail = userBeforeUpdate.getEmail();
		String updatedEmail = updatedUser.getEmail();
		if (!persistedEmail.equals(updatedEmail)) {
			AppUser u = repo.findUserByEmail(updatedEmail);
			if (u != null) {
				throw new UserUpdateException("Username is already in use");
			}
			
		}

		String persistedRole = userBeforeUpdate.getRole();
		String updatedRole = updatedUser.getRole();
		if (!requester.getRole().equals("ADMIN") && !updatedRole.equals(persistedRole)) {
			throw new UserUpdateException("Could not update user role");
		}
		
		repo.save(updatedUser);
		
		return true;
		
	}

	/**
	 * Attempts to delete a user from the data repository using the provided id
	 * 
	 * @param id the id of the user for deletion
	 * 
	 * @return true, if the delete was successful; otherwise an exception is thrown
	 * 
	 * @throws BadRequestException   if the provided id is invalid
	 * 
	 * @throws UserNotFoundException if not user is found with the provided id
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteUserById(int id) {

		if (id <= 0) {
			throw new BadRequestException("Invalid id value provided");
		}

		Optional<AppUser> _user = repo.findById(id);

		if (!_user.isPresent()) {
			throw new UserNotFoundException("No user found with provided id");
		}

		repo.delete(_user.get());
		return true;

	}

	/**
	 * Checks to see if there is a user record with the provided username
	 * 
	 * @param username the username being checked for availability
	 * 
	 * @return true if present, false if not
	 */
	public boolean isUsernameAvailable(String username) {

		try {
			findUserByUsername(username);
		} catch (UserNotFoundException unfe) {
			return true;
		}

		return false;

	}

	/**
	 * Checks to see if there is a user record with the provided email
	 * 
	 * @param email the email being checked for availability
	 * 
	 * @return true if present, false if not
	 */
	public boolean isEmailAddressAvailable(String email) {

		try {
			findUserByEmail(email);
		} catch (UserNotFoundException unfe) {
			return true;
		}

		return false;

	}

	/**
	 * Checks to see if the provided user objcet has valid fields.
	 * 
	 * @param user
	 * @return true if valid, false if invalid
	 */
	public boolean validateFields(AppUser user) {
		if (user == null)
			return false;
		if (user.getFirstName() == null || user.getFirstName().equals(""))
			return false;
		if (user.getLastName() == null || user.getLastName().equals(""))
			return false;
		if (user.getUsername() == null || user.getUsername().equals(""))
			return false;
		if (user.getPassword() == null || user.getPassword().equals(""))
			return false;
		if (user.getEmail() == null || user.getEmail().equals(""))
			return false;
		if (user.getRole() == null || user.getRole().equals(""))
			return false;
		return true;
	}

	/**
	 * Overrides Spring Security's UserDetailService interface method to return a
	 * user from the data repository with the provided username.
	 * 
	 * @param username The username of a user requesting authentication
	 * 
	 * @return UserDetails Provides core user information used by Spring Security
	 *         for authentication
	 * 
	 * @throws BadRequestException       if the username provided is invalid
	 * 
	 * @throws UsernameNotFoundException Throws this if there is no user found with
	 *                                   the given username.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username == null || username.equals("")) {
			throw new BadRequestException("Invalid username valud provided");
		}

		AppUser retrievedUser = repo.findUserByUsername(username);

		if (retrievedUser == null) {
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
