package com.ppc.eligibility.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class VerifyEligibilityRequest {

	private String clientID;
	private String cSEAID;
	private String firstName;
	private String lastName;
	private String token;
	private String timestamp;
	
	
	public VerifyEligibilityRequest(String clientID, String cSEAID,
			String firstName, String lastName, String token, String timestamp) {
		super();
		this.clientID = clientID;
		this.cSEAID = cSEAID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.token = token;
		this.timestamp = timestamp;
	}
	
	
	public VerifyEligibilityRequest() {
	}


	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getCSEAID() {
		return cSEAID;
	}
	public void setCSEAID(String cSEAID) {
		this.cSEAID = cSEAID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
