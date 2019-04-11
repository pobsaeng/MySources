package com.company.dao;

import com.company.entity.TbUser;
import com.company.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;

public class UserDao implements Serializable {

    //จำเป็นต้องใช้เมื่อมีการติดต่อ Server หรือ Network ต่างๆ
    private static final long serialVersionUID = -4348166072822664315L;

    public void save(TbUser tbUser) {
//        Open connection
//        Insert
//        Commit
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        sess.save(tbUser);
        sess.getTransaction().commit();
    }

    public void edit(TbUser tbUser) {
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        sess.merge(tbUser);
        sess.getTransaction().commit();
    }

    public void delete(TbUser tbUser) {
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        TbUser user = (TbUser) sess.get(TbUser.class, tbUser.getId());
        sess.delete(user);
        sess.getTransaction().commit();
    }

    public List<TbUser> fineAllCriterial() {
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        Criteria cri = sess.createCriteria(TbUser.class);
        return cri.list();
    }

    public List<TbUser> fineAllNameQuery() {
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        Query q = sess.getNamedQuery("");
        return q.list();
    }

    public TbUser findSingleHQL(Integer userId) {
        System.out.println("*********************Hibernate findAllHQL************************");
        System.out.println("userId: " + userId);
        org.hibernate.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Query query = session.createQuery("select t from TbUser t where t.id = :userId");
        query.setParameter("userId", userId);
        return (TbUser) query.uniqueResult();
    }

    public List<TbUser> queryLike(String user, String pass) {
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        String QUERY = "FROM t FROM TbUser t WHERE t.user =:user Or t.pass =:pass";
        return (List<TbUser>) sess.createQuery(QUERY).setString("user", user).setString("pass", pass).list();
    }
}
