package com.mycompany.main;

import com.company.dao.SupplierDao;
import com.company.entity.Supplier;
import java.util.List;

public class App {

    public static void main(String[] args) {
//        delete();
//        add();

//        getAllSupplier();
        SupplierDao dao = new SupplierDao();
        Supplier s = dao.findById("006");
        System.out.println(s.getSupplierID());
        System.out.println(s.getSupplierName());
    }

    private static void getAllSupplier() {
        SupplierDao dao = new SupplierDao();
        List<Supplier> list = dao.query();
        for (Supplier s : list) {
            System.out.println(s.getSupplierID());
            System.out.println(s.getSupplierName());
            System.out.println(s.getContactName());
            System.out.println(s.getTelephone());
        }

    }

    private static void add() {
        SupplierDao sup_dao = new SupplierDao();
        Supplier sup = new Supplier();
        sup.setSupplierID("001");
        sup.setSupplierName("บ.บัวลอย");
        sup.setAddress("บ้านกระโตน");
        sup.setContactName("ไกรภพ แสงขุนทด");
        sup.setEmail("pob_itbuu@hotmail.co.th");
        sup.setRemark("ติดต่อในเวลาทำการ");
        sup.setTelephone("0805236254");
        sup.setFax("021245875");
        sup.setMobile("0805236254");
        sup.setWebsite("https://www.facebook.com");
        sup_dao.save(sup);
    }

    private static void edit() {
        SupplierDao sup_dao = new SupplierDao();
        Supplier sup = new Supplier();
        sup.setSupplierID("006");
        sup.setSupplierName("บ.กระโตนทอง");
        sup.setAddress("บ้านกระโตน");
        sup.setContactName("ไกรภพ แสงขุนทด");
        sup.setEmail("pob@hotmail.co.th");
        sup.setRemark("ติดต่อในเวลาทำการ");
        sup.setTelephone("0865236254");
        sup.setFax("021245875");
        sup.setMobile("0895236254");
        sup.setWebsite("http://terai.xrea.jp/");
        sup_dao.update(sup);
    }

    private static void delete() {
        SupplierDao sup_dao = new SupplierDao();
        Supplier sup = new Supplier();
        sup.setSupplierID("000");
        sup_dao.delete(sup);
    }
}
