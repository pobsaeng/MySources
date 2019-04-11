package com.company.app;

import com.company.dao.StockDao;
import com.company.entity.Stock;
import com.company.entity.StockDetail;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hibernate one to one (Annotation)");
        
//        Stock stock = new Stock();
//        stock.setStockCode("123");
//        stock.setStockName("123");
//        
//        StockDetail stockDetail = new StockDetail();
//        stockDetail.setCompDesc("I'm OK");
//        stockDetail.setCompName("10001");
//        stockDetail.setRemark("1112");
//        stockDetail.setListedDate(new Date());
//        stockDetail.setStock(stock);
//        
//        stock.setStockDetail(stockDetail);
        StockDao stockDao = new StockDao();
        
//        System.out.println("***************************Insert*************************");
//        stockDao.insertCascade(stock);
        System.out.println("Insert : "+stockDao.findById(81));
        
//        Stock _stock = stockDao.findById(81);
//        _stock.setStockName("CRC02");
        
//        StockDetail stockDetail = new StockDetail();
//        stockDetail.setCompDesc("I'm OK");
//        stockDetail.setCompName("10001");
//        stockDetail.setRemark("1112");
//        stockDetail.setListedDate(new Date());
//        stockDetail.setStock(_stock);
//        _stock.setStockDetail(stockDetail);
        
//        System.out.println("***************************Update*************************");
//        stockDao.updateCascade(_stock);
//        System.out.println("Update : "+stockDao.findById(81));
        
//        System.out.println("***************************Delete*************************");
//        stockDao.deleteCascade(stock.getStockId());
//        System.out.println("Delete done!!");
//       List list = stockDao.listViewCriteria();
//        for (Object object : list) {
//            System.out.println("Result: " + object);
//        }
    }
}
