package dao;

import model.Book;
import org.hibernate.Session;
import java.util.List;
import java.util.Arrays;
import util.HibernateUtil;

public class BookDAO {
	
	public void insert(Book book) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.save(book);
	    	
	     }catch(Exception ex){	   
	    	 throw ex;
	     }
	 }
	
	 public void delete(Book book) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.delete(book);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	 
	 public void update(Book book) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.update(book);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	 
	 public Book findByPK(long bookId)throws Exception {
		 
		 Book book = null;
		 try{
	    	 Session session = HibernateUtil.getSession();
	    	 book = (Book)session.get(Book.class, bookId);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return book;
	 }
	 
	 public List<Book> findAll() throws Exception
	 {
		 
		 List<Book> list = null;
		 try{
			 Session session = HibernateUtil.getSession();
	    	 org.hibernate.Query q = session.createQuery("from Book");
	    	 list = toList(q.list());
			
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return list;		 
			
	 }
	 
	 public static List<Book> toList(final List<?> beans)
	 {
	   if ( beans == null )  return null;
	   if ( beans.isEmpty())  return null;
	   int size = beans.size();
	   Book[] list = new Book[size];
	   list = (Book[]) beans.toArray(list);
	   return Arrays.asList(list);
	 }
}

