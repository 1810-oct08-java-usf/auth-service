package com.revature.security;

import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.UserCredentials;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Filter used for authenticating a login request using the provided username and password. Upon 
 * successful authentication a JWT will be passed back to the client via a HTTP response header.
 */
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	/* Spring Security's AuthenticationManager which is used to validate the 
	 * user credentials
	 */
	private AuthenticationManager authManager;

	/*
	 * Used to provide configuration during the creation of the JWT
	 */
	private final JwtConfig jwtConfig;

	/**
	 * Constructor for the JwtUsernameAndPasswordAuthenticationFilter that
	 * instantiates the AuthenticationManager and the JwtConfig fields.
	 * 
	 * @param authManager
	 *      Used to process authentication requests.
	 * 
	 * @param jwtConfig
	 *		Provides the configuration for how JWT tokens are created/validated.
	 */
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;

		/*
		 * By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
		 * This application will use the "/auth" path instead. To do this, it is
		 * necessary to override the defaults using the URI defined within the JwtConfig
		 * class.
		 */
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
	}

	/**
	 * Attempts to authenticate a login request. Credentials are retrieved from the
	 * HTTP request body and stores them within a UserCredentials object. This is
	 * passed to the constructor of Spring Security's UsernamePasswordAuthenticationToken 
	 * class in order to generate an authentication token that will be authenticated
	 * by the AuthenticationManager.
	 * 
	 * @param request 
	 * 		Provides information regarding the HTTP request.
	 * 
	 * @param response 
	 * 		Provides information regarding the HTTP response.
	 * 
	 * @return Authentication 
	 * 		Represents the token for an authenticated principal
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

		try {

			// 1. Get credentials from request body
			UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

			/* 
			 * 2. Create an authentication token (contains user credentials) which will be
			 * used by the AuthenticationManager
			 */
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), Collections.emptyList());

			/*
			 * 3. Leverage AuthenticationManager to authenticate the user, and use
			 * UserDetailsServiceImpl::loadUserByUsername() method to load the user.
			 */
			return authManager.authenticate(authToken);

		} catch (IOException e) {

			/*
			 * TODO This should be refactored to log the failed authentication attempt,
			 * including the IP address of the requester.
			 */
			throw new RuntimeException(e);
		}
	}

	/**
	 * Upon a successful authentication, a token should be generated. The token is
	 * generated using the configuration found within the JwtConfig field.
	 * 
	 * @param request 
	 * 		Provides information regarding the HTTP request.
	 * 
	 * @param response 
	 * 		Provides information regarding the HTTP response.
	 * 
	 * @param chain 
	 * 		Used to pass the HTTP request and response objects to the next filter in the chain (unused here).
	 * 
	 * @param auth
	 *      Represents the principle user which was successfully authenticated.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		// The current time in milliseconds, to be used as part of the JWT creation
		Long now = System.currentTimeMillis();

		/*
		 * Build the JWT and store it within a string to be added, along with the
		 * prefix, to the response header
		 */
		String token = Jwts.builder()

				// Identifies the principal (authenticated user) that is the subject of the JWT
				.setSubject(auth.getName())

				/* 
				 * Convert to list of strings. This is important because it affects the way we
				 * get them back in the Gateway.
				 */
				.claim("authorities", auth.getAuthorities().stream()

						// map the list of GrantedAuthority objects to a list of representative strings
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))

				// Set when the token was issued
				.setIssuedAt(new Date(now))

				/*
				 * Set the expiration time for the token (24 hours), using the JwtConfig
				 * expiration value
				 */
				.setExpiration(new Date(now + jwtConfig.getExpiration() * 1000)) // in milliseconds

				/*
				 * Sign the JWT token using the HS512 algorithm, using the JwtConfig secret
				 * value
				 */
				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())

				// Builds the JWT and serializes it to a compact, URL-safe string
				.compact();

		// Add token to the response header
		response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
	}
}
