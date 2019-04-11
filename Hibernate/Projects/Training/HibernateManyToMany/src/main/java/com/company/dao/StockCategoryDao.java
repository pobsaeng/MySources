/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.dao;

import com.company.entity.Stock;
import com.company.utils.HibernateUtil;
import java.io.Serializable;
import org.apache.log4j.Logger;
import org.hibernate.Session;

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
}
