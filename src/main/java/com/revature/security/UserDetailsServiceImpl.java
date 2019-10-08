package com.revature.security;

import java.util.ArrayList;
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

import com.revature.models.AppUser;
import com.revature.models.UserPrincipal;
import com.revature.repositories.UserRepository;

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
	
	@Autowired
	public UserDetailsServiceImpl(BCryptPasswordEncoder encoder,  UserRepository userRepo) {
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
				
		AppUser retrievedUser = userRepo.findUserByUsername(username);
		
		if(retrievedUser == null) {
			throw new UsernameNotFoundException("Username: " + username + " not found");
		}
		
		String userPw = retrievedUser.getPassword();
		String encodedPw = encoder.encode(userPw);
		String userRole = retrievedUser.getRole();
		retrievedUser.setPassword(encodedPw);
	
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(userRole);

		return new UserPrincipal(retrievedUser, username, userPw, grantedAuthorities);

	}
	
}
