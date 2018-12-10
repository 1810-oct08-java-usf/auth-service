package com.revature.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.models.AppUser;
import com.revature.repository.UserRepository;


@Service
public class UserService {
	
	private UserRepository repo;
	
	public UserService() {}
	
	@Autowired
	public UserService(UserRepository repo) {
		this.repo = repo;
	}
	
	@Transactional(readOnly=true, isolation=Isolation.SERIALIZABLE)
	public List<AppUser> findAllUsers(){
		return repo.findAll();
	}
	
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public AppUser findById(int id) {
		Optional<AppUser> optUser = repo.findById(id);
		if(optUser.isPresent()) return optUser.get();
		else return null;
	}
	
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public AppUser findUserByUsername(String username) {
		if(repo.findUserByUsername(username) == null) return null;
		else return repo.findUserByUsername(username);
	}
	
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public AppUser findUserByEmail(String email) {
		return repo.findUserByEmail(email);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public AppUser addUser(AppUser newUser) {
		AppUser tempUser = findUserByUsername(newUser.getUsername());
		if(tempUser != null) return null;
		tempUser = findUserByEmail(newUser.getEmail());
		if(tempUser != null) return null;
		newUser.setRole("ROLE_USER");
		return repo.save(newUser);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean saveUser(AppUser user) {
		if(user == null) return false;
		repo.save(user);
		return true;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean deleteUserById(int id) {
		Optional<AppUser> optUser = repo.findById(id);
		if(optUser.isPresent()) {
			AppUser tempUser = optUser.get();
			repo.delete(tempUser);
			return true;
		}
		else {
			return false;
		}
	}
	
}
