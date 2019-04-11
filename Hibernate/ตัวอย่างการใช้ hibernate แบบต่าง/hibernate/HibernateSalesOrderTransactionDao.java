package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SalesOrderTransactionDao;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.SalesTransactionItem;
import com.ie.icon.domain.so.SalesOrder;
import com.ie.icon.domain.so.SalesOrderGroup;
import com.ie.icon.domain.so.SalesOrderTransaction;

public class HibernateSalesOrderTransactionDao extends HibernateCommonDao implements SalesOrderTransactionDao {

	public void create(SalesOrderTransaction salesOrderTransaction) throws DataAccessException {
		getHibernateTemplate().save(salesOrderTransaction);
		System.out.println("########## Create : SalesOrderTransaction ########## ");
	}

	public void update(SalesOrderTransaction salesOrderTransaction) throws DataAccessException {
		getHibernateTemplate().update(salesOrderTransaction);
	}
	
	public SalesOrderTransaction getSalesOrderTransaction(long salesTransactionOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderTransaction.class);
		criteria.add(Restrictions.eq("salesOrderTransactionId.salesTransactionOid", new Long(salesTransactionOid)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0)
			return null;
		else {
			SalesOrderTransaction soTrans = (SalesOrderTransaction)result.get(0);
			initialData(soTrans);
			return soTrans;
		}
	}
	
	public List getlist_SalesOrderTransaction(long salesTransactionOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderTransaction.class);
		criteria.add(Restrictions.eq("salesOrderTransactionId.salesTransactionOid", new Long(salesTransactionOid)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0)
			return null;
		else {
//			SalesOrderTransaction soTrans = (SalesOrderTransaction)result.get(0);
//			initialData(soTrans);
//			return soTrans;
			return result;
		}
	}
	
	public List getSalesOrderTransaction(Date tr_date,String pos_id,String tick_no,String salesorderno, String status){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderTransaction.class);
		criteria.createAlias("salesTransaction", "salesTransaction");
		if(tick_no!=null && !"".equals(tick_no)){
			criteria.add(Restrictions.eq("salesTransaction.ticketNo", tick_no));
		}
		if(pos_id!=null && !"".equals(pos_id)){
			criteria.add(Restrictions.eq("salesTransaction.posId", pos_id));
		}
		if(status!=null && !"".equals(status)){
			criteria.createAlias("salesTransaction.status", "status");
			criteria.add(Restrictions.eq("status.transactionStatusId",status));
		}
		if(salesorderno!=null && !"".equals(salesorderno)){
			criteria.add(Restrictions.eq("salesOrderTransactionId.salesOrderNo", salesorderno));
		}
		criteria.add(Restrictions.eq("salesTransaction.typeId","S"));
		criteria.add(Restrictions.eq("salesOrderTransactionId.transactionDate", tr_date));
		criteria.addOrder(Order.asc("salesTransaction.objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
public List getSalesOrderTransaction(Date transactionDate,String salesorderno, String storeId) throws DataAccessException{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderTransaction.class);
		criteria.createAlias("salesTransaction", "salesTransaction");
	
		if(salesorderno!=null && !"".equals(salesorderno)){
			criteria.add(Restrictions.eq("salesOrderTransactionId.salesOrderNo", salesorderno));
		}
		if(storeId!=null && !"".equals(storeId)){
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		
		//criteria.add(Restrictions.eq("salesTransaction.typeId","S"));
		criteria.add(Restrictions.eq("salesOrderTransactionId.transactionDate", transactionDate));
		criteria.addOrder(Order.asc("salesTransaction.objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	public SalesOrderTransaction getSalesOrderTransaction(){
		
		List result = getHibernateTemplate().loadAll(SalesOrderTransaction.class);
		
		if ( result.size() == 0)
			return null;
		else {
			SalesOrderTransaction soTrans = (SalesOrderTransaction)result.get(0);
			initialData(soTrans);
			return soTrans;
		}
	}
	
	private void initialData(SalesOrderTransaction salesOrderTransaction) {
		initialSalesTransaction(salesOrderTransaction.getSalesTransaction());
	}
	
	private void initialSalesOrder(SalesOrder salesOrder) {
		getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
		getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
		
		for ( int index = 0; index < salesOrder.getSalesOrderGroups().size(); index++ ) {
			SalesOrderGroup group = (SalesOrderGroup)salesOrder.getSalesOrderGroups().get(index);
			getHibernateTemplate().initialize(group.getSalesOrderItems());
		}
	}
	
	private void initialSalesTransaction(SalesTransaction sales) {
		getHibernateTemplate().initialize(sales.getSalesTransactionItems());
		getHibernateTemplate().initialize(sales.getCashierTransactions());
		getHibernateTemplate().initialize(sales.getTransactionTotalDiscounts());
		for ( int index = 0; index < sales.getSalesTransactionItems().size(); index++ ) {
			SalesTransactionItem item = (SalesTransactionItem)sales.getSalesTransactionItems().get(index);
			getHibernateTemplate().initialize(item.getItemDiscounts());
		}		
	}

	public List getSalesOrderTransactions(long salestransactionOID) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderTransaction.class);
		criteria.add(Restrictions.eq("salesOrderTransactionId.salesTransactionOid", new Long(salestransactionOID)));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void delete(long salesOrderOid) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}
	
	public SalesOrderTransaction getSalesOrderTransaction(long salesTransactionOid,String storeId,Date transactionDate,String salesorderno){
		System.out.println("<<<<<<<<<<<<<  HibernateSalesOrderTransactionDao >>>>>>>>>>>>>>");
		System.out.println("salesTransactionOid >>"+salesTransactionOid);
		System.out.println("storeId >>"+storeId);
		System.out.println("transactionDate >>"+transactionDate);
		System.out.println("salesorderno >>"+salesorderno);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderTransaction.class);
		criteria.add(Restrictions.eq("salesOrderTransactionId.salesTransactionOid", new Long(salesTransactionOid)));
		if(storeId!=null && !"".equals(storeId)){
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		criteria.add(Restrictions.eq("salesOrderTransactionId.transactionDate", transactionDate));
		criteria.createAlias("salesTransaction", "salesTransaction");
		if(salesorderno!=null && !"".equals(salesorderno)){
			criteria.add(Restrictions.eq("salesOrderTransactionId.salesOrderNo", salesorderno));
		}
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0)
			return null;
		else {
			SalesOrderTransaction soTrans = (SalesOrderTransaction)result.get(0);
			//initialData(soTrans);
			return soTrans;
		}
	}
}
