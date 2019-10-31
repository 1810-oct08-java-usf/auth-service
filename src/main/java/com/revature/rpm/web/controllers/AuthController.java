package com.revature.rpm.web.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.rpm.dtos.ErrorResponse;
import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.services.TokenService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

/**
 * This controller contains all authentication- and token-related
 * front-facing endpoints for the RPM system.
 * 
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	private TokenService tokenService;

	@Autowired
	public AuthController(TokenService service) {
		this.tokenService = service;
	}

	/**
	 * Serves as a front-facing endpoint for determining the scopes specified within
	 * a token
	 * 
	 * @return a comma-separated list of granted scopes
	 */
	@GetMapping(value = "/scopes", produces = "text/plain")
	public String getGrantedScopes(@RequestHeader("access_token") String token) {
		return tokenService.extractGrantedScopes(token);
	}

	/**
	 * Serves as a front-facing endpoint obtaining a new access token using the
	 * provided refresh token.
	 * 
	 * @return
	 * 
	 */
	@GetMapping(value = "/refresh", produces = "application/json")
	public UserPrincipal refreshAccessToken(@RequestHeader("refresh_token") String refreshToken) {
		return tokenService.refreshAccessToken(refreshToken);
	}

	/**
	 * This handles any MalformedJwtException thrown in the AuthController.
	 * 
	 * @param sige the thrown exception
	 * @param resp a facade of the HTTP response object
	 * 
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleSignatureException(SignatureException sige, HttpServletResponse resp) {

		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(sige.getMessage());
		error.setTimestamp(System.currentTimeMillis());

		resp.setHeader("WWW-Authenticate", "Bearer realm=\"auth-service\", " + "error=\"invalid_request\", "
				+ "error_description=\"Invalid token provided\"");

		return error;

	}

	/**
	 * This handles any MalformedJwtException thrown in the AuthController.
	 * 
	 * @param mje  the thrown exception
	 * @param resp a facade of the HTTP response object
	 * 
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMalformedJwtException(MalformedJwtException mje, HttpServletResponse resp) {

		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(mje.getMessage());
		error.setTimestamp(System.currentTimeMillis());

		resp.setHeader("WWW-Authenticate", "Bearer realm=\"auth-service\", " + "error=\"invalid_request\", "
				+ "error_description=\"Invalid token provided\"");

		return error;

	}

	/**
	 * This handles any ExpiredJwtException thrown in the AuthController.
	 * 
	 * @param eje  the thrown exception
	 * @param resp a facade of the HTTP response object
	 * 
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleExpiredJwtException(ExpiredJwtException eje, HttpServletResponse resp) {

		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(eje.getMessage());
		error.setTimestamp(System.currentTimeMillis());

		resp.setHeader("WWW-Authenticate", "Bearer realm=\"auth-service\", " + "error=\"invalid_token\", "
				+ "error_description=\"Access token expired\"");

		return error;

	}

	/**
	 * This handles any SecurityException thrown in the AuthController.
	 * 
	 * @param se   the thrown exception
	 * @param resp a facade of the HTTP response object
	 * 
	 * @return This method will return an error of type UserErrorResponse
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleSecurityException(SecurityException se, HttpServletResponse resp) {

		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(se.getMessage());
		error.setTimestamp(System.currentTimeMillis());

		resp.setHeader("WWW-Authenticate", "Bearer realm=\"auth-service\", " + "error=\"invalid_token\", "
				+ "error_description=\"Token is associated with locked subject\"");

		return error;

	}

}
