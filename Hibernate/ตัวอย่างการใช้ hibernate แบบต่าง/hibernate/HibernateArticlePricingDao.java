package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ArticlePricingDao;
import com.ie.icon.domain.mch.ArticlePricing;
import com.ie.icon.domain.mch.ArticlePricingTMP;

public class HibernateArticlePricingDao extends HibernateCommonDao implements
		ArticlePricingDao {

	public void create(ArticlePricing articlePricing)
			throws DataAccessException {
		getHibernateTemplate().save(articlePricing);
	}
	/** soyhu added to insert data to TMP_ARTC_Pricing  1/7/2009.*/
	public void create(ArticlePricingTMP articlePricingTMP)
		throws DataAccessException {
		getHibernateTemplate().save(articlePricingTMP);
}

	public void delete(ArticlePricing articlePricing)
			throws DataAccessException {
		getHibernateTemplate().delete(articlePricing);
	}

	public void update(ArticlePricing articlePricing)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articlePricing);
	}
	/** Soyhu added to update TMP_ART_PRICE table to */
	public void updateTMP(ArticlePricingTMP articlePricingTMP)
	throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articlePricingTMP);
}	
    public void createOrUpdate(ArticlePricing articlePricing)
            throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(articlePricing);
    }
    
	public ArticlePricing getArtcilePricing(String articleId, String sellUnit, String sapNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePricing.class);
		
		criteria.createAlias("article", "article");
		criteria.add(Restrictions.eq("this.sapNo", sapNo));
		criteria.add(Restrictions.eq("article.articleId", articleId));
		criteria.add(Restrictions.eq("this.sellUnit", sellUnit));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (ArticlePricing)result.get(0);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.ArticlePricingDao#getArticlePricingsByUpdDttmGtPubDttm(String storeId)
	 */
	public List getArticlePricingsByUpdDttmGtPubDttm(String storeId, int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePricing.class);
		
//        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
//                Restrictions.isNull("lastPublishedDateTime")));
		
		criteria.add(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

    public int getRowByUpdDttmGtPubDttm(String storeId)
            throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePricing.class);
        
//        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
//                Restrictions.isNull("lastPublishedDateTime")));
        
        criteria.add(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
   /** Soyhu added to get datafrom temp  1- 7 -2009*/
    public List getArticlePricingsByUpdDttmGtPubDttmTMP(String storeId, int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePricingTMP.class);
		
//        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
//                Restrictions.isNull("lastPublishedDateTime")));
		
		criteria.add(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

    public int getRowByUpdDttmGtPubDttmTMP(String storeId)
            throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePricingTMP.class);
        
//        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
//                Restrictions.isNull("lastPublishedDateTime")));
        
        criteria.add(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
    /** Soyhu added  to get datafrom temp  1- 7 -2009*/

}
