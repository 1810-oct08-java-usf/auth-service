package com.revature.rpm.exceptions;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1309146242244033937L;

	public UserNotFoundException(String message) {
		super(message);
	}

}
