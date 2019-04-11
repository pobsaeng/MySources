package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.common.util.DateTimeUtil;
import com.ie.icon.constant.Constant;
import com.ie.icon.dao.SalesDao;
import com.ie.icon.domain.cashier.CashierTransaction;
import com.ie.icon.domain.log.ItemNotFoundLog;
import com.ie.icon.domain.promotion.PromotionPremiumRedemption;
import com.ie.icon.domain.promotion.PromotionSales;
import com.ie.icon.domain.promotion.PromotionSalesItem;
import com.ie.icon.domain.sale.DiscountConditionType;
import com.ie.icon.domain.sale.SalesPerformance;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.SalesTransactionItem;
import com.ie.icon.domain.sale.SalesTransactionPartner;
import com.ie.icon.domain.sale.TransactionStatus;
import com.ie.icon.domain.so.SalesOrderTransaction;

public class HibernateSalesDao extends HibernateCommonDao implements SalesDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ie.icon.dao.SalesDao#create(com.ie.icon.domain.sale.SalesTransaction)
	 */
	public void create(SalesTransaction salesTransaction) {
		getHibernateTemplate().save(salesTransaction);
		System.out.println("########## Create : SalesTransaction ########## ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ie.icon.dao.SalesDao#update(com.ie.icon.domain.sale.SalesTransaction)
	 */
	public void update(SalesTransaction salesTransaction) {
		getHibernateTemplate().update(salesTransaction);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ie.icon.dao.SalesDao#delete(com.ie.icon.domain.sale.SalesTransaction)
	 */
	public void delete(SalesTransaction salesTransaction) {
		getHibernateTemplate().delete(salesTransaction);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.SalesDao#getSalesTransaction(java.lang.String)
	 */
	public SalesTransaction getSalesTransaction(String ticketNo, String posId) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.createAlias("salesTransactionItems", "salesTransactionItems");
		criteria.setFetchMode("salesTransactionItems", FetchMode.JOIN);
		criteria.addOrder(Order.asc("salesTransactionItems.seqNo"));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(tran.getTransactionTotalDiscounts());
			getHibernateTemplate().initialize(tran.getSalesOrderTransaction());

			return tran;
		}
	}

	public SalesTransaction getSalesTransaction(long objectId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionItem.class);
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.objectId", new Long(objectId)));
		criteria.addOrder(Order.asc("seqNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = new SalesTransaction();
			SalesTransactionItem item = (SalesTransactionItem) result.get(0);
			tran = item.getSalesTransaction();
			tran.getSalesTransactionItems().clear();
			int num = 1;
			for (int i = 0; i < result.size(); i++) {
				SalesTransactionItem tranItem = (SalesTransactionItem) result.get(i);
				
				//-- 02/09/2013 by Aon : Move Reference Ticket No.
				if(tranItem.getArticleNo().equals(Constant.ArticleDeposit) ){
					tranItem = setOwnerSalesTransaction(objectId, tranItem, num );
				}else{
					num++;
				}
				
				tran.addSalesTransactionItem(tranItem);
			}
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(tran.getSalesOrderTransaction());
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getTransactionTotalDiscounts());
			
			// Pawan Add for case payment ticket full error 22/12/2011
			if (tran.getPosPromotionRedemption() != null) {
				getHibernateTemplate().initialize(tran.getPosPromotionRedemption());
				getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionPremiumRedemptions());
				getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionNoReceiveRedemptions());
				getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionDiscountRedemptions());
				getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionSales());
				for (int j = 0; j < tran.getPosPromotionRedemption().getPromotionSales().size(); j++) {
					PromotionSales promotionSales = (PromotionSales) tran.getPosPromotionRedemption().getPromotionSales().get(j);
					for (int k = 0; k < promotionSales.getPromotionSalesItems().size(); k++) {
						PromotionSalesItem promotionSalesItem = (PromotionSalesItem) promotionSales.getPromotionSalesItems().get(k);
						getHibernateTemplate().initialize(promotionSalesItem);
					}
				}
			}

			try {
				if (tran.getCashierTransactions().size() > 0) {
					CashierTransaction cashTrans;
					int index = 0;
					do {
						cashTrans = (CashierTransaction) tran.getCashierTransactions().get(index);
						if (!cashTrans.getCashierType().getCashierTransactionTypeId().equals(Constant.CashierTransactionType.POS) && !cashTrans.getCashierType().getCashierTransactionTypeId().equals(Constant.CashierTransactionType.SO))
							tran.getCashierTransactions().remove(index);
						else
							index++;
					} while (index < tran.getCashierTransactions().size());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tran;
		}
	}
	
	//-- 02/09/2013 by Aon : Move Reference Ticket No.
	public SalesTransactionItem setOwnerSalesTransaction(long objectId, SalesTransactionItem tranItem, int num){
		List result = null;
		SalesTransactionPartner stp = null;
		SalesTransaction st = null;
	
		try{
			if(tranItem != null){
				//-- STEP 1 : Get SalesPartner
				stp = getSalesPartner(objectId);
				
				if(stp != null){
					//-- STEP 2 : Get Sales Tran From Partner by Sequence
					result = getSalesPartnerByParentSalesOid(stp.getSalesTransactionPartnerId().getParentSalesOid());
					stp = (SalesTransactionPartner)result.get(tranItem.getSeqNo()-num);
					st = getSalesTrnByParent(stp.getSalesTransactionPartnerId().getSalesTranOid());
					
					//-- STEP 3 : Set Ownber Sales Transaction
					tranItem.setOwnerSalesTransaction(st);
				}
			}
		}catch(Exception e){}
		
		return tranItem;
	}
	
	//-- 02/09/2013 by Aon : Move Reference Ticket No.
	public SalesTransactionPartner getSalesPartner(long salesTransactionOid){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);   
		criteria.add(Restrictions.eq("salesTransactionMapChield.objectId",new Long(salesTransactionOid)));

		List result = getHibernateTemplate().findByCriteria(criteria);    
		if(result.size()==0)   
			return null;          
		else
			return (SalesTransactionPartner)result.get(0);
	}
	
	//-- 02/09/2013 by Aon : Move Reference Ticket No.
	public List getSalesPartnerByParentSalesOid(long parentSalesOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);
		
		criteria.add(Restrictions.eq("salesTransactionPartnerId.parentSalesOid",new Long(parentSalesOid)));
		criteria.addOrder(Order.asc("salesTransactionPartnerId.salesTranOid"));
		
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
				getHibernateTemplate().initialize(tranChild.getCashierTransactions());
			}    
			return result;
		}
	}
	
	//-- 02/09/2013 by Aon : Move Reference Ticket No.
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

	public SalesTransaction getSalesTransactionByOid(long objectId) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("objectId", new Long(objectId)));
		criteria.createAlias("salesTransactionItems", "salesTransactionItems");
		criteria.setFetchMode("salesTransactionItems", FetchMode.JOIN);
		criteria.addOrder(Order.asc("salesTransactionItems.seqNo"));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(
					tran.getTransactionTotalDiscounts());
			getHibernateTemplate().initialize(tran.getSalesOrderTransaction());
			if (tran.getSalesTranPartnerParentItems().size() > 0) {
				System.out
						.println("tran.getSalesTranPartnerParentItems().size() : "
								+ tran.getSalesTranPartnerParentItems().size());
				for (int index = 0; index < tran
						.getSalesTranPartnerParentItems().size(); index++) {
					SalesTransactionPartner salesTranPartner = (SalesTransactionPartner) tran
							.getSalesTranPartnerParentItems().get(index);
					getHibernateTemplate().initialize(
							salesTranPartner.getSalesTransactionMapChield()
									.getSalesTransactionItems());
					getHibernateTemplate().initialize(salesTranPartner);
					System.out
							.println(" = = = = = initial complete = = = = = ");
				}
			}

			// Pawan Add for case payment ticket full error 22/12/2011
			// if (tran.getPosPromotionRedemption()!=null) {
			// getHibernateTemplate().initialize(tran.getPosPromotionRedemption());
			// getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionPremiumRedemptions());
			// getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionNoReceiveRedemptions());
			// getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionDiscountRedemptions());
			// getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionSales());
			// getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionDiscountRedemptions());
			//				
			// for ( int j=0;
			// j<tran.getPosPromotionRedemption().getPromotionSales().size();
			// j++ ) {
			// PromotionSales promotionSales =
			// (PromotionSales)tran.getPosPromotionRedemption().getPromotionSales().get(j);
			// for ( int k=0; k<promotionSales.getPromotionSalesItems().size();
			// k++ ) {
			// PromotionSalesItem promotionSalesItem =
			// (PromotionSalesItem)promotionSales.getPromotionSalesItems().get(k);
			// getHibernateTemplate().initialize(promotionSalesItem);
			// }
			// }
			// }

			return tran;
		}
	}

	public List getSalesTransaction(Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("transactionDate", transactionDate));

		DetachedCriteria statusCri = criteria.createCriteria("status");
		statusCri.add(Restrictions.eq("transactionStatusId", "P"));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public List getSalesTransaction(String storeId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("transactionDate", transactionDate));

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		DetachedCriteria statusCri = criteria.createCriteria("status");
		statusCri.add(Restrictions.eq("transactionStatusId", "P"));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public List getSalesTransaction(String storeId, Date transactionDate,
			String ticketNo, String posId) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("transactionDate", transactionDate));

		criteria.add(Restrictions.isNotNull("ticketNo"));
		criteria.add(Restrictions.like("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		DetachedCriteria statusCri = criteria.createCriteria("status");
		// EDIT TO CONSTANT
		statusCri.add(Restrictions.or(Restrictions.eq("transactionStatusId",
				"P"), (Restrictions.eq("transactionStatusId", "B"))));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result != null && result.size() > 0) {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(
					tran.getTransactionTotalDiscounts());
			// add
			getHibernateTemplate().initialize(tran.getSalesOrderTransaction());
		}
		return result;
	}

	public List getSalesTransaction(String storeId, Date fromDate, Date toDate) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.ge("transactionDate", fromDate));
		criteria.add(Restrictions.le("transactionDate", toDate));

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		DetachedCriteria statusCri = criteria.createCriteria("status");
		statusCri.add(Restrictions.eq("transactionStatusId", "P"));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public SalesTransaction getSalesTransaction(String storeId,
			String ticketNo, String posId, Date transDate, String cashierTypeId) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("transactionDate", transDate));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(
					tran.getTransactionTotalDiscounts());
			// Pawan Add 10/05/2012
			getHibernateTemplate().initialize(tran.getSalesOrderTransaction());
			try {
				if (tran.getCashierTransactions().size() > 0) {
					// temp process
					// remove cashier Type != cashierTypeId
					CashierTransaction cashTrans;
					int index = 0;
					do {
						cashTrans = (CashierTransaction) tran
								.getCashierTransactions().get(index);
						if (!cashTrans.getCashierType()
								.getCashierTransactionTypeId().equals(
										cashierTypeId))
							tran.getCashierTransactions().remove(index);
						else
							index++;
					} while (index < tran.getCashierTransactions().size());
				}
			} catch (Exception e) {
				Logger.getLogger(" ").log(Level.WARNING,
						"Cannot remove cashierTransaction Data");
				e.printStackTrace();
			}
			return tran;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.SalesDao#getSalesTransaction(java.lang.String,
	 * java.lang.String, java.util.Date, java.math.BigDecimal)
	 */
	public SalesTransaction getSalesTransaction(String storeId,String ticketNo, String posId, Date transactionDate,BigDecimal netTransactionAmount) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		// add
		System.out.println("transactionDate : "+transactionDate);
		System.out.println("ticketNo : "+ticketNo);
		System.out.println("posId : "+posId);
		System.out.println("storeId : "+storeId);
		System.out.println("netTransactionAmount : "+netTransactionAmount);
		
		if (transactionDate != null) {
			criteria.add(Restrictions.eq("transactionDate", transactionDate));
		}
		
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		
		/*if(ticketNo != null && !ticketNo.equals("")){
			criteria.add(Restrictions.eq("ticketNo", ticketNo));
		}
		else{
			criteria.add(Restrictions.isNull("ticketNo"));
		}*/
		
		criteria.add(Restrictions.eq("posId", posId));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		if (netTransactionAmount != null&& !netTransactionAmount.toString().equals("")) {
			criteria.add(Restrictions.eq("netTransactionAmount",netTransactionAmount));
		}
		List result = null;
		//tod add try
		try{
		    result = getHibernateTemplate().findByCriteria(criteria);
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
		System.out.println("result ==== >>> "+result);
		if (result.size() == 0)
			return null;
		else {
			
			System.out.println(" = = = = = = ELSE  = = = = = = = ");
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(tran.getTransactionTotalDiscounts());
			// Pawan Add 09/05/2012
			getHibernateTemplate().initialize(tran.getSalesOrderTransaction());
			
			List tranItems = (List) tran.getSalesTransactionItems();
			if (tranItems != null && !tranItems.isEmpty()) {
				for (int x = 0; x < tranItems.size(); x++) {
					SalesTransactionItem item = (SalesTransactionItem) tranItems.get(x);
					getHibernateTemplate().initialize(item.getSalesTransactionSetItems());
				}
			}
			
			if (tran.getCashierTransactions().size() > 0) {
				CashierTransaction cashTrans;
				int index = 0;
				do {
					cashTrans = (CashierTransaction) tran.getCashierTransactions().get(index);
					if (!cashTrans.getCashierType().getCashierTransactionTypeId().equals(Constant.CashierTransactionType.POS) && !cashTrans.getCashierType().getCashierTransactionTypeId().equals(Constant.CashierTransactionType.SO))
						tran.getCashierTransactions().remove(index);
					else
						index++;
				} while (index < tran.getCashierTransactions().size());
			}
			return tran;
		}
	}

	// ECM
	public List getSalesTransaction(Date transactionDate, String ticketNo,String posId) {
		System.out.println("========== List getSalesTransaction ==========");
		System.out.println("========== transactionDate ==========" + transactionDate);
		System.out.println("========== ticketNo ==========" + ticketNo);
		System.out.println("========== posId ==========" + posId);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		if (transactionDate != null) {
			criteria.add(Restrictions.eq("transactionDate", transactionDate));
		}
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		System.out.println("========== result ==========" + result.size());
		if (result != null && !result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				SalesTransaction tran = (SalesTransaction) result.get(i);
				getHibernateTemplate().initialize(tran.getSalesTransactionItems());
				List tranItems = (List) tran.getSalesTransactionItems();
				if (tranItems != null && !tranItems.isEmpty()) {
					for (int x = 0; x < tranItems.size(); x++) {
						SalesTransactionItem item = (SalesTransactionItem) tranItems.get(x);
						getHibernateTemplate().initialize(item.getSalesTransactionSetItems());
					}
				}

				getHibernateTemplate().initialize(tran.getCashierTransactions());
				getHibernateTemplate().initialize(tran.getTransactionTotalDiscounts());
				if (tran.getPosPromotionRedemption() != null) {
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption());
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionPremiumRedemptions());
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionNoReceiveRedemptions());
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionDiscountRedemptions());
				}
			}

		}
		return result;
	}

	// Appointment
	public SalesTransaction getSalesTransactionRedemption(String storeId,String ticketNo, String posId, Date transactionDate,BigDecimal netTransactionAmount) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		// add
		if (transactionDate != null) {
			criteria.add(Restrictions.eq("transactionDate", transactionDate));
		}
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		if (netTransactionAmount != null && !netTransactionAmount.toString().equals("")) {
			criteria.add(Restrictions.eq("netTransactionAmount",netTransactionAmount));
		}

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(tran.getTransactionTotalDiscounts());

			try {
				if (tran.getCashierTransactions().size() > 0) {
					/** temp process */
					/** remove cashier Type != 'POS' and 'SO' */
					CashierTransaction cashTrans;
					int index = 0;
					do {
						cashTrans = (CashierTransaction) tran.getCashierTransactions().get(index);
						if (!cashTrans.getCashierType().getCashierTransactionTypeId().equals(Constant.CashierTransactionType.POS) && !cashTrans.getCashierType().getCashierTransactionTypeId().equals(Constant.CashierTransactionType.SO))
							tran.getCashierTransactions().remove(index);
						else
							index++;
					} while (index < tran.getCashierTransactions().size());
				}
				// add
				if (tran.getPosPromotionRedemption() != null) {
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption());
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionPremiumRedemptions());
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionNoReceiveRedemptions());
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionDiscountRedemptions());
					getHibernateTemplate().initialize(tran.getPosPromotionRedemption().getPromotionSales());
					for (int i = 0; i < tran.getPosPromotionRedemption().getPromotionSales().size(); i++) {
						PromotionSales prmtnSales = (PromotionSales) tran.getPosPromotionRedemption().getPromotionSales().get(i);
						getHibernateTemplate().initialize(prmtnSales.getPromotionSalesItems());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return tran;
		}
	}

	public List getSalesTransaction(Date startDate, Date endDate, String upc,String posId, String customerName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.add(Restrictions.ge("createDate", startDate));
		criteria.add(Restrictions.le("createDate", endDate));

		if (posId != null) {
			if (posId.trim().length() > 0) {
				posId = getParameter(replace(posId));
				criteria.add(Restrictions.like("posId", posId));
			}
		}

		/** search in customer */
		if (customerName != null) {
			if (customerName.trim().length() > 0) {
				DetachedCriteria customerCri = criteria.createCriteria("customer");
				customerName = getParameter(replace(customerName));
				customerCri.add(Restrictions.like("firstName", customerName));
			}
		}

		/** search in salesTransactionItem */
		if (upc != null) {
			if (upc.trim().length() > 0) {
				DetachedCriteria salesItemCri = criteria.createCriteria("salesTransactionItems");
				upc = getParameter(replace(upc));
				salesItemCri.add(Restrictions.like("itemUPC", upc));
			}
		}

		criteria.addOrder(Order.asc("ticketNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else
			return result;
	}

	public List getSalesTransaction(Date startDate, Date endDate, String upc,String posId, String customerName, String description) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.add(Restrictions.ge("createDate", startDate));
		criteria.add(Restrictions.le("createDate", endDate));

		if (posId != null) {
			if (posId.trim().length() > 0) {
				posId = getParameter(replace(posId));
				criteria.add(Restrictions.like("posId", posId));
			}
		}

		/** search in customer */
		if (customerName != null) {
			if (customerName.trim().length() > 0) {
				DetachedCriteria customerCri = criteria.createCriteria("customer");
				customerName = getParameter(replace(customerName));
				customerCri.add(Restrictions.like("firstName", customerName));
			}
		}

		/** search in salesTransactionItem */
		if (upc != null) {
			if (upc.trim().length() > 0) {
				DetachedCriteria salesItemCri = criteria.createCriteria("salesTransactionItems");
				upc = getParameter(replace(upc));
				salesItemCri.add(Restrictions.like("itemUPC", upc));
			}
		}

		/** search in salesTransactionItem ProductName */
		if (description != null) {
			if (description.trim().length() > 0) {
				DetachedCriteria salesItemCri = criteria.createCriteria("salesTransactionItems");
				description = getParameter(replace(description));
				salesItemCri.add(Restrictions.like("description", description));
			}
		}

		criteria.addOrder(Order.asc("ticketNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else
			return result;
	}

	public List getSalesTransactionByPosId(String posId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("posId", posId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public SalesTransaction getSalesTransactionBySuspendId(String suspendId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);

		criteria.add(Restrictions.eq("suspendId", suspendId));
		criteria.createAlias("status", "status");
		criteria.add(Restrictions.eq("status.transactionStatusId",Constant.TransactionStatus.SUSPENDED));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else
			return (SalesTransaction) result.get(0);
	}

	public List getSalesTransactionsByStatus(String status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		DetachedCriteria statusCri = criteria.createCriteria("status");
		statusCri.add(Restrictions.eq("transactionStatusId", status));
		criteria.addOrder(Order.asc("suspendId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public DiscountConditionType getDiscountConditionType(String discountConditionTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
		criteria.add(Restrictions.eq("discountConditionTypeId",discountConditionTypeId));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else
			return (DiscountConditionType) result.get(0);
	}

	public TransactionStatus getTransactionStatus(String transactionStatusId) {
		return (TransactionStatus) getHibernateTemplate().get(TransactionStatus.class, transactionStatusId);
	}

	public void create(ItemNotFoundLog log) {
		getHibernateTemplate().save(log);
	}

	public List getDiscountConditionTypes(int discountType,boolean isPercentDiscount) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);

		criteria.add(Restrictions.eq("discountType", new Integer(discountType)));
		criteria.add(Restrictions.eq("isPercentDiscount", new Boolean(isPercentDiscount)));
		criteria.addOrder(Order.asc("discountConditionTypeId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getDiscountConditionTypes_checkType(int discountType,boolean isPercentDiscount) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);

		criteria.add(Restrictions.eq("discountType", new Integer(discountType)));
		criteria.add(Restrictions.eq("isPercentDiscount", new Boolean(isPercentDiscount)));
		criteria.add(Restrictions.eq("isPos", new Boolean(true)));
		criteria.add(Restrictions.eq("isSo", new Boolean(true)));
		criteria.addOrder(Order.asc("discountConditionTypeId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public BigDecimal totalTransactionNetAmount(final char param,final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(tran.netTransactionAmount) "
						+ "from SalesTransaction tran where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalTransactionNetAmountSO_Pos(final String userId,final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(tran.netTransactionAmount) "
						+ "from SalesTransaction tran where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public int totalTransaction(final char param, final String userId,final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran "
						+ "where tran.status.transactionStatusId = 'P' and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public int totalTransactionSO_Pos(final String userId, final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran "
						+ "where tran.status.transactionStatusId = 'P' and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public BigDecimal totalTransactionItem(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(item.quantity) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalTransactionItemSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(item.quantity) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalItemDiscountAmount(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(dscnt.discountAmount) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "dscntType.discountType <> 5 and "
						+ "dscntType.discountType <> 7 and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalItemDiscountAmountSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(dscnt.discountAmount) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "dscntType.discountType <> 5 and "
						+ "dscntType.discountType <> 7 and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public int totalItemDiscountTransaction(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(distinct tran.objectId) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType not in (5,6,7)) and "
						+
						// "(dscntType.discountType <> 5 and " +
						// "dscntType.discountType <> 6) and " +
						"tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public int totalItemDiscountTransactionSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(distinct tran.objectId) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType not in (5,6,7)) and "
						+
						// "(dscntType.discountType <> 5 and " +
						// "dscntType.discountType <> 6) and " +
						" ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public BigDecimal totalItemDiscountTransactionItem(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(item.quantity) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType not in (5,6,7)) and "
						+
						// "(dscntType.discountType <> 5 and " +
						// "dscntType.discountType <> 6) and " +
						"tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalItemDiscountTransactionItemSO_Pos(
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(item.quantity) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType not in (5,6,7)) and "
						+
						// "(dscntType.discountType <> 5 and " +
						// "dscntType.discountType <> 6) and " +
						" ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalPriceOverrideAmount(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(dscnt.discountAmount) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType = 5 or "
						+ "dscntType.discountType = 6) and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalPriceOverrideAmountSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(dscnt.discountAmount) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType = 5 or "
						+ "dscntType.discountType = 6) and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public int totalPriceOverrideTransaction(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(distinct tran.objectId) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType = 5 or "
						+ "dscntType.discountType = 6) and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public int totalPriceOverrideTransactionSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(distinct tran.objectId) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType = 5 or "
						+ "dscntType.discountType = 6) and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public BigDecimal totalPriceOverrideTransactionItem(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(item.quantity) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType = 5 or "
						+ "dscntType.discountType = 6) and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalPriceOverrideTransactionItemSO_Pos(
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(item.quantity) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item join "
						+ "item.itemDiscounts dscnt join "
						+ "dscnt.discountConditionType dscntType where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "(dscntType.discountType = 5 or "
						+ "dscntType.discountType = 6) and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public int totalSuspendTransaction(final char param, final String userId,
			final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran where "
						+ "(tran.status.transactionStatusId = 'S' or "
						+ "tran.status.transactionStatusId = 'R') and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public int totalSuspendTransactionSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran where "
						+ "(tran.status.transactionStatusId = 'S' or "
						+ "tran.status.transactionStatusId = 'R') and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public BigDecimal totalSuspendTransactionNetAmount(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(tran.netTransactionAmount) from "
						+ "SalesTransaction tran where "
						+ "(tran.status.transactionStatusId = 'S' or "
						+ "tran.status.transactionStatusId = 'R') and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalSuspendTransactionNetAmountSO_Pos(
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(tran.netTransactionAmount) from "
						+ "SalesTransaction tran where "
						+ "(tran.status.transactionStatusId = 'S' or "
						+ "tran.status.transactionStatusId = 'R') and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalTotalDiscountAmount(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(dscnt.discountAmount) from "
						+ "SalesTransaction tran join "
						+ "tran.transactionTotalDiscounts dscnt where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public BigDecimal totalTotalDiscountAmountSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(dscnt.discountAmount) from "
						+ "SalesTransaction tran join "
						+ "tran.transactionTotalDiscounts dscnt where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return new BigDecimal("0.00");

		return rv.value;
	}

	public int totalTotalDiscountTransaction(final char param,
			final String userId, final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran join "
						+ "tran.transactionTotalDiscounts dscnt where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "tran.typeId like ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public int totalTotalDiscountTransactionSO_Pos(final String userId,
			final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran join "
						+ "tran.transactionTotalDiscounts dscnt where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ?";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public int totalNumOfItems(final char param, final boolean isScannedItem,
			final String userId, final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "item.isScannedItem = ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ? and "
						+ "tran.typeId like ?";
				Query query = session.createQuery(queryString);
				if (isScannedItem)
					query.setCharacter(0, 'Y');
				else
					query.setCharacter(0, 'N');
				query.setString(1, userId);
				query.setDate(2, trnDate);
				query.setCharacter(3, param);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public int totalNumOfItemsSO_Pos(final boolean isScannedItem,
			final String userId, final Date trnDate) {
		class ReturnValue {
			Integer value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select count(*) from "
						+ "SalesTransaction tran join "
						+ "tran.salesTransactionItems item where "
						+ "tran.status.transactionStatusId = 'P' and "
						+ "item.isScannedItem = ? and "
						+ "tran.createUserId like ? and "
						+ "tran.transactionDate = ? and "
						+ " ((tran.typeId = 'P' ) OR (tran.typeId = 'S'))  ";
				Query query = session.createQuery(queryString);
				if (isScannedItem)
					query.setCharacter(0, 'Y');
				else
					query.setCharacter(0, 'N');
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = (Integer) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			return 0;

		return rv.value.intValue();
	}

	public void deleteByStatus(String statusId) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		criteria.createAlias("status", "status");
		criteria.add(Restrictions.eq("status.transactionStatusId", statusId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		getHibernateTemplate().deleteAll(result);
	}

	public SalesPerformance getSalesPerformance(String storeId,
			char salesTransTypeId, Date fromDate, Date toDate, Date toDateChk) {
		String queryString = "select sum(sales.netTransactionAmount), sum(sales.numOfItems), count(*) "
				+ "from SalesTransaction sales "
				+ "where sales.createDate >= :fromDate "
				+ "  and sales.createDate < :toDate "
				+ "  and sales.typeId = :typeId"
				+ "  and sales.status.transactionStatusId = :stsId"
				+ "  and sales.store.storeId = :storeId";

		List list = getHibernateTemplate().findByNamedParam(
				queryString,
				new String[] { "fromDate", "toDate", "typeId", "stsId",
						"storeId" },
				new Object[] { fromDate, toDateChk,
						new Character(salesTransTypeId), new Character('P'),
						storeId });

		if (list.size() != 1)
			return null;
		else {
			Object[] object = (Object[]) list.get(0);
			SalesPerformance salesPerf = new SalesPerformance();
			salesPerf.setSalesTypeId(salesTransTypeId);
			salesPerf.setFromTime(fromDate);
			salesPerf.setToTime(toDate);
			if (object[0] != null)
				salesPerf
						.setNetAmount(Double.parseDouble(object[0].toString()));
			else
				salesPerf.setNetAmount(0);

			if (object[2] != null)
				salesPerf.setTransactionQty(Integer.parseInt(object[2]
						.toString()));
			else
				salesPerf.setTransactionQty(0);

			/** get total item quantity */
			queryString = "select sum(salesItem.quantity), count(salesItem.quantity) "
					+ "from SalesTransaction sales join "
					+ "sales.salesTransactionItems salesItem "
					+ "where sales.createDate >= :fromDate "
					+ "  and sales.createDate < :toDate "
					+ "  and sales.typeId = :typeId"
					+ "  and sales.status.transactionStatusId = :stsId"
					+ "  and sales.store.storeId = :storeId";

			list = getHibernateTemplate().findByNamedParam(
					queryString,
					new String[] { "fromDate", "toDate", "typeId", "stsId",
							"storeId" },
					new Object[] { fromDate, toDateChk,
							new Character(salesTransTypeId),
							new Character('P'), storeId });
			if (list.size() == 1) {
				Object[] objectTmp = (Object[]) list.get(0);
				if (objectTmp[0] != null)
					salesPerf.setNetItemQty(Double.parseDouble(objectTmp[0]
							.toString()));
				else
					salesPerf.setNetItemQty(0);
			}

			return salesPerf;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.SalesDao#getSalesTransactionItem(String , String,
	 * String, int)
	 */
	public SalesTransactionItem getSalesTransactionItem(String ticketNo,
			Date trnDate, String storeId, String posId, int seqNo) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransactionItem.class);

		criteria.add(Restrictions.eq("seqNo", new Integer(seqNo)));
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.transactionDate",
				trnDate));
		criteria.add(Restrictions.eq("salesTransaction.ticketNo", ticketNo));
		criteria.add(Restrictions.eq("salesTransaction.posId", posId));
		criteria.createAlias("salesTransaction.store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 1)
			return (SalesTransactionItem) result.get(0);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ie.icon.dao.SalesDao#update(com.ie.icon.domain.sale.SalesTransactionItem
	 * )
	 */
	public void update(SalesTransactionItem item) {
		getHibernateTemplate().update(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.SalesDao#getSalesTransaction(java.lang.String,
	 * java.lang.String, java.lang.String, java.util.Date)
	 */
	public SalesTransaction getSalesTransaction(String ticketNo,String storeId, String posId, Date trnDate) {
		System.out.println("========== SalesTransaction getSalesTransaction ==========");
		System.out.println("========== transactionDate ==========" + ticketNo);
		System.out.println("========== ticketNo ==========" + storeId);
		System.out.println("========== posId ==========" + posId);
		System.out.println("========== posId ==========" + trnDate);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);

		criteria.add(Restrictions.eq("transactionDate", trnDate));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));

		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		System.out.println("========== result ==========" + result.size());
		
		if (result.size() == 0)
			return null;
		else
			return (SalesTransaction) result.get(0);
	}
	
	public List getSalesTransactionList(String ticketNo,String storeId, String posId, Date trnDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("transactionDate", trnDate));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0)
			return null;
		else
			return  result;
	}

	public SalesTransaction getSalesTransactionForRedemption(String storeId,
			String taxInvoiceNo, Date transactionDate,
			BigDecimal netTransactionAmount) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.eq("taxInvoiceNo", taxInvoiceNo));
		criteria.add(Restrictions.eq("netTransactionAmount",
				netTransactionAmount));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			initialData(tran);
			return tran;
		}
	}

	public SalesTransaction getSalesTransactionForRedemption(String storeId,
			String ticketNo, String posId, Date transactionDate,
			BigDecimal netTransactionAmount) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("netTransactionAmount",
				netTransactionAmount));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			initialData(tran);
			return tran;
		}
	}

	public SalesTransaction getSalesTransaction(String storeId,String ticketNo, String posId, char salesTransTypeId,Date transactionDate) {
		System.out.println("<<<< storeId >>>>" + storeId );
		System.out.println("<<<< ticketNo >>>>" + ticketNo );
		System.out.println("<<<< posId >>>>" + posId );
		System.out.println("<<<< salesTransTypeId >>>>" + salesTransTypeId );
		System.out.println("<<<< transactionDate >>>>" + transactionDate );
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		char pos = Constant.SalesTransactionType.POS;
		char so = Constant.SalesTransactionType.SO;
		char ar = Constant.SalesTransactionType.AR;
		// if salesTransTypeId equals some type set X to other type for not
		// select
		switch (salesTransTypeId) {
		case Constant.SalesTransactionType.POS:
			so = 'X';
			ar = 'X';
			break;
		case Constant.SalesTransactionType.SO:
			pos = 'X';
			break;
		}

		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.in("typeId", new Character[] {new Character(pos), new Character(so), new Character(ar) }));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(tran.getTransactionTotalDiscounts());
			initialDataWithoutReturn(tran);
			return tran;
		}
	}

	public SalesTransaction getSalesTransaction(String storeId,
			String taxInvoiceNo, char salesTransTypeId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		char pos = Constant.SalesTransactionType.POS;
		char so = Constant.SalesTransactionType.SO;
		char ar = Constant.SalesTransactionType.AR;
		// if salesTransTypeId equals some type set X to another type for not
		// select
		switch (salesTransTypeId) {
		case Constant.SalesTransactionType.POS:
			so = 'X';
			ar = 'X';
			break;
		case Constant.SalesTransactionType.SO:
			pos = 'X';
			break;
		}

		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.eq("isTaxInvoice", new Boolean(true)));
		criteria.add(Restrictions.eq("taxInvoiceNo", taxInvoiceNo));
		criteria.add(Restrictions.in("typeId", new Character[] {
				new Character(pos), new Character(so), new Character(ar) }));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			initialDataWithoutReturn(tran);
			return tran;
		}
	}

	private void initialData(SalesTransaction sales) {
		getHibernateTemplate().initialize(sales.getSalesTransactionItems());
		getHibernateTemplate().initialize(sales.getCashierTransactions());
		getHibernateTemplate().initialize(sales.getTransactionTotalDiscounts());

		// initial item discount
		for (int index = 0; index < sales.getSalesTransactionItems().size(); index++) {
			SalesTransactionItem item = (SalesTransactionItem) sales
					.getSalesTransactionItems().get(index);
			getHibernateTemplate().initialize(item.getItemDiscounts());
		}
	}

	private void initialDataWithoutReturn(SalesTransaction sales) {
		getHibernateTemplate().initialize(sales.getSalesTransactionItems());
		getHibernateTemplate().initialize(sales.getCashierTransactions());
		getHibernateTemplate().initialize(sales.getTransactionTotalDiscounts());

		// initial item discount
		for (int index = 0; index < sales.getSalesTransactionItems().size(); index++) {
			SalesTransactionItem item = (SalesTransactionItem) sales
					.getSalesTransactionItems().get(index);
			getHibernateTemplate().initialize(item.getItemDiscounts());
		}

		if (sales.getCashierTransactions().size() > 0) {
			/** temp process */
			/** remove cashier Type != 'POS' and 'SO' */
			CashierTransaction cashTrans;
			int index = 0;
			do {
				cashTrans = (CashierTransaction) sales.getCashierTransactions()
						.get(index);
				if (!cashTrans.getCashierType().getCashierTransactionTypeId()
						.equals(Constant.CashierTransactionType.POS)
						&& !cashTrans.getCashierType()
								.getCashierTransactionTypeId().equals(
										Constant.CashierTransactionType.SO))
					sales.getCashierTransactions().remove(index);
				else
					index++;
			} while (index < sales.getCashierTransactions().size());
		}
	}

	public SalesTransactionItem getSalesTransactionItem(String storeId,
			String ticketNo, String posId, Date trnDate, char salesTypeId,
			int seqNo) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransactionItem.class);

		criteria.add(Restrictions.eq("seqNo", new Integer(seqNo)));
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.add(Restrictions.eq("salesTransaction.transactionDate",
				trnDate));
		criteria.add(Restrictions.eq("salesTransaction.ticketNo", ticketNo));
		criteria.add(Restrictions.eq("salesTransaction.posId", posId));
		criteria.add(Restrictions.eq("salesTransaction.typeId", new Character(
				salesTypeId)));
		criteria.createAlias("salesTransaction.store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 1)
			return (SalesTransactionItem) result.get(0);
		else
			return null;
	}

	public List getSalesTransaction(String storeId, Date transactionDate,
			String ticketNo, String posId, char salesType) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		char pos = Constant.SalesTransactionType.POS;
		char so = Constant.SalesTransactionType.SO;
		char ar = Constant.SalesTransactionType.AR;
		// if salesTransTypeId equals some type set X to other type for not
		// select
		switch (salesType) {
		case Constant.SalesTransactionType.POS:
			so = 'X';
			ar = 'X';
			break;
		case Constant.SalesTransactionType.SO:
			pos = 'X';
			break;
		}

		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		if (ticketNo != null && !"".equals(ticketNo.trim()))
			criteria.add(Restrictions.eq("ticketNo", ticketNo));
		if (posId != null && !"".equals(posId.trim()))
			criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.in("typeId", new Character[] {
				new Character(pos), new Character(so), new Character(ar) }));

		if (storeId != null && !"".equals(storeId.trim())) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		criteria.createAlias("status", "status");
		criteria.add(Restrictions.eq("status.transactionStatusId", String
				.valueOf(Constant.SalesTransactionType.POS)));
		criteria.addOrder(Order.asc("ticketNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				SalesTransaction salestrans = (SalesTransaction) result.get(i);
				getHibernateTemplate().initialize(
						salestrans.getCashierTransactions());
				getHibernateTemplate().initialize(
						salestrans.getSalesOrderTransaction());
				getHibernateTemplate().initialize(
						salestrans.getSalesTransactionItems());

				for (int index = 0; index < salestrans
						.getSalesTransactionItems().size(); index++) {
					SalesTransactionItem item = (SalesTransactionItem) salestrans
							.getSalesTransactionItems().get(index);
					getHibernateTemplate().initialize(item.getItemDiscounts());
				}
			}
		}
		return result;
	}

	public BigDecimal getMCAmount(final String storeId,
			final Date transactionDate, final String mc) {
		class ReturnValue {
			BigDecimal value;
		}
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "select sum(item.netItemAmount) from "
						+ "SalesTransaction tran "
						+ "join tran.salesTransactionItems item "
						+ "join tran.store store "
						+ "where tran.status.transactionStatusId = 'P' "
						+ " and tran.transactionDate = ?" +
						// " and substring(item.mc9, 1, 2) = ?";
						" and item.mc9 like ?";
				if (storeId != null && !"".equals(storeId))
					queryString += " and store.storeId = ?";
				Query query = session.createQuery(queryString);
				query.setDate(0, transactionDate);
				query.setString(1, mc + "%");
				if (storeId != null && !"".equals(storeId))
					query.setString(2, storeId);

				rv.value = (BigDecimal) query.uniqueResult();
				return null;
			}
		});

		if (rv.value == null)
			rv.value = new BigDecimal("0.00");

		return rv.value;
	}

	public SalesTransactionPartner getSalesTransactionPartner(SalesTransactionPartner obj) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionPartner.class);

		criteria.add(Restrictions.eq("salesTransactionPartnerId.salesTranOid",new Long(obj.getSalesTransactionPartnerId().getSalesTranOid())));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && !result.isEmpty()) {
			return (SalesTransactionPartner) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @author Praewy 16 Jun 2009
	 */
	public SalesTransaction getSalesTransactionByBillNo(String storeId,
			String sapBillNo) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		criteria.add(Restrictions.eq("sapBillNo", sapBillNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);

			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(
					tran.getTransactionTotalDiscounts());

			/*
			 * try { if ( tran.getCashierTransactions().size() > 0 ) { /** temp
			 * process
			 */
			/**
			 * remove cashier Type != 'POS' and 'SO' CashierTransaction
			 * cashTrans; int index = 0; do { cashTrans = (CashierTransaction)
			 * tran.getCashierTransactions().get( index ); if (
			 * !cashTrans.getCashierType().getCashierTransactionTypeId().equals(
			 * Constant.CashierTransactionType.POS ) &&
			 * !cashTrans.getCashierType().getCashierTransactionTypeId().equals(
			 * Constant.CashierTransactionType.SO ) )
			 * tran.getCashierTransactions().remove( index ); else index++; }
			 * while ( index < tran.getCashierTransactions().size()); } } catch
			 * ( Exception e ) { e.printStackTrace(); }
			 */

			return tran;
		}
	}

	public List getList_SalesTransactionByBillNo(String storeId,
			String sapBillNo) {

		Date transactionDate = new Date();
		transactionDate = DateTimeUtil.getDateFromString("01112009");
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		criteria.add(Restrictions.eq("sapBillNo", sapBillNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.gt("transactionDate", transactionDate));

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0)
			return null;
		else {
			for (int i = 0; i < result.size(); i++) {
				SalesTransaction tran = (SalesTransaction) result.get(i);
				getHibernateTemplate().initialize(
						tran.getSalesTransactionItems());
				getHibernateTemplate().initialize(
						tran.getTransactionTotalDiscounts());
				getHibernateTemplate().initialize(
						tran.getSalesTransactionItems());
				getHibernateTemplate()
						.initialize(tran.getCashierTransactions());
				getHibernateTemplate().initialize(
						tran.getTransactionTotalDiscounts());
			}
			return result;
		}
	}

	/**
	 * @author Praewy 16 Jun 2009
	 */
	public SalesTransaction getSalesTransactionByBillNo(long objectId) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.eq("objectId", new Long(objectId)));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(
					tran.getTransactionTotalDiscounts());

			try {
				if (tran.getCashierTransactions().size() > 0) {
					/** temp process */
					/** remove cashier Type != 'POS' && 'SO' */
					CashierTransaction cashTrans;
					int index = 0;
					do {
						cashTrans = (CashierTransaction) tran
								.getCashierTransactions().get(index);
						if (!cashTrans.getCashierType()
								.getCashierTransactionTypeId().equals(
										Constant.CashierTransactionType.POS)
								&& !cashTrans
										.getCashierType()
										.getCashierTransactionTypeId()
										.equals(
												Constant.CashierTransactionType.SO))
							tran.getCashierTransactions().remove(index);
						else
							index++;
					} while (index < tran.getCashierTransactions().size());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tran;
		}
	}

	public List getSalesTrnByRdptnDateAndRdptnOid(Date rdptnDate,
			Date beforeDate, long RdptnOid) throws Exception {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		criteria.createAlias("promotionRedemption", "promotionRedemption");
		criteria.add(Restrictions.eq("promotionRedemption.objectId", new Long(
				RdptnOid)));

		criteria.add(Restrictions.ge("transactionDate", beforeDate));
		criteria.add(Restrictions.le("transactionDate", rdptnDate));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getSalesTransaction(Map map) {

		Date startDate = null;
		Date endDate = null;
		String upc = null;
		String posId = null;
		String customerName = null;
		String storeId = null;

		if (map.get("startDate") != null)
			startDate = (Date) map.get("startDate");
		if (map.get("endDate") != null)
			endDate = (Date) map.get("endDate");
		if (map.get("upc") != null)
			upc = (String) map.get("upc");
		if (map.get("posId") != null)
			posId = (String) map.get("posId");
		if (map.get("customerName") != null)
			customerName = (String) map.get("customerName");
		if (map.get("storeId") != null)
			storeId = (String) map.get("storeId");

		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);
		criteria.add(Restrictions.ge("createDate", startDate));
		criteria.add(Restrictions.le("createDate", endDate));

		if (posId != null) {
			if (posId.trim().length() > 0) {
				posId = getParameter(replace(posId));
				criteria.add(Restrictions.like("posId", posId));
			}
		}

		/** search in customer */
		if (customerName != null) {
			if (customerName.trim().length() > 0) {
				DetachedCriteria customerCri = criteria
						.createCriteria("customer");
				customerName = getParameter(replace(customerName));
				customerCri.add(Restrictions.like("firstName", customerName));
			}
		}

		/** search in salesTransactionItem */
		if (upc != null) {
			if (upc.trim().length() > 0) {
				DetachedCriteria salesItemCri = criteria
						.createCriteria("salesTransactionItems");
				upc = getParameter(replace(upc));
				salesItemCri.add(Restrictions.like("itemUPC", upc));
			}
		}
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}

		criteria.addOrder(Order.asc("ticketNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else
			return result;
	}

	// add
	public SalesTransaction getSalesTransactionByTicket(String storeId,
			Date transactionDate, String ticketNo, String posId)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesTransaction.class);

		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result != null && result.size() > 0) {
			SalesTransaction tran = (SalesTransaction) result.get(0);
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(
					tran.getTransactionTotalDiscounts());
			getHibernateTemplate().initialize(tran.getPromotionRedemption());
			getHibernateTemplate().initialize(tran.getPosPromotionRedemption());

			if (tran.getPromotionRedemption() != null
					&& tran.getPromotionRedemption()
							.getPromotionPremiumRedemptions() != null) {
				for (int index = 0; index < tran.getPromotionRedemption()
						.getPromotionPremiumRedemptions().size(); index++) {
					PromotionPremiumRedemption premium = (PromotionPremiumRedemption) tran
							.getPromotionRedemption()
							.getPromotionPremiumRedemptions().get(index);
					getHibernateTemplate().initialize(premium);
				}
			}
		}

		if (result != null && result.size() > 0) {
			return (SalesTransaction) result.get(0);
		} else {
			return null;
		}
	}

	public DiscountConditionType getDiscountConditionTypeIsPosSO(
			String reasonCode) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(DiscountConditionType.class);

		criteria.add(Restrictions.eq("discountConditionTypeId", reasonCode));
		criteria.add(Restrictions.eq("isPos", new Boolean(true)));
		criteria.add(Restrictions.eq("isSo", new Boolean(true)));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else
			return (DiscountConditionType) result.get(0);
	}

	public SalesTransaction getSalesTransactionByTicketWithTransactionDate(
			String storeId, Date transactionDate, String ticketNo, String posId)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SalesOrderTransaction.class);
		/*
		 * pornpan change condition transactionDate from SaleTransaction to
		 * SaleOrderTransaction
		 */
		
		criteria.createAlias("salesTransaction", "salesTransaction");
		// TOEY BEGIN ADD salesTransaction.transactionDate for cross over day
		criteria.add(Restrictions.eq("salesOrderTransactionId.transactionDate",
				transactionDate));
		// TOEY END ADD salesTransaction.transactionDate for cross over day
		criteria.add(Restrictions.eq("salesTransaction.ticketNo", ticketNo));
		criteria.add(Restrictions.eq("salesTransaction.posId", posId));
		criteria.createAlias("salesTransaction.store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result != null && result.size() > 0) {
			SalesOrderTransaction trans = (SalesOrderTransaction) result.get(0);
			SalesTransaction tran = (SalesTransaction) trans
					.getSalesTransaction();
			getHibernateTemplate().initialize(tran.getSalesTransactionItems());
			getHibernateTemplate().initialize(tran.getCashierTransactions());
			getHibernateTemplate().initialize(
					tran.getTransactionTotalDiscounts());
			getHibernateTemplate().initialize(tran.getPromotionRedemption());
			getHibernateTemplate().initialize(tran.getPosPromotionRedemption());

			if (tran.getPromotionRedemption() != null
					&& tran.getPromotionRedemption()
							.getPromotionPremiumRedemptions() != null) {
				for (int index = 0; index < tran.getPromotionRedemption()
						.getPromotionPremiumRedemptions().size(); index++) {
					PromotionPremiumRedemption premium = (PromotionPremiumRedemption) tran
							.getPromotionRedemption()
							.getPromotionPremiumRedemptions().get(index);
					getHibernateTemplate().initialize(premium);
				}
			}
		}

		if (result != null && result.size() > 0) {
			SalesOrderTransaction trans = (SalesOrderTransaction) result.get(0);
			SalesTransaction tran = (SalesTransaction) trans
					.getSalesTransaction();
			return tran;
		} else {
			return null;
		}
	}

}