package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.POSAccessProfileDao;
import com.ie.icon.domain.authorization.POSAccessProfile;
import com.ie.icon.domain.authorization.POSAccessProfileMenuItem;

public class HibernatePOSAccessProfileDao extends HibernateCommonDao implements POSAccessProfileDao{

	public List getPOSAccessProfiles() {
		DetachedCriteria criteria = DetachedCriteria.forClass(POSAccessProfile.class);
        criteria.addOrder(Order.asc("objectId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}

	public void create(POSAccessProfile posAccessProfile) {
		getHibernateTemplate().save(posAccessProfile);

	}

	public POSAccessProfile getPOSAccessProfileByCode(String code) {
		DetachedCriteria criteria = DetachedCriteria.forClass(POSAccessProfile.class);
		
		criteria.add(Restrictions.eq("code", code));
 
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1)
			return null;
		else {
			POSAccessProfile posAccessProfile = (POSAccessProfile)result.get(0);
			getHibernateTemplate().initialize(posAccessProfile.getMenuItems());
			return posAccessProfile;
		}
	}

	public void update(POSAccessProfile posAccessProfile) {
		getHibernateTemplate().update(posAccessProfile);

	}
	
	public List getPOSAccessProfileMenuItems(long profileOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(POSAccessProfileMenuItem.class);
		
		criteria.createAlias("posAccessProfile", "posAccessProfile");
		criteria.add(Restrictions.eq("posAccessProfile.objectId", new Long(profileOid)));
		
		criteria.addOrder(Order.asc("sequenceNo"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.POSAccessProfileDao#getPOSAccessProfileByUpdDttmGtPubDttm(int, int)
	 */
	public List getPOSAccessProfileByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(POSAccessProfile.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setFetchMode("menuItems", FetchMode.JOIN);
        criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
        List result = getHibernateTemplate().findByCriteria(criteria, first, max);
    	
		return result;
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.POSAccessProfileDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(POSAccessProfile.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
		
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.POSAccessProfileDao#deletePOSAccessProfileMenuItem(com.ie.icon.domain.authorization.POSAccessProfileMenuItem)
	 */
	public void deletePOSAccessProfileMenuItem(
			POSAccessProfileMenuItem posAccessProfileMenuItem) {
		getHibernateTemplate().delete(posAccessProfileMenuItem);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.POSAccessProfileDao#createOrUpdate(com.ie.icon.domain.authorization.POSAccessProfile)
	 */
	public void createOrUpdate(POSAccessProfile posAccessProfile)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(posAccessProfile);

	}

	public void deletePOSAccessProfileMenuItem(final POSAccessProfile posAccessProfile) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "delete from POSAccessProfileMenuItem " +
						" where posAccessProfile = ?" ;
				Query query = session.createQuery(queryString);
				query.setLong(0, posAccessProfile.getObjectId());
				
//				 int rowCount = query.executeUpdate();
				session.close();
				return null;
			}
		});
	}
}
