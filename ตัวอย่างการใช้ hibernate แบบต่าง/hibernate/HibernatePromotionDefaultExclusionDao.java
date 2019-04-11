package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.PromotionDefaultExclusionDao;
import com.ie.icon.domain.promotion.PromotionDefaultExclusion;

public class HibernatePromotionDefaultExclusionDao extends HibernateCommonDao implements PromotionDefaultExclusionDao {

	public List getPromotionDefaultExclusionsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionDefaultExclusion.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionDefaultExclusion.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);
		return ((Integer) result.get(0)).intValue();
	}

	public void update(PromotionDefaultExclusion promotionDefaultExclusion) throws DataAccessException {
		getHibernateTemplate().update(promotionDefaultExclusion);
	}

	public void createOrUpdate(PromotionDefaultExclusion promotionDefaultExclusion) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(promotionDefaultExclusion);
	}

	public List getPromotionDefaultExclusions(String promotionType, String exclusionType) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionDefaultExclusion.class);
		criteria.add(Restrictions.eq("promotionTypeId", promotionType));
		criteria.add(Restrictions.eq("exclusionType", exclusionType));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		return getHibernateTemplate().findByCriteria(criteria);
	}

}
