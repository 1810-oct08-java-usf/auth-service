package com.revature.rpm.dtos;

import java.io.Serializable;
import java.util.Objects;

/**
 * Stores a status, message, and timestamp so they can be transfered back as a single unit upon an
 * exception being thrown.
 */
public class UserErrorResponse implements Serializable {

  private static final long serialVersionUID = -780898122370684787L;

  private int status;
  private String message;
  private long timestamp;

  public UserErrorResponse() {}

  /**
   * Constructor for UserErrorResponse that takes in a status, message and timestamp.
   *
   * @param status - Status to assign.
   * @param message - Message to assign.
   * @param timestamp - Timestamp to assign.
   */
  public UserErrorResponse(int status, String message, long timestamp) {
    super();
    this.status = status;
    this.message = message;
    this.timestamp = timestamp;
  }

  /**
   * status getter method.
   *
   * @return status associated with this instance of UserErrorRespponse.
   */
  public int getStatus() {
    return status;
  }

  /**
   * status setter method.
   *
   * @param status - New status.
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * message getter method.
   *
   * @return message associated with this instance of UserErrorRespponse.
   */
  public String getMessage() {
    return message;
  }

  /**
   * message setter method.
   *
   * @param message - New message.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * timestamp getter method.
   *
   * @return timestamp associated with this instance of UserErrorRespponse.
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * timestamp setter method.
   *
   * @param timestamp - New timestamp.
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Overridden method allowing for hash codes to be genreated for UserErrorResponse objects.
   *
   * @return hash of a UserErrorResponse object given its message, status and timestamp as
   *     parameters.
   */
  @Override
  public int hashCode() {
    return Objects.hash(message, status, timestamp);
  }

  /**
   * Overridden method that determines if objects are equivalent to an instance of UserErrorResponse.
   * 
   * @param obj - Object to compare.
   * @return boolean denoting the equivalence of the objects.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof UserErrorResponse)) {
      return false;
    }
    UserErrorResponse other = (UserErrorResponse) obj;
    return Objects.equals(message, other.message)
        && status == other.status
        && timestamp == other.timestamp;
  }

  /**
   * Overridden method allowing UserErrorResponse objects to be converted to strings.
   *
   * @return a stringified UserErrorResponse with each field name and its value appended as a
   *     string.
   */
  @Override
  public String toString() {
    return "UserErrorResponse [status="
        + status
        + ", message="
        + message
        + ", timestamp="
        + timestamp
        + "]";
  }
}
