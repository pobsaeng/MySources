package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.PriceGroupDao;
import com.ie.icon.domain.so.PriceGroup;

public class HibernatePriceGroupDao extends HibernateCommonDao implements PriceGroupDao{

	public List getPriceGroupByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(PriceGroup.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(PriceGroup.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(PriceGroup priceGroup) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(priceGroup);
	}

	public void createOrUpdate(PriceGroup priceGroup) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdate(priceGroup);
	}
	
	public List getPriceGroup(){
		DetachedCriteria criteria = DetachedCriteria.forClass(PriceGroup.class);
		List retult = getHibernateTemplate().findByCriteria(criteria);
		return retult;
	}

}
