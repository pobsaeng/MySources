package com.company.main;

import com.company.dao.UserDao;
import com.company.entity.TblUser;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("*********************Hibernate************************");
        findSingleHQL_EX();
        System.out.println("*********************End************************");
    }

    private static void save_Ex() {
        TblUser user = new TblUser();
        UserDao dao = new UserDao();
        user.setFname("Kraipob");
        user.setLname("Saengkhunthod");
        dao.save(user);
    }

    private static void findAlHQL_EX() {
        UserDao dao = new UserDao();
        List<TblUser> list = dao.findAllHQL();
        for (TblUser tblUser : list) {
            System.out.println(tblUser.getFname());
        }

    }

    private static void findSingleHQL_EX() {
        UserDao dao = new UserDao();
        TblUser l = dao.findSingleHQL(16);
        System.out.println("Name: " + l.getFname());
    }
}
