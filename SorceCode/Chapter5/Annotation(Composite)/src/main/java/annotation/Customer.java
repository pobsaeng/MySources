package annotation;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import annotation.Address;

@Entity
@Table(name="customer") 
public class Customer {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	@Column(name="chvFirNam")
	private String firstname;	
	
	@Column(name="chvSurNam")
	private String surname;
	
	@Column(name="intAge")
	private int age;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Cust_Addr", 
			joinColumns = { @JoinColumn(name = "cust_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "addr_id") })
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
			this.addresses = new HashSet<Address>(0);
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
	

//…Getter, Setter methods
		
}
