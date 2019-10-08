package com.revature.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.UserCredentials;
import com.revature.models.UserPrincipal;

/**
 * Filter used for authenticating a login request using the provided username and password. Upon 
 * successful authentication a JWT will be passed back to the client via a HTTP response header.
 */
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
	
	/** Spring Security's AuthenticationManager which is used to validate the 
	 * user credentials
	 */
	private AuthenticationManager authManager;
	/**
	 * Class for determining properties of generated JWTs.
	 */
	private JwtConfig jwtConfig;

	/**
	 * Constructor for the JwtUsernameAndPasswordAuthenticationFilter that
	 * instantiates the AuthenticationManager and the JwtConfig fields.
	 * <br><br>
	 * The default endpoint is being leveraged. All authentication (login)
	 * requests should be POST requests made to /login
	 * 
	 * @param authManager
	 *      Used to process authentication requests.
	 * 
	 * @param jwtConfig
	 *		Provides the configuration for how JWT tokens are created/validated.
	 */
	public AuthFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
	}

	/**
	 * Attempts to authenticate a login request. Credentials are retrieved from the
	 * HTTP request body and stores them within a UserCredentials object. This is
	 * passed to the constructor of Spring Security's UsernamePasswordAuthenticationToken 
	 * class in order to generate an authentication token that will be authenticated
	 * by the AuthenticationManager.
	 * <br><br>
	 * Tries to:<br>
	 * 1. Get credentials from request body.<br>
	 * 2. Create an authentication token (contains user credentials) which will be used by the AuthenticationManager.<br>
	 * 3. Leverage AuthenticationManager to authenticate the user.
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
			
			UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), Collections.emptyList());
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
	 * generated from the JwtGenerator using the configuration found within the JwtConfig field.
	 * After a token is generated, add AppUser object as JSON to the response body upon 
	 * successful login by getting the print writer of HttpServletResponse.
	 * Token is then added to the response header from JwtConfig with corresponding
	 * prefix. 
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

		String token = JwtGenerator.createJwt(auth,jwtConfig);
		response.getWriter().write(new ObjectMapper().writeValueAsString(((UserPrincipal)auth.getPrincipal()).getAppUser()));
		response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
	}	
	
}
