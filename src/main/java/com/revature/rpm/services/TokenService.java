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
	
	public String extractGrantedScopesFromAccessToken(String accessToken) {
		
		checkForInvalidToken(accessToken);
		Claims tokenClaims = extractClaimsFromToken(accessToken);
		return (String) tokenClaims.get("scopes");
		
	}
	
	public Claims extractClaimsFromToken(String token) {
		
		checkForInvalidToken(token);
		return tokenParser.parseClaims(token);
		
	}
	
	public UserPrincipal refreshAccessToken(String refreshToken) {

		UserPrincipal principal = new UserPrincipal();
		
		checkForInvalidToken(refreshToken);
		Claims tokenClaims = extractClaimsFromToken(refreshToken);
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
	
	private void checkForInvalidToken(String token) {
		
		if (token == null || token.isEmpty()) {
			throw new MalformedJwtException("Invalid access token provided");
		}
		
	}
	
}
