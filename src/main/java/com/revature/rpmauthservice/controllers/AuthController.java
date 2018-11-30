package com.revature.rpmauthservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@RequestMapping("/")
	public String home() {
		return "best string";
	}
}
