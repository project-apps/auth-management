package org.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auth.provider.UserPrincipal;
import org.auth.util.TokenProvider;
import org.parser.Base64Parser;
import org.parser.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

@Service
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final Log logger = LogFactory.getLog(OAuth2AuthenticationSuccessHandler.class);
	@Autowired
	protected TokenProvider tokenProvider; 
	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String targetUrl = determineTargetUrl(request, response);
		//targetUrl = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request)).replaceQuery(null)
		//	.build().toUriString();
		//targetUrl = (targetUrl != null ? targetUrl : getDefaultTargetUrl());
		//targetUrl = targetUrl.replaceAll("/code/", "/callback/");	
		targetUrl =  "http://localhost:8080/app/login/callback";
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		AuthUser user = new AuthUser();
		user.setEmail(userPrincipal.getEmail());
		user.setName(userPrincipal.getName());
		targetUrl += "/"+Base64Parser.serialize(user);

		logger.debug("Redirecting to: " + targetUrl);
		return targetUrl;
	}
}