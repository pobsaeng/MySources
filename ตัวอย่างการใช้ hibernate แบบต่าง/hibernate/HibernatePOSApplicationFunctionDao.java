/*
 * Created on Oct 30, 2006
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

import com.ie.icon.dao.POSApplicationFunctionDao;
import com.ie.icon.domain.authorization.POSApplicationFunction;

/**
 * @author amaritk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernatePOSApplicationFunctionDao extends HibernateCommonDao implements POSApplicationFunctionDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.POSApplicationFunctionDao#getPOSApplicationFunctions()
	 */
	public List getPOSApplicationFunctions() {
		DetachedCriteria criteria = DetachedCriteria.forClass(POSApplicationFunction.class);
        criteria.addOrder(Order.asc("code"));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPOSApplicationFuncByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(POSApplicationFunction.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(POSApplicationFunction.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(POSApplicationFunction posApplicationFunction) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(posApplicationFunction);
	}

	public void createOrUpdate(POSApplicationFunction posApplicationFunction) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdate(posApplicationFunction);
	}

}
