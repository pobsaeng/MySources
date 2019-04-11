package dao;

import model.*;
import model.Cust_Book;
import org.hibernate.Session;


import util.HibernateUtil;


public class Cust_BookDAO{
	
	public void insert(Cust_Book cust_book) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.save(cust_book);
	    	
	     }catch(Exception ex){	   
	    	 throw ex;
	     }
	 }
	
	public void update(Cust_Book cust_book) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.update(cust_book);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	
	public void delete(Cust_Book cust_book)throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.delete(cust_book);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	
 public Cust_Book findByPK(Cust_Book_ID temp)throws Exception {
		 
	 Cust_Book cust_book = null;
		 try{
	    	 Session session = HibernateUtil.getSession();
	    	 cust_book = (Cust_Book)session.get(Cust_Book.class, temp);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return cust_book;
	 }


}


