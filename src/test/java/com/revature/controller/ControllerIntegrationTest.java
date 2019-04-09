package com.revature.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.service.UserService;

/**
 * This method tests integration of the controller with MockMVC
 * 
 * @author Jaitee Pitts
 * @author Ankit Patel
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class ControllerIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@Test
	public void deleteUser() throws Exception{
		this.mvc.perform(delete("/id/0")).andExpect(status().isOk());
	}
	
	
	

}
