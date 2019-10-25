package com.revature.rpm.security.config;

import org.springframework.beans.factory.annotation.Value;

/** Provides configuration which will be used during JWT creation upon successful authentication. */
public class JwtConfig {

  /** Indicates the name of the HTTP header that will contain the prefix and JWT as a value. */
  @Value("${security.jwt.header:Authorization}")
  private String header;

  /**
   * A prefix that will come before the JWT within the response header. Can be used to help
   * distinguish this application's Authorization header token from similarly named headers/tokens
   * from other applications.
   */
  @Value("${security.jwt.prefix:Bearer }")
  private String prefix;

  /** Amount of time a token is valid, in milliseconds. */
  @Value("${security.jwt.expiration:#{24*60*60*1000}}")
  private int expiration;

  /** Used as a key with the encryption algorithm to generate JWTs */
  @Value("${security.jwt.secret}")
  private String secret;

  /**
   * header getter method.
   *
   * @return header associated with this instance of JwtConfig.
   */
  public String getHeader() {
    return header;
  }
  /**
   * prefix getter method.
   *
   * @return prefix associated with this instance of JwtConfig.
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * expiration getter method.
   *
   * @return expiration associated with this instance of JwtConfig.
   */
  public int getExpiration() {
    return expiration;
  }

  /**
   * secret getter method.
   *
   * @return secret associated with this instance of JwtConfig.
   */
  public String getSecret() {
    return secret;
  }
}
