package com.ppc.eligibility.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ELIGIBILITYSERVICELOG", schema = "SERVICE_MIDDLE_TIER")
public class EligibilityServiceLogEntity implements java.io.Serializable {

    private String id;
    private String clientID;
    private String uID;
    private String customerFirstName;
    private String customerLastName;
    private String token;
    private String timestamp;
    private String rawResponse;
    private String ineligibilityReasonCode;
    private String systemErrorCode;
    private Date createDate;
    private String createdBy;
    private Date updateDate;
    private String updatedBy;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CLIENTID", nullable = true)
    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Column(name = "CUSTOMERUID", nullable = true)
    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    @Column(name = "CUSTOMERFIRSTNAME", nullable = true)
    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    @Column(name = "CUSTOMERLASTNAME", nullable = true)
    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    @Column(name = "TOKEN", nullable = true)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "TIMESTAMP", nullable = true)
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "RAWRESPONSE", nullable = true)

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    @Column(name = "INELIGIBILITYREASONCODE", nullable = true)
    public String getIneligibilityReasonCode() {
        return ineligibilityReasonCode;
    }

    public void setIneligibilityReasonCode(String ineligibilityReasonCode) {
        this.ineligibilityReasonCode = ineligibilityReasonCode;
    }

    @Column(name = "SYSTEMERRORCODE", nullable = true)
    public String getSystemErrorCode() {
        return systemErrorCode;
    }

    public void setSystemErrorCode(String systemErrorCode) {
        this.systemErrorCode = systemErrorCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDATE", length = 7)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "CREATEDBY", nullable = true)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATEDATE", length = 7)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "UPDATEDBY", nullable = true)
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }


}
