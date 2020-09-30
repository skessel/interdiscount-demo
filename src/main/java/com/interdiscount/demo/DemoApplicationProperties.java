package com.interdiscount.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "demo")
public class DemoApplicationProperties {

	private String adminId;
	private String adminUsername;
	private String adminPassword;
	
	private String userId;
	private String userUsername;
	private String userPassword;

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserUsername() {
		return userUsername;
	}

	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}