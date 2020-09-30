package com.interdiscount.demo.security;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.web.client.HttpClientErrorException;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ResourceOwnerPasswordCredentialsGrant;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.Tokens;

public class OAuthTokenRequestProvider {
	
	private final ClientRegistration clientRegistration;
	
	public OAuthTokenRequestProvider(ClientRegistration clientRegistration) {
		this.clientRegistration = clientRegistration;
	}
	
	public Tokens login(String username, String password) {
		
		// Construct the password grant from the username and password
		AuthorizationGrant passwordGrant = new ResourceOwnerPasswordCredentialsGrant(username, new Secret(password));
		
		// The credentials to authenticate the client at the token endpoint
		ClientID clientID = new ClientID(this.clientRegistration.getClientId());
		Secret clientSecret = new Secret(this.clientRegistration.getClientSecret());
		ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
		
		// The request scope for the token (may be optional)
		Scope scope = new Scope(this.clientRegistration.getScopes().toArray(new String[0]));
		
		// The token endpoint
		String tokenEnpointStirng = this.clientRegistration.getProviderDetails().getTokenUri();
		URI tokenEndpoint = fromHttpUrl(tokenEnpointStirng).build().toUri();
		
		try {
			// Make the token request
			TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, passwordGrant, scope);
			TokenResponse response = TokenResponse.parse(request.toHTTPRequest().send());
			

			if (response.indicatesSuccess()) {
				return response.toSuccessResponse().getTokens();
			} else {
				 // We got an error response...
			    TokenErrorResponse errorResponse = response.toErrorResponse();
			    HttpStatus httpStatus = HttpStatus.resolve(errorResponse.toHTTPResponse().getStatusCode());
			    String message = errorResponse.toJSONObject().toString();
			    throw new HttpClientErrorException(httpStatus, message);
			}
		} catch (Exception e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	public String loginHeader(String username, String password) {
		Tokens tokens = login(username, password);
		return tokens.getAccessToken().getType().getValue().concat(" ").concat(tokens.getAccessToken().getValue()); 
	}

}
