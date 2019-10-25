package com.revature.rpm.exceptions;

/**
 * Extends RuntimeException and allows for a custom exception to be thrown when an attempt to
 * subvert the gateway is detected.
 */
public class GatewaySubversionException extends RuntimeException {

  private static final long serialVersionUID = -1736687159994666796L;

  /**
   * A constructor that creates a GatewaySubversionException by passing a custom message to the
   * parent's appropriate constructor.
   *
   * @param message - A custom message to print with the exception.
   */
  public GatewaySubversionException(String message) {
    super(message);
  }
}
