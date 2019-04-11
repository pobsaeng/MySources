/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.entity;

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
@Table(name = "tbl_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblUser.findAll", query = "SELECT t FROM TblUser t"),
    @NamedQuery(name = "TblUser.findByUserId", query = "SELECT t FROM TblUser t WHERE t.userId = :userId"),
    @NamedQuery(name = "TblUser.findByUsername", query = "SELECT t FROM TblUser t WHERE t.username = :username"),
    @NamedQuery(name = "TblUser.findByIsActive", query = "SELECT t FROM TblUser t WHERE t.isActive = :isActive"),
    @NamedQuery(name = "TblUser.findByFname", query = "SELECT t FROM TblUser t WHERE t.fname = :fname"),
    @NamedQuery(name = "TblUser.findByLname", query = "SELECT t FROM TblUser t WHERE t.lname = :lname"),
    @NamedQuery(name = "TblUser.findBySex", query = "SELECT t FROM TblUser t WHERE t.sex = :sex"),
    @NamedQuery(name = "TblUser.findByUserGroupId", query = "SELECT t FROM TblUser t WHERE t.userGroupId = :userGroupId"),
    @NamedQuery(name = "TblUser.findByProvinceId", query = "SELECT t FROM TblUser t WHERE t.provinceId = :provinceId")})
public class TblUser implements Serializable {
    //สร้างใหม่
    private static final long serialVersionUID = 2322189299930988779L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private Integer userId;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @Column(name = "FNAME")
    private String fname;
    @Column(name = "LNAME")
    private String lname;
    @Column(name = "SEX")
    private Integer sex;
    @Column(name = "USER_GROUP_ID")
    private Integer userGroupId;
    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    public TblUser() {
    }

    public TblUser(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblUser)) {
            return false;
        }
        TblUser other = (TblUser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }
    //สร้างใหม่
    @Override
    public String toString() {
        return "TblUser{" + "userId=" + userId + ", username=" + username + ", isActive=" + isActive + ", fname=" + fname + ", lname=" + lname + ", sex=" + sex + ", userGroupId=" + userGroupId + ", provinceId=" + provinceId + '}';
    }

}
