package com.auth.exception;

import javax.security.sasl.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    
	private static final long serialVersionUID = 9065163405785722435L;

	public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}