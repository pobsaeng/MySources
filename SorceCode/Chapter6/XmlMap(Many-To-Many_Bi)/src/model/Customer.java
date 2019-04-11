package model;

import model.Address;
import java.util.HashSet;
import java.util.Set;

public class Customer {

	private long id;
	private String firstname;
	private String surname;
	private int age;
	private Set<Address> addresses;
	
	
	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Customer(){
	}	
	
	public Customer(String firstname, String surname, int age){	
		this.firstname = firstname;
		this.surname = surname;
		this.age = age;
		this.addresses = new HashSet<Address>();

	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstname() {
		return firstname;
	}
	

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
}

