package client;

import util.HibernateUtil;
import dao.CustomerDAO;
import annotation.Customer;
import annotation.CustomerVIP;

public class ClientTest {
	
	public static void main(String[] args){	
			Customer cust1 = new CustomerVIP("JACK", "Marlet",15,"Always"); 
			Customer cust2 = new Customer("YAN", "BILLANA",20 ); 
		try{
			HibernateUtil.beginTransaction();
			CustomerDAO custdao = new CustomerDAO();
			custdao.insert(cust1);
			custdao.insert(cust2);
			System.out.println("DONE");
			HibernateUtil.commitTransaction();
		}catch(Exception ex){
			System.out.println("ERROR");
			HibernateUtil.rollbackTransaction();
		}finally{
			HibernateUtil.closeSession();
		}
	}
}
