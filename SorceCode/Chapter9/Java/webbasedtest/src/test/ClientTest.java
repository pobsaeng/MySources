package test;

import model.Book;
import util.HibernateUtil;
import dao.BookDAO;

public class ClientTest {

	public static void main(String[] args){
				Book book1 = new Book("Basic Java", "For the beginner");
				Book book2 = new Book("Hibernate Java", "OR Mapping");
				Book book3 = new Book("JBoss", "Java Application Server: Open Source");
				
				
				try{
					
					HibernateUtil.beginTransaction();
					
				
					BookDAO bookdao = new BookDAO();
				    bookdao.insert(book1);
				   	bookdao.insert(book2);
				    bookdao.insert(book3);
					HibernateUtil.commitTransaction();
					
			 	 }catch(Exception ex){
			
			 		ex.printStackTrace ();	 		
			 		HibernateUtil.rollbackTransaction();
			 		System.out.println("Can not add books into Book table");
			
			 	 }finally {
			
					 HibernateUtil.closeSession();
			
				 }
			}
	}
			

