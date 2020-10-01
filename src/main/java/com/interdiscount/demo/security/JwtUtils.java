package com.interdiscount.demo.security;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.StringUtils;

public class JwtUtils {

	private static final String MISSING_SIGNATURE_VERIFIER_ERROR_CODE = "missing_signature_verifier";

	public static JwtDecoder getJwtDecoder(ClientRegistration clientRegistration) {

		if (!StringUtils.hasText(clientRegistration.getProviderDetails().getJwkSetUri())) {
			OAuth2Error oauth2Error = new OAuth2Error(MISSING_SIGNATURE_VERIFIER_ERROR_CODE, "Failed to find a Signature Verifier for Client Registration: '" + 
					clientRegistration.getRegistrationId() + "'. Check to ensure you have configured the JwkSet URI.", null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}

		return getJwtDecoder(clientRegistration.getProviderDetails().getJwkSetUri());
	}

	public static JwtDecoder getJwtDecoder(String jwkSetUri) {
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

}
