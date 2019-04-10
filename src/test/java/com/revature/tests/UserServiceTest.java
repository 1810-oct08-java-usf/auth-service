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
 *  May it please Zack, the GitLord <br> and Jose,
 *  our Master of Scribes and Documentation. 
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
	
	@Test
	public void testAddUserIfUsernameAlreadyExists() {
		when(testUserService.findUserByUsername("William"))
		.thenReturn(mockAppUser);
		when(mockAppUser.getUsername()).thenReturn("William"); 
		
		assertEquals(null, testUserService.addUser(mockAppUser));
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testAddUserIfEmailAlreadyExists() {
		when(mockAppUser.getUsername()).thenReturn("William");
		when(testUserService.findUserByUsername("William"))
		.thenReturn(null);
		
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
