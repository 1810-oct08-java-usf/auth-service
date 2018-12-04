package com.revature.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * TODO Implement registration functionality/endpoints
 */
@RestController
@RequestMapping("/auth/register")
public class AuthServiceController {
	
	@GetMapping("/test")
	public String home() {
		return "HELLLLOOO!";
	}

}
