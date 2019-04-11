package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import com.ie.icon.common.util.DateTimeUtil;
import com.ie.icon.constant.Constant;
import com.ie.icon.dao.SalesTransactionDao;
import com.ie.icon.domain.customer.Customer;
import com.ie.icon.domain.promotion.PromotionRedemption;
import com.ie.icon.domain.sale.SalesDocument;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.SalesTransactionItem;
import com.ie.icon.domain.sale.SalesTransactionPartner;
import com.ie.icon.domain.sale.TransactionStatus;

public class HibernateSalesTransactionDao extends HibernateCommonDao implements SalesTransactionDao{
	
	public List getdataSalesTrans(Date trnDate,String ticketno,String pos_id,String status){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);		
		criteria.add(Restrictions.eq("transactionDate",trnDate));
		if(ticketno != null && !ticketno.equals("")){
			criteria.add(Restrictions.eq("ticketNo",ticketno));
		}
		if(pos_id != null && !pos_id.equals("")){
			criteria.add(Restrictions.eq("posId",pos_id));
		}
		if(status != null && !status.equals("")){
			criteria.createAlias("status","status");
			criteria.add(Restrictions.eq("status.transactionStatusId",status));
		}
		criteria.add(Restrictions.eq("typeId","S"));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getCashierTransactions());					
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
				
				//Soyhu added to get Discount item list .
				for ( int index = 0; index < salestrans.getSalesTransactionItems().size(); index++ ) {
					SalesTransactionItem item = (SalesTransactionItem)salestrans.getSalesTransactionItems().get(index);
					getHibernateTemplate().initialize(item.getItemDiscounts());
				}	
				
			}
		}
		
		return result;
	}
	
	public List getSalesTransAllType(Date trnDate,String ticketno,String pos_id,String status) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);		
		criteria.add(Restrictions.eq("transactionDate",trnDate));
		if(ticketno != null && !ticketno.equals("")){
			criteria.add(Restrictions.eq("ticketNo",ticketno));
		}
		if(pos_id != null && !pos_id.equals("")){
			criteria.add(Restrictions.eq("posId",pos_id));
		}
		if(status != null && !status.equals("")){
			criteria.createAlias("status","status");
			criteria.add(Restrictions.eq("status.transactionStatusId",status));
		}
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getCashierTransactions());					
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
				
				for ( int index = 0; index < salestrans.getSalesTransactionItems().size(); index++ ) {
					SalesTransactionItem item = (SalesTransactionItem)salestrans.getSalesTransactionItems().get(index);
					getHibernateTemplate().initialize(item.getItemDiscounts());
				}	
				
			}
		}
		
		return result;
	}
	public List getdataSalesTrans(long objectid){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);		
		criteria.add(Restrictions.eq("objectId",new Long(objectid)));
		criteria.addOrder(Order.asc("objectId"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
			}
		}
		return result;
	}
	
	
	public List getdataSalesTransInitialParent(long objectid){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);		
		criteria.add(Restrictions.eq("objectId",new Long(objectid)));
		criteria.addOrder(Order.asc("objectId"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
				if(salestrans.getSalesTranPartnerParentItems().size() > 0){
					for(int index =0;index<salestrans.getSalesTranPartnerParentItems().size();index++){
						SalesTransactionPartner salesTranPartner = (SalesTransactionPartner)salestrans.getSalesTranPartnerParentItems().get(index);
						getHibernateTemplate().initialize(salesTranPartner.getSalesTransactionMapChield().getSalesTransactionItems());
					}
				}
			}
		}
		
		return result;
		
	}
	
	public List bbb(Date tr_date) throws DataAccessException{
		String query = "select str.objectId,str.ticketNo,str.posId,str.totalTransactionAmount,str.totalDiscountAmount," +
				"str.discountCardNo,str.referrerCardNo,str.taxInvoiceNo,str.customer.objectId,str.status.transactionStatusId " +
				"from SalesTransaction str " +
				"where str.ticketNo not in (select ticketNumber from SalesDocument) " +
				"and str.transactionDate = :transactionDate";
		List result = (List)getHibernateTemplate().findByNamedParam(query, "transactionDate", tr_date);
		List list_salestrans = new ArrayList(); 
		if(result != null && result.size()>0){
			for(int i = 0; i< result.size(); i++){
				SalesTransaction salestrans = new SalesTransaction();
				Object [] array = (Object[]) result.get(i);
				if(array[0] != null){salestrans.setObjectId(Long.parseLong(array[0].toString()));}
				if(array[1] != null){salestrans.setTicketNo(array[1].toString());}
				if(array[2] != null){salestrans.setPosId(array[2].toString());}
				if(array[3] != null){
					Object ob3 = array[3];
					BigDecimal bd = (BigDecimal)ob3;
					salestrans.setTotalTransactionAmount(bd);
				}
				if(array[4] != null){
					Object ob4 = array[4];
					BigDecimal dis_bd = (BigDecimal)ob4;
					salestrans.setTotalDiscountAmount(dis_bd);
				}		
				if(array[5] != null){salestrans.setDiscountCardNo(array[5].toString());}	
				if(array[6] != null){salestrans.setReferrerCardNo(array[6].toString());}	
				if(array[7] != null){salestrans.setTaxInvoiceNo(array[7].toString());}
				if(array[8] != null){
					Customer customer = new Customer();
					customer.setObjectId(Long.parseLong(array[8].toString()));
					salestrans.setCustomer(customer);
				}
				if(array[9] != null){
					TransactionStatus status = new TransactionStatus();
					status.setTransactionStatusId(array[9].toString());
				}
				list_salestrans.add(salestrans);
			}
		}
		return list_salestrans;
	}
	
	public List getSalestransItem(Date tr_date, String pos_no, String ticket_id, String sales_order,String status,String sapBillingNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);		

		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.transactionDate",tr_date));
		if(!"".equals(pos_no)){
			criteria.add(Restrictions.eq("salesTransaction.posId",pos_no));
		}
		if(!"".equals(ticket_id)){
			criteria.add(Restrictions.eq("salesTransaction.ticketNo",ticket_id));
		}
		if(!"".equals(sales_order)){
			criteria.add(Restrictions.eq("preSaleNo",sales_order));
		}
		if(!"".equals(status)){
			criteria.createAlias("salesTransaction.status", "status");
			criteria.add(Restrictions.eq("status.transactionStatusId",status));
		}
		if(!"".equals(sapBillingNo)){
			criteria.createAlias("salesTransaction.sapBillNo", sapBillingNo);
		}
		criteria.add(Restrictions.eq("salesTransaction.typeId","S"));
		criteria.addOrder(Order.asc("preSaleNo"));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	 public void updateSalesTrans(SalesTransaction salestransaction){
		 getHibernateTemplate().update(salestransaction);
	 }
	 
	 public List getDataSalesOrderTransaction(Date tr_date, String pos_no, String ticket_id,String salesOrderNo){
		 
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);		
		List listSalesOrderTrans = new ArrayList();
		criteria.add(Restrictions.eq("transactionDate",tr_date));
		if(ticket_id != null && !ticket_id.equals("")){
			criteria.add(Restrictions.eq("ticketNo",ticket_id));
		}
		if(pos_no != null && !pos_no.equals("")){
			criteria.add(Restrictions.eq("posId",pos_no));
		}
		criteria.add(Restrictions.eq("typeId","S"));
		
		//...***Edit
		criteria.add(Restrictions.eq("isOnline", new Boolean(true)));
		
