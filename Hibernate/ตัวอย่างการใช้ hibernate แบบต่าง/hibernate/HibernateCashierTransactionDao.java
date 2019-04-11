/*
 * Created on Sep 30, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.CashierTransactionDao;
import com.ie.icon.domain.cashier.CashierTransaction;
import com.ie.icon.domain.sale.SalesTransaction;

/**
 * @author yo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateCashierTransactionDao extends HibernateCommonDao 
							implements CashierTransactionDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CashierTransactionDao#create(com.ie.icon.domain.cashier.CashierTransaction)
	 */
	public void create(CashierTransaction cashierTransaction) {
		getHibernateTemplate().save(cashierTransaction);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CashierTransactionDao#update(com.ie.icon.domain.cashier.CashierTransaction)
	 */
	public void update(CashierTransaction cashierTransaction) {
		getHibernateTemplate().update(cashierTransaction);
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CashierTransactionDao#getCashierTransaction(java.lang.String)
	 */
	public CashierTransaction getCashierTransaction(String cashierTransactionId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		
		criteria.add( Restrictions.eq( "objectId", new Long(cashierTransactionId )) );
		
		List result = getHibernateTemplate().findByCriteria( criteria );
		
		if ( result.size() > 1 )
			return (CashierTransaction)result.get( 0 );
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CashierTransactionDao#getCashierTransactionList(java.lang.String)
	 */
	public List getCashierTransactionList(long salesTransactionId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.createAlias("cashierType", "cashierType");
		criteria.add( Restrictions.eq( "salesTransaction.objectId", new Long(salesTransactionId)) );
		criteria.add(Restrictions.ne("cashierType.cashierTransactionTypeId", Constant.CashierTransactionType.EXCESS));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CashierTransactionDao#getCashierTransactionList(java.lang.String, java.lang.String)
	 */
	public List getCashierTransactionList(long salesTransactionId,
			String cashierTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add( Restrictions.eq( "salesTransaction.objectId", new Long(salesTransactionId)) );
		
		/** find cashierTransactionType */
		if ( cashierTypeId != null ) {
			DetachedCriteria cashierTypeCri = criteria.createCriteria( "cashierType" );
			cashierTypeCri.add( Restrictions.eq( "cashierTransactionTypeId", cashierTypeId ) );			
		}		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	public List getCashierTransaction(String storeId, Date transactionDate, String cashierTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		criteria.add( Restrictions.eq( "transactionDate", transactionDate) );
		
		criteria.createAlias("store", "store");
		criteria.add( Restrictions.eq( "store.storeId", storeId) );
		
		DetachedCriteria cashierTypeCri = criteria.createCriteria( "cashierType" );
		cashierTypeCri.add( Restrictions.eq( "cashierTransactionTypeId", cashierTypeId ) );
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	public List getCashierTransaction(String storeId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		criteria.add( Restrictions.eq( "transactionDate", transactionDate) );
		
		criteria.createAlias("store", "store");
		criteria.add( Restrictions.eq( "store.storeId", storeId) );
		
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	public List getCreditCard(Date trnDate, String ticketno, String pos_id,String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		List result = new ArrayList();
		criteria.add(Restrictions.eq("transactionDate",trnDate));
		if(ticketno != null && !ticketno.equals("")){
			criteria.add(Restrictions.eq("ticketNo",ticketno));
		}
		if(pos_id != null && !pos_id.equals("")){
			criteria.add(Restrictions.eq("posId",pos_id));
		}
		if(storeId != null && !storeId.equals("")){
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}   
		criteria.addOrder(Order.asc("objectId"));
		List salesTranList = getHibernateTemplate().findByCriteria(criteria);
		if(salesTranList!=null && salesTranList.size()>0){
			for(int i=0;i<salesTranList.size();i++){
				SalesTransaction salesTran = (SalesTransaction)salesTranList.get(i);   
				DetachedCriteria criteriaCashier = DetachedCriteria.forClass(CashierTransaction.class);
				criteriaCashier.createAlias("salesTransaction", "salesTransaction");
				criteriaCashier.add( Restrictions.eq( "salesTransaction.objectId", new Long(salesTran.getObjectId())) );
				criteriaCashier.createAlias("tender", "tender");     
				criteriaCashier.add( Restrictions.eq( "tender.type", new Integer(Constant.TenderType.CREDIT_CARD)));
				List cashTranList = getHibernateTemplate().findByCriteria(criteriaCashier);
				if(cashTranList!=null && cashTranList.size()>0){
					result.add(cashTranList.get(0));   
				}
			}
			return result;
		}else{
			return salesTranList;
		}
	}

	public List getCashierAdjust(String storeId, Date transactionDate, String cashierTypeId, String tenderId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId",storeId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		DetachedCriteria cashierTypeCri = criteria.createCriteria( "cashierType" );
		cashierTypeCri.add( Restrictions.eq( "cashierTransactionTypeId", cashierTypeId ) );
		criteria.createAlias("tender", "tender");
		criteria.add(Restrictions.eq("tender.tenderId", tenderId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	
	public List getCashierTransactionListCrCard(long salesTransactionId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.createAlias("cashierType", "cashierType");
		criteria.add( Restrictions.eq( "salesTransaction.objectId", new Long(salesTransactionId)) );
		criteria.add(Restrictions.ne("cashierType.cashierTransactionTypeId", Constant.CashierTransactionType.EXCESS));
		criteria.add( Restrictions.isNotNull("creditCardApprovalCode") );
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
}















































































