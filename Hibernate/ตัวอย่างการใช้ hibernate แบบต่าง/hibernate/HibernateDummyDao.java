package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.DummyDao;
import com.ie.icon.domain.Dummy;

public class HibernateDummyDao extends HibernateCommonDao implements DummyDao {

	public void create(Dummy dummy) throws DataAccessException {
		getHibernateTemplate().save(dummy);
	}

	public void createOrUpdate(Dummy dummy) throws DataAccessException {
		System.out.println("--- HibernateDummyDao.createOrUpdate() @ DummyNo : " +  dummy.getDummyNo());
		getHibernateTemplate().saveOrUpdate(dummy);
	}

	public void update(Dummy dummy) throws DataAccessException {
		getHibernateTemplate().update(dummy);
	}
	
	public void delete(Dummy dummy) throws DataAccessException {
		getHibernateTemplate().delete(dummy);
	}

	public List getDummyByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dummy.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dummy.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public Dummy getDummyByNo(String dummyNo, String dummyType) throws DataAccessException {
		Dummy dummy = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(Dummy.class);
		criteria.add(Restrictions.eq("dummyNo", dummyNo));
		criteria.add(Restrictions.eq("dummyType", dummyType));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.desc("createDateTime"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			dummy = (Dummy)result.get(0);
		}
		return dummy; 
	}
}
