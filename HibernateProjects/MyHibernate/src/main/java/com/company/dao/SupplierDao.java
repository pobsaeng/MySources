package com.company.dao;

import com.company.entity.Supplier;
import com.company.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class SupplierDao implements Serializable {

    private static final long serialVersionUID = 124371353071596106L;

    public void save(Supplier supplier) {
        //Open connection 
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        session.save(supplier);//Save
        session.getTransaction().commit();//Commit

    }

    public void update(Supplier supplier) {
        //Open connection 
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        session.update(supplier);//update
        session.getTransaction().commit();//Commit  
    }

    public void delete(Supplier supplier) {
        //Open connection 
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Supplier sup = (Supplier) session.get(Supplier.class, supplier.getSupplierID());
        session.delete(sup);
        session.getTransaction().commit();
    }

    public List<Supplier> query() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createSQLQuery("SELECT * FROM Supplier").addEntity(Supplier.class);
        return query.list();
    }

    public Supplier findById(String id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query s = session.createSQLQuery("SELECT * FROM Supplier Where SupplierID='" + id + "'").addEntity(Supplier.class);
        return (Supplier) s.uniqueResult();
    }
}
