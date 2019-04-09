package com.revature.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.RpmAuthServiceApplication;
import com.revature.security.ZuulConfig;
import com.revature.service.UserService;

/**
 * This method tests integration of the controller with MockMVC
 * 
 * @author Jaitee Pitts
 * @author Ankit Patel
 *
 */
@RunWith(SpringRunner.class)
//@WebMvcTest(AuthController.class)
@WebMvcTest(RpmAuthServiceApplication.class)
public class ControllerIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	
	@MockBean
	private UserService userService;
	
//	@MockBean
//	private ZuulConfig zuulConfig;
	
	@Before
	public void setup() {
		//TODO 
				
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
	 */
	@Test
	@WithMockUser(roles="ADMIN")
	public void SubVersionAttemptTest() throws Exception{
		this.mvc.perform(delete("/id/0")).andExpect(status().isUnauthorized());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
//	@Test
//	@WithMockUser(roles="ADMIN")
//	public void deleteUser() throws Exception{
//		this.zuulConfig.getHeader();
//		this.mvc.perform(delete("/id/1").header(zuulConfig.getHeader(), zuulConfig.getSalt()+zuulConfig.getSecret()))
//		
//		.andExpect(status().isOk());
//	}
	
	
	

}
