package com.company.dao;
import com.company.entity.Stock;
import com.company.entity.ViewStock;
import com.company.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class StockDao implements Serializable {

    private static final long serialVersionUID = -6564358536543357089L;

    public List<ViewStock> listViewStock() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //เรื่อง performance แบบนี้เลย
        Query query = session.createSQLQuery("SELECT"
                + " stock.STOCK_ID,"
                + " stock.STOCK_CODE,"
                + " stock.STOCK_NAME,"
                + " stock_detail.COMP_NAME,"
                + " stock_detail.COMP_DESC,"
                + " stock_detail.REMARK,"
                + " stock_detail.LISTED_DATE"
                + " FROM"
                + " stock"
                + " INNER JOIN stock_detail ON stock.STOCK_ID = stock_detail.STOCK_ID").addEntity(ViewStock.class);//addEntity บอกว่า Map กับคลาสไหน
        return query.list();
    }
    
    public List<Stock> listStock() {
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //เรื่อง performance ไม่ดี
        Query query = session.createQuery("select t from Stock t");
        return query.list();
    }
}
