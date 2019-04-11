package annotation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="address")
public class Address {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	@Column(name="chvHomNo")
	private String homeno;
	@Column(name="chvRoad")
	private String road;
	@Column(name="chvProvince")
	private String province;
	@Column(name="chvZipCode")
	private String zipcode;

	public Address(){
		
	}
	

	public Address(String homeno, String road, String province, String zipcode){
		this.homeno = homeno;
		this.road = road;
		this.province = province;
		this.zipcode = zipcode;
	}
	//ÖGetter and Setter methods(‡À¡◊Õπ°—∫∫∑∑’Ë 4) Ö


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
