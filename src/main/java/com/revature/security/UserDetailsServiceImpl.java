package com.revature.security;

import java.util.Arrays;
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

/**
 * An implementation of Spring Security's UserDetailService which can be used
 * for loading a user by username and other user-related service
 * functionalities.
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	// Used to encode passwords before storing them within the data source
	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * Locates the user based on the username. In the actual implementation, the
	 * search may possibly be case sensitive, or case insensitive depending on how
	 * the implementation instance is configured. In this case, the UserDetails
	 * object that comes back may have a username that is of a different case than
	 * what was actually requested.
	 * 
	 * @param username Used to find a user with a matching username in the data source
	 * @return UserDetails Provides core user information
	 * @throws UserNotFoundException If there is no user found with the provided username
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		/*
		 * TODO Leverage an embedded H2 database and communicate with it through a
		 * repository layer
		 */
		final List<AppUser> users = Arrays.asList(new AppUser(1, "admin", encoder.encode("p4ssw0rd"), "ADMIN"),
				new AppUser(2, "test-user", encoder.encode("test"), "USER"));

		/*
		 * TODO This needs to be refactored into a method which invokes a repository
		 * method
		 */
		for (AppUser appUser : users) {
			if (appUser.getUsername().equals(username)) {

				/*
				 * Spring needs roles to be in this format: "ROLE_ADMIN". So, we need to set it
				 * to that format, so we can verify and compare roles and provide a list of
				 * granted authorities based upon the user's role.
				 */
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils
						.commaSeparatedStringToAuthorityList("ROLE_" + appUser.getRole());

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
