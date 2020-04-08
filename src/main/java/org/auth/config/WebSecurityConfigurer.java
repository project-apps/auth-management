package org.auth.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auth.filter.RestAuthenticationEntryPoint;
import org.auth.filter.TokenAuthenticationFilter;
import org.auth.service.CustomOAuth2UserService;
import org.auth.service.CustomOidcUserService;
import org.auth.service.CustomUserDetailsService;
import org.auth.service.OAuth2AuthenticationFailureHandler;
import org.auth.service.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	private static final Log logger = LogFactory.getLog(WebSecurityConfigurer.class);
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private CustomOidcUserService oidcUserService;
	@Autowired
	private CustomOAuth2UserService oauth2UserService;
	@Autowired
	private OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	private OAuth2AuthenticationFailureHandler authenticationFailureHandler;
	
	protected WebSecurityConfigurer() {
		super();
		logger.trace("WebSecurity Intialized.");
		
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
		exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint()).and().authorizeRequests()
		.antMatchers("/", "/**").permitAll()
		.and().oauth2Login().userInfoEndpoint().oidcUserService(oidcUserService).userService(oauth2UserService)
		.and().successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);
		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter();
	}
	
	/*@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		//configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
*/
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
