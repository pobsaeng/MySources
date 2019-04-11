package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ProfitCenterDao;
import com.ie.icon.domain.so.ProfitCenter;

public class HibernateProfitCenterDao extends HibernateCommonDao implements
		ProfitCenterDao {

	public void create(ProfitCenter profitCenter)
			throws DataAccessException {
		getHibernateTemplate().save(profitCenter);
	}

	public void update(ProfitCenter profitCenter)
			throws DataAccessException {
		getHibernateTemplate().update(profitCenter);
	}

	public void delete(ProfitCenter profitCenter)
			throws DataAccessException {
		getHibernateTemplate().delete(profitCenter);
	}

	public ProfitCenter getProfitCenter(String profitCenterId)
			throws DataAccessException {
		return (ProfitCenter)getHibernateTemplate().get(ProfitCenter.class, profitCenterId);
	}

	public void createOrUpdate(ProfitCenter profitCenter) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(profitCenter);
	}
	
	public List getProfitCenters() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProfitCenter.class);
        criteria.addOrder(Order.asc("description"));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}


}
