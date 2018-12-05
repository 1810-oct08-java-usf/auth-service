package com.revature.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.models.AppUser;
import com.revature.repository.UserRepository;

/**
 * An implementation of Spring Security's UserDetailService which can be used
 * for loading a user by username and other user-related service
 * functionalities.
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	// Used to encode passwords before storing them within the data source
	private BCryptPasswordEncoder encoder;
	
	// Used to invoke repository methods for persisting to the DB
	private UserRepository userRepo;
	
	//@Autowired
	public UserDetailsServiceImpl(BCryptPasswordEncoder encoder, @Lazy UserRepository userRepo) {
		super();
		this.encoder = encoder;
		this.userRepo = userRepo;
	}
	
	/**
	 * Returns a user from the DB using the provided username. If no user is found
	 * with the given username, an exception is thrown.
	 * 
	 *  @param username
	 *  	The username of a user.
	 *  
	 *  @return UserDetails
	 *  	Provides core user information.
	 *  
	 *  @throws UsernameNotFoundException
	 *  	Throws this if there is no user found with the given username.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		/*
		 * TODO This method should be refactored to actually pull a user from the embedded 
		 * DB based upon their user name. The current implementation is not efficient.
		 */
		List<AppUser> users = new ArrayList<AppUser>();
		userRepo.findAll().forEach(user -> users.add(user));
		
		for(AppUser appUser : users) {
			appUser.setPassword(encoder.encode(appUser.getPassword()));
		}
		
		for (AppUser appUser : users) {
			if (appUser.getUsername().equals(username)) {

				/*
				 * Spring needs roles to be in this format: "ROLE_ADMIN". So, we need to set it
				 * to that format, so we can verify and compare roles and provide a list of
				 * granted authorities based upon the user's role.
				 */
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils
						.commaSeparatedStringToAuthorityList(appUser.getRole());

				/*
				 * The "User" class is provided by Spring and represents a model class for user
				 * to be returned by UserDetailsService, and used by AuthenticationManager to
				 * verify and check user authentication.
				 */
				return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
			}
		}

		// If no user is not found, throw a UsernameNotFoundException
		throw new UsernameNotFoundException("Username: " + username + " not found");
	}
}
