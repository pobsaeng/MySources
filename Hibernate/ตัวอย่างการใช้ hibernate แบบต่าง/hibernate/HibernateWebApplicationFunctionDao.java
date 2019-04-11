/*
 * Created on Oct 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.WebApplicationFunctionDao;
import com.ie.icon.domain.authorization.WebApplicationFunction;

/**
 * @author amaritk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateWebApplicationFunctionDao extends HibernateCommonDao implements WebApplicationFunctionDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.WebApplicationFunctionDao#getWebApplicationFunctions()
	 */
	public List getWebApplicationFunctions() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebApplicationFunction.class);
        criteria.addOrder(Order.asc("name"));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}
	public List getWebApplicationFuncByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(WebApplicationFunction.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(WebApplicationFunction.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(WebApplicationFunction webApplicationFunction) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(webApplicationFunction);
	}
	public void createOrUpdate(WebApplicationFunction webApplicationFunction) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdate(webApplicationFunction);
	}
}
