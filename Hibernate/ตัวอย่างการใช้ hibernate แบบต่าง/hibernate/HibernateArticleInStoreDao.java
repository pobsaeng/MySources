package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ArticleInStoreDao;
import com.ie.icon.domain.mch.ArticleInStore;

public class HibernateArticleInStoreDao extends HibernateCommonDao implements ArticleInStoreDao {

	public ArticleInStore getArticleInStore(String storeId, String articleId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleInStore.class);
		criteria.add(Restrictions.eq("articleInStoreId.storeId", storeId));
		criteria.add(Restrictions.eq("articleInStoreId.articleId", articleId));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 1)
			return (ArticleInStore) result.get(0);
		return null;
	}     

	public ArticleInStore getArticleInStore(String storeId, String articleId,String vendorId,String vendorName) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleInStore.class);
		criteria.add(Restrictions.eq("articleInStoreId.storeId", storeId));
		criteria.add(Restrictions.eq("articleInStoreId.articleId", articleId));
		criteria.createAlias("vendor", "vendor");
		if (vendorId != null && !"".equals(vendorId.trim())) {   
			criteria.add(Restrictions.like("vendor.vendorId", vendorId, MatchMode.END));
		} 
		if (vendorName != null && !"".equals(vendorName.trim())) {
			criteria.add(Restrictions.like("vendor.name", replace(vendorName)).ignoreCase());
		}
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 1)
			return (ArticleInStore) result.get(0);
		return null;
	}
	   
	public void create(ArticleInStore articleInStore) throws DataAccessException {
		getHibernateTemplate().save(articleInStore);		
		
	}   

	public void createOrUpdate(ArticleInStore articleInStore) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articleInStore);

	}

	public void delete(ArticleInStore articleInStore) throws DataAccessException {
		getHibernateTemplate().delete(articleInStore);
		
	}

	public void update(ArticleInStore articleInStore) throws DataAccessException {
		getHibernateTemplate().update(articleInStore);
		
	}

	public List getArticleInStoreByUpdDttmGtPubDttm(String storeId, int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleInStore.class);
		
        //criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
        //        Restrictions.isNull("lastPublishedDateTime")));
		
		criteria.add(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"));
		criteria.add(Restrictions.eq("articleInStoreId.storeId", storeId));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm(String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleInStore.class);
	        
	    //criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
	    //        Restrictions.isNull("lastPublishedDateTime")));
		 	
		criteria.add(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"));
		criteria.add(Restrictions.eq("articleInStoreId.storeId", storeId));
	    criteria.setProjection(Projections.rowCount());
	        
	    List result = getHibernateTemplate().findByCriteria(criteria);
	        
	    return ((Integer)result.get(0)).intValue();
	}
	
	

}
