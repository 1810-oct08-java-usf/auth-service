package com.revature.rpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Configures this app to be a Spring Boot application (implies @component, @EnableAutoConfiguration
 * and @ComponentScan) and a client of the Eureka discovery service. The only method, main, runs
 * this app as a Spring application with passed in parameters
 */
@SpringBootApplication
@EnableEurekaClient
public class RpmAuthServiceApplication {

  /**
   * Bootstrap and launch application and return the running application context.
   *
   * @param args - An array of Strings corresponding to arguments from the command line.
   */
  public static void main(String[] args) {
    SpringApplication.run(RpmAuthServiceApplication.class, args);
  }
}
