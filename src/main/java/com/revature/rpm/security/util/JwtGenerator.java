package com.revature.rpm.security.util;

import com.revature.rpm.security.config.JwtConfig;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/** A JWT generator. */
public class JwtGenerator {

  private JwtGenerator() {
    super();
  }

  /**
   * Creates a JWT based on an authentication token. <br>
   * <br>
   * Encodes the following inside the JWT: 
   *   - The username of the subject<br>
   *   - The issuer of the JWT token<br>
   *   - The authority claims of the user based upon their role<br>
   *   - The time that the JWT token was issued<br>
   *   - When the JWT token will expire (in milliseconds) <br>
   *
   * @param auth - Authentication token from Spring Security.
   * @param jwtConfig - Configures the settings for the JWT's creation.
   * @return The JWT (without prefix).
   */
  public static String createJwt(Authentication auth, JwtConfig jwtConfig) {
    System.out.println("creating new JWT for: " + auth.getName());

    SignatureAlgorithm sigAlg = SignatureAlgorithm.HS512;
    long nowMillis = System.currentTimeMillis();

    /*
     * Converts info in .claim() to list of strings
     *
     * IMPORTANT: this affects the way we get them back in the Gateway.
     */
    JwtBuilder builder =
        Jwts.builder()
            .setSubject(auth.getName())
            .setIssuer("revature")
            .claim(
                "authorities",
                auth.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .setIssuedAt(new Date(nowMillis))
            .setExpiration(new Date(nowMillis + jwtConfig.getExpiration()))
            .signWith(sigAlg, jwtConfig.getSecret().getBytes());

    System.out.println("JWT successfully created");

    return builder.compact();
  }
}
