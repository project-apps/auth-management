package com.auth.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.web.client.RestTemplate;

import com.auth.filter.RestAuthenticationEntryPoint;
import com.auth.service.CustomAuthorizationRequestResolver;
import com.auth.service.CustomOAuth2UserService;
import com.auth.service.CustomOidcUserService;
import com.auth.service.CustomUserDetailsService;
import com.auth.service.OAuth2AuthenticationFailureHandler;
import com.auth.service.OAuth2AuthenticationSuccessHandler;

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
	/*
	 * @Autowired private RequestFilter requestFilter;
	 */
	@Autowired
	protected ClientRegistrationRepository clientRegistrationRepository;
	
	protected WebSecurityConfigurer() {
		super();
		logger.trace("WebSecurity Intialized.");
		
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint()).and().authorizeRequests()
		.antMatchers("/", "/**").permitAll()
		.and().oauth2Login().authorizationEndpoint().authorizationRequestResolver(authorizationRequestResolver())
		.and().userInfoEndpoint().oidcUserService(oidcUserService).userService(oauth2UserService)
		.and().successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);
		//http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
		//http.addFilterBefore(authorizationRequestRedirectFilter, OAuth2AuthorizationRequestRedirectFilter.class);
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	public OAuth2AuthorizationRequestResolver  authorizationRequestResolver() {
		CustomAuthorizationRequestResolver authorizationRequestResolver = 
				new CustomAuthorizationRequestResolver(clientRegistrationRepository);
		return authorizationRequestResolver;
	}
	
	/*
	 * @Bean public TokenAuthenticationFilter tokenAuthenticationFilter() { return
	 * new TokenAuthenticationFilter(); }
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
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
