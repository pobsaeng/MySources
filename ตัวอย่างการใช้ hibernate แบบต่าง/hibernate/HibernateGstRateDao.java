package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.GstRateDao;
import com.ie.icon.domain.so.GstRate;

public class HibernateGstRateDao extends HibernateCommonDao implements GstRateDao {

	public void create(GstRate gstRate) {
		getHibernateTemplate().save(gstRate);
	}

	public void update(GstRate gstRate) {
		getHibernateTemplate().update(gstRate);
	}

	public void delete(GstRate gstRate) {
		getHibernateTemplate().delete(gstRate);
	}

	public void createOrUpdate(GstRate gstRate) {
		getHibernateTemplate().saveOrUpdate(gstRate);
	}

	public List getGstRate() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstRate.class);
		criteria.addOrder(Order.asc("gstRateId")) ;
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstRate.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getGstRateByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstRate.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

}