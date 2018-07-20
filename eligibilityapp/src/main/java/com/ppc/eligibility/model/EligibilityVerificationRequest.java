package com.ppc.eligibility.model;

import java.util.Date;

public class EligibilityVerificationRequest {

	private String CustomerID;
	private String payrollID;
	private String ssn;
	private String hostID;
	private boolean payrollIdReq;
	private String custFirstName;
	private String custLastName;
	private Integer EligDataExpirationInDays;
	private Integer clientMinTenure;
	private String uid;
	private Date selfReportedHireDate;

    private Integer fileAgeThreshold;


    public Integer getFileAgeThreshold() {
        return fileAgeThreshold;
    }

    public void setFileAgeThreshold(Integer fileAgeThreshold) {
        this.fileAgeThreshold = fileAgeThreshold;
    }

	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}
	public String getPayrollID() {
		return payrollID;
	}
	public void setPayrollID(String payrollID) {
		this.payrollID = payrollID;
	}
	
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}
	
	public boolean isPayrollIdReq() {
		return payrollIdReq;
	}
	public void setPayrollIdReq(boolean payrollIdReq) {
		this.payrollIdReq = payrollIdReq;
	}
	public String getCustFirstName() {
		return custFirstName;
	}
	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}
	public String getCustLastName() {
		return custLastName;
	}
	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}
	public Integer getEligDataExpirationInDays() {
		return EligDataExpirationInDays;
	}
	public void setEligDataExpirationInDays(Integer eligDataExpirationInDays) {
		EligDataExpirationInDays = eligDataExpirationInDays;
	}
	public Integer getClientMinTenure() {
		return clientMinTenure;
	}
	public void setClientMinTenure(Integer clientMinTenure) {
		this.clientMinTenure = clientMinTenure;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Date getSelfReportedHireDate() {
		return selfReportedHireDate;
	}
	public void setSelfReportedHireDate(Date selfReportedHireDate) {
		this.selfReportedHireDate = selfReportedHireDate;
	}
	
	
	
	
}
