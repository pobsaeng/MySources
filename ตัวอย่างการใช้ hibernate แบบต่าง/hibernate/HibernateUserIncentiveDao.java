package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.UserIncentiveDao;
import com.ie.icon.domain.so.UserIncentive;

public class HibernateUserIncentiveDao extends HibernateCommonDao implements UserIncentiveDao {

	public void create(UserIncentive userIncentive) {
        getHibernateTemplate().save(userIncentive);
    }
	
    public void update(UserIncentive userIncentive) {
        getHibernateTemplate().update(userIncentive);
    }

    public void delete(UserIncentive userIncentive)  {
        getHibernateTemplate().delete(userIncentive);
    }
    
    public void createOrUpdate(UserIncentive userIncentive) {
		getHibernateTemplate().saveOrUpdate(userIncentive);
	}
    
	public List getUserIncentive(String userIncentiveId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserIncentive.class);
		criteria.add(Restrictions.eq("userIncentiveId", userIncentiveId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getUserIncentive(String userIncentiveId, String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserIncentive.class);
		criteria.add(Restrictions.eq("userIncentiveId", userIncentiveId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.add(Restrictions.eq("storeId",storeId));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getUserIncentiveLike(String userIncentiveId,String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserIncentive.class);
		criteria.add(Restrictions.like("userIncentiveId", "%"+userIncentiveId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.add(Restrictions.eq("storeId",storeId));
		List result =  getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserIncentive.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPubDttm"), 
                Restrictions.isNull("lastPubDttm")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	public List getUserIncentiveByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserIncentive.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPubDttm"), 
                Restrictions.isNull("lastPubDttm")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

}