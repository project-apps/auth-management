package com.auth.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;

public class CustomRedirectStrategy extends DefaultRedirectStrategy {
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response,
			String url) throws IOException {
		String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
		redirectUrl = response.encodeRedirectURL(redirectUrl);

		if (logger.isDebugEnabled()) {
			logger.debug("Redirecting to '" + redirectUrl + "'");
		}
		try {
			request.getRequestDispatcher(redirectUrl).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}
}
