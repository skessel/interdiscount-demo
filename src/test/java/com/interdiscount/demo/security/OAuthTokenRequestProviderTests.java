package com.interdiscount.demo.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;

import com.interdiscount.demo.DemoApplicationTestBase;

public class OAuthTokenRequestProviderTests extends DemoApplicationTestBase {
	
	@Autowired
	private OAuthTokenRequestProvider tokenProvider;
	
	@Test
	public void login() {
		assertThat(this.tokenProvider.login(this.properties.getUserUsername(), this.properties.getAdminPassword())).isNotNull();
	}
	
	@Test
	public void loginInvalid() {
		try {
			assertThat(this.tokenProvider.login(this.properties.getAdminId(), this.properties.getAdminPassword())).isNotNull();
			failBecauseExceptionWasNotThrown(AuthenticationServiceException.class);
		} catch (AuthenticationServiceException e) {
			assertThat(e.getMessage()).contains(Integer.valueOf(HttpStatus.UNAUTHORIZED.value()).toString());
		}
		
	}

}
