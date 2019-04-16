package com.revature.tests;

import static org.junit.Assert.assertEquals;
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

import com.revature.controller.AuthController;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.AppUser;
import com.revature.service.UserService;
/**
 * This class will be used to test methods in the AuthController. Testing will
 * be done using Junit to test different scenarios.
 * 
 * @RunWith(): Specifies that we will be using the MockitoJUnit runner class.
 * 
 * @author Ankit Patel (190107-java-spark-usf)
 * @author Jaitee Pitts (190107-java-spark-usf)
 * @author Jose Rivera (190107-Java-Spark-USF)
 * @author Christopher Shanor (190107-Java-Spark-USF)
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

	@Test (expected = UserNotFoundException.class)
	public void testDeleteUserIfUserDoesntExist() {
		when(userService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(null);
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
		
		when(backUser.getPassword()).thenReturn("newPass");
		
		
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
		
		when(backUser.getPassword()).thenReturn("seashore");
		when(backUser.getUsername()).thenReturn("NotSally");
		
		authControl.updateUser(mockUser, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		
	}

	@Mock
	private AppUser mockAppUser;

	@InjectMocks
	private AuthController authController;

	/**
	 * This method will test if we can check if a email is in use. We will provide a
	 * valid email, and we will us an assert to check if the controller will return
	 * the correct value.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfEmailIsInUsePassingValidEmail() {

		// Create a valid email to check for
		String email = "testuser@goodemail.com";

		// Expected assert value, result should be true
		String expected = "{\"emailIsInUse\": true}";

		when(userService.findUserByEmail(email)).thenReturn(mockAppUser);
		assertEquals(expected, authController.checkIfEmailIsInUse(email));
	}

	/**
	 * This method will test if we can check if a email is in use. We will provide a
	 * invalid email, and we will use an assert to check if the controller will
	 * return the correct value.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfEmailIsInUsePassingInvalidEmail() {

		// Create a invalid email to check for
		String email = "testuser@bademail.com";

		// Expected assert value, result should be false
		String expected = "{\"emailIsInUse\": false}";

		when(userService.findUserByEmail(email)).thenReturn(null);
		assertEquals(expected, authController.checkIfEmailIsInUse(email));
	}

	/**
	 * This method will test if we can check if a username is available. We will
	 * provide a username that is not available, and test if the controller will
	 * return us the proper result.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfUsernameIsAvailablePassingAnUnavailableUsername() {

		// Create a username to check for
		String username = "cshanor";

		// Expected assert value, Username should not be available
		String expected = "{\"usernameIsAvailable\":false}";

		when(userService.findUserByUsername(username)).thenReturn(mockAppUser);
		assertEquals(expected, authController.checkIfUsernameIsAvailable(username));
	}

	/**
	 * This method will test if we can check if a username is available. We will
	 * provide a username that is available, and test if the controller will return
	 * us the proper result.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 */
	@Test
	public void testCheckIfUsernameIsAvailablePassingAnAvailableUsername() {

		// Create a username to check for
		String username = "cshanor";

		// Expected assert value, Username should be available
		String expected = "{\"usernameIsAvailable\": true}";

		when(userService.findUserByUsername(username)).thenReturn(null);
		assertEquals(expected, authController.checkIfUsernameIsAvailable(username));
	}
	
}
