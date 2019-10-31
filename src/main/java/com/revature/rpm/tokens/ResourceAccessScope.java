package com.revature.rpm.tokens;

/**
 * An enumeration that lists the known and accepted resource access scopes for
 * users of the RPM system.
 * 
 */
public enum ResourceAccessScope {

	GET_ALL_USERS("get_all_users"), GET_USERS_BY_ID("get_users_by_id"), GET_USERS_BY_USERNAME("get_users_by_username"),
	GET_USERS_BY_EMAIL("get_users_by_email"), CREATE_USERS("create_users"), MUTATE_USERS("mutate_users"),
	DELETE_USERS("delete_users"), CHECK_AVAILABILITY("check_availability"),

	GET_ALL_PROJECTS("get_all_projects"), GET_PROJECT_BY_ID("get_project_by_id"),
	GET_PROJECTS_BY_TRAINER("get_projects_by_trainer"), GET_PROJECTS_BY_BATCH("get_projects_by_batch"),
	CREATE_PROJECTS("create_projects"), MUTATE_PROJECTS("mutate_projects"), DELETE_PROJECTS("delete_projects");

	private String scopeLevel;

	private ResourceAccessScope(String scope) {
		this.scopeLevel = scope;
	}

	public String toString() {
		return this.scopeLevel;
	}

}
