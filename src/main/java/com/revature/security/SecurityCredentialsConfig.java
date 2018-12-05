package com.revature.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Provides the configuration for the Spring Security framework.
 */
@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtConfig jwtConfig;

	/**
	 * Allows configuring web based security for specific http requests. By default it will be applied 
	 * to all requests, but can be restricted using requestMatcher(RequestMatcher) or other similar 
	 * methods.
	 * 
	 * @param http
	 * 		Used to configure Spring Security with regard to HTTP requests
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			/*
			 * Disables the protection against Cross-Site Request Forgery (CSRF), otherwise requests
			 * cannot be made to this request from the zuul-service.
			 */
			.csrf().disable()
			
			/*
			 * Necessary to prevent Spring Security from blocking frames that will be loaded for 
			 * the H2 console (will be removed once a external datasource is implemented).
			 */
			.headers().frameOptions().disable().and()
			
			/* 
			 * Ensure that a stateless session is used; session will not be used to store user 
			 * information/state.
			 */
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			
			/*
			 * Handle any exceptions thrown during authentication by sending a response status
			 * of unauthorized (401).
			 */
			.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()

			/*
			 * We need to add a filter to validate user credentials and add token in the
			 * response header. This filter will take in an AuthenticationManager object and
			 * a JwtConfig object.
			 * 
			 * AuthenticationManager is an object provided by WebSecurityConfigurerAdapter,
			 * used to authenticate the user using the provided credentials
			 */
			.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))

			/*
			 *  Allows for the access to specific endpoints to be restricted and for others
			 * to be unrestricted
			 */
			.authorizeRequests()
			
				// Allow all POST requests to the "/auth" endpoint
				.antMatchers(jwtConfig.getUri()).permitAll()
				
				/**
				 * TODO Restrict access to the H2 console, so that only admins can access it
				 */
				// Allow unrestricted access to h2-console (for now)
            	.antMatchers("/h2-console/**").permitAll()
            	
            	/* 
            	 * Allow unrestricted access to the actuator/info endpoint. Otherwise, AWS ELB cannot
            	 * perform a health check on the instance and it drains the instances. 
            	 */
            	.antMatchers(HttpMethod.GET, "/actuator/info").permitAll()
            	
            	// Allow unrestricted access to the actuator/mappings endpoint for debugging purposes
            	.antMatchers(HttpMethod.GET, "/**/actuator/mappings").permitAll()
            	
				// All other requests must be authenticated
				.anyRequest().authenticated();
	}

	/*
	 * Spring has UserDetailsService interface, which can be overriden to provide
	 * our implementation for fetching user from the database (or any other source).
	 * 
	 * The UserDetailsService object is used by the AuthenticationManager to load
	 * the user from database. Additionally, we need to define the password encoder
	 * also. So, our AuthentcationManager object can compare and verify passwords.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
