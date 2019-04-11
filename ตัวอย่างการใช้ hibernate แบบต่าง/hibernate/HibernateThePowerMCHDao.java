package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ThePowerMCHDao;
import com.ie.icon.domain.mch.ThePowerMCH;

public class HibernateThePowerMCHDao extends HibernateCommonDao implements ThePowerMCHDao {

	public ThePowerMCH getThePowerMCH(String mchId) throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ThePowerMCH.class);
        
        criteria.add(Restrictions.eq("mchId", mchId));
        
        List ret = getHibernateTemplate().findByCriteria(criteria);
        
        if ( ret.size() != 1 )
        	return null;
        else
        	return (ThePowerMCH)ret.get(0);
	}
	
}
