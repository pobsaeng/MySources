package model;
import java.util.HashSet;
import java.util.Set;

public class Service {
	private long id;
	private String des;
	private Set<Customer> customers;
	
	public Service(){
		
	}
	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Service(String des) {
		this.des = des;
		this.customers = new HashSet<Customer>();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
