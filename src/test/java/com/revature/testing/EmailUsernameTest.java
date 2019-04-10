package com.revature.testing;



import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Filter;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.controller.AuthController;
import com.revature.service.UserService;

/**
 * Class containing MockMVC tests for the email and username validation methods.
 * 
 * @author Christopher Shanor [190107-Java-Spark-USF]
 * @author Jose Rivera [190107-Java-Spark-USF]
 *
 */

@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(AuthController.class)
public class EmailUsernameTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private Filter filter;  // maybe don't need
	
	@MockBean
	private UserService userService;   // maybe don't need

	/**
	 * This method is going to test that our context loads and is not null
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * @author Christopher Shanor [190107-Java-Spark-USF]
	 * @author Jose Rivera [190107-Java-Spark-USF]
	 */
	@Test
	public void testContextLoads() throws Exception {
		assertThat(this.mockMvc).isNotNull();
	}
	
	/**
	 * This method will test the get mapping for checkIfEmailIsInUse() in the auth controller
	 * 
	 * @throws Exception: Test fails if an exception is thrown
	 * 
	 * @author Christopher Shanor [190107-Java-Spark-USF]
	 * @author Jose Rivera [190107-Java-Spark-USF]
	 */
	@Test
	public void testGetCheckIfEmailInUse() throws Exception {
		String uri = "/users/emailInUse/dummy@dummyemail.com";
		this.mockMvc.perform(get(uri)).andExpect(status().isOk());
	}
	
	/**
	 * This method will test the get mapping for checkIfEmailIsInUse() in the auth controller
	 * with no email address.
	 * 
	 * @throws Exception: Test fails if an exception is thrown
	 * 
	 * @author Christopher Shanor [190107-Java-Spark-USF]
	 * @author Jose Rivera [190107-Java-Spark-USF]
	 */
	@Test
	public void testGetCheckIfEmailInUseWithNoEmail() throws Exception {
		String uri = "/users/emailInUse/";
		this.mockMvc.perform(get(uri)).andExpect(status().isNotFound());
	}
	
	/**
	 * This method will test the get mapping for checkIfUsernameIsAvailable() in the auth controller
	 * with a username.
	 * 
	 * @throws Exception: Test fails if an exception is thrown
	 * 
	 * @author Christopher Shanor [190107-Java-Spark-USF]
	 * @author Jose Rivera [190107-Java-Spark-USF]
	 */
	@Test
	public void testGetCheckIfUsernameIsAvailable() throws Exception {
		String uri = "/users/usernameAvailable/jrivera";
		this.mockMvc.perform(get(uri)).andExpect(status().isOk());
	}
	
	/**
	 * This method will test the get mapping for checkIfUsernameIsAvailable() in the auth controller
	 * with no username.
	 * 
	 * @throws Exception: Test fails if an exception is thrown
	 * 
	 * @author Christopher Shanor [190107-Java-Spark-USF]
	 * @author Jose Rivera [190107-Java-Spark-USF]
	 */
	@Test
	public void testGetCheckIfUsernameIsAvailableWithNoUsername() throws Exception {
		String uri = "/users/usernameAvailable/";
		this.mockMvc.perform(get(uri)).andExpect(status().isNotFound());
	}
}
