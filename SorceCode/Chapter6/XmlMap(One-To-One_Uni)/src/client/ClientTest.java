package client;

import util.HibernateUtil;
import dao.CustomerDAO;
import model.Address;
import model.Customer;

public class ClientTest {	

	public static void main(String[] args){
		
		Customer cust1 = new Customer("YAN", "BILLANA",20 );
		Customer cust2 = new Customer("JACK", "Marlet",15); 
		cust1.setAddress(new Address("30-32", "Latphroa", "BKK", "10310"));
		cust2.setAddress(new Address("55-89", "Bangna", "BKK", "12345"));
		try{
	 		HibernateUtil.beginTransaction(); 
	 		CustomerDAO custdao = new CustomerDAO();
		    custdao.insert(cust1);
		    	custdao.insert(cust2);
		    	HibernateUtil.commitTransaction();
		    
	 	 }catch(Exception ex){

	 		ex.printStackTrace ();	 		
	 		HibernateUtil.rollbackTransaction();
	 		System.out.println
	 		("Can not add records into customer, Address");

	 	 }finally {

			 HibernateUtil.closeSession();

		 }
	 	 
	 	 //test delete
	 	try{
	 		HibernateUtil.beginTransaction(); 
	 		CustomerDAO custdao = new CustomerDAO();
		    Customer cust3 = custdao.findByPK(1);
		    custdao.delete(cust3);
		    HibernateUtil.commitTransaction();
		    
	 	 }catch(Exception ex){

	 		ex.printStackTrace ();	 		
	 		HibernateUtil.rollbackTransaction();
	 		System.out.println
	 		("Can not delete records in customer, Address");

	 	 }finally {
			 HibernateUtil.closeSession();
		 }
		
	}	


}
