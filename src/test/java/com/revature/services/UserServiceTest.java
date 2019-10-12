package com.revature.services;

import static org.junit.Assert.assertEquals;
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
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
	 */
	@Test
	public void testFindUserByIdWithValidId() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		Optional<AppUser> mockedOptional = Optional.of(expectedResult);
		when(mockRepo.findById(1)).thenReturn(mockedOptional);
	
		AppUser testResult = userService.findUserById(1);
		assertNotNull("The AppUser returned should not be a null value", testResult);
		assertEquals("The AppUser returned should match the mocked one", expectedResult, testResult);
	}
	
	/**
	 * This test case verifies proper functionality of the UserService.findById() method when it is given an invalid id.
	 * A BadRequestException is expected to be thrown.
	 */
	@Test(expected=BadRequestException.class)
	public void testFindUserByIdWithInvalidId() {
		userService.findUserById(0);
		verify(mockRepo, times(0)).findById(Mockito.anyInt());
	}
	
	/**
	 * This test case verifies proper functionality of the UserService.findById() method when it is given an valid id,
	 * which does not correspond to any user in the data source. A UserNotFoundException is expected to be thrown.
	 */
	@Test(expected=UserNotFoundException.class)
	public void testFindUserByIdWithValidIdNotFound() {
		when(mockRepo.findById(1)).thenReturn(Optional.ofNullable(null));
		userService.findUserById(1);
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
	@Test(expected=BadRequestException.class)
	public void testFindUserByUsernameInvalidUsername() {
		userService.findUserByUsername("");
		verify(mockRepo, times(0)).findUserByUsername(Mockito.anyString());
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
	 * 	Tests behavior of UserService's findUserByEmail() when passed an invalid email value.
	 * A BadRequestException is expected to be thrown.
	 */
	@Test(expected=BadRequestException.class)
	public void testFindUserByEmailInvalidEmail() {
		userService.findUserByEmail("");
		verify(mockRepo, times(0)).findUserByEmail(Mockito.anyString());
	}
	
	/**
	 * 	Tests the addUser() of the UserService
	 * 	when the given user does not already exists
	 * 	in the database. 
	 */
	@Test
	public void testAddUserIfUserNotInDatabase() {
		AppUser mockedUser = new AppUser(0, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.save(mockedUser)).thenReturn(mockedUser);
		assertEquals(mockedUser, userService.addUser(mockedUser));
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
	@Test(expected=UserNotFoundException.class)
	public void testAddUserIfUsernameAlreadyExists() {
		when(userService.findUserByUsername("William"))
		.thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("William"); 
		
		assertEquals(null, userService.addUser(mockUser));
	}
	
	
	/** 
	 * Tests the UserService.addUser functionality when a new user object is provided that has an email
	 * that is already present in the data source. A UserCreationException is expected to be thrown and
	 * it is expected that the no methods of the UserRepository are invoked.
	 */
	@Test(expected=UserCreationException.class)
	public void testAddUserIfEmailAlreadyExists() {
		
		AppUser mockedUser = new AppUser(0, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findUserByEmail("mocked@email.com")).thenReturn(new AppUser());
		userService.addUser(mockedUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests the UserService.updateUser functionality when a invalid user object is provided. A 
	 * BadRequestException is expected to be thrown, and it is expected that the no methods of the 
	 * UserRepository are invoked.
	 */
	@Test(expected=BadRequestException.class)
	public void testUpdateUserNull() {
		userService.updateUser(null, false);
		verify(mockRepo, times(0)).save(Mockito.any());
	}
	
	/**
	 * Tests UserService.updateUser when a valid user object is provided which is not attempting to update
	 * their username, email, or role. 
	 */
	@Test
	public void testUpdateUserValidNotChangingUsernameEmailOrRole() {
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(validMockUser));
		assertTrue(userService.updateUser(validMockUser, false));
	}
	
	/**
	 * This test case verifies the proper functionality of the UserService.deleteUserById() method when a valid id
	 * is provided to it. The expected result is that the UserRepository.delete() method is called once and that the
	 * returned value is true.
	 */
	@Test
	public void testDeleteUserByIdValidId() {
		AppUser mockedUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findById(1)).thenReturn(Optional.of(mockedUser));
		assertTrue(userService.deleteUserById(1));
	}
	
	/**
	 * This test case verifies the proper functionality of the UserService.deleteUserById() method when a invalid id
	 * is provided to it. The expected result is that a BadRequestException is thrown and that the UserRepository.delete() 
	 * method is not called.
	 */
	@Test(expected=BadRequestException.class)
	public void testDeleteUserByIdInvalidId() {
		userService.deleteUserById(0);
		verify(mockRepo, times(0)).delete(Mockito.any());
	}
	
	/**
	 * This test case verifies the proper functionality of the UserService.deleteUserById() method when a valid id
	 * is provided to it, but not user is found. The expected result is that a UserNotFoundException is thrown.
	 * 
	 * @exception UserNotFoundException
	 */
	@Test(expected=UserNotFoundException.class)
	public void testDeleteUserByIdValidIdNotFound() {
		AppUser mockedUser = null;
		Optional<AppUser> mockedOptional = Optional.ofNullable(mockedUser);
		when(mockRepo.findById(1)).thenReturn(mockedOptional);
		
		userService.deleteUserById(1);
		verify(mockRepo, times(0)).delete(mockedUser);
	}
}
