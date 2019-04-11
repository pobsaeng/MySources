/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.company.entity.Category;
import com.company.entity.Stock;
import com.company.entity.ViewStock;
import com.company.utils.HibernateUtil;

/**
 *
 * @author Administrator
 */
public class StockCategoryDao implements Serializable {

    private static final long serialVersionUID = 1261178682675775610L;
    private static Logger logger = Logger.getLogger(StockCategoryDao.class);

    public void saveStockCategory(Stock stock) {
        logger.info("Hibernate many to many saveStockCategory");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(stock);

        session.getTransaction().commit();
        logger.info("Done");
    }

    public void deleteStockCategory(Stock stock) {
        logger.info("Hibernate many to many deleteStockCategory");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.delete(stock);

        session.getTransaction().commit();
        logger.info("Done");
    }
    
    public Stock findById(Integer id){
        logger.info("Hibernate one to many findById");
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (Stock)session.get(Stock.class, id);
    }
    public List<Category> findAll(){
//        logger.info("Hibernate one to many findById");
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session.createSQLQuery("select * from category").list();
    }
    
    public void updateCascade(Stock stock){
//        logger.info("Hibernate one to many updateCascade");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        
        session.update(stock);
        
        session.getTransaction().commit();
        logger.info("Done");
    }
    public List<ViewStock> listViewStock() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query query = session.createSQLQuery("SELECT "
                + "stock.STOCK_ID, "
                + "stock.STOCK_CODE, "
                + "stock.STOCK_NAME, "
                + "stock_detail.COMP_NAME, "
                + "stock_detail.COMP_DESC, "
                + "stock_detail.REMARK, "
                + "stock_detail.LISTED_DATE "
                + "FROM "
                + "stock "
                + "INNER JOIN stock_detail ON stock.STOCK_ID = stock_detail.STOCK_ID")
                .addEntity(ViewStock.class);

        return query.list();
    }
}
