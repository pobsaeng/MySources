package client;

import util.HibernateUtil;
import annotation.Customer;
import dao.CustomerDAO;
import annotation.Address;

public class ClientTest {
	
	public static void main(String[] args){	
		
		Customer cust1 = new Customer("JACK", "Marlet",15);
		cust1.getAddresses().add(new Address("40-50", "Pahonyothin", "BKK", "11111"));
		cust1.getAddresses().add(new Address("55-89", "Bangna", "BKK", "12345"));
			
		try{
				HibernateUtil.beginTransaction();
				CustomerDAO custdao = new CustomerDAO();
				custdao.insert(cust1);
				HibernateUtil.commitTransaction();
				System.out.println("DONE");
		}catch(Exception ex){
				System.out.println("ERROR");
				System.out.println(ex);
				HibernateUtil.rollbackTransaction();
		}finally{
				HibernateUtil.closeSession();
		}
			
	}



}
