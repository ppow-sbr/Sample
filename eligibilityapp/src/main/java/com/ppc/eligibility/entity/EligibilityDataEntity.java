package com.ppc.eligibility.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TARGET_CUST_ATTRIBS", schema = "SERVICE_MIDDLE_TIER")
public class EligibilityDataEntity {

    private EligibilityDataEntityID id;
    private String agency;
    private String local;
    private String firstName;
    private String lastName;
    private String middleName;
    private String payrollID;
    private Date creationDate;
    private Date modificationDate;
    private Date verificationDate;
    private String payFrequency;
    private String status;
    private String createUser;
    //private BigDecimal sno;
    private String uid;

    public EligibilityDataEntity() {
    }

    public EligibilityDataEntity(EligibilityDataEntityID id) {
        this.id = id;
    }

    @EmbeddedId
    @AttributeOverrides({

            @AttributeOverride(name = "hostid", column = @Column(name = "HOSTID")),
            @AttributeOverride(name = "custid", column = @Column(name = "CUSTID")),
    })
    public EligibilityDataEntityID getId() {
        return this.id;
    }

    public void setId(EligibilityDataEntityID id) {
        this.id = id;
    }


    @Column(name = "AGENCY", nullable = true)
    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    @Column(name = "LOCAL", nullable = true)
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Column(name = "FIRSTNAME", nullable = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LASTNAME", nullable = true)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "MI", nullable = true)
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Column(name = "PAYROLLID", nullable = true)
    public String getPayrollID() {
        return payrollID;
    }

    public void setPayrollID(String payrollID) {
        this.payrollID = payrollID;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DT", length = 7)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFICATIONDATE", length = 7)
    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VERIFICATIONDATE", length = 7)
    public Date getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Date verificationDate) {
        this.verificationDate = verificationDate;
    }

    @Column(name = "PAYFREQ", nullable = true)
    public String getPayFrequency() {
        return payFrequency;
    }

    public void setPayFrequency(String payFrequency) {
        this.payFrequency = payFrequency;
    }

    @Column(name = "STATUS", nullable = true)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Column(name = "EMAIL", nullable = true)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(name = "CREATE_USER", nullable = false)
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }


}
