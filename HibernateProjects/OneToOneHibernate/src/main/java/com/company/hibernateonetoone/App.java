package com.company.hibernateonetoone;

import com.company.dao.StockDao;
import com.company.entity.Stock;
import com.company.entity.StockDetail;
import com.company.entity.ViewStock;

public class App {

    public static void main(String[] args) {
        System.out.println("Hibernate one to one (Annotation)");
//        Stock stock = new Stock();
//        stock.setStockName("A-11");
//        stock.setStockCode("CRC50");
//        
//        StockDetail stockDetail = new StockDetail();
//        stockDetail.setStockId();
//        stock.setStockDetail(stockDetail);
        
        StockDao stockDao = new StockDao();
        
        
        
//        for(Stock s : stockDao.listViewStockHQL()){
//            System.out.println("******************listViewStockHQL********************");
//            System.out.println("Stock : "+s);
//            System.out.println("StockDetail : "+s.getStockDetail());
//            System.out.println("**************************************");
//        }
        
         for(ViewStock v : stockDao.listViewStock()){
            System.out.println("***************listViewStock***********************");
            System.out.println("Stock : "+v);
//            System.out.println("StockDetail : "+v.get);
            System.out.println("**************************************");
        }
    }
}
