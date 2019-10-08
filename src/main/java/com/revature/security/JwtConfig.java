package com.revature.security;

import org.springframework.beans.factory.annotation.Value;

/**
 * Provides configuration which will be used during JWT creation upon successful authentication.
 * Attempts to make this class static were unsuccessful due to how Spring Security was
 * being used to set the values of the fields; getters were used to make the code work.
 * <br><br>
 * header: Indicates the name of the HTTP header that will contain the prefix and JWT as a value<br>
 * <br>
 * prefix: A prefix that will come before the JWT within the response header. Can be used to help distinguish this 
 * 		application's Authorization header token from similarly named headers/tokens from other applications.
 * <br>
 * expiration: Amount of time a token is valid, in milliseconds
 * <br>
 * secret: Used as a key with the encryption algorithm to generate JWTs
 * <br>
 * signingKey: create a signing key using the JWT secret key
 * 
 */
public class JwtConfig {

	@Value("${security.jwt.header:Authorization}")
	private String header;

	@Value("${security.jwt.prefix:Bearer }")
	private String prefix;

	@Value("${security.jwt.expiration:#{24*60*60*1000}}")
	private int expiration;

	@Value("${security.jwt.secret}")
	private String secret;

	public String getHeader() {
		return header;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getExpiration() {
		return expiration;
	}

	public String getSecret() {
		return secret;
	}

}