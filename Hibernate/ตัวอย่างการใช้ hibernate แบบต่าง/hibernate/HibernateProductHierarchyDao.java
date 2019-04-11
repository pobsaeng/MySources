package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.ProductHierarchyDao;
import com.ie.icon.domain.promotion.ProductHierarchy;

public class HibernateProductHierarchyDao extends HibernateCommonDao implements ProductHierarchyDao {

	public List getProductHierarchyTypes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductHierarchy.class);
		criteria.addOrder(Order.desc("level"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	public ProductHierarchy getProductHierarchy(int level) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductHierarchy.class);
		criteria.add(Restrictions.eq("level", new Integer(level)));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() == 1)
			return (ProductHierarchy) list.get(0);
		return null;
	}
}
