/**
 * 
 */
package com.revature.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.AppUser;
import com.revature.models.UserPrincipal;
import com.revature.services.UserService;

/**
 * Testing that validation are applied when we update a user profile.
 * 
 * @author Testing team (ago 2019) - USF
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class AuthControllerValidationIntegrationTest {

	private String uri = "/users";

	@Autowired
	private MockMvc mockMvc; // Mock MVC to emulate the server is running
	
	@Mock
	private UserPrincipal mockPrincipal; // Mock user principal, need it to run controller test
	
	@Mock
	private Authentication mockAuth; // Mock authentication

	@MockBean
	private UserService userService; // Mock user service, this way we don't call the actual service
	
	@Autowired
	AuthController authController; // This is the controller that we are actually testing

	/**
	 * This test was created to make sure the annotations to perform validation on the AppUser class
	 * are actually working and returning  response status (400) Bad Request. 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(roles = { "USER" })
	public void updateUser_FirstNameIsNull_ShouldReturnBadRequestStatus() throws Exception {
		// Setting user to be authenticated
		mockAuth.setAuthenticated(true);

		// We need object mapper to marshalling object and passing it to the controller
		ObjectMapper mapper = new ObjectMapper();
		
		// Mock user to be tested
		AppUser mockedUser = new AppUser(1, null, null, "mocked@email.com", "mocked", "mocked", "ROLE_USER");

		// Calling the updateUser method in the Auth rest controller
		this.mockMvc
				.perform(
						put(uri).content(mapper.writeValueAsString(mockedUser)) // Writing values as JSON string
						.contentType(MediaType.APPLICATION_JSON) // Set JSON content type
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()); // expecting bad request

	}
	
	/**
	 * This test was created to make sure that the user is going to perform validation for the
	 * updateUser method, we provide valid fields for the AppUser, so we are expecting a
	 * successful result in this case (OK: 200).
	 * @throws Exception
	 */
	@Test
	@WithMockUser(roles = { "USER" })
	public void updateUser_ShouldReturnOKStatus() throws Exception {
		// setting user to be authenticated, this is needed to run the test
		mockAuth.setAuthenticated(true);
		
		String originalPass = "hello";
		String encryptedPassword = new BCryptPasswordEncoder().encode(originalPass);
		
		// We need object mapper to marshalling object and passing it to the controller
		ObjectMapper mapper = new ObjectMapper();
		
		// Mock user
		AppUser mockedUser = new AppUser(1, "User", "Mocked", "mocked@revature.com", "mocked", originalPass, "ROLE_USER");
		AppUser oldUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", encryptedPassword, "ROLE_USER");
		
		when(userService.findById(Mockito.anyInt())).thenReturn(oldUser);

		// Calling the updateUser method in the Auth rest controller
		this.mockMvc
				.perform(
						put(uri).content(mapper.writeValueAsString(mockedUser)) // Writing values as JSON string
						.contentType(MediaType.APPLICATION_JSON_UTF8) // Set JSON content type
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk()); // expecting ok response

	}

}
