package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.MCHDao;
import com.ie.icon.domain.mch.MCH;

public class HibernateMCHDao extends HibernateCommonDao implements MCHDao {

	public void create(MCH mch) throws DataAccessException {
		getHibernateTemplate().save(mch);
	}

	public void update(MCH mch) throws DataAccessException {
		getHibernateTemplate().update(mch);
	}

	public void delete(MCH mch) throws DataAccessException {
		getHibernateTemplate().delete(mch);
	}

    public void createOrUpdate(MCH mch) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(mch);
    }
    
	public MCH getMCH(String mchId) throws DataAccessException {
		return (MCH)getHibernateTemplate().get(MCH.class, mchId);
	}

	public MCH getMCH(String mchId, int level) throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(MCH.class);
        
        criteria.add(Restrictions.eq("mchId", mchId));
        criteria.add(Restrictions.eq("level", new Integer(level)));
        
        List ret = getHibernateTemplate().findByCriteria(criteria);
        
        if ( ret.size() != 1 )
        	return null;
        else
        	return (MCH)ret.get(0);
	}
	
	public List getMCHsByLevel(int level) throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(MCH.class);
        
        criteria.add(Restrictions.eq("level", new Integer(level)));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}

    /* (non-Javadoc)
     * @see com.ie.icon.dao.MCHDao#getMCHsByUpdDttmGtPubDttm()
     */
    public List getMCHsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(MCH.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.addOrder(Order.desc("level"));
        criteria.addOrder(Order.asc("lastUpdateDate"));
        
        return getHibernateTemplate().findByCriteria(criteria, first, max);
    }
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.MCHDao#getRowByUpdDttmGtPubDttm()
     */
    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(MCH.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
}
