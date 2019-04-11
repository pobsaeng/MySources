package client;

import util.HibernateUtil;
import dao.AddressDAO;
import dao.CustomerDAO;
import model.Address;
import model.Customer;

public class ClientTest {	

	public static void main(String[] args){
		
		Customer cust1 = new Customer("YAN", "BILLANA",20 );
		Customer cust2 = new Customer("JACK", "Marlet",15); 
		Address addr1 = new Address("30-32", "Latphroa", "BKK", "10310");
		Address addr2 = new Address("40-50", "Pahonyothin", "BKK", "11111");
		Address addr3 = new Address("55-89", "Bangna", "BKK", "12345");
		Address addr4 = new Address("54-84", "Bangkapi", "BKK", "34322");
		cust1.getAddresses().add(addr1);
		cust1.getAddresses().add(addr2);
		cust2.getAddresses().add(addr3);
		cust2.getAddresses().add(addr4);
		
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
		    Customer cust4 = custdao.findByPK(2);
		    System.out.println("Customer4 is: "+cust4.getFirstname());
		    java.util.Iterator<Address> addriter = cust4.getAddresses().iterator();
		    while(addriter.hasNext()){
		    	Address addr = (Address)addriter.next();
		    	System.out.println("Get Addr from Cust: "+ addr.getProvince());
		    }
		    AddressDAO addrdao = new AddressDAO();
		    Address addr = addrdao.findByPK(3);
		    System.out.println("Get Customer From Address: "+ addr.getCustomer().getFirstname());
		    
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
