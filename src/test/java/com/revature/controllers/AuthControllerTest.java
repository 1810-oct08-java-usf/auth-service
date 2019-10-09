package com.revature.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import com.revature.rpm.dtos.UserErrorResponse;
import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.services.UserService;
import com.revature.rpm.web.controllers.AuthController;
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
	
	/**
	 * UPDATED: This test the updateUser method, making sure everything is right
	 * and returning a successful status 
	 * 
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 * @author Testing team (ago 2019) - USF
	 */

	@Test
	public void testUpdateUser() throws Exception {
		mockAuth.setAuthenticated(true); // This is supposed to be the user authenticated in the system

		String originalPass = "Terrell12 newPassword";
		String encryptedPassword = "Terrell12";

		// Creating mock user, this user is going to be pass to the rest controller
		// simulating we are calling this method using the angular front-end
		
		AppUser oldUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", encryptedPassword, "ROLE_USER");
		AppUser newUser = new AppUser(1, "mocked", "user", "mocked@email.com", "mocked", originalPass, "");

		when(userService.findById(Mockito.anyInt())).thenReturn(oldUser);
		// We are expecting the updateUser method to be called and the result is successful
		when(userService.updateUser(Mockito.any())).thenReturn(true);

		// Calling updateUser method from the controller
		AppUser resultUser = authController.updateUser(newUser, mockAuth);

		assertEquals(resultUser.getRole(), oldUser.getRole()); // verifying that the update user is sending back to angular
		verify(userService, times(1)).updateUser(Mockito.any()); // verifying the service updateUser was called inside
																	// rest method
	}


	/**
	 * This tests that if the client gave us an incorrect password, it does not
	 * update and returns a status of 401.
	 * 
	 * UPDATED: This test was updated to avoid getting the password from the principal,
	 * and we are making sure when the user enter a wrong password its profile does not update.
	 * 
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 * @author Testing team (ago 2019) - USF
	 */
	@Test
	public void testUpdateUserWithIncorrectPassword() throws Exception {
		
		mockAuth.setAuthenticated(true); // This is supposed to be the user authenticated in the system

		String originalPass = "Terrell12 newPassword";
		String encryptedPassword = "Terrell12"; 
		


		// Creating mock user, this user is going to be pass to the rest controller
		// simulating we are calling this method using the angular front-end
		
		AppUser oldUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", encryptedPassword, "ROLE_USER");
		AppUser newUser = new AppUser(1, "mocked", "user", "mocked@email.com", "mocked", originalPass + "aa", "");
		
		when(userService.findById(Mockito.anyInt())).thenReturn(oldUser);

		// Calling updateUser method from the controller
		authController.updateUser(newUser, mockAuth);
		
		// Verifying we call the find by id method
		verify(userService).findById(Mockito.anyInt());

	}

	/**
	 * 
	 * UPDATE: If the user is null, we return null back to the front-end.
	 * 
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 * @author Testing team (ago 2019) - USF
	 */
	@Test
	public void testUpdateUserWhenNull() throws Exception {

		mockAuth.setAuthenticated(true); // User has to be authenticated
		
		// Trying to update the user, sending a null parameter
		AppUser result = authController.updateUser(null, mockAuth);
		
		assertNull(result); // Verifying we are returning null from the controller
	}

	/**
	 * This test case verifies the proper functionality of the
	 * AuthController.updateUser() method when it is provided a valid updated
	 * AppUser with all updateable fields. The expected result is an AppUser object
	 * whose fields match those of the AppUser passed as a method argument.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testUpdateUserValidUserValidWithAllUpdateableFields() {
		// Added this to test password functionality
		// Testing Team (Ago 2019) - USF
		String originalPass = "Terrell12 Terrell12";
		String encryptedPassword = "Terrell12"; 
		
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", encryptedPassword, "ROLE_USER");
		AppUser mockedUserForUpdate = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", originalPass,
				"ROLE_USER");
		AppUser mockedPersistedUser = new AppUser(1, "mock", "user", "mock@email.com", "mocked", encryptedPassword, "ROLE_USER");
		mockAuth.setAuthenticated(true);

		when(userService.findById(1)).thenReturn(mockedPersistedUser);
		when(userService.updateUser(mockedUserForUpdate)).thenReturn(true);

		AppUser testResult = authController.updateUser(mockedUserForUpdate, mockAuth);
		verify(userService, times(1)).updateUser(mockedUserForUpdate);
		assertNotNull("The AppUser returned is expected to be not null", testResult);
		assertEquals("The AppUser returned is expected to match the mocked one", expectedResult, testResult);
	}
	/**
	 * This test case verifies the proper functionality of the 
	 * AuthController.updateToAdmin() method when it is provided a valid updated AppUser with the updated fields.
	 * The expected result is an AppUser object whose fields match those of the AppUser passed as the argument. 
	 */

	@Test
	public void testUpdateUserRole() {
		String originalRole = "ROLE_ADMIN";
		String newRole = "ROLE_ADMIN";
		
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mock", newRole);
		AppUser mockedUserForUpdate = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mock", originalRole);
		
		AppUser mockedPersistedAdmin = new AppUser(1, "mock", "user", "mock@email.com", "mocked", "mock", newRole);
		mockAuth.setAuthenticated(true);

		when(userService.findById(1)).thenReturn(mockedPersistedAdmin);
		when(userService.updateUser(mockedUserForUpdate)).thenReturn(true);

		AppUser testResult = authController.updateUserRole(mockedUserForUpdate, mockAuth);
		verify(userService, times(1)).updateUser(mockedUserForUpdate);
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