package org.auth.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTTokenUtil implements Serializable {

	private static final long serialVersionUID = 3631990210064298203L;
	public static final long JWT_TOKEN_VALIDITY = 10;
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Value("${app.jwt.tokenSecret}")
	private String secret;
	
	//retrieve username from jwt token
		public String getUsernameFromToken(String token) {
			return getClaimFromToken(token, Claims::getSubject);
		}

		//retrieve expiration date from jwt token
		public Date getExpirationDateFromToken(String token) {
			return getClaimFromToken(token, Claims::getExpiration);
		}

		public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
			final Claims claims = getAllClaimsFromToken(token);
			return claimsResolver.apply(claims);
		}
	    //for retrieveing any information from token we will need the secret key
		private Claims getAllClaimsFromToken(String token) {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		}

		//check if the token has expired
		private Boolean isTokenExpired(String token) {
			final Date expiration = getExpirationDateFromToken(token);
			return expiration.before(new Date());
		}

		//generate token for user
		public String generateToken(String username) {
			Map<String, Object> claims = new HashMap<String, Object>();
			return doGenerateToken(claims, username);
		}

		//while creating the token -
		//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
		//2. Sign the JWT using the HS512 algorithm and secret key.
		//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
		//   compaction of the JWT to a URL-safe string 
		private String doGenerateToken(Map<String, Object> claims, String subject) {

			return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 10000))
					.signWith(SignatureAlgorithm.HS512, secret).compact();
		}

		//validate token
		public Boolean validateToken(String token, UserDetails userDetails) {
			final String username = getUsernameFromToken(token);
			Boolean status = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
			logger.info("Validating token for username and token expiry, Status: "+status);
			return status;
		}

}
