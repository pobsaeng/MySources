/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.company.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author totoland
 */
@Entity
@Table(name = "stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stock.findAll", query = "SELECT s FROM Stock s"),
    @NamedQuery(name = "Stock.findByStockId", query = "SELECT s FROM Stock s WHERE s.stockId = :stockId"),
    @NamedQuery(name = "Stock.findByStockCode", query = "SELECT s FROM Stock s WHERE s.stockCode = :stockCode"),
    @NamedQuery(name = "Stock.findByStockName", query = "SELECT s FROM Stock s WHERE s.stockName = :stockName")})
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "STOCK_ID")
    private Integer stockId;
    @Basic(optional = false)
    @Column(name = "STOCK_CODE")
    private String stockCode;
    @Basic(optional = false)
    @Column(name = "STOCK_NAME")
    private String stockName;
    @OneToMany(targetEntity = StockDailyRecord.class, cascade = CascadeType.ALL, mappedBy = "stockId",fetch = FetchType.EAGER)
    private List<StockDailyRecord> stockDailyRecordList;

    public Stock() {
    }

    public Stock(Integer stockId) {
        this.stockId = stockId;
    }

    public Stock(Integer stockId, String stockCode, String stockName) {
        this.stockId = stockId;
        this.stockCode = stockCode;
        this.stockName = stockName;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @XmlTransient
    public List<StockDailyRecord> getStockDailyRecordList() {
        return stockDailyRecordList;
    }

    public void setStockDailyRecordList(List<StockDailyRecord> stockDailyRecordList) {
        this.stockDailyRecordList = stockDailyRecordList;
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
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.stockId == null && other.stockId != null) || (this.stockId != null && !this.stockId.equals(other.stockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.company.dto.Stock[ stockId=" + stockId + " ]";
    }
    
}
