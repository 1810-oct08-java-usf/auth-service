package com.revature.security;

import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.UserCredentials;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter   {
	
	// We use Spring Security's AuthenticationManager to validate the user credentials
    private AuthenticationManager authManager;
    
    private final JwtConfig jwtConfig;
    
    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        
        /*
         * By default, UsernamePasswordAuthenticationFilter listens to "/login" path. For this demo, we will use 
         * the "/auth" path instead. So, we need to override the defaults using the URI defined by our JwtConfig class.
         */
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        try {
            
            // 1. Get credentials from request
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
            
            System.out.println(creds.getUsername() + "/" + creds.getPassword());
            
            // 2. Create an authentication token (contains credentials) which will be used by the AuthenticationManager
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), creds.getPassword(), Collections.emptyList());
            
            /*
             * 3. Leverage AuthenticationManager to authenticate the user, and use UserDetailsServiceImpl::loadUserByUsername() 
             * method to load the user.
             */
            return authManager.authenticate(authToken);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /*
     * Upon a successful authentication, we should generate a token. The 'auth' that is being passed to this method is the current 
     * authenticated user.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        
        Long now = System.currentTimeMillis();
        
        String token = Jwts.builder()
            .setSubject(auth.getName())    
            // Convert to list of strings. 
            // This is important because it affects the way we get them back in the Gateway.
            .claim("authorities", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
            .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
            .compact();
        
        // Add token to header
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
    }
}
