package com.company.myhibernate1;

import dao.StockDao;
import entity.Stock;
import entity.StockDetail;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
//         ProvinceDao provinceDao = new ProvinceDao();
//        for (Province pro : provinceDao.findAll()) {
//            System.out.println("Id : " + pro);
//            System.out.println("ProvinceCode : " + pro.getProvinceCode());
//            System.out.println("ProvinceName : " + pro.getProvinceName());
//        }
//        System.out.println("Insert Stock");
        Stock stock = new Stock();
//        stock.setStockId(9);
        stock.setStockCode("0002");
        stock.setStockName("OOP");
//        
        System.out.println("Insert StockDetail");
        StockDetail stockDetail = new StockDetail();
        stockDetail.setCompDesc("รายการหนังสือ");
        stockDetail.setCompName("java เบื้องต้น");
        stockDetail.setRemark("ขายที่ซีเอ็ด");
        stockDetail.setListedDate(new Date());
        stockDetail.setStock(stock);

        stock.setStockDetail(stockDetail);
        StockDao stockDao = new StockDao();

        stockDao.insertCascade(stock);
//        System.out.println("Insert : " + stockDao.findById(stock.getStockId()));

//        System.out.println("***************************Update*************************");
//        stockDao.updateCascade(stock);
//        System.out.println("Update : " + stockDao.findById(stock.getStockId()));
//        System.out.println("***************************Delete*************************");
//        stockDao.deleteCascade(stock.getStockId());
//        System.out.println("Delete done!!");
//        System.out.println("insert complete");
    }
}
