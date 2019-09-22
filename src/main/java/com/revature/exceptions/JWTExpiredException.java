package com.revature.exceptions;

public class JWTExpiredException extends RuntimeException {

    /**
     * This class, as the name implies, is to provide logic that is
     * executed in the event of an expired JWT. It returns HTTP Status
     * code 401 (Unauthorized) to the ng-ui which in turn results in
     * the purge of the expired JWT and a reroute to the Login page.
     *
     * @author Ian Baker (1904-Apr22-Java-USF)
     */

    private static final long serialVersionUID = 1L;

    //TODO Add implementation to return a 401 when this exception is thrown

    public JWTExpiredException(String message, Throwable cause) {
        super(message, cause);

    }

    public JWTExpiredException(String message) {
        super(message);

    }

    public JWTExpiredException(Throwable cause) {
        super(cause);

    }

}