package model;
import model.*;
import java.util.Set;
import java.util.HashSet;

public class Cust_Addr {
	
	private static final long serialVersionUID = 1L;
	private String comment;
	private Set<Address> addresses;
	private Set<Customer> customers;
	private Cust_Addr_ID id;
	public Cust_Addr(){}
	public Cust_Addr(String comment){
		this.comment = comment;
		this.customers = new HashSet<Customer>();
		this.addresses = new HashSet<Address>();
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Set<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}
	public Set<Customer> getCustomers() {
		return customers;
	}
	public void setCustomeres(Set<Customer> customers) {
		this.customers = customers;
	}
	public Cust_Addr_ID getId() {
		return id;
	}
	public void setId(Cust_Addr_ID id) {
		this.id = id;
	}
	
	
}

