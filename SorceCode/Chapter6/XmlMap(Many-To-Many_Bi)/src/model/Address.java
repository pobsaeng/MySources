package model;

import java.util.Set;
import java.util.HashSet;

public class Address {
	
	private long id;
	private String homeno;
	private String road;
	private String province;
	private String zipcode;
	private Set <Customer> customers;
	

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}
	public Address(){
		
	}
	
	public Address(String homeno, String road, String province, String zipcode){
		this.homeno = homeno;
		this.road = road;
		this.province = province;
		this.zipcode = zipcode;
		this.customers = new HashSet<Customer>();
		
	}
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getHomeno() {
		return homeno;
	}

	public void setHomeno(String homeno) {
		this.homeno = homeno;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	
}
