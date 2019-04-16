package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import com.revature.controller.AuthController;
import com.revature.exceptions.UserCreationException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.AppUser;
import com.revature.models.UserErrorResponse;
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
	public void testDeleteWithInvalidId(){
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
	
	/**
	 * This test case verifies proper functionality of the AuthController.getAllUsers() method.
	 * A non-null ArrayList of AppUser objects with a size of one is expected to be returned.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testGetAllUsers() {
		List<AppUser> expectedResult = new ArrayList<>();
		AppUser mockedUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		expectedResult.add(mockedUser);
		when(userService.findAllUsers()).thenReturn(expectedResult);
		
		List<AppUser> testResult = authController.getAllUsers();
		assertNotNull("The ArrayList returned is expected to be not null", testResult);
		assertEquals("The ArrayList returned is expected to have a size of one", 1, testResult.size());
		assertEquals("The AppUser within the returned ArrayList is expected to match the mocked one", mockedUser, testResult.get(0)); 
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.getUserById() method.
	 * A non-null AppUser object is expected to be returned.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testGetUserByIdWithValidId() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(userService.findById(1)).thenReturn(expectedResult);
		
		AppUser testResult = authController.getUserById(1);
		assertNotNull("The AppUser returned is expected to be not null", testResult);
		assertEquals("The AppUser returned is expected to match the mocked one", expectedResult, testResult); 
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.getUserById() method.
	 * A null AppUser object is expected to be returned from the service, which will cause a UserNotFoundException to be thrown.
	 * 
	 * @author Wezley Singleton
	 */
	@Test(expected=UserNotFoundException.class)
	public void testGetUserByIdWithInvalidId() {
		when(userService.findById(1)).thenReturn(null);
		authController.getUserById(1);
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.getUserByUsername() method.
	 * A non-null AppUser object is expected to be returned.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testGetUserByUsernameWithValidUsername() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(userService.findUserByUsername("mocked")).thenReturn(expectedResult);
		
		AppUser testResult = authController.getUserByUsername("mocked");
		assertNotNull("The AppUser returned is expected to be not null", testResult);
		assertEquals("The AppUser returned is expected to match the mocked one", expectedResult, testResult); 
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.getUserByUsername() method.
	 * A null AppUser object is expected to be returned from the service, which will cause a UserNotFoundException to be thrown.
	 * 
	 * @author Wezley Singleton
	 */
	@Test(expected=UserNotFoundException.class)
	public void testGetUserByUsernameWithInvalidUsername() {
		when(userService.findUserByUsername("mocked")).thenReturn(null);
		authController.getUserByUsername("mocked");
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.getUserByUsername() method.
	 * A non-null AppUser object is expected to be returned.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testGetUserByEmailWithValidEmail() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(userService.findUserByEmail("mocked@email.com")).thenReturn(expectedResult);
		
		AppUser testResult = authController.getUserByEmail("mocked@email.com");
		assertNotNull("The AppUser returned is expected to be not null", testResult);
		assertEquals("The AppUser returned is expected to match the mocked one", expectedResult, testResult); 
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.getUserByEmail() method.
	 * A null AppUser object is expected to be returned from the service, which will cause a UserNotFoundException to be thrown.
	 * 
	 * @author Wezley Singleton
	 */
	@Test(expected=UserNotFoundException.class)
	public void testGetUserByEmailWithInvalidEmail() {
		when(userService.findUserByEmail("mocked@email.com")).thenReturn(null);
		authController.getUserByEmail("mocked@email.com");
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.handleUserNotFoundException()
	 * A non-null UserErrorResponse is excepted to be returned. It should have a status of 404 and the correct message.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testHandleUserNotFoundException() {
		UserNotFoundException mockedException = new UserNotFoundException("Mocked user not found exception");

		UserErrorResponse testResult = authController.handleUserNotFoundException(mockedException);
		assertNotNull("The UserErrorResponse returned is expected to be not null", testResult);
		assertEquals("The UserErrorResponse.status is expected to be a 404 (Not Found)", 404 , testResult.getStatus());
		assertEquals("The UserErrorResponse.message is expected to match the mocked message", "Mocked user not found exception", testResult.getMessage());
	}

	/**
	 * This test case verifies proper functionality of the AuthController.handleUserCreationException()
	 * A non-null UserErrorResponse is excepted to be returned. It should have a status of 409 and the correct message.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testHandleUserCreationException() {
		UserCreationException mockedException = new UserCreationException("Mocked user creation exception");
		
		UserErrorResponse testResult = authController.handleUserCreationException(mockedException);
		assertNotNull("The UserErrorResponse returned is expected to be not null", testResult);
		assertEquals("The UserErrorResponse.status is expected to be a 409 (Conflict)", 409 , testResult.getStatus());
		assertEquals("The UserErrorResponse.message is expected to match the mocked message", "Mocked user creation exception", testResult.getMessage());
	}
	
}
