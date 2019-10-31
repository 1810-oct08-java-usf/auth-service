package com.revature.rpm.tokens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

/**
 * Serves as a utility object whose purpose is to parse provided tokens for
 * claims.
 * 
 */
public class TokenParser {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String accessSecret;
	private String refreshSecret;

	public TokenParser(String accessSecret, String refreshSecret) {
		super();
		this.accessSecret = accessSecret;
		this.refreshSecret = refreshSecret;
	}

	/**
	 * Attempts to parse the provided token based upon the specified TokenType.
	 * 
	 * @param type of token being parsed (either access or refresh)
	 * 
	 * @param token for parsing
	 * 
	 * @return the Claims which were specified within the token
	 * 
	 */
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
