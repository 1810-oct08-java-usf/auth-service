package com.revature.rpm.security.config;

import org.springframework.beans.factory.annotation.Value;

/** Provides configuration for Zuul Header Authentication. */
public class ZuulConfig {

  /**
   * Indicates the name of the HTTP header that will contain the prefix and Zuul header as a value.
   */
  @Value("${security.zsign.header:RPM_ZUUL_ACCESS_HEADER}")
  private String header;

  /**
   * Indicates the salt value that will be combined with the secret to form the hash that will be
   * transferred with the request.
   */
  @Value("${security.zsign.salt}")
  private String salt;

  /** A secret key that's used with the SHA-512 algorithm to generate a hash. */
  @Value("${security.zsign.secret}")
  private String secret;

  public ZuulConfig() {
    super();
  }

  /**
   * header getter method.
   *
   * @return header associated with this instance of ZuulConfig.
   */
  public String getHeader() {
    return header;
  }

  /**
   * header setter method.
   *
   * @param header - New header.
   */
  public void setHeader(String header) {
    this.header = header;
  }

  /**
   * salt getter method.
   *
   * @return salt associated with this instance of ZuulConfig.
   */
  public String getSalt() {
    return salt;
  }

  /**
   * salt setter method.
   *
   * @param salt - New salt.
   */
  public void setSalt(String salt) {
    this.salt = salt;
  }

  /**
   * secret getter method.
   *
   * @return secret associated with this instance of ZuulConfig.
   */
  public String getSecret() {
    return secret;
  }

  /**
   * secret setter method.
   *
   * @param secret - New secret.
   */
  public void setSecret(String secret) {
    this.secret = secret;
  }
}
