package com.company.app;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.company.dao.StockCategoryDao;
import com.company.entity.Category;
import com.company.entity.Stock;
import com.company.entity.ViewStock;
import com.company.utils.HibernateUtil;

public class App {

    public static void main(String[] args) {
        System.out.println("Hibernate many to many (Annotation)");
        StockCategoryDao stockCategoryDao = new StockCategoryDao();
        
//        Stock stock = new Stock();
//        stock.setStockCode("1001");
//        stock.setStockName("CES-101");
//        stockCategoryDao.saveStockCategory(stock);
//        Stock stock = stockCategoryDao.findById(1);
        
        List<Category> categories = stockCategoryDao.findAll();
        for (Category category : categories) {
        	System.out.println(category);
		}
        
//        List<ViewStock> listViewStock = stockCategoryDao.listViewStock();
//        if(listViewStock == null) return;
//        for (ViewStock viewStock : listViewStock) {
//			System.out.println(viewStock);
//		}
    }
    public void update() {
    	 Session session = HibernateUtil.getSessionFactory().openSession();
         session.beginTransaction();

         Stock stock = new Stock();
         stock.setStockCode("7052");
         stock.setStockName("PADINI");

         Category category1 = new Category("CONSUMER", "CONSUMER COMPANY");
         Category category2 = new Category("INVESTMENT", "INVESTMENT COMPANY");

         //Set 'Stock' values and assigned the 'Categories' values to the 'Stock'
         Set<Category> categories = new HashSet<Category>();
         categories.add(category1);
         categories.add(category2);
         stock.setCategories(categories);

         session.save(stock);
         
         session.getTransaction().commit();
         System.out.println("Done");
    }
    public void save(){
    	 Session session = HibernateUtil.getSessionFactory().openSession();
         session.beginTransaction();

         Stock stock = new Stock();
         stock.setStockCode("7052");
         stock.setStockName("PADINI");

         Category category1 = new Category("CONSUMER", "CONSUMER COMPANY");
         Category category2 = new Category("INVESTMENT", "INVESTMENT COMPANY");

         //Set 'Stock' values and assigned the 'Categories' values to the 'Stock'
         Set<Category> categories = new HashSet<Category>();
         categories.add(category1);
         categories.add(category2);
         stock.setCategories(categories);

         session.save(stock);
         
         session.getTransaction().commit();
         System.out.println("Done");
    }
}
