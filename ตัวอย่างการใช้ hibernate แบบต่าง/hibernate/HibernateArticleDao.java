package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.ArticleDao;
import com.ie.icon.domain.mch.Article;
import com.ie.icon.domain.mch.ArticleInStore;
import com.ie.icon.domain.mch.ArticlePricing;
import com.ie.icon.domain.mch.MCH;
import com.ie.icon.domain.mch.MainUPC;
import com.ie.icon.domain.mch.ScalingPrice;
import com.ie.icon.domain.mch.ValuationClass;
import com.ie.icon.domain.mch.VendorUPC;
import com.ie.icon.domain.sale.PriceConditionType;

public class HibernateArticleDao extends HibernateCommonDao implements ArticleDao {

	public void create(Article article) throws DataAccessException {
		getHibernateTemplate().save(article);
	}

	public void update(Article article) throws DataAccessException {
		getHibernateTemplate().update(article);
	}

	public void delete(Article article) throws DataAccessException {
		getHibernateTemplate().delete(article);
	}

	public void createOrUpdate(Article article) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(article);
	}
	public Article getArticle(String articleId) throws DataAccessException {
		return (Article) getHibernateTemplate().get(Article.class, articleId);
	}
   
	public Article getArticleByVendorPart(String vendorPartNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleInStore.class);   
		criteria.add(Restrictions.eq("vendorPartNo", vendorPartNo));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() != 1) {
			return null;
		} else {
			ArticleInStore artIns =  (ArticleInStore)result.get(0);
			return (Article)getArticle(artIns.getArticleInStoreId().getArticleId());
		}

	}

	public List getMCHByLevel(int level) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MCH.class);
		criteria.add(Restrictions.eq("level", new Integer(level)));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public MainUPC getMainUPCById(String itemCode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
		criteria.add(Restrictions.eq("mainUPC", itemCode));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1) return null;
		else return (MainUPC) result.get(0);
	}

	public VendorUPC getVendorUPCById(String itemCode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
		criteria.add(Restrictions.eq("vendorUPC", itemCode));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1) return null;
		else return (VendorUPC) result.get(0);
	}

	public ArticlePricing getPrice(String pricingTypeId, String articleId, String sellUnit) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePricing.class);
		criteria.add(Restrictions.eq("sellUnit", sellUnit));
		criteria.add(Restrictions.le("startDate", today()));
		criteria.add(Restrictions.ge("endDate", today()));
		DetachedCriteria articleCri = criteria.createCriteria("article");
		articleCri.add(Restrictions.eq("articleId", articleId));
		DetachedCriteria pricingTypeCri = criteria.createCriteria("priceConditionType");
		pricingTypeCri.add(Restrictions.eq("priceConditionTypeId", pricingTypeId));
		criteria.addOrder(Order.desc("createDate"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) return null;
		else return (ArticlePricing) result.get(0);
	}

	public ArticlePricing getPrice(String storeId, String pricingTypeId, String articleId, String sellUnit) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticlePricing.class);

		criteria.add(Restrictions.eq("sellUnit", sellUnit));
		criteria.add(Restrictions.le("startDate", today()));
		criteria.add(Restrictions.ge("endDate", today()));

		if (storeId != null && !"".equals(storeId)) {
			DetachedCriteria storeCri = criteria.createCriteria("store");
			storeCri.add(Restrictions.eq("storeId", storeId));
		}

		DetachedCriteria articleCri = criteria.createCriteria("article");
		articleCri.add(Restrictions.eq("articleId", articleId));

		DetachedCriteria pricingTypeCri = criteria.createCriteria("priceConditionType");
		pricingTypeCri.add(Restrictions.eq("priceConditionTypeId", pricingTypeId));

		criteria.addOrder(Order.desc("createDate"));

		List result = getHibernateTemplate().findByCriteria(criteria);
	

		if (result.size() == 0) {
			return null;
		} else {
			ArticlePricing pricing = (ArticlePricing)result.get(0);
			getHibernateTemplate().initialize(pricing.getScalingPrices());
			return pricing;
		}
	}

	public List getScalingPrices(long scalingPriceOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ScalingPrice.class);

		DetachedCriteria articlePricingCri = criteria.createCriteria("articlePricing");
		articlePricingCri.add(Restrictions.eq("objectId", new Long(scalingPriceOid)));

		criteria.addOrder(Order.desc("sellQuantity"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public PriceConditionType getPriceConditionType(String priceConditionTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PriceConditionType.class);

		criteria.add(Restrictions.eq("priceConditionTypeId", priceConditionTypeId));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1) return null;
		else return (PriceConditionType) result.get(0);
	}

	public List getArticlesByChildArticle(String articleId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);

		criteria.add(Restrictions.or(Restrictions.isNull("blockSell"), Restrictions.and(Restrictions.ne("blockSell", Constant.BlockSell.Z1), Restrictions.ne("blockSell", Constant.BlockSell.Z2))));

		DetachedCriteria articleSetCri = criteria.createCriteria("articleSets");
		DetachedCriteria childArticleCri = articleSetCri.createCriteria("childArticle");

		childArticleCri.add(Restrictions.eq("articleId", articleId));

		return getHibernateTemplate().findByCriteria(criteria);

	}
	
	public Article getArticleCheckBlock(String articleId) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.add(Restrictions.ne("blockSell", Constant.BlockSell.Z1));
		//criteria.add(Restrictions.ne("isBlockPurchase", new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size()>0){
			Article article = (Article)result.get(0);
			return article;
		}else{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ie.icon.dao.ArticleDao#getArticlesByUpdDttmGtPubDttm()
	 */
	public List getArticlesByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getMainUPCByArticle(String articleId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);

		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.createAlias("article", "article");
		criteria.add(Restrictions.eq("article.articleId", articleId));

		return getHibernateTemplate().findByCriteria(criteria);  
	}

	public List getVariantArticle(String articleId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.createAlias("parentGenericArticle", "genericArticle");
		criteria.add(Restrictions.eq("genericArticle.articleId", articleId));

		return getHibernateTemplate().findByCriteria(criteria);
	}   

	public List getArticles(String articleName,String brandName) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);		
		criteria.add(Restrictions.ilike("description", replace(articleName.trim()))); 
		if (brandName != null && !"".equals(brandName.trim())) {
			criteria.add(Restrictions.like("brand", replace(brandName)).ignoreCase());
		}
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("description"));  
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public MainUPC getMainUPCByMainUPCNo(String MainUPC)throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);

		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.add(Restrictions.eq("mainUPC", MainUPC));

		List list_mainupc = getHibernateTemplate().findByCriteria(criteria);
		if(list_mainupc != null && list_mainupc.size() != 0){
			MainUPC mainUpc = (MainUPC)list_mainupc.get(0);
			return mainUpc;
		}else{
			return null;
		}
	}

	public String getArticleByMainUPC(String mainUPC) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
		criteria.add(Restrictions.eq("mainUPC", mainUPC));

		List list_mainupc = getHibernateTemplate().findByCriteria(criteria);
		if(list_mainupc != null && list_mainupc.size() > 0){
			MainUPC mainUpc = (MainUPC)list_mainupc.get(0);
			return mainUpc.getArticle().getArticleId();
		}else{
			return "";
		}
	}

	public List getArticleList(List articleIdList) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);		
		criteria.add(Restrictions.in("articleId", articleIdList)); 
		criteria.addOrder(Order.asc("description"));  
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public String getValuationClassById(String valuationClass) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ValuationClass.class);
		criteria.add(Restrictions.eq("valuationClassId", valuationClass));
		List list_valuation = getHibernateTemplate().findByCriteria(criteria);
		if(list_valuation != null && list_valuation.size() != 0){
			ValuationClass valClass = (ValuationClass)list_valuation.get(0);
			return valClass.getDescription();
		}else{
			return "";
		}
	}
	public String getArticleIdFromVendorUPCByVendorUPCId(String vendorUPCId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
		criteria.add(Restrictions.eq("vendorUPC", vendorUPCId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);

		VendorUPC vendorUPC = null;
		if (result != null && result.size() > 0) {
			vendorUPC = (VendorUPC) result.get(0);
			return vendorUPC.getArticle().getArticleId();
		}else
			return null;
	}
	
	public VendorUPC getVendorUPCByVendorUpcId(String itemCode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
		criteria.add(Restrictions.eq("vendorUPC", itemCode));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		VendorUPC vendorUPC = null;
		if (result != null && result.size() > 0) {
			vendorUPC = (VendorUPC) result.get(0);
			return vendorUPC;
		}else
			return null;
		
	}

	
}