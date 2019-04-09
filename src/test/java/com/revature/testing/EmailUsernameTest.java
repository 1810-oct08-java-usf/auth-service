package com.revature.testing;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Filter;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.service.UserService;

/**
 * Class containing MockMVC tests for the email and username validation methods.
 * 
 * @author Christopher Shanor [190107-Java-Spark-USF]
 * @author Jose Rivera [190107-Java-Spark-USF]
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmailUsernameTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private Filter filter;
	
	@MockBean
	private UserService userService;

	/**
	 * This method is going to test that our context loads and is not null
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * @author Christopher Shanor [190107-Java-Spark-USF]
	 * @author Jose Rivera [190107-Java-Spark-USF]
	 */
//	@Test
//	public void testContextLoads() throws Exception {
//		assertThat(this.mockMvc).isNotNull();
//	}
}
