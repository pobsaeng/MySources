/*
 * Created on Aug 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Boing
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FactoryUtil {
	public static ApplicationContext context;
	
	public static Object getBean(String name){
	    if (context==null) {
	        context = new ClassPathXmlApplicationContext("applicationContext.xml");
	    }
		return context.getBean(name);
	}
}
