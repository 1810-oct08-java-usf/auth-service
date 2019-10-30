package com.revature.rpm.tokens;

import java.time.ZoneOffset;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenGenerator {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String accessSecret;
	private String refreshSecret;
	
	public TokenGenerator(String accessSecret, String refreshSecret) {
		super();
		this.accessSecret = accessSecret;
		this.refreshSecret = refreshSecret;
	}
	
	public String generateToken(GenericTokenDetails tokenDetails) {

		Date creationTime = new Date(tokenDetails.getIat().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
		Date expirationTime = new Date(tokenDetails.getExp().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
		
		JwtBuilder tokenBuilder = Jwts.builder()
				  .setSubject(tokenDetails.getSubject())
				  .setIssuer(tokenDetails.getIss())
				  .setIssuedAt(creationTime)
				  .setExpiration(expirationTime)
				  .claim("scopes", tokenDetails.getClaims());
		
		switch (tokenDetails.getType()) {
		
		case ACCESS:
			logger.info("Generating access token for subject: " + tokenDetails.getSubject());
			tokenBuilder.signWith(SignatureAlgorithm.HS256, accessSecret.getBytes());
			break;
		
		case REFRESH:
			logger.info("Generating refresh token for subject: " + tokenDetails.getSubject());
			tokenBuilder.signWith(SignatureAlgorithm.HS512, refreshSecret.getBytes());
		
		}
		
		logger.info("Successfully generated token for subject: " + tokenDetails.getSubject());

		return tokenBuilder.compact();
		
	}

}
