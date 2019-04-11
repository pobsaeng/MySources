package client;

import util.HibernateUtil;
import dao.CustomerDAO;
import model.Customer;
import model.CustomerVIP;

public class ClientTest {	

	public static void main(String[] args){
		
		 Customer cust3 = new CustomerVIP("JACK", "Marlet",15,"Always"); 
		 Customer cust2 = new Customer("YAN", "BILLANA",20 ); 
		 Customer cust1 = new Customer("Tamoki", "JAKATA",35 ); 
		 
		try{
	 		HibernateUtil.beginTransaction(); 
	 		CustomerDAO custdao = new CustomerDAO();
		    custdao.insert(cust1);
		    custdao.insert(cust2);
		    custdao.insert(cust3);
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
		    Customer cust4 = custdao.findByPK(1);
		    //custdao.delete(cust4);
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
