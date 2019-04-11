package com.company.main;

import com.company.animal.Animal;
import com.company.animal.Report;
import com.company.animal.impl.Cat;
import com.company.animal.impl.reportXLS;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.company.services.CustomerService;

public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context = 
    		new ClassPathXmlApplicationContext(new String[] {"Spring-Customer.xml"});

        
        
        Animal animal = (Cat)context.getBean("cat");
        
        System.out.println(animal.say());
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
//    	CustomerService custA = (CustomerService)context.getBean("customerService");
//    	//custA.setMessage("Message by custA");
//    	System.out.println("Message : " + custA.getMessage());
//    	
//    	//retrieve it again
//    	CustomerService custB = (CustomerService)context.getBean("customerService");
//    	System.out.println("Message : " + custB.getMessage());
    }
}
