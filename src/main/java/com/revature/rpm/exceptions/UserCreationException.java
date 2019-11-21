package com.revature.rpm.exceptions;

/** Extends RuntimeException and allows for a custom exception to be thrown when creating a user. */
public class UserCreationException extends RuntimeException {

  private static final long serialVersionUID = -8767875927041927726L;

  /**
   * A constructor that creates a UserCreationException by passing a custom message to the parent's
   * appropriate constructor.
   *
   * @param message - A custom message to print with the exception.
   */
  public UserCreationException(String message) {
    super(message);
  }
}
