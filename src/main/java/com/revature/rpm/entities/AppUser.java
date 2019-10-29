package com.revature.rpm.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Represents an authenticated user with all of their core information.
 */
@Entity
@Table(name="RPM_USERS")
public class AppUser {

	@Id
	@Column(name="RPM_USER_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Column(name="RPM_USER_FN")
	private String firstName;
	
	@NotNull
	@Column(name="RPM_USER_LN")
	private String lastName;
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9_.+-]+(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?@(.+)$")
	@Column(name="RPM_USER_EMAIL")
	private String email;
	
	@NotNull
	@Column(name="RPM_USER_USERNAME")
	private String username;
	
	@NotNull
	@Column(name="RPM_USER_PW")
	private String password;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	public AppUser() {
		super();
		firstName = "";
		lastName = "";
		email = "";
		username = "";
		password = "";
		role = UserRole.ROLE_LOCKED;
	}

	public AppUser(Integer id, String firstName, String lastName, String email, String username, String password,
			UserRole role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, id, lastName, password, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AppUser))
			return false;
		AppUser other = (AppUser) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(password, other.password) && Objects.equals(role, other.role)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}
	
}
