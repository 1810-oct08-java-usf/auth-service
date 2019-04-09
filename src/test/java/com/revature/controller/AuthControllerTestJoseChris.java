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
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfEmailIsInUsePassingValidEmail() {

		// Create a valid email to check for
		String email = "testuser@goodemail.com";

		// Expected assert value, result should be true
		String expected = "{\"emailIsInUse\": true}";

		when(userService.findUserByEmail(email)).thenReturn(mockAppUser);
		when(mockAppUser.getId()).thenReturn(0);
		assertEquals(expected, authController.checkIfEmailIsInUse(email));
	}

	/**
	 * This method will test if we can check if a email is in use. We will provide a
	 * invalid email, and we will use an assert to check if the controller will
	 * return the correct value.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfEmailIsInUsePassingInvalidEmail() {

		// Create a invalid email to check for
		String email = "testuser@bademail.com";

		// Expected assert value, result should be false
		String expected = "{\"emailIsInUse\": false}";

		when(userService.findUserByEmail(email)).thenReturn(null);
		when(mockAppUser.getId()).thenReturn(null);
		assertEquals(expected, authController.checkIfEmailIsInUse(email));
	}

	/**
	 * This method will test if we can check if a username is available. We will
	 * provide a username that is not available, and test if the controller will
	 * return us the proper result.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfUsernameIsAvailablePassingAnUnavailableUsername() {

		// Create a username to check for
		String username = "cshanor";

		// Expected assert value, Username should not be available
		String expected = "{\"usernameIsAvailable\":false}";

		when(userService.findUserByUsername(username)).thenReturn(mockAppUser);
		when(mockAppUser.getId()).thenReturn(0);
		assertEquals(expected, authController.checkIfUsernameIsAvailable(username));
	}

	/**
	 * This method will test if we can check if a username is available. We will
	 * provide a username that is available, and test if the controller will return
	 * us the proper result.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfUsernameIsAvailablePassingAnAvailableUsername() {

		// Create a username to check for
		String username = "cshanor";

		// Expected assert value, Username should be available
		String expected = "{\"usernameIsAvailable\": true}";

		when(userService.findUserByUsername(username)).thenReturn(null);
		when(mockAppUser.getId()).thenReturn(null);
		assertEquals(expected, authController.checkIfUsernameIsAvailable(username));
	}
}
