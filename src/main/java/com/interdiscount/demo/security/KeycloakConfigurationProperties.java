package com.interdiscount.demo.security;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kc")
public class KeycloakConfigurationProperties {

	private URI baseUrl;
	private URI baseAuthUrl;
	private URI baseAdminUrl;
	private URI realmAuthUrl;
	private URI realmAdminUrl;

	private String realm;

	public URI getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(URI baseUrl) {
		this.baseUrl = baseUrl;
	}

	public URI getBaseAuthUrl() {
		return baseAuthUrl;
	}

	public void setBaseAuthUrl(URI baseAuthUrl) {
		this.baseAuthUrl = baseAuthUrl;
	}

	public URI getBaseAdminUrl() {
		return baseAdminUrl;
	}

	public void setBaseAdminUrl(URI baseAdminUrl) {
		this.baseAdminUrl = baseAdminUrl;
	}

	public URI getRealmAuthUrl() {
		return realmAuthUrl;
	}

	public void setRealmAuthUrl(URI realmAuthUrl) {
		this.realmAuthUrl = realmAuthUrl;
	}

	public URI getRealmAdminUrl() {
		return realmAdminUrl;
	}

	public void setRealmAdminUrl(URI realmAdminUrl) {
		this.realmAdminUrl = realmAdminUrl;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

}
