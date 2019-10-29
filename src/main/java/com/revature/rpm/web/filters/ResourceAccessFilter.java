package com.revature.rpm.web.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.revature.rpm.tokens.TokenParser;

import io.jsonwebtoken.Claims;

/**
 * A filter used to intercept all requests and validate the JWT, if present, in
 * the HTTP request header.
 * 
 * @author Wezley Singleton
 */
public class ResourceAccessFilter extends OncePerRequestFilter {
	
	private TokenParser tokenParser;
	
	public ResourceAccessFilter(TokenParser parser) {
		this.tokenParser = parser;
	}

	/**
	 * Performs the JWT validation. If no Authorization header is present, the
	 * request is passed along to the next filter in the chain (in case of requests
	 * to unrestricted endpoints). The token is valid only if it has the proper
	 * prefix, a proper principal, and is unexpired.
	 *
	 * @param req
	 *            Provides information regarding the HTTP request.
	 *
	 * @param resp
	 *            Provides information regarding the HTTP response.
	 *
	 * @param chain
	 *            Used to pass the HTTP request and response objects to the next
	 *            filter in the chain.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
		
		logger.info("Request intercepted by ResourceAccessFilter");
	
		String headerValue = req.getHeader("Authorization");

		if (headerValue == null || !headerValue.startsWith("Bearer ")) {
			logger.info("No resource access header value found on request");
			chain.doFilter(req, resp);
			return;
		}

		logger.info("Resource access header detected, obtaining token value");
		String token = headerValue.replaceAll("Bearer ", "");

		try {

			Claims tokenClaims = tokenParser.parseClaims(token);
			
			String username = tokenClaims.getSubject();

			if (username != null) {
				
				String grantedScopes = (String) tokenClaims.get("scopes");
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(grantedScopes);
				
				logger.info("Resource access scopes determined, setting security context");
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
				SecurityContextHolder.getContext().setAuthentication(auth);
				
			}

		} catch (Exception e) {
			
			logger.warn("Error parsing resource access token for claim information");
			SecurityContextHolder.clearContext();
			return;
			
		}

		chain.doFilter(req, resp);
	}

}