//		if(salesOrderNo != null && !"".equals(salesOrderNo)){
//			criteria.createAlias("salesTransactionItems", "salesTransactionItems");
//			criteria.add(Restrictions.eq("salesTransactionItems.preSaleNo",salesOrderNo));
//		}
		if(salesOrderNo != null && !"".equals(salesOrderNo)){
			criteria.createAlias("salesOrderTransaction", "salesOrderTransaction");
			criteria.add(Restrictions.eq("salesOrderTransaction.salesOrderTransactionId.salesOrderNo",salesOrderNo));
		}
		
		criteria.add(Restrictions.isNull("saleDocNo"));
		criteria.addOrder(Order.asc("objectId"));
		listSalesOrderTrans = getHibernateTemplate().findByCriteria(criteria);
		if (listSalesOrderTrans != null && listSalesOrderTrans.size() > 0) {
			for(int i=0; i< listSalesOrderTrans.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)listSalesOrderTrans.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
				//Soyhu added to get Discount item list .
				for ( int index = 0; index < salestrans.getSalesTransactionItems().size(); index++ ) {
					SalesTransactionItem item = (SalesTransactionItem)salestrans.getSalesTransactionItems().get(index);
					getHibernateTemplate().initialize(item.getItemDiscounts());
				}	
			}
		}
	
		return listSalesOrderTrans;
	 }
	 
	 public List getSalesTransactionSellOffline(Date tr_date, String pos_no, String ticket_id,String salesOrderNo) throws DataAccessException{
			DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);		
			List result = new ArrayList();
			criteria.add(Restrictions.eq("transactionDate",tr_date));
			if(ticket_id != null && !ticket_id.equals("")){
				criteria.add(Restrictions.eq("ticketNo",ticket_id));
			}
			if(pos_no != null && !pos_no.equals("")){
				criteria.add(Restrictions.eq("posId",pos_no));
			}   
			criteria.add(Restrictions.eq("typeId","S"));
			
			//...***Edit
