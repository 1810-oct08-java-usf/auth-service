package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import com.revature.models.AppUser;
import com.revature.models.UserCredentials;
import com.revature.models.UserErrorResponse;

/** 
 *  UserErrorResponse test suite
 *  
 * @author Alonzo Muncy (190107 Java-Spark-USF)
 *  
 */

@RunWith(MockitoJUnitRunner.class)
public class UserErrorResponseTest {
	//set up variables
	Integer teststatus = 38;
	String testmessage = "A Message";
	Long testtimestamp = 2402350928528509L;
	
	String testToString = "UserErrorResponse [status=38, message=A Message, timestamp=2402350928528509]";
	Integer testHashCode = 1653425810;
	/**
	 * Testing constructors, getters, setters, equals, hashCode, and toString.
	 * @author Alonzo Muncy
	 */
	@Test
	public void testUserErrorResponse() {
		
		UserErrorResponse testObject = new UserErrorResponse();
		UserErrorResponse nullObject = new UserErrorResponse();
		
		assertEquals(nullObject, testObject);
		
		testObject.setMessage(testmessage);
		testObject.setStatus(teststatus);
		testObject.setTimestamp(testtimestamp);
		
		UserErrorResponse compareObject = new UserErrorResponse(teststatus, testmessage, testtimestamp);
		
		assertEquals(compareObject, testObject);
		
		Integer testObjectHashCode = testObject.hashCode();
		String testObjectToString = testObject.toString();
		
		assertEquals(testHashCode, testObjectHashCode);
		assertEquals(testToString, testObjectToString);
	}
}
