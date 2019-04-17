package com.revature.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


/**
 * UserPrincipal
 * Custom implementation of provided User object from Spring Security. Extends User.
 */
public class UserPrincipal extends User {

	private static final long serialVersionUID = 1L;
	
	private final AppUser appUser;

	/**
	 * Constructor UserPrincipal
	 * Although the getters and setters aren't visible, 
	 * you are still able to get the username and password because of the user superclass
	 * 
	 * @param appUser
	 * @param username
	 * @param password
	 * @param authorities
	 */
	public UserPrincipal(AppUser appUser, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.appUser = appUser;
		// TODO Auto-generated constructor stub
	}

	public AppUser getAppUser() {
		return appUser;
	}
	
}
