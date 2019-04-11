package com.revature.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import com.revature.exceptions.UserNotFoundException;
import com.revature.models.AppUser;
import com.revature.service.UserService;

/**
 * 
 * @author Ankit Patel (190107-java-spark-usf)
 * @author Jaitee Pitts (190107-java-spark-usf)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

	@Mock
	private UserService userService;
	@Mock
	private AppUser mockUser;
	@Mock
	private AppUser backUser;
	@Mock
	private Authentication mockAuth;
		
	@InjectMocks
	AuthController authControl;
	
	
	private String roleAdmin = "role_admin";
	
	/**
	 * makes a mock auth controller for each test methods.
	 * @author Ankit Patel
	 * @author Jaitee Pitts
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);		
	}
		
	
	/**
	 * Tests Delete user functionality when userID is null.
	 * @author Ankit Patel
	 * @author Jaitee Pitts
	 * @throws Exception
	 */
	@Test(expected = UserNotFoundException.class)  
	public void testDeleteWIthNullId(){
		when(userService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(null);
		authControl.deleteUser(0);
		
	}
	
	/**
	 *
	 * Test Delete user with good values.
	 * @author Ankit Patel
	 * @author Jaitee Pitts
	 * @throws Exception 
	 */
	@Test
	public void testDeleteUserIfUserExist() throws Exception {
		when(userService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(0);
		when(userService.deleteUserById(0)).thenReturn(true);
		authControl.deleteUser(0);
		verify(userService, times(1)).deleteUserById(0);
	}
		

	/**
	 * Test if user object doesn't exist.
	 * Fails with a Null Pointer Exception.
	 * @author Ankit Patel
	 * @author Jaitee Pitts
	 * @throws UserNotFoundException
	 */

	@Test(expected = UserNotFoundException.class)
	public void testDeleteUserIfUserDoesntExist() {
		when(userService.findById(0)).thenReturn(null);
		authControl.deleteUser(0);
	}
	
//-------------------------------------------------------------------------------------------------------
//				Test Update
//-------------------------------------------------------------------------------------------------------
	
	//
	/**
	 *  Tests if it updates successfully with given user.
	 *  Please refactor the controller method to use already fetched user object.
	 *  @author Ankit Patel
	 * 	@author Jaitee Pitts
	 */
	@Test
	public void testUpdateUserWithValidInfo() {
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("sally");
		
		when(userService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(backUser);
		
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
		
		when(userService.findById(mockUser.getId())).thenReturn(backUser);
		when(userService.updateUser(mockUser)).thenReturn(true);
		
		authControl.updateUser(mockUser, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		
	}
	
	/**
	 * Tests update user 
	 * where backend password is different from given password from front end.
	 * @author Jaitee Pitts
	 * @author Ankit Patel
	 */
	
	@Test(expected = UserNotFoundException.class)
	public void testUpdateUserWithDifferentBackEndPassword() {
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("sally");
		
		when(userService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(backUser);
		
		when(mockUser.getRole()).thenReturn(roleAdmin);
		when(mockUser.getPassword()).thenReturn("seashore newPass");
		when(mockUser.getUsername()).thenReturn("sally");
		when(mockUser.getFirstName()).thenReturn("salleo");
		when(mockUser.getLastName()).thenReturn("pealle");
		when(mockUser.getId()).thenReturn(1001);
		
		when(backUser.getPassword()).thenReturn("newPass");
		
		when(userService.findById(mockUser.getId())).thenReturn(backUser);
		when(userService.updateUser(mockUser)).thenReturn(true);
		
		authControl.updateUser(mockUser, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		
	}
	

	/**
	 * Test if backend user has a different username then front end user.
	 * Pretty sure this is impossible in actual implementation
	 * backend user is fetched from the DB by the front end username 
	 * there's no reason to check if they have the same username.
	 * @author Jaitee Pitts
	 * @author Ankit Patel
	 */
	@Test(expected = UserNotFoundException.class)
	public void testUpdateUserWithDifferentUsername() {
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("sally");
		
		when(userService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(backUser);
		
		when(mockUser.getRole()).thenReturn(roleAdmin);
		when(mockUser.getPassword()).thenReturn("seashore newPass");
		when(mockUser.getUsername()).thenReturn("sally");
		when(mockUser.getFirstName()).thenReturn("salleo");
		when(mockUser.getLastName()).thenReturn("pealle");
		when(mockUser.getId()).thenReturn(1001);
		
		when(backUser.getPassword()).thenReturn("seashore");
		when(backUser.getUsername()).thenReturn("NotSally");
		
		authControl.updateUser(mockUser, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		
	}
	
	
}
