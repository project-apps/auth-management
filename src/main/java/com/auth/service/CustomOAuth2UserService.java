package com.auth.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.auth.exception.OAuth2AuthenticationProcessingException;
import com.auth.provider.OAuth2UserInfo;
import com.auth.provider.OAuth2UserInfoFactory;
import com.auth.provider.UserPrincipal;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	protected static final Log logger = LogFactory.getLog(CustomOAuth2UserService.class);
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user =  super.loadUser(userRequest);
		logger.debug("loading Oauth2User: " + user);
		try {
			return processOAuth2User(userRequest, user);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User user)
			throws javax.security.sasl.AuthenticationException {
		logger.debug("Processing OAuth2User.");
		OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
				userRequest.getClientRegistration().getRegistrationId(), user.getAttributes());

		if (StringUtils.isEmpty(userInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		/*Optional<AuthUser> userOptional = Optional.empty(); //userRepository.findByEmail(oAuth2UserInfo.getEmail());
		AuthUser user;
		if (userOptional.isPresent()) {
			user = userOptional.get();
			if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
				throw new OAuth2AuthenticationProcessingException(
						"Looks like you're signed up with " + user.getProvider() + " account. Please use your "
								+ user.getProvider() + " account to login.");
			}
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
		}*/
		return UserPrincipal.create(userInfo, user);
	}

	
	/*private AuthUser registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		logger.debug("Registring OAuth2User.");
		AuthUser user = new AuthUser();
		user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
		user.setProviderId(oAuth2UserInfo.getProviderId());
		user.setName(oAuth2UserInfo.getName());
		user.setEmail(oAuth2UserInfo.getEmail());
		user.setImageUrl(oAuth2UserInfo.getImageUrl());
		//return userRepository.save(user);
		return user;
	}

	private AuthUser updateExistingUser(AuthUser existingUser, OAuth2UserInfo oAuth2UserInfo) {
		logger.debug("Updating OAuthe2User.");
		existingUser.setName(oAuth2UserInfo.getName());
		existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
		//return userRepository.save(existingUser);
		return existingUser;
	}
	 */
}