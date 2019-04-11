package client;

import util.HibernateUtil;
import annotation.Customer;
import dao.CustomerDAO;

public class ClientTest {
	
	public static void main(String[] args){
		
		Customer cust1 = new Customer("JACK", "Marlet",15);
		Customer cust2 = new Customer("YAN", "BILLANA",20 ); 
		
		try{
HibernateUtil.beginTransaction();			
CustomerDAO custdao = new CustomerDAO();
			custdao.insert(cust1);
			custdao.insert(cust2);
			HibernateUtil.commitTransaction();
			System.out.println("DONE");

		}catch(Exception ex){

			System.out.println("ERROR");
			HibernateUtil.rollbackTransaction();

		}finally{

			HibernateUtil.closeSession();

		}
	}
}
