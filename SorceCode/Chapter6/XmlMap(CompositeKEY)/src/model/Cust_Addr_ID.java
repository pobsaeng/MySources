package model;

import java.io.Serializable;

public class Cust_Addr_ID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long adaddrid;
	private long adcustid;
	public long getAdaddrid() {
		return adaddrid;
	}
	public void setAdaddrid(long adaddrid) {
		this.adaddrid = adaddrid;
	}
	public long getAdcustid() {
		return adcustid;
	}
	public void setAdcustid(long adcustid) {
		this.adcustid = adcustid;
	}
	
	
	
}
