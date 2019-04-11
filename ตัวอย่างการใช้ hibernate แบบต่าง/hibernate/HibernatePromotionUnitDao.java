package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.PromotionUnitDao;
import com.ie.icon.domain.promotion.PromotionUnit;

public class HibernatePromotionUnitDao extends HibernateCommonDao implements PromotionUnitDao {

	public void create(PromotionUnit promotionUnit) throws DataAccessException {
		getHibernateTemplate().save(promotionUnit);
		
	}

	public List displayPromotionUnits() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionUnit.class);
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.desc("name"));
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	public void update(PromotionUnit promotionUnit) throws DataAccessException {
		getHibernateTemplate().update(promotionUnit);
		
	}

	public void createOrUpdate(PromotionUnit promotionUnit) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(promotionUnit);
	}

	public List getPromotionUnitByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionUnit.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionUnit.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

}
