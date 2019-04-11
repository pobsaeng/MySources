package com.company.entity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "supplier")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
//    @NamedQuery(name = "Supplier.findBySupplierID", query = "SELECT s FROM Supplier s WHERE s.supplierID = :supplierID"),
//    @NamedQuery(name = "Supplier.findBySupplierName", query = "SELECT s FROM Supplier s WHERE s.supplierName = :supplierName"),
//    @NamedQuery(name = "Supplier.findByAddress", query = "SELECT s FROM Supplier s WHERE s.address = :address"),
//    @NamedQuery(name = "Supplier.findByContactName", query = "SELECT s FROM Supplier s WHERE s.contactName = :contactName"),
//    @NamedQuery(name = "Supplier.findByMobile", query = "SELECT s FROM Supplier s WHERE s.mobile = :mobile"),
//    @NamedQuery(name = "Supplier.findByTelephone", query = "SELECT s FROM Supplier s WHERE s.telephone = :telephone"),
//    @NamedQuery(name = "Supplier.findByFax", query = "SELECT s FROM Supplier s WHERE s.fax = :fax"),
//    @NamedQuery(name = "Supplier.findByEmail", query = "SELECT s FROM Supplier s WHERE s.email = :email")})
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SupplierID")
    private String supplierID;
    @Column(name = "SupplierName")
    private String supplierName;
    @Column(name = "Address")
    private String address;
    @Column(name = "ContactName")
    private String contactName;
    @Basic(optional = false)
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "Telephone")
    private String telephone;
    @Basic(optional = false)
    @Column(name = "Fax")
    private String fax;
    @Basic(optional = false)
    @Column(name = "Email")
    private String email;
    @Basic(optional = false)
    @Lob
    @Column(name = "website")
    private String website;
    @Basic(optional = false)
    @Lob
    @Column(name = "remark")
    private String remark;

    public Supplier() {
    }

    public Supplier(String supplierID) {
        this.supplierID = supplierID;
    }

    public Supplier(String supplierID, String mobile, String fax, String email, String website, String remark) {
        this.supplierID = supplierID;
        this.mobile = mobile;
        this.fax = fax;
        this.email = email;
        this.website = website;
        this.remark = remark;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supplierID != null ? supplierID.hashCode() : 0);
        return hash;
    }

//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Supplier)) {
//            return false;
//        }
//        Supplier other = (Supplier) object;
//        if ((this.supplierID == null && other.supplierID != null) || (this.supplierID != null && !this.supplierID.equals(other.supplierID))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "Supplier{" + "supplierID=" + supplierID + ", supplierName=" + supplierName + ", address=" + address + ", contactName=" + contactName + ", mobile=" + mobile + ", telephone=" + telephone + ", fax=" + fax + ", email=" + email + ", website=" + website + ", remark=" + remark + '}';
    }

   
    
}
