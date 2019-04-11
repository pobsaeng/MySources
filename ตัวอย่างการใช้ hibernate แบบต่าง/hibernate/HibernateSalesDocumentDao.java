package com.ie.icon.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import com.hp.www.common.util.ConnectionUtil;
import com.ie.icon.dao.SalesDocumentDao;
import com.ie.icon.domain.sale.SalesDocument;
import com.ie.icon.domain.sale.SalesDocumentGroup;
import com.ie.icon.domain.sale.SalesDocumentItem;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.SalesTransactionItem;
import com.ie.icon.domain.sale.TaxInvoice;
import com.ie.icon.domain.so.CollectSalesOrder;
import com.ie.icon.domain.so.DsVendorGroup;
import com.ie.icon.domain.so.SapReturnMsgLog;

public class HibernateSalesDocumentDao extends HibernateCommonDao implements SalesDocumentDao {
	public List getSalesDocument(String salesDocNo, Date trn_Date,String status, String posId, String ticketNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocument.class);
		criteria.add(Restrictions.eq("transDate", trn_Date));
		if (!"".equals(salesDocNo)) {
			criteria.add(Restrictions.eq("saleDocNo", salesDocNo));
		}
		if (!"".equals(status)) {
			criteria.createAlias("salesDocStatus", "salesDocStatus");
			criteria.add(Restrictions.eq("salesDocStatus.salesDocumentStatusId", status));
		}
		if (!"".equals(posId)) {
			criteria.add(Restrictions.eq("posID", posId));
		}
		if (!"".equals(ticketNo)) {
			criteria.add(Restrictions.eq("ticketNumber", ticketNo));
		}
		criteria.addOrder(Order.asc("ticketNumber"));
	 
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				SalesDocument salesdocument = (SalesDocument) result.get(i);
				getHibernateTemplate().initialize(salesdocument.getSalesDocumentItems());
				getHibernateTemplate().initialize(salesdocument.getSalesDocumentGroups());
				if (salesdocument.getSalesDocumentItems() != null
						&& salesdocument.getSalesDocumentItems().size() > 0) {
					for (int j = 0; j < salesdocument.getSalesDocumentItems().size(); j++) {
						SalesDocumentItem salesdocitem = (SalesDocumentItem) salesdocument.getSalesDocumentItems().get(j);
						getHibernateTemplate().initialize(salesdocitem.getSalesDocumentSetItems());
					}
				}
			}
		}
		return result;
	}

	public List getSalesDocumentById(long objectId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocument.class);
		criteria.add(Restrictions.eq("objectId", new Long(objectId)));
		criteria.addOrder(Order.asc("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				SalesDocument salesdocument = (SalesDocument) result.get(i);
				getHibernateTemplate().initialize(
						salesdocument.getSalesDocumentItems());
				getHibernateTemplate().initialize(
						salesdocument.getSalesDocumentGroups());
				if (salesdocument.getSalesDocumentItems() != null
						&& salesdocument.getSalesDocumentItems().size() > 0) {
					for (int j = 0; j < salesdocument.getSalesDocumentItems()
							.size(); j++) {
						SalesDocumentItem salesdocitem = (SalesDocumentItem) salesdocument
								.getSalesDocumentItems().get(j);
						getHibernateTemplate().initialize(
								salesdocitem.getSalesDocumentSetItems());
					}
				}
			}
		}
		return result;
	}

	public void create(SalesDocument salesdoc) throws DataAccessException {
		try {
			getHibernateTemplate().save(salesdoc);
			System.out.println("########## Create : SalesDocument ########## ");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	public void delete(long objectId) throws DataAccessException {
		try {
			SalesDocument salesdoc = (SalesDocument) getHibernateTemplate()
					.load(SalesDocument.class, new Long(objectId));
			getHibernateTemplate().delete(salesdoc);
			getHibernateTemplate().flush();
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(SalesDocument salesdoc) throws DataAccessException {
		
		try {
			getHibernateTemplate().update(salesdoc);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		
	}   
	
	public DsVendorGroup getGroupVendor(String vendorId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorGroup.class);
		criteria.createAlias("hpVendor", "hpVendor");
		criteria.add(Restrictions.eq("hpVendor.vendorId",vendorId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		DsVendorGroup ds = new DsVendorGroup();
		if(result.size() > 0){
			ds = (DsVendorGroup)result.get(0);
		}
		return ds;
	}
	
	public List getVendorGroups(String groupId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorGroup.class);
		criteria.add(Restrictions.eq("vendorGroupId.dsVendorId",groupId));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getSalesDocumentItem(String salesdocno) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentItem.class);
		criteria.createAlias("salesDocOId","salesDocOId");
		criteria.add(Restrictions.eq("salesDocOId.saleDocNo",salesdocno));
		criteria.addOrder(Order.desc("salesOrderNo"));
		List retult = getHibernateTemplate().findByCriteria(criteria);
		return retult;
	}
	
	public int checkSalesOrderInSaleDoc(String salesOrderNo) throws DataAccessException {
		int chk = 0;
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentItem.class);
		criteria.add(Restrictions.eq("salesOrderNo",salesOrderNo));
		criteria.createAlias("salesDocOId", "salesDocOId");
		criteria.createAlias("salesDocOId.salesDocStatus", "salesDocStatus");
		criteria.add(Restrictions.eq("salesDocStatus.salesDocumentStatusId", "BILLED"));
			
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size() > 0){
			chk++;
		}
		return chk;
	}
	
	public int checkSalesOrderInSaleDoc(String salesOrderNo, Date tr_date,String storeNo) throws DataAccessException {
		int chk = 0;
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentItem.class);
		criteria.add(Restrictions.eq("salesOrderNo",salesOrderNo));
		criteria.createAlias("salesDocOId", "salesDocOId");
		criteria.createAlias("salesDocOId.salesDocStatus", "salesDocStatus");
		criteria.add(Restrictions.eq("salesDocStatus.salesDocumentStatusId", "BILLED"));
		
		if(tr_date != null){
			criteria.add(Restrictions.eq("salesDocOId.transDate", tr_date));
		}		
		if(storeNo != null){
			criteria.createAlias("salesDocOId.store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeNo ));	
		}

		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size() > 0){
			chk++;
		}
		return chk;
	}
	
	
	public List getDocumentFlow(String salorderno, String pos_id, String ticket_no, String sap_saleorder_no, String saledocument_no, String do_no, String queue_no, String status, Date tr_date){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentItem.class);
		criteria.createAlias("salesDocOId", "salesDocOId");
		criteria.createAlias("salesDocGroupId", "salesDocGroupId");		
		if(!"".equals(salorderno)){
			criteria.add(Restrictions.eq("salesOrderNo", salorderno));
		}
		if(!"".equals(pos_id)){
			criteria.add(Restrictions.eq("salesDocOId.posID", pos_id));
		}
		if(!"".equals(ticket_no)){
			criteria.add(Restrictions.eq("salesDocOId.ticketNumber", ticket_no));
		}
		if(!"".equals(sap_saleorder_no)){
			criteria.add(Restrictions.eq("sapSONo", sap_saleorder_no));
		}
		if(!"".equals(saledocument_no)){
			criteria.add(Restrictions.eq("salesDocOId.saleDocNo", saledocument_no));
		}
		if(!"".equals(do_no)){
			
		}
		if(!"".equals(queue_no)){
			criteria.add(Restrictions.eq("salesDocGroupId.deliveryQuqeNo", queue_no));
		}
		if(!"".equals(status)){
			criteria.createAlias("salesDocOId.salesDocStatus", "salesDocStatus");
			criteria.add(Restrictions.eq("salesDocStatus.salesDocumentStatusId", status));			
		}
		if(tr_date != null){
			criteria.add(Restrictions.eq("salesDocOId.transDate", tr_date));
		}
		
		criteria.addOrder(Order.desc("salesDocOId"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		return result;
	}
	
	public List getSalesDocumentGroup(long objectid){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentGroup.class);		
		criteria.add(Restrictions.eq("objectId",new Long(objectid)));
		criteria.addOrder(Order.asc("objectId"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				SalesDocumentGroup salesdoc_group = (SalesDocumentGroup)result.get(i);
				getHibernateTemplate().initialize(salesdoc_group.getSalesDocumenttItems());
			}
		}
		
		return result;
	}	
	
	public List getSalesDocumentItem(String pos_id, String ticket_no, Date tranDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentItem.class);
		criteria.createAlias("salesDocOId","salesDocOId");
		criteria.add(Restrictions.eq("salesDocOId.posID",pos_id));
		criteria.add(Restrictions.eq("salesDocOId.ticketNumber",ticket_no));
		criteria.add(Restrictions.eq("salesDocOId.transDate",tranDate));
		criteria.createAlias("salesDocGroupId","salesDocGroupId");
		criteria.addOrder(Order.desc("salesDocGroupId.objectId"));
		criteria.addOrder(Order.desc("salesOrderNo"));
		criteria.addOrder(Order.desc("seqNo"));
		List retult = getHibernateTemplate().findByCriteria(criteria);
		return retult;
	}

	public void updateSalesTrans(SalesDocument salesdocument) throws DataAccessException {
		 getHibernateTemplate().update(salesdocument);
	}
	public SalesDocument getSalesDocuments(String ticketNo , String storeId , String posId , Date trnDate) {
		System.out.println("---- 1. ticketNo ----" + ticketNo);
		System.out.println("---- 2. storeId ----" + storeId);
		System.out.println("---- 3. posId ----" + posId);
		System.out.println("---- 4. trnDate ----" + trnDate);
		
		SalesDocument  salesDocument = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocument.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId ));	
		criteria.add(Restrictions.eq("transDate", trnDate));
		criteria.add(Restrictions.eq("posID", posId));
		criteria.add(Restrictions.eq("ticketNumber", ticketNo));
		
		List retult = getHibernateTemplate().findByCriteria(criteria);
		if (retult != null && retult.size() > 0) {
			for (int i = 0; i < retult.size(); i++) {
				salesDocument = (SalesDocument) retult.get(i);
			}

			getHibernateTemplate().initialize(salesDocument.getSalesDocumentGroups());
			for (int i = 0; i < salesDocument.getSalesDocumentGroups().size(); i++) {
				SalesDocumentGroup salesdoc_group = (SalesDocumentGroup) salesDocument.getSalesDocumentGroups().get(i);
				getHibernateTemplate().initialize(salesdoc_group.getSalesDocumenttItems());
			}
		}
		return salesDocument;
		
	}
	
	public SalesDocument getSalesDocumentsByKey(String ticketNo , String storeId , String posId , Date trnDate) {
		SalesDocument  salesDocument = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocument.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId ));	
		criteria.add(Restrictions.eq("transDate", trnDate));
		criteria.add(Restrictions.eq("posID", posId));
		criteria.add(Restrictions.eq("ticketNumber", ticketNo));
		
		List retult = getHibernateTemplate().findByCriteria(criteria);
		if(retult.size() > 0){
			  salesDocument = (SalesDocument)retult.get(0);
		}
		return salesDocument;
	}
	public SalesDocument getSalesDocumentByOid(String salesDocNo , Date trnDate , String storeId){
		//Pook edit set initial salesDoc =  null 26/11/2013
		SalesDocument salesDoc = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocument.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId ));	
		criteria.add(Restrictions.eq("transDate", trnDate));
		criteria.add(Restrictions.eq("saleDocNo", salesDocNo));
		
		List retult = getHibernateTemplate().findByCriteria(criteria);
		for(int i=0;i<retult.size();i++){
			salesDoc = (SalesDocument)retult.get(i);
		}
		
		return salesDoc;
	}
	
	public SalesTransaction getSalesTransaction(String pos_id, String ticket_no, Date tranDate) throws DataAccessException{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);		
		criteria.add(Restrictions.eq("transactionDate",tranDate));
		if(!"".equals(pos_id)){
			criteria.add(Restrictions.eq("posId",pos_id));
		}
		if(!"".equals(ticket_no)){
			criteria.add(Restrictions.eq("ticketNo",ticket_no));
		}		
		System.out.println("tranDate : "+tranDate);
		System.out.println("pos_id : "+pos_id);
		System.out.println("ticket_no : "+ticket_no);
		List result = getHibernateTemplate().findByCriteria(criteria);
		System.out.println("result.size() : "+result.size());
		if ( result.size() != 1 ) {
        	return null;
        }
		else {
			SalesTransaction saleTrans = (SalesTransaction)result.get(0);
			Hibernate.initialize(saleTrans.getSalesOrderTransaction());
			getHibernateTemplate().initialize(saleTrans.getSalesOrderTransaction());
			getHibernateTemplate().initialize(saleTrans.getSalesTransactionItems());
			getHibernateTemplate().initialize(saleTrans.getCashierTransactions());	
			//Soyhu added to get Discount item list .
			for ( int index = 0; index < saleTrans.getSalesTransactionItems().size(); index++ ) {
				SalesTransactionItem item = (SalesTransactionItem)saleTrans.getSalesTransactionItems().get(index);
				getHibernateTemplate().initialize(item.getItemDiscounts());
			}	
			return saleTrans;
        }
	}
	
	public List getMessageLogByTranNoTranDate(String tranNo , Date tranDate){
		DetachedCriteria criteria = DetachedCriteria.forClass(SapReturnMsgLog.class);
		List result = new ArrayList();
		
		if(tranDate != null && tranNo != null){
			criteria.add(Restrictions.eq("tranDate", tranDate));
			criteria.add(Restrictions.eq("tranNo", tranNo.trim()));
			result = getHibernateTemplate().findByCriteria(criteria);
		}else{
			return null;
		}
		
		return result;
	}
	
	public List getCreditNoteFromSaleTransaction(String ticket_no , String posId , Date tranDate , char typeId){
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("transactionDate", tranDate));
		criteria.add(Restrictions.eq("ticketNo", ticket_no));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("typeId",new Character(typeId)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		return result;
	}
	
	/** soyhu added */
	public CollectSalesOrder getCollectSalesOrderBySalesTransaction( SalesTransaction salestran){			
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectSalesOrder.class);
		if (salestran != null ){
			SalesTransaction tmp = this.getSalesTransaction(salestran.getPosId(), salestran.getTicketNo(), salestran.getTransactionDate());
			criteria.add(Restrictions.eq("collectSalesOrderNo", tmp.getCollSalesOrderNo()));
		}
		CollectSalesOrder collect = null;					
		try{
			List resultList = getHibernateTemplate().findByCriteria(criteria);			
			if (resultList != null && resultList.size() > 0) {   
				collect = (CollectSalesOrder) resultList.get(0);				
			}
		} catch (Exception e) {
			return null;
		}
		return collect;
	}
	
	/**@author Praewy
	 * @ 17 Jun 2009
	 */
	public SalesDocument getSalesDocBySapBillNo(String sapBillingNo, String storeId) {
		SalesDocument salesDocument = new SalesDocument();
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocument.class);

		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId ));	
		criteria.add(Restrictions.eq("sapBillingNo", sapBillingNo));
		
		List retult = getHibernateTemplate().findByCriteria(criteria);

		for(int i=0;i<retult.size();i++){
		 salesDocument = (SalesDocument)retult.get(i);
		}
		
		return salesDocument;
	}

	public SalesDocument getSalesDocuments(String qno) throws DataAccessException {
		SalesDocument doc = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentGroup.class);
		criteria.add(Restrictions.or(Restrictions.eq("deliveryQuqeNo",qno),Restrictions.eq("installQueueNo",qno)));	
		criteria.addOrder(Order.desc("objectId"));
		
		List retult = getHibernateTemplate().findByCriteria(criteria);
		for(int i=0;i<retult.size();i++){
			SalesDocumentGroup grp = (SalesDocumentGroup)retult.get(i);
			
			DetachedCriteria criteria2 = DetachedCriteria.forClass(SalesDocument.class);
			criteria2.add(Restrictions.eq("objectId",new Long(grp.getObjectId())));
			List retult2 = getHibernateTemplate().findByCriteria(criteria2);
			
			for(int j=0;j<retult2.size();j++){
				doc = (SalesDocument)retult2.get(i);
				break;
			}
			break;
		}
		
		return doc;
	}
	
	public Object getRefTransaction(String storeId, Date trnDate, String posId, String ticketNo, String jndiName){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet result = null;
        HashMap hash = null;
        
        try{
              String st = "select trn_dt, pos_id, ticket_no "
                    + " from sales_trn "
                    + " where sales_trn_oid in( "
                    + " select min(sales_tran_oid) from sales_tran_partner "
                    + " where parent_sales_oid in(  "
                    + " select sp.parent_sales_oid  "
                    + " from sales_trn s, sales_tran_partner sp  "
                    + " where s.sales_trn_oid = sp.sales_tran_oid "
                    + " and s.store_id = ? "
                    + " and s.trn_dt = ? "
                    + " and s.pos_id = ? "
                    + " and s.ticket_no = ? "
                    + " )) ";
              
              System.err.println("-----result   -----"+result);
              System.err.println("-----storeId   -----"+storeId);
              System.err.println("-----trnDate   -----"+new java.sql.Date(trnDate.getTime()));
              System.err.println("-----posId   -----"+posId);
              System.err.println("-----ticketNo   -----"+ticketNo);
              System.err.println("-----jndiName   -----"+jndiName);
              
              con = ConnectionUtil.getConnection(jndiName);               
              prepStmt = con.prepareStatement(st,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
              
              prepStmt.setString(1, storeId);                 
              prepStmt.setDate(2, new java.sql.Date(trnDate.getTime()));
              prepStmt.setString(3, posId);
              prepStmt.setString(4, ticketNo);
              
              result = prepStmt.executeQuery();
              
              while (result.next()){
                    hash = new HashMap();
                    hash.put("trnDate", result.getDate("trn_dt"));
                    hash.put("posId", result.getString("pos_id"));
                    hash.put("ticketNo", result.getString("ticket_no"));
                    break;
                    
              }
              
              st = null;
        }catch(Exception ex){
              ex.printStackTrace();
        }finally{
              try{
                    ConnectionUtil.closeConnection(con, prepStmt, result);
              }catch(Exception ex){
                    ex.printStackTrace();
              }
        }
        
        return hash;
	}
	public Object getFirstRefTransaction(String storeId, Date trnDate, String posId, String ticketNo, String jndiName){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet result = null;
        HashMap hash = null;
        
        try{
              String st = "select trn_dt, pos_id, ticket_no, sales_trn_oid"
                    + " from sales_trn "
                    + " where sales_trn_oid in( "
                    + " select min(sales_tran_oid) from sales_tran_partner "
                    + " where parent_sales_oid in(  "
                    + " select sp.parent_sales_oid  "
                    + " from sales_trn s, sales_tran_partner sp  "
                    + " where s.sales_trn_oid = sp.sales_tran_oid "
                    + " and s.store_id = ? "
                    + " and s.trn_dt = ? "
                    + " and s.pos_id = ? "
                    + " and s.ticket_no = ? "
                    + " )) ";
              
              System.err.println("-----result   -----"+result);
              System.err.println("-----storeId   -----"+storeId);
              System.err.println("-----trnDate   -----"+new java.sql.Date(trnDate.getTime()));
              System.err.println("-----posId   -----"+posId);
              System.err.println("-----ticketNo   -----"+ticketNo);
              System.err.println("-----jndiName   -----"+jndiName);
              
              con = ConnectionUtil.getConnection(jndiName);               
              prepStmt = con.prepareStatement(st,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
              
              prepStmt.setString(1, storeId);                 
              prepStmt.setDate(2, new java.sql.Date(trnDate.getTime()));
              prepStmt.setString(3, posId);
              prepStmt.setString(4, ticketNo);
              
              result = prepStmt.executeQuery();
              
              while (result.next()){
                    hash = new HashMap();
                    hash.put("trnDate", result.getDate("trn_dt"));
                    hash.put("posId", result.getString("pos_id"));
                    hash.put("ticketNo", result.getString("ticket_no"));
                    hash.put("salesTrnOid", result.getString("sales_trn_oid"));
                    
                    break;
                    
              }
              
              st = null;
        }catch(Exception ex){
              ex.printStackTrace();
        }finally{
              try{
                    ConnectionUtil.closeConnection(con, prepStmt, result);
              }catch(Exception ex){
                    ex.printStackTrace();
              }
        }
        
        return hash;
	}
	public List getSalesTrnPartner(String storeId, Date trnDate, String posId, String ticketNo, String jndiName){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet result = null;
        HashMap hash = null;
        List list = null;
        
        try{
        	StringBuffer sql = new StringBuffer();
        	sql.append(" SELECT TICKET_NO, POS_ID, TRN_DT, STORE_ID ");
        	sql.append("    FROM (SELECT TICKET_NO, POS_ID, TRN_DT, STORE_ID ");
        	sql.append("            FROM (SELECT S2.SALES_TRAN_OID ");
        	sql.append("                    FROM (SELECT sp.PARENT_SALES_OID ");
        	sql.append("                            FROM SALES_TRAN_PARTNER sp, sales_trn t ");
        	sql.append("                           WHERE     SP.SALES_TRAN_OID = T.SALES_TRN_OID ");
        	sql.append("                                 AND T.TICKET_NO = ? ");
        	sql.append("                                 AND T.POS_ID = ? ");
        	sql.append("                                 AND T.STORE_ID = ? ");
        	sql.append("                                 AND T.TRN_DT = ? ) S, ");
        	sql.append("                         SALES_TRAN_PARTNER S2 ");
        	sql.append("                   WHERE s.PARENT_SALES_OID = s2.PARENT_SALES_OID) A, ");
        	sql.append("                 sales_trn st ");
        	sql.append("           WHERE st.sales_trn_oid = A.SALES_TRAN_OID ");
        	sql.append("          UNION ALL ");
        	sql.append("          SELECT F.TICKET_NO, F.POS_ID, F.TRN_DT, F.STORE_ID ");
        	sql.append("            FROM (SELECT S2.SALES_TRAN_OID ");
        	sql.append("                    FROM (SELECT sp.PARENT_SALES_OID ");
        	sql.append("                            FROM SALES_TRAN_PARTNER sp, sales_trn t ");
        	sql.append("                           WHERE     SP.SALES_TRAN_OID = T.SALES_TRN_OID ");
        	sql.append("                                 AND T.TICKET_NO = ? ");
        	sql.append("                                 AND T.POS_ID = ? ");
        	sql.append("                                 AND T.STORE_ID = ? ");
        	sql.append("                                 AND T.TRN_DT = ? ");
        	sql.append("                                        				) S, ");
        	sql.append("                         SALES_TRAN_PARTNER S2 ");
        	sql.append("                   WHERE s.PARENT_SALES_OID = s2.PARENT_SALES_OID) A, ");
        	sql.append("                 sales_trn st, ");
        	sql.append("                 refund f ");
        	sql.append("           WHERE     A.SALES_TRAN_OID = st.sales_trn_oid ");
        	sql.append("                 AND ST.TICKET_NO = F.TICKET_NO ");
        	sql.append("                 AND ST.POS_ID = F.POS_ID ");
        	sql.append("                 AND ST.TRN_DT = F.TRN_DT ");
        	sql.append("                 AND ST.STORE_ID = F.STORE_ID) ");
        	sql.append(" GROUP BY TICKET_NO, POS_ID, TRN_DT, STORE_ID ");
        	sql.append(" HAVING COUNT (*) = 1 ");
              
              System.err.println("-----result   -----"+result);
              System.err.println("-----storeId   -----"+storeId);
              System.err.println("-----trnDate   -----"+new java.sql.Date(trnDate.getTime()));
              System.err.println("-----posId   -----"+posId);
              System.err.println("-----ticketNo   -----"+ticketNo);
              System.err.println("-----jndiName   -----"+jndiName);
              
              con = ConnectionUtil.getConnection(jndiName);               
              prepStmt = con.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
              
              prepStmt.setString(1, ticketNo);
              prepStmt.setString(2, posId);
              prepStmt.setString(3, storeId);                 
              prepStmt.setDate(4, new java.sql.Date(trnDate.getTime()));
              prepStmt.setString(5, ticketNo);
              prepStmt.setString(6, posId);
              prepStmt.setString(7, storeId);                 
              prepStmt.setDate(8, new java.sql.Date(trnDate.getTime()));
                                    
              result = prepStmt.executeQuery();
              list = new ArrayList();
              while (result.next()){
                    hash = new HashMap();
                    hash.put("trnDate", result.getDate("trn_dt"));
                    hash.put("posId", result.getString("pos_id"));
                    hash.put("ticketNo", result.getString("ticket_no"));
                    list.add(hash);
              }
              
        }catch(Exception ex){
              ex.printStackTrace();
        }finally{
              try{
                    ConnectionUtil.closeConnection(con, prepStmt, result);
              }catch(Exception ex){
                    ex.printStackTrace();
              }
        }
        
        return list;
	}
	
}
