package com.revature.rpm.dtos;

import java.util.Objects;

/**
 * Used to store user credentials recieved from request body of an
 * authentication request.
 */
public class UserCredentials {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserCredentials))
			return false;
		UserCredentials other = (UserCredentials) obj;
		return Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserCredentials [username=" + username + ", password=" + password + "]";
	}

}
