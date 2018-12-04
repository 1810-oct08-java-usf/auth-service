package com.revature.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthServiceController {
	
	@GetMapping("/test")
	public String home() {
		return "HELLLLOOO!";
	}

}
