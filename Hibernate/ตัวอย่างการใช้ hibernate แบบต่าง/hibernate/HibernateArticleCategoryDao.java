package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ArticleCategoryDao;
import com.ie.icon.domain.mch.ArticleCategory;

public class HibernateArticleCategoryDao extends HibernateCommonDao implements
		ArticleCategoryDao {

	public void create(ArticleCategory articleCategory)
			throws DataAccessException {
		getHibernateTemplate().save(articleCategory);
	}

	public void update(ArticleCategory articleCategory)
			throws DataAccessException {
		getHibernateTemplate().update(articleCategory);
	}

	public void delete(ArticleCategory articleCategory)
			throws DataAccessException {
		getHibernateTemplate().delete(articleCategory);
	}

	public ArticleCategory getArticleCategory(String articleCategoryId)
			throws DataAccessException {
		return (ArticleCategory)getHibernateTemplate().get(ArticleCategory.class, articleCategoryId);
	}

	public void createOrUpdate(ArticleCategory articleCategory) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articleCategory);
	}

	public List getArticleCategoryByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleCategory.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleCategory.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

}
