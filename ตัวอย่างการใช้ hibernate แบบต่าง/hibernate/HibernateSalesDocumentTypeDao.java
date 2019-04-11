package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.SalesDocumentTypeDao;
import com.ie.icon.domain.sale.SalesDocumentType;

public class HibernateSalesDocumentTypeDao extends HibernateCommonDao implements SalesDocumentTypeDao{
	public List getSalesDocumentType(){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentType.class);
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}	
	
	public List getSalesDocumentTypeByID(String salesDocumentTypeId){
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesDocumentType.class);
		criteria.add(Restrictions.eq("salesDocumentTypeId",salesDocumentTypeId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}	
}
