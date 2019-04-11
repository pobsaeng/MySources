package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.ArticleOCPBFeatureSizeDao;
import com.ie.icon.domain.ArticleOCPBFeatureSize;
import com.ie.icon.domain.ArticleOCPBFeatureSize.Id;


public class HibernateArticleOCPBFeatureSizeDao extends HibernateCommonDao implements ArticleOCPBFeatureSizeDao{

	public void update(ArticleOCPBFeatureSize articleOCPBFeatureSize) {
		getHibernateTemplate().update(articleOCPBFeatureSize);
	}

	public void createOrUpdate(ArticleOCPBFeatureSize articleOCPBFeatureSize) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articleOCPBFeatureSize);
	}

	public ArticleOCPBFeatureSize getArticleOCPBFeatureSize(Id id) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleOCPBFeatureSize.class);
		return (ArticleOCPBFeatureSize)getHibernateTemplate().get(ArticleOCPBFeatureSize.class, id);
	}
	
	public void delete(ArticleOCPBFeatureSize articleOCPBFeatureSize) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleOCPBFeatureSize.class);
		ArticleOCPBFeatureSize.Id id = new ArticleOCPBFeatureSize.Id();
		criteria.add(Restrictions.eq("articleOCPBFeatureSizeId.articleId",id.getArticleId()));
		List list = getHibernateTemplate().findByCriteria(criteria);
		getHibernateTemplate().deleteAll(list);
	}
	
	public void deleteAllList(List articleOCPBFeatureSizeList) throws DataAccessException {
		getHibernateTemplate().deleteAll(articleOCPBFeatureSizeList);
	}
}
