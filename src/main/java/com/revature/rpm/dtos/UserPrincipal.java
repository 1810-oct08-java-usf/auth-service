package com.revature.rpm.dtos;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

/**
 * Acts as a data transfer object containing basic user information which is
 * sent back to the client upon successful user authentication.
 */
public class UserPrincipal {

	private String username;
	private String accessToken;
	private String accessTokenCreatedAt;
	private String accessTokenExpiresAt;
	private String refreshToken;
	private String grantedScopes;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getAccessTokenCreatedAt() {
		return accessTokenCreatedAt;
	}

	public void setAccessTokenCreatedAt(String accessTokenCreatedAt) {
		this.accessTokenCreatedAt = accessTokenCreatedAt;
	}

	public String getAccessTokenExpiresAt() {
		return accessTokenExpiresAt;
	}

	public void setAccessTokenExpiresAt(String accessTokenExpiresAt) {
		this.accessTokenExpiresAt = accessTokenExpiresAt;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getGrantedScopes() {
		return grantedScopes;
	}

	public void setGrantedScopes(Collection<GrantedAuthority> authorities) {
		
		StringBuilder scopeBuilder = new StringBuilder("");
		Iterator<GrantedAuthority> authorityIterator = authorities.iterator();
		
		int ctr = 0;
		while(authorityIterator.hasNext()) {
			if (ctr == 0) {
				scopeBuilder.append(authorityIterator.next().toString()); 
				ctr++;
			} else {
				scopeBuilder.append(", " + authorityIterator.next().toString());
			}
		}
		
		this.grantedScopes = scopeBuilder.toString();
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessToken, accessTokenCreatedAt, accessTokenExpiresAt, grantedScopes, refreshToken,
				username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserPrincipal))
			return false;
		UserPrincipal other = (UserPrincipal) obj;
		return Objects.equals(accessToken, other.accessToken)
				&& Objects.equals(accessTokenCreatedAt, other.accessTokenCreatedAt)
				&& Objects.equals(accessTokenExpiresAt, other.accessTokenExpiresAt)
				&& Objects.equals(grantedScopes, other.grantedScopes)
				&& Objects.equals(refreshToken, other.refreshToken) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserPrincipal [username=" + username + ", accessToken=" + accessToken + ", accessTokenCreatedAt="
				+ accessTokenCreatedAt + ", accessTokenExpiresAt=" + accessTokenExpiresAt + ", refreshToken="
				+ refreshToken + ", grantedScopes=" + grantedScopes + "]";
	}

}
