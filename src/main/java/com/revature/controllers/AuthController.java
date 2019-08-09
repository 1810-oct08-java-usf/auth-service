package com.revature.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.UserCreationException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.AppUser;
import com.revature.models.UserErrorResponse;
import com.revature.services.UserService;

/*
 * TODO 
 * 
 * 		1) Currently, users can edit any other user if using Postman or curl.
 * 		   There needs to be a way to ensure that a user record can only be 
 * 		   updated by a user a matching id, or an admin.
 * 
 * 		2) Should we implement method-level security, instead of using the 
 * 		   HttpSecurity object in the SecurityCredentialsConfig.class? Delete
 * 		   mapping for this controller already uses it.
 */

/**
 * This class contains all CRUD functionality for the users
 * 
 * @author Caleb
 *
 */
@RestController
@RequestMapping("/users")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * This method will get all users
	 * 
	 * @return All users
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<AppUser> getAllUsers() {
		return userService.findAllUsers();
	}

	/**
	 * This method will get the user with the specified id
	 * 
	 * @param id
	 * @return The user with specified id
	 * @throws UserNotFoundException
	 */
	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser getUserById(@PathVariable int id) {
		AppUser user = userService.findById(id);
		if (user == null)
			throw new UserNotFoundException("There is no user with that ID.");
		return user;
	}

	/**
	 * This method will get the user with the specified username
	 * 
	 * @param username
	 * @return The user with specified username
	 * @throws UserNotFoundException
	 */
	@GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser getUserByUsername(@PathVariable String username) {
		if (userService.findUserByUsername(username) == null)
			throw new UserNotFoundException("There is no user with that username.");
		return userService.findUserByUsername(username);
	}

	/**
	 * This method will return the user with the specified email
	 * 
	 * @param email
	 * @return The user with the specified email
	 * @throws UserNotFoundException
	 */
	@GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser getUserByEmail(@PathVariable String email) {
		if (userService.findUserByEmail(email) == null)
			throw new UserNotFoundException("There is no user with that email address.");
		return userService.findUserByEmail(email);
	}

	/**
	 * Used in checking if email is already in use
	 * 
	 * @param email
	 * @return String
	 */
	@GetMapping(value="/emailInUse/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String checkIfEmailIsInUse(@PathVariable String email) {
		if (userService.findUserByEmail(email) == null)
			return "{\"emailIsInUse\": false}";
		return "{\"emailIsInUse\": true}";
	}

	/**
	 * Used in checking if the username is available
	 * 
	 * @param username
	 * @return String
	 */
	@GetMapping(value="/usernameAvailable/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String checkIfUsernameIsAvailable(@PathVariable String username) {
		if (userService.findUserByUsername(username) == null)
			return "{\"usernameIsAvailable\": true}";
		return "{\"usernameIsAvailable\":false}";
	}

	/**
	 * This is the method for registering a new user
	 * 
	 * @param user
	 * @return The user who was just registered
	 * @throws UserCreationException
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public AppUser registerUser(@RequestBody AppUser user) {
		if (userService.addUser(user) == null)
			throw new UserCreationException("That username or email already exists.");
		return user;
	}

	/**
	 * This method will update a user with the newly provided information
	 * 
	 * @param frontEndUser This is the user information that is taken from the front
	 *                     end.
	 * @param auth
	 * @return frontEndUser This is the updated frontEndUser with information filled
	 *         in from the back
	 * @return null if any of the fields are blank
	 * @throws UserNotFoundException
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser updateUser(@Valid @RequestBody AppUser frontEndUser, Authentication auth) {
		// If the user we got from the front-end is null we don't do anything and we
		// just return null
		if (frontEndUser == null) {
			return null;
		}

		// Verifying the username is not null, this is just a precaution
		if (frontEndUser.getUsername() == null) {
			return null;
		}

		// We get the old user from the database, to assign the properties from the user
		// that
		// we don't want the user to change
		AppUser oldUser = userService.findById(frontEndUser.getId());
		//old password^
		frontEndUser.setRole(oldUser.getRole());
		
		if (!frontEndUser.getUsername().equals(oldUser.getUsername())) {
			return null;
		}
		

		//parse frontEndUser.getPassword() on the first space and then set it into a new variable
		String passwordToParse = frontEndUser.getPassword();
		String[] passwordArray = passwordToParse.split("\\s+");
		
		
		// Verifying the user provided its current password in order to make the changes

		if (!passwordArray[0].equals(oldUser.getPassword())) {
			throw new UserNotFoundException("The given password is incorrect");
		}

		// Setting the stored password to the updated user
		frontEndUser.setPassword(passwordArray[1]);

		// Updating the user, if the user is updated we return the new updated user to the front end
		if (userService.updateUser(frontEndUser)) {

			return frontEndUser;
			
		}

		return null;

	}

	/**
	 * Method takes in a user object and updates it to ROLE_ADMIN
	 * User must be ROLE_ADMIN
	 * (Needs unit test)
	 * 
	 * @param user
	 * @param auth
	 * @return AppUser
	 * 
	 * @author Austin Bark, Aaron Rea, Joshua Karvelis (190422-Java-USF)
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser updateToAdmin(@RequestBody AppUser user, Authentication auth) {
		System.out.println("in update to Admin");
		AppUser user1 = userService.findById(user.getId());
		System.out.println(user.getId());
		if(user1 == null) { throw new UserNotFoundException("user with id: " +user.getId() +", not found"); }
		user.setRole("ROLE_ADMIN");
		userService.updateUser(user);
		return user;
		
		
	
	}	

	/**
	 * This method will delete a user given that user's id. This method is only
	 * accessible to users with the ADMIN role
	 * 
	 * @param id
	 * @throws UserNotFoundException
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/id/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable int id) {
		AppUser user = userService.findById(id);
		if (user == null)
			throw new UserNotFoundException("User with id: " + id + " not found");
		if (!userService.deleteUserById(user.getId()))
			throw new UserNotFoundException("User with id: " + user.getId() + " does not exist.");
	}

	/**
	 * This handles any UserNotFoundException thrown in the AuthController.
	 * 
	 * @param unfe
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler({UserNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public UserErrorResponse handleUserNotFoundException(UserNotFoundException unfe) {
		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(unfe.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return error;
	}

	/**
	 * This handles any UserCreationException thrown in the AuthController.
	 * 
	 * @param uce
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public UserErrorResponse handleUserCreationException(UserCreationException uce) {
		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.CONFLICT.value());
		error.setMessage(uce.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return error;
	}

}