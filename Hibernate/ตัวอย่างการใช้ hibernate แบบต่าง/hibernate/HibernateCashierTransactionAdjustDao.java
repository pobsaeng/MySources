package com.ie.icon.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.CashierTransactionAdjustDao;
import com.ie.icon.domain.cashier.CashierTransactionAdjust;
import com.ie.icon.domain.promotion.SmsTrn;

public class HibernateCashierTransactionAdjustDao extends HibernateCommonDao implements CashierTransactionAdjustDao {

	public void create(CashierTransactionAdjust cshTranAdj) throws DataAccessException {
		System.out.println("--- HibernateCashierTransactionAdjustDao.create() : " + cshTranAdj.getObjectId());
		getHibernateTemplate().saveOrUpdate(cshTranAdj);
		System.out.println("--- Finish : saveOrUpdate(cshTranAdj) ---");
	}

	public void update(CashierTransactionAdjust cshTranAdj) throws DataAccessException {
		getHibernateTemplate().update(cshTranAdj);
	}

	public void createOrUpdate(CashierTransactionAdjust cshTranAdj) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(cshTranAdj);
	}

	public List getCashierTranAdjust(String storeId, Date fromDate, Date toDate, boolean flagSearchRange) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjust.class);
		if (flagSearchRange) {
			criteria.add(Restrictions.between("transactionDate", fromDate, toDate));
		} else {
			criteria.add(Restrictions.eq("transactionDate", fromDate));
		}
		if (storeId != null && !"".equals(storeId)) {
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		criteria.addOrder(Order.asc("transactionDate"));
		criteria.addOrder(Order.desc("jvno"));
		return getHibernateTemplate().findByCriteria(criteria);

	}
	
	public List getCashierTranAdjustNotCancel(String storeId, Date trnDate) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjust.class);
			criteria.add(Restrictions.eq("transactionDate", trnDate));
			
		if (storeId != null && !"".equals(storeId)) {
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		criteria.add(Restrictions.ne("stsId", "C")); 
		criteria.addOrder(Order.asc("transactionDate"));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		
		if(resultList!=null && !resultList.isEmpty()){
			for(int i = 0 ; i<resultList.size() ; i++){
				CashierTransactionAdjust thisCashTranAdj = (CashierTransactionAdjust)resultList.get(i);
				getHibernateTemplate().initialize(thisCashTranAdj.getCashierTransactionAdjustItems());
			}
		}
		
		return resultList;

	}

	public List getCashierTransactionAdjusts(String storeId, Date fromDate, Date toDate) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		c.add(Calendar.MILLISECOND, -1);
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjust.class);
		
		System.out.println("storeId :: "+storeId);
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			System.out.println(" START Condition storeId :: "+storeId);
			criteria.add(Restrictions.eq("storeId", storeId));
			System.out.println(" END Condition storeId :: "+storeId);
		}		
		criteria.add(Restrictions.in("stsId", new String[]{"S", "C"}));
		criteria.add(Restrictions.between("transactionDate", fromDate, c.getTime()));
		criteria.addOrder(Order.asc("transactionDate"));
		criteria.addOrder(Order.asc("storeId"));
		criteria.addOrder(Order.asc("documentNo"));
		
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);	
		
		System.out.println(" resultList : " + resultList);
		if(resultList!=null && !resultList.isEmpty()){
			System.out.println(" resultList.SIZE : " + resultList.size());
			for(int i = 0 ; i<resultList.size() ; i++){
				CashierTransactionAdjust thisCashTranAdj = (CashierTransactionAdjust)resultList.get(i);
				getHibernateTemplate().initialize(thisCashTranAdj.getCashierTransactionAdjustItems());
			}
		}
		
		return resultList;
	}
	
	public String getMaxJvNo(String storeId,Date transactionDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjust.class);
		String result = null;
		if (storeId != null && !"".equals(storeId)) {
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		if (transactionDate != null && !transactionDate.equals("")) {
			criteria.add(Restrictions.eq("transactionDate", transactionDate));   
		}      
		criteria.addOrder(Order.desc("jvno"));
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		
		if (resultList != null && resultList.size() > 0) {    
			CashierTransactionAdjust cash = (CashierTransactionAdjust) resultList.get(0);
			result = cash.getJvno();
		}
		return result;
		
	}
	
	// Pook add method for boizz use to resend monitor 09/01/2014
	public CashierTransactionAdjust getCashierTransactionAdjust(String documentNo, Date transDate) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjust.class);
		CashierTransactionAdjust thisCashTranAdj = null;
		System.out.println("documentNo :: "+documentNo);
		criteria.add(Restrictions.eq("documentNo", documentNo));
		criteria.add(Restrictions.eq("transactionDate", transDate));
		
			
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if(resultList==null || resultList.size()==0){
			return null;
		} else {
			thisCashTranAdj = (CashierTransactionAdjust) resultList.get(0);
			System.out.println(" cashTrnAdjust : " + thisCashTranAdj);
			if(thisCashTranAdj!=null){
				getHibernateTemplate().initialize(thisCashTranAdj.getCashierTransactionAdjustItems());
			}
		}
		
		
		return thisCashTranAdj;
	}	

	//...Boizz(+) : Get & Delete CashierTransactionAdjust @ 09-Jan-2014
	public CashierTransactionAdjust getCashierTransactionAdjust(String storeId, Date trnDate, String documentNo) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjust.class);
		criteria.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.eq("transactionDate", trnDate));
		criteria.add(Restrictions.eq("documentNo", documentNo));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList != null && resultList.size() > 0) {   
			CashierTransactionAdjust cashTranAdj = (CashierTransactionAdjust) resultList.get(0);
			return cashTranAdj;
		}
		
		return null;
	}
	
	public void delete(CashierTransactionAdjust cashierTransactionAdjust) throws Exception {
		getHibernateTemplate().delete(cashierTransactionAdjust);
	}
	//...End(+)
}
