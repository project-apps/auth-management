package org.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.auth.service.AbstractUserDetailsService;
import org.auth.util.JWTTokenUtil;
import org.parser.model.AuthUser;
import org.parser.model.JWTRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = { "/user" })
public class UserAuthController extends AbstractGenericController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	protected JWTTokenUtil jwtTokenUtil;

	@Autowired
	private AbstractUserDetailsService userDetailsService;

	@PostMapping(path = {"/authenticate"})
	public ResponseEntity<?> loginAndCreateUserAuthToken(@RequestBody JWTRequest jwtRequest) throws Exception {
		ResponseEntity.BodyBuilder responseEntity = ResponseEntity.status(HttpStatus.OK);
		AuthUser user = null;
		try{
			logger.info("Authenticating jwtRequest for username: "+jwtRequest.getUsername());
			Authentication authentication = authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
			String username = authentication.getName();
			logger.info("Processing jwtRequest: "+jwtRequest+", user: "+username);
			String jwtToken = jwtTokenUtil.generateToken(username);
			logger.info("For username: "+username+", JwtToken generated : "+jwtToken);
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.AUTHORIZATION, jwtToken);
			responseEntity = responseEntity.headers(headers);
			user = userDetailsService.getUserDetails(jwtRequest.getUsername());
		}catch(Exception e) {
			responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT);
			logger.info(e.getMessage());
			//e.printStackTrace();
		}
		return responseEntity.body(user);
	}
	private Authentication authenticate(String username, String password) throws Exception {
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
	@GetMapping(path = { "/error" })
	public String error(HttpServletRequest request) {
		logger.debug("Error occured.");
		request.getSession().removeAttribute("error.message");
		String message = (String) request.getSession().getAttribute("error.message");
		return "Error Message: " + message;
	}
}
