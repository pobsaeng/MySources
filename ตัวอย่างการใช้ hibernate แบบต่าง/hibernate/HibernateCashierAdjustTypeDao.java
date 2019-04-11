package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.CashierAdjustTypeDao;
import com.ie.icon.domain.cashier.CashierAdjustCauseGroup;
import com.ie.icon.domain.cashier.CashierAdjustType;

public class HibernateCashierAdjustTypeDao extends HibernateCommonDao implements CashierAdjustTypeDao {

	public void create(CashierAdjustType obj) throws DataAccessException {
		getHibernateTemplate().save(obj);
	}

	public void update(CashierAdjustType obj) throws DataAccessException {
		getHibernateTemplate().update(obj);
	}

	public void createOrUpdate(CashierAdjustType obj) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public List getAdjustTenders() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustType.class);
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.add(Restrictions.eq("isInterFaceSap", new Boolean(true)));	
		criteria.addOrder(Order.asc("cashierAdjTypId"));
		List cshAdjTyp = getHibernateTemplate().findByCriteria(criteria);
		
		if (cshAdjTyp != null) {
			for (int i = 0; i < cshAdjTyp.size(); i++) {
				CashierAdjustType cashierAdjustType = (CashierAdjustType) cshAdjTyp.get(i);
				getHibernateTemplate().initialize(cashierAdjustType.getCashierAdjustCauseGroup());
				getHibernateTemplate().initialize(cashierAdjustType.getCashierTransactionAdjustItem());
			}
		}
		

		return cshAdjTyp;
	}
	public List getCashierAdjustType() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustType.class);
		criteria.add(Restrictions.eq("isInterFaceSap", new Boolean(true)));
		criteria.addOrder(Order.asc("cashierAdjTypId"));
		//return getHibernateTemplate().findByCriteria(criteria);
		
        List cshAdjTyp = getHibernateTemplate().findByCriteria(criteria);
		
		if (cshAdjTyp != null) {
			for (int i = 0; i < cshAdjTyp.size(); i++) {
				CashierAdjustType cashierAdjustType = (CashierAdjustType) cshAdjTyp.get(i);
				getHibernateTemplate().initialize(cashierAdjustType.getCashierAdjustCauseGroup());
			}
		}
		return cshAdjTyp;
	}
	public List getSearchCashierAdjustType(String cashierAdjustTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustType.class);
		criteria.add(Restrictions.eq("cashierAdjTypId", cashierAdjustTypeId));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getCashierAdjustTypeByCashierAdjustTypeId(String cashierAdjustTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustType.class);
		criteria.add(Restrictions.eq("cashierAdjTypId", cashierAdjustTypeId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.add(Restrictions.eq("isInterFaceSap", new Boolean(true)));	
		criteria.addOrder(Order.asc("cashierAdjTypId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getCashierAdjustTypeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdDttm", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));

		//return getHibernateTemplate().findByCriteria(criteria, first, max);
		
        List cshAdjTyp = getHibernateTemplate().findByCriteria(criteria, first, max);
		
        if (cshAdjTyp != null) {
			for (int i = 0; i < cshAdjTyp.size(); i++) {
				CashierAdjustType cashierAdjustType = (CashierAdjustType) cshAdjTyp.get(i);
				getHibernateTemplate().initialize(cashierAdjustType.getCashierAdjustCauseGroup());
				getHibernateTemplate().initialize(cashierAdjustType.getCashierTransactionAdjustItem());
			}
		}
		return cshAdjTyp;
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdDttm", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}
	
	public List getCashierAdjustCauseGroup() throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustCauseGroup.class);
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("cashierGroupSeq"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if ( result.size() == 0 )
			return null;
		else
			return result;
	}

	public void deleteCashierAdjustTypeCauseGroup(List cashierAdjustTypeCauseGroup)
	{
		/*final Iterator iterator = cashierAdjustTypeCauseGroup.iterator();
		while (iterator.hasNext()) 
		{
			List subcashierAdjustTypeCauseGroup = (List) iterator.next();
			
			final String cashierAdjustTypeId = (String) subcashierAdjustTypeCauseGroup.get(0);
			final String cashierAdjustCauseGroupId = (String) subcashierAdjustTypeCauseGroup.get(1);
			
			Object result = getHibernateTemplate().execute(new HibernateCallback() 
			{
				public Object doInHibernate(Session sess)throws HibernateException, SQLException 
				{
					String sqlFinal = "delete from csh_adj_typ_cause_grp  where csh_adj_typ_id = :cashierAdjustTypeId and csh_adj_cause_grp_id = :cashierAdjustCauseGroupId";
					Query query = sess.createSQLQuery(sqlFinal);
					query.setString("cashierAdjustTypeId", cashierAdjustTypeId);
					query.setString("cashierAdjustCauseGroupId", cashierAdjustCauseGroupId);

					return query.executeUpdate();
				}
			});
		}*/

	}
	
	public List getAllCashierAdjustCauseGroup() throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustCauseGroup.class);
		criteria.addOrder(Order.asc("cashierGroupSeq"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if ( result.size() == 0 )
			return null;
		else
			return result;
	}
	
	public void create(CashierAdjustCauseGroup obj) throws DataAccessException {
		getHibernateTemplate().save(obj);
	}

	public void update(CashierAdjustCauseGroup obj) throws DataAccessException {
		getHibernateTemplate().update(obj);
	}
	
	public void createOrUpdate(CashierAdjustCauseGroup cashierAdjustCauseGroup) throws DataAccessException{
		getHibernateTemplate().saveOrUpdate(cashierAdjustCauseGroup);
	}

	public List getAllCashierAdjustTypeByCashierAdjustTypeId(String cashierAdjTypId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustType.class);
		criteria.add(Restrictions.eq("cashierAdjTypId", cashierAdjTypId));
		criteria.addOrder(Order.asc("cashierAdjTypId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getCashierAdjustCauseGroupID(String CashierAdjustCauseGroupID) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierAdjustCauseGroup.class);
		criteria.add(Restrictions.eq("cashierAdjustCauseGroup", CashierAdjustCauseGroupID));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("cashierGroupSeq"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if ( result.size() == 0 )
			return null;
		else
			return result;
	}
}
