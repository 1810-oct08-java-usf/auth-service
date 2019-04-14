package com.revature.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.revature.models.AppUser;
import com.revature.security.CustomAuthenticationFilter;
import com.revature.service.UserService;


/**
 * This method tests integration of the controller with MockMVC
 * 
 * @author Jaitee Pitts
 * @author Ankit Patel
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)

public class ControllerIntegrationTest {
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mvc;
	
	
	@MockBean
	private UserService userService;
	
	@InjectMocks
	private CustomAuthenticationFilter filter;
	
	AppUser mockUser;
	
	/**
	 * Sets up the MVC configuration with security context
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Before
	public void setup() {
		mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) 
                .build();

	}
	
	/**
	 * Tests that delete user cannot be called without a user role
	 * @throws Exception
	 */
	@Test
	public void deleteUserUnauthorized() throws Exception{
		this.mvc.perform(delete("/id/0")).andExpect(status().isUnauthorized());
		
	}
	/**
	 * Tests that delete user cannot be called without a Zuul header
	 * @throws Exception
	 * 
	 */
	@Test
	@WithMockUser(roles= {"USER"})
	public void SubVersionAttemptTest() throws Exception{
		this.mvc.perform(delete("/id/0")).andExpect(status().isUnauthorized());
	}
	

}
