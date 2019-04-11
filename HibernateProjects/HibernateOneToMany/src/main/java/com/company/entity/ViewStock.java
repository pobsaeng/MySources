package com.company.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;

public class ViewStock implements Serializable {

    private static final long serialVersionUID = -3781704331282500957L;
    @Id //Primary key
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

    @Column(name = "LISTER_DATE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date listedDate;
    //*************************************************************************
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

    @Override
    public String toString() {
        return "ViewStock{" + "stockId=" + stockId + ", stockCode=" + stockCode + ", stockName=" + stockName + ", compName=" + compName + ", compDesc=" + compDesc + ", remark=" + remark + ", listedDate=" + listedDate + '}';
    }

}
