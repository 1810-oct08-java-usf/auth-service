package com.revature.rpm.web.controllers;

import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.rpm.dtos.UserErrorResponse;
import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.services.UserService;

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
 * This controller contains all CRUD functionality for the users
 * and is mapped to handle all requests to /users.
 * 
 */
@RestController
@RequestMapping("/users")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthController {
	
	private UserService userService;

	@Autowired
	public AuthController(UserService service) {
		this.userService = service;
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
		return userService.findById(id);
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
		return userService.findUserByEmail(email);
	}

	/**
	 * Used for checking the availability of user fields where uniqueness is enforced.
	 * 
	 * @param field
	 * 		Specifies the field (i.e. username, email) being checked for availability
	 * 
	 * @param value
	 * 		The value being checked for uniqueness
	 * 
	 * @return true if available, false if not
	 */
	@GetMapping(value="/available", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public boolean isAvailable(@RequestParam String field, @RequestParam String value) {
		
		switch(field) {
		case "username":
			return userService.isUsernameAvailable(value);
		case "email":
			return userService.isEmailAddressAvailable(value);
		default:
			throw new BadRequestException("Invalid field value specified");
		}
		
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
		return userService.addUser(user);
	}

	/**
	 * This method will update a user with the newly provided information
	 * 
	 * @param frontEndUser This is the user information that is taken from the front
	 *                     end. The password is sent with two fields, the first being
	 *                     the users current password and the next being the password the
	 *                     user wants to have. There is now a parser in order separate the 
	 *                     passwords and put them into an array.
	 * @param auth
	 * @return frontEndUser This is the updated frontEndUser with information filled
	 *         in from the back
	 * @return null if any of the fields are blank
	 * @throws UserNotFoundException
	 * @author Tevin Thomas, Donald Henderson, Glory Umeasalugo, Tan Ho, Mohamad Hijazi (1905-Java-Nick)
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public boolean updateUser(@Valid @RequestBody AppUser updatedUser) {
		return userService.updateUser(updatedUser, false);
	}

	/**
	 * Method takes in a user object and updates it to ROLE_ADMIN
	 * or ROLE_USER based on the Admin's choice. 
	 * 
	 * User changing the roles must be ROLE_ADMIN
	 * 
	 * 
	 * @param user
	 * @param auth
	 * @return AppUser
	 * 
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public boolean updateUserRole(@RequestBody AppUser user) {
		return userService.updateUser(user, true);
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
	public boolean deleteUser(@PathVariable int id) {
		return userService.deleteUserById(id);
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
	
	/**
	 * This handles any UserCreationException thrown in the AuthController.
	 * 
	 * @param uce
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public UserErrorResponse handleBadRequestException(BadRequestException bre) {
		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(bre.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return error;
	}

}