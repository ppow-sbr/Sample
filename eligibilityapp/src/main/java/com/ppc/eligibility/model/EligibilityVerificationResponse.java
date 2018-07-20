package com.ppc.eligibility.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;


public class EligibilityVerificationResponse {

	@JsonProperty("kickoutReasons")
	private ArrayList<String> kickoutReasons;
	@JsonProperty("verifiedSalary")
	private Double verifiedSalary;
	@JsonProperty("salarySource")
	private String salarySource;
	@JsonProperty("verifiedSalaryDate")
	private Date verifiedSalaryDate;
	@JsonProperty("verifiedHireDate")
	private Date verifiedHireDate;
	@JsonProperty("employmentType")
	private String employmentType;
	@JsonProperty("eligibilityFileDate")
	private Date eligibilityFileDate;
	@JsonProperty("verifiedPayDate")
	private Date verifiedPayDate;
	@JsonProperty("payFrequency")
	private String payFrequency;
	@JsonProperty("daysFileLastVerified")
	private int daysFileLastVerified;
	@JsonProperty("responseCode")
	private String responseCode = "200-OK";
	@JsonProperty("payrollId")
	private String payrollId;

	public String getPayrollId() {
		return payrollId;
	}

	public void setPayrollId(String payrollId) {
		this.payrollId = payrollId;
	}

	public ArrayList<String> getKickoutReasons() {
		return kickoutReasons;
	}

	public void setKickoutReasons(ArrayList<String> kickoutReasons) {
		this.kickoutReasons = kickoutReasons;
	}

	public Double getVerifiedSalary() {
		return verifiedSalary;
	}

	public void setVerifiedSalary(Double verifiedSalary) {
		this.verifiedSalary = verifiedSalary;
	}

	public String getSalarySource() {
		return salarySource;
	}

	public void setSalarySource(String salarySource) {
		this.salarySource = salarySource;
	}

	public Date getVerifiedSalaryDate() {
		return verifiedSalaryDate;
	}

	public void setVerifiedSalaryDate(Date verifiedSalaryDate) {
		this.verifiedSalaryDate = verifiedSalaryDate;
	}

	public Date getVerifiedHireDate() {
		return verifiedHireDate;
	}

	public void setVerifiedHireDate(Date verifiedHireDate) {
		this.verifiedHireDate = verifiedHireDate;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public Date getEligibilityFileDate() {
		return eligibilityFileDate;
	}

	public void setEligibilityFileDate(Date eligibilityFileDate) {
		this.eligibilityFileDate = eligibilityFileDate;
	}

	public Date getVerifiedPayDate() {
		return verifiedPayDate;
	}

	public void setVerifiedPayDate(Date verifiedPayDate) {
		this.verifiedPayDate = verifiedPayDate;
	}

	public String getPayFrequency() {
		return payFrequency;
	}

	public void setPayFrequency(String payFrequency) {
		this.payFrequency = payFrequency;
	}

	public int getDaysFileLastVerified() {
		return daysFileLastVerified;
	}

	public void setDaysFileLastVerified(int daysFileLastVerified) {
		this.daysFileLastVerified = daysFileLastVerified;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}


	
	
}
