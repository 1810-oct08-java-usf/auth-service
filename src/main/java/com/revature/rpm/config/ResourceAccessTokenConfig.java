package com.revature.rpm.config;

import org.springframework.beans.factory.annotation.Value;

public class ResourceAccessTokenConfig {
	
	@Value("${rpm.security.auth.header:Authorization}")
	private String accessHeader;
	
	@Value("${rpm.security.auth.prefix:Bearer }")
	private String accessPrefix;
	
	@Value("${rpm.security.auth.secret:local-deploy}")
	private String accessSecret;
	
	@Value("${rpm.security.auth.expiration:#{24*60*60*1000}}")
	private String accessExpiration;

	public String getAccessHeader() {
		return accessHeader;
	}

	public String getAccessPrefix() {
		return accessPrefix;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public String getAccessExpiration() {
		return accessExpiration;
	}
	
}
