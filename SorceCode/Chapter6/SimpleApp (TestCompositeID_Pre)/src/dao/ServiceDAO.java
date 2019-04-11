package dao;

import model.Service;
import org.hibernate.Session;
import java.util.Arrays;
import java.util.List;
import util.HibernateUtil;

public class ServiceDAO{

	public void insert(Service service) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.save(service);
	    	
	     }catch(Exception ex){	   
	    	 throw ex;
	     }
	 }
	
	 public void delete(Service service) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.delete(service);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	 
	 public void update(Service service) throws Exception {
	     try{
	    	 Session session = HibernateUtil.getSession();
	    	 session.update(service);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	 }
	 
	 public Service findByPK(long serviceId)throws Exception {
		 
		 Service service = null;
		 try{
	    	 Session session = HibernateUtil.getSession();
	    	 service = (Service)session.get(Service.class, serviceId);
	    	
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return service;
	 }
	 
	 public List<Service> findAll() throws Exception
	 {
		 
		 List<Service> list = null;
		 try{
			 Session session = HibernateUtil.getSession();
	    	 org.hibernate.Query q = session.createQuery("from Service");
	    	 list = toList(q.list());
			
	     }catch(Exception ex){
	    	 throw ex;
	     }
	     return list;		 
			
	 }
	 
	 public static List<Service> toList(final List<?> beans)
	 {
	   if ( beans == null )  return null;
	   if ( beans.isEmpty())  return null;
	   int size = beans.size();
	   Service[] list = new Service[size];
	   list = (Service[]) beans.toArray(list);
	   return Arrays.asList(list);
	 }
}
