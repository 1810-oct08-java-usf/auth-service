package com.revature.rpm.tokens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class TokenParser {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String tokenSecret;
	
	public TokenParser(String secret) {
		this.tokenSecret = secret;
	}
	
	public Claims parseClaims(String token) {
	
		logger.info("Parsing token for resource access claims");
		
		return Jwts.parser()
				   .setSigningKey(tokenSecret.getBytes())
				   .parseClaimsJws(token)
				   .getBody();

	}
}
