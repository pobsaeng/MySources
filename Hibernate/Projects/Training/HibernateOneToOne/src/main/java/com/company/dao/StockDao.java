/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import com.company.entity.Stock;
import com.company.entity.ViewStock;
import com.company.utils.HibernateUtil;

/**
 *
 * @author totoland
 */
public class StockDao implements Serializable {

    private static final long serialVersionUID = -6564358536543357089L;

    public void insertCascade(Stock stock) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(stock);

        session.getTransaction().commit();

    }

    public void updateCascade(Stock stock) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.update(stock);

        session.getTransaction().commit();

    }

    public void deleteCascade(Integer id) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        Stock stock = (Stock) session.get(Stock.class, id);
        session.delete(stock);

        session.getTransaction().commit();

    }
    
    public Stock findById(Integer id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session.get(arg0, arg1);
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

    public List<Stock> listViewCriteria() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Stock.class);
//        cr.add(Restrictions.eq("stockCode", "1235")); //เท่ากับ
//        cr.add(Restrictions.gt("stockId", 9)); //มากกว่า
//        cr.add(Restrictions.lt("stockId", 9)); //น้อยกว่า
        Criterion salary = Restrictions.gt("stockCode", "0002");
        Criterion name = Restrictions.between("stockId", 6, 9);

//        cr.add(Restrictions.between("stockId", 6, 9)); //ระหว่าง
        LogicalExpression orExp = Restrictions.and(salary, name);
        cr.add(orExp);

//        cr.addOrder(Order.desc("stockId")); 
        List results = cr.list();
        return results;

    }

}
