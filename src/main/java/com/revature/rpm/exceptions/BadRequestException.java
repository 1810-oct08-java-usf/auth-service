package com.revature.rpm.exceptions;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -8645119532144252077L;

	public BadRequestException(String message) {
		super(message);
	}

}
