/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.company.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author totoland
 */
/***
 * 
 * @author totoland
 */
@Entity
public class ViewStock implements Serializable{
    private static final long serialVersionUID = -3781704331282500957L;
    
    @Id
    @Column(name = "STOCK_ID")
    private Integer stockId;
    
    @Column(name = "STOCK_CODE")
    private String stockCode;
    
    @Column(name = "STOCK_NAME")
    private String stockName;
    
    @Column(name = "COMP_NAME")
    private String compName;
    
    @Column(name = "COMP_DESC")
    private String compDesc;
    
    @Column(name = "REMARK")
    private String remark;
    
    @Column(name ="LISTED_DATE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date listedDate;

    /**
     * @return the stockId
     */
    public Integer getStockId() {
        return stockId;
    }

    /**
     * @param stockId the stockId to set
     */
    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    /**
     * @return the stockCode
     */
    public String getStockCode() {
        return stockCode;
    }

    /**
     * @param stockCode the stockCode to set
     */
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    /**
     * @return the stockName
     */
    public String getStockName() {
        return stockName;
    }

    /**
     * @param stockName the stockName to set
     */
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    /**
     * @return the compName
     */
    public String getCompName() {
        return compName;
    }

    /**
     * @param compName the compName to set
     */
    public void setCompName(String compName) {
        this.compName = compName;
    }

    /**
     * @return the compDesc
     */
    public String getCompDesc() {
        return compDesc;
    }

    /**
     * @param compDesc the compDesc to set
     */
    public void setCompDesc(String compDesc) {
        this.compDesc = compDesc;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the listedDate
     */
    public Date getListedDate() {
        return listedDate;
    }

    /**
     * @param listedDate the listedDate to set
     */
    public void setListedDate(Date listedDate) {
        this.listedDate = listedDate;
    }

    @Override
    public String toString() {
        return "ViewStock{" + "stockId=" + stockId + ", stockCode=" + stockCode + ", stockName=" + stockName + ", compName=" + compName + ", compDesc=" + compDesc + ", remark=" + remark + ", listedDate=" + listedDate + '}';
    }
    
}
