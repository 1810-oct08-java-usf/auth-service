package com.revature.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.models.AppUser;
import com.revature.service.UserService;

/**
 * This class will be used to test methods in the AuthController. Testing will
 * be done using Junit to test different scenarios.
 * 
 * @RunWith(): Specifies that we will be using the MockitoJUnit runner class.
 * 
 * @author Jose Rivera (190107-Java-Spark-USF)
 * @author Christopher Shanor (190107-Java-Spark-USF)
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AuthControllerTestJoseChris {

	@Mock
	private UserService userService;

	@Mock
	private AppUser mockAppUser;

	@InjectMocks
	private AuthController authController;

	/**
	 * This method will test if we can check if a email is in use. We will provide a
	 * valid email, and we will us an assert to check if the controller will return
	 * the correct value.
	 */
	@Test
	public void testCheckIfEmailIsInUsePassingValidEmail() {

		// Create a valid email to check for
		String email = "testuser@goodemail.com";

		// Expected assert value
		String expected = "{\"emailIsInUse\": true}";

		when(userService.findUserByEmail(email)).thenReturn(mockAppUser);
		when(mockAppUser.getId()).thenReturn(0);
		assertEquals(expected, authController.checkIfEmailIsInUse(email));
	}

	/**
	 * This method will test if we can check if a email is in use. We will provide a
	 * invalid email, and the service should attempt to find a user by that email
	 * and respond with null.
	 * 
	 * @throws Exception: Exception will be thrown if the test was a failure.
	 */
//	@Test
//	public void testCheckIfEmailIsInUsePassingInvalidEmail() throws Exception {
//
//		// Create a valid email to check for
//		String email = "testuser@bademail.com";
//
//		when(userService.findUserByEmail(email)).thenReturn(null);
//		when(mockAppUser.getId()).thenReturn(null);
//		authController.checkIfEmailIsInUse(email);
//		assertEquals("{\"emailIsInUse\": true}", authController.checkIfEmailIsInUse(email));
//	}
}
