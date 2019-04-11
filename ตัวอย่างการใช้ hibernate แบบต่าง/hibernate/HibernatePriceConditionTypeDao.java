package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.PriceConditionTypeDao;
import com.ie.icon.domain.sale.PriceConditionType;

/**
 * @author amaritk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernatePriceConditionTypeDao extends HibernateCommonDao implements
		PriceConditionTypeDao{
	

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PriceConditionTypeDao#create(com.ie.icon.domain.sale.PriceConditionType)
	 */
	public void create(PriceConditionType priceConditionType)
			throws DataAccessException {
		getHibernateTemplate().save(priceConditionType);

	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PriceConditionTypeDao#delete(com.ie.icon.domain.sale.PriceConditionType)
	 */
	public void delete(PriceConditionType priceConditionType)
			throws DataAccessException {
		getHibernateTemplate().delete(priceConditionType);

	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PriceConditionTypeDao#update(com.ie.icon.domain.sale.PriceConditionType)
	 */
	public void update(PriceConditionType priceConditionType)
			throws DataAccessException {
		getHibernateTemplate().update(priceConditionType);

	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PriceConditionTypeDao#getPriceConditionTypes()
	 */
	public List getPriceConditionTypes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(PriceConditionType.class);
        
        return getHibernateTemplate().findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PriceConditionTypeDao#createOrUpdate(com.ie.icon.domain.sale.PriceConditionType)
	 */
	public void createOrUpdate(PriceConditionType priceConditionType)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(priceConditionType);

	}
	public List getPriceConditionTypeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PriceConditionType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(PriceConditionType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
}
