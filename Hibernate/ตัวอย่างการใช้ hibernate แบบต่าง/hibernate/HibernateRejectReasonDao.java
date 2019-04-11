/*
 * Created on Sep 22, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.RejectReasonDao;
import com.ie.icon.domain.so.RejectReason;

/**
 * @author yo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateRejectReasonDao extends HibernateCommonDao implements RejectReasonDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.RejectReasonDao#getRejectReason()
	 */
	public List getRejectReason() {
		DetachedCriteria criteria = DetachedCriteria.forClass(RejectReason.class);
		
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        if ( result.size() > 0 )
            return result;
        else
            return null;
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.RejectReasonDao#getRejectReasonById(java.lang.String)
	 */
	public RejectReason getRejectReasonById(String id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RejectReason.class);
		
		criteria.add( Restrictions.eq( "rejectReasonId", id) );
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        if ( result.size() > 0 )
            return null;
        else
            return (RejectReason)result.get(0);
	}

}
