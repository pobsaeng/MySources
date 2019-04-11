package client;

import util.HibernateUtil;
import annotation.Customer;
import dao.AddressDAO;
import dao.CustomerDAO;
import annotation.Address;

public class ClientTest {
	
	public static void main(String[] args){	
		
		//Insert
		Address addr1 = new Address("55-89", "Bangna", "BKK", "12345");
		Customer cust1 = new Customer("JACK", "Marlet",15);
		cust1.getAddresses().add(new Address("40-50", "Pahonyothin", "BKK", "11111"));
		cust1.getAddresses().add(addr1);
			
		Customer cust2 = new Customer("LATE", "SARAMANA",33);
		cust2.getAddresses().add(new Address("10-22", "Rama6", "BKK", "22222"));
		cust2.getAddresses().add(addr1);
			
		try{
			HibernateUtil.beginTransaction();
			CustomerDAO custdao = new CustomerDAO();
			custdao.insert(cust1);
			custdao.insert(cust2);
			HibernateUtil.commitTransaction();
			System.out.println("DONE");

		}catch(Exception ex){

			System.out.println("ERROR");
			System.out.println(ex);
			HibernateUtil.rollbackTransaction();

		}finally{

			HibernateUtil.closeSession();

		}
		
		//Bidirection
		try	{

			HibernateUtil.beginTransaction();		
			AddressDAO addrdao = new AddressDAO();
			Address addr = addrdao.findByPK(3);
			System.out.println("Addr: "+addr.getRoad());
			java.util.Iterator<Customer> custs = addr.getCustomers().iterator();

			while(custs.hasNext()){
				Customer cust = (Customer)custs.next();
				System.out.println("FirstName: "+cust.getFirstname()); 
			}

			//addrdao.delete(addr);
			HibernateUtil.commitTransaction();

			}catch(Exception ex){

				System.out.println("ERROR");
				System.out.println(ex);
				HibernateUtil.rollbackTransaction();

			}finally{

				HibernateUtil.closeSession();
			}	

			//test delete
			
			try{

				HibernateUtil.beginTransaction();
				CustomerDAO custdao = new CustomerDAO();
				Customer cust3 = custdao.findByPK(1);
				System.out.println("cust's Addr: "+cust3.getFirstname());
				java.util.Iterator<Address> addrs = cust3.getAddresses().iterator();
				while(addrs.hasNext()){

					Address addr = (Address)addrs.next();
					System.out.println("Address: " + addr.getRoad());

				}
					custdao.delete(cust3);
					HibernateUtil.commitTransaction();
						
				}catch(Exception ex){

					System.out.println("ERROR");
					System.out.println(ex);
					HibernateUtil.commitTransaction();

				}finally{

					HibernateUtil.closeSession();
				}

	}
	
}
