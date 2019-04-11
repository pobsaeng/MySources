package com.company.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.company.services.CustomerService;

public class App {
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "Spring-bean.xml" });

		CustomerService cust = (CustomerService) appContext.getBean("customerServiceProxy");

		System.out.println("*************************");
		cust.printName();
		System.out.println("*************************");
		try {
			cust.printThrowException();
		} catch (Exception e) {

		}

	}
}