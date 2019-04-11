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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author totoland
 */
@Entity
@Table(name = "stock_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockDetail.findAll", query = "SELECT s FROM StockDetail s"),
    @NamedQuery(name = "StockDetail.findByStockId", query = "SELECT s FROM StockDetail s WHERE s.stockId = :stockId"),
    @NamedQuery(name = "StockDetail.findByCompName", query = "SELECT s FROM StockDetail s WHERE s.compName = :compName"),
    @NamedQuery(name = "StockDetail.findByCompDesc", query = "SELECT s FROM StockDetail s WHERE s.compDesc = :compDesc"),
    @NamedQuery(name = "StockDetail.findByRemark", query = "SELECT s FROM StockDetail s WHERE s.remark = :remark"),
    @NamedQuery(name = "StockDetail.findByListedDate", query = "SELECT s FROM StockDetail s WHERE s.listedDate = :listedDate")})
public class StockDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "STOCK_ID")
    private Integer stockId;
    
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
    @OneToOne(optional = false)
    private Stock stock;

    public StockDetail() {
    }

    public StockDetail(Integer stockId) {
        this.stockId = stockId;
    }

    public StockDetail(Integer stockId, String compName, String compDesc, String remark, Date listedDate) {
        this.stockId = stockId;
        this.compName = compName;
        this.compDesc = compDesc;
        this.remark = remark;
        this.listedDate = listedDate;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
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
    public int hashCode() {
        int hash = 0;
        hash += (stockId != null ? stockId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDetail)) {
            return false;
        }
        StockDetail other = (StockDetail) object;
        if ((this.stockId == null && other.stockId != null) || (this.stockId != null && !this.stockId.equals(other.stockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StockDetail{" + "stockId=" + stockId + ", compName=" + compName + ", compDesc=" + compDesc + ", remark=" + remark + ", listedDate=" + listedDate + '}';
    }

    
    
}
