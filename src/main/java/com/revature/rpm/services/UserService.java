package com.revature.rpm.services;

import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.entities.AppUser;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.UserCreationException;
import com.revature.rpm.exceptions.UserNotFoundException;
import com.revature.rpm.exceptions.UserUpdateException;
import com.revature.rpm.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * A middle-man that sits between the controller and the repository (DAO) performing validation on
 * all user input received by the auth controller.
 */
@Service
public class UserService implements UserDetailsService {

  private BCryptPasswordEncoder encoder;
  private UserRepository repo;

  @Autowired
  public UserService(UserRepository repo, BCryptPasswordEncoder encoder) {
    this.repo = repo;
    this.encoder = encoder;
  }

  /**
   * Retrieves a list of all users from the database.
   *
   * @return A List containing all registered users.
   */
  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<AppUser> findAllUsers() {
    return repo.findAll();
  }

  /**
   * Performs validation on the provided ID and fetches the user if the ID is valid.
   *
   * @param id - The ID of the user to fetch.
   * @return The user with the specified ID.
   * @throws BadRequestException if the provided ID is invalid.
   * @throws UserNotFoundException if there no user with the specified ID is found.
   */
  @Transactional(readOnly = true)
  public AppUser findUserById(int id) {

    if (id <= 0) {
      throw new BadRequestException("Invalid id value provided");
    }

    Optional<AppUser> _user = repo.findById(id);
    if (!_user.isPresent()) {
      throw new UserNotFoundException("No user found with provided id");
    }

    return _user.get();
  }

  /**
   * Validates the provided username and fetches the user if the username is valid.
   *
   * @param username - The username of the user to fetch.
   * @return The user with the specified username.
   * @throws BadRequestException if the provided username is invalid.
   * @throws UserNotFoundException if no user with the specified username is found.
   */
  @Transactional(readOnly = true)
  public AppUser findUserByUsername(String username) {

    if (username == null || username.equals("")) {
      throw new BadRequestException("Invalid username value provided");
    }

    AppUser retrievedUser = repo.findUserByUsername(username);

    if (retrievedUser == null) {
      throw new UserNotFoundException("No user found with provided username");
    }

    return retrievedUser;
  }

  /**
   * Validates the provided email address and fetches the user if the address is valid.
   *
   * @param email - The email address of the sought user.
   * @return The user with the specified email.
   * @throws BadRequestException if the provided email address is invalid.
   * @throws UserNotFoundException if no user with the specified email address is found.
   */
  @Transactional(readOnly = true)
  public AppUser findUserByEmail(String email) {

    if (email == null || email.equals("")) {
      throw new BadRequestException("Invalid email value provided");
    }

    AppUser retrievedUser = repo.findUserByEmail(email);

    if (retrievedUser == null) {
      throw new UserNotFoundException("No user found with provided email");
    }

    return retrievedUser;
  }

