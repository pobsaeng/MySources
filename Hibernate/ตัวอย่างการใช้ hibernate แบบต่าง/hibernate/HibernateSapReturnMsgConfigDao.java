package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SapReturnMsgConfigDao;
import com.ie.icon.domain.so.SapReturnMsgConfig;

public class HibernateSapReturnMsgConfigDao  extends HibernateCommonDao implements SapReturnMsgConfigDao{
	public List getSapReturnMsgConfig(String returnType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SapReturnMsgConfig.class);
		criteria.add(Restrictions.eq("returnType", returnType));
		criteria.add(Restrictions.eq("isActive",new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public int getRowSapRetMsgConfigByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SapReturnMsgConfig.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public List getSapRetMsgConfigByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SapReturnMsgConfig.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public void update(SapReturnMsgConfig sapReturnMsgConfig) throws DataAccessException {
		getHibernateTemplate().update(sapReturnMsgConfig);
		
	}

	public void createOrUpdate(SapReturnMsgConfig sapReturnMsgConfig) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(sapReturnMsgConfig);
		
	}
	
}
