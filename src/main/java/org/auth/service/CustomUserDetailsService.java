package org.auth.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auth.exception.UserNotFoudException;
import org.auth.provider.UserPrincipal;
import org.parser.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	@Qualifier("userAPIService")
	private APIService apiService;
	@Autowired
	private RestTemplate restTemplate;

	private Map<String, AuthUser> userDetailsCache =  new ConcurrentHashMap<String, AuthUser>();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			logger.info("Getting user details in UserDetailsService for username: "+username);
			AuthUser user = getUserDetails(username);
			logger.info("Got user details in UserDetailsService for username: "+username);
			List<GrantedAuthority> authorities = Collections.emptyList();
			return new User(user.getEmail(), user.getPassword(), authorities);
		}catch(Exception e){
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}
	public UserDetails loadUserById(Long userId) {
		return new UserPrincipal();
	}
	public AuthUser getUserDetails(String username) throws UserNotFoudException {
		AuthUser user = userDetailsCache.get(username);
		if(user==null) {
			logger.info("User not found in cache, Getting form API.");
			user = getUserFromAPI(username);
			userDetailsCache.put(username, user);
			logger.info("User placed in cache.");
		}
		return user;
	}

	private AuthUser getUserFromAPI(String username)  throws UserNotFoudException  {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("username", username);
		logger.info("Getting user for username: "+username+" from API.");
		try {
			return apiService.getEntity(uriVariables, AuthUser.class).getBody();
		}catch(Exception e) {
			throw new UserNotFoudException(username, e);
		}
	}
	public AuthUser save(AuthUser user) {
		ResponseEntity<AuthUser> responseEntity = restTemplate.postForEntity(apiService.getEndPointURL(), user, AuthUser.class);
		return responseEntity.getBody();
	}
	public void update(AuthUser user) {
		restTemplate.put(apiService.getEndPointURL(), user);
	}
	public void delete(AuthUser user) {
		Map<String,AuthUser> params = new HashMap<String, AuthUser>();
		restTemplate.delete(apiService.getEndPointURL(),params);
	}
}
