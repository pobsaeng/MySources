package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.StoreDao;
import com.ie.icon.domain.Store;

public class HibernateStoreDao extends HibernateCommonDao implements StoreDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.StoreDao#create(com.ie.icon.domain.Store)
	 */
	public void create(Store store) throws DataAccessException {
		getHibernateTemplate().save(store);

	}

	public Store getStoreByStoreId(String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Store.class);

		criteria.add(Restrictions.eq("storeId", storeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else
			return (Store) result.get(0);
	}

	public List getStores() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Store.class);
		criteria.addOrder(Order.asc("storeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getStoresExcepItself(String storeId) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(Store.class);
		criteria.add(Restrictions.ne("storeId", storeId));
		criteria.addOrder(Order.asc("storeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void update(Store store) {
		getHibernateTemplate().update(store);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.StoreDao#getStoresByUpdDttmLtPudDttm()
	 */
	public List getStoresByUpdDttmGtPudDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Store.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.StoreDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Store.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public void createOrUpdate(Store store) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(store);
	}

	public List getStores(boolean isStore) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Store.class);

		if (isStore)
			criteria.add(Restrictions.like("storeId", "S", MatchMode.START));
		else
			criteria.add(Restrictions.like("storeId", "XP", MatchMode.START));
		criteria.addOrder(Order.asc("storeId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}
}
