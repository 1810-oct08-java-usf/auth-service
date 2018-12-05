package com.revature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
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
 * TODO Implement registration functionality/endpoints
 */
@RestController
@RequestMapping("/auth/users")
public class AuthServiceController {
	
	private UserService userService;
	
	public AuthServiceController() {}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/test")
	public String home() {
		return "HELLLLOOO!";
	}
	
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<AppUser> findAllUsers(){
		return userService.findAllUsers();
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public AppUser registerUser(@RequestBody AppUser user) {
		if(userService.addUser(user) == null) throw new UserCreationException("That username or email already exists.");
		return user;
	}
	
	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public AppUser updateUser(@RequestBody AppUser user) {
		if(user.getId() == null) throw new UserNotFoundException("Id cannot be null!");
		if(userService.findById(user.getId()) == null) throw new UserNotFoundException("User with id: " + user.getId() + " not found");
		if(!userService.saveUser(user)) throw new UserNotFoundException("User with id: " + user.getId() + "not found");
		return user;
	}
	
	@DeleteMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@RequestBody AppUser user) {
		if(user.getId() == null) throw new UserNotFoundException("Id cannot be null!");
		if(userService.findById(user.getId()) == null) throw new UserNotFoundException("User with id: " + user.getId() + " not found");
		if(!userService.deleteUserById(user.getId())) throw new UserNotFoundException("User with id: " + user.getId() + " does not exist.");
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public UserErrorResponse handleException(UserNotFoundException unfe) {
		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(unfe.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return error;
	}

}
