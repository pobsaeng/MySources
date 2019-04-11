package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.TenderDenominationDao;
import com.ie.icon.domain.TenderDenomination;

public class HibernateTenderDenominationDao extends HibernateCommonDao implements TenderDenominationDao{

	public void create(TenderDenomination tenderDen) {
		getHibernateTemplate().save(tenderDen);

	}

	public List getTenderDenomination(String tenderId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TenderDenomination.class);
		
		DetachedCriteria criTender = criteria.createCriteria( "tender" );
		criTender.add(Restrictions.eq("tenderId", tenderId));
		
		criteria.addOrder( Order.asc( "objectId" ) );
		return getHibernateTemplate().findByCriteria(criteria);
	}
}
