package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.auth.util.JWTTokenUtil;

public abstract class GenericAuthController extends GenericController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	protected JWTTokenUtil jwtTokenUtil;

	protected Authentication authenticate(String username, String password) throws Exception {
		try {
			logger.info("Authenticating for username: " + username);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception e) {
			if (e instanceof DisabledException) {
				logger.info("username: "+username +" Disabled.");
				throw new DisabledException("USER_DISABLED", e);
			}
			if (e instanceof BadCredentialsException) {
				logger.info("Invalid credentials for username: "+username);
				throw new BadCredentialsException("INVALID_CREDENTIALS", e);
			}else {
				throw e;
			}
		}
	}
}
