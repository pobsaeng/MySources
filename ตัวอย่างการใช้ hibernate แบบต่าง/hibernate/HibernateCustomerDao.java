package com.ie.icon.dao.hibernate;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.constant.Constant;
import com.ie.icon.constant.CustomerConstant;
import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.CustomerDao;
import com.ie.icon.domain.Country;
import com.ie.icon.domain.Province;
import com.ie.icon.domain.Title;
import com.ie.icon.domain.customer.Customer;
import com.ie.icon.domain.customer.CustomerGroup;
import com.ie.icon.domain.customer.CustomerInterface;
import com.ie.icon.domain.customer.CustomerMemberCard;
import com.ie.icon.domain.customer.CustomerPartner;
import com.ie.icon.domain.customer.TransportData;


public class HibernateCustomerDao extends HibernateCommonDao implements
        CustomerDao {
	
	private String getSapId(String customerId) {
		String sapId = customerId;
		
		if ( sapId != null && sapId.length() < CustomerConstant.SAP_LEN ) {
			for ( int index = sapId.length(); index < CustomerConstant.SAP_LEN; index++ ) {
				sapId = "0" + sapId;
			}
		}
		
		return sapId;
	}
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#create(com.ie.icon.domain.customer.Customer)
     */
    public void create(Customer customer) {
        getHibernateTemplate().save(customer);
    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#update(com.ie.icon.domain.customer.Customer)
     */
    public void update(Customer customer) {
        getHibernateTemplate().update(customer);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#delete(com.ie.icon.domain.customer.Customer)
     */
    public void delete(Customer customer) {
        getHibernateTemplate().delete(customer);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#createOrUpdate(com.ie.icon.domain.customer.Customer)
     */
    public void createOrUpdate(Customer customer) {
        getHibernateTemplate().saveOrUpdate(customer);
    }
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCustomerById(java.lang.String)
     */
    public Customer getCustomerById(String customerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("sapId", getSapId(customerId)));
        
        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));

        List result = getHibernateTemplate().findByCriteria(criteria);
        
        if ( result.size() != 1 )
            return null;
        else {
            Customer customer = (Customer)result.get(0);
//            getHibernateTemplate().initialize(customer.getCustomerMemberCards());
        	return customer;
        }
    }    

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCustomersById(java.lang.String)
     */
    public List getCustomersById(String customerId) {
        customerId = replace(customerId);
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        
        criteria.add(Restrictions.like("sapId", getSapId(customerId)));
        
        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        criteria.addOrder(Order.desc("lastUpdateDate"));
        return getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);
    }
    
    public List getEmployeesById(String empId){
    	DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
    	criteria.add(Restrictions.like("sapId", "%"+empId));
    	return getHibernateTemplate().findByCriteria(criteria);
    }

    public List getCustomersById(Long customerOid[], String customerId) {
//        customerId = replace(customerId);
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        criteria.add(Restrictions.like("sapId", getSapId(customerId)));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public List getCustomersByIdPartnerType(Long customerOid[], String customerId,String partnerFunctionTypeId) {
//      customerId = replace(customerId);
      DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
      if(customerOid != null){
    	  criteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
      }
      criteria.add(Restrictions.like("sapId", getSapId(customerId)));
      criteria.add(Restrictions.eq("isActive", new Boolean(true)));
      List resultList = getHibernateTemplate().findByCriteria(criteria);
      
//      if(!"".equals(partnerFunctionTypeId)){
//      	criteria.createAlias("partnerType", "partnerType");
//      	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",partnerFunctionTypeId));
//      }
//      return getHibernateTemplate().findByCriteria(criteria);
      List ret3 = new ArrayList();
      if(resultList == null)
      	return null;
      // Add Search customerPartner 
      else{
	        	for(int i = 0;i<resultList.size();i++){	
	        		Customer cust = (Customer)resultList.get(i);
	 	    	   	
	        		if(cust.getCustomerPartners().size() == 0){
	        			if(cust.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
	        				cust.setShowPartner(partnerFunctionTypeId);
		        			ret3.add(cust);
	        			}
	        		}else if(cust.getCustomerPartners() != null && cust.getCustomerPartners().size() > 0){
	 	    			 for(int index = 0;index<cust.getCustomerPartners().size();index++){
		    	    		 CustomerPartner custPart = (CustomerPartner)cust.getCustomerPartners().get(index);
		    	    		 if(custPart.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
		    	    			 if(custPart.getCustomerPartnerId().getCustomerPartnerOid() == cust.getObjectId()){
		    	    				 custPart.getPartnerCustomer().setShowPartner(partnerFunctionTypeId);
		    	    				 ret3.add(custPart.getPartnerCustomer());
		    	    			 }
		    	    		 }   
		    	    	 }
	        		}else{
//	        			if(cust.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
//	        				cust.setShowPartner(partnerFunctionTypeId);
//		        			ret3.add(cust);
//	        			}
	        		}
	        	}
      }
      return ret3;
  }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCustomersByName(java.lang.String)
     */
    public List getCustomersByName(String name) {
        name = getParameterRightLike(replace(name));
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
//        criteria.add(Restrictions.ilike("firstName", name));
        criteria.add(Restrictions.like("firstName", name));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        
        List resultList=getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);

        return resultList;
    }
    
    // get customer without owner soldto
    public List getCustomersByName(Long customerOid[], String name) {
        name = getParameterRightLike(replace(name));
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	 criteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
       
//        criteria.add(Restrictions.ilike("firstName", name));
        criteria.add(Restrictions.like("firstName", name));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        List resultList=getHibernateTemplate().findByCriteria(criteria);

        return resultList;
    }
    
    // get customer by PartnerType
    public List getCustomersByNamePartnerType(Long customerOid[], String name,String partnerFunctionTypeId) {
        name = getParameterRightLike(replace(name));
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
//        criteria.add(Restrictions.ilike("firstName", replace(name)),MatchMode.ANYWHERE);
        criteria.add(Restrictions.like("firstName", replace(name),MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        
//        if(!"".equals(partnerFunctionTypeId)){
//        	criteria.createAlias("partnerType", "partnerType");
//        	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",partnerFunctionTypeId));
//        }
        
        List resultList=getHibernateTemplate().findByCriteria(criteria);
        List ret3 = new ArrayList();
        if(resultList == null)
        	return null;
        // Add Search customerPartner 
        else{
	        	for(int i = 0;i<resultList.size();i++){	
	        		Customer cust = (Customer)resultList.get(i);
	        		if(cust.getCustomerPartners().size() == 0){
	        			if(cust.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
	        				cust.setShowPartner(partnerFunctionTypeId);
		        			ret3.add(cust);
	        			}
	        		}else if(cust.getCustomerPartners() != null && cust.getCustomerPartners().size() > 0){
	 	    	   	 for(int index = 0;index<cust.getCustomerPartners().size();index++){
	    	    		 CustomerPartner custPart = (CustomerPartner)cust.getCustomerPartners().get(index);
	    	    		 if(custPart.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
	    	    			 if(custPart.getCustomerPartnerId().getCustomerPartnerOid() == cust.getObjectId()){
	    	    				 custPart.getPartnerCustomer().setShowPartner(partnerFunctionTypeId);
	    	    				 ret3.add(custPart.getPartnerCustomer());
	    	    			 }
	    	    		 }   
	    	    	 }
	 	    	   	}
	        	}
        }
        return ret3;
    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCustomersBySurname(java.lang.String)
     */
    public List getCustomersBySurname(String surname) {
        surname = getParameterRightLike(replace(surname));
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        
//        criteria.add(Restrictions.ilike("lastName", surname));
        criteria.add(Restrictions.like("lastName", surname));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));

        return getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);
    }
	
    public List getCustomersBySurname(Long customerOid[], String surname) {
        surname = getParameterRightLike(replace(surname));
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	 criteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
//        criteria.add(Restrictions.ilike("lastName", surname));
        criteria.add(Restrictions.like("lastName", surname));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public List getCustomersBySurnamePartnerType(Long customerOid[], String surname,String partnerFunctionTypeId) {
        surname = getParameterRightLike(replace(surname));
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
//        criteria.add(Restrictions.ilike("lastName", surname));
        criteria.add(Restrictions.like("lastName", replace(surname),MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        
//        if(!"".equals(partnerFunctionTypeId)){
//        	criteria.createAlias("partnerType", "partnerType");
//        	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",partnerFunctionTypeId));
//        }
//        
//        return getHibernateTemplate().findByCriteria(criteria);
        
        List resultList= getHibernateTemplate().findByCriteria(criteria);
        List ret3 = new ArrayList();
        if(resultList == null)
        	return null;
        // Add Search customerPartner 
        else{
	        	for(int i = 0;i<resultList.size();i++){	
	        		Customer cust = (Customer)resultList.get(i);
	        		
	        		if(cust.getCustomerPartners().size() == 0){
	        			if(cust.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
	        				cust.setShowPartner(partnerFunctionTypeId);
		        			ret3.add(cust);
	        			}
	        		}else if(cust.getCustomerPartners() != null && cust.getCustomerPartners().size() > 0){
	        			 for(int index = 0;index<cust.getCustomerPartners().size();index++){
		    	    		 CustomerPartner custPart = (CustomerPartner)cust.getCustomerPartners().get(index);
		    	    		 if(custPart.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
		    	    			 if(custPart.getCustomerPartnerId().getCustomerPartnerOid() == cust.getObjectId()){
		    	    				 custPart.getPartnerCustomer().setShowPartner(partnerFunctionTypeId);
		    	    				 ret3.add(custPart.getPartnerCustomer());
		    	    			 }
		    	    		 }   
		    	    	 }
	        		}
	        	}
        }
        return ret3;
    }
	
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCustomersByPhone(java.lang.String)
     */
    public List getCustomersByPhone(String phoneNo) {
        phoneNo = getParameterRightLike(replace(phoneNo));
        List resultList=new ArrayList();
        
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
        criteria1.add(Restrictions.like("phoneNumber1", phoneNo));
        List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
        
        DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
        criteria2.add(Restrictions.like("phoneNumber2", phoneNo));
        List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
        
        DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
        criteria3.add(Restrictions.like("phoneNumber3", phoneNo));
        List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);        
        
        resultList=customers1;
        for(int i=0;i<customers2.size();i++){
        	Customer cs=(Customer)customers2.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }
        for(int i=0;i<customers3.size();i++){
        	Customer cs=(Customer)customers3.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }        
        return resultList;
    }

    public List getCustomersByPhone(Long customerOid[], String phoneNo) {
        phoneNo = getParameterRightLike(replace(phoneNo));
        List resultList=new ArrayList();
        
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria1.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        
        criteria1.add(Restrictions.like("phoneNumber1", phoneNo));
        criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
        
        DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria2.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        
        criteria2.add(Restrictions.like("phoneNumber2", phoneNo));
        criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
        
        DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria3.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        
        criteria3.add(Restrictions.like("phoneNumber3", phoneNo));
        criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);        
        
        DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria4.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        
        criteria4.add(Restrictions.like("phoneNumber4", phoneNo));
        criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers4=getHibernateTemplate().findByCriteria(criteria4, 0, CustomerConstant.SEARCH_SIZE);   
        
        
        DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria5.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        
        criteria5.add(Restrictions.like("fax", phoneNo));
        criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers5=getHibernateTemplate().findByCriteria(criteria5, 0, CustomerConstant.SEARCH_SIZE);   
        
        
        for(int i=0;i<customers2.size();i++){
        	Customer cs=(Customer)customers2.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }
        for(int i=0;i<customers3.size();i++){
        	Customer cs=(Customer)customers3.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }     
        
        for(int i=0;i<customers4.size();i++){
        	Customer cs=(Customer)customers4.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        } 
        
        for(int i=0;i<customers5.size();i++){
        	Customer cs=(Customer)customers5.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        } 
        resultList=customers1;
        return resultList;
    }
    
    public List getCustomersMemberByPhone(String phoneNo) {
//        phoneNo = getParameterRightLike(replace(phoneNo));
        List resultList=new ArrayList();
        
        phoneNo = phoneNo.trim();
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
        criteria1.add(Restrictions.eq("phoneNumber1", phoneNo));
        criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria1.add(Restrictions.eq("customerGroup", "M8"));
        List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
        
        DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
        criteria2.add(Restrictions.eq("phoneNumber2", phoneNo));
        criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria2.add(Restrictions.eq("customerGroup", "M8"));
        List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
        
        DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
        criteria3.add(Restrictions.eq("phoneNumber3", phoneNo));
        criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria3.add(Restrictions.eq("customerGroup", "M8"));
        List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);        
        
        DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
        criteria4.add(Restrictions.eq("phoneNumber4", phoneNo));
        criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria4.add(Restrictions.eq("customerGroup", "M8"));
        List customers4=getHibernateTemplate().findByCriteria(criteria4, 0, CustomerConstant.SEARCH_SIZE);   
        
        
        DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
        criteria5.add(Restrictions.eq("fax", phoneNo));
        criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria5.add(Restrictions.eq("customerGroup", "M8"));
        List customers5=getHibernateTemplate().findByCriteria(criteria5, 0, CustomerConstant.SEARCH_SIZE);   
    
        for(int i=0;i<customers2.size();i++){
        	Customer cs=(Customer)customers2.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }
        for(int i=0;i<customers3.size();i++){
        	Customer cs=(Customer)customers3.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }     
        
        for(int i=0;i<customers4.size();i++){
        	Customer cs=(Customer)customers4.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        } 
        
        for(int i=0;i<customers5.size();i++){
        	Customer cs=(Customer)customers5.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        } 
        resultList=customers1;
        return resultList;
    }
    
    public List getCustomersAllByPhone(String phoneNo) {
//      phoneNo = getParameter(replace(phoneNo));
      List resultList=new ArrayList();
      
      phoneNo = phoneNo.trim();
      DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
      criteria1.add(Restrictions.eq("phoneNumber1", phoneNo));
      criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
      List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
      
      DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
      criteria2.add(Restrictions.eq("phoneNumber2", phoneNo));
      criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
      List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
      
      DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
      criteria3.add(Restrictions.eq("phoneNumber3", phoneNo));
      criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
      List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);        
      
      DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
      criteria4.add(Restrictions.eq("phoneNumber4", phoneNo));
      criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
      List customers4=getHibernateTemplate().findByCriteria(criteria4, 0, CustomerConstant.SEARCH_SIZE);   
      
      
      DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
      criteria5.add(Restrictions.eq("fax", phoneNo));
      criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
      List customers5=getHibernateTemplate().findByCriteria(criteria5, 0, CustomerConstant.SEARCH_SIZE);   
  
      for(int i=0;i<customers2.size();i++){
      	Customer cs=(Customer)customers2.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      }
      for(int i=0;i<customers3.size();i++){
      	Customer cs=(Customer)customers3.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      }     
      
      for(int i=0;i<customers4.size();i++){
      	Customer cs=(Customer)customers4.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      } 
      
      for(int i=0;i<customers5.size();i++){
      	Customer cs=(Customer)customers5.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      } 
      resultList=customers1;
      return resultList;
  }
  
    public List getPosCustomersAllByPhone(String phoneNo) {
//      phoneNo = getParameter(replace(phoneNo));
      List resultList=new ArrayList();
      
      String creditCustomer = "C1";
      String generalCustomer = "Z3";
      
      phoneNo = phoneNo.trim();
      DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
      criteria1.add(Restrictions.eq("phoneNumber1", phoneNo));
      criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
      criteria1.add(Restrictions.ne("customerGroup", creditCustomer));
      criteria1.add(Restrictions.ne("customerGroup", generalCustomer));
      List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
      
      DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
      criteria2.add(Restrictions.eq("phoneNumber2", phoneNo));
      criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
      criteria2.add(Restrictions.ne("customerGroup", creditCustomer));
      criteria2.add(Restrictions.ne("customerGroup", generalCustomer));
      List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
      
      DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
      criteria3.add(Restrictions.eq("phoneNumber3", phoneNo));
      criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
      criteria3.add(Restrictions.ne("customerGroup", creditCustomer));
      criteria3.add(Restrictions.ne("customerGroup", generalCustomer));
      List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);        
      
      DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
      criteria4.add(Restrictions.eq("phoneNumber4", phoneNo));
      criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
      criteria4.add(Restrictions.ne("customerGroup", creditCustomer));
      criteria4.add(Restrictions.ne("customerGroup", generalCustomer));
      List customers4=getHibernateTemplate().findByCriteria(criteria4, 0, CustomerConstant.SEARCH_SIZE);   
      
      
      DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
      criteria5.add(Restrictions.eq("fax", phoneNo));
      criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
      criteria5.add(Restrictions.ne("customerGroup", creditCustomer));
      criteria5.add(Restrictions.ne("customerGroup", generalCustomer));
      List customers5=getHibernateTemplate().findByCriteria(criteria5, 0, CustomerConstant.SEARCH_SIZE);   
  
      for(int i=0;i<customers2.size();i++){
      	Customer cs=(Customer)customers2.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      }
      for(int i=0;i<customers3.size();i++){
      	Customer cs=(Customer)customers3.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      }     
      
      for(int i=0;i<customers4.size();i++){
      	Customer cs=(Customer)customers4.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      } 
      
      for(int i=0;i<customers5.size();i++){
      	Customer cs=(Customer)customers5.get(i);
      	if(!customers1.contains(cs)){
      		customers1.add(cs);
      	}
      } 
      resultList=customers1;
      return resultList;
  }
    
    public List getCustomersByPhonePartnerType(Long customerOid[], String phoneNo,String partnerFunctionTypeId) {
        phoneNo = getParameterRightLike(replace(phoneNo));
        List resultList=new ArrayList();
        
        // ** criteria1 ** //
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria1.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        criteria1.add(Restrictions.like("phoneNumber1", phoneNo));
        criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
        
        // ** criteria2 ** //
        DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria2.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        criteria2.add(Restrictions.like("phoneNumber2", phoneNo));
        criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
        
        // ** criteria3 ** //
        DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria3.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        criteria3.add(Restrictions.like("phoneNumber3", phoneNo));
        criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);        
        
        // ** criteria4 ** //
        DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria4.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        criteria4.add(Restrictions.like("phoneNumber4", phoneNo));
        criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers4=getHibernateTemplate().findByCriteria(criteria4, 0, CustomerConstant.SEARCH_SIZE);    
        
        
        // ** criteria5 ** //
        DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
        if(customerOid != null){
        	criteria5.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
        }
        criteria5.add(Restrictions.like("fax", phoneNo));
        criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
        List customers5=getHibernateTemplate().findByCriteria(criteria5, 0, CustomerConstant.SEARCH_SIZE);    
        
        
        for(int i=0;i<customers2.size();i++){
        	Customer cs=(Customer)customers2.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }
        for(int i=0;i<customers3.size();i++){
        	Customer cs=(Customer)customers3.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }        
        
        for(int i=0;i<customers4.size();i++){
        	Customer cs=(Customer)customers4.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }     
        
        for(int i=0;i<customers5.size();i++){
        	Customer cs=(Customer)customers5.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }   
        
        resultList=customers1;
        
