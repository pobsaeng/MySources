package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.DiscountCondTypDao;
import com.ie.icon.domain.sale.DiscountConditionType;
import com.ie.icon.domain.sale.ItemDiscount;

public class HibernateDiscountCondTypDao extends HibernateCommonDao implements DiscountCondTypDao{

	public List getDiscountCondTypByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}  

	public void update(DiscountConditionType discountConditionType) throws DataAccessException {
		getHibernateTemplate().update(discountConditionType);
		
	}

	public void createOrUpdate(DiscountConditionType discountConditionType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(discountConditionType);
		
	}
	
	
	public DiscountConditionType getiscountConditionTypeById(String discountConditionTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
		
		criteria.add( Restrictions.eq( "discountConditionTypeId", discountConditionTypeId ) );
		
		List result = getHibernateTemplate().findByCriteria( criteria );
		
		if ( result.size() > 1 )
			return (DiscountConditionType)result.get( 0 );
		else
			return null;
	}

	public List getDiscountConditionTypes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
		return getHibernateTemplate().findByCriteria(criteria);		
	}

	public List getDiscountConditionTypeYI() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ItemDiscount.class);
		
		criteria.createAlias("discountConditionType", "discountConditionType");
		criteria.add(Restrictions.ilike("discountConditionType.discountConditionTypeId",Constant.Reason.YI,MatchMode.START));
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("discountConditionType.discountConditionTypeId")));
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("discountConditionType.description")));
		
		return getHibernateTemplate().findByCriteria(criteria);		
	}


}
