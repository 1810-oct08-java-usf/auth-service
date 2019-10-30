package com.revature.rpm.tokens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

public class TokenParser {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String accessSecret;
	private String refreshSecret;
	
	public TokenParser(String accessSecret, String refreshSecret) {
		super();
		this.accessSecret = accessSecret;
		this.refreshSecret = refreshSecret;
	}
	
	public Claims parseClaims(TokenType type, String token) {
	
		logger.info("Parsing token for resource access claims");
		
		JwtParser tokenParser = Jwts.parser();
		
		switch (type) {
		
		case ACCESS:
			tokenParser.setSigningKey(accessSecret.getBytes());
			break;
			
		case REFRESH:
			tokenParser.setSigningKey(refreshSecret.getBytes());
			
		}
	   
		return tokenParser.parseClaimsJws(token).getBody();

	}
}
