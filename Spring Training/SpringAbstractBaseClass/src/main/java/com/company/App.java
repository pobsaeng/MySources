package com.company;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-bean.xml");

		Customer cust = (Customer) context.getBean("CustomerBean");
		System.out.println(cust.getAction());
                System.out.println(cust.getCountry());
                System.out.println(cust.getType());
	}
}