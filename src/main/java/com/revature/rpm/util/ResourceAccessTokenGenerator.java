package com.revature.rpm.util;

import java.sql.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class ResourceAccessTokenGenerator {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Method used for generating a JWT based upon an authentication object. 
	 * Creates a token which encodes the following: 
	 * <br><br>
	 * - The username of the subject<br>
	 * - The issuer of the token<br>
	 * - The authority claims of the user based upon their role<br>
	 * - The time that the issued was issued<br>
	 * - The time which the token expires<br>
	 * 
	 * 
	 * @param auth 
	 * 		Authentication object from Spring Security
	 * 
	 * @param jwtConfig 
	 * 		configures the settings for the JWT's creation.
	 * 
	 * @return String
	 * 		the JWT (no prefix present)
	 */

	public String createJwt(Authentication auth, long expiration, String secret) {
		
		logger.info("Generating resource access token for principal: " + auth.getName());

		SignatureAlgorithm sigAlg = SignatureAlgorithm.HS512;
		long nowMillis = System.currentTimeMillis();

		JwtBuilder builder = Jwts.builder()
				.setSubject(auth.getName())
				.setIssuer("revature")
				.claim("authorities", auth.getAuthorities().stream()
														   .map(GrantedAuthority::getAuthority)
														   .collect(Collectors.toList()))
				.setIssuedAt(new Date(nowMillis))
				.setExpiration(new Date(nowMillis + expiration))
				.signWith(sigAlg, secret.getBytes());

		logger.info("Successfully generated resource access token for principal: " + auth.getName());

		return builder.compact();
	}

}
