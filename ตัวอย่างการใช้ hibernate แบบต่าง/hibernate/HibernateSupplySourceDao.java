package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SupplySourceDao;
import com.ie.icon.domain.mch.SupplySource;

public class HibernateSupplySourceDao extends HibernateCommonDao implements
		SupplySourceDao {

	public void create(SupplySource supplySource) throws DataAccessException {
		getHibernateTemplate().save(supplySource);
	}

	public void update(SupplySource supplySource) throws DataAccessException {
		getHibernateTemplate().update(supplySource);
	}

	public void delete(SupplySource supplySource) throws DataAccessException {
		getHibernateTemplate().delete(supplySource);
	}

	public SupplySource getSupplySource(String supplySourceId)
		throws DataAccessException {
		SupplySource supplySrc = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(SupplySource.class);
        criteria.add(Restrictions.eq("supplySourceId", supplySourceId));
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        if(result!=null && result.size()>0){
        	supplySrc = (SupplySource)result.get(0);
        }
        return supplySrc;
	}

	public void createOrUpdate(SupplySource supplySource) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(supplySource);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SupplySource.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public List getSupplySourceByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SupplySource.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

}
