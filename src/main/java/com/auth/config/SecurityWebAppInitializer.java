package com.auth.config;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
/*
 * @Configuration public class SecurityWebAppInitializer extends
 * AbstractSecurityWebApplicationInitializer { private static final Log logger =
 * LogFactory.getLog(SecurityWebAppInitializer.class);
 * 
 * protected SecurityWebAppInitializer(){ super(WebSecurityConfigurer.class);
 * logger.trace("WebApplicationInitializer called."); }
 * 
 * @Override protected void beforeSpringSecurityFilterChain(ServletContext
 * servletContext) { logger.trace("In beforeSpringSecurityFilterChain.");
 * super.beforeSpringSecurityFilterChain(servletContext); }
 * 
 * @Override protected void afterSpringSecurityFilterChain(ServletContext
 * servletContext) { logger.trace("In afterSpringSecurityFilterChain.");
 * super.afterSpringSecurityFilterChain(servletContext); }
 * 
 * }
 */