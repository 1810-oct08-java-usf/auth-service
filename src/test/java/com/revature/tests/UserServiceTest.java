package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.models.AppUser;
import com.revature.repository.UserRepository;
import com.revature.service.UserService;



/** <h2> Test Suite for the UserService class. </h2> 
 *  May it please Zachary Marazita, the GitLord <br> and Jose Rivera,
 *  our Master of Scribes and Documentation. <br>  
 *  <strong> Special thanks </strong> to <strong> Alonzo Muncy </strong>
 *	for his helpful explanations regarding the Unit Testing process.   
 *  
 * @author Brandon Morris <br> (190107 Java-Spark-USF) <br> 2019-04-09
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
	
	/* When making a test suite,
	 *  1) Create Mock Dependencies 
	 *  2) Inject these into the class
	 *  	 that you are testing. 
	 */
	@InjectMocks
	UserService testUserService;
	
	
	//-------------------------------------------------------------------
	
	/* Tests Needed for findAllUsers() 
	 * 
	 */
	
	@Test
	public void testFindAllUsers() {
		fail("Not yet implemented");
	}

	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for findById()
	 */
	

	
	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}
	
	
	
	
	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for findUserByUsername()
	 */
	
	
	
	@Test
	public void testFindUserByUsername() {
		fail("Not yet implemented");
	}
	
	
	//-------------------------------------------------------------------
	
	/*
	 * Tests Needed for findUserByEmail
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
	 *  UserService mockUserService 
	 *  
	 */
	
	
	/**
	 * 
	 */
	@Test
	public void testAddUserIfUserIsNull() {
		when(mockAppUser.getUsername()).thenReturn("William");
		when(testUserService.findUserByUsername("William"))
		.thenReturn(null);
		
		when(mockAppUser.getEmail()).thenReturn("William@gmail.com");
		when(testUserService.findUserByEmail("William@gmail.com"))
		.thenReturn(null);
		
		when(mockUserRepo.save(mockAppUser)).thenReturn(mockAppUser);
		
		assertEquals(null, testUserService.addUser(mockAppUser));
		
	}
	
	
	/**	When the UserService's addUser() is invoked, 
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
	
	
	/** In the addUser(), null is returned 
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
