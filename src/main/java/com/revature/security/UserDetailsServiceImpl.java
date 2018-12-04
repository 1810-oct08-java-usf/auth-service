package com.revature.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.models.AppUser;
import com.revature.repository.LoginRepository;

@Service   // It has to be annotated with @Service.
public class UserDetailsServiceImpl implements UserDetailsService  {
	
	@Autowired
	private LoginRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public UserDetailsServiceImpl() {
		super();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// hard coding the users. All passwords must be encoded.
		List<AppUser> users = new ArrayList<AppUser>();
		repo.findAll().forEach(user -> users.add(user));
		
		for(AppUser appUser: users) {
			appUser.setPassword(encoder.encode(appUser.getPassword()));
		}

		for(AppUser appUser: users) {
			if(appUser.getUsername().equals(username)) {
				
				// Remember that Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_ADMIN")
				// So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("ADMIN")).
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils
		                	.commaSeparatedStringToAuthorityList("ROLE_" + appUser.getRole());
				
				// The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
				// And used by auth manager to verify and check user authentication.
				return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
			}
		}
		
		
		
		// If user not found. Throw this exception.
		throw new UsernameNotFoundException("Username: " + username + " not found");
	}
}
