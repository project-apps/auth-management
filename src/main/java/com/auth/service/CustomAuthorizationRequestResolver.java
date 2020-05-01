package com.auth.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.parser.model.AppConstEnum;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {

		this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, "/oauth2/authorization");
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request);

		return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest, request) : null;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {

		OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request,
				clientRegistrationId);

		return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest, request) : null;
	}

	private OAuth2AuthorizationRequest customAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {

		Map<String, Object> additionalParameters = new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
		additionalParameters.put(AppConstEnum.CALLBACK_URL.value, request.getParameter(AppConstEnum.CALLBACK_URL.value));
		request.getSession().setAttribute(AppConstEnum.CALLBACK_URL.value,request.getParameter(AppConstEnum.CALLBACK_URL.value));
		return OAuth2AuthorizationRequest.from(authorizationRequest).additionalParameters(additionalParameters).build();
	}
}