//        return resultList;
        List ret3 = new ArrayList();
        if(resultList == null)
        	return null;
        // Add Search customerPartner 
        else{
	        	for(int i = 0;i<resultList.size();i++){	
	        		Customer cust = (Customer)resultList.get(i);
	        		
	        		if(cust.getCustomerPartners().size() == 0){
	        			if(cust.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
	        				cust.setShowPartner(partnerFunctionTypeId);
		        			ret3.add(cust);
	        			}
	        		}else if(cust.getCustomerPartners() != null && cust.getCustomerPartners().size() > 0){
	        			 for(int index = 0;index<cust.getCustomerPartners().size();index++){
		    	    		 CustomerPartner custPart = (CustomerPartner)cust.getCustomerPartners().get(index);
		    	    		 if(custPart.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
		    	    			 if(custPart.getCustomerPartnerId().getCustomerPartnerOid() == cust.getObjectId()){
		    	    				 custPart.getPartnerCustomer().setShowPartner(partnerFunctionTypeId);
		    	    				 ret3.add(custPart.getPartnerCustomer());
		    	    			 }
		    	    		 }   
		    	    	 }
	        		}
	        	}
        }
        return ret3;
    }
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCustomersByMemberCard(java.lang.String)
     */
    public List getCustomersByMemberCard(String cardNo) {    
    
    	/** retrive sap id from cust_mbr_card use to find customer */
    	DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
        //criteria.add(Restrictions.like("cardNumber", cardNo, MatchMode.ANYWHERE));
    	criteria.add(Restrictions.like("cardNumber", cardNo, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("status", new Character('1')));
        
        List ret = getHibernateTemplate().findByCriteria(criteria);

        if ( ret.size() != 1 ) {
        	return null;
        }
        else {
        	CustomerMemberCard custMemberCard = (CustomerMemberCard)ret.get(0);
	        /** find customer from sap id */
	        DetachedCriteria cusCriteria = DetachedCriteria.forClass(Customer.class);
	        cusCriteria.add(Restrictions.eq("sapId", getSapId(custMemberCard.getSapId())));
	        
	        /** get only partnerFunctionType Sold To */
	        DetachedCriteria partnerCri = cusCriteria.createCriteria( "partnerType" );
	        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
	        
	        return getHibernateTemplate().findByCriteria(cusCriteria);
        }
    }

    public List getCustomersByMemberCard(Long customerOid[], String cardNo) {    
    	/** retrive sap id from cust_mbr_card use to find customer */
    	DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
        criteria.add(Restrictions.like("cardNumber", cardNo, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("status", new Character('1')));
		
        List ret = getHibernateTemplate().findByCriteria(criteria);
        
        if ( ret.size() != 1 ) {
        	return null;
        }
        else {
        	CustomerMemberCard custMemberCard = (CustomerMemberCard)ret.get(0);
	        /** find customer from sap id */
	        DetachedCriteria cusCriteria = DetachedCriteria.forClass(Customer.class);
	        if(customerOid != null){
	        	cusCriteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
	        }
	        
	        cusCriteria.add(Restrictions.eq("sapId", getSapId(custMemberCard.getSapId())));
	        cusCriteria.add(Restrictions.eq("isActive", new Boolean(true)));
	        cusCriteria.addOrder(Order.asc("sapId"));
	        
	        /** get only partnerFunctionType Sold To */
	        DetachedCriteria partnerCri = cusCriteria.createCriteria( "partnerType" );
	        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
	        
	        return getHibernateTemplate().findByCriteria(cusCriteria);
        }
    }
    
    public List getCustomersByMemberCardPartnerType(Long customerOid[], String cardNo,String partnerFunctionTypeId) {  
    	/** retrive sap id from cust_mbr_card use to find customer */
    	DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
//        criteria.add(Restrictions.like("cardNumber", cardNo, MatchMode.ANYWHERE));     
        criteria.add(Restrictions.eq("cardNumber", cardNo));
		criteria.add(Restrictions.eq("status", new Character('1')));
		
        List ret = getHibernateTemplate().findByCriteria(criteria);
        
        List ret3 = new ArrayList();
        
        if ( ret.size() != 1 ) {
        	return null;
        }else {
        	
        	try{
        		
//        		CustomerMemberCard custMemberCard = (CustomerMemberCard)ret.get(0);
//    	        /** find customer from sap id */
//    	        DetachedCriteria cusCriteria = DetachedCriteria.forClass(Customer.class);
//    	        cusCriteria.add(Restrictions.eq("sapId", getSapId(custMemberCard.getSapId())));
//    	        cusCriteria.add(Restrictions.eq("isActive", new Boolean(true)));
//    	        cusCriteria.addOrder(Order.asc("sapId"));
            	CustomerMemberCard custMemberCard = (CustomerMemberCard)ret.get(0);
    	        /** find customer from sap id */
    	        DetachedCriteria cusCriteria = DetachedCriteria.forClass(Customer.class);
    	        if(customerOid != null){
    	        	 cusCriteria.add(Restrictions.not(Restrictions.in("objectId", customerOid)));
    	        }
    	        if(custMemberCard.getSapId() != null &&!custMemberCard.getSapId().equals("")){
    	        	if(custMemberCard.getSapId().substring(0, 2).equals(SystemConfigConstant.EMP_CODE)){
    	        		  cusCriteria.add(Restrictions.eq("sapId", custMemberCard.getSapId()));
    	        	}else{
    	        		  cusCriteria.add(Restrictions.eq("sapId", getSapId(custMemberCard.getSapId())));
    	        	}
    	        }
//    	        cusCriteria.add(Restrictions.eq("sapId", getSapId(custMemberCard.getSapId())));
    	        cusCriteria.add(Restrictions.eq("isActive", new Boolean(true)));
    	        cusCriteria.addOrder(Order.asc("sapId"));
    	        /** get  partnerFunctionType condition */
//    	        if("RE".equals(partnerFunctionTypeId)){
//    	        	DetachedCriteria partnerCri = cusCriteria.createCriteria( "partnerType" );
//    		        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.BILL_TO));
//    		        
//    	        }else if("WE".equals(partnerFunctionTypeId)){
//    	        	DetachedCriteria partnerCri = cusCriteria.createCriteria( "partnerType" );
//    		        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SHIP_TO));
//    	        }
    	        List ret2 = getHibernateTemplate().findByCriteria(cusCriteria);
    	        if(ret2.size() != 1){
    	        	return null;
    	        }
    	        // Add Search customerPartner 
    	        else{
//	    	    	   Customer cust = (Customer)ret2.get(0);
//	    	    	 for(int index = 0;index<cust.getCustomerPartners().size();index++){
//	    	    		 CustomerPartner custPart = (CustomerPartner)cust.getCustomerPartners().get(index);
//	    	    		 
//	    	    		 if(custPart.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
//	    	    			 if(custPart.getCustomerPartnerId().getCustomerPartnerOid() == cust.getObjectId()){
//	    	    				 ret3.add(custPart.getPartnerCustomer());
//	    	    			 }
//	    	    		 }   
//	    	    	 }
	    	    	 

	 	        	for(int i = 0;i<ret2.size();i++){	
	 	        		Customer cust = (Customer)ret2.get(i);
	 	        		
	 	        		if(cust.getCustomerPartners().size() == 0){
		        			if(cust.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
		        				cust.setShowPartner(partnerFunctionTypeId);
			        			ret3.add(cust);
		        			}
		        		}else if(cust.getCustomerPartners() != null && cust.getCustomerPartners().size() > 0){
	 	        			 for(int index = 0;index<cust.getCustomerPartners().size();index++){
	 		    	    		 CustomerPartner custPart = (CustomerPartner)cust.getCustomerPartners().get(index);
	 		    	    		 if(custPart.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
	 		    	    			 if(custPart.getCustomerPartnerId().getCustomerPartnerOid() == cust.getObjectId()){
	 		    	    				 custPart.getPartnerCustomer().setShowPartner(partnerFunctionTypeId);
	 		    	    				 ret3.add(custPart.getPartnerCustomer());
	 		    	    			 }
	 		    	    		 }   
	 		    	    	 }
	 	        		}else{
//	 	        			if(cust.getPartnerType().getPartnerFunctionTypeId().equals(partnerFunctionTypeId)){
//	 	        				cust.setShowPartner(partnerFunctionTypeId);
//	 		        			ret3.add(cust);
//	 	        			}
	 	        		}
	 	        	}
	 	        	
    	        }
    	        return ret3;
    	        
//    	        return getHibernateTemplate().findByCriteria(cusCriteria);
    	        
        	}catch(Exception e){
        		e.printStackTrace();
        		return null;
        	}
        	
        }
    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getTitles()
     */
    public List getTitles() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);
        
        criteria.addOrder(Order.asc("titleId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getProvinces()
     */
    public List getProvinces() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
        criteria.addOrder(Order.asc("nameThai"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCountries()
     */
    public List getCountries() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Country.class);
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public List getCustomerPartners(long customerOid, String partnerFunctionTypeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class);        
        
        DetachedCriteria customerCri = criteria.createCriteria("customer");
        customerCri.add(Restrictions.eq("objectId", new Long(customerOid)));
        
        DetachedCriteria functionTypeCri = criteria.createCriteria("partnerType");
        functionTypeCri.add(Restrictions.eq("partnerFunctionTypeId", partnerFunctionTypeId));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public List getCustomerPartnersBypartnercustomerOid(long partnercustomerOid, String partnerFunctionTypeId , String condition) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class);        
        
//        DetachedCriteria customerCri = criteria.createCriteria("customer");
        criteria.add(Restrictions.eq("customerPartnerId.customerPartnerOid", new Long(partnercustomerOid)));
        if(partnerFunctionTypeId != null && !"".equals(partnerFunctionTypeId)){
        	 DetachedCriteria functionTypeCri = criteria.createCriteria("partnerType");
             functionTypeCri.add(Restrictions.eq("partnerFunctionTypeId", partnerFunctionTypeId));
        }
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public List getCustomerPartners(long customerOid, String partnerFunctionTypeId, boolean isDefault) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class);        
        criteria.add(Restrictions.eq("isDefault", new Boolean(isDefault)));
        
        DetachedCriteria customerCri = criteria.createCriteria("customer");
        customerCri.add(Restrictions.eq("objectId", new Long(customerOid)));
        
        DetachedCriteria functionTypeCri = criteria.createCriteria("partnerType");
        functionTypeCri.add(Restrictions.eq("partnerFunctionTypeId", partnerFunctionTypeId));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerDao#getCustomerPartners(long)
	 */
	public List getCustomerPartners(long customerOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class); 
		DetachedCriteria customerCri = criteria.createCriteria("customer");
		customerCri.add(Restrictions.eq("objectId", new Long(customerOid)));
        return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getCustomerPartnerById(long customerOid ,long customerPartnerOid, String partnerFunctionTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class); 
		
		DetachedCriteria customerCri = criteria.createCriteria("customer");
		customerCri.add(Restrictions.eq("objectId", new Long(customerOid)));
		
		DetachedCriteria customerCri2 = criteria.createCriteria("partnerCustomer");
		customerCri2.add(Restrictions.eq("objectId", new Long(customerPartnerOid)));
	        
	    DetachedCriteria functionTypeCri = criteria.createCriteria("partnerType");
	    functionTypeCri.add(Restrictions.eq("partnerFunctionTypeId", partnerFunctionTypeId));
	        
	    return getHibernateTemplate().findByCriteria(criteria);

//		List ret = getHibernateTemplate().findByCriteria(criteria);
//		  if ( ret.size() > 0 ) {
//	        	CustomerPartner cust_part = (CustomerPartner)ret.get(0);
////				getHibernateTemplate().initialize(cust_part.getCustomerPartners());
//				return cust_part;
//	        }
//			else
//	        	return null;
	}
	
	public List getCustomerPartnerByCustOid(long customerOid , String partnerFunctionTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class); 
		
		DetachedCriteria customerCri = criteria.createCriteria("customer");
		customerCri.add(Restrictions.eq("objectId", new Long(customerOid)));
	        
	    DetachedCriteria functionTypeCri = criteria.createCriteria("partnerType");
	    functionTypeCri.add(Restrictions.eq("partnerFunctionTypeId", partnerFunctionTypeId));
	    
	    List ret = getHibernateTemplate().findByCriteria(criteria);
	    List retCustpartner = new ArrayList();
        if ( ret.size() > 0 ) {
        	for(int i=0;i<ret.size();i++){
        		CustomerPartner custPart = (CustomerPartner)ret.get(i);
        		if(custPart.getPartnerCustomer().getIsActive()){
        			retCustpartner.add(custPart);
        		}
        	}
        	
        	if(retCustpartner != null && retCustpartner.size() > 0){
        		return retCustpartner;
        	}else{
        		return null;
        	}
//			return ret;
        }else
        	return null;

	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerDao#getCustomerPartners(long, java.lang.String)
	 */
	public Customer getCustomer(long customerOid, String partnerFunctionTypeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class);        
        
        DetachedCriteria customerCri = criteria.createCriteria("customer");
        customerCri.add(Restrictions.eq("objectId", new Long(customerOid)));
        
        DetachedCriteria functionTypeCri = criteria.createCriteria("partnerType");
        functionTypeCri.add(Restrictions.eq("partnerFunctionTypeId", partnerFunctionTypeId));
        
        List ret = getHibernateTemplate().findByCriteria(criteria);
        if ( ret.size() > 0 ) {
        	Customer cus = (Customer)ret.get(0);
			getHibernateTemplate().initialize(cus.getCustomerPartners());
			return cus;
        }
		else
        	return null;
	}
	public Customer getCustomer(long customerOid) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("objectId", new Long(customerOid)));
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        if ( result.size() != 1 )
            return null;
        else {
            Customer cus = (Customer)result.get(0);
            getHibernateTemplate().initialize(cus.getCustomerPartners());
        	return cus;
        }
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerDao#getCustomerBySapId(java.lang.String)
	 */
	public Customer getCustomerBySapId(String sapId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if(sapId.startsWith("EN")){
        	criteria.add(Restrictions.eq("sapId",sapId));      
        }else{      
        	criteria.add(Restrictions.eq("sapId", getSapId(sapId)));
        }
        
        criteria.addOrder(Order.desc("isActive"));
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        if ( result.size() == 0 )
            return null;
        else {
            Customer customer = (Customer)result.get(0);
        	return customer;
        }
	}
	
	public Customer getCustomerEmployeeBySapId(String sapId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("sapId", sapId));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        if ( result.size() == 0 )
            return null;
        else {
            Customer customer = (Customer)result.get(0);
        	return customer;
        }
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerDao#getCustomersByUpdDttmGtPubDttm()
	 */
	public List getCustomersByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	
		//criteria.setFetchMode("customerPartners", FetchMode.JOIN);
		//criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
	
	public List getCustomersNonVat(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("vatClassification",  new Boolean(false)));
        
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
    public int getRowNonVat() throws DataAccessException {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
    	criteria.add(Restrictions.eq("vatClassification",  new Boolean(false)));
    	criteria.setProjection(Projections.rowCount());
        criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
    	
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerDao#deleteCustomerPartners(java.util.List)
	 */
	public void deleteCustomerPartners(List list) {
		getHibernateTemplate().deleteAll(list);
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerDao#create(com.ie.icon.domain.customer.CustomerPartner)
	 */
	public void create(CustomerPartner partner) {
		getHibernateTemplate().save(partner);
	}
	
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerDao#getCustomerGroup(java.lang.String)
     */
    public CustomerGroup getCustomerGroup(String groupId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerGroup.class);
		criteria.add(Restrictions.eq("customerGroupId", groupId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if (result.size() == 0 )
			return null;
		else
			return (CustomerGroup)result.get(0);
    }
    
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerDao#getCustomerCustPartnerBySapId(java.lang.String)
	 */
	public Customer getCustomerCustPartnerBySapId(String sapId) {
		 DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
	        criteria.add(Restrictions.eq("sapId", getSapId(sapId)));
	        criteria.setFetchMode("customerPartners", FetchMode.JOIN);
	        
	        List result = getHibernateTemplate().findByCriteria(criteria);
	        
	        if ( result.size() == 0 )
	            return null;
	        else {
	            Customer customer = (Customer)result.get(0);
	        	return customer;
	        }
	}
    public List getCustomersByActive(boolean isActive) { 
    
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("isActive", new Boolean(isActive)));
      
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        return getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);
    }
    public List getCustomersByMemberCard2Active(String cardNo,boolean isActive) {    
    	/** retrive sap id from cust_mbr_card use to find customer */
    	
    	DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
        criteria.add(Restrictions.eq("cardNumber", cardNo));
    	
        
        
        if(isActive){
    		criteria.add(Restrictions.eq("status", new Character('1')));
    	}else if(isActive == false){
    		criteria.add(Restrictions.eq("status", new Character('2')));
    	}else
    		criteria.add(Restrictions.eq("status", new Character('1')));
    	
		// criteria.add(Restrictions.eq("status", new Boolean(isActive)));
		criteria.addOrder(Order.asc("cardNumber"));
        
        List ret = getHibernateTemplate().findByCriteria(criteria);
        if ( ret.size() != 1 ) {
        	return null;
        }
        else {
        	CustomerMemberCard custMemberCard = (CustomerMemberCard)ret.get(0);
	        /** find customer from sap id */
	        DetachedCriteria cusCriteria = DetachedCriteria.forClass(Customer.class);
	        if(custMemberCard.getSapId() != null &&!custMemberCard.getSapId().equals("")){
	        	if(custMemberCard.getSapId().substring(0, 2).equals("EN")){
	        		  cusCriteria.add(Restrictions.eq("sapId", custMemberCard.getSapId()));
	        	}else{
	        		 cusCriteria.add(Restrictions.eq("sapId", getSapId(custMemberCard.getSapId())));
	        	}
	        }
	        cusCriteria.add(Restrictions.eq("isActive", new Boolean(isActive)));
	        cusCriteria.addOrder(Order.asc("sapId"));
	        
	        /** get only partnerFunctionType Sold To */
	        DetachedCriteria partnerCri = cusCriteria.createCriteria( "partnerType" );
	        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
	        
//	        List aa =  getHibernateTemplate().findByCriteria(cusCriteria);
	        return getHibernateTemplate().findByCriteria(cusCriteria);
        }
    }
    
    public List getCustomersById2Active(String customerId,boolean isActive) {
        customerId = replace(customerId);
         
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        
        criteria.add(Restrictions.eq("sapId", customerId));
        criteria.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria.addOrder(Order.asc("sapId"));
        
        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        
        return getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);
    }
    
    public List getCustomersByName2Active(String name,boolean isActive) {
        name = getParameterRightLike(replace(name));
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.ilike("firstName", name));
        criteria.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria.addOrder(Order.asc("firstName"));   
        criteria.addOrder(Order.asc("lastName"));
        criteria.addOrder(Order.desc("lastUpdateDate"));
        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        
        List resultList=getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);

        return resultList;
    }
    
    public List getCustomersBySurname2Active(String surname,boolean isActive) {
        surname = getParameterRightLike(replace(surname));
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        
        criteria.add(Restrictions.ilike("lastName", surname));
        criteria.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria.addOrder(Order.asc("firstName")); 
        criteria.addOrder(Order.asc("lastName"));

        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));

        return getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);
    }
    
    public List getCustomersByPhone2Active(String phoneNo,boolean isActive) {
        //phoneNo = getParameterRightLike(replace(phoneNo));
        List resultList=new ArrayList();
        
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
        criteria1.add(Restrictions.eq("phoneNumber1", phoneNo));
        criteria1.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria1.addOrder(Order.asc("phoneNumber1"));
        
        /** get only partnerFunctionType Sold To */
        DetachedCriteria partnerCri = criteria1.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        
        List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
        
        DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
        criteria2.add(Restrictions.eq("phoneNumber2", phoneNo));
        criteria2.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria2.addOrder(Order.asc("phoneNumber2"));
        
        /** get only partnerFunctionType Sold To */
        partnerCri = criteria2.createCriteria( "partnerType" );
       partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));    
        
        List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
        

        DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
        criteria3.add(Restrictions.eq("phoneNumber3", phoneNo));
        criteria3.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria3.addOrder(Order.asc("phoneNumber3"));
        
        partnerCri = criteria3.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        
        List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);   

        DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
        criteria4.add(Restrictions.eq("phoneNumber4", phoneNo));
        criteria4.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria4.addOrder(Order.asc("phoneNumber4"));
        
        partnerCri = criteria4.createCriteria( "partnerType" );
        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
        
        List customers4=getHibernateTemplate().findByCriteria(criteria4, 0, CustomerConstant.SEARCH_SIZE);    

        
        DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
        criteria5.add(Restrictions.eq("fax", phoneNo));
        criteria5.add(Restrictions.eq("isActive", new Boolean(isActive)));
        criteria5.addOrder(Order.asc("fax"));
        /** get only partnerFunctionType Sold To */
        partnerCri = criteria5.createCriteria( "partnerType" );
       partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
       
        List customers5=getHibernateTemplate().findByCriteria(criteria5, 0, CustomerConstant.SEARCH_SIZE);        
        
        resultList=customers1;
        for(int i=0;i<customers2.size();i++){
        	Customer cs=(Customer)customers2.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }
        for(int i=0;i<customers3.size();i++){
        	Customer cs=(Customer)customers3.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }        
        for(int i=0;i<customers4.size();i++){
        	Customer cs=(Customer)customers4.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        } 
        for(int i=0;i<customers5.size();i++){
        	Customer cs=(Customer)customers5.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        } 
        return resultList;
    }

	public List getCustomerPartners(long customerOid, String partnerFunctionTypeId, String condition, String conditionValue) throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPartner.class);        

        DetachedCriteria customerCri = criteria.createCriteria("customer");
        customerCri.add(Restrictions.eq("objectId", new Long(customerOid)));
        
        DetachedCriteria functionTypeCri = criteria.createCriteria("partnerType");
        functionTypeCri.add(Restrictions.eq("partnerFunctionTypeId", partnerFunctionTypeId));
        
        criteria.createAlias("partnerCustomer", "partnerCustomer");
        criteria.add(Restrictions.eq("partnerCustomer.isActive", Boolean.TRUE));
        
        if ( "CUST".equals(condition) ) {
        	criteria.add(Restrictions.eq("partnerCustomer.sapId", getSapId(conditionValue.trim())));
        } else
        if ( "NAME".equals(condition) ) {
        	String param1 = getParameterRightLike(replace(conditionValue));
        	criteria.add(Restrictions.ilike("partnerCustomer.firstName", param1));
        } else
        if ( "SURNAME".equals(condition) ) {
        	String param1 = getParameterRightLike(replace(conditionValue));
        	criteria.add(Restrictions.ilike("partnerCustomer.lastName", param1));
        } else 
        if ( "PHONE".equals(condition) ) {
        	Restrictions.or(Restrictions.eq("partnerCustomer.phoneNumber1", conditionValue), Restrictions.or(Restrictions.eq("partnerCustomer.phoneNumber2", conditionValue), Restrictions.eq("partnerCustomer.phoneNumber3", conditionValue)));
        }
        
        return getHibernateTemplate().findByCriteria(criteria);
	}
	
	/** B007 **/
	public List getCustomer(String partnerFunctionTypeId, String name, String lastname, String phoneNo, String SAPID) throws DataAccessException{
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if(!"".equals(partnerFunctionTypeId)){
    		criteria.createAlias("partnerType", "partnerType");
        	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",partnerFunctionTypeId));
        }
        if(!"".equals(name)){
        	criteria.add(Restrictions.like("firstName","%"+name+"%"));
        }
        if(!"".equals(lastname)){
        	criteria.add(Restrictions.like("lastName","%"+lastname+"%"));
        }
        if(!"".equals(phoneNo)){
        	criteria.add(Restrictions.or(Restrictions.eq("phoneNumber1", phoneNo), Restrictions.or(Restrictions.eq("phoneNumber2", phoneNo), Restrictions.eq("phoneNumber3", phoneNo))));
        }
        if(!"".equals(SAPID)){
        	criteria.add(Restrictions.eq("sapId",SAPID));
        }
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        List result = getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);
		return result;
	}
	
	public List getCustomer(String partnerFunctionTypeId, String name, String lastname, String phoneNo, String SAPID,String customerGroup) throws DataAccessException{
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        
        if(!"".equals(partnerFunctionTypeId)){
    		criteria.createAlias("partnerType", "partnerType");
        	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",partnerFunctionTypeId));    
        }
        if(customerGroup!=null && !"".equals(customerGroup)){   
        	if(Constant.CustomerGroup.CREDIT.equals(customerGroup)){
        		criteria.add(Restrictions.or(Restrictions.ilike("customerGroup", Constant.CustomerGroup.CREDIT, MatchMode.START), 
        				Restrictions.ilike("customerGroup", Constant.CustomerGroup.VENDOR, MatchMode.START)));
        	}else{
        		criteria.add(Restrictions.not(Restrictions.ilike("customerGroup", Constant.CustomerGroup.CREDIT, MatchMode.START)));
        	}  
        }
        
        if(!"".equals(name)){
        	criteria.add(Restrictions.like("firstName","%"+name+"%"));
        }
        if(!"".equals(lastname)){
        	criteria.add(Restrictions.like("lastName","%"+lastname+"%"));
        }
        if(!"".equals(phoneNo)){
        	criteria.add(Restrictions.or(Restrictions.eq("phoneNumber1", phoneNo), Restrictions.or(Restrictions.eq("phoneNumber2", phoneNo), Restrictions.eq("phoneNumber3", phoneNo))));
        }
        if(!"".equals(SAPID)){
//        	criteria.add(Restrictions.eq("sapId",getSapId(SAPID.trim())));
	        if(SAPID.substring(0, 2).equals("EN")){
	        	criteria.add(Restrictions.eq("sapId", SAPID.trim()));
	      	}else{
	      		criteria.add(Restrictions.eq("sapId", getSapId(SAPID.trim())));
	      	}
        }
//        else{
//        	criteria.add(Restrictions.eq("isActive", new Boolean(true)));
//        }       
        
        
    	criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        
        List result = getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);
        List returnList = new ArrayList();
        if(result != null && result.size()>0){   
        	for(int i=0; i<result.size(); i++){				
        		Customer cust = (Customer)result.get(i);       
				if(cust.getSapId()!=null && !cust.getSapId().equals("")){
					returnList.add(cust);
				}           
			}
		}
		return returnList;
	}
	
	
	
	/** Check Exist Customer **/
	public List getCustomersByNameSurnameExist(String name, String surname,String partnerType) {
		
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("firstName", name));
        criteria.add(Restrictions.eq("lastName", surname));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        if(!"".equals(partnerType)){
    		criteria.createAlias("partnerType", "partnerType");
        	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",partnerType));
        }
        
