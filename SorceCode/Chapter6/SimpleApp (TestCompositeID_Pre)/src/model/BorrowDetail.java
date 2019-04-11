package model;

import java.util.Date;
import java.io.Serializable;

public class BorrowDetail implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Date returndate;
	private Date borrowdate;
	private Date duedate;
	private Book book;
	private Customer customer;
	
	public BorrowDetail(Customer customer, Date returndate, Date borrowdate,
			Date duedate, Book book) {
		this.returndate = returndate;
		this.borrowdate = borrowdate;
		this.duedate = duedate;
		this.customer = customer;
		this.book = book;	
	}
	
	public BorrowDetail(){
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getReturndate() {
		return returndate;
	}

	public void setReturndate(Date returndate) {
		this.returndate = returndate;
	}

	public Date getBorrowdate() {
		return borrowdate;
	}

	public void setBorrowdate(Date borrowdate) {
		this.borrowdate = borrowdate;
	}

	public Date getDuedate() {
		return duedate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
}

