package org.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class AuthorizationRequestRedirectFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String callback = null;
		if((callback = request.getParameter("callback"))!=null) {
			Cookie cookie = new Cookie("callback", callback);
			response.addCookie(cookie);
			request.getServletContext().setAttribute("callback", callback);
		}
		filterChain.doFilter(request, response);
	}

}
