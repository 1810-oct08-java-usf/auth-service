package com.revature.rpm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.rpm.entities.AppUser;

/**
 * Serves as the primary data access object for retrieving and persisting
 * AppUser objects.
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

	AppUser findUserByUsername(String username);

	AppUser findUserByEmail(String email);

	AppUser findUserByUsernameAndPassword(String username, String password);

}
