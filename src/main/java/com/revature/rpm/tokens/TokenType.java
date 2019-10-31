package com.revature.rpm.tokens;

/**
 * An enumeration that lists the known and accepted token types for requesters of the
 * RPM system.
 * 
 */
public enum TokenType {

	ACCESS("access"), REFRESH("refresh");

	private String tokenType;

	private TokenType(String type) {
		this.tokenType = type;
	}

	public String toString() {
		return tokenType;
	}

}
