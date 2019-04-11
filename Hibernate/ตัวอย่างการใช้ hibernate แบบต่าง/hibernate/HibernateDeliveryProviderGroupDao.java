package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.DeliveryProviderGroupDao;
import com.ie.icon.domain.so.DeliveryProviderGroup;

public class HibernateDeliveryProviderGroupDao extends HibernateCommonDao implements DeliveryProviderGroupDao{

	public List getDeliveryProviderGroup(String shipping) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeliveryProviderGroup.class);
        criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("supplySource"), "supplySource")));
		criteria.createAlias("shippingPoint", "shippingPoint");
		criteria.add(Restrictions.eq("shippingPoint.shippingPointId",shipping));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	public List getDeliveryProviderGroups(String shipping,String supplysource) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeliveryProviderGroup.class);
		criteria.createAlias("shippingPoint", "shippingPoint");
		criteria.add(Restrictions.eq("shippingPoint.shippingPointId",shipping));
		criteria.createAlias("supplySource", "supplySource");
		criteria.add(Restrictions.eq("supplySource.supplySourceId",supplysource));
		criteria.addOrder(Order.asc("supplySource.supplySourceId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	public List getDistinctSupplySource(String shipping) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeliveryProviderGroup.class);	
		criteria.createAlias("supplySource", "supplySource");
        criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("supplySource.supplySourceId"), "supplySource.supplySourceId")));
		criteria.createAlias("shippingPoint", "shippingPoint");
		criteria.add(Restrictions.eq("shippingPoint.shippingPointId",shipping));
		criteria.addOrder(Order.asc("supplySource.supplySourceId"));
		List result = getHibernateTemplate().findByCriteria(criteria);	
		return result;
	}
}
