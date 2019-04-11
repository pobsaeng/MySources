package com.ie.icon.dao.hibernate;

import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ArticleTypeDao;
import com.ie.icon.domain.mch.ArticleType;

public class HibernateArticleTypeDao extends HibernateCommonDao implements
		ArticleTypeDao {

	public void create(ArticleType articleType) throws DataAccessException {
		getHibernateTemplate().save(articleType);
	}

	public void update(ArticleType articleType) throws DataAccessException {
		getHibernateTemplate().update(articleType);
	}

	public void delete(ArticleType articleType) throws DataAccessException {
		getHibernateTemplate().delete(articleType);
	}

	public ArticleType getArticleType(String articleTypeId) throws DataAccessException {
		return (ArticleType)getHibernateTemplate().get(ArticleType.class, articleTypeId);
	}

}
