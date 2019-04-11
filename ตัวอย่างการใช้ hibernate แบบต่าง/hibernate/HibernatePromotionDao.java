package com.ie.icon.dao.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.constant.PosAppContext;
import com.ie.icon.constant.PromotionConstant;
import com.ie.icon.constant.Constant.TRNF_TYPE;
import com.ie.icon.dao.PromotionDao;
import com.ie.icon.dao.jdbc.JdbcPromotionDao;
import com.ie.icon.domain.promotion.ArticlePromotion;
import com.ie.icon.domain.promotion.BlockCodeCoverage;
import com.ie.icon.domain.promotion.BrandCoverage;
import com.ie.icon.domain.promotion.CorporatePromotion;
import com.ie.icon.domain.promotion.CreditCardCoverage;
import com.ie.icon.domain.promotion.HierarchyCondition;
import com.ie.icon.domain.promotion.MemberCoverage;
import com.ie.icon.domain.promotion.Promotion;
import com.ie.icon.domain.promotion.PromotionArticle;
import com.ie.icon.domain.promotion.PromotionArticleOption;
import com.ie.icon.domain.promotion.PromotionArticleOptionItem;
import com.ie.icon.domain.promotion.PromotionArticleTier;
import com.ie.icon.domain.promotion.PromotionCoverage;
import com.ie.icon.domain.promotion.PromotionDefaultExclusion;
import com.ie.icon.domain.promotion.PromotionHistory;
import com.ie.icon.domain.promotion.StoreCoverage;
import com.ie.icon.domain.promotion.TenderCoverage;
import com.ie.icon.domain.promotion.VendorCoverage;

public class HibernatePromotionDao extends HibernateCommonDao implements PromotionDao {

	public void create(BlockCodeCoverage blockCodeCoverage) throws DataAccessException {
		getHibernateTemplate().save(blockCodeCoverage);
	}

	public void create(BrandCoverage brandCoverage) throws DataAccessException {
		getHibernateTemplate().save(brandCoverage);
	}

	public void create(CreditCardCoverage creditCardCoverage) throws DataAccessException {
		getHibernateTemplate().save(creditCardCoverage);
	}

	public void create(MemberCoverage memberCoverage) throws DataAccessException {
		getHibernateTemplate().save(memberCoverage);
	}

	public void create(Promotion promotion) throws DataAccessException {
		getHibernateTemplate().save(promotion);
	}

	public void create(PromotionCoverage promotionCoverage) throws DataAccessException {
		getHibernateTemplate().save(promotionCoverage);
	}

	public void create(PromotionHistory promotionHistory) throws DataAccessException {
		getHibernateTemplate().save(promotionHistory);
	}

	public void create(StoreCoverage storeCoverage) throws DataAccessException {
		getHibernateTemplate().save(storeCoverage);
	}

	public void create(TenderCoverage tenderCoverage) throws DataAccessException {
		getHibernateTemplate().save(tenderCoverage);
	}

	public void create(VendorCoverage vendorCoverage) throws DataAccessException {
		getHibernateTemplate().save(vendorCoverage);
	}

