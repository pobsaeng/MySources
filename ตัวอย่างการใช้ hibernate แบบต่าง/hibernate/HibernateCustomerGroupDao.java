/*
 * Created on Sep 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.CustomerGroupDao;
import com.ie.icon.domain.customer.CustomerGroup;

/**
 * @author visawee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateCustomerGroupDao extends HibernateCommonDao implements
		CustomerGroupDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerGroupDao#getAllCustomerGroups()
	 */
	public List getAllCustomerGroups() {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerGroup.class);
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerGroupDao#getCustomerGroupByID()
	 */
	public CustomerGroup getCustomerGroupByID(String customerGroupID) {
		// TODO Auto-generated method stub
		try {
			Logger.getLogger(" ").log(Level.WARNING,"customerGroup ID="+customerGroupID);
			DetachedCriteria criteria = DetachedCriteria.forClass(CustomerGroup.class);

			criteria.add(Restrictions.eq("customerGroupId",customerGroupID));

			List customerGroupList = getHibernateTemplate().findByCriteria(criteria);
			
			if (customerGroupList.size() == 1) {
				return (CustomerGroup) customerGroupList.get(0);
			} else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerGroupDao#createOrUpdate(com.ie.icon.domain.customer.CustomerGroup)
	 */
	public void createOrUpdate(CustomerGroup customerGroup)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(customerGroup);

	}
	public List getCustomerGroupByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerGroup.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerGroup.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(CustomerGroup customerGroup) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(customerGroup);
		
	}
	
	public List getCustomerGroupByCustGroupId(String customerGroupId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerGroup.class);
		
		criteria.add(Restrictions.eq("customerGroupId",customerGroupId));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
}
