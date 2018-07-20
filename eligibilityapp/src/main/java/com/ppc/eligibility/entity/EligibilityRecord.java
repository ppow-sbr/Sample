/**
 *
 */
package com.ppc.eligibility.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "TARGET_CUST_ATTRIBS_V", schema = "SERVICE_MIDDLE_TIER")
public class EligibilityRecord implements java.io.Serializable {
    private Double sno;
    private BigDecimal clientId;
    private String customerId;
    private String firstName;
    private String lastName;
    private Date verificationDate;
    private Date creationDate;
    private Date modificationDate;
    private String ssn;
    private String payrollId;
    private String employmentType;
    private String payFrequency;
    private String status;
    private String salary;
    private String hireDate;

    /**
     * @return the clientId
     */
    @Column(name = "HOSTID", nullable = false)
    public BigDecimal getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(final BigDecimal clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the customerId
     */
    @Column(name = "CUSTID", nullable = false)
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the firstName
     */
    @Column(name = "FIRSTNAME", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    @Column(name = "LASTNAME", nullable = false)
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the verificationDate
     */
    //	@Column(name = "VERIFICATIONDATE", nullable = true)
    //	public Date getVerificationDate() {
    //		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:33:42");
    //        Date dateStr=null;
    //		try {
    //			dateStr = formatter.parse(verificationDate);
    //		} catch (ParseException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}
    //		return dateStr;
    //	}
    @Column(name = "VERIFICATIONDATE", nullable = true)
    public Date getVerificationDate() {
        return verificationDate;
    }

    /**
     * @param verificationDate the verificationDate to set
     */
    public void setVerificationDate(final Date verificationDate) {
        this.verificationDate = verificationDate;
    }

    /**
     * @return the creationDate
     */
    @Column(name = "CREATIONDATE", nullable = true)
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the modificationDate
     */
    @Column(name = "MODIFICATIONDATE", nullable = true)
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(final Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return the ssn
     */
    @Column(name = "SSN", nullable = false)
    public String getSsn() {
        return ssn;
    }

    /**
     * @param ssn the ssn to set
     */
    public void setSsn(final String ssn) {
        this.ssn = ssn;
    }

    /**
     * @return the payrollId
     */
    @Column(name = "PAYROLLID", nullable = true)
    public String getPayrollId() {
        return payrollId;
    }

    /**
     * @param payrollId the payrollId to set
     */
    public void setPayrollId(final String payrollId) {
        this.payrollId = payrollId;
    }

    /**
     * @return the employmentType
     */
    @Column(name = "EMPLOYMENT", nullable = true)
    public String getEmploymentType() {
        return employmentType;
    }

    /**
     * @param employmentType the employmentType to set
     */
    public void setEmploymentType(final String employmentType) {
        this.employmentType = employmentType;
    }

    /**
     * @return the payFrequency
     */
    @Column(name = "PAYFREQ", nullable = true)
    public String getPayFrequency() {
        return payFrequency;
    }

    /**
     * @param payFrequency the payFrequency to set
     */
    public void setPayFrequency(final String payFrequency) {
        this.payFrequency = payFrequency;
    }

    /**
     * @return the status
     */
    @Column(name = "STATUS", nullable = true)
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return the salary
     */
    @Column(name = "SALARY", nullable = true)
    public String getSalary() {
        return salary;
    }

    /**
     * @param salary the salary to set
     */
    public void setSalary(final String salary) {
        this.salary = salary;
    }

    /**
     * @return the hireDate
     */
    @Column(name = "HIREDATE", nullable = true)
    public String getHireDate() {
        return hireDate;
    }

    /**
     * @param hireDate the hireDate to set
     */
    public void setHireDate(final String hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * @return the sno
     */
    @Id
    @Column(name = "SNO", nullable = true)
    public Double getSno() {
        return sno;
    }

    /**
     * @param sno the sno to set
     */
    public void setSno(final Double sno) {
        this.sno = sno;
    }


}
