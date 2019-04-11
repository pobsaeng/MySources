package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.SubDistrictDao;
import com.ie.icon.domain.SubDistrict;

public class HibernateSubDistrictDao extends HibernateCommonDao implements SubDistrictDao{
	 
	public void create(SubDistrict subDistrict) {
        getHibernateTemplate().save(subDistrict);
    }
	
	public SubDistrict getSubDistrictByDistrictId(String subDistrictId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubDistrict.class);
		
		criteria.add(Restrictions.eq("subDistrictId", subDistrictId));
		criteria.add(Restrictions.eq("status", "Y"));
		criteria.addOrder( Order.asc("name") );
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (SubDistrict)result.get(0);
	}
	
	public List getSubDistricts() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubDistrict.class);
		criteria.add(Restrictions.eq("status", "Y"));
		criteria.addOrder( Order.asc("name") );
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	
	public List getAllSubDistrict(String orderBy){
		DetachedCriteria criteria = DetachedCriteria.forClass(SubDistrict.class);
		criteria.add(Restrictions.eq("status", "Y"));
		criteria.addOrder( Order.asc(orderBy) );
		return getHibernateTemplate().findByCriteria(criteria);
	}

}
