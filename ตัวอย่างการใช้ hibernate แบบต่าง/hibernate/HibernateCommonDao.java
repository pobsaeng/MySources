package com.ie.icon.dao.hibernate;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateCommonDao extends HibernateDaoSupport {
	protected String getParameter(String param) {
		if ( param == null )
			param = "";
		return "%" + param + "%";
	}
	
	protected String replace(String param) {
	    if ( param == null )
	        return param;
	    
	    return param.replace('*', '%');
	}
	
	protected Date today() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}
	
	public boolean isDatabaseConnected() {
		try {
			Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			//session.createSQLQuery("select 1 as a from dual").addScalar("a", Hibernate.INTEGER).uniqueResult();
			session.connection();
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	//for search like
	public int getMathmode(String param) {
		int result = 0;
		if ( param == null )
			return 0;
		
		if(param.indexOf("*") == 0 && param.lastIndexOf("*") == (param.length()-1)){
			result= 1;
		}else if(param.indexOf("*") == 0){
			result= 2;
		}else if(param.lastIndexOf("*") == (param.length()-1)){
			result= 3;
		}else{
			result= 4;
		}
		
		return result;
	}
	protected String clearStar(String param) {
	    if ( param == null )
	        return param;
	    param = param.replace('*', ' ');
	    param = param.trim();
	    
	    return param;
	}
	
	protected String getParameterRightLike(String param) {
		if ( param == null )
			param = "";
		return param + "%";
	}	
}