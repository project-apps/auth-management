package com.auth.model;

import java.io.Serializable;

public class JWTRequest implements Serializable {

	private static final long serialVersionUID = -1738193162651299128L;
	private String username;
	private String password;

	public JWTRequest() {
	}
	public JWTRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}



}
