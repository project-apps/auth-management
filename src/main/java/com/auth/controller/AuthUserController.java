package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.model.JWTRequest;
import com.auth.model.dto.UserDto;
import com.auth.service.AuthUserDetailsService;

@RestController
@RequestMapping(path = { "/authenticate" })
public class AuthUserController extends GenericAuthController {
	@Autowired
	private AuthUserDetailsService authUserDetails;

	@PostMapping("/user")
	public ResponseEntity<?> loginAndCreateUserAuthToken(@RequestBody JWTRequest jwtRequest) throws Exception {
		ResponseEntity.BodyBuilder responseEntity = ResponseEntity.status(HttpStatus.OK);
		UserDto user = null;
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
			user = authUserDetails.getUserDetails(jwtRequest.getUsername());
		}catch(Exception e) {
			responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT);
			logger.info(e.getMessage());
			//e.printStackTrace();
		}
		return responseEntity.body(user);
	}
}
