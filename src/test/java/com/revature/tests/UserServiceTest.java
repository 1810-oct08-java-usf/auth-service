package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.models.AppUser;
import com.revature.repository.UserRepository;
import com.revature.service.UserService;



/** Test Suite for the UserService class. 
 *  May it please Zachary Marazita, the GitLord and Jose Rivera,
 *  our Master of Documentation.  
 *  Special thanks to Alonzo Muncy
 *	for his helpful explanations regarding the Unit Testing process.   
 *  
 * @author Brandon Morris (190107 Java-Spark-USF) 2019-04-09
 *  
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	/*
	 * Required Mocks for this class are:
	 * 	UserRepository
	 */
	
	@Mock 
	UserRepository mockUserRepo;  
	
	@Mock
	AppUser mockAppUser;

	@Mock
	List<AppUser> mockUserList;
	
	
	/*	NOTICE: 
	 * 	this version of Mockito does not allow
	 * 	for mocking of final classes such as the 
	 * 	Optional<AppUser> mockOptUser;
	 * 
	 */	
	
	
	
	/* When making a test suite,
	 *  1) Create Mock Dependencies 
	 *  2) Inject these into the class
	 *  	 that you are testing. 
	 */
	@InjectMocks
	UserService testUserService;
	
	
	//-------------------------------------------------------------------
	
	/* Tests Needed for findAllUsers() 
	 * 1) Simple check for the proper calling of the repo.findAll()
	 *  
	 * Mocks
	 * List<AppUser> userList;
	 */
	
	/** Simple test for UserService.findAllUsers() 
	 * 	Test that the repo method findAll() is called and
	 * 	returns a list of users.  	
	 */
	@Test
	public void testFindAllUsers() {
		when(mockUserRepo.findAll()).thenReturn(mockUserList);
		assertEquals(testUserService.findAllUsers(), mockUserList);
	}

	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for findById()
	 * 	1) If id found in db
	 * 	2) If id not found in db
	 *  
	 *  Mocks for findById() Optional<AppUser> optUser;
	 *  
	 */
	

	/** TODO: Not Testable with this version Mockito!
	 * 	If we want to test this method, we need to
	 * 	create a wrapper class for the Optional
	 * 	because the version of Mockito presently in use
	 * 	does not support mocking of Final Classes 
	 * 	such as Optional. 
	 * 
	 *  Tests the findById() in UserService
	 *  when the id is found in the database. 
	 *  The following is how we can test this method in the future. 
	 * 	when(mockUserRepo.findById(47)).thenReturn(mockUser);
	 *	when(mockOptUser.isPresent()).thenReturn(true); 
	 * 	when(mockOptUser.get()).thenReturn(mockAppUser);
	 * 	assertEquals(mockAppUser, testUserService.findById(47));
	 */
	@Test
	public void testFindByIdIfIdFound() {
		
		fail("Revisit this with either a new version of Mockito "
				+ "(ie. Mockito 2) or create a wrapper class for "
				+ "the final class named Optional.class. /n");
	}
	
	
	/**  
	 *  TODO:  Not Testable with this version Mockito!
	 * 	If we want to test this method, we need to
	 * 	create a wrapper class for the Optional
	 * 	because the version of Mockito presently in use
	 * 	does not support mocking of Final Classes 
	 * 	such as Optional. 
	 * 
	 * 	Tests the findById() in UserService
	 *  when the id is not found in the database. 
	 */
	@Test
	public void testFindByIdIfIdNotFound() {
		fail("Cannot Mock Final Classes in this version of Mockito.");
	}
	
	
	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for findUserByUsername()
	 * 1) If the username is found. 
	 * 2) If the username is not found. 
	 * 
	 */
	
	
	/**
	 * Test for UserService.findUserByUsername()
	 * When the given username is found in the database. 
	 */
	@Test
	public void testFindUserByUsernameIfUsernameIsFound() {
		when(mockUserRepo.findUserByUsername("wShatner")).thenReturn(mockAppUser);
		assertEquals(mockAppUser, testUserService.findUserByUsername("wShatner"));
	}
	
	/**
	 * Test for UserSErvice.findUserByUsername() 
	 * When the given username is not found in the database. 
	 */
	@Test
	public void testFindUserByUsernameIfUsernameIsNotFound() {
		when(mockUserRepo.findUserByUsername("wShatner")).thenReturn(null);
		assertEquals(null, testUserService.findUserByUsername("wShatner"));
	}
	
	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for findUserByEmail
	 * 1) Found in database
	 * 2) Not Found in database
	 * 3) Invoked with null
	 * 
	 */
	
	/**
	 * 
	 */
	@Test
	public void testFindUserByEmail() {
		fail("Not yet implemented");
	}
	
	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for addUser()
	 *  testAddUserIfNewUserIsNull()
	 *  testAddUserIfUserAlreadyExists()
	 *  testAddUserIfEmailAlreadyExists()
	 *  
	 *  Three conditions to test for: 
	 *  1) if tempUser is null after findUserByUsername() is invoked. 
	 *  2) if tempUser is null after findUserByEmail() is invoked. 
	 *  3) normal behavior. 
	 * 		Ie. test that addUser() will 
	 *  	return the result of repo.save(newUser)
	 *    
	 *  Mocks for addUser(): 
	 *  AppUser mockAppUser;
	 *    
	 */
	
	
	/**
	 * 	Tests the addUser() of the UserService
	 * 	when the given user does not already exists
	 * 	in the database. 
	 * 
	 */
	@Test
	public void testAddUserIfUserNotInDatabase() {
		when(mockAppUser.getUsername()).thenReturn("William");
		when(testUserService.findUserByUsername("William"))
		.thenReturn(null);
		
		when(mockAppUser.getEmail()).thenReturn("William@gmail.com");
		when(testUserService.findUserByEmail("William@gmail.com"))
		.thenReturn(null);
		
		when(mockUserRepo.save(mockAppUser)).thenReturn(mockAppUser);
		
		assertEquals(mockAppUser, testUserService.addUser(mockAppUser));
		
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
		when(testUserService.findUserByUsername("William"))
		.thenReturn(mockAppUser);
		when(mockAppUser.getUsername()).thenReturn("William"); 
		
		assertEquals(null, testUserService.addUser(mockAppUser));
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
		when(mockAppUser.getUsername()).thenReturn("William");
		when(testUserService.findUserByUsername("William"))
		.thenReturn(mockAppUser);
		
		when(mockAppUser.getEmail()).thenReturn("William@gmail.com");
		when(testUserService.findUserByEmail("William@gmail.com"))
		.thenReturn(mockAppUser);
		
		
		assertEquals(null, testUserService.addUser(mockAppUser));
	}
	
	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for updateUser()
	 */
	
	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}
	
	
	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for DeleteUserById()
	 */
	
	
	@Test
	public void testDeleteUserById() {
		fail("Not yet implemented");
	}

}
