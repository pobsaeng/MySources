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

import com.ie.icon.dao.CountryDao;
import com.ie.icon.domain.Country;

/**
 * @author visawee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateCountryDao extends HibernateCommonDao implements CountryDao {

	public void create(Country country) {
		getHibernateTemplate().save(country);
	}

	public void delete(Country country) {
		getHibernateTemplate().delete(country);
	}

	public void update(Country country) {
		getHibernateTemplate().update(country);
	}

	public Country getCountryByName(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Country.class);
		
		criteria.add(Restrictions.eq("name", name));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (Country)result.get(0);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CountryDao#getAllCountries()
	 */
	public List getAllCountries() {
		DetachedCriteria criteria1 = DetachedCriteria.forClass(Country.class);
		criteria1.add(Restrictions.eq("name", "TH"));
		criteria1.addOrder(Order.asc("name"));
		
		List list1 = getHibernateTemplate().findByCriteria(criteria1);
		
		DetachedCriteria criteria2 = DetachedCriteria.forClass(Country.class);
		criteria2.add(Restrictions.ne("name", "TH"));
		criteria2.addOrder(Order.asc("name"));
		
		List list2 = getHibernateTemplate().findByCriteria(criteria2);
		
		for(int i = 0 ; i<list2.size() ; i++){
			list1.add(list2.get(i));
		}
		return list1;		
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CountryDao#getCountryByID(java.lang.String)
	 */
	public Country getCountryByID(String countryID) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Country.class);

			criteria.add(Restrictions.eq("countryId",countryID));

			List countryList = getHibernateTemplate().findByCriteria(criteria);
			
			if (countryList.size() == 1) {
				return (Country) countryList.get(0);
			} else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CountryDao#createOrUpdate(com.ie.icon.domain.Country)
	 */
	public void createOrUpdate(Country country) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(country);

	}
	public List getCountryByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Country.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(Country.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
}
