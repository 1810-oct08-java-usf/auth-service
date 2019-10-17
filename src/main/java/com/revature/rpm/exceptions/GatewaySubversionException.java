package com.revature.rpm.exceptions;

import java.io.IOException;

public class GatewaySubversionException extends IOException {

	private static final long serialVersionUID = -1736687159994666796L;

	public GatewaySubversionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public GatewaySubversionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public GatewaySubversionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
