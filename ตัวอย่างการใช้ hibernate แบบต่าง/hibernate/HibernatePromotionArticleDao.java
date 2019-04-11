package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.PromotionConstant;
import com.ie.icon.dao.PromotionArticleDao;
import com.ie.icon.domain.promotion.PromotionArticle;
import com.ie.icon.domain.promotion.PromotionArticleType;

public class HibernatePromotionArticleDao extends HibernateCommonDao implements PromotionArticleDao {

	public void create(PromotionArticle promotionArticle) throws DataAccessException {
		getHibernateTemplate().save(promotionArticle);
	}

	public void createOrUpdate(PromotionArticle promotionArticle) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(promotionArticle);
	}
	
	public void delete(PromotionArticle promotiionArticle) throws DataAccessException{
		getHibernateTemplate().delete(promotiionArticle);
	}

	public List get(String type, String articleId, String description, Date fromDate, Date toDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticle.class);
		
		if ( type != null && !"".equals(type.trim())) {
			criteria.createAlias("promotionArticleType", "promotionArticleType");
			criteria.add(Restrictions.eq("promotionArticleType.promotionArticleTypeId", type));
		}
		
		if ( articleId != null && !"".equals(articleId) ) {
			criteria.add(Restrictions.like("promotionArticleId", replace(articleId)));
		}
		
		if ( description != null && !"".equals(description) ) {
			criteria.add(Restrictions.like("description", replace(description)));
		}
		
		criteria.add(Restrictions.or(Restrictions.and(Restrictions.le("effectiveDate", fromDate), Restrictions.ge("endDate", fromDate)), 
				Restrictions.between("effectiveDate", fromDate, toDate)));
		
		criteria.addOrder(Order.asc("promotionArticleId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public PromotionArticle getByArticleId(String articleId) throws DataAccessException {
		return (PromotionArticle)getHibernateTemplate().get(PromotionArticle.class, articleId); 
	}

	public void update(PromotionArticle promotionArticle) throws DataAccessException {
		getHibernateTemplate().update(promotionArticle);
	}

	public PromotionArticleType getPromotionArticleType(String typeId) throws DataAccessException {
		return (PromotionArticleType)getHibernateTemplate().get(PromotionArticleType.class, typeId);
	}

	public List getPromotionArticleTypes() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticleType.class);
		criteria.addOrder(Order.asc("promotionArticleTypeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getByPromotionArticleTypeId(String promotionArticleTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticle.class);
		
		criteria.createAlias("promotionArticleType", "promotionArticleType");
		criteria.add(Restrictions.eq("promotionArticleType.promotionArticleTypeId", promotionArticleTypeId));
		criteria.add(Restrictions.le("effectiveDate", today()));
		criteria.add(Restrictions.ge("endDate", today()));
		criteria.addOrder(Order.asc("description"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getArticleByPromotionArticleTypeId(String promotionArticleTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticle.class);
		
		criteria.createAlias("promotionArticleType", "promotionArticleType");
		criteria.add(Restrictions.eq("promotionArticleType.promotionArticleTypeId", promotionArticleTypeId));
		criteria.add(Restrictions.ge("endDate", today()));
		criteria.addOrder(Order.asc("description"));
		return getHibernateTemplate().findByCriteria(criteria);
	}	

	public List getPromotionArticleTypes(String promotionTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticleType.class);
		
		if ( PromotionConstant.PromotionType.CorporateID.equals(promotionTypeId) )
			criteria.add(Restrictions.eq("isCorperatePromotion", new Boolean(true)));
		else
		if ( PromotionConstant.PromotionType.CategoryID.equals(promotionTypeId) )
			criteria.add(Restrictions.eq("isCategoryPromotion", new Boolean(true)));
		else
		if ( PromotionConstant.PromotionType.ArticleID.equals(promotionTypeId) )
			criteria.add(Restrictions.eq("isArticlePromotion", new Boolean(true)));
		else
		if ( PromotionConstant.PromotionType.TenderID.equals(promotionTypeId) )
			criteria.add(Restrictions.eq("isTenderPromotion", new Boolean(true)));
			
		criteria.addOrder(Order.asc("promotionArticleTypeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPromotionArticleTypes(boolean isStock) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticleType.class);
		criteria.add(Restrictions.eq("isStock", new Boolean(isStock)));
		criteria.addOrder(Order.asc("promotionArticleTypeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPromotionArticlesByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticle.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(PromotionArticle.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
}
