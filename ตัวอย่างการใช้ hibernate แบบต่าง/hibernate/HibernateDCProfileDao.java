package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.DCProfileDao;
import com.ie.icon.domain.DCProfile;

public class HibernateDCProfileDao extends HibernateCommonDao implements
		DCProfileDao {

	public void create(DCProfile profile) throws DataAccessException {
		getHibernateTemplate().save(profile);
	}

	public void update(DCProfile profile) throws DataAccessException {
		getHibernateTemplate().update(profile);
	}

	public void delete(DCProfile profile) throws DataAccessException {
		getHibernateTemplate().delete(profile);
	}

	public DCProfile getDCProfile(String dcProfileId)
			throws DataAccessException {
		return (DCProfile)getHibernateTemplate().get(DCProfile.class, dcProfileId);
	}

	public void createOrUpdate(DCProfile profile) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(profile);
	}

	public List getDCProfileByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DCProfile.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DCProfile.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

}