  /**
   * Validates a new user and creates the user in the database if the user's fields are valid.
   *
   * @param newUser - The new user to create in the database.
   * @return The persisted user with its generated id.
   * @throws BadRequestException if an invalid user is provided.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AppUser addUser(AppUser newUser) {

    if (!validateFields(newUser)) {
      throw new BadRequestException("Invalid user object provided");
    }

    if (!isUsernameAvailable(newUser.getUsername())) {
      throw new UserCreationException("Username already in use");
    }

    if (!isEmailAddressAvailable(newUser.getEmail())) {
      throw new UserCreationException("Email address already in use");
    }

    newUser.setRole("ROLE_USER");
    return repo.save(newUser);
  }

  /**
   * Validates a user's fields and updates their values if they're valid. Also checks the requester
   * parameter to ensure the user requesting the update has permission to alter the role as well.
   *
   * @param updatedUser - An updated user whose changes have not been persisted.
   * @param requester - Determines whether or not a role can be updated by the user issuing the
   *     request.
   * @return True if the update was successful. Otherwise an exception is thrown.
   * @throws BadRequestException <br>
   *     - If requester is null. <br>
   *     - If any field of the updatedUser fails validation. <br>
   *     - If updatedUser is null.
   * @throws UserUpdateException <br>
   *     - If no user with a matching ID is found. <br>
   *     - If the updated username is already taken. <br>
   *     - If the updated email is already taken. <br>
   * @throws SecurityException if a user attempts to update their role without admin privileges.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean updateUser(AppUser updatedUser, AppUser requester) {

    if (requester == null) {
      throw new BadRequestException("Invalid requester object provided");
    }

    if (!validateFields(updatedUser) || updatedUser.getId() == null) {
      throw new BadRequestException("Invalid user object provided");
    }

    if (!updatedUser.getId().equals(requester.getId()) && !requester.getRole().equals("ADMIN")) {
      throw new SecurityException("Illegal update request made by " + requester.getUsername());
    }

    AppUser userBeforeUpdate = null;

    try {
      userBeforeUpdate = findUserById(updatedUser.getId());
    } catch (UserNotFoundException unfe) {
      throw new UserUpdateException(unfe.getMessage());
    }

    String persistedUsername = userBeforeUpdate.getUsername();
    String updatedUsername = updatedUser.getUsername();
    if (!persistedUsername.equals(updatedUsername)) {
      AppUser u = repo.findUserByUsername(updatedUsername);
      if (u != null) {
        throw new UserUpdateException("Username is already in use");
      }
    }

    String persistedEmail = userBeforeUpdate.getEmail();
    String updatedEmail = updatedUser.getEmail();
    if (!persistedEmail.equals(updatedEmail)) {
      AppUser u = repo.findUserByEmail(updatedEmail);
      if (u != null) {
        throw new UserUpdateException("Username is already in use");
      }
    }

    String persistedRole = userBeforeUpdate.getRole();
    String updatedRole = updatedUser.getRole();
    if (!requester.getRole().equals("ADMIN") && !updatedRole.equals(persistedRole)) {
      throw new UserUpdateException("Could not update user role");
    }

    repo.save(updatedUser);

    return true;
  }

  /**
   * Validates an ID value and checks if a user with a matching ID already exists. If so, they're
   * deleted from the database.
   *
   * @param id - The ID of the user to delete.
   * @return True, if the delete was successful. Otherwise, an exception is thrown.
   * @throws BadRequestException if the provided ID is invalid.
   * @throws UserNotFoundException if no user with the specified ID is found.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean deleteUserById(int id) {

    if (id <= 0) {
      throw new BadRequestException("Invalid id value provided");
    }

    Optional<AppUser> _user = repo.findById(id);

    if (!_user.isPresent()) {
      throw new UserNotFoundException("No user found with provided id");
    }

    repo.delete(_user.get());
    return true;
  }

  /**
   * Checks whether or not a username is available.
   *
   * @param username - The username to check.
   * @return True if present. Otherwise, false.
   */
  @Transactional(readOnly = true)
  public boolean isUsernameAvailable(String username) {

    try {
      findUserByUsername(username);
    } catch (UserNotFoundException unfe) {
      return true;
    }

    return false;
  }

  /**
   * Checks whether a user with the provided email already exists in the database.
   *
   * @param email - The email to check.
   * @return True if present. Otherwise, false.
   */
  @Transactional(readOnly = true)
  public boolean isEmailAddressAvailable(String email) {

    try {
      findUserByEmail(email);
    } catch (UserNotFoundException unfe) {
      return true;
    }

    return false;
  }

  /**
   * Performs validation on all user fields.
   *
   * @param user - a user with fields that require validation.
   * @return True if valid and false otherwise.
   */
  public boolean validateFields(AppUser user) {
    if (user == null) {
      return false;
    }
    if (user.getFirstName() == null || user.getFirstName().equals("")) {
      return false;
    }
    if (user.getLastName() == null || user.getLastName().equals("")) {
      return false;
    }
    if (user.getUsername() == null || user.getUsername().equals("")) {
      return false;
    }
    if (user.getPassword() == null || user.getPassword().equals("")) {
      return false;
    }
    if (user.getEmail() == null || user.getEmail().equals("")) {
      return false;
    }
    if (user.getRole() == null || user.getRole().equals("")) {
      return false;
    }
    return true;
  }

  /**
   * Overrides Spring Security's UserDetailService interface method to return a user from the data
   * repository with the provided username.
   *
   * @param username - The username of a user requesting authentication.
   * @return UserDetails that provide core user information used by Spring Security for
   *     authentication
   * @throws BadRequestException if the provided username is invalid.
   * @throws UsernameNotFoundException = if no user with the specified username is found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) {

    if (username == null || username.equals("")) {
      throw new BadRequestException("Invalid username value provided");
    }

    AppUser retrievedUser = repo.findUserByUsername(username);

    if (retrievedUser == null) {
      throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    String userPw = retrievedUser.getPassword();
    String encodedPw = encoder.encode(userPw);
    String userRole = retrievedUser.getRole();
    retrievedUser.setPassword(encodedPw);

    List<GrantedAuthority> grantedAuthorities =
        AuthorityUtils.commaSeparatedStringToAuthorityList(userRole);

    return new UserPrincipal(retrievedUser, username, encodedPw, grantedAuthorities);
  }
}
