package com.interdiscount.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import com.interdiscount.demo.security.OAuthTokenRequestProvider;

@Configuration
public class DemoApplicationTestConfiguration {
	
	protected final static String TEST_OAUTH_CLIENT = "procuration";
	
	@Autowired
	protected ClientRegistrationRepository clientRegistrationRepository;
	
	@Bean
	public ClientRegistration clientRegistration() {
		return this.clientRegistrationRepository.findByRegistrationId(TEST_OAUTH_CLIENT);
	}
	
	@Bean
	public OAuthTokenRequestProvider testClient(ClientRegistration clientRegistration) {
		return new OAuthTokenRequestProvider(clientRegistration);
	}
}
