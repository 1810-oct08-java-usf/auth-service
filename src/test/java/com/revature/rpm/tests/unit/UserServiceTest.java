package com.revature.rpm.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.revature.rpm.entities.AppUser;
import com.revature.rpm.entities.UserRole;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.exceptions.UserUpdateException;
import com.revature.rpm.repositories.UserRepository;
import com.revature.rpm.services.UserService;
import com.revature.rpm.tokens.ScopeMapper;

/**
 * Test Suite for the UserService class. Methods unit tested in this suite
 * include:<br>
 * <br>
 * - findAllUsers: List&lt;AppUser&gt;<br>
 * - findUserById: AppUser<br>
 * - findUserByUsername: AppUser<br>
 * - findUserByEmail: AppUser<br>
 * - addUser: AppUser<br>
 * - updateUser: boolean<br>
 * - deleteUserById: boolean<br>
 * - isUsernameAvailable: boolean<br>
 * - isEmailAddressAvailable: boolean<br>
 * - validateFields: boolean<br>
 * - loadUserByUsername: UserPrincipal<br>
 * <br>
 * Current code coverage: 100%<br>
 * Current branch coverage: 100%
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	UserRepository mockRepo;

	@Mock
	BCryptPasswordEncoder mockEncoder;
	
	@Mock
	ScopeMapper mockScopeMapper;

	@InjectMocks
	UserService userService;

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.findAllUsers method, whose
	 * function is to call UserRepository.findAll to fetch all AppUser objects from
	 * the data source.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Tests behavior of UserService.findAllUsers. The expected result is for the
	 * method invoke the UserRepository.findAll method and returns a list of AppUser
	 * objects.
	 */
	@Test
	public void testFindAllUsers() {
		List<AppUser> mockList = new ArrayList<>();
		when(mockRepo.findAll()).thenReturn(mockList);
		assertEquals(mockList, userService.findAllUsers());
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.findUserById method, whose
	 * function is to call UserRepository.findAll to fetch a AppUser object from the
	 * data source whose id matches the provided value.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Verifies proper functionality of the UserService.findById() method. A
	 * non-null AppUser object is expected to be returned.
	 */
	@Test
	public void testFindUserByIdWithValidId() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findById(1)).thenReturn(Optional.of(expectedResult));

		AppUser testResult = userService.findUserById(1);
		assertNotNull(testResult);
		assertEquals(expectedResult, testResult);
	}

	/**
	 * This test case verifies proper functionality of the UserService.findById()
	 * method when it is given an invalid id. A BadRequestException is expected to
	 * be thrown.
	 */
	@Test(expected = BadRequestException.class)
	public void testFindUserByIdWithInvalidId() {
		userService.findUserById(0);
		verify(mockRepo, times(0)).findById(Mockito.anyInt());
	}

	/**
	 * This test case verifies proper functionality of the UserService.findById()
	 * method when it is given an valid id, which does not correspond to any user in
	 * the data source. A UserNotFoundException is expected to be thrown.
	 */
	@Test(expected = UserNotFoundException.class)
	public void testFindUserByIdWithValidIdNotFound() {
		when(mockRepo.findById(1)).thenReturn(Optional.ofNullable(null));
		userService.findUserById(1);
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.findUserByUsername method, whose
	 * function is to call UserRepository.findUserByUsername to fetch an AppUser
	 * object from the data source whose username matches the provided string value.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Test for UserService.findUserByUsername() When the given username is found in
	 * the database.
	 */
	@Test
	public void testFindUserByUsernameValidUsername() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findUserByUsername("mocked")).thenReturn(expectedResult);

		AppUser testResult = userService.findUserByUsername("mocked");
		assertNotNull(testResult);
		assertEquals(expectedResult, testResult);
	}

	/**
	 * Tests behavior of UserService.findUserByEmail when a valid username is
	 * provided that matches to a user found in the data source.
	 */
	@Test(expected = BadRequestException.class)
	public void testFindUserByUsernameInvalidUsername() {
		userService.findUserByUsername("");
		verify(mockRepo, times(0)).findUserByUsername(Mockito.anyString());
	}

	/**
	 * Tests behavior of UserService.findUserByEmail when a null username is
	 * provided that matches to a user found in the data source.
	 */
	@Test(expected = BadRequestException.class)
	public void testFindUserByUsernameNullUsername() {
		userService.findUserByUsername(null);
		verify(mockRepo, times(0)).findUserByUsername(Mockito.anyString());
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.findUserByEmail method, whose
	 * function is to call UserRepository.findUserByUsername to fetch an AppUser
	 * object from the data source whose email matches the provided string value.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Tests behavior of UserService.findUserByEmail when a valid email is provided
	 * that matches to a user found in the data source.
	 */
	@Test
	public void testFindUserByEmailValidEmail() {
		AppUser expectedResult = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findUserByEmail("mocked@email.com")).thenReturn(expectedResult);
		assertEquals(expectedResult, userService.findUserByEmail("mocked@email.com"));
	}

	/**
	 * Tests behavior of UserService.findUserByEmail when passed an invalid email
	 * value. A BadRequestException is expected to be thrown.
	 */
	@Test(expected = BadRequestException.class)
	public void testFindUserByEmailInvalidEmail() {
		userService.findUserByEmail("");
		verify(mockRepo, times(0)).findUserByEmail(Mockito.anyString());
	}

	/**
	 * Tests behavior of UserService.findUserByEmail when passed an null email
	 * value. A BadRequestException is expected to be thrown.
	 */
	@Test(expected = BadRequestException.class)
	public void testFindUserByEmailNullEmail() {
		userService.findUserByEmail(null);
		verify(mockRepo, times(0)).findUserByEmail(Mockito.anyString());
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.addUser method, whose function
	 * is to call UserRepository.save to persist a new valid AppUser object from the
	 * data source. A valid AppUser passes the UserService.validateFields check, and
	 * does not have a username or email that is already used by another user in the
	 * data source.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Tests behavior of UserService.addUser when passed a valid AppUser object
	 * whose provided username nor email is not already used within the data source.
	 * The expected result is for the method invoke the UserRepository.save method
	 * and returns a persisted AppUser.
	 */
	@Test
	public void testAddUserIfUserNotInDatabase() {
		AppUser mockedUser = new AppUser(0, "Mocked", "User", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_USER);
		when(mockRepo.save(mockedUser)).thenReturn(mockedUser);
		assertEquals(mockedUser, userService.addUser(mockedUser));
	}

	/**
	 * Tests behavior of UserService.addUser when passed a valid AppUser object
	 * whose provided username is already used within the data source. The expected
	 * result is for the method to throw a UserCreationException and that the
	 * UserRepository.save method is never invoked.
	 */
	@Test(expected = UserCreationException.class)
	public void testAddUserIfUsernameAlreadyExists() {
		AppUser mockedUser = new AppUser(0, "Mocked", "User", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_USER);
		when(mockRepo.findUserByUsername("mocked")).thenReturn(new AppUser());
		userService.addUser(mockedUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests behavior of UserService.addUser when passed a valid AppUser object
	 * whose provided email is already used within the data source. The expected
	 * result is for the method to throw a UserCreationException and that the
	 * UserRepository.save method is never invoked.
	 */
	@Test(expected = UserCreationException.class)
	public void testAddUserIfEmailAlreadyExists() {
		AppUser mockedUser = new AppUser(0, "Mocked", "User", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_USER);
		when(mockRepo.findUserByEmail("mocked@email.com")).thenReturn(new AppUser());
		userService.addUser(mockedUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests behavior of UserService.addUser when passed a invalid AppUser object.
	 * The expected result is for the method to throw a BadRequestException and that
	 * the UserRepository.save method is never invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testAddInvalidUser() {
		AppUser nullMockedUser = null;
		userService.addUser(nullMockedUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.updateUser method, whose
	 * function is to call UserRepository.save to persist an updated AppUser object
	 * from the data source using a provided id.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Tests the UserService.updateUser functionality when a null user object and
	 * null requester are provided. A BadRequestException is expected to be thrown,
	 * and it is verified that UserRepository.save is never invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testUpdateNullUserAndNullRequester() {
		AppUser nullMockUser = null;
		AppUser nullRequester = null;
		userService.updateUser(nullMockUser, nullRequester);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests the UserService.updateUser functionality when a user object with a null
	 * id and a null requester are provided. A BadRequestException is expected to be
	 * thrown, and it is verified that UserRepository.save is never invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testUpdateUserWithNullIdAndNullRequester() {
		AppUser invalidMockUser = new AppUser(null, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser nullRequester = null;
		userService.updateUser(invalidMockUser, nullRequester);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests the UserService.updateUser functionality when a user object with a null
	 * id and a valid requester are provided. A BadRequestException is expected to
	 * be thrown, and it is verified that UserRepository.save is never invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testUpdateUserWithNullIdAndValidRequester() {
		AppUser invalidMockUser = new AppUser(null, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser requestingUser = new AppUser(3, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		userService.updateUser(invalidMockUser, requestingUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests the UserService.updateUser functionality when a valid user object and
	 * null requester are provided. A BadRequestException is expected to be thrown,
	 * and it is verified that UserRepository.save is never invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testUpdateValidUserAndNullRequester() {
		AppUser mockUserForUpdate = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser nullRequester = null;
		userService.updateUser(mockUserForUpdate, nullRequester);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests the UserService.updateUser functionality when a valid user object and
	 * null requester are provided. A BadRequestException is expected to be thrown,
	 * and it is verified that UserRepository.save is never invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testUpdateNullUserAndValidRequester() {
		AppUser requestingUser = new AppUser(3, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser nullMockUser = null;
		userService.updateUser(nullMockUser, requestingUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests the UserService.updateUser functionality when a valid user object is
	 * provided but by an unauthorized requester. A SecurityException is expected to
	 * be thrown, and it is verified that UserRepository.save is never invoked.
	 */
	@Test(expected = SecurityException.class)
	public void testUpdateValidUserFromUnauthRequester() {
		AppUser requestingUser = new AppUser(3, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser mockUserForUpdate = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		userService.updateUser(mockUserForUpdate, requestingUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests the UserService.updateUser functionality when a valid user object is
	 * provided but by an unauthorized requester. A SecurityException is expected to
	 * be thrown, and it is verified that UserRepository.save is never invoked.
	 */
	@Test
	public void testUpdateValidUserFromAdminRequester() {
		AppUser requestingUser = new AppUser(3, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_ADMIN);
		AppUser persistedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser mockUserForUpdate = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findById(mockUserForUpdate.getId())).thenReturn(Optional.of(persistedUser));
		boolean actualResult = userService.updateUser(mockUserForUpdate, requestingUser);
		assertTrue(actualResult);
	}

	/**
	 * Tests the UserService.updateUser functionality when a valid user object is
	 * provided by an authorized requester. No username, email, or role updates are
	 * attempted. The expected result is for the method to invoke
	 * UserRepository.save and to return true.
	 */
	@Test
	public void testUpdateValidUserFromAuthRequester() {
		AppUser requestingUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser persistedUser = requestingUser;
		AppUser mockUserForUpdate = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findById(mockUserForUpdate.getId())).thenReturn(Optional.of(persistedUser));
		boolean actualResult = userService.updateUser(mockUserForUpdate, requestingUser);
		assertTrue(actualResult);
	}

	/**
	 * Tests UserService.updateUser when a valid user object is provided which is
	 * not attempting to update their username, email, or role.
	 */
	@Test
	public void testUpdateUserValidNotChangingUsernameEmailOrRole() {
		AppUser requestingUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser persistedUser = requestingUser;
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_USER);
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		assertTrue(userService.updateUser(validMockUser, requestingUser));
	}

	/**
	 * Tests UserService.updateUser when a valid user object is provided which
	 * attempting to update their username which is not taken
	 */
	@Test
	public void testUpdateUserValidChangingUsernameNotTaken() {
		AppUser requestingUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser persistedUser = requestingUser;
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "new-mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByUsername("new-mocked")).thenReturn(null);
		assertTrue(userService.updateUser(validMockUser, requestingUser));
	}

	/**
	 * Tests UserService.updateUser when a valid user object is provided which
	 * attempting to update their username which is taken
	 */
	@Test(expected = UserUpdateException.class)
	public void testUpdateUserValidChangingUsernameTaken() {
		AppUser requestingUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser persistedUser = requestingUser;
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "new-mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser userWithUsername = new AppUser(2, "Existing", "User", "existing@email.com", "existing", "pw",
				UserRole.ROLE_USER);

		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByUsername(validMockUser.getUsername())).thenReturn(userWithUsername);
		userService.updateUser(validMockUser, requestingUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests UserService.updateUser when a valid user object is provided which
	 * attempting to update their username which is not taken
	 */
	@Test
	public void testUpdateUserValidChangingEmailNotTaken() {
		AppUser requestingUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser persistedUser = requestingUser;
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "new-mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByEmail("new-mocked@email.com")).thenReturn(null);
		assertTrue(userService.updateUser(validMockUser, requestingUser));
	}

	/**
	 * Tests UserService.updateUser when a valid user object is provided which
	 * attempting to update their username which is taken
	 */
	@Test(expected = UserUpdateException.class)
	public void testUpdateUserValidChangingEmailTaken() {
		AppUser requestingUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser persistedUser = requestingUser;
		AppUser validMockUser = new AppUser(1, "Mocked", "User", "new-mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser userWithEmail = new AppUser(2, "Existing", "User", "new-mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		when(mockRepo.findById(validMockUser.getId())).thenReturn(Optional.of(persistedUser));
		when(mockRepo.findUserByEmail("new-mocked@email.com")).thenReturn(userWithEmail);
		userService.updateUser(validMockUser, requestingUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests behavior of UserService.updateUser when passed an AppUser object whose
	 * id does not match any records found in the data source. The requester must be
	 * a valid user with a role of ADMIN. The expected result is for the method to
	 * throw a UserUpdateException and that the UserRepository.save method is never
	 * invoked.
	 */
	@Test(expected = UserUpdateException.class)
	public void testUpdateUserWithAnUnknownId() {
		AppUser requestingUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_ADMIN);
		AppUser unknownMockedUser = new AppUser(9, "unknown", "mock", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		userService.updateUser(unknownMockedUser, requestingUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests behavior of UserService.updateUser when passed an AppUser object who is
	 * attempting to update their role, although the update role flag of this method
	 * is not set to true. The expected result is for the method to throw a
	 * UserUpdateException and that the UserRepository.save method is never invoked.
	 */
	@Test(expected = UserUpdateException.class)
	public void testUpdateUserRoleWithInvalidAuth() {
		AppUser requestingUser = new AppUser(9, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		AppUser persistedUser = requestingUser;
		AppUser mockedUser = new AppUser(9, "mocked", "mocked", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_ADMIN);
		when(mockRepo.findById(mockedUser.getId())).thenReturn(Optional.of(persistedUser));
		userService.updateUser(mockedUser, requestingUser);
		verify(mockRepo, times(0)).save(Mockito.any());
	}

	/**
	 * Tests behavior of UserService.updateUser when passed an AppUser object who is
	 * attempting to update their role, with the update role flag of this method set
	 * to true. The expected result is for the method call the UserRepository.save
	 * method to persist the updated user and return true.
	 */
	@Test
	public void testUpdateUserRoleWithValidAuth() {
		AppUser requestingUser = new AppUser(9, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_ADMIN);
		AppUser persistedUser = requestingUser;
		AppUser mockedUser = new AppUser(9, "updated", "mock", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_ADMIN);
		when(mockRepo.findById(mockedUser.getId())).thenReturn(Optional.of(persistedUser));
		boolean actualResult = userService.updateUser(mockedUser, requestingUser);
		assertTrue(actualResult);
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.deleteUserById method, whose
	 * function is to call UserRepository.delete to delete an AppUser object from
	 * the data source using a provided id.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * This test case verifies the proper functionality of the
	 * UserService.deleteUserById() method when a valid id is provided to it. The
	 * expected result is that the UserRepository.delete() method is called once and
	 * that the returned value is true.
	 */
	@Test
	public void testDeleteUserByIdValidId() {
		AppUser mockedUser = new AppUser(1, "Mocked", "User", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_USER);
		when(mockRepo.findById(1)).thenReturn(Optional.of(mockedUser));
		assertTrue(userService.deleteUserById(1));
	}

	/**
	 * This test case verifies the proper functionality of the
	 * UserService.deleteUserById() method when a invalid id is provided to it. The
	 * expected result is that a BadRequestException is thrown and that the
	 * UserRepository.delete() method is not called.
	 */
	@Test(expected = BadRequestException.class)
	public void testDeleteUserByIdInvalidId() {
		userService.deleteUserById(0);
		verify(mockRepo, times(0)).delete(Mockito.any());
	}

	/**
	 * This test case verifies the proper functionality of the
	 * UserService.deleteUserById() method when a valid id is provided to it, but
	 * not user is found. The expected result is that a UserNotFoundException is
	 * thrown.
	 */
	@Test(expected = UserNotFoundException.class)
	public void testDeleteUserByIdValidIdNotFound() {
		AppUser mockedUser = null;
		Optional<AppUser> mockedOptional = Optional.ofNullable(mockedUser);
		when(mockRepo.findById(1)).thenReturn(mockedOptional);

		userService.deleteUserById(1);
		verify(mockRepo, times(0)).delete(mockedUser);
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.isUsernameAvailable method,
	 * whose function is to determine whether or not a provided username value is
	 * available for use or not.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Tests the behavior of UserService.isUsernameAvailable when an available
	 * username is provided. The expected result is for the method to return true.
	 */
	@Test
	public void testIsUsernameAvailableWhenAvailable() {
		String availableUsername = "mocked";
		when(mockRepo.findUserByUsername(availableUsername)).thenReturn(null);
		boolean actualResult = userService.isUsernameAvailable(availableUsername);
		assertTrue(actualResult);
	}

	/**
	 * Tests the behavior of UserService.isUsernameAvailable when an unavailable
	 * username is provided. The expected result is for the method to return false.
	 */
	@Test
	public void testIsUsernameAvailableWhenNotAvailable() {
		AppUser persistedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		String unavailableUsername = "mocked";
		when(mockRepo.findUserByUsername(unavailableUsername)).thenReturn(persistedUser);
		boolean actualResult = userService.isUsernameAvailable(unavailableUsername);
		assertFalse(actualResult);
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.isEmailAddressAvailable method,
	 * whose function is to determine whether or not a provided email value is
	 * available for use or not.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Tests the behavior of UserService.isEmailAddressAvailable when an available
	 * email is provided. The expected result is for the method to return true.
	 */
	@Test
	public void testIsEmailAvailableWhenAvailable() {
		String availableEmail = "mocked@email.com";
		when(mockRepo.findUserByEmail(availableEmail)).thenReturn(null);
		boolean actualResult = userService.isEmailAddressAvailable(availableEmail);
		assertTrue(actualResult);
	}

	/**
	 * Tests the behavior of UserService.isEmailAddressAvailable when an unavailable
	 * email is provided. The expected result is for the method to return false.
	 */
	@Test
	public void testIsEmailAvailableWhenNotAvailable() {
		AppUser persistedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		String unavailableEmail = "mocked@email.com";
		when(mockRepo.findUserByUsername(unavailableEmail)).thenReturn(persistedUser);
		boolean actualResult = userService.isUsernameAvailable(unavailableEmail);
		assertFalse(actualResult);
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.validateFields method, whose
	 * function is to determine whether or not a given AppUser object is valid or
	 * not. A valid AppUser object is not null, has no null fields, and does not
	 * have fields with a empty string as their value.
	 * 
	 * Current coverage of method: 100%
	 * 
	 * Current branch coverage: 100%
	 */

	/**
	 * Tests the behavior of UserService.validateFields when a valid user is
	 * provided. Expected result is for the method to return true.
	 */
	@Test
	public void testValidateFieldsValidUser() {
		AppUser nullMockedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(nullMockedUser);
		assertTrue(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a null user is
	 * provided. Expected result is for the method to return false.
	 */
	@Test
	public void testValidateFieldsNullUser() {
		AppUser nullMockedUser = null;
		boolean actualResult = userService.validateFields(nullMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a invalid first name is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsInvalidFirstName() {
		AppUser invalidMockedUser = new AppUser(1, "", "mocked", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a null first name is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsNullFirstName() {
		AppUser invalidMockedUser = new AppUser(1, null, "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a invalid last name is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsInvalidLastName() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "", "mocked@email.com", "mocked", "mocked", UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a null last name is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsNullLastName() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", null, "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a invalid email is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsInvalidEmail() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", "", "mocked", "mocked", UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a null email is given
	 * in the provided user. Expected result is for the method to return false.
	 */
	@Test
	public void testValidateFieldsNullEmail() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", null, "mocked", "mocked", UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a invalid username is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsInvalidUsername() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "", "mocked", UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a null username is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsNullUsername() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", null, "mocked",
				UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a invalid password is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsInvalidPassword() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "", UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a null password is
	 * given in the provided user. Expected result is for the method to return
	 * false.
	 */
	@Test
	public void testValidateFieldsNullPassword() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", null,
				UserRole.ROLE_USER);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a invalid role is given
	 * in the provided user. Expected result is for the method to throw an
	 * IllegalArgumentException and return false.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateFieldsInvalidRole() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.valueOf(""));
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	/**
	 * Tests the behavior of UserService.validateFields when a null role is given in
	 * the provided user. Expected result is for the method to return false.
	 */
	@Test
	public void testValidateFieldsNullRole() {
		AppUser invalidMockedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked", null);
		boolean actualResult = userService.validateFields(invalidMockedUser);
		assertFalse(actualResult);
	}

	// --------------------------------------------------------------------------------------------------------------------

	/*
	 * Below are the unit tests for the UserService.loadUserByUsername method, whose
	 * function is to call UserRepository.findUserByUsername to fetch an AppUser
	 * object from the data source whose username matches the provided string value.
	 * The AppUser object is used with Spring Security to build a UserPrincipal
	 * object which is used as part of a end user's authentication request.
	 * 
	 * Current coverage of method: 0%
	 * 
	 * Current branch coverage: 0%
	 */

	/**
	 * Tests the behavior of UserService.loadUserByUsername when a invalid username
	 * is provided. The expected result is for the method to throw a
	 * BadRequestException and the UserRepository.loadUserByUsername method should
	 * not be invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testLoadUserByUsernameInvalidUsername() {
		String invalidUsername = "";
		userService.loadUserByUsername(invalidUsername);
		verify(mockRepo, times(0)).findUserByUsername(Mockito.anyString());
	}

	/**
	 * Tests the behavior of UserService.loadUserByUsername when a null username is
	 * provided. The expected result is for the method to throw a
	 * BadRequestException and the UserRepository.loadUserByUsername method should
	 * not be invoked.
	 */
	@Test(expected = BadRequestException.class)
	public void testLoadUserByUsernameNullUsername() {
		String nullUsername = null;
		userService.loadUserByUsername(nullUsername);
		verify(mockRepo, times(0)).findUserByUsername(Mockito.anyString());
	}

	/**
	 * Tests the behavior of UserService.loadUserByUsername when an unknown username
	 * is provided. The expected result is for the method to throw a
	 * UsernameNotFoundException (provided by Spring Security).
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameWithUnknownUsername() {
		String unknownUsername = "unknown";
		when(mockRepo.findUserByUsername(unknownUsername)).thenReturn(null);
		userService.loadUserByUsername(unknownUsername);
	}

	/**
	 * Tests the behavior of UserService.loadUserByUsername when an valid and known
	 * username is provided. The expected result is for the method to return a valid
	 * UserPrincipal object.
	 * 
	 * TODO use JMockit or PowerMock to provide support for mocking static methods
	 * 
	 * TODO current test implementation is not a true unit test due to inability to
	 * mock the AuthorityUtils.commaSeparatedStringToAuthorityList method call.
	 */
	@Test
	public void testLoadUserByUsernameWithValidKnownUsername() {
		AppUser retrievedUser = new AppUser(1, "mocked", "mocked", "mocked@email.com", "mocked", "mocked",
				UserRole.ROLE_USER);
		String username = "valid-known";
		when(mockRepo.findUserByUsername(username)).thenReturn(retrievedUser);
		when(mockScopeMapper.mapScopesBasedUponRole(retrievedUser.getRole())).thenReturn("fake, list, of, scopes");
		UserDetails result = userService.loadUserByUsername(username);
		assertNotNull(result);
	}

}
