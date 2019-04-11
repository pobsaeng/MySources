package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.SpecialCharPeriodDao;
import com.ie.icon.domain.Brand;
import com.ie.icon.domain.promotion.SmsPrmtn;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.SalesTransactionPartner;
import com.ie.icon.domain.sale.SpecialCharPeriod;
import com.ie.icon.domain.sale.SpecialCharPeriodItem;
import com.ie.icon.domain.sale.SpecialCharPeriodItemId;

public class HibernateSpecialCharPeriodDao extends HibernateCommonDao implements
		SpecialCharPeriodDao {
	
	public SpecialCharPeriodItem getSpecialCharPeriodItemById(SpecialCharPeriodItemId id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SpecialCharPeriodItem.class);
		criteria.add(Restrictions.eq("id.spcCharPeriodOid", new Long(id.getSpcCharPeriodOid())));
		criteria.add(Restrictions.eq("id.storeId", id.getStoreId()));
		
		List ret = getHibernateTemplate().findByCriteria(criteria);

		if (ret == null || ret.size()==0)
			return null;
		else {
			return (SpecialCharPeriodItem) ret.get(0);
		}
	}
	
	public List getSpecialCharPeriodItemNotInArray(String spcCharPeriodOid,
			String[] storeid) {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(SpecialCharPeriodItem.class);
		criteria.add(Restrictions.eq("id.spcCharPeriodOid", spcCharPeriodOid));
		criteria.add(Restrictions.not(Restrictions.in("id.storeId", storeid)));

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			return result;
		}
	}

	public List getActiveSpecialCharPeriod(long spcCharPeriodOid) {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(SpecialCharPeriod.class);
		criteria.add(Restrictions.eq("spcCharPeriodOid",new Long(spcCharPeriodOid)));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			return result;
		}
	}
	
	public List getActivePeriod( final Date frmDate, final Date toDate){

		DetachedCriteria criteria = DetachedCriteria.forClass(SpecialCharPeriod.class);

		criteria.add(Expression.ge("endDt",frmDate));
		criteria.add(Expression.le("startDt",toDate));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size()==0)
			return null;
		else
		{
			for (int i = 0; i < result.size(); i++) {
				SpecialCharPeriod sp = (SpecialCharPeriod)result.get(i);
				getHibernateTemplate().initialize(sp.getSpcCharPeriodItems());
			}
			return result;
		}
	}
	
	public SpecialCharPeriod getSpecialCharPeriodInRange(String spchrId, Date dateRange) throws Exception {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(SpecialCharPeriod.class);
		criteria.add(Restrictions.eq("spcCharId", spchrId));
		criteria.add(Restrictions.le("startDt", dateRange));
		criteria.add(Restrictions.ge("endDt", dateRange));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			return (SpecialCharPeriod) result.get(0);
		}
	}
	
	public List getSpecialCharPeriodInRange(String spchrId, Date frmDate,Date toDate) {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(SpecialCharPeriod.class);
		criteria.add(Restrictions.eq("spcCharId", spchrId));
		criteria.add(Restrictions.le("startDt", frmDate));
		criteria.add(Restrictions.ge("endDt", toDate));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			return result;
		}
	}
	
	public SpecialCharPeriod getSpecialCharPeriodInRangeLikeMch(String spchrId, Date dateRange) {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(SpecialCharPeriod.class);
		criteria.add(Restrictions.like("spcCharId", spchrId, MatchMode.START));
		criteria.add(Restrictions.le("startDt", dateRange));
		criteria.add(Restrictions.ge("endDt", dateRange));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			return (SpecialCharPeriod) result.get(0);
		}
	}
	
	public SpecialCharPeriod getSpecialCharPeriod(String oid, Date frmDate, Date toDate) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(SpecialCharPeriod.class);
		criteria.add(Restrictions.eq("spcCharPeriodOid", oid));
		criteria.add(Restrictions.eq("startDt", frmDate));
		criteria.add(Restrictions.eq("endDt", toDate));

		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			return (SpecialCharPeriod) result.get(0);
		}
	}
	
	public void createSpecialCharPeriod(List spList){
		
		Iterator iterator = spList.iterator();
		while(iterator.hasNext()){
			SpecialCharPeriod sp = (SpecialCharPeriod)iterator.next();
			getHibernateTemplate().save(sp);
		}
	}

	public void createSpecialCharPeriod(SpecialCharPeriod sp) {
		getHibernateTemplate().save(sp);
	}

	public void createSpecialCharPeriodItem(SpecialCharPeriodItem item) {
		getHibernateTemplate().save(item);
	}

	public void updateSpecialCharPeriod(SpecialCharPeriod sp) {
		getHibernateTemplate().update(sp);
	}
	
	public void updateSpecialCharPeriodItem(SpecialCharPeriodItem item) {
		getHibernateTemplate().update(item);
	}

	public void createOrUpdateSpecialCharPeriodItem(SpecialCharPeriodItem item) {
		getHibernateTemplate().saveOrUpdate(item);
	}
	
	public void createOrUpdate(SpecialCharPeriod sp) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(sp);
	}
	
	public List getSpecialCharPeriodByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SpecialCharPeriod.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdDttm", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		
		List result = getHibernateTemplate().findByCriteria(criteria,first, max);
		if(result.size()==0)
			return null;
		else
		{
			for (int i = 0; i < result.size(); i++) {
				SpecialCharPeriod spc = (SpecialCharPeriod)result.get(i);
				getHibernateTemplate().initialize(spc.getSpcCharPeriodItems());
			}
			return result;
		}
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SpecialCharPeriod.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdDttm", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

}