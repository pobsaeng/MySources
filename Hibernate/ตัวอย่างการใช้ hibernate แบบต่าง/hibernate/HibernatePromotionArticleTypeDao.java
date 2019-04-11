package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.PromotionArticleTypeDao;
import com.ie.icon.domain.promotion.PromotionArticleType;

public class HibernatePromotionArticleTypeDao extends HibernateCommonDao implements PromotionArticleTypeDao{

	public List getPrmtnArtcTypByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticleType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticleType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(PromotionArticleType promotionArticleType) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(promotionArticleType);
	}

	public void createOrUpdate(PromotionArticleType promotionArticleType) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdate(promotionArticleType);
	}

}
