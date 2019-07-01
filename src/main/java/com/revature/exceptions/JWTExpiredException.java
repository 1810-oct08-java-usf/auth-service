package com.revature.exceptions;

public class JWTExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JWTExpiredException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public JWTExpiredException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public JWTExpiredException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
