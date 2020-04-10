package org.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("userAPIService")
public class UserAPIService implements APIService {
	@Value("${user.endpoint}")
	private String endPointURL;

	@Override
	public String getEndPointURL() {
		return endPointURL+"/user/{username}";
	}
}
