package com.auth.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.auth.util.JWTTokenUtil;

@Component
public class LogoutSuccessHandlerService extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private AuthUserDetailsService userDetailsService; 
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String refererUrl = request.getHeader("Referer");
		System.out.println("Logout referelUrl: "+refererUrl);
		String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		userDetailsService.removeCachedUser(username);		
		super.onLogoutSuccess(request, response, authentication);
	}
}