package com.auth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.exception.UserNotFoudException;
import com.auth.model.dto.UserDto;

@Service
public class UserDetailsServiceImpl extends GenericServiceImpl implements UserDetailsService, AuthUserDetailsService {

	@Autowired
	@Qualifier("apiService")
	private APIService apiService;

	private Map<String, UserDto> userDetailsCache =  new ConcurrentHashMap<String, UserDto>();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			logger.info("Getting user details in UserDetailsService for username: "+username);
			UserDto user = getUserDetails(username);
			logger.info("Got user details in UserDetailsService for username: "+username);
			return new User(user.getEmail(), user.getPassword(), user.getAuthorities());
		}catch(Exception e){
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}
	@Override
	public UserDto getUserDetails(String username) throws UserNotFoudException {
		UserDto user = userDetailsCache.get(username);
		if(user==null) {
			logger.info("User not found in cache, Getting form API.");
			user = getUserFromAPI(username);
			userDetailsCache.put(username, user);
			logger.info("User placed in cache.");
		}
		return user;
	}

	private UserDto getUserFromAPI(String username)  throws UserNotFoudException  {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("username", username);
		logger.info("Getting user for username: "+username+" from API.");
		try {
			return apiService.getEntity(uriVariables, UserDto.class).getBody();
		}catch(Exception e) {
			throw new UserNotFoudException(username, e);
		}
	}

	@Override
	public void removeCachedUser(String username) throws UsernameNotFoundException {
		UserDto user = userDetailsCache.get(username);
		if(user!=null) {
			userDetailsCache.remove(username);
			logger.info("user: "+username+" removed from cache." );
		}
	}
}
