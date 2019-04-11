package com.ie.icon.dao.hibernate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.DsVendorGroupDao;
import com.ie.icon.domain.so.DsVendorGroup;

public class HibernateDsVendorGroupDao extends HibernateCommonDao implements DsVendorGroupDao {
	public boolean isDsVendorGroup(String[] vendorIds) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorGroup.class);
		criteria.add(Restrictions.in("vendorGroupId.hpVendorId", vendorIds));
		
		criteria.createAlias("dsVendor", "dsVendor");
		criteria.add(Restrictions.eq("dsVendor.status","Y"));
    	
		List list = getHibernateTemplate().findByCriteria(criteria);
		Set vendorIdsset = new HashSet();
		
		
		if (vendorIds != null){ 
			for (int index = 0; index < vendorIds.length; index++) {
				vendorIdsset.add(vendorIds[index]);
			}
		}
	
		if (list != null && vendorIds != null && list.size() == vendorIdsset.size()) {
			Set set = new HashSet();
			for (int index = 0; index < list.size(); index++) {
				DsVendorGroup dsVendorGroup = (DsVendorGroup) list.get(index);
				set.add(dsVendorGroup.getVendorGroupId().getDsVendorId());
			}
			if (set.size() == 1)
				return true;
		}
		return false;  
	}
	
	public boolean isDsVendorGroupExist(String[] vendorIds) throws DataAccessException {
		boolean isExist;
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorGroup.class);
		criteria.add(Restrictions.in("vendorGroupId.hpVendorId", vendorIds));
		
		criteria.createAlias("dsVendor", "dsVendor");
		criteria.add(Restrictions.eq("dsVendor.status","Y"));
    	
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if(list!=null){
			isExist = true;
		}else{
			isExist = false;
		}
		return isExist;
	}
	
	public HashMap getDsVendorGroup(String[] vendorIds) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorGroup.class);
		criteria.add(Restrictions.in("vendorGroupId.hpVendorId", vendorIds));
		
		criteria.createAlias("dsVendor", "dsVendor");
		criteria.add(Restrictions.eq("dsVendor.status","Y"));
    	
		List list = getHibernateTemplate().findByCriteria(criteria);
		HashMap map = new HashMap();
		
		if (list != null && !list.isEmpty()) {
			for (int index = 0; index < list.size(); index++) {
				DsVendorGroup dsVendorGroup = (DsVendorGroup) list.get(index);
				map.put(dsVendorGroup.getVendorGroupId().getDsVendorId(),dsVendorGroup.getVendorGroupId().getDsVendorId());
			}
			
		}
		return map;
	}
}
