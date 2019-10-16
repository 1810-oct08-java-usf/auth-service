package com.revature.rpm.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.exceptions.UserUpdateException;
import com.revature.rpm.repositories.UserRepository;
import com.revature.rpm.services.UserService;

/*
 * TODO
 * 
 * 	- Add unit tests for validateFields(AppUser user)
 *  - Add unit tests for loadUserByUsername(String username)
 *  - Add unit tests for cover missed branches based on Jacoco coverage report
 */

/**
 * Test Suite for the UserService class.
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@Mock UserRepository mockRepo;  

	@InjectMocks
	UserService userService;

	/** 
	 *  Simple test for UserService.findAllUsers() 
	 * 	Test that the repo method findAll() is called and
	 * 	returns a list of users.  	
	 */
	@Test
	public void testFindAllUsers() {
		List<AppUser> mockList = new ArrayList<>();
		when(mockRepo.findAll()).thenReturn(mockList);
		assertEquals(mockList, userService.findAllUsers());
	}
	
	/**
	 * Verifies proper functionality of the UserService.findById() method.
	 * A non-null AppUser object is expected to be returned.
	 */
	@Test
	public void testFindUserByIdWithValidId() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findById(1)).thenReturn(Optional.of(expectedResult));
	
		AppUser testResult = userService.findUserById(1);
		assertNotNull(testResult);
		assertEquals(expectedResult, testResult);
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
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findUserByUsername("mocked")).thenReturn(expectedResult);
	
		AppUser testResult = userService.findUserByUsername("mocked");
		assertNotNull(testResult);
		assertEquals(expectedResult, testResult);
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
	 * Tests behavior of UserService.findUserByEmail when a valid email is provided that
	 * matches to a user found in the data source.  
	 */
	@Test
	public void testFindUserByEmailValidEmail() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findUserByEmail("mocked@email.com")).thenReturn(expectedResult);
		assertEquals(expectedResult, userService.findUserByEmail("mocked@email.com"));
	}
	
	/**
	 * Tests behavior of UserService.findUserByEmail when passed an invalid email value.
	 * A BadRequestException is expected to be thrown.
	 */
	@Test(expected=BadRequestException.class)
	public void testFindUserByEmailInvalidEmail() {
		userService.findUserByEmail("");
		verify(mockRepo, times(0)).findUserByEmail(Mockito.anyString());
	}
	
	/**
	 * Tests behavior of UserService.addUser when passed a valid user object whose
	 * provided username nor email is already used within the data source. 
	 */
	@Test
	public void testAddUserIfUserNotInDatabase() {
		AppUser mockedUser = new AppUser(0, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.save(mockedUser)).thenReturn(mockedUser);
		assertEquals(mockedUser, userService.addUser(mockedUser));
	}
	
	
	/**
	 * Tests behavior of UserService.addUser when passed a valid user object whose
	 * provided username is already used within the data source. 
	 */
	@Test(expected=UserCreationException.class)
	public void testAddUserIfUsernameAlreadyExists() {
		AppUser mockedUser = new AppUser(0, "Mocked", "User", "mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findUserByUsername("mocked")).thenReturn(new AppUser());
		userService.addUser(mockedUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}
	
	
	/**
	 * Tests behavior of UserService.addUser when passed a valid user object whose
	 * provided email is already used within the data source. 
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
	public void testUpdateInvalidUser() {
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
	 * Tests UserService.updateUser when a valid user object is provided which attempting to update their 
	 * username which is not taken
	 */
	@Test
	public void testUpdateUserValidChangingUsernameNotTaken() {
		AppUser persistedUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "old-mocked", "mocked", "USER");
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "new-mocked", "mocked", "USER");
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByUsername("new-mocked")).thenReturn(null);
		assertTrue(userService.updateUser(validMockUser, false));
	}
	
	/**
	 * Tests UserService.updateUser when a valid user object is provided which attempting to update their 
	 * username which is taken
	 */
	@Test(expected=UserUpdateException.class)
	public void testUpdateUserValidChangingUsernameTaken() {
		AppUser persistedUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "old-mocked", "mocked", "USER");
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "new-mocked", "mocked", "USER");
		AppUser userWithTakenUsername = new AppUser(2, "Existing", "User", "existing@email.com", "new-mocked", "password", "USER");
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByUsername(validMockUser.getUsername())).thenReturn(userWithTakenUsername);
		userService.updateUser(validMockUser, false);
		verify(mockRepo, times(0)).save(Mockito.any());
	}
	
	/**
	 * Tests UserService.updateUser when a valid user object is provided which attempting to update their 
	 * username which is not taken
	 */
	@Test
	public void testUpdateUserValidChangingEmailNotTaken() {
		AppUser persistedUser = new AppUser(1, "Mocked", "User", "old-mocked@email.com", "mocked", "mocked", "USER");
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "new-mocked@email.com", "mocked", "mocked", "USER");
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByEmail("new-mocked@email.com")).thenReturn(null);
		assertTrue(userService.updateUser(validMockUser, false));
	}
	
	/**
	 * Tests UserService.updateUser when a valid user object is provided which attempting to update their 
	 * username which is taken
	 */
	@Test(expected=UserUpdateException.class)
	public void testUpdateUserValidChangingEmailTaken() {
		AppUser persistedUser = new AppUser(1, "Mocked", "User", "old-mocked@email.com", "mocked", "mocked", "USER");
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "new-mocked@email.com", "mocked", "mocked", "USER");
		AppUser userWithTakenEmail = new AppUser(2, "Existing", "User", "new-mocked@email.com", "existing", "password", "USER");
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByEmail("new-mocked@email.com")).thenReturn(userWithTakenEmail);
		userService.updateUser(validMockUser, false);
		System.out.println("\n\n\n\n");
		verify(mockRepo, times(0)).save(Mockito.any());
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
