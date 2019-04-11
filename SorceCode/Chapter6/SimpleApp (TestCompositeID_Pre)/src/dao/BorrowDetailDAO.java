package dao;

import java.util.Arrays;
import java.util.List;
import model.BorrowDetail;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;


import util.HibernateUtil;


public class BorrowDetailDAO{
	
	public void insert(BorrowDetail borrowdetail) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.save(borrowdetail);
	    	
	     }catch(Exception ex){	   
	    	 throw ex;
	     }
	 }
	
	public void update(BorrowDetail borrowdetail) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.update(borrowdetail);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	
	public void delete(BorrowDetail borrowdetail) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.delete(borrowdetail);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	
	public List<BorrowDetail> findByBookId(int bookId)throws Exception {
		List<BorrowDetail> list = null;
		 try{
	    	 Session session = HibernateUtil.getSession();
	    	 org.hibernate.Query q = 
 session.createQuery(" from BorrowDetail d where d.book = :book");
	    	 q.setInteger("book",1);
			 list = toList(q.list());
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return list;
	 }
	
	public List<BorrowDetail> findByCustId(int customerid)throws Exception {
		List<BorrowDetail> list = null;
		 try{
	    	 Session session = HibernateUtil.getSession();
	    	 org.hibernate.Query q = session.createQuery
                 (" from BorrowDetail d where d.customer = :customer");
	    	 q.setInteger("customer",customerid);
			 list = toList(q.list());
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return list;
	 }
	
	
	 public List<BorrowDetail> findByCustNam(String name) throws Exception
	 {
		 List<BorrowDetail> list = null;
		 try{
			 
			 Session session = HibernateUtil.getSession();			  
			 Criteria crit = session.createCriteria(BorrowDetail.class);
			 crit.setFetchMode("customer", FetchMode.JOIN);
			 list = toList(crit.list());
			
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return list;		 
			
	 }

	public static List<BorrowDetail> toList(final List<?> beans)
	 {
	   if ( beans == null )  return null;
	   if ( beans.isEmpty())  return null;
	   int size = beans.size();
	   BorrowDetail[] list = new BorrowDetail[size];
	   list = (BorrowDetail[]) beans.toArray(list);
	   return Arrays.asList(list);
	 }

}

