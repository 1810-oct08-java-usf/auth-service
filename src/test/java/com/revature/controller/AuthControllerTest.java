package com.revature.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Filter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revature.exceptions.UserNotFoundException;
import com.revature.models.AppUser;
import com.revature.service.UserService;

/**
 * 
 * @author Ankit Patel and Jaitee Pitts
 *
 */
public class AuthControllerTest {

	@Mock
	private UserService uService;
	@Mock
	private AppUser mockUser;
		
	@InjectMocks
	AuthController aControl;
	
	private MockMvc mmc;
	private Filter springSecurityFilterChain;

	private String mUri = "/users";
	private String dUri = "/id/"; 
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mmc = MockMvcBuilders.standaloneSetup(aControl).build();
	}
	
	
	/**
	 * Test if MockMvc container is not null.
	 * @throws Exception
	 */
	@Test
	public void contextLoads() throws Exception {
	       assertThat(this.mmc).isNotNull();
	}	
	
	/**
	 * Tests Delete user functionality
	 * @throws Exception
	 */
	@Test(expected = UserNotFoundException.class)  
	public void testDeleteNull(){
		when(uService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(null);
		aControl.deleteUser(0);
		
	}
	
	/**
	 * Test if user object doesn't exist.
	 * @throws Exception 
	 */
	@Test
	public void deleteUserIfUserExist() throws Exception {
		when(uService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(0);
		when(uService.deleteUserById(0)).thenReturn(true);
		aControl.deleteUser(0);
		verify(uService, times(1)).deleteUserById(0);
	}
	
	/**
	 * Test if user object doesn't exist.
	 */
	@Test
	public void deleteUserIfUserDoesntExist() {
		when(uService.findById(0)).thenReturn(null);
	
	}
	
}
