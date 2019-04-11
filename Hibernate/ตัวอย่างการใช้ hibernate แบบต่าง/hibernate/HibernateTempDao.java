package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.TempDao;
import com.ie.icon.domain.customer.Customer;
import com.ie.icon.domain.temp.TmpShipToMap;

public class HibernateTempDao extends HibernateCommonDao implements TempDao{	
	
	public List getTmpShipToMapByShipToNo(String shipToNo){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TmpShipToMap.class);
		criteria.add(Restrictions.eq("shipToNo", shipToNo)); 
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public void create(Customer newCustomer){
		
		
		TmpShipToMap newShipTo = new TmpShipToMap();
		newShipTo.setShipToNo(newCustomer.getSapId());
		if(newCustomer.getTransportData()!=null && newCustomer.getTransportData().getMapName()!=null && !newCustomer.getTransportData().getMapName().equals("")){
			newShipTo.setMapNo(newCustomer.getTransportData().getMapName());
		}
		newShipTo.setCreateDateTime(new Date());
		newShipTo.setLastUpdateDate(new Date());
		
		getHibernateTemplate().save(newShipTo);
	}
	
	public void update(TmpShipToMap newShipTo){
		getHibernateTemplate().update(newShipTo);
	}
	
	public void delete(TmpShipToMap newShipTo){
		getHibernateTemplate().delete(newShipTo);
	}
}
