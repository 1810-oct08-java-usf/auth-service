package com.revature.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revature.models.AppUser;
import com.revature.service.UserService;

/**
 * This class will be used to test methods in the AuthController. Testing
 * methods will be using Junit to test different scenarios.
 * 
 * @RunWith(): Specifies that we will be using the MockitoJUnit runner class.
 * 
 * @author Jose Rivera
 * @author Christopher Shanor
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
	 * This method will test if we can check if a valid email is in use. We will
	 * provide a valid email, and the service should attempt to find a user by that
	 * email and respond with true.
	 * 
	 * @throws Exception: Exception will be thrown if the test was a failure.
	 */
	@Test
	public void testCheckIfEmailIsInUsePassingValidEmail() throws Exception {

		// Create a valid email to check for
		String email = "testuser@dummyemail.com";

		when(userService.findUserByEmail(email)).thenReturn(mockAppUser);
		when(mockAppUser.getId()).thenReturn(0);
		authController.checkIfEmailIsInUse(email);
	}
}
