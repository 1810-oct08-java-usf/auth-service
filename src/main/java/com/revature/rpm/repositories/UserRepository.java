package com.revature.rpm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.rpm.entities.AppUser;

/**
 * Class is responsible for storing data into the database
 * 
 * @author Austin Bark (190422-Java-Spark)
 *
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer>{
	
	AppUser findUserByUsername(String username);
	AppUser findUserByEmail(String email);
	AppUser findUserByUsernameAndPassword(String username, String password);

}
