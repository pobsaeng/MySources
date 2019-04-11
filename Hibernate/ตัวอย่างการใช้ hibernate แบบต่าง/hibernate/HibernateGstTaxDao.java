package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.GstTaxDao;
import com.ie.icon.domain.so.GstTax;

public class HibernateGstTaxDao extends HibernateCommonDao implements GstTaxDao {

	public void create(GstTax gstTax) {
		getHibernateTemplate().save(gstTax);
	}

	public void update(GstTax gstTax) {
		getHibernateTemplate().update(gstTax);
	}

	public void delete(GstTax gstTax) {
		getHibernateTemplate().delete(gstTax);
	}

	public void createOrUpdate(GstTax gstTax) {
		getHibernateTemplate().saveOrUpdate(gstTax);
	}

	public List getGstTax() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstTax.class);
		criteria.addOrder(Order.asc("gstCustTaxClassId")) ;
		criteria.addOrder(Order.asc("gstArtcTaxClassId")) ;
		criteria.addOrder(Order.asc("gstSaleTaxClassId")) ;
		criteria.addOrder(Order.asc("gstRateId")) ;
		criteria.addOrder(Order.asc("gstexcludeGstId")) ;
		return getHibernateTemplate().findByCriteria(criteria);
	}


	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstTax.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getGstTaxByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstTax.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

}