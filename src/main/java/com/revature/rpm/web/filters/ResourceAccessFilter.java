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
import com.revature.rpm.tokens.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

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

			Claims tokenClaims = tokenParser.parseClaims(TokenType.ACCESS, token);
			
			String username = tokenClaims.getSubject();

			if (username != null) {
				
				String grantedScopes = (String) tokenClaims.get("scopes");
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(grantedScopes);
				
				logger.info("Resource access scopes determined, setting security context");
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
				SecurityContextHolder.getContext().setAuthentication(auth);
				
			}

		} catch (ExpiredJwtException eje) {
			
			logger.warn("Provided access token is expired");
			
			resp.setStatus(401);
			resp.setHeader("WWW-Authenticate", "Bearer realm=\"auth-service\", "
													+ "error=\"invalid_token\", "
													+ "error_description=\"Access token expired\"");
			
			SecurityContextHolder.clearContext();
			return;
			
		} catch (Exception e) {
			
			logger.error("Error parsing resource access token for claim information");
			
			resp.setStatus(400);
			resp.setHeader("WWW-Authenticate", "Bearer realm=\"auth-service\",\n"
													+ "error=\"invalid_request\",\n"
													+ "error_description=\"Error parsing provided access token\"");
			
			SecurityContextHolder.clearContext();
			return;
			
		}

		chain.doFilter(req, resp);
	}

}