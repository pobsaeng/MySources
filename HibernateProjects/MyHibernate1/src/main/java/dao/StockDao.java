/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Stock;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

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

    public Stock findById(Integer id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        return (Stock) session.get(Stock.class, id);

    }

//    public List<ViewStock> listViewStock() {
//
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//
//        Query query = session.createSQLQuery("SELECT "
//                + "stock.STOCK_ID, "
//                + "stock.STOCK_CODE, "
//                + "stock.STOCK_NAME, "
//                + "stock_detail.COMP_NAME, "
//                + "stock_detail.COMP_DESC, "
//                + "stock_detail.REMARK, "
//                + "stock_detail.LISTED_DATE "
//                + "FROM "
//                + "stock "
//                + "INNER JOIN stock_detail ON stock.STOCK_ID = stock_detail.STOCK_ID")
//                .addEntity(ViewStock.class);
//
//        return query.list();
//    }
    public List<Stock> listViewStockHQL() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("select t from Stock t");

        return query.list();

    }

}
