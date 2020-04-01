package com.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.model.JWTRequest;

@RestController
@RequestMapping(path = { "/authenticate" })
public class AuthApiController extends GenericAuthController {

	@GetMapping("/api")
	public ResponseEntity<?> authenticateApi(@RequestBody JWTRequest jwtRequest) throws Exception {
		ResponseEntity.BodyBuilder responseEntity = ResponseEntity.status(HttpStatus.OK);
		return responseEntity.build();
	}

}
