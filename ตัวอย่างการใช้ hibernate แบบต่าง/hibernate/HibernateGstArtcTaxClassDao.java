package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.GstArtcTaxClassDao;
import com.ie.icon.domain.so.GstArtcTaxClass;

public class HibernateGstArtcTaxClassDao extends HibernateCommonDao implements GstArtcTaxClassDao {

	public void create(GstArtcTaxClass gstArtcTaxClass) {
		getHibernateTemplate().save(gstArtcTaxClass);
	}

	public void update(GstArtcTaxClass gstArtcTaxClass) {
		getHibernateTemplate().update(gstArtcTaxClass);
	}

	public void delete(GstArtcTaxClass gstArtcTaxClass) {
		getHibernateTemplate().delete(gstArtcTaxClass);
	}

	public void createOrUpdate(GstArtcTaxClass gstArtcTaxClass) {
		getHibernateTemplate().saveOrUpdate(gstArtcTaxClass);
	}

	public List getGstArtcTaxClass() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstArtcTaxClass.class);
		criteria.addOrder(Order.asc("artcTaxId")) ;
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstArtcTaxClass.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getGstArtcTaxClassByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(GstArtcTaxClass.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

}