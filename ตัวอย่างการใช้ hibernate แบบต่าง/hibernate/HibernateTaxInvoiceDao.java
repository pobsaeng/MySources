package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.TaxInvoiceDao;
import com.ie.icon.domain.TaxCode;
import com.ie.icon.domain.sale.InvoiceTender;
import com.ie.icon.domain.sale.PriceConditionType;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.TaxInvoice;
import com.ie.icon.domain.sale.TransactionStatus;

public class HibernateTaxInvoiceDao extends HibernateCommonDao implements TaxInvoiceDao {
	private Character taxInvoiceType[] = new Character[]{new Character(Constant.TaxInvoiceType.SALE), new Character(Constant.TaxInvoiceType.AR)};

	public void create(TaxInvoice taxInvoice) {
		getHibernateTemplate().save(taxInvoice);
	}
 
	public List getTaxInvoiceCN(Date issueDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("typeId",new Character(Constant.TaxInvoiceType.CREDIT_NOTE)));
		criteria.add(Restrictions.eq("issueDate", issueDate));
		criteria.addOrder(Order.asc("createDateTime"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getTaxInvoiceFullTax(Date issueDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("typeId",new Character(Constant.TaxInvoiceType.S)));
		criteria.add(Restrictions.eq("issueDate", issueDate));
		criteria.addOrder(Order.asc("createDateTime"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public TaxInvoice getTaxInvoiceByTicketNo(String storeId, char typeId, String ticketNo, String posId, Date transactionDate,BigDecimal netTransactionAmount) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		if (typeId == Constant.TaxInvoiceType.SALE || typeId == Constant.TaxInvoiceType.AR)
			criteria.add(Restrictions.in("typeId", taxInvoiceType));
		else
		criteria.add(Restrictions.not(Restrictions.in("typeId", taxInvoiceType)));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.eq("netTransactionAmount", netTransactionAmount));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else
			return (TaxInvoice) result.get(0);
	}

	public List getTaxInvoiceList(String storeId, Date transactionDate, char typeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		criteria.add(Restrictions.eq("issueDate", transactionDate));
		if (typeId == Constant.TaxInvoiceType.SALE || typeId == Constant.TaxInvoiceType.AR)
			criteria.add(Restrictions.in("typeId", taxInvoiceType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", taxInvoiceType)));
		criteria.addOrder(Order.asc("taxInvoiceId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getTaxInvoiceList(String storeId, Date transactionDate, char typeId, char salesTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		criteria.add(Restrictions.eq("issueDate", transactionDate));
		if (typeId == Constant.TaxInvoiceType.SALE || typeId == Constant.TaxInvoiceType.AR)
			criteria.add(Restrictions.in("typeId", taxInvoiceType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", taxInvoiceType)));

		char pos = Constant.SalesTransactionType.POS;
		char so = Constant.SalesTransactionType.SO;
		char ar = Constant.SalesTransactionType.AR;
		// if salesTransTypeId equals some type set X to another type for not select
		switch (salesTypeId) {
			case Constant.SalesTransactionType.POS :
				so = 'X';
				ar = 'X';
				break;
			case Constant.SalesTransactionType.SO :
				pos = 'X';
				break;
		}
		criteria.add(Restrictions.in("salesTransactionType", new Character[]{new Character(pos), new Character(so), new Character(ar)}));

		criteria.addOrder(Order.asc("taxInvoiceId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getTaxInvoiceList(String storeId, Date fromDate, Date toDate, char typeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		criteria.add(Restrictions.ge("issueDate", fromDate));
		criteria.add(Restrictions.le("issueDate", toDate));
		if (typeId == Constant.TaxInvoiceType.SALE || typeId == Constant.TaxInvoiceType.AR)
			criteria.add(Restrictions.in("typeId", taxInvoiceType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", taxInvoiceType)));

		criteria.addOrder(Order.asc("taxInvoiceId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void update(TaxInvoice taxInvoice) {
		getHibernateTemplate().update(taxInvoice);
	}

	public BigDecimal totalReturnTransactionAmount(char salesType, String userId, Date trnDate) {
		String queryString = "select sum(inv.netTransactionAmount) from " + "TaxInvoice inv where " + "inv.typeId = 'C' and " + "inv.salesTransactionType like :salesType and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";
		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"salesType", "userId", "trnDate"}, new Object[]{new Character(salesType), userId, trnDate});

		if (list.size() != 0) {
			BigDecimal object = (BigDecimal) list.get(0);
			if (object != null)
				return object;
			else
				return new BigDecimal("0.00");
		}

		return null;
	}
	public BigDecimal totalReturnTransactionAmountSO_Pos(String userId, Date trnDate) {
		String queryString = "select sum(inv.netTransactionAmount) from " + "TaxInvoice inv where " + "inv.typeId = 'C' and " + " ((inv.salesTransactionType = 'P' ) OR (inv.salesTransactionType = 'S')) and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";
		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"userId", "trnDate"}, new Object[]{userId, trnDate});

		if (list.size() != 0) {
			BigDecimal object = (BigDecimal) list.get(0);
			if (object != null)
				return object;
			else
				return new BigDecimal("0.00");
		}

		return null;
	}
	public int totalReturnTransaction(char salesType, String userId, Date trnDate) {
		String queryString = "select count(*) from " + "TaxInvoice inv where " + "inv.typeId = 'C' and " + "inv.salesTransactionType like :salesType and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"salesType", "userId", "trnDate"}, new Object[]{new Character(salesType), userId, trnDate});

		if (list.size() != 0) {
			return ((Integer) list.get(0)).intValue();
		}

		return 0;
	}
	public int totalReturnTransactionSO_Pos(String userId, Date trnDate) {
		String queryString = "select count(*) from " + "TaxInvoice inv where " + "inv.typeId = 'C' and " + " ((inv.salesTransactionType = 'P' ) OR (inv.salesTransactionType = 'S')) and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"userId", "trnDate"}, new Object[]{userId, trnDate});

		if (list.size() != 0) {
			return ((Integer) list.get(0)).intValue();
		}

		return 0;
	}
	public BigDecimal totalReturnTransactionItem(char salesType, String userId, Date trnDate) {
		String queryString = "select sum(item.quantity) from " + "TaxInvoice inv join " + "inv.taxInvoiceItems item where " + "inv.typeId = 'C' and " + "inv.salesTransactionType like :salesType and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"salesType", "userId", "trnDate"}, new Object[]{new Character(salesType), userId, trnDate});

		if (list.size() != 0) {
			BigDecimal object = (BigDecimal) list.get(0);
			if (object != null)
				return object;
			else
				return new BigDecimal("0.00");
		}

		return null;
	}

	public BigDecimal totalReturnTransactionItemSO_Pos(String userId, Date trnDate) {
		String queryString = "select sum(item.quantity) from " + "TaxInvoice inv join " + "inv.taxInvoiceItems item where " + "inv.typeId = 'C' and " + " ((inv.salesTransactionType = 'P' ) OR (inv.salesTransactionType = 'S')) and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"userId", "trnDate"}, new Object[]{userId, trnDate});

		if (list.size() != 0) {
			BigDecimal object = (BigDecimal) list.get(0);
			if (object != null)
				return object;
			else
				return new BigDecimal("0.00");
		}

		return null;
	}
	public TaxInvoice getTaxInvoice(String storeId, String taxInvoiceId, char typeId, Date issueDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		criteria.add(Restrictions.eq("taxInvoiceId", taxInvoiceId));
		if (typeId == Constant.TaxInvoiceType.SALE || typeId == Constant.TaxInvoiceType.AR)
			criteria.add(Restrictions.in("typeId", taxInvoiceType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", taxInvoiceType)));
		if(issueDate != null){
			criteria.add(Restrictions.eq("issueDate", issueDate));
		}
		
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 1)
			return (TaxInvoice) result.get(0);
		else
			return null;
	}
	
	public TaxInvoice getFullTaxInvoice(String storeId, String taxInvoiceId, char typeId, Date tranDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		criteria.add(Restrictions.eq("taxInvoiceId", taxInvoiceId));
		if (typeId == Constant.TaxInvoiceType.SALE || typeId == Constant.TaxInvoiceType.AR)
			criteria.add(Restrictions.in("typeId", taxInvoiceType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", taxInvoiceType)));
		criteria.add(Restrictions.eq("transactionDate", tranDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 1)
			return (TaxInvoice) result.get(0);
		else
			return null;
	}

	public List getTaxInvoices(String storeId, char typeId, char returnStatus, char salesTransactionType, String tenderId, Date fromDate, Date toDate, String createUserId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.ge("issueDate", fromDate));
		criteria.add(Restrictions.le("issueDate", toDate));

		if (Constant.TaxInvoiceType.ALL != typeId) {
			criteria.add(Restrictions.eq("typeId", new Character(typeId)));
		}
		// else if (Constant.TaxInvoiceType.ALL == typeId) {
		// Character[] c = new Character[]{new Character('M'), new Character('N'), new Character('R'), new Character('S')};
		// criteria.add(Restrictions.in("typeId", c));
		// }

		if (Constant.ReturnStatus.ALL != returnStatus) {
			criteria.add(Restrictions.eq("returnStatus", new Character(returnStatus)));
		}

		if (createUserId != null && !"".equals(createUserId.trim()))
			criteria.add(Restrictions.eq("createUserId", createUserId));

		if (tenderId != null && !"".equals(tenderId.trim())) {
//			DetachedCriteria invCri = criteria.createCriteria("invoiceTenders");
//			DetachedCriteria tenderCri = invCri.createCriteria("tender");
//			tenderCri.add(Restrictions.eq("tenderId", tenderId));
		}

//		char pos = Constant.SalesTransactionType.POS;
//		char so = Constant.SalesTransactionType.SO;   
//		switch (salesTransactionType) {
//			case Constant.SalesTransactionType.POS :
//				so = 'X';
//				break;
//			case Constant.SalesTransactionType.SO :
//				pos = 'X';
//				break;
//		}
		
		if (Constant.SalesTransactionType.ALL != salesTransactionType && Constant.SalesTransactionType.MIX_SALES  != salesTransactionType) {
			criteria.add(Restrictions.eq("salesTransactionType", new Character(salesTransactionType)));
		}

		criteria.addOrder(Order.asc("issueDate"));
		criteria.addOrder(Order.asc("taxInvoiceId"));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if(list!=null && list.size()>0 && tenderId!=null && !tenderId.trim().equals("")){
			for(int i=0;i<list.size();i++){
				TaxInvoice taxinvoice = (TaxInvoice)list.get(i);
				if(taxinvoice.getInvoiceTenders()!=null){
					int index = 0;
					InvoiceTender tender;
					do {
						tender = (InvoiceTender)taxinvoice.getInvoiceTenders().get(index);
						if(tender!=null && tender.getTender()!=null && !tender.getTender().getTenderId().trim().equals(tenderId)){
							taxinvoice.getInvoiceTenders().remove(index);
						}else{
							index++;
						}
					} while ( index < taxinvoice.getInvoiceTenders().size());
					
				}
				
			}
		}
		
		
		return list;
	}
	public TaxInvoice getTaxInvoice(String taxInvoiceId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("taxInvoiceId", taxInvoiceId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 1)
			return (TaxInvoice) result.get(0);
		else
			return null;
	}
	
	public List getTaxInvoiceByTicketNo(String storeId, String ticketNo, String posId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	public List getTaxInvoiceByCN(String storeId, char typeId, String ticketNo, String posId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.not(Restrictions.in("typeId", taxInvoiceType)));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public TaxInvoice getTaxInvoiceByBillNo(String storeId, String sapBillingNo) 
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.eq("SapbillingNo", sapBillingNo));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0)
			return null;
		else
			return (TaxInvoice) result.get(0);
	}

	public List getTaxInvoiceByBillNo(String sapBillingNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.createAlias("store", "store");
		//criteria.add(Restrictions.eq("store.storeId", storeId));
		
		criteria.add(Restrictions.eq("billingNo", sapBillingNo));
		
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return null;
	}

	public TaxInvoice getTaxInvoiceBySapBillNo(String storeId,String sapBillNo) 
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.createAlias("salesTransaction", "salesTransaction");
		criteria.createAlias("store", "store");
		
		criteria.add(Restrictions.eq("salesTransaction.sapBillNo", sapBillNo));
		criteria.add(Restrictions.eq("store.storeId", storeId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return null;
	}

	public TaxCode getTaxCodeByTaxCodeId(String taxCodeId) {
		return (TaxCode)getHibernateTemplate().get(TaxCode.class, taxCodeId);
	}    

	public PriceConditionType getPriceCondTyp(String priceConditionTypeId) {
		return (PriceConditionType)getHibernateTemplate().get(PriceConditionType.class, priceConditionTypeId);
	}

	public TransactionStatus getTranSts(String transactionStatusId) {
		return (TransactionStatus)getHibernateTemplate().get(TransactionStatus.class, transactionStatusId);
	}

	public List getTaxInvoiceListAllCN(String storeId, Date transactionDate, String typeId ,String posId , String[] ticketNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));
		
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.in("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("typeId", typeId));
		
		criteria.addOrder(Order.asc("createDateTime"));
		
		List result  = getHibernateTemplate().findByCriteria(criteria);
		if(result != null)
			return result;
		else
			return null;
	}
	
	public List getTaxInvoiceListAllCN(String storeId, Date transactionDate, String typeId ,String posId , String[] ticketNo,String refundNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		
		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));
		
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.in("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("typeId", typeId));
		criteria.add(Restrictions.eq("refundId", refundNo));
		
		criteria.addOrder(Order.asc("createDateTime"));
		
		List result  = getHibernateTemplate().findByCriteria(criteria);
		if(result != null)
			return result;
		else
			return null;
	}
	
	public List getTaxInvResubmitToSap(String storeId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		List result = new ArrayList();
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));   
		criteria.add(Restrictions.eq("issueDate", transactionDate));
		criteria.add(Restrictions.isNull("sapCNBillNo"));
		criteria.add(Restrictions.isNotNull("billType"));
		criteria.addOrder(Order.asc("taxInvoiceId"));
		List taxList = getHibernateTemplate().findByCriteria(criteria);
		if(taxList != null){
			for(int i=0;i<taxList.size();i++){   
				TaxInvoice tax = (TaxInvoice)taxList.get(i);
				DetachedCriteria criteriaSales = DetachedCriteria.forClass(SalesTransaction.class);
				criteriaSales.createAlias("store", "store");  
				criteriaSales.add(Restrictions.eq("store.storeId", storeId));
				criteriaSales.add(Restrictions.eq("ticketNo", tax.getTicketNo()));
				criteriaSales.add(Restrictions.eq("posId", tax.getPosId()));
				criteriaSales.add(Restrictions.eq("transactionDate",tax.getTransactionDate()));
				List salesList = getHibernateTemplate().findByCriteria(criteriaSales);
				if(salesList.size()>0){
					SalesTransaction salesTran = (SalesTransaction)salesList.get(0);
					tax.setSales(salesTran);
					result.add(tax);
				}      
			}
			return result;
			   
		}else{
			return null;
		}
	}
	              
	public List getTaxInvResubmitToSap(String storeId, Date fromDate,Date toDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		List result = new ArrayList();
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));   
		criteria.add(Restrictions.between("issueDate", fromDate, toDate));
		criteria.add(Restrictions.isNull("sapCNBillNo"));
		criteria.add(Restrictions.isNotNull("billType"));
		criteria.addOrder(Order.asc("taxInvoiceId"));
		List taxList = getHibernateTemplate().findByCriteria(criteria);
		if(taxList != null){
			for(int i=0;i<taxList.size();i++){   
				TaxInvoice tax = (TaxInvoice)taxList.get(i);
				DetachedCriteria criteriaSales = DetachedCriteria.forClass(SalesTransaction.class);
				criteriaSales.createAlias("store", "store");  
				criteriaSales.add(Restrictions.eq("store.storeId", storeId));
				criteriaSales.add(Restrictions.eq("ticketNo", tax.getTicketNo()));
				criteriaSales.add(Restrictions.eq("posId", tax.getPosId()));
				criteriaSales.add(Restrictions.eq("transactionDate",tax.getTransactionDate()));
				List salesList = getHibernateTemplate().findByCriteria(criteriaSales);
				if(salesList.size()>0){
					SalesTransaction salesTran = (SalesTransaction)salesList.get(0);
					tax.setSales(salesTran);
					result.add(tax);
				}      
			}
			return result;
			   
		}else{
			return null;
		}
	}
	
	
	public TaxInvoice getTaxInvoiceByTaxInvoiceId(String storeId, String ticketNo, String posId,String taxInvoiceId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("taxInvoiceId", taxInvoiceId));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 1)
			return (TaxInvoice) result.get(0);
		else
			return null;
	}
	
	public List getTaxInvByTaxInvIdAndIssueDt(String taxInvId, Date issueDt)throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("taxInvoiceId", taxInvId));
		criteria.add(Restrictions.eq("issueDate", issueDt));
		return getHibernateTemplate().findByCriteria(criteria);	
	}
	
	public List getTaxInvoiceByDate(String storeId, Date date) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.eq("issueDate", date));
		criteria.addOrder(Order.asc("taxInvoiceId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getTaxInvoiceByMonth(String storeId, Date month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(month);
		cal.add(Calendar.MONTH, 1);
		Date todate = cal.getTime();
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.between("issueDate", month, cal.getTime()));
		criteria.addOrder(Order.asc("taxInvoiceId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	//Bum Add Check already create data **table tax_inv** 13/05/2013  Line 568 to 583
	public TaxInvoice getTaxInvoiceByStoreIdTicketNoPosIdTranDate(String storeId, String ticketNo, String posId,Date tranDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", tranDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() > 0)
			return (TaxInvoice) result.get(0);
		else
			return null;
	}	
	
	// Pook add method for check CN can't duplicate 07/02/2014
	public TaxInvoice getTaxInvoiceByStoreForCheckDuplicate(TaxInvoice taxInvoice) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoice.class);
		criteria.add(Restrictions.eq("issueDate", taxInvoice.getIssueDate()));
		criteria.add(Restrictions.eq("typeId", String.valueOf(taxInvoice.getTypeId())));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", taxInvoice.getStore().getStoreId()));
		criteria.add(Restrictions.eq("ticketNo", taxInvoice.getTicketNo()));
		criteria.add(Restrictions.eq("posId", taxInvoice.getPosId()));
		criteria.add(Restrictions.eq("refundId", taxInvoice.getRefundId()));
		criteria.add(Restrictions.eq("transactionDate", taxInvoice.getTransactionDate()));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() > 0)
			return (TaxInvoice) result.get(0);
		else
			return null;
	}	

}
