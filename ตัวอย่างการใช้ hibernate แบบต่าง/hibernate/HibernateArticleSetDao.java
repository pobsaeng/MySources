package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ArticleSetDao;
import com.ie.icon.domain.mch.ArticleSet;

public class HibernateArticleSetDao extends HibernateCommonDao implements
		ArticleSetDao {

	public void create(ArticleSet articleSet) throws DataAccessException {
		getHibernateTemplate().save(articleSet);
	}

	public void delete(ArticleSet articleSet) throws DataAccessException {
		getHibernateTemplate().delete(articleSet);
	}

	public void update(ArticleSet articleSet) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articleSet);
	}

    public void createOrUpdate(ArticleSet articleSet)
            throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(articleSet);
    }
    
	public ArticleSet getArticleSet(String articleId, String childArticleId, String sellUnit) throws DataAccessException {
		return (ArticleSet)getHibernateTemplate().get(ArticleSet.class, new ArticleSet.Id(articleId, childArticleId, sellUnit));
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.ArticleSetDao#getArticleSetsByUpdDttmGtPubDttm()
	 */
	public List getArticleSetsByUpdDttmGtPubDttm(int first, int max) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleSet.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ArticleSet.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
}
