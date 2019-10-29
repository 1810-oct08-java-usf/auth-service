package com.revature.rpm.entities;

public enum UserRole {

	ROLE_ADMIN("ROLE_ADMIN"), ROLE_DEV("ROLE_DEV"), ROLE_USER("ROLE_USER"), ROLE_CLIENT("ROLE_CLIENT"), ROLE_LOCKED("ROLE_LOCKED");
	
	private String roleName;
	
	private UserRole(String name) {
		this.roleName = name;
	}
	
	public String toString() {
		return roleName;
	}
	
}
