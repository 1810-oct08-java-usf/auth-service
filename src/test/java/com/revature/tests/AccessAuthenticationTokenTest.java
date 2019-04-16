package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.security.AccessAuthenticationToken;

@RunWith(MockitoJUnitRunner.class)
public class AccessAuthenticationTokenTest {

	@Mock
	Object principal;

	@Mock
	Object credentials;

	@InjectMocks
	AccessAuthenticationToken authToken;
	
	/**
	 * This tests erasing credentials
	 * @Author Tracy Cummings (190107 Java-Spark-USF)
	 *
	 */

	@Test
	public void testEraseCredentials() {

		// Clears the credentials and sets it to null
		authToken.eraseCredentials();

		// Runs the test and checks if credentials is pointing at nothing
		assertEquals(null, authToken.getCredentials());
	}
}
