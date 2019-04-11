package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.CashierTransactionAdjustExcludeDao;
import com.ie.icon.domain.cashier.CashierTransactionAdjustExclude;

public class HibernateCashierTransactionAdjustExcludeDao extends HibernateCommonDao implements CashierTransactionAdjustExcludeDao {

	public void create(CashierTransactionAdjustExclude cshTranAdjEx) throws DataAccessException {
		getHibernateTemplate().save(cshTranAdjEx);
	}

	public void update(CashierTransactionAdjustExclude cshTranAdjEx) throws DataAccessException {
		getHibernateTemplate().update(cshTranAdjEx);
	}

	public List getCashierAdjEx(String storeId, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjustExclude.class);
		if(storeId != null && !"".equals(storeId))
		{
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		
		if(trnDate != null && !"".equals(trnDate))
		{
			criteria.add(Restrictions.eq("transactionDate", trnDate));
		}
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("cshTrnTypeId"));	
		criteria.addOrder(Order.asc("tenderId"));		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getCashierAdjustExclude(String storeId, Date fromDate, Date toDate, String cshType) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjustExclude.class);
		if(storeId != null && !"".equals(storeId))
		{
			criteria.add(Restrictions.eq("storeId", storeId));
		}
		
		if(fromDate != null && toDate != null)
		{
			criteria.add(Restrictions.between("transactionDate", fromDate, toDate));
		}
		
		if(cshType != null && !"".equals(cshType))
		{
			criteria.add(Restrictions.eq("cshTrnTypeId", cshType));
		}
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("transactionDate"));
		criteria.addOrder(Order.asc("cshTrnTypeId"));
		criteria.addOrder(Order.asc("tenderId"));
		criteria.addOrder(Order.desc("lastUpdDttm"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void deleteCshTrnAdjEx(CashierTransactionAdjustExclude cshTrnAdjEx) throws DataAccessException {
		getHibernateTemplate().delete(cshTrnAdjEx);
	}

	public List getCashierTransactionAdjustsEx(Date transactionDate, String storeId, String cshTrnTypeId, String tenderId) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjustExclude.class);
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.eq("cshTrnTypeId", cshTrnTypeId));
		criteria.add(Restrictions.eq("tenderId", tenderId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public CashierTransactionAdjustExclude getCashierTransactionAdjustsEx(String storeId, Date transactionDate, String cshTrnTypeId, String tenderId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjustExclude.class);
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.eq("cshTrnTypeId", cshTrnTypeId));
		criteria.add(Restrictions.eq("tenderId", tenderId));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList != null && !resultList.isEmpty()) {
			return (CashierTransactionAdjustExclude)resultList.get(0);
		}
		
		return null;
	}
}
