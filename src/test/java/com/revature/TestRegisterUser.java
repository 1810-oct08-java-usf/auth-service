package com.revature;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.controller.AuthController;
import com.revature.exceptions.UserCreationException;
import com.revature.models.AppUser;
import com.revature.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class TestRegisterUser {
	
	@Mock
	UserService mockUserService;
	
	@Mock
	AppUser mockAppUser;
	
	@InjectMocks
	AuthController authController;

	/**
	 * testNullUser
	 * 
	 * The purpose of this method is to ensure that the proper exception
	 * (UserCreationException) is being thrown when the User Service
	 * finds that the email or username that is trying to be registered
	 * is already in use
	 * 
	 * @Author: Marco Van Rhyn 4/8/2019
	 */
	@Test(expected = UserCreationException.class)
	public void testNullUser() {
		when(mockUserService.addUser(mockAppUser)).thenReturn(null);
		authController.registerUser(mockAppUser);
	}
	
	/**
	 * testRegisterUser
	 * 
	 * The purpose of this method is to check that when we register
	 * a user with a unique username and email, we are returned that
	 * user information.
	 * 
	 * @Author: Marco Van Rhyn 4/8/2019
	 */
	@Test
	public void testRegisterUser() {
		when(mockUserService.addUser(mockAppUser)).thenReturn(mockAppUser);
		assertEquals(authController.registerUser(mockAppUser), mockAppUser);
	}

}
