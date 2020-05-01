package com.auth.service;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

public class CustomsCorsConfigurationSource implements CorsConfigurationSource {

	@Override
	public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
		
		return null;
	}

}
