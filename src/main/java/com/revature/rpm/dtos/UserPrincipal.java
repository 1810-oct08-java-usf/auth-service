package com.revature.rpm.dtos;

import com.revature.rpm.entities.AppUser;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * UserPrincipal Custom implementation of provided User object from Spring Security. Extends User.
 */
public class UserPrincipal extends User {

  private static final long serialVersionUID = 1L;

  private final AppUser appUser;

  /**
   * UserPrincipal is needed to act as a data transfer object between client and server. This class
   * is required for the configuration of the Spring Security framework. Although the getters and
   * setters aren't visible, the username and password are still retrievable because of the User
   * superclass.
   *
   * @param appUser - A user to store in the principal
   * @param username - A user's username
   * @param password - A user's password
   * @param authorities - A collection of objects that can be any type provided they extend
   *     GrantedAuthority. Used for authentication.
   */
  public UserPrincipal(
      AppUser appUser,
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.appUser = appUser;
  }
  /**
   * appUser getter method.
   *
   * @return appUser associated with this instance of UserPrincipal.
   */
  public AppUser getAppUser() {
    return appUser;
  }
}
