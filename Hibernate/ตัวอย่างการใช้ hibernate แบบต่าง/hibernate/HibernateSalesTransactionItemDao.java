package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SalesTransactionItemDao;
import com.ie.icon.domain.sale.ItemDiscount;
import com.ie.icon.domain.sale.SalesTransactionItem;

public class HibernateSalesTransactionItemDao extends HibernateCommonDao implements SalesTransactionItemDao{
	public List displaySalesTransactionItem(long salestransaction) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);		
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.objectId",new Long(salestransaction)));
		criteria.addOrder(Order.asc("preSaleNo"));
		criteria.addOrder(Order.asc("sapSeqNo"));
		//....Boizz(+) : Order by Seq No. ASC
		criteria.addOrder(Order.asc("seqNo"));
		//....End(+)
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result != null && result.size() > 0){
			for(int i=0; i<result.size(); i++){
				SalesTransactionItem salestranitem = (SalesTransactionItem)result.get(i);
				getHibernateTemplate().initialize(salestranitem.getSalesTransactionSetItems());
			}
		}
		return result;
	}
	
	public String getCheckMixMode(long salestransaction) throws DataAccessException {
		String mode = "";
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);
        criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("isSOItem"), "isSOItem")));
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.objectId",new Long(salestransaction)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size()>1){
			mode = "mixmode";
		}else if(result.size()==1){
			String obj = result.get(0).toString();
			if(obj.equals("true")){
				mode = "somode";
			}else if(obj.equals("false")){
				mode = "posmode";
			}
		}		
		return mode;
	}
	
	public List getlistSalesTransactionItem(long salestransactionOid) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);		
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.objectId",new Long(salestransactionOid)));
		criteria.addOrder(Order.asc("sapSeqNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result != null && result.size() > 0){
			for(int i=0; i<result.size(); i++){
				SalesTransactionItem salestranitem = (SalesTransactionItem)result.get(i);
				Hibernate.initialize(salestranitem);   
				getHibernateTemplate().initialize(salestranitem.getSalesTransaction());
				getHibernateTemplate().initialize(salestranitem.getSalesTransactionSetItems());
				getHibernateTemplate().initialize(salestranitem.getSalesTransaction().getCashierTransactions());			
			}
		}
		return result;
	}

	public void update(SalesTransactionItem salestranItem ) throws DataAccessException {		
		try {
			getHibernateTemplate().update(salestranItem);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}		
		
	}

	public List getItemDiscountbySalesTrransactionItem(SalesTransactionItem salesTransactionItem) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ItemDiscount.class);
		criteria.add(Restrictions.eq("salesTransactionItem", salesTransactionItem));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
//		if(result != null && result.size() > 0){
//			for(int i=0; i<result.size(); i++){
//				ItemDiscount itemDiscount = (ItemDiscount)result.get(i);
//				Hibernate.initialize(itemDiscount);
//
//			}
//		}
		return result;
	}

	public List getSalesTransactionItem(SalesTransactionItem salesTransactionItem) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);
		if(salesTransactionItem != null){
			if(salesTransactionItem.getItemUPC()!= null){
				criteria.add(Restrictions.eq("itemUPC", salesTransactionItem.getItemUPC()));
			}
		}
		
		List result = getHibernateTemplate().findByCriteria(criteria);

		return result;
	}

	 	
	
	
}
