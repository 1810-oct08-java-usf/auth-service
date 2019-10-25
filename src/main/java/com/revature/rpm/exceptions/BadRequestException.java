package com.revature.rpm.exceptions;

/**
 * Extends RuntimeException and allows for a custom exception to be thrown when a bad request is
 * issued.
 */
public class BadRequestException extends RuntimeException {

  private static final long serialVersionUID = -8645119532144252077L;

  /**
   * A constructor that creates a BadRequestException by passing a custom message to the parent's
   * appropriate constructor.
   *
   * @param message - A custom message to print with the exception.
   */
  public BadRequestException(String message) {
    super(message);
  }
}
