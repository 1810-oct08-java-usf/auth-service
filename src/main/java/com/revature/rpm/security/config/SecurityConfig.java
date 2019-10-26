package com.revature.rpm.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.revature.rpm.web.filters.AuthFilter;
import com.revature.rpm.web.filters.GatewaySubversionFilter;
import com.revature.rpm.web.filters.ResourceAccessFilter;

/**
 * Provides the configuration for the Spring Security framework.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${rpm.gateway.header:X-RPM-Gateway}")
	private String gatewayHeader;
	
	@Value("${rpm.gateway.salt:local-deploy}")
	private String gatewaySalt;

	@Value("${rpm.gateway.secret:local-deploy}")
	private String gatewaySecret;
	
	@Value("${rpm.security.auth.header:Authorization}")
	private String accessHeader;
	
	@Value("${rpm.security.auth.prefix:Bearer }")
	private String accessPrefix;
	
	@Value("${rpm.security.auth.secret:local-deploy}")
	private String accessSecret;
	
	@Value("${rpm.security.auth.expiration:#{24*60*60*1000}}")
	private String accessExpiration;
	
	@Lazy
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				/*
				 * Disables the protection against Cross-Site Request Forgery (CSRF), otherwise
				 * requests cannot be made to this business service from the zuul-service.
				 */
				.csrf().disable()

				/*
				 * Necessary to prevent Spring Security from blocking frames that will be loaded
				 * for the H2 console (will be removed once a external datasource is
				 * implemented).
				 */
				.headers().frameOptions().disable().and()

				/*
				 * Ensure that a stateless session is used; session will not be used to store
				 * user information/state.
				 */
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				/*
				 * Handle any exceptions thrown during authentication by sending a response
				 * status of unauthorized (401).
				 */
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				
				/*
				 * Registering filters:
				 * 
				 * 		- CustomAuthenticationFilter
				 * 		- JwtUsernameAndPasswordAuthenticationFilter
				 * 		- JwtTokenAuthenticationFilter
				 */
	            .addFilterBefore(new GatewaySubversionFilter(gatewaySalt, gatewaySecret), AuthFilter.class)
				.addFilter(new AuthFilter(authenticationManager(), accessHeader, accessPrefix, accessSecret, accessExpiration))
				.addFilterAfter(new ResourceAccessFilter(accessHeader, accessPrefix, accessSecret), AuthFilter.class)
				
				/*
				 * Allows for the access to specific endpoints to be restricted and for others
				 * to be unrestricted
				 */
				.authorizeRequests()

				/*
				 *  Allow unrestricted access to:
				 *  
				 *  	- POST requests to 
				 *  	- POST requests to 
				 *  	- GET requests to /users/
				 *  	- GET requests to /users/usernameAvailable
				 *  	- GET requests to /actuator/info (needed for ELB)
				 *  	- GET requests to /actuator/routes (needed for ELB)
				 *  	- All requests to Swagger API doc endpoints (will be restricted in production)
				 */
				.mvcMatchers(HttpMethod.POST, "/auth").permitAll()
				.mvcMatchers(HttpMethod.POST, "/users").permitAll()
				
				.mvcMatchers(HttpMethod.GET, "/actuator/info").permitAll()
				.mvcMatchers(HttpMethod.GET, "/actuator/routes").permitAll()
				
				.mvcMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()

				/*
				 *  Allow only admins to access:
				 *  	
				 *  	- All requests to /h2-console
				 *  	- GET requests to /auth/users
				 *  	- POST requests to /auth/users/admin
				 */
				.mvcMatchers("/h2-console/**").hasRole("ADMIN")
				.mvcMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
				
				// All other requests must be authenticated
				.anyRequest().authenticated();
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
