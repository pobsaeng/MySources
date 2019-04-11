/**
 * 
 */
package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.CorperateMessageGroupDao;
import com.ie.icon.domain.message.CorperateMessageGroup;

/**
 * @author Arnon
 *
 */
public class HibernateCorperateMessageGroupDao extends HibernateCommonDao
		implements CorperateMessageGroupDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CorperateMessageGroupDao#create(com.ie.icon.domain.message.CorperateMessageGroup)
	 */
	public void create(CorperateMessageGroup msgGroup)
			throws DataAccessException {
		getHibernateTemplate().save(msgGroup);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CorperateMessageGroupDao#delete(com.ie.icon.domain.message.CorperateMessageGroup)
	 */
	public void delete(CorperateMessageGroup msgGroup)
			throws DataAccessException {
		getHibernateTemplate().delete(msgGroup);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CorperateMessageGroupDao#update(com.ie.icon.domain.message.CorperateMessageGroup)
	 */
	public void update(CorperateMessageGroup msgGroup)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(msgGroup);
	}

    public void createOrUpdate(CorperateMessageGroup msgGroup)
            throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(msgGroup);
    }
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CorperateMessageGroupDao#getCorperateMessageGroupsByUpdDttmGtPubDttm()
	 */
	public List getCorperateMessageGroupsByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CorperateMessageGroup.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

    /* (non-Javadoc)
     * @see com.ie.icon.dao.CorperateMessageGroupDao#getRowByUpdDttmGtPubDttm()
     */
    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(CorperateMessageGroup.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
    
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CorperateMessageGroupDao#getCorperateMessageGroups()
	 */
	public List getCorperateMessageGroups() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CorperateMessageGroup.class);
		
		criteria.addOrder(Order.asc("objectId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CorperateMessageGroupDao#getLastestCorperateMessageGroup()
	 */
	public CorperateMessageGroup getLastestCorperateMessageGroup() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CorperateMessageGroup.class);
		
		criteria.add(Restrictions.le("startDate", today()));
		criteria.add(Restrictions.ge("endDate", today()));
		criteria.createAlias("corperateMessages", "corperateMessages");

		criteria.addOrder(Order.desc("createDateTime"));
		criteria.addOrder(Order.asc("corperateMessages.sequenceNo"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (CorperateMessageGroup)result.get(0);
	}

	public void deleteCorperateMessage(final CorperateMessageGroup msgGroup) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "delete from CorperateMessage " +
						" where corperateMessageGroup = ?" ;
				
				Query query = session.createQuery(queryString);
				query.setLong(0, msgGroup.getObjectId());
				
				session.close();
				return null;
			}
		});
	}    
	
}
