package com.auth.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parser.model.AppConstEnum;
import org.parser.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.auth.provider.UserPrincipal;
import com.auth.util.TokenProvider;

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
		targetUrl = String.valueOf(request.getSession().getAttribute(AppConstEnum.CALLBACK_URL.value));
		//targetUrl =  "http://localhost:8080/app/login/callback";
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		AuthUser user = new AuthUser();
		user.setEmail(userPrincipal.getEmail());
		user.setName(userPrincipal.getName());
		String names[] = userPrincipal.getName().split(" ");
		user.setFirstName(names[0]);
		user.setLastName(names[1]);
		//targetUrl += "/"+Base64Parser.serialize(user);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		try {
			response.getWriter().write(user.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("Redirecting to: " + targetUrl);
		return targetUrl;
	}
}