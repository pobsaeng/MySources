package annotation;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

@Entity
@Table(name="customer") 
public class Customer {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	@Column(name = "chvFirNam", nullable = false, length=50) 
	private String firstname;	
	
	@Column(name="chvSurNam")
	private String surname;
	
	@Column(name="intAge")
	private int age;
	
	public Customer(){
	}	
	
	public Customer(String firstname, String surname, int age){	
		this.firstname = firstname;
		this.surname = surname;
		this.age = age;
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
