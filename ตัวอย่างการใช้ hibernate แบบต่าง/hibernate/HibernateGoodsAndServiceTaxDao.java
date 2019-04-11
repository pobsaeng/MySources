package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.GoodsAndServiceTaxDao;
import com.ie.icon.domain.so.VWGstTax;

public class HibernateGoodsAndServiceTaxDao extends HibernateCommonDao implements GoodsAndServiceTaxDao {

	public List getGoodsAndServiceTaxGrid() throws DataAccessException {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(VWGstTax.class);
		return getHibernateTemplate().findByCriteria(criteria);
	}
	public List getGoodsAndServiceTaxGrid(String customerTaxClassId,String articleTexClassId,String salesTaxClassId, String gstRateId, String gstexcludeGstId , Date startDate,Date endDate,String status)throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(VWGstTax.class);
		
		if( customerTaxClassId!=null && !"AL".equals(customerTaxClassId)){
			criteria.add(Restrictions.eq("gstCustTaxClassId",customerTaxClassId));
		}
		if( articleTexClassId!=null && !"AL".equals(articleTexClassId)){
			criteria.add(Restrictions.eq("gstArtcTaxClassId",articleTexClassId));
		}
		if( salesTaxClassId!=null && !"AL".equals(salesTaxClassId)){
			criteria.add(Restrictions.eq("gstSaleTaxClassId",salesTaxClassId));
		}
		if( gstRateId!=null && !"AL".equals(gstRateId)){
			criteria.add(Restrictions.eq("gstRateId",gstRateId));
		}
		if( gstexcludeGstId!=null && !"AL".equals(gstexcludeGstId)){
			criteria.add(Restrictions.eq("gstexcludeGstId",gstexcludeGstId));
		}
		if( startDate!=null && !"".equals(startDate) ){
			criteria.add(Restrictions.eq("startDate", startDate));
		}
		if( endDate!=null && !"".equals(endDate)){
			criteria.add(Restrictions.eq("endDate", endDate));
		}
		if( status!=null && !"AL".equals(status)){
			criteria.add(Restrictions.eq("status", status));
		}
		
		criteria.addOrder(Order.asc("gstCustTaxClassId")) ;
		criteria.addOrder(Order.asc("gstArtcTaxClassId")) ;
		criteria.addOrder(Order.asc("gstSaleTaxClassId")) ;
		criteria.addOrder(Order.asc("gstRateId")) ;
		criteria.addOrder(Order.asc("gstexcludeGstId")) ;
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

}