/**
 * 
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.CustomerSurveyDao;
import com.ie.icon.domain.message.CustomerSurveyGroup;
import com.ie.icon.domain.message.CustomerSurveyResult;

/**
 * @author Arnon
 *
 */
public class HibernateCustomerSurveyDao extends HibernateCommonDao implements
		CustomerSurveyDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#create(com.ie.icon.domain.message.CustomerSurveyGroup)
	 */
	public void create(CustomerSurveyGroup grp) throws DataAccessException {
		getHibernateTemplate().save(grp);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#delete(com.ie.icon.domain.message.CustomerSurveyGroup)
	 */
	public void delete(CustomerSurveyGroup grp) throws DataAccessException {
		getHibernateTemplate().delete(grp);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#update(com.ie.icon.domain.message.CustomerSurveyGroup)
	 */
	public void update(CustomerSurveyGroup grp) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(grp);
	}

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerSurveyDao#createOrUpdate(com.ie.icon.domain.message.CustomerSurveyGroup)
     */
    public void createOrUpdate(CustomerSurveyGroup grp)
            throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(grp);
    }
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#CustomerSurveyGroupByUpdDttmGtPubDttm()
	 */
	public List getCustomerSurveyGroupByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerSurveyGroup.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CustomerSurveyDao#getRowByUpdDttmGtPubDttm()
     */
    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(CustomerSurveyGroup.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#getCustomerSurveyGroups()
	 */
	public List getCustomerSurveyGroups() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerSurveyGroup.class);
		
		criteria.addOrder(Order.asc("customerSurveyGroupId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#getLastestCustomerSurvey()
	 */
	public CustomerSurveyGroup getLastestCustomerSurvey() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerSurveyGroup.class);
		
		criteria.add(Restrictions.le("startDate", today()));
		criteria.add(Restrictions.ge("endDate", today()));

		criteria.addOrder(Order.desc("createDateTime"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (CustomerSurveyGroup)result.get(0);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#create(com.ie.icon.domain.message.CustomerSurveyResult)
	 */
	public void create(CustomerSurveyResult result) throws DataAccessException {
		getHibernateTemplate().save(result);
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerSurveyDao#getCustomerSurveyGroup()
	 */
	public CustomerSurveyGroup getCustomerSurveyGroup(long code)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerSurveyGroup.class);
		
		criteria.add(Restrictions.eq("objectId", new Long(code)));
 
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1)
			return null;
		else {
			CustomerSurveyGroup customerSurveyGroup = (CustomerSurveyGroup)result.get(0);
			getHibernateTemplate().initialize(customerSurveyGroup.getCustomerSurveyQuestions());
			return customerSurveyGroup;
		}
	}
	
}
