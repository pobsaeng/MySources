/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.company.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author totoland
 */
@Entity
@Table(name = "stock_detail")
public class StockDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STOCK_DETAIL_ID")
    private Integer stockDetailId;
    @Basic(optional = false)
    @Column(name = "COMP_NAME")
    private String compName;
    @Basic(optional = false)
    @Column(name = "COMP_DESC")
    private String compDesc;
    @Basic(optional = false)
    @Column(name = "REMARK")
    private String remark;
    @Basic(optional = false)
    @Column(name = "LISTED_DATE")
    @Temporal(TemporalType.DATE)
    private Date listedDate;
    @JoinColumn(name = "STOCK_ID", referencedColumnName = "STOCK_ID")
    @OneToOne(targetEntity = Stock.class,fetch = FetchType.EAGER)
    private Stock stock;

    public StockDetail() {
    }


    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompDesc() {
        return compDesc;
    }

    public void setCompDesc(String compDesc) {
        this.compDesc = compDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getListedDate() {
        return listedDate;
    }

    public void setListedDate(Date listedDate) {
        this.listedDate = listedDate;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "StockDetail{" + "stockDetailId=" + stockDetailId + ", compName=" + compName + ", compDesc=" + compDesc + ", remark=" + remark + ", listedDate=" + listedDate + '}';
    }

    /**
     * @return the stockDetailId
     */
    public Integer getStockDetailId() {
        return stockDetailId;
    }

    /**
     * @param stockDetailId the stockDetailId to set
     */
    public void setStockDetailId(Integer stockDetailId) {
        this.stockDetailId = stockDetailId;
    }

    
    
}
