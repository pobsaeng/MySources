package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SiteGroupDao;
import com.ie.icon.domain.so.SiteGroup;

public class HibernateSiteGroupDao extends HibernateCommonDao implements
		SiteGroupDao {

	public void create(SiteGroup siteGroup)
			throws DataAccessException {  
		getHibernateTemplate().save(siteGroup);
	}  

	public void update(SiteGroup siteGroup)
			throws DataAccessException {
		getHibernateTemplate().update(siteGroup);
	}

	public void delete(SiteGroup siteGroup)
			throws DataAccessException {
		getHibernateTemplate().delete(siteGroup);
	}

	public SiteGroup getSiteGroup(String siteGroupId)
			throws DataAccessException {
		return (SiteGroup)getHibernateTemplate().get(SiteGroup.class, siteGroupId);
	}

	public void createOrUpdate(SiteGroup siteGroup) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(siteGroup);
	}

	public List getSiteGroups() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SiteGroup.class);
        criteria.addOrder(Order.asc("siteGroupId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}

}
