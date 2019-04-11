package model;
import java.util.*;

public class Cust_Book {
	private Cust_Book_ID id;
	private String comment;
    private Customer customer;
	private Book book;
		
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Cust_Book_ID getId() {
		return id;
	}
	public void setId(Cust_Book_ID id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	

	public Cust_Book(){
		
	}
	
	public Cust_Book(String comment, Customer cust, Book book){
		this.comment = comment;
		this.id = new Cust_Book_ID(cust.getId(), book.getId());
		
	}
	
}
