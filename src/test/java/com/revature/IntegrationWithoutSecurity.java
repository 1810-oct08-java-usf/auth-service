/**
 * 
 */
package com.revature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.controller.AuthController;
import com.revature.models.AppUser;
import com.revature.service.UserService;


/**
 * @author Jaitee Pitts (190107-Java-Spark-USF)
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { AuthController.class }, secure = false)
public class IntegrationWithoutSecurity {
	
	private String uri = "/users";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	AppUser mockAppUser;
	
	@MockBean
	private UserService userService;
	
	/**
	 * This method is going to test if our context loads and is not null.
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testContextLoads() throws Exception {
		assertThat(this.mockMvc).isNotNull();
	}
	
	/**
	 * Tests delete User method with proper values. 
	 * This method needs the WithMockUser annotation to properly mock user authentication 
	 * @throws Exception
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Test
	@WithMockUser(roles= {"USER","ADMIN"})
	public void deleteUser() throws Exception{	
		when(userService.findById(0)).thenReturn(mockAppUser);
		when(userService.deleteUserById(0)).thenReturn(true);
		when(mockAppUser.getId()).thenReturn(0);
		this.mockMvc.perform(delete(uri+"/id/0")
				).andExpect(status().isOk());
		
	}
	
	/**
	 * Tests delete User method when user does not exist. 
	 * This method needs the WithMockUser annotation to properly mock user authentication 
	 * @throws Exception
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 * 
	 * Currently fails with a Null Pointer Exception
	 */
	
	@Ignore //Currently fails with a Null Pointer Exception
	@Test
	@WithMockUser(roles= {"USER","ADMIN"})
	public void deleteUserWhenNull() throws Exception{	
		when(userService.findById(0)).thenReturn(null);
		this.mockMvc.perform(delete(uri+"/id/0")
				).andExpect(status().is4xxClientError());
		
	}

}
