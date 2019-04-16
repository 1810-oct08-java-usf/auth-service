package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import com.revature.models.AppUser;
import com.revature.models.UserCredentials;

/** 
 *  UserCredentials test suite
 *  
 * @author Alonzo Muncy (190107 Java-Spark-USF)
 *  
 */

@RunWith(MockitoJUnitRunner.class)
public class UserCredentialsTest {
	String testusername = "japple";
	String testpassword = "snapple";
	
	String testToString = "UserCredentials [username=japple, password=snapple]";
	Integer testHashCode = -882458894;
	
	@Test
	public void testAppUser() {
		
		UserCredentials testObject = new UserCredentials();
		UserCredentials nullObject = new UserCredentials();
		
		assertEquals(nullObject, testObject);
		
		testObject.setPassword(testpassword);
		testObject.setUsername(testusername);
		
		nullObject.setPassword(testpassword);
		nullObject.setUsername(testusername);
		
		assertEquals(nullObject, testObject);
		
		assertEquals(testObject.getPassword(), testpassword);
		assertEquals(testObject.getUsername(), testusername);
		
		Integer testObjectHashCode = testObject.hashCode();
		String testObjectToString = testObject.toString();
		
		assertEquals(testHashCode, testObjectHashCode);
		assertEquals(testToString, testObjectToString);;
	}
}
