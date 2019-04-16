package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import com.revature.models.AppUser;

/** 
 *  AppUser test suite
 *  
 * @author Alonzo Muncy (190107 Java-Spark-USF)
 *  
 */

@RunWith(MockitoJUnitRunner.class)
public class AppUserTest {
	//Set up variables
	Integer testid = 91;
	String testfirstName = "Johnny";
	String testlastName = "Appleseed";
	String testemail = "AppleTrees@GoWest.com";
	String testusername = "japple";
	String testpassword = "ILikeApples";
	String testrole = "ADMIN";
	String testToString = "AppUser [id=91, firstName=Johnny, lastName=Appleseed, email=AppleTrees@GoWest.com, username=japple, password=ILikeApples, role=ADMIN]";
	Integer testHashCode = -775241502;
	
	/**
	 * Testing constructors, getters, setters, equals, hashCode, and toString.
	 * @Author Alonzo Muncy 
	 */
	@Test
	public void testAppUser() {
		
		AppUser testObject = new AppUser();
		AppUser nullObject = new AppUser();
		
		assertEquals(nullObject, testObject);
		
		testObject.setEmail(testemail);
		testObject.setFirstName(testfirstName);
		testObject.setId(testid);
		testObject.setLastName(testlastName);
		testObject.setPassword(testpassword);
		testObject.setRole(testrole);
		testObject.setUsername(testusername);
		
		assertEquals(testObject.getEmail(),testemail);
		assertEquals(testObject.getFirstName(),testfirstName);
		assertEquals(testObject.getId(),testid);
		assertEquals(testObject.getLastName(),testlastName);
		assertEquals(testObject.getPassword(),testpassword);
		assertEquals(testObject.getRole(),testrole);
		assertEquals(testObject.getUsername(),testusername);
		
		AppUser compareObject = new AppUser(testid, testfirstName, testlastName, testemail, testusername, testpassword, testrole);
		
		assertTrue(testObject.equals(compareObject));
		
		Integer testObjectHashCode = testObject.hashCode();
		String testObjectToString = testObject.toString();
		
		assertEquals(testHashCode, testObjectHashCode);
		assertEquals(testToString, testObjectToString);
	}
	
}
