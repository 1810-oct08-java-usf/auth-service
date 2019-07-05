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
	 * JwtUsernameAndPasswordAuthenticationFilter.successfulAuthentication() An
	 * Authentication object from Spring Security is passed in to make this
	 * previously designed code to behave the same way.
	 * 
	 * 
	 * Build the JWT and store it within a string to be added, along with the
	 * prefix, to the response header Identifies the principal (authenticated user)
	 * that is the subject of the JWT Convert claim to list of strings. This is
	 * important because it affects the way we get them back in the Gateway. map the
	 * list of GrantedAuthority objects to a list of representative strings compact
	 * builds the JWT and serializes it to a compact, URL-safe string
	 * 
	 * 
	 * 
	 * 
	 * @param auth
	 * @return
	 */

	public String createJwt(Authentication auth, JwtConfig jwtConfig) {
		System.out.println("creating new JWT for: " + auth.getName());

		SignatureAlgorithm sigAlg = SignatureAlgorithm.HS512;
		long nowMillis = System.currentTimeMillis();

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
