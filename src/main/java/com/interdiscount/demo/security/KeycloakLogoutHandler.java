package com.interdiscount.demo.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class KeycloakLogoutHandler extends SecurityContextLogoutHandler {

	private final Logger log;
	private final RestTemplate restTemplate;

	public KeycloakLogoutHandler(RestTemplate restTemplate) {
		this.log = LoggerFactory.getLogger(getClass());
		this.restTemplate = restTemplate;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		super.logout(request, response, authentication);
		propagateLogoutToKeycloak((OidcUser) authentication.getPrincipal());
	}

	private void propagateLogoutToKeycloak(OidcUser user) {

		String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";

		UriComponentsBuilder builder = UriComponentsBuilder //
				.fromUriString(endSessionEndpoint) //
				.queryParam("id_token_hint", user.getIdToken().getTokenValue());

		ResponseEntity<String> logoutResponse = this.restTemplate.getForEntity(builder.toUriString(), String.class);
		if (logoutResponse.getStatusCode().is2xxSuccessful()) {
			this.log.info("Successfulley logged out in Keycloak");
		} else {
			this.log.info("Could not propagate logout to Keycloak");
		}
	}

}