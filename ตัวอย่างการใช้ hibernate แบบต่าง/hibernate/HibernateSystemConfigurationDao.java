package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.SystemConfigurationDao;
import com.ie.icon.domain.SystemConfiguration;

public class HibernateSystemConfigurationDao extends HibernateCommonDao
		implements SystemConfigurationDao {

	public SystemConfiguration get(String key) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SystemConfiguration.class);
		
		criteria.add(Restrictions.eq("key", key).ignoreCase());
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (SystemConfiguration)result.get(0);
	}
	
	public String getValue(String key) {
		SystemConfiguration system = get(key);
		if ( system == null )
			return null;
		else
			return system.getValue();
	}

}
