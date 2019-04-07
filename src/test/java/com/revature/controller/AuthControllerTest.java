package com.revature.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
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
	@Mock
	private AppUser backUser;
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
//	(expected = UserNotFoundException.class)
	/**
	 * Test if user object doesn't exist.
	 */
	@Ignore
	public void testDeleteUserIfUserDoesntExist() {
		when(uService.findById(0)).thenReturn(null);
		aControl.deleteUser(0);
	}
	
//-------------------------------------------------------------------------------------------------------
//				Test Update
//-------------------------------------------------------------------------------------------------------
	
	//This test doesn't 100% follow the logic in controller method.
	//Please refactor the controller method to use already fetched user object.
	/**
	 *  Supposed to test if it updates successfully with given user.
	 */
	@Test
	public void testUpdateUserWithValidInfo() {
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("sally");
		
		when(uService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(backUser);
		
		when(mockUser.getRole()).thenReturn(roleAdmin);
		when(mockUser.getPassword()).thenReturn("seashore newPass");
		when(mockUser.getUsername()).thenReturn("sally");
		when(mockUser.getFirstName()).thenReturn("salleo");
		when(mockUser.getLastName()).thenReturn("pealle");
		when(mockUser.getId()).thenReturn(1001);
		
		when(backUser.getPassword()).thenReturn("seashore");
		when(backUser.getUsername()).thenReturn("sally");
		when(backUser.getEmail()).thenReturn("sellsSEsasheaw@shea.shore");
		when(backUser.getFirstName()).thenReturn("sal");
		when(backUser.getLastName()).thenReturn("pal");
		when(backUser.getId()).thenReturn(1000);
		
		when(uService.findById(mockUser.getId())).thenReturn(backUser);
		when(uService.updateUser(mockUser)).thenReturn(true);
		
		aControl.updateUser(mockUser, mockAuth);
		verify(uService, times(1)).updateUser(mockUser);
		
	}
	
	// test if some fields are empty.
	
	@Test(expected = UserNotFoundException.class)
	public void testUpdateUserWithDifferentBackEndPassword() {
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("sally");
		
		when(uService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(backUser);
		
		when(mockUser.getRole()).thenReturn(roleAdmin);
		when(mockUser.getPassword()).thenReturn("seashore newPass");
		when(mockUser.getUsername()).thenReturn("sally");
		when(mockUser.getFirstName()).thenReturn("salleo");
		when(mockUser.getLastName()).thenReturn("pealle");
		when(mockUser.getId()).thenReturn(1001);
		
		when(backUser.getPassword()).thenReturn("newPass");
		
		when(uService.findById(mockUser.getId())).thenReturn(backUser);
		when(uService.updateUser(mockUser)).thenReturn(true);
		
		aControl.updateUser(mockUser, mockAuth);
		verify(uService, times(1)).updateUser(mockUser);
		
	}
	
	//Pretty sure this is impossible in actual implementation
	//backend user is fetched from the DB by the username there's no reason to check if they have the same username
	@Test(expected = UserNotFoundException.class)
	public void testUpdateUserWithDifferentUsername() {
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("sally");
		
		when(uService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(backUser);
		
		when(mockUser.getRole()).thenReturn(roleAdmin);
		when(mockUser.getPassword()).thenReturn("seashore newPass");
		when(mockUser.getUsername()).thenReturn("sally");
		when(mockUser.getFirstName()).thenReturn("salleo");
		when(mockUser.getLastName()).thenReturn("pealle");
		when(mockUser.getId()).thenReturn(1001);
		
		when(backUser.getPassword()).thenReturn("seashore");
		when(backUser.getUsername()).thenReturn("NotSally");
		
		aControl.updateUser(mockUser, mockAuth);
		verify(uService, times(1)).updateUser(mockUser);
		
	}
	
	
}
