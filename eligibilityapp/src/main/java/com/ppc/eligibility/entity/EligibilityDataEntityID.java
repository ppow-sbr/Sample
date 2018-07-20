package com.ppc.eligibility.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EligibilityDataEntityID implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3720563279104431678L;
    /**
     *
     */
    private String hostid;
    private String custid;

    public EligibilityDataEntityID() {

    }

    public EligibilityDataEntityID(String hostid, String custid) {
        this.hostid = hostid;
        this.custid = custid;
    }

    @Column(name = "HOSTID", nullable = false)
    public String getHostid() {
        return hostid;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    @Column(name = "CUSTID", nullable = false)
    public String getCustid() {
        return custid.trim();
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((custid == null) ? 0 : custid.hashCode());
        result = prime * result + ((hostid == null) ? 0 : hostid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;


        EligibilityDataEntityID other = (EligibilityDataEntityID) obj;
        if (custid == null) {
            if (other.custid != null)
                return false;
        } else if (!custid.equals(other.custid))
            return false;
        if (hostid == null) {
            if (other.hostid != null)
                return false;
        } else if (!hostid.equals(other.hostid))
            return false;
        return true;
    }


}
