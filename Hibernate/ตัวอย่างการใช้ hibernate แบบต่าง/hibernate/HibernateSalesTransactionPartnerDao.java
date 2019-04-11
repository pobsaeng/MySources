/*
 * Created on Sep 25, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.SalesTransactionPartnerDao;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.SalesTransactionPartner;

/**
 * @author visawee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateSalesTransactionPartnerDao extends HibernateCommonDao implements SalesTransactionPartnerDao {
	public void create(SalesTransactionPartner obj) {
	    getHibernateTemplate().save(obj);
	    System.out.println("########## Create : SalesTransactionPartner ########## ");
	}

    public void update(SalesTransactionPartner obj) {
        getHibernateTemplate().update(obj);
    }

	public List getSalesPartnerByParentSalesOid(long parentSalesOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);
		criteria.add(Restrictions.eq("salesTransactionPartnerId.parentSalesOid",new Long(parentSalesOid)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if(result.size()==0)
			return null;
		else {
			for(int j=0;j<result.size();j++){
				SalesTransactionPartner partner = (SalesTransactionPartner)result.get(j);
				getHibernateTemplate().initialize(partner.getSalesTransactionMapChield());
				SalesTransaction tranChild = partner.getSalesTransactionMapChield();
				getHibernateTemplate().initialize(tranChild.getSalesTransactionItems());
				getHibernateTemplate().initialize(tranChild.getCashierTransactions());
			}    
			return result;
		}
	}
	
	public SalesTransactionPartner getSalesPartner(long salesTransactionOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);   
		criteria.add(Restrictions.eq("salesTransactionMapChield.objectId",new Long(salesTransactionOid)));
		List result = getHibernateTemplate().findByCriteria(criteria);    
		if(result.size()==0)   
			return null;          
		else
			return (SalesTransactionPartner)result.get(0);
	}	
    
	private void initialData(SalesTransactionPartner salesTransactionPartner) {
		initialSalesTranSaction(salesTransactionPartner.getSalesTransactionMapChield());
	}
	
	private void initialSalesTranSaction(SalesTransaction sales) {
	}
	
	public SalesTransactionPartner getSalesTransactionPartner(long parentSalesOid, long salesTransactionOid) {
		System.out.println("<<<<<<< parentSalesOid >>>>>>>>>" + parentSalesOid);
		System.out.println("<<<<<<< salesTransactionOid >>>>>>>>>" + salesTransactionOid);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);
		criteria.add(Restrictions.eq("salesTransactionPartnerId.parentSalesOid",new Long(parentSalesOid)));
		criteria.add(Restrictions.eq("salesTransactionPartnerId.salesTranOid",new Long(salesTransactionOid)));
		List result = null; 
		//Tod make try
		try{
			result=getHibernateTemplate().findByCriteria(criteria);    
		}catch (Exception e) {		
			e.printStackTrace();
			return null;
		}
		System.out.println("<<<<<<< result.size() >>>>>>>>>" + result.size());
		if(result.size() == 0 ) { 
			System.out.println("<<<<<<< return null >>>>>>>>>");
			return null;  
		} else {
			System.out.println("<<<<<<< result.get(0) >>>>>>>>>");
			return (SalesTransactionPartner) result.get(0);
		}
	}
}



















