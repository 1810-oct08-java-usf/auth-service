package com.revature.security;

import java.sql.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtGenerator {

	/**
	 * Method used for generating a JWT based on code gutted from
	 * JwtUsernameAndPasswordAuthenticationFilter.successfulAuthentication().
	 * Authentication object from Spring Security is passed in to make this
	 * previously designed code behave the same way it did.
	 * Similarly, a JwtConfig object is passed in to guarantee correct behavior.
	 * 
	 * @param auth 
	 * 		Authentication object from Spring Security
	 * @param jwtConfig 
	 * 		configures the settings for the JWT's creation.
	 * @return String
	 * 		contains JWT info (without prefix)
	 * @author Daniel Shaffer (190422-USF-Java)
	 */

	public static String createJwt(Authentication auth, JwtConfig jwtConfig) {
		System.out.println("creating new JWT for: " + auth.getName());

		SignatureAlgorithm sigAlg = SignatureAlgorithm.HS512;
		long nowMillis = System.currentTimeMillis();

		//Converts info in .claim() to list of strings; IMPORTANT: affects the way we get them back in the Gateway
		JwtBuilder builder = Jwts.builder()
				.setSubject(auth.getName())
				.setIssuer("revature")
				.claim("authorities",auth.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(nowMillis))
				.setExpiration(new Date(nowMillis + jwtConfig.getExpiration()))
				.signWith(sigAlg, jwtConfig.getSecret().getBytes());

		System.out.println("JWT successfully created");

		return builder.compact();
	}

	private JwtGenerator() {
		super();
	}

}
