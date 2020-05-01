package com.auth.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
    @Component
    public static class Auth {
    	@Value("${jwt.tokenSecret:s3cre2}")
        private String tokenSecret;
    	@Value("${jwt.tokenExpirationMsec:10000}")
    	private long tokenExpirationMsec;
		public String getTokenSecret() {
			return tokenSecret;
		}
		public long getTokenExpirationMsec() {
			return tokenExpirationMsec;
		}
    }
    @Component
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }
}