package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.GstCustTaxClassDao;
import com.ie.icon.domain.so.GstCustTaxClass;

public class HibernateGstCustTaxClassDao extends HibernateCommonDao implements GstCustTaxClassDao {

	public void create(GstCustTaxClass gstCustTaxClass) {
		getHibernateTemplate().save(gstCustTaxClass);
	}

	public void update(GstCustTaxClass gstCustTaxClass) {
		getHibernateTemplate().update(gstCustTaxClass);
	}

	public void delete(GstCustTaxClass gstCustTaxClass) {
		getHibernateTemplate().delete(gstCustTaxClass);
	}

	public void createOrUpdate(GstCustTaxClass gstCustTaxClass) {
		getHibernateTemplate().saveOrUpdate(gstCustTaxClass);
	}

	public List getGstCustTaxClass() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstCustTaxClass.class);
		criteria.addOrder(Order.asc("custTaxId")) ;
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstCustTaxClass.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getGstCustTaxClassByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstCustTaxClass.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

}