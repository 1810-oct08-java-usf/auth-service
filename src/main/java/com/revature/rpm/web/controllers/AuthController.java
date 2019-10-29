package com.revature.rpm.web.controllers;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.rpm.dtos.UserPrincipal;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@GetMapping(produces = "text/plain")
	public String getGrantedScopes(@CookieValue("access_token") String accessToken) {
		return "";
	}
	
	@PostMapping(produces = "application/json")
	public UserPrincipal refreshAccessToken(@CookieValue("refresh_token") String refreshToken) {
		return null;
	}

}
