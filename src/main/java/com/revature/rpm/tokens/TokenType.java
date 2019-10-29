package com.revature.rpm.tokens;

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
