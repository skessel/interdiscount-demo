package com.interdiscount.demo.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class KeycloakUserRepresentation {
	
	private final String username;
	private final String firstName;
	private final String lastName;

	@JsonCreator
	public KeycloakUserRepresentation(@JsonProperty("username") String username,
			@JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getUsername() {
		return username;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
}
