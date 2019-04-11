/*
 * Created on Sep 26, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ResidentCodeDao;
import com.ie.icon.domain.ResidentCode;
/**
 * @author visawee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateResidentCodeDao extends HibernateCommonDao implements ResidentCodeDao{

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.ResidentCodeDao#getAllResidentCodes()
	 */
	public List getAllResidentCodes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResidentCode.class);
		return getHibernateTemplate().findByCriteria(criteria);	
	}

	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.ResidentCodeDao#createOrUpdate(com.ie.icon.domain.ResidentCode)
	 */
	public void createOrUpdate(ResidentCode residentCode)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(residentCode);

	}
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ResidentCode.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	public List getResidentCodeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResidentCode.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	public void update(ResidentCode residentCode) throws DataAccessException {
			getHibernateTemplate().update(residentCode);		
	}
}
