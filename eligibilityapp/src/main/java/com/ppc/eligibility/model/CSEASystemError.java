package com.ppc.eligibility.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CSEASystemError {

	@JsonProperty("ErrorCode")
	private String ErrorCode;

	
	
    public String getErrorCode() {
		return ErrorCode;
	}



	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}



	public CSEASystemError(){
		
	}
}
