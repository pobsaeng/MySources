package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.GstSalesTaxClassDao;
import com.ie.icon.domain.so.GstSalesTaxClass;

public class HibernateGstSalesTaxClassDao extends HibernateCommonDao implements GstSalesTaxClassDao {

	public void create(GstSalesTaxClass gstSalesTaxClass) {
		getHibernateTemplate().save(gstSalesTaxClass);
	}

	public void update(GstSalesTaxClass gstSalesTaxClass) {
		getHibernateTemplate().update(gstSalesTaxClass);
	}

	public void delete(GstSalesTaxClass gstSalesTaxClass) {
		getHibernateTemplate().delete(gstSalesTaxClass);
	}

	public void createOrUpdate(GstSalesTaxClass gstSalesTaxClass) {
		getHibernateTemplate().saveOrUpdate(gstSalesTaxClass);
	}

	public List getGstSalesTaxClass() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstSalesTaxClass.class);
		criteria.addOrder(Order.asc("salesTaxId")) ;
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstSalesTaxClass.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getGstSalesTaxClassByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstSalesTaxClass.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

}