	public void createOrUpdate(Promotion promotion) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(promotion);
	}

	public List get(int condition, String conditionValue, String promotionType, String storeId, int dateType, Date fromDate, Date toDate) throws DataAccessException {
		List list = new ArrayList();
		String[] promotionTypes = new String[]{PromotionConstant.PromotionType.CorporateID, PromotionConstant.PromotionType.CategoryID, PromotionConstant.PromotionType.ArticleID, PromotionConstant.PromotionType.TenderID};
		for (int i = 0; i < promotionTypes.length; i++) {
			DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
			criteria.add(Restrictions.eq("isActive", new Boolean(true)));

			if (condition == PromotionConstant.PromotionNameCondition) {
				criteria.add(Restrictions.like("name", replace(conditionValue)).ignoreCase());
			} else if (condition == PromotionConstant.PromotionIdCondition) {
				criteria.add(Restrictions.like("promotionId", replace(conditionValue)).ignoreCase());
			}

			if (promotionType != null && !"".equals(promotionType) && !promotionType.equals("All")) {
				criteria.add(Restrictions.eq("promotionTypeId", promotionType));
				i = promotionTypes.length;
			} else {
				criteria.add(Restrictions.eq("promotionTypeId", promotionTypes[i]));
			}

			// in case find by create date or last update date, toDate + 1
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toDate);
			calendar.add(Calendar.DATE, 1);
			
			switch (dateType) {
				case Constant.DateType.START_DATE :
					criteria.add(Restrictions.between("startDate", fromDate, toDate));
					break;
				case Constant.DateType.END_DATE :
					criteria.add(Restrictions.between("endDate", fromDate, toDate));
					break;
				case Constant.DateType.LAST_UPDATE_DATE :
					criteria.add(Restrictions.between("lastUpdateDate", fromDate, calendar.getTime()));
					break;
				case Constant.DateType.START_AND_END_DATE :
					LogicalExpression a = Restrictions.or(Restrictions.between("startDate", fromDate, toDate), Restrictions.between("endDate", fromDate, toDate));
//					LogicalExpression b = Restrictions.or(Restrictions.between("startDate", fromDate, toDate), Restrictions.between("endDate", fromDate, toDate));
					LogicalExpression b = Restrictions.and(Restrictions.le("startDate", fromDate), Restrictions.ge("endDate", fromDate));
					LogicalExpression c = Restrictions.and(Restrictions.le("startDate", toDate), Restrictions.ge("endDate", toDate));
					criteria.add(Restrictions.or(a, Restrictions.or(b, c)));
					break;
			}
			
			criteria.addOrder(Order.asc("promotionId"));    
			criteria.addOrder(Order.desc("endDate"));
			list.addAll(getHibernateTemplate().findByCriteria(criteria));
			//ADD
			for (int j = 0; j < list.size(); j++) {   
				initialize((Promotion) list.get(j));
			}
		}

		if (storeId != null && !"".equals(storeId) && !"all".equals(storeId)) {
			for (int i = 0; i < list.size(); i++) {
				Promotion promotion = (Promotion) list.get(i);
				switch (promotion.getStoreCoverage().charAt(0)) {
					case Constant.CoverageType.ALL :
						break;
					case Constant.CoverageType.EXCEPT :
						for (int j = 0; j < promotion.getStoreCoverages().size(); j++) {
							StoreCoverage storeCoverage = (StoreCoverage) promotion.getStoreCoverages().get(j);
							if (storeId.equals(storeCoverage.getStore().getStoreId())) {
								list.remove(i);
								i--;
								break;
							}
						}
						break;
					case Constant.CoverageType.ONLY :
						boolean flag = true;
						for (int j = 0; j < promotion.getStoreCoverages().size(); j++) {
							StoreCoverage storeCoverage = (StoreCoverage) promotion.getStoreCoverages().get(j);
							if (storeId.equals(storeCoverage.getStore().getStoreId())) {
								flag = false;
								break;
							}
						}
						if (flag) {
							list.remove(i);
							i--;
						}
						break;
				}
			}
		}

		return list;
	}

	public List getArticlePromotions(Date trnDate, boolean isPromotionSet) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePromotion.class);
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));
		criteria.add(Restrictions.eq("isPromotionSet", new Boolean(isPromotionSet)));
		criteria.addOrder(Order.desc("lastUpdateDate"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}

	public List getByPromotionArticle(String articleId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);

		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.createAlias("promotionArticleTiers", "promotionArticleTiers");
		criteria.createAlias("promotionArticleTiers.promotionArticleOptions", "promotionArticleOptions");
		criteria.createAlias("promotionArticleOptions.items", "items");
		criteria.add(Restrictions.eq("items.promotionArticleId", articleId));
		criteria.addOrder(Order.asc("promotionId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getByTypeAndDate(String type, Date date) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
//		criteria.add(Restrictions.eq("promotionId", "TD08001212"));
		criteria.add(Restrictions.eq("promotionTypeId", type));
		criteria.add(Restrictions.le("startDate", date));
		criteria.add(Restrictions.ge("endDate", date));
		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			Promotion promotion = (Promotion) list.get(i);
			getHibernateTemplate().initialize(promotion.getPromotionCoverages());
		}
		return list;
	}

	public Promotion getPromotion(long objectId) throws DataAccessException {
		Promotion promotion = (Promotion) getHibernateTemplate().get(Promotion.class, new Long(objectId));

		if (promotion == null)
			return null;

		initialize(promotion);

		return promotion;
	}

	public Promotion getPromotion(String promotionId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("promotionId", promotionId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List ret = getHibernateTemplate().findByCriteria(criteria);

		if (ret == null)
			return null;
		else if (ret.size() != 1)
			return null;
		else {
			Promotion promotion = (Promotion) ret.get(0);
			initialize(promotion);
			return promotion;
		}
	}
	
	public Promotion getPromotionByPrmtnId(String promotionId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("promotionId", promotionId));
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		List ret = getHibernateTemplate().findByCriteria(criteria);

		if (ret == null)
			return null;
		else if (ret.size() != 1)
			return null;
		else {
			Promotion promotion = (Promotion) ret.get(0);
			initialize(promotion);
			return promotion;
		}
	}

	public List getPromotionDefaultExclusions(String promotionTypeId, String exclusionType) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionDefaultExclusion.class);
		criteria.add(Restrictions.eq("promotionTypeId", promotionTypeId));
		criteria.add(Restrictions.eq("exclusionType", exclusionType));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public Promotion getPromotionDesc(String promotionId) throws DataAccessException {

		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.eq("promotionId", promotionId));

		criteria.addOrder(Order.desc("lastUpdateDate"));

		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() > 0)
			return (Promotion) list.get(0);
		return null;
	}

	public List getPromotionExcludeArticles(String promotionId, String promotionName) {
		promotionId = replace(promotionId);   
		promotionName = replace(promotionName);

		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);

		if (promotionId != null && !"".equals(promotionId))
			criteria.add(Restrictions.like("promotionId", promotionId));
		if (promotionName != null && !"".equals(promotionName))
			criteria.add(Restrictions.like("name", promotionName).ignoreCase());
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.add(Restrictions.in("promotionTypeId", new String[]{PromotionConstant.PromotionType.CorporateID, PromotionConstant.PromotionType.CategoryID, PromotionConstant.PromotionType.TenderID}));
		criteria.addOrder(Order.asc("name"));

		return getHibernateTemplate().findByCriteria(criteria);   
	}

	public List getPromotionHistories(String promotionId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionHistory.class);
		criteria.add(Restrictions.eq("promotionId", promotionId));
		criteria.addOrder(Order.asc("lastUpdateDate"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPromotions(Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));

		criteria.addOrder(Order.desc("lastUpdateDate"));

		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}

	public List getPromotionsByUpdateDateTime(String promotionType, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.eq("promotionTypeId", promotionType));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));

		criteria.addOrder(Order.desc("lastUpdateDate"));

		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}

	public List getPromotionsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		List list = getHibernateTemplate().findByCriteria(criteria, first, max);

		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	private void initialize(Promotion promotion) {
		if (promotion instanceof CorporatePromotion) {			
			CorporatePromotion corporatePromotion = (CorporatePromotion) promotion;
			getHibernateTemplate().initialize(corporatePromotion.getVendorCoverages());
			getHibernateTemplate().initialize(corporatePromotion.getBrandCoverages());
			getHibernateTemplate().initialize(corporatePromotion.getHierarchyConditions());
		}

		if (promotion instanceof ArticlePromotion) {
			ArticlePromotion articlePromotion = (ArticlePromotion) promotion;
			getHibernateTemplate().initialize(articlePromotion.getPromotionSetArticles());
		}
		getHibernateTemplate().initialize(promotion.getStoreCoverages());  
		getHibernateTemplate().initialize(promotion.getMemberCoverages());
		getHibernateTemplate().initialize(promotion.getMemberCardCoverages());
		getHibernateTemplate().initialize(promotion.getTenderCoverages());
		getHibernateTemplate().initialize(promotion.getBlockCodeCoverages());
		getHibernateTemplate().initialize(promotion.getPromotionCoverages());
		getHibernateTemplate().initialize(promotion.getCreditCardCoverages());
		getHibernateTemplate().initialize(promotion.getPromotionArticleTiers());
		getHibernateTemplate().initialize(promotion.getSalesTypeCoverages());
		getHibernateTemplate().initialize(promotion.getDiscountConditionTypeCoverages());
		List promotionCoverages = promotion.getPromotionCoverages();
		if (promotionCoverages != null && promotionCoverages.size() > 0) {
			for (int i = 0; i < promotionCoverages.size(); i++) {
				PromotionCoverage promotionCoverage = (PromotionCoverage) promotionCoverages.get(i);
				Promotion partnerPromotion = getPromotionDesc(promotionCoverage.getPromotionCoverageId().getPartnerPromotionId());
				if (partnerPromotion != null) {
					promotionCoverage.setPromotionId(partnerPromotion.getPromotionId());
					promotionCoverage.setName(partnerPromotion.getName());
				}
			}
		}

		List promotionArticleTiers = promotion.getPromotionArticleTiers();
		if (promotionArticleTiers != null && promotionArticleTiers.size() > 0) {
			for (int i = 0; i < promotionArticleTiers.size(); i++) {
				PromotionArticleTier promotionArticleTier = (PromotionArticleTier) promotionArticleTiers.get(i);
				List promotionArticleOptions = promotionArticleTier.getPromotionArticleOptions();
				if (promotionArticleOptions != null && promotionArticleOptions.size() > 0) {
					for (int j = 0; j < promotionArticleOptions.size(); j++) {
						PromotionArticleOption promotionArticleOption = (PromotionArticleOption) promotionArticleOptions.get(j);
						List items = promotionArticleOption.getItems();
						if (items != null && items.size() > 0) {
							for (int k = 0; k < items.size(); k++) {  
								PromotionArticleOptionItem promotionArticleOptionItem = (PromotionArticleOptionItem) items.get(k);
								if (promotionArticleOptionItem.getPromotionArticleId() != null) {
									if(Constant.PromotionArticleType.HOMEPRO_FREEGOODS.equals(promotionArticleOptionItem.getPromotionArticleType().getPromotionArticleTypeId())){
										promotionArticleOptionItem.setPromotionArticleName(promotionArticleOptionItem.getArticleDescription());
									}else{
										PromotionArticle promotionArticle = (PromotionArticle) getHibernateTemplate().get(PromotionArticle.class, promotionArticleOptionItem.getPromotionArticleId());
										if (promotionArticle != null) {
											promotionArticleOptionItem.setEffectiveDate(promotionArticle.getEffectiveDate());
											promotionArticleOptionItem.setEndDate(promotionArticle.getEndDate());
											promotionArticleOptionItem.setPromotionArticleName(promotionArticle.getDescription());
										}
									}  
									
								}
							}
						}
					}
				}
			}
		}
	}

	public void update(Promotion promotion) throws DataAccessException {
		getHibernateTemplate().update(promotion);
	}
	public List getPromotionsMarketing(Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));
		criteria.add(Restrictions.eq("isPrint", Boolean.TRUE));
		criteria.addOrder(Order.desc("lastUpdateDate"));

		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}
	public List getArticlePromotions(Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePromotion.class);
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));
		criteria.addOrder(Order.desc("lastUpdateDate"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}
		return list;
	}
	public List getPromotionsNonArticle(String promotionType,Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));
		criteria.add(Restrictions.ne("promotionTypeId", promotionType));

		criteria.addOrder(Order.desc("lastUpdateDate"));

		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}	
	public List getAllPromotions(Date trnDate) throws DataAccessException {
		
		JdbcPromotionDao pro=new JdbcPromotionDao();
		//String res=pro.getAllPosPromotion(trnDate);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		//criteria.add(Restrictions.eq("objectId",res));
		/*
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));
		criteria.addOrder(Order.desc("lastUpdateDate"));
		*/

		List list = getHibernateTemplate().findByCriteria(criteria);
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}

