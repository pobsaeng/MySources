package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.RefundDao;
import com.ie.icon.domain.TaxCode;
import com.ie.icon.domain.sale.ReasonUseRefund;
import com.ie.icon.domain.sale.RefundTender;
import com.ie.icon.domain.sale.PriceConditionType;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.Refund;
import com.ie.icon.domain.sale.TransactionStatus;

public class HibernateRefundDao extends HibernateCommonDao implements RefundDao {
	private Character refundType[] = new Character[]{new Character(Constant.RefundType.SALE), new Character(Constant.RefundType.AR)};

	public void create(Refund refund) {
		getHibernateTemplate().save(refund);
	}
	
	public List getRefundTenderByRefundOidAndTenderId(Long refundOid,String tenderId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RefundTender.class);
		criteria.add(Restrictions.eq("refund.objectId",refundOid));
		criteria.add(Restrictions.eq("tender.tenderId", tenderId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
//		if (result== null ||result.size() == 0)
//			return null;
//		else
//			return (RefundTender) result.get(0);
		
	}
 
	public List getRefundCN(Date issueDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("typeId",new Character(Constant.RefundType.CREDIT_NOTE)));
		criteria.add(Restrictions.eq("issueDate", issueDate));
		return getHibernateTemplate().findByCriteria(criteria);
		
	}
	
