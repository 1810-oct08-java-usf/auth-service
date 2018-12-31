package com.revature.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.models.AppUser;
import com.revature.repository.UserRepository;
import com.revature.service.UserService;



/**
 * 
 * @author Phillip Pride
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTestSuite {
	
	// Do not mock the class you intend to test
	private UserService classUnderTest;
	
	/**
	 * A simulated UserRespository
	 */
	@Mock
	private UserRepository testRepo;
	
	/**
	 * A simulated List<Project>; this can also be accomplished using a spy.
	 */
	@Mock
	private List<AppUser> dummyList;
	
	/**
	 * A simulated User; holds data for the test methods to access during assertion.
	 */
	@Mock
	private AppUser dummyUser = new AppUser();
	
	@Before
	public void preTestInit() {			
		classUnderTest = new UserService(testRepo);
		
		// Define the behavior of dummyList
		dummyList.add(dummyUser);
		//Mockito.when(dummyList.add()).thenCallRealMethod(dummyUser);
		//*Mockito.when(dummyList.get(0)).thenReturn(dummyUser);
		
		// Define the relevant behaviors of testRepo 
		Mockito.when(testRepo.findUserByUsername("tchester")).thenReturn(dummyUser);
		Mockito.when(testRepo.findUserByEmail("tchester@revature.com")).thenReturn(dummyUser);
		
		//Define the relevant behaviors of dummyUser
		//*Mockito.when(dummyUser.getUsername()).thenReturn("tchester");
		//*Mockito.when(dummyUser.getEmail()).thenReturn("tchester@revature.com");
		//*Mockito.when(dummyUser.getRole()).thenReturn("admin");
		//Mockito.when(dummyUser.getId()).thenReturn(2);
	}
	
	/**
	 * Assertion to verify that findUserByUsername returns an AppUser instance
	 */
	// findUserbyUsername()
	@Test
	public void shouldReturnUserOnGoodUsernameSearch() {
		assertThat(classUnderTest.findUserByUsername("tchester")).isInstanceOf(AppUser.class);
	}
	
	/**
	 * Assertion to verify that findUserByUsername returns an AppUser instance
	 */
	// findUserbyUsername()
	@Test
	public void shouldReturnNullOnBadUsernameSearch() {
		assertThat(classUnderTest.findUserByUsername("test")).isNull();
	}
	
	/**
	 * Assertion should verify that method returns an AppUser instance
	 */
	// findUserByEmail()
	@Test
	public void shouldReturnUserOnGoodEmailSearch() {
		assertThat(classUnderTest.findUserByEmail("tchester@revature.com")).isInstanceOf(AppUser.class);
	}
	
	/**
	 * Assertion should verify that searching with a bad email returns null
	 */
	// findUserByEmail()
	@Test
	public void shouldReturnNullOnFailedEmailSearch() {
		assertThat(classUnderTest.findUserByEmail("test")).isNull();
	}
	
	/**
	 * Assert that findAllUsers returns a List
	 */
	// findAllUsers()
	@Test
	public void shouldReturnAList() {
		assertThat(classUnderTest.findAllUsers()).isInstanceOf(List.class);
	}
	
	/**
	 * Assert that updateUser should return a value of true when given a valid user
	 */
	// updateUser()
	@Test
	public void shouldReturnTrueOnValidUpdate() {
		assertThat(classUnderTest.updateUser(dummyUser)).isEqualTo(Boolean.TRUE);
	}
	
	/**
	 * Assert that updateUser should return a value of false when given an invalid user
	 */
	// updateUser()
	@Test
	public void shouldReturnFalseOnInvalidUpdate() {
		assertThat(classUnderTest.updateUser(null)).isEqualTo(Boolean.FALSE);
	}
	
	/**
	 * Assertion should verify that method returns a AppUser instance
	 */
	/*@Test
	public void shouldReturnUserOnGoodIdSearch() {
		dummyUser.setId(2);
		classUnderTest.addUser(dummyUser);
		assertThat(classUnderTest.findById(2)).isInstanceOf(AppUser.class);
	}*/
	
	// TODO: Add additional test methods to improve coverage of the test suite as needed.
}

