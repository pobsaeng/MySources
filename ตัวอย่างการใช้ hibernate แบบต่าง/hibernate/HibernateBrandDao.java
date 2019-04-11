package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.BrandDao;
import com.ie.icon.domain.Brand;

public class HibernateBrandDao extends HibernateCommonDao implements BrandDao {

	public void create(Brand brand) throws DataAccessException {
		getHibernateTemplate().save(brand);
	}

	public void delete(Brand brand) throws DataAccessException {
		getHibernateTemplate().delete(brand);
	}

	public void update(Brand brand) throws DataAccessException {
		getHibernateTemplate().update(brand);
	}

	public void createOrUpdate(Brand brand) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(brand);
	}

	public Brand getBrand(String brandId) throws DataAccessException {
		return (Brand) getHibernateTemplate().get(Brand.class, brandId);
	}

	public List getBrands() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Brand.class);
		criteria.addOrder(Order.asc("name"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getBrands(String brandName) throws DataAccessException {
		brandName = replace(brandName);
		DetachedCriteria criteria = DetachedCriteria.forClass(Brand.class);
		criteria.add(Restrictions.like("name", brandName, MatchMode.START).ignoreCase());
		criteria.addOrder(Order.asc("name"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.BrandDao#getBrandsByUpdDttmGtPubDttm()
	 */
	public List getBrandsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Brand.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Brand.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}
}
