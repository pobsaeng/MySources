package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.VendorDao;
import com.ie.icon.domain.Vendor;

public class HibernateVendorDao extends HibernateCommonDao implements VendorDao {

	public void create(Vendor vendor) throws DataAccessException {
		getHibernateTemplate().save(vendor);
	}

	public void update(Vendor vendor) throws DataAccessException {
		getHibernateTemplate().update(vendor);
	}
   
	public Vendor getVendor(String vendorId) throws DataAccessException {
		return (Vendor) getHibernateTemplate().get(Vendor.class, vendorId);
	}

	public List getVendors(String vendorId, String vendorName) throws DataAccessException {
		vendorName = replace(vendorName);
		DetachedCriteria criteria = DetachedCriteria.forClass(Vendor.class);
		if (vendorId != null && !"".equals(vendorId))  
			criteria.add(Restrictions.like("vendorId", vendorId, MatchMode.END));  
		if (vendorName != null && !"".equals(vendorName))
			criteria.add(Restrictions.like("name", vendorName, MatchMode.START).ignoreCase());   
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("name"));

		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getVendor(String vendorId, String vendorName) throws DataAccessException {
		vendorName = replace(vendorName);
		DetachedCriteria criteria = DetachedCriteria.forClass(Vendor.class);
		if (vendorId != null && !"".equals(vendorId))  
			criteria.add(Restrictions.like("vendorId", vendorId, MatchMode.END));  
		if (vendorName != null && !"".equals(vendorName))
			criteria.add(Restrictions.like("name", vendorName, MatchMode.START).ignoreCase());   
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("vendorId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getVendors() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Vendor.class);
		criteria.addOrder(Order.asc("name"));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.VendorDao#getVendorsByUpdDttmGtPubDttm()
	 */
	public List getVendorsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Vendor.class);
		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		//criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.VendorDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Vendor.class);
		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());
		//criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return ((Integer) result.get(0)).intValue();
	}

	public void createOrUpdate(Vendor vendor) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(vendor);
	}
}
