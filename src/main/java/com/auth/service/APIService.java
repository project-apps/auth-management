package com.auth.service;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public interface APIService {
	/** Logger available to subclasses. */
	 Log logger = LogFactory.getLog(APIService.class);
	
	public default <T extends Serializable> HttpEntity<T> getEntity(Map<String,?> uriVariables, Class<T> clazz) {
		HttpEntity<?> responseEntity = ResponseEntity.EMPTY;
		logger.info("Requesting API for entity: "+clazz);
		RestTemplate restTemplate = new RestTemplate();
		try{
			responseEntity = restTemplate.getForEntity(getEndPointURL(), clazz, uriVariables);
		}catch(Exception ex) {
			if(ex instanceof ResourceAccessException) {
				logger.info(ex.getMessage());
			}
			if(ex instanceof HttpClientErrorException) {
				logger.info(ex.getMessage());
			}
			throw ex;
		}
		return (HttpEntity<T>)responseEntity;
	}
	
	public String getEndPointURL();
		
}
