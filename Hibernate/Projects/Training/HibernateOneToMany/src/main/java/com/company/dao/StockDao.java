/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.dao;

import com.company.dto.Stock;
import com.company.utils.HibernateUtil;
import java.io.Serializable;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Administrator
 */
public class StockDao implements Serializable{
    private static final long serialVersionUID = -7779193530063353183L;
    
    private Logger logger = Logger.getLogger(StockDao.class);
    
    public void insertCascade(Stock stock){
        logger.info("Hibernate one to many Cascade");
	Session session = HibernateUtil.getSessionFactory().openSession();
 
	session.beginTransaction();
        
        session.save(stock);
        
	session.getTransaction().commit();
        session.close();
	logger.info("Done");
    }
    
    public void deleteCascade(Integer id) {
        logger.info("Hibernate one to many deleteCascade");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        
        Stock stock = (Stock)session.get(Stock.class, id);
        session.delete(stock);
        
        session.getTransaction().commit();
        logger.info("Done");
    }
    
    public void updateCascade(Stock stock){
        logger.info("Hibernate one to many updateCascade");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        
        session.update(stock);
        
        session.getTransaction().commit();
        logger.info("Done");
    }
    
    public Stock findById(Integer id){
        logger.info("Hibernate one to many findById");
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (Stock)session.get(Stock.class, id);
    }
}
