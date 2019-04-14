package com.revature.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.revature.models.AppUser;
import com.revature.security.CustomAuthenticationFilter;
import com.revature.service.UserService;
import com.sun.jersey.api.client.ClientResponse.Status;

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
	private WebApplicationContext context;
	
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
	
	/**
	 * Sets up the MVC configuration with security context
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Before
	public void setup() {
		//TODO 		
		body = filter.get_SHA_512_SecureHash(salt, secret);
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
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Test
	@WithMockUser(roles= {"USER"})
	public void SubVersionAttemptTest() throws Exception{
		this.mvc.perform(delete("/id/0")).andExpect(status().isUnauthorized());
	}
	
	/**
	 * Tests delete User method
	 * @throws Exception
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Test
	@WithMockUser(roles= {"USER","ADMIN"})
	public void deleteUser() throws Exception{		
		
		this.mvc.perform(delete(uri+"/id/0").with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).characterEncoding("utf-8")
				.header(this.zuulHeader, body)).andExpect(status().isOk());
		
	}
	
}
