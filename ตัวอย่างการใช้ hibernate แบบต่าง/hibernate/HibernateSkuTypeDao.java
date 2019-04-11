package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SkuTypeDao;
import com.ie.icon.domain.ArticleOCPBFeatureSize;
import com.ie.icon.domain.SkuType;

public class HibernateSkuTypeDao extends HibernateCommonDao implements SkuTypeDao {

	public void update(SkuType skuType) {
		getHibernateTemplate().update(skuType);
	}
	
	public List getActiveSkuType() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SkuType.class);
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("mchId"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	public void createOrUpdate(SkuType skuType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(skuType);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SkuType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getSkuConfigByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SkuType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		
		List result = getHibernateTemplate().findByCriteria(criteria,first, max);
		if(result.size()==0)
			return null;
		else
			return result;
	}

	public List getActiveFeatureSize(String mchId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleOCPBFeatureSize.class);
		
		criteria.createAlias("article", "artc");
		criteria.add(Restrictions.like("artc.mch.mchId", (mchId + "%")));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("articleOCPBFeatureSizeId.articleSize"), "articleOCPBFeatureSizeId.articleSize")));
		criteria.addOrder(Order.asc("articleOCPBFeatureSizeId.articleSize"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

}
