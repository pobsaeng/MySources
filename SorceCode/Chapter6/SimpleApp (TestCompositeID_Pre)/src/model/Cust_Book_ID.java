package model;
import java.io.Serializable;
public class Cust_Book_ID implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long custid;
	private long bookid;
	public long getBookid() {
		return bookid;
	}
	public void setBookid(long bookid) {
		this.bookid = bookid;
	}
	public Cust_Book_ID(){
		
	}
	public long getCustid() {
		return custid;
	}
	public void setCustid(long custid) {
		this.custid = custid;
	}
	
	public Cust_Book_ID(long custid, long bookid)
	{
		this.custid = custid;
		this.bookid = bookid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bookid ^ (bookid >>> 32));
		result = prime * result + (int) (custid ^ (custid >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cust_Book_ID other = (Cust_Book_ID) obj;
		if (bookid != other.bookid)
			return false;
		if (custid != other.custid)
			return false;
		return true;
	}
	
	
}
