/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.company.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author totoland
 */
@Entity
@Table(name = "stock_daily_record")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockDailyRecord.findAll", query = "SELECT s FROM StockDailyRecord s"),
    @NamedQuery(name = "StockDailyRecord.findByRecordId", query = "SELECT s FROM StockDailyRecord s WHERE s.recordId = :recordId"),
    @NamedQuery(name = "StockDailyRecord.findByPriceOpen", query = "SELECT s FROM StockDailyRecord s WHERE s.priceOpen = :priceOpen"),
    @NamedQuery(name = "StockDailyRecord.findByPriceClose", query = "SELECT s FROM StockDailyRecord s WHERE s.priceClose = :priceClose"),
    @NamedQuery(name = "StockDailyRecord.findByPriceChange", query = "SELECT s FROM StockDailyRecord s WHERE s.priceChange = :priceChange"),
    @NamedQuery(name = "StockDailyRecord.findByVolume", query = "SELECT s FROM StockDailyRecord s WHERE s.volume = :volume"),
    @NamedQuery(name = "StockDailyRecord.findByDate", query = "SELECT s FROM StockDailyRecord s WHERE s.date = :date")})
public class StockDailyRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RECORD_ID")
    private Integer recordId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE_OPEN")
    private Float priceOpen;
    @Column(name = "PRICE_CLOSE")
    private Float priceClose;
    @Column(name = "PRICE_CHANGE")
    private Float priceChange;
    @Column(name = "VOLUME")
    private BigInteger volume;
    @Basic(optional = false)
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "STOCK_ID", referencedColumnName = "STOCK_ID")
    @ManyToOne(targetEntity = Stock.class, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Stock stockId;

    public StockDailyRecord() {
    }

    public StockDailyRecord(Integer recordId) {
        this.recordId = recordId;
    }

    public StockDailyRecord(Integer recordId, Date date) {
        this.recordId = recordId;
        this.date = date;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Float getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(Float priceOpen) {
        this.priceOpen = priceOpen;
    }

    public Float getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(Float priceClose) {
        this.priceClose = priceClose;
    }

    public Float getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Float priceChange) {
        this.priceChange = priceChange;
    }

    public BigInteger getVolume() {
        return volume;
    }

    public void setVolume(BigInteger volume) {
        this.volume = volume;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Stock getStockId() {
        return stockId;
    }

    public void setStockId(Stock stockId) {
        this.stockId = stockId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDailyRecord)) {
            return false;
        }
        StockDailyRecord other = (StockDailyRecord) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.company.dto.StockDailyRecord[ recordId=" + recordId + " ]";
    }
    
}
