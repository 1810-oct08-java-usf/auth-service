package com.revature.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.controller.AuthController;
import com.revature.models.AppUser;
import com.revature.service.UserService;

/**
 * Class containing MockMVC tests for the email and username validation methods.
 * 
 * @author Christopher Shanor (190107-Java-Spark-USF)
 * @author Jose Rivera (190107-Java-Spark-USF)
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class EmailUsernameTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private AppUser mockAppUser;

	@MockBean
	private UserService userService;

	/**
	 * This method is going to test if our context loads and is not null.
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testContextLoads() throws Exception {
		assertThat(this.mockMvc).isNotNull();
	}

	/**
	 * This method will test the checkIfEmailIsInUse(). We provide the endpoint, a
	 * dummy email to use, and the expected result from the controller. We mock a
	 * user, and when the user service is called, we tell it to return to us the
	 * mocked user so our controller can provide the appropriate response.
	 * 
	 * @throws Exception: If an exception is thrown, the test is considered a
	 *                    failure.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetCheckIfEmailInUseWithEmailInUse() throws Exception {

		// The uri endpoint for the controller
		String uri = "/users/emailInUse/dummy@dummyemail.com";

		// A dummy email to check for
		String email = "dummy@dummyemail.com";

		// The expected result from the controller
		String expectedResult = "{\"emailIsInUse\": true}";

		/*
		 * When our userService.findUserByEmail() is invoked, we tell it to return a
		 * mock user so our get request can return a proper result as if it was not
		 * mocked.
		 */
		when(userService.findUserByEmail(email)).thenReturn(mockAppUser);

		/*
		 * Test our get mapping for checkIfEmailIsInUse() and check if the status is OK
		 * ( 200 ) and the expected string result is returned.
		 */
		this.mockMvc.perform(get(uri)).andExpect(status().isOk()).andExpect(content().string(expectedResult));
	}

	/**
	 * This method will test the checkIfEmailIsInUse(). We provide the endpoint, a
	 * dummy email to use, and the expected result from the controller. When the
	 * user service is called, we tell it to return to us a null, showing that a
	 * user was not found by the dummy email. Our Controller should provide the
	 * appropriate response.
	 * 
	 * @throws Exception: If an exception is thrown, the test is considered a
	 *                    failure.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetCheckIfEmailInUseWithEmailNotInUse() throws Exception {

		// The uri endpoint for the controller
		String uri = "/users/emailInUse/dummy@dummyemail.com";

		// A dummy email to check for
		String email = "dummy@dummyemail.com";

		// The expected result from the controller
		String expectedResult = "{\"emailIsInUse\": false}";

		/*
		 * When our userService.findUserByEmail() is invoked, we tell it to return null
		 * so our get request can return a proper result as if it was not mocked.
		 */
		when(userService.findUserByEmail(email)).thenReturn(null);

		/*
		 * Test our get mapping for checkIfEmailIsInUse() and check if the status is OK
		 * ( 200 ) and the expected string result is returned.
		 */
		this.mockMvc.perform(get(uri)).andExpect(status().isOk()).andExpect(content().string(expectedResult));
	}

	/**
	 * This method will test the checkIfEmailIsInUse(). We provide the endpoint,
	 * with no email in the path variable. When the controller method is invoked,
	 * the method should respond with a 404 status.
	 * 
	 * @throws Exception: If an exception is thrown, the test is considered a
	 *                    failure.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetCheckIfEmailInUseWithNoEmailProvided() throws Exception {

		// The uri endpoint with no email in the path for the controller
		String uri = "/users/emailInUse/";

		/*
		 * Test our get mapping for checkIfEmailIsInUse() and check if the status is set
		 * to is not found ( 404 ).
		 */
		this.mockMvc.perform(get(uri)).andExpect(status().isNotFound());
	}

	/**
	 * This method will test the get mapping for checkIfUsernameIsAvailable(). We
	 * will provide an endpoint, a dummy username to check the expected result from
	 * the controller. When the user service is called, we tell it to return to us a
	 * mock user, showing that a user was found by the dummy username. Our
	 * Controller should provide the appropriate response.
	 * 
	 * @throws Exception: If an exception is thrown, the test is considered a
	 *                    failure.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetCheckIfUsernameIsAvailableWithTakenUsername() throws Exception {

		// The endpoint for the controller
		String uri = "/users/usernameAvailable/jrivera";

		// A dummy username
		String username = "jrivera";

		// The expected string from the controller
		String expectedResult = "{\"usernameIsAvailable\":false}";

		/*
		 * When our userService.findUserByUsername() is invoked, we tell it to return a
		 * mocked user so our get request can return a proper result as if it was not
		 * mocked.
		 */
		when(userService.findUserByUsername(username)).thenReturn(mockAppUser);

		/*
		 * Test our get mapping for findUserByUsername() and check if the status is set
		 * to OK ( 200 ) and checking for the proper response from the controller.
		 */
		this.mockMvc.perform(get(uri)).andExpect(status().isOk()).andExpect(content().string(expectedResult));
	}

	/**
	 * This method will test the get mapping for checkIfUsernameIsAvailable(). We
	 * will provide an endpoint, a dummy username to check the expected result from
	 * the controller. When the user service is called, we tell it to return to us a
	 * null user, showing that a user was not found by the dummy username. Our
	 * Controller should provide the appropriate response.
	 * 
	 * @throws Exception: If an exception is thrown, the test is considered a
	 *                    failure.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetCheckIfUsernameIsAvailableWithAvailableUsername() throws Exception {

		// The endpoint for the controller
		String uri = "/users/usernameAvailable/jrivera";

		// A dummy username
		String username = "jrivera";

		// The expected string from the controller
		String expectedResult = "{\"usernameIsAvailable\": true}";

		/*
		 * When our userService.findUserByUsername() is invoked, we tell it to return a
		 * null user so our get request can return a proper result as if it was not
		 * mocked.
		 */
		when(userService.findUserByUsername(username)).thenReturn(null);

		/*
		 * Test our get mapping for findUserByUsername() and check if the status is set
		 * to OK ( 200 ) and checking for the proper response from the controller.
		 */
		this.mockMvc.perform(get(uri)).andExpect(status().isOk()).andExpect(content().string(expectedResult));
	}

	/**
	 * This method will test the checkIfUsernameIsAvailable(). We provide the
	 * endpoint, with no username in the path variable. When the controller method
	 * is invoked, the method should respond with a 404 status.
	 * 
	 * @throws Exception: If an exception is thrown, the test is considered a
	 *                    failure.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetCheckIfUsernameIsAvailableNoUsernameProvided() throws Exception {

		// The uri endpoint with no email in the path for the controller
		String uri = "/users/usernameAvailable/";

		/*
		 * Test our get mapping for checkIfEmailIsInUse() and check if the status is set
		 * to is not found ( 404 ).
		 */
		this.mockMvc.perform(get(uri)).andExpect(status().isNotFound());
	}
}
