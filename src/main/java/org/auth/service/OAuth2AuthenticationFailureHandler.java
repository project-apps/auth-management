package org.auth.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private static final Log logger = LogFactory.getLog(OAuth2AuthenticationFailureHandler.class);

	
	public OAuth2AuthenticationFailureHandler() {
		super("/login/oauth2/error");
	}
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		logger.debug("login failure.");
		super.onAuthenticationFailure(request, response, exception);
	}
}