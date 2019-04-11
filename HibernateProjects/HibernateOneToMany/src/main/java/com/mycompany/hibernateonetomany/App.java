package com.mycompany.hibernateonetomany;

import com.company.dao.StockDao;
import com.company.entity.Stock;
import com.company.entity.StockDetail;
import com.company.entity.ViewStock;
import com.company.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

public class App {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        StockDao dao = new StockDao();
        Stock stock = new Stock();
        stock.setStockCode("10020");
        stock.setStockName("Tomson");
        List<ViewStock> list = dao.listViewStock();
        for (ViewStock viewStock : list) {
            System.out.println(viewStock.getStockId());
            System.out.println(viewStock.getStockName());
        }

//        getData();
    }

    private static void getDt() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();//เริ่มทำ Transaction
        Stock stock = new Stock();
        stock.setStockCode("10020");
        stock.setStockName("Tomson");

        StockDetail stactDetail = new StockDetail();
        stactDetail.setCompName("Kraipob Holding Malaysia");
        stactDetail.setCompDesc("one stop shopping");
        stactDetail.setRemark("struts2 hibernate framework");
        stactDetail.setListedDate(new Date());

        stock.setStockDetail(stactDetail);
        stactDetail.setStock(stock);

        session.save(stock);
        session.getTransaction().commit();
        System.out.println("Done");
    }

    private static void getData() {
        System.out.println("*************************************");
        StockDao stdao = new StockDao();
        for (Stock s : stdao.listStock()) {
            System.out.println("Stock: " + s);
            System.out.println("Detail: " + s.getStockDetail());
        }
        System.out.println("*************************************");
    }
}
