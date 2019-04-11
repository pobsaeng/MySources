package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.SalesOrderConstant;
import com.ie.icon.dao.CollectSalesOrderDao;
import com.ie.icon.domain.so.CollectSalesOrder;
import com.ie.icon.domain.so.CollectSalesOrderItem;
import com.ie.icon.domain.so.SalesOrderStatus;

public class HibernateCollectSalesOrderDao extends HibernateCommonDao implements CollectSalesOrderDao {

	public void delete(Long objectId) throws DataAccessException {
		CollectSalesOrder collectSalesOrder = (CollectSalesOrder) getHibernateTemplate().load(CollectSalesOrder.class, objectId);
		getHibernateTemplate().delete(collectSalesOrder);
	}

	public void create(CollectSalesOrder collectSalesOrder) throws DataAccessException {   
		getHibernateTemplate().save(collectSalesOrder);
		System.out.println("########## Create : CollectSalesOrder ########## ");
	}

	public void update(CollectSalesOrder collectSalesOrder) throws DataAccessException {
		getHibernateTemplate().update(collectSalesOrder);
	}   
	
	public List getCollectSalesOrder(Date transactionDate,String customerNo,String customerName,String customerLastName,String collNo,String statusId,String salesOrderNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		
		if (transactionDate != null && !transactionDate.equals("")) {
			criteria.add(Restrictions.eq("transactionDate", transactionDate));   
		}      
		
		if((customerNo!=null && !customerNo.equals(""))||(customerName!=null && !customerName.equals("")||(customerLastName!=null && !customerLastName.equals("")) )){   
			criteria.createAlias("soldToCustomer","soldToCustomer");
		}
		
		if(customerNo!=null && !customerNo.equals("")){   
			criteria.add(Restrictions.eq("soldToCustomer.sapId", customerNo));
		}
		if(customerName!=null && !customerName.equals("")){
			criteria.add(Restrictions.like("soldToCustomer.firstName", replace(customerName)));
		}
		if(customerLastName!=null && !customerLastName.equals("")){
			criteria.add(Restrictions.like("soldToCustomer.lastName", replace(customerLastName)));
		}
		if(collNo!=null && !collNo.equals("")){
			criteria.add(Restrictions.eq("collectSalesOrderNo", collNo));
		}
		if(statusId!=null && !statusId.equals("")){
			criteria.createAlias("salesOrderStatus","salesOrderStatus");   
			criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", statusId));
		}
		if(salesOrderNo!=null && !salesOrderNo.equals("")){
			criteria.createAlias("collectSalesOrderItems","collectSalesOrderItems");
			criteria.add(Restrictions.eq("collectSalesOrderItems.salesOrderNo", salesOrderNo));
		}
		criteria.add(Restrictions.isNull("quotationId"));
		criteria.addOrder(Order.asc("collectSalesOrderNo"));
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		
		if (resultList != null && resultList.size() > 0) {    
			for (int i = 0; i < resultList.size(); i++) {
				CollectSalesOrder collect = (CollectSalesOrder) resultList.get(i);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
		}
		return resultList;
	
	}
	
	public CollectSalesOrder getMaxCollectSaleOrder(Date transactionDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		CollectSalesOrder result = null;
		
		if (transactionDate != null && !transactionDate.equals("")) {
			criteria.add(Restrictions.eq("transactionDate", transactionDate));   
		}      
		criteria.addOrder(Order.desc("collectSalesOrderNo"));
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		
		if (resultList != null && resultList.size() > 0) {    
			for (int i = 0; i < resultList.size(); i++) {
				CollectSalesOrder collect = (CollectSalesOrder) resultList.get(i);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
			result = (CollectSalesOrder) resultList.get(0);
		}
		return result;
		
	}
	public CollectSalesOrder getCollectSalesOrder(String storeid, String collNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		criteria.add(Restrictions.eq("collectSalesOrderNo", collNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		criteria.addOrder(Order.desc("collectSalesOrderNo"));
		CollectSalesOrder collect = null;
		try{
			List resultList = getHibernateTemplate().findByCriteria(criteria);
			if (resultList != null && resultList.size() > 0) {   
				collect = (CollectSalesOrder) resultList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collect;
	}	
	
	public CollectSalesOrder getCollectSalesOrder(String storeid, String collNo, Date transactionDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		criteria.add(Restrictions.eq("collectSalesOrderNo", collNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		criteria.add(Restrictions.eq("transactionDate", transactionDate)); 
		criteria.addOrder(Order.desc("collectSalesOrderNo"));
		CollectSalesOrder collect = null;
		try{
			List resultList = getHibernateTemplate().findByCriteria(criteria);
			if (resultList != null && resultList.size() > 0) {   
				collect = (CollectSalesOrder) resultList.get(0);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collect;
	}	
	
	public CollectSalesOrder getCollectSalesOrderbySapBillNo(String storeid, String collectSales , Date trndt) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		criteria.add(Restrictions.eq("collectSalesOrderNo", collectSales));
		criteria.add(Restrictions.eq("transactionDate", trndt));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		CollectSalesOrder collect = null;
		try{
			List resultList = getHibernateTemplate().findByCriteria(criteria);
			if (resultList != null && resultList.size() > 0) {   
				collect = (CollectSalesOrder) resultList.get(0);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collect;
	}	
	
	
	public CollectSalesOrder getCollectSalesOrderbySapBillNo(String storeid, String collectSales) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);

		criteria.add(Restrictions.eq("collectSalesOrderNo", collectSales));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		CollectSalesOrder collect = null;
		try{
			List resultList = getHibernateTemplate().findByCriteria(criteria);
			if (resultList != null && resultList.size() > 0) {   
				collect = (CollectSalesOrder) resultList.get(0);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collect;
	}	
	
	
	public CollectSalesOrder getCollectSalesOrderbyCollectSalesOrderNo(String collectSalesOrderNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		
		criteria.add(Restrictions.eq("collectSalesOrderNo", collectSalesOrderNo));
		CollectSalesOrder collect = null;
		
		try{
			List resultList = getHibernateTemplate().findByCriteria(criteria);
			if (resultList != null && resultList.size() > 0) {   
				collect = (CollectSalesOrder) resultList.get(0);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collect;
	}
	
	public CollectSalesOrderItem getCollSalesOrderItems(String salesOrderNo) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrderItem.class);
		
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		CollectSalesOrderItem collect = null;
			
			try{
				List resultList = getHibernateTemplate().findByCriteria(criteria);
				
				if (resultList != null && resultList.size() > 0) {   
					collect = (CollectSalesOrderItem) resultList.get(0);
					getHibernateTemplate().initialize(collect.getCollectSalesOrder());
				}
			} catch (Exception e) {
				return null;
			}
	
			return collect;


	}
	
	public CollectSalesOrderItem getCollSalesOrderItems(String salesOrderNo, String storeId , Date trDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrderItem.class);

		if(salesOrderNo != null && !"".equals(salesOrderNo)){
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		}
		
		if(storeId != null && !"".equals(storeId)){
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		
		if(trDate != null && !"".equals(trDate)){
			criteria.add(Restrictions.eq("salesTransactionDate", trDate));
		}
		
		CollectSalesOrderItem collect = null;
			
			try{
				List resultList = getHibernateTemplate().findByCriteria(criteria);
				if (resultList != null && resultList.size() > 0) {   
					for(int i=0;i<resultList.size();i++){
						collect = (CollectSalesOrderItem) resultList.get(i);
						if(collect != null){
							getHibernateTemplate().initialize(collect.getCollectSalesOrder());
						}
					}
					
				}
			} catch (Exception e) {
				return null;
			}
	
			return collect;
	}
	
	public List getCollSalesOrderItemsList(String salesOrderNo, String storeId , Date trDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrderItem.class);

		if(salesOrderNo != null && !"".equals(salesOrderNo)){
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		}
		
		if(storeId != null && !"".equals(storeId)){
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		
		if(trDate != null && !"".equals(trDate)){
			criteria.add(Restrictions.eq("salesTransactionDate", trDate));
		}
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList != null && resultList.size() > 0) {   
			for(int i=0;i<resultList.size();i++){
				CollectSalesOrderItem collectItem = (CollectSalesOrderItem) resultList.get(i);
				if(collectItem != null){
					getHibernateTemplate().initialize(collectItem.getCollectSalesOrder());
				}
			}
			
		}

		return resultList;

	}
	
	
	
	public CollectSalesOrderItem getCollSalesOrderItemsByKeyAndStatus(String salesOrderNo, String storeId , Date trDate , String status) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrderItem.class);

		if(salesOrderNo != null && !"".equals(salesOrderNo)){
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		}
		
		if(storeId != null && !"".equals(storeId)){
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		
		if(trDate != null && !"".equals(trDate)){
			criteria.add(Restrictions.eq("salesTransactionDate", trDate));
		}
		if(status!=null && !status.equals("")){
			criteria.createAlias("collectSalesOrder", "collectSalesOrder");
			criteria.createAlias("collectSalesOrder.salesOrderStatus", "salesOrderStatus");
			criteria.add(Restrictions.eq("collectSalesOrder.salesOrderStatus.salesOrderStatusId", status));
		}
		
		CollectSalesOrderItem collect = null;
			
			try{
				List resultList = getHibernateTemplate().findByCriteria(criteria);
				if (resultList != null && resultList.size() > 0) {
					for(int i=0;i<resultList.size();i++){
						collect = (CollectSalesOrderItem) resultList.get(i);
						if(collect != null){
							getHibernateTemplate().initialize(collect.getCollectSalesOrder());
						}
					}
					
				}else{
					criteria = DetachedCriteria.forClass(CollectSalesOrderItem.class);

					if(salesOrderNo != null && !"".equals(salesOrderNo)){
						criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
					}
					
					if(storeId != null && !"".equals(storeId)){
						criteria.add(Restrictions.eq("storeId", storeId));
					}
					
					if(trDate != null && !"".equals(trDate)){
						criteria.add(Restrictions.eq("salesTransactionDate", trDate));
					}
					
					criteria.createAlias("collectSalesOrder", "collectSalesOrder");
					criteria.createAlias("collectSalesOrder.salesOrderStatus", "salesOrderStatus");
					criteria.add(Restrictions.eq("collectSalesOrder.salesOrderStatus.salesOrderStatusId", SalesOrderConstant.COLL_STATUS));
					
					collect = null;
					resultList = getHibernateTemplate().findByCriteria(criteria);
					if (resultList != null && resultList.size() > 0) {
						for(int i=0;i<resultList.size();i++){
							collect = (CollectSalesOrderItem) resultList.get(i);
							if(collect != null){
								getHibernateTemplate().initialize(collect.getCollectSalesOrder());
							}
						}
					}
				}
			} catch (Exception e) {
				return null;
			}
	
			return collect;
	}
	
	public List getCollectSalesOrderByKeyItem(Date transactionDate,String salesOrderNo,String storeNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		
		criteria.createAlias("collectSalesOrderItems","collectSalesOrderItems");
		
		if (transactionDate != null && !transactionDate.equals("")) {
//			criteria.createAlias("collectSalesOrderItems","collectSalesOrderItems");
			criteria.add(Restrictions.eq("collectSalesOrderItems.salesTransactionDate", transactionDate));
		}
		if(salesOrderNo!=null && !salesOrderNo.equals("")){
//			criteria.createAlias("collectSalesOrderItems","collectSalesOrderItems");
			criteria.add(Restrictions.eq("collectSalesOrderItems.salesOrderNo", salesOrderNo));
		}
		
		if(storeNo!=null && !storeNo.equals("")){
//			criteria.createAlias("collectSalesOrderItems","collectSalesOrderItems");
			criteria.add(Restrictions.eq("collectSalesOrderItems.storeId", storeNo));
		}
		
		criteria.addOrder(Order.asc("collectSalesOrderNo"));
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		
		if (resultList != null && resultList.size() > 0) {    
			for (int i = 0; i < resultList.size(); i++) {
				CollectSalesOrder collect = (CollectSalesOrder) resultList.get(i);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
		}  
		return resultList;
	
	}
	
	public List getCollectSalesOrderQuotation(String quotationNo, String versionId) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		criteria.add(Restrictions.eq("quotationId", quotationNo));
		if(versionId!=null){
			criteria.add(Restrictions.eq("versionId", versionId));
		}
		SalesOrderStatus  salesOrderStatus = new SalesOrderStatus();
		salesOrderStatus.setSalesOrderStatusId(SalesOrderConstant.CANCELLED_STATUS);
		criteria.add(Restrictions.ne("salesOrderStatus", salesOrderStatus));
		criteria.addOrder(Order.asc("collectSalesOrderNo"));
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList != null && resultList.size() > 0) {    
			for (int i = 0; i < resultList.size(); i++) {
				CollectSalesOrder collect = (CollectSalesOrder) resultList.get(i);
				getHibernateTemplate().initialize(collect.getCollectSalesOrderItems());
			}
		}
		return resultList;
	}
	
	public void delete(List collList)throws DataAccessException{
		for(int i=0;i<collList.size();i++){
			CollectSalesOrder salesOrders = (CollectSalesOrder)collList.get(i);
			getHibernateTemplate().delete(salesOrders);      
		}
	}
	
	public void cancelCollSalesOrders(List collList) throws DataAccessException{
		SalesOrderStatus  salesOrderStatus = new SalesOrderStatus();
		salesOrderStatus.setSalesOrderStatusId(SalesOrderConstant.CANCELLED_STATUS);
		for(int i=0;i<collList.size();i++){
			CollectSalesOrder collectSalesOrder = (CollectSalesOrder)collList.get(i);			
			collectSalesOrder.setSalesOrderStatus(salesOrderStatus);
			getHibernateTemplate().update(collectSalesOrder);      
		}
	}
	
	public List getCollSalesOrderItems(String storeId , Date fromDate , Date toDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrderItem.class);
		
		if(storeId != null && !"".equals(storeId)){
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		
		criteria.add(Restrictions.between("salesTransactionDate", fromDate, toDate));
		
		criteria.createAlias("collectSalesOrder", "collectSalesOrder");
		criteria.add(Restrictions.eq("collectSalesOrder.isHirePurchasePayment", new Boolean(true)));
		
		CollectSalesOrderItem collect = null;
		try{
			List resultList = getHibernateTemplate().findByCriteria(criteria);
			if (resultList != null && resultList.size() > 0) {   
				return resultList;
				
			}else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
