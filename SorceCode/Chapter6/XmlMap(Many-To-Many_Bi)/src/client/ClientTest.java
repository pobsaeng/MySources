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
		Customer cust3 = new Customer("RANA", "TERNATO", 35);
		Address addr1 = new Address("30-32", "Latphroa", "BKK", "10310");
		Address addr2 = new Address("40-50", "Pahonyothin", "BKK", "11111");
		Address addr3 = new Address("55-89", "Bangna", "BKK", "12345");
		cust1.getAddresses().add(addr1);
		cust1.getAddresses().add(addr2);
		cust2.getAddresses().add(addr2);
		cust3.getAddresses().add(addr3);
		
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
		    Customer cust4 = custdao.findByPK(3);
		    custdao.delete(cust4);
		    Customer cust5 = custdao.findByPK(1);
		    System.out.println("Customer4 is: "+cust5.getFirstname());
		    java.util.Iterator<Address> addriter = cust5.getAddresses().iterator();
		    while(addriter.hasNext()){
		    	Address addr = (Address)addriter.next();
		    	System.out.println("Get Addr from Cust: "+ addr.getProvince());
		    }
		    
		    AddressDAO addrdao = new AddressDAO();
		    Address addr = addrdao.findByPK(1);
		    java.util.Iterator<Customer> custiter = addr.getCustomers().iterator();
		    while(custiter.hasNext()){
		    	Customer cust = (Customer)custiter.next();
		    	System.out.println("Get Customer from Address: "+ cust.getFirstname());
		    }
		    Address addr4 = addrdao.findByPK(3);
		 //   addrdao.delete(addr4);
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
