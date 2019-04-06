package com.revature.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.core.Authentication;

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
	@Mock
	private Authentication mockAuth;
		
	@InjectMocks
	AuthController aControl;
	
	private MockMvc mmc;
	
	private String roleAdmin = "role_admin";
	
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
	public void testContextLoads() throws Exception {
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
	public void testDeleteUserIfUserExist() throws Exception {
		when(uService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(0);
		when(uService.deleteUserById(0)).thenReturn(true);
		aControl.deleteUser(0);
		verify(uService, times(1)).deleteUserById(0);
	}
	
	// fails with null pointer exception.
	/**
	 * Test if user object doesn't exist.
	 */
	@Test(expected = UserNotFoundException.class)
	public void testDeleteUserIfUserDoesntExist() {
		when(uService.findById(0)).thenReturn(null);
		aControl.deleteUser(0);
	}
	
	/**
	 * 
	 * Test Update
	 * 
	 */
	
	//This test doesn't 100% follow the logic in controller method.
	//two user objects one from ui with uname to get one from db
	/**
	 *  Supposed to test if it updates successfully with given user.
	 */
	@Test
	public void testUpdateUserWithValidInfo() {
		mockUser.setUsername("sally");
		mockUser.setFirstName("sal");
		mockUser.setLastName("pal");
		mockUser.setEmail("sallysellSeashell@sea.shore");
		mockUser.setPassword("seasone");
		mockUser.setRole(roleAdmin);
		mockAuth.setAuthenticated(true);
		when(uService.findUserByUsername("sally")).thenReturn(mockUser);
		when(mockUser.getRole()).thenReturn(roleAdmin);
		aControl.updateUser(mockUser, mockAuth);
	}
}
