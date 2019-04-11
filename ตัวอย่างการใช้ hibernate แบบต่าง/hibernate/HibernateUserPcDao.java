package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.UserPcDao;
import com.ie.icon.domain.so.UserPc;

public class HibernateUserPcDao extends HibernateCommonDao implements UserPcDao {

	public void create(UserPc UserPc) {
        getHibernateTemplate().save(UserPc);
    }
	
    public void update(UserPc UserPc) {
        getHibernateTemplate().update(UserPc);
    }

    public void delete(UserPc UserPc)  {
        getHibernateTemplate().delete(UserPc);
    }
    
    public void createOrUpdate(UserPc UserPc) {
		getHibernateTemplate().saveOrUpdate(UserPc);
	}
    
	public List getUserPcs(String userPcId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPc.class);
		criteria.add(Restrictions.eq("userPcId", userPcId));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPc.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	public List getUserPcByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPc.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
}
