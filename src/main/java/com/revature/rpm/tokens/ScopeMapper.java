package com.revature.rpm.tokens;

import org.springframework.stereotype.Component;

import com.revature.rpm.entities.UserRole;

@Component
public class ScopeMapper {
	
	public String mapScopesBasedUponRole(UserRole role) {
		
		ResourceAccessScope[] allowableScopes = ResourceAccessScope.values();
		StringBuilder grantedScopes = new StringBuilder("");
		
		switch (role) {
		
		case ROLE_ADMIN:
			
			grantedScopes.append(allowableScopes[0]);
			for(int i = 1; i < allowableScopes.length; i++) {
				grantedScopes.append(", " + allowableScopes[i]);
			}
			
			break;
			
		case ROLE_DEV:
			
			grantedScopes.append(allowableScopes[0]);
			for(int i = 1; i < allowableScopes.length; i++) {
				
				ResourceAccessScope scope = allowableScopes[i];
				
				if (!scope.equals(ResourceAccessScope.DELETE_USERS)) {
					grantedScopes.append(", " + allowableScopes[i]);
				}
				
			}
			
			break;
			
		case ROLE_CLIENT:

			grantedScopes.append(ResourceAccessScope.CREATE_USERS);
			grantedScopes.append(", " + ResourceAccessScope.CHECK_AVAILABILITY);
			grantedScopes.append(", " + ResourceAccessScope.GET_ALL_PROJECTS);
			grantedScopes.append(", " + ResourceAccessScope.GET_PROJECT_BY_ID);
			grantedScopes.append(", " + ResourceAccessScope.GET_PROJECTS_BY_BATCH);
			grantedScopes.append(", " + ResourceAccessScope.GET_PROJECTS_BY_TRAINER);
			grantedScopes.append(", " + ResourceAccessScope.CREATE_PROJECTS);
			
			break;
			
			
		case ROLE_USER:
			
			grantedScopes.append(ResourceAccessScope.GET_ALL_PROJECTS);
			grantedScopes.append(", " + ResourceAccessScope.GET_PROJECT_BY_ID);
			grantedScopes.append(", " + ResourceAccessScope.GET_PROJECTS_BY_BATCH);
			grantedScopes.append(", " + ResourceAccessScope.GET_PROJECTS_BY_TRAINER);
			grantedScopes.append(", " + ResourceAccessScope.CREATE_PROJECTS);
			
			break;

		default:
			grantedScopes = new StringBuilder("");
		}
		
		return grantedScopes.toString();
	
	}
	
}
