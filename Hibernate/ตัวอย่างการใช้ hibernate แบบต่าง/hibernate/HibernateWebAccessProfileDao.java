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

import com.ie.icon.dao.WebAccessProfileDao;
import com.ie.icon.domain.authorization.WebAccessProfile;
import com.ie.icon.domain.authorization.WebAccessProfileMenuItem;

public class HibernateWebAccessProfileDao extends HibernateCommonDao implements WebAccessProfileDao{
	public List getWebAccessProfiles() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebAccessProfile.class);
        criteria.addOrder(Order.asc("objectId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public WebAccessProfile getWebAccessProfileByCode(String code) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebAccessProfile.class);
		
		criteria.add(Restrictions.eq("code", code));
 
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1)
			return null;
		else {
			WebAccessProfile webAccessProfile = (WebAccessProfile)result.get(0);
			getHibernateTemplate().initialize(webAccessProfile.getMenuItems());
			return webAccessProfile;
		}
	}
	

	public List getWebAccessProfileMenuItems(long profileOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebAccessProfileMenuItem.class);
		
		criteria.createAlias("webAccessProfile", "webAccessProfile");
		criteria.add(Restrictions.eq("webAccessProfile.objectId", new Long(profileOid)));
		criteria.add(Restrictions.eq("type", new Character('I')));
		criteria.addOrder(Order.asc("sequenceNo"));
		return getHibernateTemplate().findByCriteria(criteria);
	}


	public void create(WebAccessProfile webAccessProfile) {
		getHibernateTemplate().save(webAccessProfile);

	}
	
	public void update(WebAccessProfile webAccessProfile) {
		 getHibernateTemplate().update(webAccessProfile);

	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.WebAccessProfileDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebAccessProfile.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.WebAccessProfileDao#getWebAccessProfileByUpdDttmGtPubDttm(int, int)
	 */
	public List getWebAccessProfileByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebAccessProfile.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));        
        criteria.setFetchMode("menuItems", FetchMode.JOIN);
        criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        List result = getHibernateTemplate().findByCriteria(criteria, first, max);
		
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.WebAccessProfileDao#deleteWebAccessProfileMenuItem(com.ie.icon.domain.authorization.WebAccessProfileMenuItem)
	 */
	public void deleteWebAccessProfileMenuItem(
			WebAccessProfileMenuItem webAccessProfileMenuItem) {
		getHibernateTemplate().delete(webAccessProfileMenuItem);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.WebAccessProfileDao#createOrUpdate(com.ie.icon.domain.authorization.WebAccessProfile)
	 */
	public void createOrUpdate(WebAccessProfile webAccessProfile)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(webAccessProfile);

	}

	public void deleteWebAccessProfileMenuItem(final WebAccessProfile webAccessProfile) {
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "delete from WebAccessProfileMenuItem " +
						" where webAccessProfile = ?" ;
				Query query = session.createQuery(queryString);
				query.setLong(0, webAccessProfile.getObjectId());
				
				//int rowCount = query.executeUpdate();
				session.close();
				return null;
			}
		});
	}
}
