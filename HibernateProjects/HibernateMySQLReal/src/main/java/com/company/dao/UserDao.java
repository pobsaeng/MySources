package com.company.dao;

import com.company.entity.TblUser;
import com.company.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserDao implements Serializable {

    private static final long serialVersionUID = -4348166072822664315L;

    public void save(TblUser tblUser) {
        //Open connection 
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        session.save(tblUser);//Save
        session.getTransaction().commit();//Commit

    }

    public void edit(TblUser tblUser) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        session.update(tblUser);//update
        session.getTransaction().commit();//Commit
    }

    public void delete(TblUser tblUser) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        session.delete(tblUser);//delete
        session.getTransaction().commit();//Commit
    }

    public List<TblUser> findAllHQL() {
        System.out.println("*********************Hibernate findAllHQL************************");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Query query = session.createQuery("select t from TblUser t");
        return query.list();
    }

    public TblUser findSingleHQL(Integer userId) {
        System.out.println("*********************Hibernate findAllHQL************************");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Query query = session.createQuery("select t from TblUser t where t.userId = :userId");
        query.setParameter("userId", userId);
        return (TblUser) query.uniqueResult();
    }

    //SQL โดยตรง
    public List<TblUser> findAllNativeSQL() {
        System.out.println("*********************Hibernate findAllNativeSQL************************");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Query query = session.createQuery("SELECT * FROM TBL_USER");
        return query.list();
    }

    public TblUser findByUserIdNativeSQL(Integer userId) {
        System.out.println("*********************Hibernate findAllNativeSQL************************");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Query query = session.createQuery("SELECT * FROM TBL_USER "
                + " WHERE USER_ID = :userId");
        
        return (TblUser) query.uniqueResult();
    }
    
    public List<TblUser> findByFNameAndLNameNativeSQL(String fName, String lName) {
        System.out.println("*********************Hibernate findAllNativeSQL************************");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Query query = session.createQuery("SELECT * FROM TBL_USER "
                + " WHERE FNAME = :fName AND LNAME = :lName");
        
        query.setParameter("fName", fName);
        query.setParameter("lName", lName);
        return query.list();
    }

    public List<TblUser> findAllCriteria() {
        System.out.println("*********************Hibernate findAllCriteria************************");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction

        Criteria criteria = session.createCriteria(TblUser.class);
        return criteria.list();
    }

    public List<TblUser> findAllNamedQuery() {
        System.out.println("*********************Hibernate findAllNamedQuery************************");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction

        Query query = session.getNamedQuery("TblUser.findAll");
        return query.list();
    }
}
