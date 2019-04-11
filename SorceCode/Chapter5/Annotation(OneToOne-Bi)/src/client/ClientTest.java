package client;

import util.HibernateUtil;
import annotation.Customer;
import dao.CustomerDAO;
import annotation.Address;
import dao.AddressDAO;

public class ClientTest {
	
	public static void main(String[] args){
		
		Customer cust1 = new Customer("JACK", "Marlet",15);
		Customer cust2 = new Customer("YAN", "BILLANA",20 ); 
		cust1.setAddress(new Address("40-50", "Pahonyothin", "BKK", "11111"));
		cust2.setAddress(new Address("55-89", "Bangna", "BKK", "12345"));

		try{
			HibernateUtil.beginTransaction();
			CustomerDAO custdao = new CustomerDAO();
			custdao.insert(cust1);
			custdao.insert(cust2);
			HibernateUtil.commitTransaction();
			HibernateUtil.closeSession();
			System.out.println("DONE");

		}catch(Exception ex){

			System.out.println("ERROR");
			HibernateUtil.rollbackTransaction();

		}
		
		try{
			HibernateUtil.beginTransaction();
			CustomerDAO custdao = new CustomerDAO();
			Customer cust3 = custdao.findByPK(1);
			custdao.delete(cust3);
			HibernateUtil.commitTransaction();
			HibernateUtil.closeSession();
			System.out.println("DONE");
		}catch(Exception ex){

			System.out.println("ERROR");
			HibernateUtil.rollbackTransaction();

		} finally{

			HibernateUtil.closeSession();
		}
		
		AddressDAO addrdao = new AddressDAO();
		try{
			HibernateUtil.beginTransaction();
			Address addr = addrdao.findByPK(2);
			System.out.println(addr.getCustomer().getFirstname());

		}catch(Exception ex){

			System.out.println("ERROR");
			HibernateUtil.rollbackTransaction();

		}finally{

			HibernateUtil.closeSession();
		}


	}
}
