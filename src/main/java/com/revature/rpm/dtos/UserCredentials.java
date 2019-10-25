package com.revature.rpm.dtos;

import java.util.Objects;

/** Used to store user credentials extracted from the request body of an authentication request. */
public class UserCredentials {

  private String username;
  private String password;

  /**
   * username getter method.
   *
   * @return username associated with this instance of UserCredentials
   */
  public String getUsername() {
    return username;
  }

  /**
   * username setter method.
   *
   * @param username - New username.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * password getter method.
   *
   * @return password associated with this instance of UserCredentials.
   */
  public String getPassword() {
    return password;
  }

  /**
   * passwrd setter method.
   *
   * @param password - New passwrod.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Overridden method allowing for hash codes to be genreated for UserCredential objects.
   *
   * @return hash of a UserCrediantials object given its password and username as parameters.
   */
  @Override
  public int hashCode() {
    return Objects.hash(password, username);
  }

  /**
   * Overriden method that determines if objects are equivalent to an instance of UserCredentials.
   *
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
    if (!(obj instanceof UserCredentials)) {
      return false;
    }
    UserCredentials other = (UserCredentials) obj;
    return Objects.equals(password, other.password) && Objects.equals(username, other.username);
  }

  /**
   * Overriden method allowing UserCredentials objects to be converted to strings.
   *
   * @return a stringified UserCredials with each field name and its value appended as a string.
   */
  @Override
  public String toString() {
    return "UserCredentials [username=" + username + ", password=" + password + "]";
  }
}
