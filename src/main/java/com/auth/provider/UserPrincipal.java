package com.auth.provider;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserPrincipal implements OidcUser, UserDetails {
	
	private static final long serialVersionUID = 892106070870210969L;
	private static final Log logger = LogFactory.getLog(UserPrincipal.class);
	private String providerId;
	private String name;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;
	private Map<String, Object> claims;
	private OidcUserInfo userInfo;
	private OidcIdToken idToken;

	public UserPrincipal() {
		logger.debug("Initializing UserPrincipal");
	}

	public UserPrincipal(String providerId, String name, String email,
			Collection<? extends GrantedAuthority> authorities) {
		this.providerId = providerId;
		this.name = name;
		this.email = email;
		this.authorities = authorities;
	}

	public UserPrincipal(Map<String, Object> claims, OidcUserInfo userInfo, OidcIdToken idToken) {
		this.claims = claims;
		this.userInfo = userInfo;
		this.idToken = idToken;
	}

	/*
	 * public static UserPrincipal create(AuthUser user) {
	 * logger.debug("Creating UserPrincipal from AuthUser."); List<GrantedAuthority>
	 * authorities = Collections.singletonList(new
	 * SimpleGrantedAuthority("ROLE_USER")); return new
	 * UserPrincipal(user.getProviderId(), user.getEmail(), authorities); }
	 * 
	 * public static UserPrincipal create(AuthUser user, Map<String, Object>
	 * attributes) {
	 * logger.debug("Creating UserPrincipal from AuthUser and attributes..");
	 * UserPrincipal userPrincipal = UserPrincipal.create(user);
	 * userPrincipal.setAttributes(attributes); return userPrincipal; }
	 */
	public static UserPrincipal create(OAuth2UserInfo auth2UserInfo, OAuth2User user) {
		UserPrincipal userPrincipal = new UserPrincipal(auth2UserInfo.getProviderId(), auth2UserInfo.getName(),
				auth2UserInfo.getEmail(), user.getAuthorities());
		userPrincipal.setAttributes(user.getAttributes());
		return userPrincipal;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	private void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getClaims() {
		return claims;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return userInfo;
	}

	@Override
	public OidcIdToken getIdToken() {
		return idToken;
	}

	public String getProviderId() {
		return providerId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserPrincipal [providerId=");
		builder.append(providerId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", email=");
		builder.append(email);
		builder.append(", password=");
		builder.append(password);
		builder.append(", authorities=");
		builder.append(authorities);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append(", claims=");
		builder.append(claims);
		builder.append(", userInfo=");
		builder.append(userInfo);
		builder.append(", idToken=");
		builder.append(idToken);
		builder.append("]");
		return builder.toString();
	}
}
