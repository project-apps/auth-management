package com.auth.filter;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public class CustomCorsFilter extends CorsFilter {

	public CustomCorsFilter(CorsConfigurationSource configSource) {
		super(configSource);
	}

	 

}
