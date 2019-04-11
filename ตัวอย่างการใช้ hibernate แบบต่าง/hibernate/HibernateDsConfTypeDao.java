package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.DsConfTypeDao;
import com.ie.icon.domain.so.DsConfType;

public class HibernateDsConfTypeDao extends HibernateCommonDao implements DsConfTypeDao{
	public List getDsConfType(){
		DetachedCriteria criteria = DetachedCriteria.forClass(DsConfType.class);
		criteria.add(Restrictions.eq("isActive","Y"));
		criteria.addOrder(Order.asc("seqNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

	public void create(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().save(dsConfType);
		
	}

	public void createOrUpdate(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsConfType);
		
	}

	public void delete(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().delete(dsConfType);
		
	}

	public void update(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().update(dsConfType);
		
	}
	
	
}
