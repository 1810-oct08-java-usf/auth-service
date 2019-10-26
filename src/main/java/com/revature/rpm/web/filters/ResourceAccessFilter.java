package com.revature.rpm.web.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.revature.rpm.security.config.JwtConfig;

/**
 * A filter used to intercept all requests and validate the JWT, if present, in
 * the HTTP request header.
 * 
 * @author Wezley Singleton
 */
public class TokenFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;

	/**
	 * Constructor for JwtTokenAuthenticationFilter that instantiates the JwtConfig
	 * field.
	 *
	 * @param jwtConfig
	 *            Provides configuration for validating JWTs
	 */
	public TokenFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
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
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws ServletException, IOException {

	
		String header = req.getHeader(jwtConfig.getHeader());

		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			chain.doFilter(req, resp);
			return;
		}

		String token = header.replaceAll(jwtConfig.getPrefix(), "");

		try {

			Claims claims = Jwts.parser()
					.setSigningKey(jwtConfig.getSecret().getBytes())
					.parseClaimsJws(token)
					.getBody();

			String username = claims.getSubject();

			if (username != null) {
				
				@SuppressWarnings("unchecked")
				List<String> authoritiesClaim = (List<String>) claims.get("authorities");
				
				List<SimpleGrantedAuthority> grantedAuthorities = authoritiesClaim.stream()
																				  .map(SimpleGrantedAuthority::new)
																				  .collect(Collectors.toList());
				
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}

		chain.doFilter(req, resp);
	}

}