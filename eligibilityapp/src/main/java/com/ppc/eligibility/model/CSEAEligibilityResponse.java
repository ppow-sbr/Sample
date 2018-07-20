package com.ppc.eligibility.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CSEAEligibilityResponse {

	@JsonProperty("EligibilityData")
	private EligibilityData eligibilityData;
	@JsonProperty("SystemError")
	private CSEASystemError systemError;

	public EligibilityData getEligibilityData() {
		return eligibilityData;
	}

	public void setEligibilityData(EligibilityData eligibilityData) {
		this.eligibilityData = eligibilityData;
	}

	public CSEASystemError getSystemError() {
		return systemError;
	}

	public void setSystemError(CSEASystemError systemError) {
		this.systemError = systemError;
	}

	@Override
	public String toString(){
		
		return "CustomerID -"+this.eligibilityData.getUID()+",IneligibilityCode-"+this.eligibilityData.getIneligibilityCode()+",Error Code-"+this.systemError.getErrorCode();
	}

}
