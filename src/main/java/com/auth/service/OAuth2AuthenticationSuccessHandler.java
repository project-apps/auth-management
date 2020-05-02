package com.auth.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juneau.serializer.SerializeException;
import org.parser.Base64Parser;
import org.parser.model.AppConstEnum;
import org.parser.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.auth.provider.UserPrincipal;
import com.auth.util.CustomRedirectStrategy;
import com.auth.util.TokenProvider;

@Service
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final Log logger = LogFactory.getLog(OAuth2AuthenticationSuccessHandler.class);
	@Autowired
	protected TokenProvider tokenProvider;
	private RedirectStrategy redirectStrategy = new CustomRedirectStrategy();

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		String targetUrl = determineTargetUrl(request, response);
		targetUrl = String.valueOf(request.getSession().getAttribute(AppConstEnum.CALLBACK_URL.value));
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		AuthUser user = new AuthUser();
		user.setEmail(userPrincipal.getEmail());
		user.setName(userPrincipal.getName());
		String names[] = userPrincipal.getName().split(" ");
		user.setFirstName(names[0]);
		user.setLastName(names[1]);
		try {
			targetUrl += "/" + Base64Parser.encodeToString(Base64Parser.jsonSerializer(user));
		} catch (UnsupportedEncodingException | SerializeException e) {
			e.printStackTrace();
		}

		return targetUrl;
	}

	@Override
	/**
	 * Invokes the configured {@code RedirectStrategy} with the URL returned by the
	 * {@code determineTargetUrl} method.
	 * <p>
	 * The redirect will not be performed if the response has already been
	 * committed.
	 */

	protected void handle(HttpServletRequest request, HttpServletResponse
			response, Authentication authentication) throws IOException, ServletException
	{ String targetUrl = determineTargetUrl(request, response, authentication);

	if (response.isCommitted()) {
		logger.debug("Response has already been committed. Unable to redirect to " +
				targetUrl); return; }

	redirectStrategy.sendRedirect(request, response, targetUrl); }
}