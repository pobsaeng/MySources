package com.ie.icon.dao.hibernate;

import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ArticleDimensionDao;
import com.ie.icon.domain.mch.ArticleDimension;
import com.ie.icon.domain.mch.ArticleDimensionTMP;

public class HibernateArticleDimensionDao extends HibernateCommonDao implements 
	ArticleDimensionDao{

	public void create(ArticleDimension articleDimension) throws DataAccessException {
		getHibernateTemplate().save(articleDimension);
		
	}

	public void create(ArticleDimensionTMP articleDimensionTMP) throws DataAccessException {
		getHibernateTemplate().save(articleDimensionTMP);
		
	}

	public void createOrUpdate(ArticleDimension articleDimension) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articleDimension);
		
	}

	public void createOrUpdate(ArticleDimensionTMP articleDimensionTMP) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articleDimensionTMP);
		
	}

	public void delete(ArticleDimension articleDimension) throws DataAccessException {
		getHibernateTemplate().delete(articleDimension);
		
	}

	public void delete(ArticleDimensionTMP articleDimensionTMP) throws DataAccessException {
		getHibernateTemplate().delete(articleDimensionTMP);
		
	}

	public void update(ArticleDimension articleDimension) throws DataAccessException {
		getHibernateTemplate().update(articleDimension);
		
	}

	public void update(ArticleDimensionTMP articleDimensionTMP) throws DataAccessException {
		getHibernateTemplate().update(articleDimensionTMP);
		
	}
	

}
