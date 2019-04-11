package com.company.dao;
import com.company.entity.User;
import com.company.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Iterator;

public class UserDao {

    private void insert(User user) {
        Session session;
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();

    }

    private void update(User user) {

        Session session;
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }

    private void query() {

        Session session;
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Iterator iter = session.createQuery("SELECT u FROM User u").iterate();

        while (iter.hasNext()) {
            User tuple = (User) iter.next();
            System.out.println("Id: " + tuple.getId());
            System.out.println("FirstName: " + tuple.getFirstname());
            System.out.println("SurName: " + tuple.getSurname());
            System.out.println("Age: " + tuple.getAge());
        }
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }

    private void delete(User user) {
        Session session;
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }

    public static void main(String[] args) {

//        User user1 = new User();
//        User user2 = new User();
//        
//        user1.setFirstname("FirstName1");
//        user1.setSurname("SurName1");
//        user1.setAge(10);
//        
//        user2.setFirstname("FirstName2");
//        user2.setSurname("SurName2");
//        user2.setAge(20);
//
//        String strAction = "Insert Record";
//        System.out.println("=============" + strAction + "==============");
        UserDao mgr = new UserDao();
//        mgr.insert(user1);
//        mgr.insert(user2);

//        strAction = "Query Record";
//        System.out.println("=============" + strAction + "==============");
//        mgr.query();
//        strAction = "update Record";
//        System.out.println("=============" + strAction + "==============");
//        user2.setId(1);
//        user2.setFirstname("ไกรภพ");
//        user2.setSurname("แสงขุนทด");
//        user2.setAge(20);
//        mgr.update(user2);
//
//        strAction = "Delete Record";
//        System.out.println("=============" + strAction + "==============");
//        user1.setId(4);
//        user1.setFirstname("Pob");
//        user1.setSurname("Saeng");
//        user1.setAge(20);
//        mgr.delete(user1);
//        strAction = "Query Record";
//        System.out.println("=============" + strAction + "==============");
        mgr.query();
        
//        testAnnotation();

    }
}