//	todsapon create for Start get promotion
public List getAllPromotions(List prmtnid) throws DataAccessException {
		
		//JdbcPromotionDao pro=new JdbcPromotionDao();
		//String res=pro.getAllPosPromotion(trnDate);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Promotion.class);
		criteria.add(Restrictions.in("objectId",prmtnid));
		/*
		criteria.add(Restrictions.eq("isActive", Boolean.TRUE));
		criteria.add(Restrictions.le("startDate", trnDate));
		criteria.add(Restrictions.ge("endDate", trnDate));
		criteria.addOrder(Order.desc("lastUpdateDate"));
		*/
        
		List list = getHibernateTemplate().findByCriteria(criteria);
		//System.out.println("Size "+list.size());
		for (int i = 0; i < list.size(); i++) {
			initialize((Promotion) list.get(i));
		}

		return list;
	}

	public List getPromotionHistoryByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionHistory.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowPromotionHistoryByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(PromotionHistory.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	public void updatePromotionHistory(PromotionHistory promotionHistory) throws DataAccessException {
		getHibernateTemplate().update(promotionHistory);
	}
	
	public void createOrUpdatePromotionHistory(PromotionHistory promotionHistory) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(promotionHistory);

	}
	
	public List getHierarchyConditionByPromotionOid(long promotionOid) throws DataAccessException{
		DetachedCriteria criteria  = DetachedCriteria.forClass(HierarchyCondition.class);
		
		criteria.createAlias("promotion","promotion");
		criteria.add(Restrictions.eq("promotion.objectId",new Long(promotionOid)));
		criteria.createAlias("productHierarchy","productHierarchy");
		criteria.addOrder(Order.desc("productHierarchy.level"));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		return resultList;
	}
}
