package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.JobProductInstallDao;
import com.ie.icon.domain.so.JobProductInstall;

public class HibernateJobProductInstallDao extends HibernateCommonDao implements JobProductInstallDao{

	public List getJobProductInstallByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(JobProductInstall.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(JobProductInstall.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(JobProductInstall jobProductInstall) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(jobProductInstall);
	}

	public void createOrUpdate(JobProductInstall jobProductInstall) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdate(jobProductInstall);
	}

}
