package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.UserLimitProfileDao;
import com.ie.icon.domain.authorization.LimitType;
import com.ie.icon.domain.authorization.UserLimitProfile;


public class HibernateUserLimitProfileDao extends HibernateCommonDao implements
        UserLimitProfileDao {

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserLimitProfileDao#create(com.ie.icon.domain.authorization.UserLimitProfile)
     */
    public void create(UserLimitProfile profile) {
        getHibernateTemplate().save(profile);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserLimitProfileDao#update(com.ie.icon.domain.authorization.UserLimitProfile)
     */
    public void update(UserLimitProfile profile) {
        getHibernateTemplate().update(profile);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserLimitProfileDao#getUserLimitProfiles()
     */
    public List getUserLimitProfiles() {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserLimitProfile.class);
        criteria.addOrder(Order.asc("code"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserLimitProfileDao#getLimitTypes()
     */
    public List getLimitTypes() {
        DetachedCriteria criteria = DetachedCriteria.forClass(LimitType.class);
        criteria.addOrder(Order.asc("objectId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public UserLimitProfile getUserLimitProfileByCode(String profileId) {
    	 DetachedCriteria criteria = DetachedCriteria.forClass(UserLimitProfile.class);
         
         criteria.add(Restrictions.eq("code", profileId));
         
         List list = getHibernateTemplate().findByCriteria(criteria);
         
         if ( list.size() == 1 ){
         	UserLimitProfile userLimitProfile = (UserLimitProfile)list.get(0);
			getHibernateTemplate().initialize(userLimitProfile.getProfileLimits());
            return (UserLimitProfile)list.get(0);
         }
         else
             return null;
    }

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.UserLimitProfileDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserLimitProfile.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.UserLimitProfileDao#getUserLimitProfileByUpdDttmGtPubDttm(int, int)
	 */
	public List getUserLimitProfileByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserLimitProfile.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setFetchMode("profileLimits", FetchMode.JOIN);
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.UserLimitProfileDao#createOrUpdate(com.ie.icon.domain.authorization.UserLimitProfile)
	 */
	public void createOrUpdate(UserLimitProfile userLimitProfile)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(userLimitProfile);

	}
}
