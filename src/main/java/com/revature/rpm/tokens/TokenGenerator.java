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

	private String tokenSecret;
	
	public TokenGenerator(String secret) {
		super();
		this.tokenSecret = secret;
	}
	
	public String generateToken(GenericTokenDetails tokenDetails) {

		SignatureAlgorithm sigAlg = null;
		
		switch (tokenDetails.getType()) {
		
		case ACCESS:
			logger.info("Generating access token for subject: " + tokenDetails.getSubject());
			sigAlg = SignatureAlgorithm.HS256;
			break;
		
		case REFRESH:
			logger.info("Generating refresh token for subject: " + tokenDetails.getSubject());
			sigAlg = SignatureAlgorithm.HS512;

		}
		Date creationTime = new Date(tokenDetails.getIat().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
		Date expirationTime = new Date(tokenDetails.getExp().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
		
		JwtBuilder tokenBuilder = Jwts.builder()
									  .setSubject(tokenDetails.getSubject())
									  .setIssuer(tokenDetails.getIss())
									  .setIssuedAt(creationTime)
									  .setExpiration(expirationTime)
								  	  .claim("scopes", tokenDetails.getClaims())
								  	  .signWith(sigAlg, tokenSecret.getBytes());
		
		logger.info("Successfully generated token for subject: " + tokenDetails.getSubject());

		return tokenBuilder.compact();
		
	}

}
