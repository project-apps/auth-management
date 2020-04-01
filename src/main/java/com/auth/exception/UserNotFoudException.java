package com.auth.exception;

import javax.naming.AuthenticationException;

public class UserNotFoudException extends AuthenticationException{

	private static final long serialVersionUID = 3477893656755476361L;

	public UserNotFoudException(String username, Throwable cause){
		super("user not found for: "+username);
	}
	
}
