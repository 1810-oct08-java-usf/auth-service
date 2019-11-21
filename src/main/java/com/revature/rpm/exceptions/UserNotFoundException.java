package com.revature.rpm.exceptions;

/**
 * Extends RuntimeException and allows for a custom exception to be thrown when checking for an
 * existing user.
 */
public class UserNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -1309146242244033937L;

  /**
   * A constructor that creates a UserNotFoundException by passing a custom message to the parent's
   * appropriate constructor.
   *
   * @param message - A custom message to print with the exception.
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
