package com.auth.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class UserDto implements Serializable {
	private static final long serialVersionUID = 5439945553603054387L;
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<GrantedAuthority> authorities;
	
	public UserDto() {
	}
	
	public UserDto(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<GrantedAuthority> getAuthorities() {
		if(authorities==null) {
			authorities = new ArrayList<GrantedAuthority>();
		}
		return authorities;
	}
	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
}
