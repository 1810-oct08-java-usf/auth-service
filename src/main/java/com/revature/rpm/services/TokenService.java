package com.revature.rpm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.entities.AppUser;
import com.revature.rpm.entities.UserRole;
import com.revature.rpm.tokens.GenericTokenDetails;
import com.revature.rpm.tokens.TokenGenerator;
import com.revature.rpm.tokens.TokenParser;
import com.revature.rpm.tokens.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;

/**
 * Serves as the primary service class for all token-related operations.
 *
 */
@Service
public class TokenService {

	private TokenParser tokenParser;
	private TokenGenerator tokenGenerator;
	private UserService userService;
	
	@Autowired
	public TokenService(TokenParser parser, TokenGenerator generator, UserService service) {
		this.tokenParser = parser;
		this.tokenGenerator = generator;
		this.userService = service;
	}
	
	/**
	 * Parses and validates the provided access token to provide a simple
	 * comma-separated list of granted scopes.
	 * 
	 * @param accessToken
	 * 
	 * @return a comma-separated list of granted scopes
	 */
	public String extractGrantedScopesFromToken(String token) {
		
		token = removePrefixIfPresent(token);
		checkForInvalidToken(token);
		
		Claims tokenClaims = extractClaimsFromToken(TokenType.ACCESS, token);
		return (String) tokenClaims.get("scopes");
		
	}
	
	/**
	 * Simply parses and validates the provided token for claims.
	 * 
	 * @param token
	 * @return encoded claims found within the provided token
	 */
	public Claims extractClaimsFromToken(TokenType type, String token) {
		
		checkForInvalidToken(token);
		token = removePrefixIfPresent(token);
		return tokenParser.parseClaims(type, token);
		
	}
	
	
	/**
	 * Parses and validates the provided refresh token in order to generate a new
	 * access token, which is included within both the associated response header as
	 * well as the response body. Note that the refresh token is not included within
	 * the response payload.
	 * 
	 * @param refreshToken
	 * 
	 * @return UserPrincipal containing the access token and related information
	 */
	public UserPrincipal refreshAccessToken(String refreshToken) {

		UserPrincipal principal = new UserPrincipal();
		
		checkForInvalidToken(refreshToken);
		refreshToken = removePrefixIfPresent(refreshToken);
		
		Claims tokenClaims = extractClaimsFromToken(TokenType.REFRESH, refreshToken);
		String grantedScopes = (String) tokenClaims.get("scopes");
		
		String subject = tokenClaims.getSubject();
		AppUser refreshSubject = userService.findUserByUsername(subject);
		
		if (refreshSubject == null || refreshSubject.getRole().equals(UserRole.ROLE_LOCKED)) {
			throw new SecurityException("Illegal token provided");
		}
		
		GenericTokenDetails tokenConfig = new GenericTokenDetails(TokenType.ACCESS, "Revature", subject);
		String accessToken = tokenGenerator.generateToken(tokenConfig);
		
		principal.setUsername(subject);
		principal.setGrantedScopes(grantedScopes);
		principal.setAccessToken(accessToken);
		principal.setAccessTokenCreatedAt(tokenConfig.getIat().toString());
		principal.setAccessTokenExpiresAt(tokenConfig.getExp().toString());
		
		return principal;
		
	}
	
	/**
	 * Simple check for an obviously invalid token (null or empty string)
	 * 
	 * @param token
	 * 
	 * @throws MalformedJwtException
	 */
	private void checkForInvalidToken(String token) {
		
		if (token == null || token.isEmpty()) {
			throw new MalformedJwtException("Invalid access token provided");
		}
		
	}
	
	private String removePrefixIfPresent(String token) {
		
		if (token.startsWith("Bearer ")) {
			token = token.replaceAll("Bearer ", "");
		}
		
		return token;
	}
	
}
