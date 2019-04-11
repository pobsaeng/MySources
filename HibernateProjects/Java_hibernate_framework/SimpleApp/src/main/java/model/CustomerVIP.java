package model;

public class CustomerVIP extends Customer {

	private String frequency;
	
	public CustomerVIP(){
	}
	
public CustomerVIP(String firstname, String surname, int age, String frequency) {
		super(firstname, surname, age);
		this.frequency = frequency;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

}
