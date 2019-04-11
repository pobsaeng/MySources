package entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraipob.s
 */
@Entity
@Table(name = "stock_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockDetail.findAll", query = "SELECT s FROM StockDetail s")})
public class StockDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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

    public StockDetail(Integer stockDetailId) {
        this.stockDetailId = stockDetailId;
    }

    public StockDetail(Integer stockDetailId, String compName, String compDesc, String remark, Date listedDate) {
        this.stockDetailId = stockDetailId;
        this.compName = compName;
        this.compDesc = compDesc;
        this.remark = remark;
        this.listedDate = listedDate;
    }

    public Integer getStockDetailId() {
        return stockDetailId;
    }

    public void setStockDetailId(Integer stockDetailId) {
        this.stockDetailId = stockDetailId;
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
        hash += (stockDetailId != null ? stockDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDetail)) {
            return false;
        }
        StockDetail other = (StockDetail) object;
        if ((this.stockDetailId == null && other.stockDetailId != null) || (this.stockDetailId != null && !this.stockDetailId.equals(other.stockDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StockDetail{" + "stockDetailId=" + stockDetailId + ", compName=" + compName + ", compDesc=" + compDesc + ", remark=" + remark + ", listedDate=" + listedDate + ", stock=" + stock + '}';
    }


   
}
