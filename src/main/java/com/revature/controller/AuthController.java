package com.revature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
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
import com.revature.service.UserService;

/*
 * TODO Currently, users can edit any other user from postman.
 * There needs to be a way to check the id of the user who is
 * attempting the update.
 */
@RestController
@RequestMapping("/users")
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class AuthController {

	private UserService userService;

	public AuthController() {
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<AppUser> getAllUsers() {
		return userService.findAllUsers();
	}

	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser getProjectById(@PathVariable int id) {
		return userService.findById(id);
	}
	
	@GetMapping(value="/username/{username}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AppUser getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }
	
	@GetMapping(value="/email/{email}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AppUser getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public AppUser registerUser(@RequestBody AppUser user) {
		if (userService.addUser(user) == null)
			throw new UserCreationException("That username or email already exists.");
		return user;
	}

	/*
	 * TODO This needs to be cleaned up. There is likely a more efficient way to
	 * check if passed in values are null.
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser updateUser(@RequestBody AppUser user, Authentication auth) {
		AppUser tempUser = userService.findUserByUsername(auth.getPrincipal().toString());
		if (!tempUser.getUsername().equals(user.getUsername()))
			throw new UserNotFoundException("Username cannot be changed.");
		if(user.getEmail() == null)
			user.setEmail(tempUser.getEmail());
		if(user.getFirstName() == null)
			user.setFirstName(tempUser.getFirstName());
		if(user.getLastName() == null)
			user.setLastName(tempUser.getLastName());
		if(user.getPassword() == null)
			user.setPassword(tempUser.getPassword());
		if (user.getId() == null)
			throw new UserNotFoundException("Id cannot be null!");
		if (userService.findById(user.getId()) == null)
			throw new UserNotFoundException("User with id: " + user.getId() + " not found");
		if (!userService.updateUser(user))
			throw new UserNotFoundException("User with id: " + user.getId() + "not found");
		return user;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value="/id/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable int id) {
		AppUser user = userService.findById(id);
		if (user.getId() == null)
			throw new UserNotFoundException("Id cannot be null!");
		if (userService.findById(user.getId()) == null)
			throw new UserNotFoundException("User with id: " + user.getId() + " not found");
		if (!userService.deleteUserById(user.getId()))
			throw new UserNotFoundException("User with id: " + user.getId() + " does not exist.");
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public UserErrorResponse handleUserNotFoundException(UserNotFoundException unfe) {
		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(unfe.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return error;
	}

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
