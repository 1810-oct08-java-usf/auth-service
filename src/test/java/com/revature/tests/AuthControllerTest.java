package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
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
import com.revature.models.UserPrincipal;
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
	private UserPrincipal mockPrincipal;
	@Mock
	private AppUser backUser;
	@Mock
	private Authentication mockAuth;

	@InjectMocks
	private AuthController authController;
	
	
	private String roleAdmin = "role_admin";
	private String password = "valid";
	
	/**
	 * makes a mock auth controller for each test methods.
	 * @author Ankit Patel (190107-Java-Spark-USF)
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);		
	}
		
	
	
	/**
	 * Test Delete user with good values.
	 * 
	 * @author Ankit Patel (190107-Java-Spark-USF)
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 * @throws Exception 
	 */
	@Test
	public void testDeleteUserIfUserExist() throws Exception {
		when(userService.findById(0)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(0);
		when(userService.deleteUserById(0)).thenReturn(true);
		authController.deleteUser(0);
		verify(userService, times(1)).deleteUserById(0);
	}
		

	/**
	 * Test if user object doesn't exist.
	 * @author Ankit Patel (190107-Java-Spark-USF)
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 * @throws UserNotFoundException
	 */

	@Test (expected = UserNotFoundException.class)
	public void testDeleteUserIfUserDoesntExist() {
		when(userService.findById(0)).thenReturn(null);		
		authController.deleteUser(0);
	}
	
//-------------------------------------------------------------------------------------------------------
//				Test Update
//-------------------------------------------------------------------------------------------------------
	
	//
	/**
	 *  Tests if it updates successfully with given user.
	 *  Please refactor the controller method to use already fetched user object.
	 *  @author Ankit Patel (190107-Java-Spark-USF)
	 * 	@author Jaitee Pitts (190107-Java-Spark-USF)
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
		
		authController.updateUser(mockUser, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		
	}
	
	/**
	 * Tests update user 
	 * where backend password is different from given password from front end.
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 * @author Ankit Patel (190107-Java-Spark-USF)
	 */
	
	@Test(expected = UserNotFoundException.class)
	public void testUpdateUserWithDifferentBackEndPassword() {
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("sally");
		
		when(userService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(backUser);
		
		when(mockUser.getRole()).thenReturn(roleAdmin);
		when(mockUser.getPassword()).thenReturn("seashore newPass");
		
		when(backUser.getPassword()).thenReturn("newPass");
		
		
		authController.updateUser(mockUser, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		
	}
	//------------------------------
	// New Update Tests
	//------------------------------
	

	/**
	 * Current version of UpdateUser has complicated logic.
	 * This test is written with Test Driven Development in mind
	 * The rewritten version of update user should use this test as a guideline.
	 * 
	 * Ideally these checks would happen in the service layer,
	 * however since they were originally in the controller this test is placed here.
	 * 
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Ignore
	public void testUpdateUser() throws Exception {
		
		mockAuth.setAuthenticated(true);
		
		//assuming front end is refactored to send a userPrincipal
		when(mockPrincipal.getAppUser()).thenReturn(mockUser);
		when(userService.findUserByUsername(mockPrincipal.getUsername())).thenReturn(backUser);
		
		//checking that principal gave a valid password
		when(backUser.getPassword()).thenReturn(password);
		when(mockPrincipal.getPassword()).thenReturn(password);
		
		//commented out because it's incompatible with current method
//		authController.updateUser(mockPrincipal, mockAuth);
		
	}
	
	/**
	 * This tests that if the client gave us an incorrect password,
	 * it does not update and returns a status of 401.
	 * 
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Ignore
	public void testUpdateUserWithIncorrectPassword() throws Exception {
		mockAuth.setAuthenticated(true);

		when(mockPrincipal.getAppUser()).thenReturn(mockUser);
		when(userService.findUserByUsername(mockPrincipal.getUsername())).thenReturn(backUser);
		
		//checking that principal gave a valid password
		when(backUser.getPassword()).thenReturn(password);
		when(mockPrincipal.getPassword()).thenReturn("invalid");
		
		
		//commented out because it's incompatible with current method
//		authController.updateUser(mockPrincipal, mockAuth);
		
	}
	
	/**
	 * This tests that if the client gave us an incorrect username,
	 * it does not update and throws and exception.
	 * 
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Ignore
	public void testUpdateUserWhenNull() throws Exception {
		mockAuth.setAuthenticated(true);

		when(mockPrincipal.getAppUser()).thenReturn(mockUser);
		when(userService.findUserByUsername(mockPrincipal.getUsername())).thenReturn(null);
		
		
		//commented out because it's incompatible with current method
//		authController.updateUser(mockPrincipal, mockAuth);
		
	}
	
	//----------------------------------------------------------------
	//                   Testing checking methods
	//----------------------------------------------------------------



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

		when(userService.findUserByEmail(email)).thenReturn(mockUser);
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

		when(userService.findUserByUsername(username)).thenReturn(mockUser);
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
