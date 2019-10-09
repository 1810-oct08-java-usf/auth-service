package com.revature.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.rpm.entities.AppUser;
import com.revature.rpm.repositories.UserRepository;
import com.revature.rpm.services.UserService;



/** Test Suite for the UserService class. 
 *  Special thanks to Alonzo Muncy
 *	for his helpful explanations regarding the Unit Testing process.   
 *  
 * @author Brandon Morris (190107 Java-Spark-USF) 2019-04-09
 *  
 * Test Suite for the UserService class.  
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@Mock UserRepository mockRepo;  
	
	@Mock AppUser mockUser;

	@Mock List<AppUser> mockUserList;

	@InjectMocks
	UserService userService;

	/** 
	 *  Simple test for UserService.findAllUsers() 
	 * 	Test that the repo method findAll() is called and
	 * 	returns a list of users.  	
	 */
	@Test
	public void testFindAllUsers() {
		when(mockRepo.findAll()).thenReturn(mockUserList);
		assertEquals(userService.findAllUsers(), mockUserList);
	}
	
	/**
	 * This test case verifies proper functionality of the UserService.findById() method.
	 * A non-null AppUser object is expected to be returned.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testFindUserByIdWithValidId() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		Optional<AppUser> mockedOptional = Optional.of(expectedResult);
		when(mockRepo.findById(1)).thenReturn(mockedOptional);
	
		AppUser testResult = userService.findById(1);
		assertNotNull("The AppUser returned should not be a null value", testResult);
		assertEquals("The AppUser returned should match the mocked one", expectedResult, testResult);
	}
	
	/**
	 * This test case verifies proper functionality of the AuthController.UserService.findById() method when it is given an invalid id.
	 * A null AppUser object is expected to be returned from the service, which will cause a UserNotFoundException to be thrown.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testFindUserByIdWithInvalidId() {
		Optional<AppUser> mockedOptional = Optional.ofNullable(null);
		when(mockRepo.findById(1)).thenReturn(mockedOptional);
	
		AppUser testResult = userService.findById(1);
		assertNull("The AppUser returned should be a null value", testResult);
	}
	
	/**
	 * Test for UserService.findUserByUsername()
	 * When the given username is found in the database. 
	 */
	@Test
	public void testFindUserByUsernameValidUsername() {
		when(mockRepo.findUserByUsername("wShatner")).thenReturn(mockUser);
		assertEquals(mockUser, userService.findUserByUsername("wShatner"));
	}
	
	/**
	 * Test for UserSErvice.findUserByUsername() 
	 * When the given username is not found in the database. 
	 */
	@Test
	public void testFindUserByUsernameInvalidUsername() {
		when(mockRepo.findUserByUsername("wShatner")).thenReturn(null);
		assertEquals(null, userService.findUserByUsername("wShatner"));
	}
	
	/**
	 * 	Tests behavior of UserService's findUserByEmail()
	 * 	when the user is found in the database.  
	 */
	@Test
	public void testFindUserByEmailValidEmail() {
		when(mockRepo.findUserByEmail("wshatner@gmail.com")).thenReturn(mockUser);
		assertEquals(mockUser, userService.findUserByEmail("wshatner@gmail.com"));
	}
	
	/**
	 * 	Tests behavior of UserService's findUserByEmail()
	 *	when the user is NOT found in the database. 
	 */
	@Test
	public void testFindUserByEmailInvalidEmail() {
		when(mockRepo.findUserByEmail("wshatner@gmail.com")).thenReturn(null);
		assertEquals(null, userService.findUserByEmail("wshatner@gmail.com"));
	}
	
	/**
	 * 	Tests the addUser() of the UserService
	 * 	when the given user does not already exists
	 * 	in the database. 
	 */
	@Test
	public void testAddUserIfUserNotInDatabase() {
//		mockup the mockAppUser's methods. 
		when(mockUser.getUsername()).thenReturn("William");
	
		when(mockUser.getEmail()).thenReturn("William@gmail.com");

		when(mockRepo.save(mockUser)).thenReturn(mockUser);

//		Now test the actual results of invocation. 
//		Since there is no such user in the database,
//		we should be returned the user that was added just now.  
		assertEquals(mockUser, userService.addUser(mockUser));
	}
	
	
	/**	
	 * 	Tests the addUser() of the UserService 
	 * 	when the given username already exists
	 * 	in the database. 
	 * 	
	 * 	When the UserService's addUser() is invoked, 
	 *  the simulated method call findUserByUsername() 
	 *  is fed an arbitrary string as the username,
	 *  and the mocked AppUser object is returned.   
	 * 	
	 *  Then we simulate the mock AppUser's getUsername() 
	 *  and return another arbitrary string.   
	 */ 
	@Test
	public void testAddUserIfUsernameAlreadyExists() {
		when(userService.findUserByUsername("William"))
		.thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("William"); 
		
		assertEquals(null, userService.addUser(mockUser));
	}
	
	
	/** 
	 * 	Tests the addUser() of UserService
	 * 	when the given email is already present
	 * 	in the database. 
	 * 
	 * 
	 * 	In the addUser(), null is returned 
	 *  if either the username already exists
	 *  in the database, or the email already exists 
	 *  in the database. But it will check for the Username first.
	 *  
	 *  So, when given that a certain username does exist,
	 *  and when given that a certain email does exist in 
	 *  the database, then the value returned should be
	 *  null. (If the username is not found addUser() returns 
	 *  a newUser object that was added to the database. 
	 *  
	 *  So our assertEquals statement says "we expect this to 
	 *  return null when given a user that is already in the 
	 *  database. 
	 */
	@Test
	public void testAddUserIfEmailAlreadyExists() {
	
		when(mockUser.getEmail()).thenReturn("William@gmail.com");
		when(userService.findUserByEmail("William@gmail.com"))
		.thenReturn(mockUser);
		
		
		assertEquals(null, userService.addUser(mockUser));
	}

	/**
	 * Test UserService's updateUser()
	 * when passed a null value. 
	 */
	@Test
	public void testUpdateUserNull() {
		assertEquals(false, userService.updateUser(null));
	}
	
	/**
	 * 	Test UserService's updateUser()
	 *  when passed a valid User object. 
	 */
	@Test
	public void testUpdateUserValid() {
		when(mockRepo.save(mockUser)).thenReturn(mockUser);
		assertEquals(true, userService.updateUser(mockUser));
	}
	
	/**
	 * This test case verifies the proper functionality of the UserService.deleteUserById() method when a valid id
	 * is provided to it. The expected result is that the UserRepository.delete() method is called once and that the
	 * returned value is true.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testDeleteUserByIdValidId() {
		AppUser mockedUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		Optional<AppUser> mockedOptional = Optional.of(mockedUser);
		when(mockRepo.findById(1)).thenReturn(mockedOptional);
		boolean testResult = userService.deleteUserById(1);
		verify(mockRepo, times(1)).delete(mockedUser);
		assertTrue("The expected result is true, meaning that the user was successfully deleted", testResult);
	}
	
	/**
	 * This test case verifies the proper functionality of the UserService.deleteUserById() method when a valid id
	 * is provided to it. The expected result is that the UserRepository.delete() method is not called and that the
	 * returned value is false.
	 * 
	 * @author Wezley Singleton
	 */
	@Test
	public void testDeleteUserByIdInvalidId() {
		AppUser mockedUser = null;
		Optional<AppUser> mockedOptional = Optional.ofNullable(mockedUser);
		when(mockRepo.findById(1)).thenReturn(mockedOptional);
		
		boolean testResult = userService.deleteUserById(1);
		verify(mockRepo, times(0)).delete(mockedUser);
		assertFalse("The expected result is false, meaning that no user was deleted", testResult);
	}
}
