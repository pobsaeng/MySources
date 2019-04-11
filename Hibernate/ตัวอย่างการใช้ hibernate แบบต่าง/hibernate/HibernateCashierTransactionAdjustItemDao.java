package com.ie.icon.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.CashierTransactionAdjustItemDao;
import com.ie.icon.domain.cashier.CashierTransactionAdjustItem;

public class HibernateCashierTransactionAdjustItemDao extends HibernateCommonDao implements CashierTransactionAdjustItemDao {

	public void create(CashierTransactionAdjustItem cshTranAdjItem) throws DataAccessException {
		getHibernateTemplate().save(cshTranAdjItem);
	}

	public void update(CashierTransactionAdjustItem cshTranAdjItem) throws DataAccessException {
		getHibernateTemplate().update(cshTranAdjItem);
	}

	public void delete(CashierTransactionAdjustItem cshTranAdjItem) throws DataAccessException {
		getHibernateTemplate().delete(cshTranAdjItem);
	}

	public void createOrUpdate(CashierTransactionAdjustItem cshTranAdjItem) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(cshTranAdjItem);
	}

	public List getCashierTransactionAdjustItems(String storeId, Date fromDate, Date toDate) throws DataAccessException {
		Calendar c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		c.add(Calendar.MILLISECOND, -1);
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjustItem.class);
		criteria.createAlias("cashierTransactionAdjust", "cashierTransactionAdjust");
		criteria.add(Restrictions.eq("cashierTransactionAdjust.storeId", storeId));
		criteria.add(Restrictions.in("cashierTransactionAdjust.stsId", new String[]{"S", "C"}));
		criteria.add(Restrictions.between("cashierTransactionAdjust.transactionDate", fromDate, c.getTime()));
		criteria.addOrder(Order.asc("cashierTransactionAdjust.transactionDate"));
		criteria.addOrder(Order.asc("tenderId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getCashierTransactionAdjustItemsByType(String storeId, Date fromDate, Date toDate, String type) throws DataAccessException {
		Calendar c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		c.add(Calendar.MILLISECOND, -1);
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionAdjustItem.class);
		criteria.createAlias("cashierTransactionAdjust", "cashierTransactionAdjust");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			criteria.add(Restrictions.eq("cashierTransactionAdjust.storeId", storeId)); 
		}
		criteria.add(Restrictions.in("cashierTransactionAdjust.stsId", new String[]{"S"}));
		criteria.add(Restrictions.between("cashierTransactionAdjust.transactionDate", fromDate, c.getTime()));
		
		criteria.createAlias("cashierAdjustType", "cashierAdjustType");
		criteria.add(Restrictions.eq("cashierAdjustType.cashierAdjTypId", type));
		
		criteria.addOrder(Order.asc("cashierTransactionAdjust.transactionDate"));
		criteria.addOrder(Order.asc("cashierTransactionAdjust.storeId"));
		criteria.addOrder(Order.asc("tenderId"));
		criteria.addOrder(Order.asc("creditTenderId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

}
