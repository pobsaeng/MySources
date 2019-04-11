/*
 * Created on Sep 15, 2006
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

import com.ie.icon.dao.PartnerFunctionTypeDao;
import com.ie.icon.domain.customer.PartnerFunctionType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author visawee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernatePartnerFunctionTypeDao extends HibernateCommonDao
		implements PartnerFunctionTypeDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PartnerFunctionTypeDao#getAllPartnerFunctionTypes()
	 */
	public List getAllPartnerFunctionTypes() {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(PartnerFunctionType.class);
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PartnerFunctionTypeDao#getPartnerFunctionTypeByID(java.lang.String)
	 */
	public PartnerFunctionType getPartnerFunctionTypeByID(String partnerFunctionTypeID) {
		// TODO Auto-generated method stub
		try {
			Logger.getLogger(" ").log(Level.WARNING,"partnerFunctionType ID="+partnerFunctionTypeID);
			DetachedCriteria criteria = DetachedCriteria.forClass(PartnerFunctionType.class);

			criteria.add(Restrictions.eq("partnerFunctionTypeId",partnerFunctionTypeID));

			List partnerFunctionTypeList = getHibernateTemplate().findByCriteria(criteria);
			
			if (partnerFunctionTypeList.size() == 1) {
				return (PartnerFunctionType) partnerFunctionTypeList.get(0);
			} else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PartnerFunctionType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	public List getPartnerFuncTypByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PartnerFunctionType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public void update(PartnerFunctionType partnerFunctionType) throws DataAccessException {
			getHibernateTemplate().update(partnerFunctionType);	
	}
	public void createOrUpdate(PartnerFunctionType partnerFunctionType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(partnerFunctionType);
	}
}
