package com.revature.rpm.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Represents an authenticated user with all of their core information and declares them an entity
 * that can be persisted. Also includes column and relationship annotations for Hibernate to create
 * a table called RPM_USERS that stores AppUsers.
 */
@Entity
@Table(name = "RPM_USERS")
public class AppUser implements Serializable {

  private static final long serialVersionUID = -2361806217291440694L;

  @Id
  @Column(name = "RPM_USER_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @Column(name = "RPM_USER_FN")
  private String firstName;

  @NotNull
  @Column(name = "RPM_USER_LN")
  private String lastName;

  @NotNull
  @Pattern(regexp = "^[a-zA-Z0-9_.+-]+(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?@(.+)$")
  @Column(name = "RPM_USER_EMAIL")
  private String email;

  @NotNull
  @Column(name = "RPM_USER_USERNAME")
  private String username;

  @NotNull
  @Column(name = "RPM_USER_PW")
  private String password;

  @Column(name = "RPM_USER_ROLE")
  private String role;

  /** A no-args constructor that sets defaults for a newly created AppUser object. */
  public AppUser() {
    super();
    firstName = "";
    lastName = "";
    email = "";
    username = "";
    password = "";
    role = "LOCKED";
  }

  /**
   * Constructor for AppUser that takes in an id, first name, last name, email, username, password
   * and role.
   *
   * @param id - User's unique ID.
   * @param firstName - User's first name.
   * @param lastName - User's last name.
   * @param email - User's email.
   * @param username - User's username.
   * @param password - User's password.
   * @param role - User's role.
   */
  public AppUser(
      Integer id,
      String firstName,
      String lastName,
      String email,
      String username,
      String password,
      String role) {
    super();
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  /**
   * id getter method.
   *
   * @return id associated with this instance of AppUser.
   */
  public Integer getId() {
    return id;
  }

  /**
   * id setter method.
   *
   * @param id - New id.
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * firstName getter method.
   *
   * @return firstName associated with this instance of AppUser.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * firstName setter method.
   *
   * @param firstName - New firstnamd.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * lastName getter method.
   *
   * @return lastName associated with this instance of AppUser.
   */
  public String getlastName() {
    return lastName;
  }

  /**
   * lastName setter method.
   *
   * @param lastName - New lastName.
   */
  public void setlastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * email getter method.
   *
   * @return email associated with this instance of AppUser.
   */
  public String getEmail() {
    return email;
  }

  /**
   * email setter method.
   *
   * @param email - New email.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * username getter method.
   *
   * @return username associated with this instance of AppUser.
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
   * @return password associated with this instance of AppUser.
   */
  public String getPassword() {
    return password;
  }

  /**
   * password setter method.
   *
   * @param password - New password.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * role getter method.
   *
   * @return role associated with this instance of AppUser.
   */
  public String getRole() {
    return role;
  }

  /**
   * role setter method.
   *
   * @param role - New role.
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Overridden method allowing for hash codes to be genreated for AppUser objects.
   *
   * @return hash of a AppUser object given its email, firstName, id, lastName, password, role and
   *     username as parameters.
   */
  @Override
  public int hashCode() {
    return Objects.hash(email, firstName, id, lastName, password, role, username);
  }

  /**
   * Overridden method that determines if objects are equivalent to an instance of AppUser.
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
    if (!(obj instanceof AppUser)) {
      return false;
    }
    AppUser other = (AppUser) obj;
    return Objects.equals(email, other.email)
        && Objects.equals(firstName, other.firstName)
        && Objects.equals(id, other.id)
        && Objects.equals(lastName, other.lastName)
        && Objects.equals(password, other.password)
        && Objects.equals(role, other.role)
        && Objects.equals(username, other.username);
  }

  /**
   * Overridden method allowing AppUser objects to be converted to strings.
   *
   * @return a stringified AppUser with each field name and its value appended as a string.
   */
  @Override
  public String toString() {
    return "AppUser [id="
        + id
        + ", firstName="
        + firstName
        + ", lastName="
        + lastName
        + ", email="
        + email
        + ", username="
        + username
        + ", password="
        + password
        + ", role="
        + role
        + "]";
  }
}