//			criteria.add(Restrictions.isNull("collSalesOrderNo"));
			
			criteria.createAlias("status", "status");
			criteria.add(Restrictions.eq("status.transactionStatusId",Constant.TransactionStatus.PAID));
			
			//...***Edit
			criteria.add(Restrictions.eq("isOnline", new Boolean(false)));
	
			
			if(salesOrderNo != null && !"".equals(salesOrderNo)){
				criteria.createAlias("salesOrderTransaction", "salesOrderTransaction");
				criteria.add(Restrictions.eq("salesOrderTransaction.salesOrderTransactionId.salesOrderNo",salesOrderNo));
			}
			
			criteria.add(Restrictions.isNull("saleDocNo"));
			criteria.addOrder(Order.asc("objectId"));
			result = getHibernateTemplate().findByCriteria(criteria);
			if (result != null && result.size() > 0) {
				for(int i=0; i< result.size(); i++){
					SalesTransaction salestrans = (SalesTransaction)result.get(i);
					getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
					getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
					for ( int index = 0; index < salestrans.getSalesTransactionItems().size(); index++ ) {
						SalesTransactionItem item = (SalesTransactionItem)salestrans.getSalesTransactionItems().get(index);
						getHibernateTemplate().initialize(item.getItemDiscounts());
					}	
				}
			}
		
			return result;
		 }
	
	public List getSalestransItem(Date tr_date, String pos_no, String ticket_id, String sales_order,String status) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);		
		
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.transactionDate",tr_date));
		if(!"".equals(pos_no)){
			criteria.add(Restrictions.eq("salesTransaction.posId",pos_no));
		}
		if(!"".equals(ticket_id)){
			criteria.add(Restrictions.eq("salesTransaction.ticketNo",ticket_id));
		}
		if(!"".equals(sales_order)){
			criteria.add(Restrictions.eq("preSaleNo",sales_order));
		}
		if(!"".equals(status)){
			criteria.createAlias("salesTransaction.status", "status");
			criteria.add(Restrictions.eq("status.transactionStatusId",status));
		}
		criteria.add(Restrictions.eq("salesTransaction.typeId","S"));
		criteria.addOrder(Order.asc("preSaleNo"));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	

	public List getdataSalesTrans(Date trnDate, String ticketno, String pos_id, String status, String sapBillNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		
		criteria.add(Restrictions.eq("transactionDate",trnDate));
		if(ticketno != null && !ticketno.equals("")){
			criteria.add(Restrictions.eq("ticketNo",ticketno));
		}
		if(pos_id != null && !pos_id.equals("")){
			criteria.add(Restrictions.eq("posId",pos_id));
		}
		if(status != null && !status.equals("")){
			criteria.createAlias("status","status");
			criteria.add(Restrictions.eq("status.transactionStatusId",status));
		}
		if(sapBillNo != null && !sapBillNo.equals("")){
			criteria.add(Restrictions.eq("sapBillNo", sapBillNo));
		}
		criteria.add(Restrictions.eq("typeId","S"));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
				//Soyhu added to get Discount item list .
				for ( int index = 0; index < salestrans.getSalesTransactionItems().size(); index++ ) {
					SalesTransactionItem item = (SalesTransactionItem)salestrans.getSalesTransactionItems().get(index);
					getHibernateTemplate().initialize(item.getItemDiscounts());
				}	
			}
		}
		return result;
	}
	
	public List getSalesTransactionToSAP(Date trnDate, String ticketno, String pos_id,String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
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
		criteria.add(Restrictions.eq("typeId","S"));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		//don't be initial.
		return result;
	}
	public List getSalesTransactionCenter(Date trnDate, String ticketno, String pos_id,String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
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
		List result = getHibernateTemplate().findByCriteria(criteria);
		System.out.println("******** result ******** "+result.size());
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getCashierTransactions());					
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
				for ( int index = 0; index < salestrans.getSalesTransactionItems().size(); index++ ) {
					SalesTransactionItem item = (SalesTransactionItem)salestrans.getSalesTransactionItems().get(index);
					getHibernateTemplate().initialize(item.getItemDiscounts());
				}	
				
			}
		}
		
		return result;
	}
	
	public List getSalesTransactionByTicket(Date trnDate, String ticketno, String pos_id,String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
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
		criteria.add(Restrictions.eq("typeId","S"));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
			}
		}
		return result;
	}
	
	public List getSalesTransactionByCollect(Date trnDate, String storeId,String collectNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		
		criteria.add(Restrictions.eq("transactionDate",trnDate));
		if(storeId != null && !storeId.equals("")){
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		if(collectNo != null && !collectNo.equals("")){
			criteria.add(Restrictions.eq("collSalesOrderNo", collectNo));
		}
		criteria.add(Restrictions.eq("typeId","S"));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
			}
		}
		return result;	
	}
	public List getSalesTransactionByCollect(String Collect_sale_order_no) throws DataAccessException{		
			DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);				
		List SaletransList = new ArrayList();
			if(Collect_sale_order_no != null && !Collect_sale_order_no.equals("")){
			criteria.add(Restrictions.eq("collSalesOrderNo", Collect_sale_order_no));
		}		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
			}	
		}					
		return SaletransList;			
	}			
	
	public void update(List salesTranList) throws DataAccessException {
		for(int i=0;i<salesTranList.size();i++){
			SalesTransaction salesTran = (SalesTransaction)salesTranList.get(i);
			salesTran.setLastUpdateDate(DateTimeUtil.getCurrentDateTime());
			getHibernateTemplate().update(salesTran);      
		}
	}

	public List getSalesTransactionItem(String collSalesOrderNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);	
		
		criteria.add(Restrictions.eq("collSalesOrderNo", collSalesOrderNo));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public SalesTransaction getSalesTrnByParent(long objectId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);	
		
		criteria.add(Restrictions.eq("objectId",new Long(objectId)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size() == 0)
		{
			return null;
		}
		else
		{
			for(int i = 0 ; i<result.size() ; i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
				getHibernateTemplate().initialize(salestrans.getCashierTransactions());
			}
			return (SalesTransaction)result.get(0);
		}
		
	}
	
	public List getSalesPartnerByParentOid(long parentSalesOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);
		
		criteria.add(Restrictions.eq("salesTransactionPartnerId.parentSalesOid",new Long(parentSalesOid)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if(result.size()==0)
			return null;
		else
		{
			for(int j=0;j<result.size();j++){
				SalesTransactionPartner partner = (SalesTransactionPartner)result.get(j);
				getHibernateTemplate().initialize(partner.getSalesTransactionMapChield());
				SalesTransaction tranChild = partner.getSalesTransactionMapChield();
				getHibernateTemplate().initialize(tranChild.getSalesTransactionItems());
				SalesTransaction tranParent = partner.getSalesTransactionMapParent();
				getHibernateTemplate().initialize(tranParent.getSalesTransactionItems());
			}    
			return result;
		}
	}
	
	public List getSalesPartnerByPartnerSalesOid(long parentSalesOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);
		
		criteria.add(Restrictions.eq("salesTransactionPartnerId.parentSalesOid",new Long(parentSalesOid)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if(result.size()==0)
			return null;
		else
		{
			for(int j=0;j<result.size();j++){
				SalesTransactionPartner partner = (SalesTransactionPartner)result.get(j);
				getHibernateTemplate().initialize(partner.getSalesTransactionMapChield());
				SalesTransaction tranChild = partner.getSalesTransactionMapChield();
				SalesTransaction tranParent = partner.getSalesTransactionMapParent();
			}    
			return result;
		}
	}

	public List getNotBilling(String storeId, Date fromDate, Date toDate){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		
		if(storeId != null && !storeId.equals("")){
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		String status = "P";
		if(status != null && !status.equals("")){
			criteria.createAlias("status","status");
			criteria.add(Restrictions.eq("status.transactionStatusId",status));
		}
		criteria.add(Restrictions.eq("typeId", "S"));
		criteria.add(Restrictions.between("transactionDate", fromDate, toDate));
		criteria.addOrder(Order.asc("transactionDate"));
		criteria.addOrder(Order.asc("posId"));
		criteria.addOrder(Order.asc("ticketNo"));
		return getHibernateTemplate().findByCriteria(criteria);
	}	
	
	public List getSalesDoc(String saleDocNo, Date trnDate, String posId, String ticketNo){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocument.class);
		criteria.add(Restrictions.eq("saleDocNo", saleDocNo));
		criteria.add(Restrictions.eq("transDate", trnDate));
		criteria.add(Restrictions.eq("posID", posId));
		criteria.add(Restrictions.eq("ticketNumber", ticketNo));	
		//criteria.add(Restrictions.eq("salesDocStatus", "SALEDOC"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size() == 0)
		{
			return null;
		}
		else
		{
			for(int i = 0 ; i<result.size() ; i++){
				SalesDocument salesDocument = (SalesDocument)result.get(i);
				getHibernateTemplate().initialize(salesDocument.getSalesDocumentGroups());
			
			}
			return result;
		}
		
	}
	
	public List getSalesTrnByPrmtnRdptnOid(String prmtnOid) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.createAlias("posPromotionRedemption", "posPromotionRedemption");
		criteria.add(Restrictions.eq("posPromotionRedemption.promotionRedemptionId", prmtnOid)); 
		return getHibernateTemplate().findByCriteria(criteria);	
	}
	
	public List getSalesTransByPrmtnRdptn(PromotionRedemption promotionRedemption) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("promotionRedemption", promotionRedemption)); 
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getSalesTransByPrmtnRdptnFromCA(PromotionRedemption posPromotionRedemption) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("posPromotionRedemption", posPromotionRedemption)); 
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getSalesTransByPosTicketStore(String pos, String ticket, String storeId) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("posId", pos));
		criteria.add(Restrictions.eq("ticketNo", ticket));	
		if(storeId != null && !storeId.equals("")){
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getSalesTransactionByCondition(Date trnDate, String ticketno, String pos_id, String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
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
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesTransaction salestrans = (SalesTransaction)result.get(i);
				getHibernateTemplate().initialize(salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(salestrans.getSalesTransactionItems());
			}
		}
		return result;
	}
}
