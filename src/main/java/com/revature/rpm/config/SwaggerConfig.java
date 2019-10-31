package com.revature.rpm.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The SwaggerConfig is used to show information about the auth-service.
 * Information includes swagger version, basic information, host, base path,
 * tags, what the API consumes and produces, end points, and model definitions.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final Contact DEFAULT_CONTACT = new Contact("Wezley Singleton", "https://github.com/wsingleton",
			"wezley.singleton@revature.com");

	public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("RPM Authorization API", "RPM Authorization API", "1.0",
			"urn:tos", DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");

	private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(
			Arrays.asList("application/json", "application/xml"));

	/**
	 * When the v2/api-docs end point is hit, it will retrieve information about the
	 * project service in relation to only the controller class, build the JSON
	 * documentation, and display it.
	 *
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT_API_INFO).produces(DEFAULT_PRODUCES_AND_CONSUMES)
				.consumes(DEFAULT_PRODUCES_AND_CONSUMES).select()
				.apis(RequestHandlerSelectors.basePackage("com.revature.rpm.web.controllers"))
				.paths(PathSelectors.any()).build();
	}

}