	public List getRefundFullTax(Date issueDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("typeId",new Character(Constant.RefundType.S)));
		criteria.add(Restrictions.eq("issueDate", issueDate));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public Refund getRefundByTicketNo(String storeId, char typeId, String ticketNo, String posId, Date transactionDate,BigDecimal netTransactionAmount) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		if (typeId == Constant.RefundType.SALE || typeId == Constant.RefundType.AR)
			criteria.add(Restrictions.in("typeId", refundType));
		else
		criteria.add(Restrictions.not(Restrictions.in("typeId", refundType)));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.eq("netTransactionAmount", netTransactionAmount));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result== null ||result.size() == 0)
			return null;
		else
			return (Refund) result.get(0);
	}

	public List getRefundList(String storeId, Date transactionDate, char typeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		criteria.add(Restrictions.eq("issueDate", transactionDate));
		if (typeId == Constant.RefundType.SALE || typeId == Constant.RefundType.AR)
			criteria.add(Restrictions.in("typeId", refundType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", refundType)));
		criteria.addOrder(Order.asc("refundId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getRefundList(String storeId, Date transactionDate, char typeId, char salesTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		criteria.add(Restrictions.eq("issueDate", transactionDate));
		if (typeId == Constant.RefundType.SALE || typeId == Constant.RefundType.AR)
			criteria.add(Restrictions.in("typeId", refundType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", refundType)));

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

		criteria.addOrder(Order.asc("refundId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getRefundList(String storeId, Date fromDate, Date toDate, char typeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);

		DetachedCriteria storeCri = criteria.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));

		criteria.add(Restrictions.ge("issueDate", fromDate));
		criteria.add(Restrictions.le("issueDate", toDate));
		if (typeId == Constant.RefundType.SALE || typeId == Constant.RefundType.AR)
			criteria.add(Restrictions.in("typeId", refundType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", refundType)));

		criteria.addOrder(Order.asc("refundId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void update(Refund refund) {
		getHibernateTemplate().update(refund);
	}

	public BigDecimal totalReturnTransactionAmount(char salesType, String userId, Date trnDate) {
		String queryString = "select sum(inv.netTransactionAmount) from " + "Refund inv where " + "inv.typeId = 'C' and " + "inv.salesTransactionType like :salesType and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";
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
		String queryString = "select sum(inv.netTransactionAmount) from " + "Refund inv where " + "inv.typeId = 'C' and " + " ((inv.salesTransactionType = 'P' ) OR (inv.salesTransactionType = 'S')) and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";
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
		String queryString = "select count(*) from " + "Refund inv where " + "inv.typeId = 'C' and " + "inv.salesTransactionType like :salesType and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"salesType", "userId", "trnDate"}, new Object[]{new Character(salesType), userId, trnDate});

		if (list.size() != 0) {
			return ((Integer) list.get(0)).intValue();
		}

		return 0;
	}
	public int totalReturnTransactionSO_Pos(String userId, Date trnDate) {
		String queryString = "select count(*) from " + "Refund inv where " + "inv.typeId = 'C' and " + " ((inv.salesTransactionType = 'P' ) OR (inv.salesTransactionType = 'S')) and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

		List list = getHibernateTemplate().findByNamedParam(queryString, new String[]{"userId", "trnDate"}, new Object[]{userId, trnDate});

		if (list.size() != 0) {
			return ((Integer) list.get(0)).intValue();
		}

		return 0;
	}
	public BigDecimal totalReturnTransactionItem(char salesType, String userId, Date trnDate) {
		String queryString = "select sum(item.quantity) from " + "Refund inv join " + "inv.refundItems item where " + "inv.typeId = 'C' and " + "inv.salesTransactionType like :salesType and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

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
		String queryString = "select sum(item.quantity) from " + "Refund inv join " + "inv.refundItems item where " + "inv.typeId = 'C' and " + " ((inv.salesTransactionType = 'P' ) OR (inv.salesTransactionType = 'S')) and " + "inv.createUserId like :userId and " + "inv.issueDate = :trnDate";

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
	public Refund getRefund(String storeId, String refundId, char typeId, Date issueDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);

		criteria.add(Restrictions.eq("refundId", refundId));
		if (typeId == Constant.RefundType.SALE || typeId == Constant.RefundType.AR)
			criteria.add(Restrictions.in("typeId", refundType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", refundType)));
		if(issueDate != null){
			criteria.add(Restrictions.eq("issueDate", issueDate));
		}
		
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 1)
			return (Refund) result.get(0);
		else
			return null;
	}
	
	public Refund getFullRefund(String storeId, String refundId, char typeId, Date tranDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);

		criteria.add(Restrictions.eq("refundId", refundId));
		if (typeId == Constant.RefundType.SALE || typeId == Constant.RefundType.AR)
			criteria.add(Restrictions.in("typeId", refundType));
		else
			criteria.add(Restrictions.not(Restrictions.in("typeId", refundType)));
		criteria.add(Restrictions.eq("transactionDate", tranDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 1)
			return (Refund) result.get(0);
		else
			return null;
	}

	public List getRefunds(String storeId, char typeId, char returnStatus, char salesTransactionType, String tenderId, Date fromDate, Date toDate, String createUserId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.ge("issueDate", fromDate));
		criteria.add(Restrictions.le("issueDate", toDate));

		if (Constant.RefundType.ALL != typeId) {
			criteria.add(Restrictions.eq("typeId", new Character(typeId)));
		}
		// else if (Constant.RefundType.ALL == typeId) {
		// Character[] c = new Character[]{new Character('M'), new Character('N'), new Character('R'), new Character('S')};
		// criteria.add(Restrictions.in("typeId", c));
		// }

		if (Constant.ReturnStatus.ALL != returnStatus) {
			criteria.add(Restrictions.eq("returnStatus", new Character(returnStatus)));
		}

		if (createUserId != null && !"".equals(createUserId.trim()))
			criteria.add(Restrictions.eq("createUserId", createUserId));

		if (tenderId != null && !"".equals(tenderId.trim())) {
//			DetachedCriteria invCri = criteria.createCriteria("refundTenders");
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
		criteria.addOrder(Order.asc("refundId"));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if(list!=null && list.size()>0 && tenderId!=null && !tenderId.trim().equals("")){
			for(int i=0;i<list.size();i++){
				Refund taxrefund = (Refund)list.get(i);
				if(taxrefund.getRefundTenders()!=null){
					int index = 0;
					RefundTender tender;
					do {
						tender = (RefundTender)taxrefund.getRefundTenders().get(index);
						if(tender!=null && tender.getTender()!=null && !tender.getTender().getTenderId().trim().equals(tenderId)){
							taxrefund.getRefundTenders().remove(index);
						}else{
							index++;
						}
					} while ( index < taxrefund.getRefundTenders().size());
					
				}
				
			}
		}
		
		
		return list;
	}
	public Refund getRefund(String refundId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("refundId", refundId));
		criteria.add(Restrictions.ne("returnStatus",new Character(Constant.RefundStatus.CANCEL)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 1)
			return (Refund) result.get(0);
		else
			return null;
	}
	
	public List getRefundByTicketNo(String storeId, String ticketNo, String posId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.ne("returnStatus",new Character(Constant.RefundStatus.CANCEL)));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	public List getRefundByCN(String storeId, char typeId, String ticketNo, String posId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.not(Restrictions.in("typeId", refundType)));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public Refund getRefundByBillNo(String storeId, String sapBillingNo) 
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.eq("SapbillingNo", sapBillingNo));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0)
			return null;
		else
			return (Refund) result.get(0);
	}

	public List getRefundByBillNo(String sapBillingNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.createAlias("store", "store");
		//criteria.add(Restrictions.eq("store.storeId", storeId));
		
		criteria.add(Restrictions.eq("billingNo", sapBillingNo));
		
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return null;
	}

	public Refund getRefundBySapBillNo(String storeId,String sapBillNo) 
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
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

	public List getRefundListAllCN(String storeId, Date transactionDate, String typeId ,String posId , String[] ticketNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);

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
	
	public List getTaxInvResubmitToSap(String storeId, Date transactionDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		List result = new ArrayList();
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));   
		criteria.add(Restrictions.eq("issueDate", transactionDate));
		criteria.add(Restrictions.isNull("sapCNBillNo"));
		criteria.add(Restrictions.isNotNull("billType"));
		criteria.addOrder(Order.asc("refundId"));
		List taxList = getHibernateTemplate().findByCriteria(criteria);
		if(taxList != null){
			for(int i=0;i<taxList.size();i++){   
				Refund tax = (Refund)taxList.get(i);
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
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		List result = new ArrayList();
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));   
		criteria.add(Restrictions.between("issueDate", fromDate, toDate));
		criteria.add(Restrictions.isNull("sapCNBillNo"));
		criteria.add(Restrictions.isNotNull("billType"));
		criteria.addOrder(Order.asc("refundId"));
		List taxList = getHibernateTemplate().findByCriteria(criteria);
		if(taxList != null){
			for(int i=0;i<taxList.size();i++){   
				Refund tax = (Refund)taxList.get(i);
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
	
	
	public Refund getRefundByRefundId(String storeId, String ticketNo, String posId,String refundId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("refundId", refundId));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 1)
			return (Refund) result.get(0);
		else
			return null;
	}
	public List getRefundByRefundId(String storeId, Date fromIssueDate, Date toIssueDate,String refundId) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		if(fromIssueDate!=null && toIssueDate!=null){
			criteria.add(Restrictions.between("issueDate", fromIssueDate, toIssueDate));
		}else if(fromIssueDate!=null && toIssueDate==null){
			criteria.add(Restrictions.ge("issueDate", fromIssueDate));
		}else if(fromIssueDate==null && toIssueDate!=null){
			criteria.add(Restrictions.le("issueDate", toIssueDate));
		}
	
		if(refundId!=null && !("").equals(refundId)){
			criteria.add(Restrictions.eq("refundId", refundId));
		}
		
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.ne("returnStatus",new Character(Constant.RefundStatus.CANCEL)));
		criteria.addOrder(Order.desc("issueDate"));
		criteria.addOrder(Order.desc("refundId"));
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	public Refund getRefundByRefundOID(long refundOid) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);

		criteria.add(Restrictions.eq("objectId",new Long(refundOid)));

		List refundList = getHibernateTemplate().findByCriteria(criteria);
		if(refundList!=null && !refundList.isEmpty()){
			return (Refund)refundList.get(0);
		}
		return null;
	}
	
	public List getTaxInvByTaxInvIdAndIssueDt(String taxInvId, Date issueDt)throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("refundId", taxInvId));
		criteria.add(Restrictions.eq("issueDate", issueDt));
		return getHibernateTemplate().findByCriteria(criteria);	
	}

	public List getRefund(Map map) throws Exception {
		DetachedCriteria cri = DetachedCriteria.forClass(Refund.class);
		if(map.get("issueDate_ge")!= null){
			cri.add(Restrictions.ge("issueDate",map.get("issueDate_ge")));
		}		
		if(map.get("issueDate_le")!= null){
			cri.add(Restrictions.le("issueDate",map.get("issueDate_le")));
		}	
		if(map.get("startDate")!= null){
			cri.add(Restrictions.ge("transactionDate",map.get("startDate")));
		}		
		if(map.get("endDate")!= null){
			cri.add(Restrictions.le("transactionDate",map.get("endDate")));
		}		
		if(map.get("refundNo")!= null && !map.get("refundNo").toString().trim().equals("")){
			cri.add(Restrictions.ilike("refundId",replace(map.get("refundNo").toString())));
		}	
		if(map.get("storeId")!= null && !map.get("storeId").toString().trim().equals("")){
			cri.createAlias("store", "store");
			cri.add(Restrictions.eq("store.storeId", map.get("storeId").toString())); 
		}	
		if(map.get("returnStatus_ne")!= null && !map.get("returnStatus_ne").toString().trim().equals("")){
			cri.add(Restrictions.ne("returnStatus",replace(map.get("returnStatus_ne").toString())));
		}	
		if(map.get("returnStatus_ne_1")!= null && !map.get("returnStatus_ne_1").toString().trim().equals("")){
			cri.add(Restrictions.ne("returnStatus",replace(map.get("returnStatus_ne_1").toString())));
		}			

		cri.addOrder(Order.desc("refundId"));
		return getHibernateTemplate().findByCriteria(cri);	
	}
	protected String replace(String param) {
	    if ( param == null )
	        return param;
	    
	    return param.replace('*', '%');
	}

	public Refund getRefundByRefundId(String refundId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		criteria.add(Restrictions.eq("refundId", refundId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result != null && result.size() > 0)
			return (Refund) result.get(0);
		else
			return null;
	}
	
	public List getReasonUseRefundList() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReasonUseRefund.class);

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getRefundUseList(Date fromDate, Date toDate,String storeId, String storeUsed,
			String reasonUse, String refundId, Boolean isZero) {
		DetachedCriteria cri = DetachedCriteria.forClass(Refund.class);
		cri.add(Restrictions.ne("returnStatus",new Character(Constant.RefundStatus.CANCEL)));
		cri.add(Restrictions.eq("isUseRefund", new Boolean(true)));
		cri.add(Restrictions.between("useRefundTransactionDate", fromDate, toDate));
		if(storeUsed.equals("I")){
			cri.createAlias("useRefundStore", "useRefundStore");
			cri.add(Restrictions.eq("useRefundStore.storeId", storeId));
		}else if(storeUsed.equals("E")){
			cri.createAlias("useRefundStore", "useRefundStore");
			cri.add(Restrictions.ne("useRefundStore.storeId", storeId));
		}
		if(!reasonUse.equals("A")){
			cri.createAlias("reasonUseRefundId", "reasonUseRefundId");
			cri.add(Restrictions.eq("reasonUseRefundId.reasonUseRefundId", reasonUse));
		}
		if(refundId!=null && refundId.length()==10){
			cri.add(Restrictions.eq("refundId", refundId.trim()));
		}
		
		cri.addOrder(Order.asc("useRefundTransactionDate"));
		cri.addOrder(Order.asc("refundId"));
		List result = getHibernateTemplate().findByCriteria(cri);
		return result;
	}

	public List getUnusedRefund(Date fromDate, Date toDate, String storeId,
			String refundId) {
		DetachedCriteria cri = DetachedCriteria.forClass(Refund.class);
		cri.add(Restrictions.ne("returnStatus",new Character(Constant.RefundStatus.CANCEL)));
		cri.add(Restrictions.eq("isUseRefund", new Boolean(false)));
		cri.add(Restrictions.between("issueDate", fromDate, toDate));
		if(refundId!=null && refundId.length()==10){
			cri.add(Restrictions.eq("refundId", refundId.trim()));
		}
		cri.add(Restrictions.gt("refundTenderValue", new BigDecimal("0.00")));
		cri.addOrder(Order.asc("issueDate"));
		cri.addOrder(Order.asc("refundId"));
		List result = getHibernateTemplate().findByCriteria(cri);
		return result;
	}
	public List getRefundByRefundId(String storeId, String refundId) {
		System.out.println("<<<< refundId: >>>>"+ refundId);
    	System.out.println("<<<< StoreId >>>>"+ storeId);
    	
		DetachedCriteria criteria = DetachedCriteria.forClass(Refund.class);
		if(refundId!=null && !("").equals(refundId)){
			criteria.add(Restrictions.eq("refundId", refundId));
		}
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		
		criteria.add(Restrictions.ne("returnStatus",new Character(Constant.RefundStatus.CANCEL)));
		criteria.addOrder(Order.desc("issueDate"));
		criteria.addOrder(Order.desc("refundId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		System.out.println("<<< result >>>"+result.size());
		if (result.size() > 0){
			System.out.println("<<< return result >>>");
			return result;
		} else {
			System.out.println("<<< return null >>>");
			return null;
		}
	}
}
