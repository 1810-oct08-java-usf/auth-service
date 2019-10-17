package com.revature.rpm.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.exceptions.UserUpdateException;
import com.revature.rpm.services.UserService;

/*
 * TODO 
 * 
 * 		1) Currently, users can edit any other user if using Postman or curl.
 * 		   There needs to be a way to ensure that a user record can only be 
 * 		   updated by a user a matching id, or an admin.

 */

/**
 * This controller contains all CRUD functionality for the users and is mapped
 * to handle all requests to /users.
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
	 * Serves as a front-facing endpoint for fetching all users from the data
	 * source. Requesters to this endpoint must possess a role of ADMIN.
	 * 
	 * @return all users from the data source
	 */
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AppUser> getAllUsers() {
		return userService.findAllUsers();
	}

	/**
	 * Serves as a front-facing endpoint for fetching a user with the specified id.
	 * Requesters to this endpoint must possess a role of ADMIN.
	 * 
	 * @param id
	 * 
	 * @return The user with specified id, otherwise an exception will be thrown
	 *         from the service layer and handled using this controller's exception
	 *         handler.
	 */
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AppUser getUserById(@PathVariable int id) {
		return userService.findUserById(id);
	}

	/**
	 * Serves as a front-facing endpoint for fetching a user with the specified
	 * username. Requesters to this endpoint must possess a role of ADMIN.
	 * 
	 * @param username
	 * 
	 * @return The user with specified username, otherwise an exception will be
	 *         thrown from the service layer and handled using this controller's
	 *         exception handler.
	 */
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AppUser getUserByUsername(@PathVariable String username) {
		return userService.findUserByUsername(username);
	}

	/**
	 * Serves as a front-facing endpoint for fetching a user with the specified
	 * email. Requesters to this endpoint must possess a role of ADMIN.
	 * 
	 * @param email
	 * 
	 * @return The user with the specified email, otherwise an exception will be
	 *         thrown from the service layer and handled using this controller's
	 *         exception handler.
	 */
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AppUser getUserByEmail(@PathVariable String email) {
		return userService.findUserByEmail(email);
	}

	/**
	 * Serves as a front-facing endpoint for checking the availability of user
	 * fields where uniqueness is enforced.
	 * 
	 * @param field Specifies the field (i.e. username, email) being checked for
	 *              availability
	 * 
	 * @param value The value being checked for uniqueness
	 * 
	 * @return True if available, false if not
	 * 
	 * @throws BadRequestException
	 */
	@GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public boolean isAvailable(@RequestParam String field, @RequestParam String value) {

		switch (field) {
		case "username":
			return userService.isUsernameAvailable(value);
		case "email":
			return userService.isEmailAddressAvailable(value);
		default:
			throw new BadRequestException("Invalid field value specified");
		}

	}

	/**
	 * Serves as a front-facing endpoint for registering a new user.
	 * 
	 * @param user
	 * 
	 * @return The user who was just registered, otherwise an exception will be
	 *         thrown from the service layer and handled using this controller's
	 *         exception handler.
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AppUser registerUser(@RequestBody AppUser user) {
		return userService.addUser(user);
	}

	/**
	 * This method will update a user with the newly provided information
	 * 
	 * @param updatedUser
	 * 
	 * @return True if the user was successfully updated, otherwise an exception
	 *         will be thrown from the service layer and handled using this
	 *         controller's exception handler.
	 * 
	 */
	@ResponseStatus(HttpStatus.OK)
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean updateUser(@Valid @RequestBody AppUser updatedUser, HttpServletRequest req) {
		UserPrincipal principal = (UserPrincipal) req.getAttribute("principal");
		return userService.updateUser(updatedUser, principal);
	}

	/**
	 * This method will delete a user given that user's id. This method is only
	 * accessible to users with the ADMIN role
	 * 
	 * @param id
	 * 
	 * @return True if the user is successfully deleted, otherwise an exception will
	 *         be thrown from the service layer and handled using this controller's
	 *         exception handler.
	 */
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/id/{id}")
	public boolean deleteUser(@PathVariable int id) {
		return userService.deleteUserById(id);
	}

	/**
	 * This handles any UserNotFoundException thrown in the AuthController.
	 * 
	 * @param unfe
	 * 
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ UserNotFoundException.class })
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
	 * This handles any BadRequestException thrown in the AuthController.
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

	/**
	 * This handles any UserUpdateException thrown in the AuthController.
	 * 
	 * @param uce
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public UserErrorResponse handleUserUpdateException(UserUpdateException uue) {
		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.CONFLICT.value());
		error.setMessage(uue.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return error;
	}

}