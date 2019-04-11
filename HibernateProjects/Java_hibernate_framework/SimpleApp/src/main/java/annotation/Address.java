/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package annotation;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraipob.s
 */
@Entity
@Table(name = "address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")})
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "chvHomNumber")
    private String chvHomNumber;
    @Basic(optional = false)
    @Column(name = "chvRoad")
    private String chvRoad;
    @Basic(optional = false)
    @Column(name = "chvProvince")
    private String chvProvince;
    @Basic(optional = false)
    @Column(name = "chvZipCode")
    private String chvZipCode;
    @Basic(optional = false)
    @Column(name = "adcustid")
    private long adcustid;

    public Address() {
    }

    public Address(Long id) {
        this.id = id;
    }

    public Address(Long id, String chvHomNumber, String chvRoad, String chvProvince, String chvZipCode, long adcustid) {
        this.id = id;
        this.chvHomNumber = chvHomNumber;
        this.chvRoad = chvRoad;
        this.chvProvince = chvProvince;
        this.chvZipCode = chvZipCode;
        this.adcustid = adcustid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChvHomNumber() {
        return chvHomNumber;
    }

    public void setChvHomNumber(String chvHomNumber) {
        this.chvHomNumber = chvHomNumber;
    }

    public String getChvRoad() {
        return chvRoad;
    }

    public void setChvRoad(String chvRoad) {
        this.chvRoad = chvRoad;
    }

    public String getChvProvince() {
        return chvProvince;
    }

    public void setChvProvince(String chvProvince) {
        this.chvProvince = chvProvince;
    }

    public String getChvZipCode() {
        return chvZipCode;
    }

    public void setChvZipCode(String chvZipCode) {
        this.chvZipCode = chvZipCode;
    }

    public long getAdcustid() {
        return adcustid;
    }

    public void setAdcustid(long adcustid) {
        this.adcustid = adcustid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", chvHomNumber=" + chvHomNumber + ", chvRoad=" + chvRoad + ", chvProvince=" + chvProvince + ", chvZipCode=" + chvZipCode + ", adcustid=" + adcustid + '}';
    }

}
