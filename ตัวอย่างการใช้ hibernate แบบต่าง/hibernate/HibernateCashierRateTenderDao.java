package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.CashierRateTenderDao;
import com.ie.icon.domain.Tender;
import com.ie.icon.domain.cashier.CashierRateTender;
import com.ie.icon.domain.cashier.CashierTransaction;
import com.ie.icon.domain.cashier.CashierTransactionType;

public class HibernateCashierRateTenderDao extends HibernateCommonDao implements CashierRateTenderDao{

	public List getCashierRateTender(String tenderId, Date fromDate, Date toDate , String rateType) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierRateTender.class);
		if(!tenderId.equals("All"))
		{
			criteria.add(Restrictions.eq("tenderId", tenderId));
		}
		criteria.add(Restrictions.ge("rateFrom", fromDate));
		criteria.add(Restrictions.le("rateTo", toDate));
		criteria.add(Restrictions.eq("rateType", rateType));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		criteria.addOrder(Order.asc("tenderId"));
		criteria.addOrder(Order.asc("ratePayment"));
		criteria.addOrder(Order.desc("lastUpdateDate"));
		
		
		List temp = getHibernateTemplate().findByCriteria(criteria);
		if(temp != null)
			return temp;
		else
			return null;
	}

	public List getCshRateTender(Date transactionDate, String ratePayment) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierRateTender.class);
		
		criteria.add(Restrictions.le("rateFrom", transactionDate));
		criteria.add(Restrictions.ge("rateTo", transactionDate));
		criteria.add(Restrictions.eq("ratePayment", ratePayment));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		List temp = getHibernateTemplate().findByCriteria(criteria);
		if(temp != null)
		{
			return temp;
		}
		else
		{
			return null;
		}
	}

	public void create(CashierRateTender cashierRateTender) throws DataAccessException {
		getHibernateTemplate().save(cashierRateTender);
	}
	
	public List getCshRateTender(String rateType, String ratePayment,String tenderId) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierRateTender.class);
		if(rateType!=null && !rateType.trim().equals("")){
			criteria.add(Restrictions.eq("rateType", rateType));
		}
		if(ratePayment!=null && !ratePayment.trim().equals("")){
			criteria.add(Restrictions.eq("ratePayment", ratePayment));
		}
		if(tenderId!=null && !tenderId.trim().equals("") && !tenderId.equals("X")){
			criteria.add(Restrictions.eq("tenderId", tenderId));
		}	
		criteria.addOrder(Order.asc("tenderId"));
		criteria.addOrder(Order.desc("createDateTime"));
		
		//List listCshRate = getHibernateTemplate().findByCriteria(criteria);
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	//CashierRateTender cshTender
	public void updateCshTender(CashierRateTender cshTender)throws Exception{
		getHibernateTemplate().update(cshTender);
	}
	
	public List getCshRateTenderByTenderId(Date transactionDate,String ratePayment, String tenderId, String numMonth) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierRateTender.class);
		criteria.add(Restrictions.le("rateFrom", transactionDate));
		criteria.add(Restrictions.ge("rateTo", transactionDate));
		criteria.add(Restrictions.ge("rateType", "S"));
		criteria.add(Restrictions.eq("ratePayment", ratePayment));
		criteria.add(Restrictions.eq("tenderId", tenderId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		if(numMonth!=null && !numMonth.equals("")){
			criteria.add(Restrictions.eq("numMonth", Integer.valueOf(numMonth)));
		}	
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public BigDecimal getSumClosingTrn(Tender tenderId, Date trnDt)throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransaction.class);
		criteria.add(Restrictions.eq("tender", tenderId));
		criteria.add(Restrictions.eq("transactionDate", trnDt));
		CashierTransactionType type = new CashierTransactionType();
		type.setCashierTransactionTypeId("CLOSE");
		criteria.add(Restrictions.eq("cashierType", type));
		List cshTrnList = getHibernateTemplate().findByCriteria(criteria);
		BigDecimal sum = new BigDecimal("0.00");
		if(cshTrnList!=null && cshTrnList.size()>0){
			for(int i =0 ; i<cshTrnList.size() ; i++){
				CashierTransaction thisTrn = (CashierTransaction)cshTrnList.get(i);
				if(thisTrn.getTransactionAmount()!=null){
					sum = sum.add(thisTrn.getTransactionAmount().multiply(new BigDecimal("-1.00")));
				}
				 
			}
			return sum;
		}
		else{
			return new BigDecimal("0.00");
		}
		
	}
	
}
