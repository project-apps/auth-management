package org.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomOncePerRequestFilter extends OncePerRequestFilter{
	private static final Log logger = LogFactory.getLog(CustomOncePerRequestFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.trace("In CustomOncePerRequestFilter");
		filterChain.doFilter(request, response);
	}

}
