package com.company.app;

import com.company.dao.StockDao;
import com.company.dto.Stock;
import com.company.dto.StockDailyRecord;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        StockDao stockDailyDao = new StockDao();
        Stock stock = new Stock();
        stock.setStockCode("7055");
        stock.setStockName("PADINI5");
        StockDailyRecord stockDailyRecords = new StockDailyRecord();
        stockDailyRecords.setPriceOpen(new Float("1"));
        stockDailyRecords.setPriceClose(new Float("1"));
        stockDailyRecords.setPriceChange(new Float("10"));
        stockDailyRecords.setVolume(new BigInteger("3000000"));
        stockDailyRecords.setDate(new Date());
 
        stockDailyRecords.setStockId(stock);
        
        List<StockDailyRecord>stockDailyRecordsList = new ArrayList<StockDailyRecord>();
        stockDailyRecordsList.add(stockDailyRecords);
        stock.setStockDailyRecordList(stockDailyRecordsList);
        
        System.out.println("***************************Insert*************************");
        stockDailyDao.insertCascade(stock);
        System.out.println("Insert : "+stockDailyDao.findById(stock.getStockId()));
        
        System.out.println("***************************Update*************************");
        stockDailyDao.updateCascade(stock);
        System.out.println("Update : "+stockDailyDao.findById(stock.getStockId()));
        
        System.out.println("***************************Delete*************************");
        stockDailyDao.deleteCascade(stock.getStockId());
        System.out.println("Delete done!!");
        
    }
}
