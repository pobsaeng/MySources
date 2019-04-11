package client;

import util.HibernateUtil;
import annotation.Customer;
import dao.CustomerDAO;
import dao.AddressDAO;
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
	
		
		try{
			HibernateUtil.beginTransaction();	
					AddressDAO addrdao = new AddressDAO();
					Address addr = addrdao.findByPK(1);
					System.out.println("First Name: "+ addr.getCustomer().getFirstname());
					CustomerDAO custdao = new CustomerDAO();
					Customer cust = custdao.findByPK(1);
					java.util.Iterator<Address> addresses = cust.getAddresses().iterator();

					while(addresses.hasNext()){
						Address addrtemp = (Address)addresses.next();
						System.out.println("Address Road: "+addrtemp.getRoad());
					}
					 //test delete
					 custdao.delete(cust);
					 HibernateUtil.commitTransaction();
								
				}catch(Exception ex){

					System.out.println("ERROR");
					System.out.println(ex);
					HibernateUtil.rollbackTransaction();

				}finally{

					HibernateUtil.closeSession();

				}

	}
}
