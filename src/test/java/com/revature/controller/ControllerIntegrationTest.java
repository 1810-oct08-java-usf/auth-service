package com.revature.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
	
	private String uri = "/users";
	
	@Autowired
	private MockMvc mvc;
	
	
	@Value("${security.zsign.header:RPM_ZUUL_ACCESS_HEADER}")
	private String zuulHeader;
	@Value("${security.zsign.salt}")
	private String salt;
	@Value("${security.zsign.secret}")
	private String secret;
	
	private String body;
	
	@MockBean
	private UserService userService;
	
	@InjectMocks
	private CustomAuthenticationFilter filter;
	
	AppUser mockUser;
	
	
	@Before
	public void setup() {
		//TODO 		
		body = filter.get_SHA_512_SecureHash(salt, secret);
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
	 * Tests delete User method
	 * @throws Exception
	 */
	@Test
	@WithMockUser(roles= {"ADMIN"})
	public void deleteUser() throws Exception{		
		
		this.mvc.perform(MockMvcRequestBuilders.delete(uri+"/id/0").contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"key\":[\"value\"]}").accept(MediaType.APPLICATION_JSON_VALUE).characterEncoding("utf-8").header(this.zuulHeader, body)).andExpect(status().isOk());
		
	}
	
	
	

}
