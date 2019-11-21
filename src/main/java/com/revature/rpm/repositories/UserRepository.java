package com.revature.rpm.repositories;

import com.revature.rpm.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A extension of the JpaRepository interface that manages a data type and its ID and allows for
 * CRUD functionality methods to be selectively exposed.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

  AppUser findUserByUsername(String username);

  AppUser findUserByEmail(String email);

  AppUser findUserByUsernameAndPassword(String username, String password);
}
