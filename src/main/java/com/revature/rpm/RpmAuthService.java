package com.revature.rpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RpmAuthService {

	public static void main(String[] args) {
		SpringApplication.run(RpmAuthService.class, args);
	}

}
