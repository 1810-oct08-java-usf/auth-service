package com.revature.rpm.tokens;

import java.time.LocalDateTime;
import java.util.Objects;

public class GenericTokenDetails {
	
	private TokenType type;
	private String subject;
	private String iss;
	private LocalDateTime iat;
	private LocalDateTime exp;
	private String claims;
	
	public GenericTokenDetails(String subject, String iss) {
		super();
		this.subject = subject;
		this.iss = iss;
	}

	public TokenType getType() {
		return type;
	}
	
	public void setType(TokenType type) {
		this.type = type;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getIss() {
		return iss;
	}
	
	public void setIss(String iss) {
		this.iss = iss;
	}
	
	public LocalDateTime getIat() {
		return iat;
	}
	
	public void setIat(LocalDateTime iat) {
		this.iat = iat;
	}
	
	public LocalDateTime getExp() {
		return exp;
	}
	
	public void setExp(LocalDateTime exp) {
		this.exp = exp;
	}

	public String getClaims() {
		return claims;
	}

	public void setClaims(String claims) {
		this.claims = claims;
	}

	@Override
	public int hashCode() {
		return Objects.hash(claims, exp, iat, iss, subject, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GenericTokenDetails))
			return false;
		GenericTokenDetails other = (GenericTokenDetails) obj;
		return Objects.equals(claims, other.claims) && Objects.equals(exp, other.exp) && Objects.equals(iat, other.iat)
				&& Objects.equals(iss, other.iss) && Objects.equals(subject, other.subject) && type == other.type;
	}

	@Override
	public String toString() {
		return "GenericTokenDetails [type=" + type + ", subject=" + subject + ", iss=" + iss + ", iat=" + iat + ", exp="
				+ exp + ", claims=" + claims + "]";
	}

}
