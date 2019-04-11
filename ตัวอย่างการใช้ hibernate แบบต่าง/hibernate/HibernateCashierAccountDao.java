package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.CashierAccountDao;
import com.ie.icon.domain.cashier.CashierAccount;

public class HibernateCashierAccountDao extends HibernateCommonDao implements CashierAccountDao {
	public CashierAccount getCashierAccount(String cashierAccId) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAccount.class);
		criteria.add(Restrictions.eq("cashierAccId", cashierAccId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() == 1)
			return (CashierAccount) list.get(0);
		return null;

	}
	
	public List getCashierAccounts() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAccount.class);
		criteria.addOrder(Order.asc("cashierAccId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public void create(CashierAccount cashierAccount) throws DataAccessException {
		getHibernateTemplate().save(cashierAccount);
	}
	
	public void update(CashierAccount cashierAccount) throws DataAccessException {
		getHibernateTemplate().update(cashierAccount);
	}
	
	public List getSearchCashierAccounts(String cashierAccId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAccount.class);
		criteria.add(Restrictions.eq("cashierAccId", cashierAccId));

		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getCashierAccountDesc() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAccount.class);
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("cashierAccId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void createOrUpdate(CashierAccount cashierAccount) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(cashierAccount);
		
	}

	public List getCashierAccountByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAccount.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdDttm", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAccount.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdDttm", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}
	
}
