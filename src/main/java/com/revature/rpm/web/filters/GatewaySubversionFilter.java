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
	
	private String salt;
	private String secret;

	public GatewaySubversionFilter(String salt, String secret) {
		this.salt = salt;
		this.secret = secret;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		logger.debug("Request intercepted by GatewaySubversionFilter");
		
		final String GATEWAY_SUBVERTED = "gateway-subverted";
		final String REQUEST_TYPE = "request-type";

		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;

		String gatewayToken = httpReq.getHeader("X-RPM-Gateway");
		System.out.println("header value: " + gatewayToken);
		try {

			if (httpReq.getRequestURI().contains("/actuator")) {
				
				logger.debug("Health-check request detected");
				httpReq.setAttribute(GATEWAY_SUBVERTED, true);
				httpReq.setAttribute(REQUEST_TYPE, "health-check");
				
			} else if (validateGatewayHeader(gatewayToken)) {
				
				logger.debug("Request contains a valid gateway header token");
				httpReq.setAttribute(GATEWAY_SUBVERTED, false);
				httpReq.setAttribute(REQUEST_TYPE, "resource");
				
			} else {

				logger.warn("No gateway header found on request");
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
	 * Validates that the provided gateway header value is authentic. 
	 *
	 * @param header
	 */
	public boolean validateGatewayHeader(String header) {
		
		if (header == null || header.equals("")) {
			return false;
		}
		
		StringBuilder hash = new StringBuilder();

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes(StandardCharsets.UTF_8));
			byte[] bytes = md.digest(secret.getBytes(StandardCharsets.UTF_8));

			

			for (int i = 0; i < bytes.length; i++) {
				hash.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return header.equals(hash.toString());

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