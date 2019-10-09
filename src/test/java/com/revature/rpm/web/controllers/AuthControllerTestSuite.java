package com.revature.rpm.web.controllers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.services.UserService;

// TODO merge this test suite wiht AuthControllerTest
public class AuthControllerTestSuite {

	private static List<AppUser> mockUsers;

	private AuthController sut;
	private UserService userService = mock(UserService.class);
	
	@BeforeClass
	public static void suiteSetup() {
		mockUsers = new ArrayList<>();
		mockUsers.add(new AppUser(1, "Alice", "Atkinson", "alice.atkinson@gmail.com", "alice.atkinson", "AQ!SWDE#F$", "ROLE_ADMIN"));
		mockUsers.add(new AppUser(2, "Bob", "Butler", "bob.butler@gmail.com", "bob.butler", "bobbert23", "ROLE_USER"));
		mockUsers.add(new AppUser(3, "Charles", "Cohen", "charles.cohen@gmail.com", "charles.cohen", "p4ssw0rd", "ROLE_USER"));
	}

	@Before
	public void testSetup() {
		sut = new AuthController();
		sut.setUserService(userService);
	}
	
	@Test
	public void testGetAllUsers() {
		when(userService.findAllUsers()).thenReturn(mockUsers);
		Object[] expectedResult = mockUsers.toArray();
		Object[] actualResult = sut.getAllUsers().toArray();
		assertArrayEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testGetUserWithExistingId() {
		when(userService.findById(2)).thenReturn(mockUsers.get(1));
		AppUser expectedResult = mockUsers.get(1);
		AppUser actualResult = sut.getUserById(2);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void testGetUserWithNonExistentId() {
		when(userService.findById(4)).thenThrow(new UserNotFoundException("No user found with provided id"));
		sut.getUserById(4);
	}
	
	@Test
	public void testGetUserWithExistingUsername() {
		when(userService.findUserByUsername("alice.atkinson")).thenReturn(mockUsers.get(0));
		AppUser expectedResult = mockUsers.get(0);
		AppUser actualResult = sut.getUserByUsername("alice.atkinson");
		assertEquals(expectedResult, actualResult);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void testGetUserWithNonExistentUsername() {
		when(userService.findUserByUsername("garbage")).thenThrow(new UserNotFoundException("No user found with provided username"));
		sut.getUserByUsername("garbage");
	}
	
	@Test
	public void testGetUserWithExistingEmail() {
		when(userService.findUserByUsername("alice.atkinson@gmail.com")).thenReturn(mockUsers.get(0));
		AppUser expectedResult = mockUsers.get(0);
		AppUser actualResult = sut.getUserByUsername("alice.atkinson@gmail.com");
		assertEquals(expectedResult, actualResult);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void testGetUserWithNonExistentEmail() {
		when(userService.findUserByUsername("garbage@gmail.com")).thenThrow(new UserNotFoundException("No user found with provided email"));
		sut.getUserByEmail("garbage@gmail.com");
	}
	
	@Test
	public void testCheckEmailWhenEmailIsTaken() {
		fail("Pending refactor");
	}
	
	@Test
	public void testCheckEmailWhenEmailIsNotTaken() {
		fail("Pending refactor");
	}
	
	@Test
	public void testCheckEmailWhenUsernameIsTaken() {
		fail("Pending refactor");
	}
	
	@Test
	public void testCheckEmailWhenUsernameIsNotTaken() {
		fail("Pending refactor");
	}
	
	@Test
	public void testRegisterValidUser() {
		AppUser validNewUser = new AppUser(0, "Tester", "McTesterson", "test.user@gmail.com", "test.user", "test-password", "ROLE_USER");
		AppUser expectedResult = new AppUser(4, "Tester", "McTesterson", "test.user@gmail.com", "test.user", "test-password", "ROLE_USER");
		when(userService.addUser(validNewUser)).thenReturn(expectedResult);
		AppUser actualResult = sut.registerUser(validNewUser);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test(expected=UserCreationException.class)
	public void testRegisterInvalidUser() {
		AppUser invalidNewUser = new AppUser(0, "", "", "", "", "", "");
		when(userService.addUser(invalidNewUser)).thenReturn(null);
		sut.registerUser(invalidNewUser);
	}
	
	@Test(expected=UserCreationException.class)
	public void testRegisterNullUser() {
		AppUser nullNewUser = null;
		when(userService.addUser(nullNewUser)).thenReturn(null);
		sut.registerUser(nullNewUser);
	}
	
	@Test
	public void testUpdateValidUser() {
		fail("Pending refactor");
	}
	
	@Test
	public void testUpdateInvalidUser() {
		fail("Pending refactor");
	}
}
