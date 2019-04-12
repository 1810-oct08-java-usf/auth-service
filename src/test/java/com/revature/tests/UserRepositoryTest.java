package com.revature.tests;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

/** Note: "You cannot test Spring Data" 
 * 	The implementation of Spring Data Methods are 
 * 	veiled from our view. 
 * 	@see <a href="https://stackoverflow.com/questions/23435937/how-to-test-spring-data-repositories#23442457">Discussed on StackOverflow</a>    
 *  @author Brandon Morris (190107-Java-Spark-usf)
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {
	
	/* The below code was found 
	 * with @Ignore labeled on 
	 * an empty test.
	 * Documentation was entirely lacking, 
	 * so the purpose of this class is presently unclear.
	 * TODO: Before this class is deleted, check that it does not impact
	 * code coverage metrics. 
	 */
	@Ignore
	@Test
	public void testFindUserByEmail() {
		
	}

	@Ignore
	@Test
	public void testFindUserByUsername() {
		
	}
	
	@Ignore
	@Test
	public void testFindUserByUsernameAndPassword() {
		
	}
}