//        criteria.add(Restrictions.eq("partnerType", partnerType));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
	
	public List getCustomersByMemberCardBillTo(String cardNo) {    
    	/** retrive sap id from cust_mbr_card use to find customer */
    	DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
        criteria.add(Restrictions.eq("cardNumber", cardNo));
		criteria.add(Restrictions.eq("status", new Character('1')));
		
        List ret = getHibernateTemplate().findByCriteria(criteria);
        
        if ( ret.size() != 1 ) {
        	return null;
        }
        else {
        	
        	try{
        		CustomerMemberCard custMemberCard = (CustomerMemberCard)ret.get(0);
    	        /** find customer from sap id */
    	        DetachedCriteria cusCriteria = DetachedCriteria.forClass(Customer.class);
    	        cusCriteria.add(Restrictions.eq("sapId", getSapId(custMemberCard.getSapId())));
    	        cusCriteria.add(Restrictions.eq("isActive", new Boolean(true)));
    	        criteria.createAlias("partnerType", "partnerType");
            	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));
    	        cusCriteria.addOrder(Order.asc("sapId"));
    	           	        
    	        List ret2 = getHibernateTemplate().findByCriteria(cusCriteria);

    	        if(ret2.size() != 1){
    	        	return null;
    	        }
    	            
    	        return getHibernateTemplate().findByCriteria(cusCriteria);
        	}catch(Exception e){
        		e.printStackTrace();
        		return null;
        	}
        }
    }
	public List getCustomersByNameBillTo(String name) {
        name = getParameter(replace(name));
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.ilike("firstName", name , MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        
    	criteria.createAlias("partnerType", "partnerType");
    	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));
        
        List resultList=getHibernateTemplate().findByCriteria(criteria);
        return resultList;
    }
    public List getCustomersBySurnameBillTo(String surname) {
        surname = getParameter(replace(surname));
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.ilike("lastName", surname , MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        
        criteria.createAlias("partnerType", "partnerType");
    	criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
    public List getCustomersByPhoneBillTo(String phoneNo) {
        phoneNo = getParameterRightLike(replace(phoneNo));
        List resultList=new ArrayList();
        
        // ** criteria1 ** //
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
        criteria1.add(Restrictions.like("phoneNumber1", phoneNo));
        criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria1.createAlias("partnerType", "partnerType");
        criteria1.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));

        List customers1=getHibernateTemplate().findByCriteria(criteria1, 0, CustomerConstant.SEARCH_SIZE);
        
        // ** criteria2 ** //
        DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
        criteria2.add(Restrictions.like("phoneNumber2", phoneNo));
        criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria2.createAlias("partnerType", "partnerType");
        criteria2.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));

        List customers2=getHibernateTemplate().findByCriteria(criteria2, 0, CustomerConstant.SEARCH_SIZE);
        
        // ** criteria3 ** //
        DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
        criteria3.add(Restrictions.like("phoneNumber3", phoneNo));
        criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria3.createAlias("partnerType", "partnerType");
        criteria3.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));

        List customers3=getHibernateTemplate().findByCriteria(criteria3, 0, CustomerConstant.SEARCH_SIZE);        
        
        // ** criteria4 ** //
        DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
        criteria4.add(Restrictions.like("phoneNumber4", phoneNo));
        criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria4.createAlias("partnerType", "partnerType");
        criteria4.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));
     
        List customers4=getHibernateTemplate().findByCriteria(criteria4, 0, CustomerConstant.SEARCH_SIZE);    
        
        
        // ** criteria4 ** //
        DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
        criteria5.add(Restrictions.like("fax", phoneNo));
        criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria5.createAlias("partnerType", "partnerType");
        criteria5.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));
     
        List customers5=getHibernateTemplate().findByCriteria(criteria5, 0, CustomerConstant.SEARCH_SIZE);    
        
        
        resultList=customers1;
        for(int i=0;i<customers2.size();i++){
        	Customer cs=(Customer)customers2.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }
        for(int i=0;i<customers3.size();i++){
        	Customer cs=(Customer)customers3.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }        
        
        for(int i=0;i<customers4.size();i++){
        	Customer cs=(Customer)customers4.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);
        	}
        }     
        
        for(int i=0;i<customers5.size();i++){
        	Customer cs=(Customer)customers5.get(i);
        	if(!customers1.contains(cs)){
        		customers1.add(cs);   
        	}
        }     
        return resultList;
    }   
    public List getCustomersByIdBillTo(String customerId) {
//      customerId = replace(customerId);
      DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
      criteria.add(Restrictions.like("sapId", getSapId(customerId)));
      criteria.add(Restrictions.eq("isActive", new Boolean(true)));
      criteria.createAlias("partnerType", "partnerType");
      criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId",Constant.CustomerPartnerType.BILL_TO));

      return getHibernateTemplate().findByCriteria(criteria);
  }
	public void deleteCustomerPartners(final Customer customer) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "delete from CustomerPartner " +
						" where customer = ?" ;
				Query query = session.createQuery(queryString);
				query.setLong(0, customer.getObjectId());
				session.close();
				return null;
			}
		});
	}    
	
	public List getCustomerMemberCard(String sapId,String memberCardTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		if(sapId!=null){
			criteria.add(Restrictions.eq("sapId", sapId));
		}
		if(memberCardTypeId!=null){
			criteria.createAlias("memberCardType", "memberCardType");
			criteria.add(Restrictions.eq("memberCardType.memberCardTypeId",memberCardTypeId));
		}
		//status active
		criteria.add(Restrictions.eq("status", new Character('1')));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public Customer getCustomerAndCustPartBySapId(String sapId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("sapId", sapId));
        criteria.setFetchMode("customerPartners", FetchMode.JOIN);
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        if ( result.size() == 0 )
            return null;
        else {
            Customer customer = (Customer)result.get(0);
        	return customer;
        }
	}
	
	public String getRewardPoint(String rewardCardNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		criteria.add(Restrictions.eq("cardNumber", rewardCardNo));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result!=null && !result.isEmpty()) {
			CustomerMemberCard custMbr = (CustomerMemberCard)result.get(0);
			if (custMbr.getCurrentRewardPoint() == null){
				return "0";
			}else{
				int rewardPoint = custMbr.getCurrentRewardPoint().intValue();
//				return custMbr.getCurrentRewardPoint().ROUND_DOWN + "" ;
				return String.valueOf(rewardPoint);
//				return custMbr.getCurrentRewardPoint().setScale(0).toString();
			}
		}else{
			return "0";
		}
	}
	
	/**
	 * get customers by first name and last name 
	 * it use for check the same first name and last name 
	 * @param firstName
	 * @param lastName
	 * @return List of customer -> same first name and same last name and partnerType = "AG"
	 * @author nodthapobr
	 * @since 16 Jan, 2010
	 */
	public List getCustomersByNameSurname(String firstName, String lastName){
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.eq("firstName", firstName));
		criteria.add(Restrictions.eq("lastName", lastName));
		
		criteria.createAlias("partnerType","partnerType");
		criteria.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));
		
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	/**
	 * get customers by first name and last name 
	 * it use for check the same first name and last name 
	 * @param firstName
	 * @param lastName
	 * @param partnerType
	 * @return List of customer -> same first name and same last name and partnerType = "AG" and partnerType = "@param partnerType"
	 * @author nodthapobr
	 * @since 29 Jan, 2010
	 */
	public List getCustomersPartnerByNameSurname(String firstName, String lastName, String partnerType){
		
		// Get list type "AG"
		DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
		criteria1.add(Restrictions.eq("firstName", firstName));
		criteria1.add(Restrictions.eq("lastName", lastName));
		criteria1.createAlias("partnerType","partnerType");
		criteria1.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria1.add(Restrictions.eq("isActive", new Boolean(true)));	
		List listAg = getHibernateTemplate().findByCriteria(criteria1);
		
		// Get List type "@param partnerType"
		DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
		criteria2.add(Restrictions.eq("firstName", firstName));
		criteria2.add(Restrictions.eq("lastName", lastName));	
		criteria2.createAlias("partnerType","partnerType");
		criteria2.add(Restrictions.eq("partnerType.partnerFunctionTypeId", partnerType));
		criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
		List listPartnerType = getHibernateTemplate().findByCriteria(criteria2);
		
		// Add result list
		for(int i = 0 ; i< listPartnerType.size() ; i++){
			listAg.add(listPartnerType.get(i));
		}
		
		return listAg;
	}
	
	/**
	 * get customers by string phone number 
	 * it use for check the same phone 
	 * Customer type : AG
	 * @param phoneNumber
	 * @return List of customer -> same telephone number and partnerType = "AG"
	 * @author nodthapobr
	 * @see com.ie.icon.dao.CustomerDao#getCustomersByPhoneNumber(java.lang.String) 
	 * @since 16 Jan, 2010
	 */
	public List getCustomersByPhoneNumber(String phoneNumber){
		List resultList = new ArrayList();
		phoneNumber = getParameterRightLike(replace(phoneNumber));
		
		// Search in field phone_no_1
		DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
		criteria1.add(Restrictions.like("phoneNumber1", phoneNumber));
		criteria1.createAlias("partnerType","partnerType");
		criteria1.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers1 = getHibernateTemplate().findByCriteria(criteria1);
		
		// Search in field phone_no_2
		DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
		criteria2.add(Restrictions.like("phoneNumber2", phoneNumber));
		criteria2.createAlias("partnerType","partnerType");
		criteria2.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers2 = getHibernateTemplate().findByCriteria(criteria2);
		
		// Search in field phone_no_3
		DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
		criteria3.add(Restrictions.like("phoneNumber3", phoneNumber));
		criteria3.createAlias("partnerType","partnerType");
		criteria3.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers3 = getHibernateTemplate().findByCriteria(criteria3);
		
		// Search in field phone_no_4
		DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
		criteria4.add(Restrictions.like("phoneNumber4", phoneNumber));
		criteria4.createAlias("partnerType","partnerType");
		criteria4.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers4 = getHibernateTemplate().findByCriteria(criteria4);

		// Search in field fax
		DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
		criteria5.add(Restrictions.like("fax", phoneNumber));
		criteria5.createAlias("partnerType","partnerType");
		criteria5.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers5 = getHibernateTemplate().findByCriteria(criteria5);
		
		if(customers1 != null){
			for(int i = 0 ; i<customers1.size() ; i++){
				resultList.add(customers1.get(i));
			}
		}
		
		if(customers2 != null){
			for(int i = 0 ; i<customers2.size() ; i++){
				resultList.add(customers2.get(i));
			}
		}
		
		if(customers3 != null){
			for(int i = 0 ; i<customers3.size() ; i++){
				resultList.add(customers3.get(i));
			}
		}
		
		if(customers4 != null){
			for(int i = 0 ; i<customers4.size() ; i++){
				resultList.add(customers4.get(i));
			}
		}
		
		if(customers5 != null){
			for(int i = 0 ; i<customers5.size() ; i++){
				resultList.add(customers5.get(i));
			}
		}
		return resultList;
	}
	
	/**
	 * get customers by string phone number 
	 * it use for check the same phone 
	 * Customer type : AG
	 * @param phoneNumber
	 * @return List of customer -> same telephone number and partnerType = "AG" and 
	 * @author nodthapobr
	 * @see com.ie.icon.dao.CustomerDao#getCustomersByPhoneNumber(java.lang.String) 
	 * @since 29 Jan, 2010
	 */
	public List getCustomersPartnerByPhoneNumber(String phoneNumber, String partnerType){
		List resultList = new ArrayList();
		phoneNumber = getParameterRightLike(replace(phoneNumber));
		
		// Search in field phone_no_1 with type : "AG"
		DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
		criteria1.add(Restrictions.like("phoneNumber1", phoneNumber));
		criteria1.createAlias("partnerType","partnerType");
		criteria1.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers1 = getHibernateTemplate().findByCriteria(criteria1);
		
		// Search in field phone_no_2 with type : "AG"
		DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
		criteria2.add(Restrictions.like("phoneNumber2", phoneNumber));
		criteria2.createAlias("partnerType","partnerType");
		criteria2.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers2 = getHibernateTemplate().findByCriteria(criteria2);
		
		// Search in field phone_no_3 with type : "AG"
		DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
		criteria3.add(Restrictions.like("phoneNumber3", phoneNumber));
		criteria3.createAlias("partnerType","partnerType");
		criteria3.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers3 = getHibernateTemplate().findByCriteria(criteria3);
		
		// Search in field phone_no_4 with type : "AG"
		DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
		criteria4.add(Restrictions.like("phoneNumber4", phoneNumber));
		criteria4.createAlias("partnerType","partnerType");
		criteria4.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers4 = getHibernateTemplate().findByCriteria(criteria4);

		// Search in field fax with type : "AG"
		DetachedCriteria criteria5 = DetachedCriteria.forClass(Customer.class);
		criteria5.add(Restrictions.like("fax", phoneNumber));
		criteria5.createAlias("partnerType","partnerType");
		criteria5.add(Restrictions.eq("partnerType.partnerFunctionTypeId", "AG"));	
		criteria5.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers5 = getHibernateTemplate().findByCriteria(criteria5);
		
		
		
		// Search in field phone_no_1 with type : "@partnerType"
		DetachedCriteria criteria6 = DetachedCriteria.forClass(Customer.class);
		criteria6.add(Restrictions.like("phoneNumber1", phoneNumber));
		criteria6.createAlias("partnerType","partnerType");
		criteria6.add(Restrictions.eq("partnerType.partnerFunctionTypeId", partnerType));	
		criteria6.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers6 = getHibernateTemplate().findByCriteria(criteria6);
		
		// Search in field phone_no_2 with type : "@partnerType"
		DetachedCriteria criteria7 = DetachedCriteria.forClass(Customer.class);
		criteria7.add(Restrictions.like("phoneNumber2", phoneNumber));
		criteria7.createAlias("partnerType","partnerType");
		criteria7.add(Restrictions.eq("partnerType.partnerFunctionTypeId", partnerType));	
		criteria7.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers7 = getHibernateTemplate().findByCriteria(criteria7);
		
		// Search in field phone_no_3 with type : "@partnerType"
		DetachedCriteria criteria8 = DetachedCriteria.forClass(Customer.class);
		criteria8.add(Restrictions.like("phoneNumber3", phoneNumber));
		criteria8.createAlias("partnerType","partnerType");
		criteria8.add(Restrictions.eq("partnerType.partnerFunctionTypeId", partnerType));	
		criteria8.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers8 = getHibernateTemplate().findByCriteria(criteria8);
		
		// Search in field phone_no_4 with type : "@partnerType"
		DetachedCriteria criteria9 = DetachedCriteria.forClass(Customer.class);
		criteria9.add(Restrictions.like("phoneNumber4", phoneNumber));
		criteria9.createAlias("partnerType","partnerType");
		criteria9.add(Restrictions.eq("partnerType.partnerFunctionTypeId", partnerType));	
		criteria9.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers9 = getHibernateTemplate().findByCriteria(criteria9);

		// Search in field fax with type : "@partnerType"
		DetachedCriteria criteria10 = DetachedCriteria.forClass(Customer.class);
		criteria10.add(Restrictions.like("fax", phoneNumber));
		criteria10.createAlias("partnerType","partnerType");
		criteria10.add(Restrictions.eq("partnerType.partnerFunctionTypeId", partnerType));	
		criteria10.add(Restrictions.eq("isActive", new Boolean(true)));
		List customers10 = getHibernateTemplate().findByCriteria(criteria10);
		
		
		
		
		
		if(customers1 != null){
			for(int i = 0 ; i<customers1.size() ; i++){
				resultList.add(customers1.get(i));
			}
		}
		
		if(customers2 != null){
			for(int i = 0 ; i<customers2.size() ; i++){
				resultList.add(customers2.get(i));
			}
		}
		
		if(customers3 != null){
			for(int i = 0 ; i<customers3.size() ; i++){
				resultList.add(customers3.get(i));
			}
		}
		
		if(customers4 != null){
			for(int i = 0 ; i<customers4.size() ; i++){
				resultList.add(customers4.get(i));
			}
		}
		
		if(customers5 != null){
			for(int i = 0 ; i<customers5.size() ; i++){
				resultList.add(customers5.get(i));
			}
		}
		
		if(customers6 != null){
			for(int i = 0 ; i<customers6.size() ; i++){
				resultList.add(customers6.get(i));
			}
		}
		
		if(customers7 != null){
			for(int i = 0 ; i<customers7.size() ; i++){
				resultList.add(customers7.get(i));
			}
		}
		
		if(customers8 != null){
			for(int i = 0 ; i<customers8.size() ; i++){
				resultList.add(customers8.get(i));
			}
		}
		
		if(customers9 != null){
			for(int i = 0 ; i<customers9.size() ; i++){
				resultList.add(customers9.get(i));
			}
		}
		
		if(customers10 != null){
			for(int i = 0 ; i<customers10.size() ; i++){
				resultList.add(customers10.get(i));
			}
		}
		return resultList;
	}
	
	public List getCardDetialBySapId(String sapId){
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
        criteria.add(Restrictions.eq("sapId", sapId));
        
        List newList = getHibernateTemplate().findByCriteria(criteria);
        
        if(newList==null || newList.size()==0){
        	return new ArrayList();
        }
        return newList;
	}
	
	public List getCustomerPageFleetSearch(String sapId, String firstName, String lastName, String phoneNo){
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		DetachedCriteria criteria1 = DetachedCriteria.forClass(Customer.class);
		DetachedCriteria criteria2 = DetachedCriteria.forClass(Customer.class);
		DetachedCriteria criteria3 = DetachedCriteria.forClass(Customer.class);
		DetachedCriteria criteria4 = DetachedCriteria.forClass(Customer.class);
		List result = new ArrayList();
		
		
		if(!phoneNo.trim().equals("")){
						
			//phone 1
			if(!sapId.trim().equals("")){
				criteria1.add(Restrictions.eq("sapId", sapId));
			}
			
			if(!firstName.trim().equals("")){
				criteria1.add(Restrictions.like("firstName", firstName+"%"));
			}
			
			if(!lastName.trim().equals("")){
				criteria1.add(Restrictions.like("lastName", lastName+"%"));
			}
			criteria1.add(Restrictions.eq("phoneNumber1", phoneNo));
			criteria1.add(Restrictions.eq("isActive", new Boolean(true)));
			List cust1 = getHibernateTemplate().findByCriteria(criteria1);
			
			// phone 2
			if(!sapId.trim().equals("")){
				criteria2.add(Restrictions.eq("sapId", sapId));
			}
			
			if(!firstName.trim().equals("")){
				criteria2.add(Restrictions.like("firstName", firstName+"%"));
			}
			
			if(!lastName.trim().equals("")){
				criteria2.add(Restrictions.like("lastName", lastName+"%"));
			}
			criteria2.add(Restrictions.eq("phoneNumber2", phoneNo));
			criteria2.add(Restrictions.eq("isActive", new Boolean(true)));
			List cust2 = getHibernateTemplate().findByCriteria(criteria2);
			
			// phone 3
			if(!sapId.trim().equals("")){
				criteria3.add(Restrictions.eq("sapId", sapId));
			}
			
			if(!firstName.trim().equals("")){
				criteria3.add(Restrictions.like("firstName", firstName+"%"));
			}
			
			if(!lastName.trim().equals("")){
				criteria3.add(Restrictions.like("lastName", lastName+"%"));
			}
			criteria3.add(Restrictions.eq("phoneNumber3", phoneNo));
			criteria3.add(Restrictions.eq("isActive", new Boolean(true)));
			List cust3 = getHibernateTemplate().findByCriteria(criteria3);
			
			// phone 4
			if(!sapId.trim().equals("")){
				criteria4.add(Restrictions.eq("sapId", sapId));
			}
			
			if(!firstName.trim().equals("")){
				criteria4.add(Restrictions.like("firstName", firstName+"%"));
			}
			
			if(!lastName.trim().equals("")){
				criteria4.add(Restrictions.like("lastName", lastName+"%"));
			}
			criteria4.add(Restrictions.eq("phoneNumber4", phoneNo));
			criteria4.add(Restrictions.eq("isActive", new Boolean(true)));
			List cust4 = getHibernateTemplate().findByCriteria(criteria4);
			
			result.addAll(cust1);
			result.addAll(cust2);
			result.addAll(cust3);
			result.addAll(cust4);
			
			Customer convertCust;
			
			// Get all Sold To customer
			List soldToCust = new ArrayList();
			for(int i = 0 ; i < result.size() ; i++){
				if(((Customer)result.get(i)).getPartnerType().getPartnerFunctionTypeId().equals("AG")){
					soldToCust.add(result.get(i));
				}
			}
			
			// Sort Sold To customer by name
			for(int i = 0 ; i < soldToCust.size() ; i++){
				for(int j = 0 ; j < soldToCust.size() ; j++){
					if(((Customer)soldToCust.get(i)).getFirstName().compareTo(((Customer)soldToCust.get(j)).getFirstName())<0){
						convertCust = ((Customer)soldToCust.get(i));
						soldToCust.set(i, ((Customer)soldToCust.get(j)));
						soldToCust.set(j, convertCust);
						
					}
				}
			}
			
			// Get all Bill To Customer
			List billToCust = new ArrayList();
			for(int i = 0 ; i < result.size() ; i++){
				if(((Customer)result.get(i)).getPartnerType().getPartnerFunctionTypeId().equals("RE")){
					billToCust.add(result.get(i));
				}
			}
			
			// Sort Bill To customer by name
			for(int i = 0 ; i < billToCust.size() ; i++){
				for(int j = 0 ; j < billToCust.size() ; j++){
					if(((Customer)billToCust.get(i)).getFirstName().compareTo(((Customer)billToCust.get(j)).getFirstName())<0){
						convertCust = ((Customer)billToCust.get(i));
						billToCust.set(i, ((Customer)billToCust.get(j)));
						billToCust.set(j, convertCust);
						
					}
				}
			}
			
			// get all Ship To Customer
			List shipToCust = new ArrayList();
			for(int i = 0 ; i < result.size() ; i++){
				if(((Customer)result.get(i)).getPartnerType().getPartnerFunctionTypeId().equals("WE")){
					shipToCust.add(result.get(i));
				}
			}
			
			// Sort Bill To customer by name
			for(int i = 0 ; i < shipToCust.size() ; i++){
				for(int j = 0 ; j < shipToCust.size() ; j++){
					if(((Customer)shipToCust.get(i)).getFirstName().compareTo(((Customer)shipToCust.get(j)).getFirstName())<0){
						convertCust = ((Customer)shipToCust.get(i));
						shipToCust.set(i, ((Customer)shipToCust.get(j)));
						shipToCust.set(j, convertCust);
						
					}
				}
			}
			result.clear();
			result.addAll(soldToCust);
			result.addAll(billToCust);
			result.addAll(shipToCust);
		}
		else{
			if(!sapId.trim().equals("")){
				criteria.add(Restrictions.eq("sapId", sapId));
			}
			
			if(!firstName.trim().equals("")){
				criteria.add(Restrictions.like("firstName", firstName+"%"));
			}
			
			if(!lastName.trim().equals("")){
				criteria.add(Restrictions.like("lastName", lastName+"%"));
			}			
			criteria.add(Restrictions.eq("isActive", new Boolean(true)));
			criteria.addOrder(Order.asc("partnerType"));
			criteria.addOrder(Order.asc("firstName"));  
			result = getHibernateTemplate().findByCriteria(criteria);		
		}	
		
        return result;
	}
	
	public List getTransportDataByMapName(String mapName){
		DetachedCriteria criteria = DetachedCriteria.forClass(TransportData.class);
		criteria.add(Restrictions.eq("mapName", mapName));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	   public List getCustomersByType(Map arg) {
		    
	        //name = getParameterRightLike(replace(name));
	        
	        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
	        if(arg.get("firstName") != null)
	        criteria.add(Restrictions.ilike("firstName", getParameterRightLike(replace(arg.get("firstName").toString()))));

	        if(arg.get("lastName") != null)
		        criteria.add(Restrictions.ilike("lastName", getParameterRightLike(replace(arg.get("lastName").toString()))));

	        if(arg.get("isActive") != null)
	        criteria.add(Restrictions.eq("isActive", new Boolean(arg.get("isActive").toString())));
	        
	        criteria.addOrder(Order.asc("firstName"));
	        criteria.addOrder(Order.asc("lastName"));
	        criteria.addOrder(Order.desc("lastUpdateDate"));
	        /** get only partnerFunctionType Sold To */
	        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
	        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
	        
	        List resultList=getHibernateTemplate().findByCriteria(criteria, 0, CustomerConstant.SEARCH_SIZE);

	        return resultList;
	    }
	   public List getCustomerListBySapId(List custContent) {
	        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
	        System.out.println("hiber");
	        if(custContent != null){
	        	 criteria.add(Restrictions.in("sapId", custContent));
	             criteria.add(Restrictions.eq("isActive", new Boolean(true)));
	             List temp = getHibernateTemplate().findByCriteria(criteria);
//	             for (int i=0;i<temp.size(); i++) {
//	            	 System.out.println("Hibernate:"+temp.get(i).toString());
//	             }


	             return temp;
	        }
	        return null;
		}
		public List createCustomerInterface(List custList) {
			int ret = 0;
			int retitem = 0;
			String query = null;
			for (int j = 0; j < custList.size(); j++) {
				CustomerInterface custMan = (CustomerInterface) custList.get(j);
				getHibernateTemplate().save(custMan);
			}		 
			System.out.println("Insert success!");

			return null;
		}
		public List getCustomerInterface() {
			// TODO Auto-generated method stub
			return null;
		}
		
		/**
		 * Update By Chailuk.C 2014/03/10
		 * - Get Customer List By multi sap id
		 * */
		public List getCustomerListAllStatusBySapIds(List sapIds) {
			List result = null;
	        if(sapIds != null && sapIds.size()>0){
	        	for(int i=0;i<sapIds.size();i++){
	        		System.out.println("Sap ID " + i + " : " + sapIds.get(i));
	        	}
	        	DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
	        	criteria.add(Restrictions.in("sapId",sapIds));      
	        
		        criteria.addOrder(Order.desc("isActive"));
		        criteria.addOrder(Order.asc("sapId"));
		        criteria.addOrder(Order.asc("customerGroup"));

		        
//		        DetachedCriteria partnerCri = criteria.createCriteria( "partnerType" );
//		        partnerCri.add(Restrictions.eq("partnerFunctionTypeId", Constant.CustomerPartnerType.SOLD_TO));
		        
		        result = getHibernateTemplate().findByCriteria(criteria);
	        }
	        return result;
		}
		
		
//		public Long getNextRefPubIDServletSeq() {
//		    Query query = getSession().createSQLQuery("select REF_PUB_ID_SERVLET_SEQ.nextval as num from dual")
//		            .addScalar("num", Hibernate.LONG);
//
//		    return (Long)query.uniqueResult();
//		}

}