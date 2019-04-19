package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.Authentication;

import com.revature.controller.AuthController;
import com.revature.exceptions.UserCreationException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.AppUser;

import com.revature.models.UserPrincipal;
import com.revature.models.UserErrorResponse;
import com.revature.service.UserService;
/**
 * This class will be used to test methods in the AuthController. Testing will
 * be done using Junit to test different scenarios.
 * 
 * @RunWith(): Specifies that we will be using the MockitoJUnit runner class.
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
	private UserPrincipal mockPrincipal;
	@Mock
	private Authentication mockAuth;
		
	@InjectMocks
	private AuthController authController;
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
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
	
	//------------------------------------------
	
	//                 New Update Tests
	
	//------------------------------------------
	
	// TODO Test cases need to be written for AuthController.updateUser() so that the scheduled refactoring can be done.
	
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
		verify(userService, times(1)).updateUser(mockUser);
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
		exceptionRule.expect(UserNotFoundException.class);
		exceptionRule.expectMessage("The given password is incorrect");
		
		when(mockPrincipal.getAppUser()).thenReturn(mockUser);
		when(userService.findUserByUsername(mockPrincipal.getUsername())).thenReturn(backUser);

		//checking that principal gave a valid password

		when(backUser.getPassword()).thenReturn(password);
		when(mockPrincipal.getPassword()).thenReturn("invalid");

		//commented out because it's incompatible with current method
//		authController.updateUser(mockPrincipal, mockAuth);
		AppUser testResult = authController.updateUser(mockUser, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		assertNotNull("The AppUser returned is expected to be not null", testResult);
		
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
		exceptionRule.expect(UserCreationException.class);
		exceptionRule.expectMessage("User cannot be null");

		//commented out because it's incompatible with current method
//		authController.updateUser(mockPrincipal, null);
	}
	
	/**
	 * This test case verifies the proper functionality of the AuthController.updateUser() method when it
	 * is provided a valid updated AppUser with all updateable fields. The expected result is an AppUser
	 * object whose fields match those of the AppUser passed as a method argument.
	 * 
	 * @author Wezley Singleton
	 */
	@Ignore("AuthController.updateUser() is scheduled for refactor")
	public void testUpdateUserValidUserValidWithAllUpdateableFields() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "ROLE_USER");
		AppUser mockedUserForUpdate = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "ROLE_USER");
		AppUser mockedPersistedUser = new AppUser(1, "mock", "user", "mock@email.com", "mocked", "mocked", "ROLE_USER");
		mockAuth.setAuthenticated(true);
		
		when(mockAuth.getPrincipal()).thenReturn("mocked");
		when(userService.findUserByUsername(mockAuth.getPrincipal().toString())).thenReturn(mockedPersistedUser);
		when(userService.findById(1)).thenReturn(mockedPersistedUser);
		when(userService.updateUser(mockUser)).thenReturn(true);
		
		AppUser testResult = authController.updateUser(mockedUserForUpdate, mockAuth);
		verify(userService, times(1)).updateUser(mockUser);
		assertNotNull("The AppUser returned is expected to be not null", testResult);
		assertEquals("The AppUser returned is expected to match the mocked one", expectedResult, testResult);
	}

	

	//----------------------------------------------------------------

	//                   Testing checking methods

	//----------------------------------------------------------------

	
	
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
	public void testCheckIfUsernameIsAvailablePassingUnavailableUsername() {

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
	public void testCheckIfUsernameIsAvailablePassingAvailableUsername() {

		// Create a username to check for
		String username = "cshanor";

		// Expected assert value, Username should be available
		String expected = "{\"usernameIsAvailable\": true}";

		when(userService.findUserByUsername(username)).thenReturn(null);
		assertEquals(expected, authController.checkIfUsernameIsAvailable(username));
	}
	
	
		
	
	/**
	 * This test case verifies the proper functionality of the AuthController.deleteUser() method when a valid id
	 * is provided to it. The expected result is that the UserService.deleteUserById() method is called once.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testDeleteWithValidId() {
		AppUser mockedPersistentUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "ROLE_USER");
		when(userService.findById(1)).thenReturn(mockedPersistentUser);
		when(userService.deleteUserById(1)).thenReturn(true);
		authController.deleteUser(1);
		verify(userService, times(1)).deleteUserById(1);
	}
	
	/**
	 * This test case verifies the proper functionality of the AuthController.deleteUser() method when a invalid id
	 * is provided to it. The expected result is for the method to throw a UserNotFoundException.
	 * 
	 * @author Wezley Singleton
	 */
	@Test(expected=UserNotFoundException.class)  
	public void testDeleteWithInvalidId() {
		when(userService.findById(0)).thenReturn(null);
		authController.deleteUser(0);
	}
	
	/**
	 * This test case verifies the proper functionality of the AuthController.deleteUser() method when a invalid id
	 * is provided to it. The expected result is for the method to throw a UserNotFoundException.
	 * 
	 * @author Wezley Singleton
	 */
	@Test(expected=UserNotFoundException.class)  
	public void testDeleteWhenServiceReturnsFalse() {
		AppUser mockedPersistentUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "ROLE_USER");
		when(userService.findById(1)).thenReturn(mockedPersistentUser);
		when(userService.deleteUserById(1)).thenReturn(false);
		authController.deleteUser(1);
		verify(userService, times(0)).deleteUserById(1);
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
