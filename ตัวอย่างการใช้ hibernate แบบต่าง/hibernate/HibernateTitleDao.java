/*
 * Created on Sep 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.TitleDao;
import com.ie.icon.domain.Title;

/**
 * @author visawee
 *
 */
public class HibernateTitleDao extends HibernateCommonDao implements TitleDao {

	public void create(Title title) {
		getHibernateTemplate().save(title);
	}

	public void delete(Title title) {
		getHibernateTemplate().delete(title);
	}

	public void update(Title title) {
		getHibernateTemplate().update(title);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TitleDao#getAllTitles()
	 */
	public List getAllTitles() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TitleDao#getTitleByID(java.lang.String)
	 */
	public Title getTitleByID(String titleID) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);

			criteria.add(Restrictions.eq("titleId",titleID));

			List titleList = getHibernateTemplate().findByCriteria(criteria);
			
			if (titleList.size() == 1) {
				return (Title) titleList.get(0);
			} else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public Title getTitleByName(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);
		
		criteria.add(Restrictions.eq("name", name));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (Title)result.get(0);
	}

	public void createOrUpdate(Title title) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(title);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public List getTitleByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public List getTitle(String titleType){
		DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);
		criteria.add(Restrictions.eq("type", titleType));
		criteria.addOrder(Order.asc("titleId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getCurrentTitle(String titleID){
		DetachedCriteria criteria = DetachedCriteria.forClass(Title.class);
		criteria.add(Restrictions.eq("titleId", titleID));
		return getHibernateTemplate().findByCriteria(criteria);
	}
}
