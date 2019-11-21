package com.revature.rpm.exceptions;

/**
 * Extends RuntimeException and allows for a custom exception to be thrown when updating an existing
 * user.
 */
public class UserUpdateException extends RuntimeException {

  private static final long serialVersionUID = -1759991348700306860L;

  /**
   * A constructor that creates a UserUpdateException by passing a custom message to the parent's
   * appropriate constructor.
   *
   * @param message - A custom message to print with the exception.
   */
  public UserUpdateException(String message) {
    super(message);
  }
}
