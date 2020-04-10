package org.auth.service;

import org.auth.exception.UserNotFoudException;
import org.parser.model.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public abstract class AbstractUserDetailsService implements UserDetailsService {
	public abstract void removeCachedUser(String username);
	public abstract AuthUser getUserDetails(String username) throws UserNotFoudException;
	public abstract UserDetails loadUserById(Long userId);
}
