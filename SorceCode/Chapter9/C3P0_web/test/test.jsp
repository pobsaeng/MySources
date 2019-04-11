<%@ page import="org.hibernate.Transaction" %>
<%@ page import="util.HibernateUtil" %>
<%@ page import="model.Book" %>
<%@ page import="dao.BookDAO" %>

<HTML>
<HEAD> 
<title>Greetings!</title>
</HEAD> 
<BODY>
<%
		
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
		out.print("done");
				
	}catch(Exception ex){
			
		ex.printStackTrace ();	 		
		HibernateUtil.rollbackTransaction();
		out.print("Can not add books into Book table");
	
	}finally {
		
		 HibernateUtil.closeSession();
		
	 }

%> 
<br> 
</BODY> 
</HTML>
