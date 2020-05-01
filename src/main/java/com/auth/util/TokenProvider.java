package com.auth.util;

import java.util.Base64;
import java.util.Date;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.auth.provider.UserPrincipal;
import org.parser.Base64Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenProvider {

    private static final Log logger = LogFactory.getLog(TokenProvider.class);
    @Autowired
    private AppProperties appProperties;

/*    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }*/

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        return Jwts.builder()
                .setSubject(userPrincipal.getProviderId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
    public String serialize(Object object) {
    	logger.trace("Classloader of userPrincipal-2: "+object.getClass().getClassLoader());
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
	}
    public <T> T deserialize(String data, Class<T> cls) {
    	logger.trace("Classloader of userPrincipal-3: "+cls.getClassLoader());
		return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(data)));
	}
    
	public <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
	}
	
	public String encodeToString(String data) {
		return Base64Parser.encodeToString(data);
	}
	public String decodeTOString(String data) {
		return Base64Parser.decodeToString(data);
	}


}