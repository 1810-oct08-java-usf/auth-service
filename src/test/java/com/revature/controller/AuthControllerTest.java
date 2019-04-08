package com.revature.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
	private AppUser backUser;
	@Mock
	private Authentication mockAuth;
		
	@InjectMocks
	AuthController aControl;
	
	
	private String roleAdmin = "role_admin";
	
	/**
	 * makes a mock auth controller for each test methods.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);		
	}
		
	
	/**
	 * Tests Delete user functionality when userID is null
	 * @throws Exception
	 */
	@Test(expected = UserNotFoundException.class)  
	public void testDeleteWIthNullId(){
		when(uService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(null);
		aControl.deleteUser(0);
		
	}
	
	/**
	 *
	 * Test Delete user with good values
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
		

	/**
	 * Test if user object doesn't exist.
	 * @throws UserNotFoundException
	 * <p>fails with a Null Pointer Exception</p>
	 */

	@Test(expected = UserNotFoundException.class)
	public void testDeleteUserIfUserDoesntExist() {
		when(uService.findById(0)).thenReturn(null);
		aControl.deleteUser(0);
	}
	
//-------------------------------------------------------------------------------------------------------
//				Test Update
//-------------------------------------------------------------------------------------------------------
	
	//
	/**
	 *  Tests if it updates successfully with given user.
	 *  <p>Please refactor the controller method to use already fetched user object.</p>
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
	
	/**
	 * Tests update user 
	 * where backend password is different from given password from front end 
	 */
	
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
	

	/**
	 * Test if backend user has a different username then front end user.
	 * <p>Pretty sure this is impossible in actual implementation
	 * backend user is fetched from the DB by the front end username 
	 * there's no reason to check if they have the same username</p>
	 */
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
