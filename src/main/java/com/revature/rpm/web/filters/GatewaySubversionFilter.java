package com.revature.rpm.web.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.rpm.dtos.ErrorResponse;
import com.revature.rpm.exceptions.GatewaySubversionException;
import com.revature.rpm.security.config.ZuulConfig;

/*
 * TODO
 * 	- Obtain IP address of subverting requesters for logging
 * 	- Log all exceptions appropriately
 */

/**
 * The purpose of this class is to create custom filters for this service to
 * only accept and authorize resource requests from the gateway. This is
 * accomplished by looking for the value of the header from named
 * RPM_ZUUL_ACCESS_HEADER, which is applied by the gateway when the request is
 * first intercepted.
 */
public class GatewaySubversionFilter extends GenericFilterBean {

	private ZuulConfig zuulConfig;

	/**
	 * Constructor for CustomAuthenticationFilter that instantiates the ZuulConfig
	 * field.
	 *
	 * @param zuulConfig Provides configuration for validating that requests came
	 *                   through Zuul
	 */
	public GatewaySubversionFilter(ZuulConfig zuulConfig) {
		this.zuulConfig = zuulConfig;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		final String GATEWAY_SUBVERTED = "gateway-subverted";
		final String REQUEST_TYPE = "request-type";

		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;

		String headerZuul = httpReq.getHeader("X-RPM-Gateway");

		try {

			if (httpReq.getRequestURI().contains("/actuator")) {
				httpReq.setAttribute(GATEWAY_SUBVERTED, true);
				httpReq.setAttribute(REQUEST_TYPE, "health-check");
			} else if (validateHeader(headerZuul)) {
				httpReq.setAttribute(GATEWAY_SUBVERTED, false);
				httpReq.setAttribute(REQUEST_TYPE, "resource");
			} else {
				httpReq.setAttribute(GATEWAY_SUBVERTED, true);
				httpReq.setAttribute(REQUEST_TYPE, "resource");
				SecurityContextHolder.clearContext();
				httpResp.setStatus(401);
				throw new GatewaySubversionException("No gateway header found on request.");
			}

		} catch (GatewaySubversionException gse) {
			handleSubversion(httpResp);
			return;
		}

		chain.doFilter(httpReq, httpResp);

	}

	/**
	 * Method to perform a SHA-512 hash.
	 * 
	 * @param password Represents the password which will be hashed
	 */
	public String getHash(String password) {

		String generatedPassword = null;

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(zuulConfig.getSalt().getBytes(StandardCharsets.UTF_8));
			byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			generatedPassword = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return generatedPassword;
	}

	/**
	 * Method to check if the header value matches the expected value, using the
	 * ZuulConfig object.
	 *
	 * @param header The retrieved header from the request object
	 */
	public boolean validateHeader(String header) {
		if (header == null) {
			return false;
		}
		
		return header.equals("test-value");

	}

	/**
	 * Takes in the HTTP response facade object and writes an appropriate
	 * ErrorResponse object to its body while setting the correct status code.
	 * 
	 * @param resp
	 */
	public void handleSubversion(HttpServletResponse resp) {

		try {
			
			resp.setStatus(401);
			resp.setContentType("application/json");
			
			ObjectMapper mapper = new ObjectMapper();
			PrintWriter writer = resp.getWriter();
			

			ErrorResponse err = new ErrorResponse(401, System.currentTimeMillis());
			err.setMessage("No gateway header found on request.");

			String errJSON = mapper.writeValueAsString(err);
			writer.write(errJSON);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}