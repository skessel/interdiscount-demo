package com.interdiscount.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.GenericFilterBean;

public class OidcBearerTokenAuthenticationFilter extends GenericFilterBean {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final ClientRegistration clientRegistration;
	private final OidcUserService userService;

	public OidcBearerTokenAuthenticationFilter(ClientRegistration clientRegistration,
			OidcUserService userService) {
		this.clientRegistration = clientRegistration;
		this.userService = userService;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String header = request.getHeader("Authorization");

		if (header == null || !header.toLowerCase().startsWith("bearer ") || 
				SecurityContextHolder.getContext().getAuthentication() != null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String jwtToken = header.split(" ")[1].trim();
		this.log.debug("Try to authenticate user with JWT token {} to Provider {}", jwtToken, this.clientRegistration.getProviderDetails());
		
		try {
			Jwt jwt =JwtUtils.getJwtDecoder(this.clientRegistration).decode(jwtToken);
			OAuth2AccessToken accessToken = new OAuth2AccessToken(TokenType.BEARER, jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt());
			OidcIdToken idToken = new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());
			
			OidcUserRequest userRequest = new OidcUserRequest(this.clientRegistration, accessToken, idToken);
			OidcUser user = this.userService.loadUser(userRequest);
			
			Authentication auth = new OAuth2AuthenticationToken(user, user.getAuthorities(), this.clientRegistration.getClientId());
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (AuthenticationException e) {
			this.log.warn("authentication for jwt {} to provider failed", jwtToken, this.clientRegistration.getProviderDetails(), e);
		}
		
		filterChain.doFilter(request, response);
		
	}
}
