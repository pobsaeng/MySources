package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria; 
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.PromotionRedemptionDao;
import com.ie.icon.domain.Store;
import com.ie.icon.domain.promotion.ItemRedemptionHistory;
import com.ie.icon.domain.promotion.PromotionArticleType;
import com.ie.icon.domain.promotion.PromotionNoReceiveRedemption;
import com.ie.icon.domain.promotion.PromotionPremiumRedemption;
import com.ie.icon.domain.promotion.PromotionRedemption;
import com.ie.icon.domain.promotion.PromotionSales;
import com.ie.icon.domain.promotion.form.PromotionMCSummaryForm;
import com.ie.icon.domain.promotion.form.SlipSummaryForm;
import com.ie.icon.domain.sale.SalesTransaction;

public class HibernatePromotionRedemptionDao extends HibernateCommonDao implements PromotionRedemptionDao {
	public void createEvict(PromotionRedemption promotionRedemption) throws DataAccessException {
		getHibernateTemplate().evict(promotionRedemption);
		getHibernateTemplate().clear();
		getHibernateTemplate().save(promotionRedemption);
	}
	
	public void create(PromotionRedemption promotionRedemption) throws DataAccessException {
		getHibernateTemplate().save(promotionRedemption);
	}

	public void update(PromotionRedemption promotionRedemption) throws DataAccessException {
		getHibernateTemplate().update(promotionRedemption);
	}

	public void delete(PromotionRedemption promotionRedemption) throws DataAccessException {
		getHibernateTemplate().delete(promotionRedemption);
	}

	public void update(PromotionNoReceiveRedemption promotionNoReceiveRedemption) throws DataAccessException {
		getHibernateTemplate().update(promotionNoReceiveRedemption);
	}


	public PromotionRedemption getPromotionRedemption(long objectId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.eq("objectId", new Long(objectId)));
		List ret = getHibernateTemplate().findByCriteria(criteria);

		if(ret!=null && ret.size()>0){
			for(int i = 0 ; i<ret.size() ; i++){
				PromotionRedemption redemption = (PromotionRedemption)ret.get(i);
				getHibernateTemplate().initialize(redemption.getSalesTransactions());
			}
		}
		
		if (ret == null)
			return null;
		else if (ret.size() != 1)
			return null;
		else {
			PromotionRedemption redemption = (PromotionRedemption) ret.get(0);
			initialData(redemption);
			return redemption;
		}
	}

	public void create(ItemRedemptionHistory itemRedemption) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(itemRedemption);
	}
	//POR Begin Add RedemptionDate 
	public PromotionRedemption getPromotionRedemption(String storeId, String promotionRedemptionId, char redemptionType, Date redemptionDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.eq("promotionRedemptionId", promotionRedemptionId));
		criteria.add(Restrictions.eq("redemptionType", new Character(redemptionType)));
		criteria.add(Restrictions.eq("redemptionDate", redemptionDate));
	//POR End Add RedemptionDate 
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List ret = getHibernateTemplate().findByCriteria(criteria);

		if (ret == null)
			return null;
		else if (ret.size() != 1)
			return null;
		else {
			PromotionRedemption redemption = (PromotionRedemption) ret.get(0);
			initialData(redemption);
			return redemption;
		}
	}

	private void initialData(PromotionRedemption promotionRedemption) {
		getHibernateTemplate().initialize(promotionRedemption.getPromotionDiscountRedemptions());
		getHibernateTemplate().initialize(promotionRedemption.getPromotionPremiumRedemptions());
		getHibernateTemplate().initialize(promotionRedemption.getPromotionNoReceiveRedemptions());
		getHibernateTemplate().initialize(promotionRedemption.getPromotionSales());
		getHibernateTemplate().initialize(promotionRedemption.getSalesTransactions());
		for (int index = 0; index < promotionRedemption.getPromotionPremiumRedemptions().size(); index++) {
			PromotionPremiumRedemption premium = (PromotionPremiumRedemption) promotionRedemption.getPromotionPremiumRedemptions().get(index);
			getHibernateTemplate().initialize(premium.getItemRedemptionHistories());
		}
		for (int index = 0; index < promotionRedemption.getPromotionSales().size(); index++) {
			PromotionSales promotionSales = (PromotionSales) promotionRedemption.getPromotionSales().get(index);
			getHibernateTemplate().initialize(promotionSales.getPromotionSalesItems());
		}
	}

	public List getPromotionRedemptions(int condition, Object conditionValue, char status) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);  
		criteria.add(Restrictions.or(Restrictions.eq("redemptionType", new Character(Constant.RedemptionType.PREMIUM)),
				Restrictions.eq("redemptionType", new Character(Constant.RedemptionType.ALL))));

		if (condition == 0) {
			String promotionRedemptionId = (String) conditionValue;
			criteria.add(Restrictions.eq("promotionRedemptionId", promotionRedemptionId));
		} else if (condition == 1) {
			Date redemptionDate = (Date) conditionValue;
			criteria.add(Restrictions.eq("redemptionDate", redemptionDate));
		} else if (condition == 2) {
			Date nextRedemptionDate = (Date) conditionValue;
			criteria.createAlias("promotionPremiumRedemptions", "promotionPremiumRedemptions");
			criteria.add(Restrictions.eq("promotionPremiumRedemptions.nextRedemptionDate", nextRedemptionDate));
		} else if (condition == 3) {
			String name = (String) conditionValue;
			name = replace(name);
			criteria.add(Restrictions.or(Restrictions.ilike("customerFirstName", name), Restrictions.ilike("customerLastName", name)));
		} else if (condition == 4){
			String telPhone = (String) conditionValue;
			criteria.add(Restrictions.eq("customerContact", telPhone));
		}

		criteria.add(Restrictions.eq("status", new Character(status)));

		criteria.addOrder(Order.asc("promotionRedemptionId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPromotionRedemptionAppointment(int condition, Object conditionValue,Date toDate) throws DataAccessException {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.eq("redemptionType", new Character(Constant.RedemptionType.ALL)));

		if (condition == 0) {
			String promotionRedemptionId = (String) conditionValue;
			criteria.add(Restrictions.eq("promotionRedemptionId", promotionRedemptionId));
		} else if (condition == 1) {
			Date nextRedemptionDate = (Date) conditionValue;
			criteria.createAlias("promotionPremiumRedemptions","promotionPremiumRedemptions");
			criteria.add(Restrictions.between("promotionPremiumRedemptions.nextRedemptionDate", nextRedemptionDate,toDate));
		}

		criteria.addOrder(Order.asc("promotionRedemptionId"));
		
		List results = getHibernateTemplate().findByCriteria(criteria);
		if(results!=null && !results.isEmpty()){
			initialPromotionRedemptionData(results);
		}
		
		return results;
	}

	
	public List getPromotionSales(String transactionReferrence) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionSales.class);
		criteria.add(Restrictions.eq("transactionReference", transactionReferrence));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPromotionRedemptions(String storeId, String redemptionStatus, Date fromDate, Date toDate, String userId) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.eq("redemptionType", new Character(Constant.RedemptionType.PREMIUM)));
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));

//		if (!"A".equals(redemptionStatus)) {
//			criteria.add(Restrictions.eq("isRedemptionComplete", Boolean.valueOf(redemptionStatus)));
//		}

		if (!"A".equals(redemptionStatus) && !"C".equals(redemptionStatus)) {
			criteria.add(Restrictions.eq("isRedemptionComplete", Boolean.valueOf(redemptionStatus)));
		}else if("C".equals(redemptionStatus)){
			criteria.add(Restrictions.eq("status", redemptionStatus));
		}
		
		if (userId != null && !"".equals(userId.trim()))
			criteria.add(Restrictions.eq("createUserId", userId));

		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		criteria.addOrder(Order.asc("promotionRedemptionId"));

		List ret = getHibernateTemplate().findByCriteria(criteria);
		initialPromotionRedemptionData(ret);
		return ret;
	}

	
	public List getPromotionRedemptions(String storeId, String redemptionStatus, Date fromDate, Date toDate, String userId,String promotionId) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.eq("redemptionType", new Character(Constant.RedemptionType.PREMIUM)));
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));

//		if (!"A".equals(redemptionStatus)) {
//			criteria.add(Restrictions.eq("isRedemptionComplete", Boolean.valueOf(redemptionStatus)));
//		}

		if (!"A".equals(redemptionStatus) && !"C".equals(redemptionStatus)) {
			criteria.add(Restrictions.eq("isRedemptionComplete", Boolean.valueOf(redemptionStatus)));
		}else if("C".equals(redemptionStatus)){
			criteria.add(Restrictions.eq("status", redemptionStatus));
		}
		
		if (userId != null && !"".equals(userId.trim()))
			criteria.add(Restrictions.eq("createUserId", userId));
		
//		if(promotionId != null && !"".equals(promotionId.trim())){
//			criteria.add(Restrictions.eq("promotionRedemptionId", promotionId));
//		}

		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		criteria.addOrder(Order.asc("promotionRedemptionId"));

		List ret = getHibernateTemplate().findByCriteria(criteria);
//		initialPromotionRedemptionData(ret);
		initialPromotionRedemptionData_rdmt(ret);
		
		return ret;
	}
	
	private void initialPromotionRedemptionData(List results) {
		if (results != null) {
			for (int index = 0; index < results.size(); index++) {
				PromotionRedemption redemption = (PromotionRedemption) results.get(index);
				getHibernateTemplate().initialize(redemption.getSalesTransactions());
				getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
			}
		}
	}
	
	private void initialPromotionRedemptionData_rdmt(List results) {
		if (results != null) {
			for (int index = 0; index < results.size(); index++) {
				PromotionRedemption redemption = (PromotionRedemption) results.get(index);
//				getHibernateTemplate().initialize(redemption.getSalesTransactions());
				getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
			}
		}
	}
	
	public List getPromotionPremiumRedemptions(String storeId, String promotionId, Date fromDate, Date toDate, String promotionType) throws Exception {
		String hsql = "select premium.promotionId, premium.promotionName, premium.tenderReference" + " , premium.promotionArticleType.name, premium.promotionArticleId, premium.description" + " , redemption.redemptionDate, redemption.store.storeId"
				+ " , sum(premium.eligibleQuantity) as eligible, sum(premium.redeemedQuantity) as redeemed, sum(premium.eligibleQuantity - premium.redeemedQuantity) as remain" + " from Promotion as promotion , PromotionRedemption as redemption"
				+ " inner join redemption.promotionPremiumRedemptions as premium" + " inner join premium.promotionArticleType as type" 
				+ " where TO_DATE(TO_CHAR(redemption.createDateTime,'DD/MM/YYYY'),'DD/MM/YYYY') >= :fromDate" 
				+ "   and TO_DATE(TO_CHAR(redemption.createDateTime,'DD/MM/YYYY'),'DD/MM/YYYY') <= :toDate"
				+ "   and promotion.promotionId = premium.promotionId"
				+" and promotion.isActive = 'Y' ";
		if (promotionType != null && !"".equals(promotionType.trim()))
			hsql += "   and promotion.promotionTypeId = :promotionType";
		if (promotionId != null && !"".equals(promotionId.trim()))
			hsql += "   and premium.promotionId = :promotionId";
		if (storeId != null && !"".equals(storeId.trim()))
			hsql += " and redemption.store.storeId = :storeId";
		hsql += " and premium.eligibleQuantity - premium.redeemedQuantity > 0";
		hsql += " group by premium.promotionId, premium.promotionName, premium.tenderReference" + ", premium.promotionArticleType.name, premium.promotionArticleId, premium.description" + ", redemption.redemptionDate, redemption.store.storeId"
				+ " order by premium.promotionId, premium.promotionName, premium.tenderReference" + ", premium.promotionArticleType.name, premium.promotionArticleId, premium.description" + ", redemption.store.storeId, redemption.redemptionDate";
		
		String params[];
		Object value[];
		if (storeId != null && !"".equals(storeId.trim())) {
			if (promotionId != null && !"".equals(promotionId.trim())) {
				if (promotionType != null && !"".equals(promotionType.trim())) {
					params = new String[]{"fromDate", "toDate", "promotionId", "storeId", "promotionType"};
					value = new Object[]{fromDate, toDate, promotionId, storeId, promotionType};
				} else {
					params = new String[]{"fromDate", "toDate", "promotionId", "storeId"};
					value = new Object[]{fromDate, toDate, promotionId, storeId};
				}
			} else {
				if (promotionType != null && !"".equals(promotionType.trim())) {
					params = new String[]{"fromDate", "toDate", "storeId", "promotionType"};
					value = new Object[]{fromDate, toDate, storeId, promotionType};
				} else {
					params = new String[]{"fromDate", "toDate", "storeId"};
					value = new Object[]{fromDate, toDate, storeId};
				}
			}
		} else {
			if (promotionId != null && !"".equals(promotionId.trim())) {
				if (promotionType != null && !"".equals(promotionType.trim())) {
					params = new String[]{"fromDate", "toDate", "promotionId", "promotionType"};
					value = new Object[]{fromDate, toDate, promotionId, promotionType};
				} else {
					params = new String[]{"fromDate", "toDate", "promotionId"};
					value = new Object[]{fromDate, toDate, promotionId};
				}
			} else {
				if (promotionType != null && !"".equals(promotionType.trim())) {
					params = new String[]{"fromDate", "toDate", "promotionType"};
					value = new Object[]{fromDate, toDate, promotionType};
				} else {
					params = new String[]{"fromDate", "toDate"};
					value = new Object[]{fromDate, toDate};
				}
			}
		}

		List ret = getHibernateTemplate().findByNamedParam(hsql, params, value);
		List results = null;
		if (ret != null && ret.size() > 0) {
			results = new ArrayList();
			for (int index = 0; index < ret.size(); index++) {
				Object[] object = (Object[]) ret.get(index);
				results.add(mapObjectToValue(object));
			}
		}
		return results;
	}

	private PromotionPremiumRedemption mapObjectToValue(Object object[]) {
		String promotionId = object[0] != null ? object[0].toString() : "";
		String promotionName = object[1] != null ? object[1].toString() : "";
		String tenderRef = object[2] != null ? object[2].toString() : "";
		String articleType = object[3] != null ? object[3].toString() : "";
		String articleId = object[4] != null ? object[4].toString() : "";
		String articleDesc = object[5] != null ? object[5].toString() : "";
		Date redemptionDate = object[6] != null ? (Date) object[6] : null;
		String storeId = object[7] != null ? object[7].toString() : "";
		BigDecimal eligible = object[8] != null ? new BigDecimal(object[8].toString()) : new BigDecimal("0.00");
		BigDecimal redeemed = object[9] != null ? new BigDecimal(object[9].toString()) : new BigDecimal("0.00");

		PromotionRedemption redemption = new PromotionRedemption();
		Store store = new Store();
		store.setStoreId(storeId);
		redemption.setStore(store);
		redemption.setRedemptionDate(redemptionDate);

		PromotionPremiumRedemption premium = new PromotionPremiumRedemption();
		premium.setPromotionId(promotionId);
		premium.setPromotionName(promotionName);
		premium.setTenderReference(tenderRef);
		PromotionArticleType type = new PromotionArticleType();
		type.setName(articleType);
		premium.setPromotionArticleType(type);
		premium.setPromotionArticleId(articleId);
		premium.setDescription(articleDesc);
		premium.setEligibleQuantity(eligible);
		premium.setRedeemedQuantity(redeemed);
		premium.setPromotionRedemption(redemption);

		return premium;
	}
	
	public List getPrmtnRdptn(String storeId, String promotionId, String vendorId, String vendorName, Date fromDate, Date toDate, char redemptionType) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));
		
		if (storeId != null && !"".equals(storeId)) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		
		criteria.createAlias("promotionDiscountRedemptions", "promotionDiscountRedemptions");
		criteria.add(Restrictions.eq("promotionDiscountRedemptions.discountType", Constant.PromotionDiscountType.COVN));
			
		if(promotionId!=null && !promotionId.equals("")){
			criteria.add(Restrictions.eq("promotionDiscountRedemptions.promotionId", promotionId));
		}
		
		if (vendorId != null && !"".equals(vendorId.trim())) {
			criteria.add(Restrictions.eq("promotionDiscountRedemptions.vendorId", vendorId));
		}
		if (vendorName != null && !"".equals(vendorName.trim())) {
			criteria.add(Restrictions.like("promotionDiscountRedemptions.vendorName", replace(vendorName)).ignoreCase());
		}
		
		criteria.addOrder(Order.asc("promotionDiscountRedemptions.promotionId"));
		criteria.addOrder(Order.asc("promotionDiscountRedemptions.vendorId"));
		criteria.addOrder(Order.asc("redemptionDate"));
		criteria.addOrder(Order.asc("store.storeId"));
		
		List ret = getHibernateTemplate().findByCriteria(criteria);
		List result = new ArrayList();
		
		if(ret!=null && ret.size()>0){
			for(int i = 0 ; i < ret.size() ; i++){
				boolean isNotDup = true;
				PromotionRedemption newPrmtnRdptn = (PromotionRedemption)ret.get(i);
				for(int j = 0 ; j<result.size() ; j++){
					PromotionRedemption oldPrmtnRdptn = (PromotionRedemption)result.get(j);
					if(newPrmtnRdptn == oldPrmtnRdptn){
						isNotDup = false;
						continue;
					}
				}
				if(isNotDup){
					result.add(newPrmtnRdptn);
				}
			}	
		}
		
		
//		if(result!=null && result.size()>0){
//			for(int i = 0 ; i < ret.size() ; i++){
//				PromotionRedemption newPrmtnRdptn = (PromotionRedemption)ret.get(i);
//				getHibernateTemplate().initialize(newPrmtnRdptn.getSalesTransactions());
//			}
//		}
		
		return result;	
	}
	
	
	public List getPromotionRedemptions(String storeId, String promotionId, String vendorId, String vendorName, Date fromDate, Date toDate, char redemptionType) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);

		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));
		//Alias
		switch (redemptionType) {
			case Constant.RedemptionType.PREMIUM :
				criteria.add(Restrictions.eq("redemptionType", new Character(Constant.RedemptionType.PREMIUM)));
				criteria.createAlias("promotionPremiumRedemptions", "promotionPremiumRedemptions");
				break;
			case Constant.RedemptionType.DISCOUNT :
				criteria.add(Restrictions.eq("redemptionType", new Character(Constant.RedemptionType.DISCOUNT)));
				criteria.createAlias("promotionDiscountRedemptions", "promotionDiscountRedemptions");
				break;
			case Constant.RedemptionType.ALL :
				criteria.createAlias("promotionSales", "promotionSales");
				break;
		}
		
		if (storeId != null && !"".equals(storeId)) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}

		if (promotionId != null && !"".equals(promotionId.trim())) {
			switch (redemptionType) {
				case Constant.RedemptionType.PREMIUM :
					//criteria.add(Restrictions.eq("promotionPremiumRedemptions.promotionId", promotionId));   
					//eidt
					criteria.add(Restrictions.ilike("promotionPremiumRedemptions.promotionId", promotionId, MatchMode.START));
					break;
				case Constant.RedemptionType.DISCOUNT :
					criteria.add(Restrictions.eq("promotionDiscountRedemptions.promotionId", promotionId));
					break;
				case Constant.RedemptionType.ALL :
					criteria.add(Restrictions.eq("promotionSales.promotionId", promotionId));
					break;
			}
		}

		if (vendorId != null && !"".equals(vendorId.trim())) {
			criteria.add(Restrictions.eq("promotionDiscountRedemptions.vendorId", vendorId));
		}
		if (vendorName != null && !"".equals(vendorName.trim())) {
			criteria.add(Restrictions.like("promotionDiscountRedemptions.vendorName", replace(vendorName)).ignoreCase());
		}
		
		
		//Order
		switch (redemptionType) {
			case Constant.RedemptionType.PREMIUM :
				criteria.addOrder(Order.asc("promotionPremiumRedemptions.promotionId"));
				criteria.addOrder(Order.asc("store.storeId"));
				break;
			case Constant.RedemptionType.DISCOUNT :
				criteria.addOrder(Order.asc("promotionDiscountRedemptions.promotionId"));
				criteria.addOrder(Order.asc("store.storeId"));
				break;
			case Constant.RedemptionType.ALL :
				criteria.addOrder(Order.asc("promotionSales.promotionId"));
				criteria.addOrder(Order.asc("store.storeId"));
				break;
		}
		
		List ret = getHibernateTemplate().findByCriteria(criteria);
		// List results = new ArrayList();
		// if (ret != null) {
		// HashMap map = new HashMap();
		// String key = "";
		// for (int index = 0; index < ret.size(); index++) {
		// PromotionRedemption redemption = (PromotionRedemption) ret.get(index);
		// key = "" + redemption.getObjectId();
		// // skip duplicate PromotionRedemption
		// if (!map.containsKey(key)) {
		// getHibernateTemplate().initialize(redemption.getPromotionSales());
		// getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
		// getHibernateTemplate().initialize(redemption.getPromotionDiscountRedemptions());
		// for (int ii = 0; ii < redemption.getPromotionSales().size(); ii++) {
		// PromotionSales proSales = (PromotionSales) redemption.getPromotionSales().get(ii);
		// getHibernateTemplate().initialize(proSales.getPromotionSalesItems());
		// }
		// map.put(key, redemption);
		// results.add(redemption);
		// }
		// }
		// }
		return ret;
	}

	public List getPromotionSales(String storeId, String promotionId, Date fromDate, Date toDate, char redemptionType) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionSales.class);

		criteria.createAlias("promotionRedemption", "promotionRedemption");
		switch (redemptionType) {
			case Constant.RedemptionType.PREMIUM :
			case Constant.RedemptionType.DISCOUNT :
				criteria.add(Restrictions.eq("promotionRedemption.redemptionType", new Character(redemptionType)));
				break;
		}
		criteria.add(Restrictions.ge("promotionRedemption.redemptionDate", fromDate));
		criteria.add(Restrictions.le("promotionRedemption.redemptionDate", toDate));

		if (storeId != null && !"".equals(storeId)) {
			criteria.createAlias("promotionRedemption.store", "promotionRedemption.store");
			criteria.add(Restrictions.eq("promotionRedemption.store.storeId", storeId));
		}

		if (promotionId != null && !"".equals(promotionId.trim())) {
			criteria.add(Restrictions.eq("promotionId", promotionId));
		}

		criteria.addOrder(Order.asc("promotionId"));
		criteria.addOrder(Order.asc("promotionRedemption.redemptionDate"));

		List ret = getHibernateTemplate().findByCriteria(criteria);
		for (int index = 0; index < ret.size(); index++) {
			PromotionSales proSales = (PromotionSales) ret.get(index);
			getHibernateTemplate().initialize(proSales.getPromotionSalesItems());
		}
		return ret;
	}

	public List getPromotionSalesItemSummary(String storeId, String promotionId, Date fromDate, Date toDate) throws Exception {
		String hsql = "select sales.promotionId, sales.promotionName" + " , redemption.redemptionDate, redemption.store.storeId" + " , substring(item.mch3, 1, 2), sum(item.netAmount)" + " from PromotionRedemption as redemption"
				+ " inner join redemption.promotionSales as sales" + " inner join sales.promotionSalesItems as item" + " where redemption.redemptionDate >= :fromDate" + "   and redemption.redemptionDate <= :toDate"
				+ "   and sales.promotionId = :promotionId";
		if (storeId != null && !"".equals(storeId.trim()))
			hsql += " and redemption.store.storeId = :storeId";
		hsql += " group by sales.promotionId, sales.promotionName" + ", redemption.store.storeId, redemption.redemptionDate, substring(item.mch3, 1, 2)" + " order by sales.promotionId, sales.promotionName"
				+ ", redemption.store.storeId, redemption.redemptionDate, substring(item.mch3, 1, 2)";

		String params[];
		Object value[];
		if (storeId != null && !"".equals(storeId.trim())) {
			params = new String[]{"fromDate", "toDate", "promotionId", "storeId"};
			value = new Object[]{fromDate, toDate, promotionId, storeId};
		} else {
			params = new String[]{"fromDate", "toDate", "promotionId"};
			value = new Object[]{fromDate, toDate, promotionId};
		}

		List ret = getHibernateTemplate().findByNamedParam(hsql, params, value);
		List results = null;
		if (ret != null && ret.size() > 0) {
			results = new ArrayList();
			for (int index = 0; index < ret.size(); index++) {
				Object[] object = (Object[]) ret.get(index);
				PromotionMCSummaryForm form = new PromotionMCSummaryForm();
				form.setPromotionId(object[0] != null ? object[0].toString() : "");
				form.setPromotionName(object[1] != null ? object[1].toString() : "");
				form.setRedemptionDate(object[2] != null ? (Date) object[2] : null);
				form.setStoreId(object[3] != null ? object[3].toString() : "");
				form.setMc(object[4] != null ? object[4].toString() : "");
				form.setRedemptionAmount(object[5] != null ? new BigDecimal(object[5].toString()) : new BigDecimal("0.00"));
				results.add(form);
			}   
		}
		return results;   
	}
	                                          	
	public List getPromotionSlipSummary(String storeId, String promotionId,String promotionType,Date fromDate, Date toDate) throws Exception {
		
		if(promotionType!=null && !promotionType.equals("")){
			promotionType = promotionType + "%";
		}
		    
		String hsql ="select promotionSales.promotionId,promotion.name,redemption.store.storeId,count(distinct promotionSales.transactionReference),count(distinct redemption.objectId)  "+
							",SUM(promotionSales.netTransactionAmount) ,SUM(promotionSales.netAmount) "+
							"from PromotionRedemption redemption ,PromotionSales promotionSales,Promotion promotion "+
							"where redemption.objectId = promotionSales.promotionRedemption.objectId "+
							"AND promotionSales.promotionId = promotion.promotionId ";
		
		if (storeId != null && !"all".equals(storeId) && !"".equals(storeId)) {
			hsql +=	"AND redemption.store.storeId = :storeId ";
		}
							
			hsql +=	"AND redemption.redemptionDate >= :fromDate "+
					"AND redemption.redemptionDate <= :toDate ";    
							
		if (promotionType != null && !"".equals(promotionType.trim())){
				hsql += "AND promotionSales.promotionId like :promotionType ";
		}
		if (promotionId != null && !"".equals(promotionId.trim())){
				hsql += "AND promotionSales.promotionId = :promotionId ";
		}
				hsql += "GROUP BY promotionSales.promotionId,promotion.name ,redemption.store.storeId "+
							"having count(distinct promotionSales.transactionReference) > 1 "; 
		
		String params[];
		Object value[];

		if (storeId != null && !"all".equals(storeId) && !"".equals(storeId)) {
			if(promotionId!=null && !promotionId.equals("")){
				if(promotionType!=null && !promotionType.equals("")){
					params = new String[]{"storeId","promotionId","promotionType","fromDate","toDate"};
					value = new Object[]{storeId,promotionId,promotionType,fromDate,toDate};
				}else{
					params = new String[]{"storeId","promotionId","fromDate","toDate"};
					value = new Object[]{storeId,promotionId,fromDate,toDate};
				}
			}else{
				if(promotionType!=null && !promotionType.equals("")){
					params = new String[]{"storeId","promotionType","fromDate","toDate"};
					value = new Object[]{storeId,promotionType,fromDate,toDate};  
				}else{
					params = new String[]{"storeId","fromDate","toDate"};
					value = new Object[]{storeId,fromDate,toDate};
				}
			}
		}else{
			if(promotionId!=null && !promotionId.equals("")){
				if(promotionType!=null && !promotionType.equals("")){
					params = new String[]{"promotionId","promotionType","fromDate","toDate"};
					value = new Object[]{promotionId,promotionType,fromDate,toDate};
				}else{
					params = new String[]{"promotionId","fromDate","toDate"};
					value = new Object[]{promotionId,fromDate,toDate};
				}
			}else{
				if(promotionType!=null && !promotionType.equals("")){
					params = new String[]{"promotionType","fromDate","toDate"};
					value = new Object[]{promotionType,fromDate,toDate};  
				}else{
					params = new String[]{"fromDate","toDate"};
					value = new Object[]{fromDate,toDate};
				}
			}
		}
	
		List ret = getHibernateTemplate().findByNamedParam(hsql, params, value);
		
		List results = null;
		if (ret != null && ret.size() > 0) {
			results = new ArrayList();
			for (int index = 0; index < ret.size(); index++) {
				Object[] object = (Object[]) ret.get(index);
				SlipSummaryForm form = new SlipSummaryForm();
				form.setPromotionId(object[0] != null ? object[0].toString() : "");
				form.setPromotionName(object[1] != null ? object[1].toString() : "");
				form.setStoreId(object[2] != null ? object[2].toString() : "");
				form.setTicketQuantity(object[3] != null ? Integer.parseInt(object[3].toString()) : 0);
				form.setCustomerQuantity(object[4] != null ? Integer.parseInt(object[4].toString()) : 0);
				form.setNetTransactionAmount(object[5] != null ? new BigDecimal(object[5].toString()) : new BigDecimal("0.00"));
				form.setNetPromotionReceiveAmount(object[6] != null ? new BigDecimal(object[6].toString()) : new BigDecimal("0.00"));
				results.add(form);
			}   
		}
		
		return results;   
	}

	public List getPromotionPremiumRedemptions(String storeId, Date fromDate, Date toDate, String redemptionStatus) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionPremiumRedemption.class);

		
		criteria.createAlias("promotionRedemption", "promotionRedemption");
		criteria.add(Restrictions.between("promotionRedemption.redemptionDate", fromDate, toDate));
		criteria.add(Restrictions.eq("promotionRedemption.redemptionType", new Character(Constant.RedemptionType.PREMIUM)));
		if (storeId != null && !"".equals(storeId)) {
			if(!"all".equals(storeId)){
				criteria.createAlias("promotionRedemption.store", "promotionRedemption.store");
				criteria.add(Restrictions.eq("promotionRedemption.store.storeId", storeId));
			}
		}

		if (!"A".equals(redemptionStatus) && !"C".equals(redemptionStatus)) {
			boolean isComplete = Boolean.valueOf(redemptionStatus).booleanValue();
			criteria.add(Restrictions.eq("promotionRedemption.isRedemptionComplete", new Boolean(isComplete)));
		}else if("C".equals(redemptionStatus)){
//			boolean isComplete = Boolean.valueOf(redemptionStatus).booleanValue();
			criteria.add(Restrictions.eq("promotionRedemption.status", redemptionStatus));
		}

		criteria.addOrder(Order.asc("promotionRedemption.redemptionDate"));
		criteria.addOrder(Order.asc("promotionRedemption.promotionRedemptionId"));
		criteria.addOrder(Order.asc("promotionId"));
		List ret = getHibernateTemplate().findByCriteria(criteria);
		if (ret != null) {
			for (int index = 0; index < ret.size(); index++) {
				PromotionPremiumRedemption premiumRedemption = (PromotionPremiumRedemption) ret.get(index);
				getHibernateTemplate().initialize(premiumRedemption.getPromotionRedemption().getSalesTransactions());  
				getHibernateTemplate().initialize(premiumRedemption.getPromotionRedemption().getPromotionSales());
			}
		}  
		return ret;
	}
	public List getPromotionPremiumRedemptions(String promotionRedempId,String promotionId, String promotionArticleTypeId) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionPremiumRedemption.class);

		criteria.add(Restrictions.eq("promotionId", promotionId));
		criteria.createAlias("promotionRedemption", "promotionRedemption");
		criteria.add(Restrictions.eq("promotionRedemption.promotionRedemptionId",promotionRedempId));
		
		criteria.createAlias("promotionArticleType","promotionArticleType");
		criteria.add(Restrictions.eq("promotionArticleType.promotionArticleTypeId", promotionArticleTypeId));

		List ret = getHibernateTemplate().findByCriteria(criteria);
		if (ret != null) {
			for (int index = 0; index < ret.size(); index++) {
				PromotionPremiumRedemption premiumRedemption = (PromotionPremiumRedemption) ret.get(index);
				getHibernateTemplate().initialize(premiumRedemption.getPromotionRedemption().getSalesTransactions());  
				getHibernateTemplate().initialize(premiumRedemption.getPromotionRedemption().getPromotionSales());
			}
		}  
		return ret;
	}

	public void cancelPromotionRedemption(String promotionRedemptionId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);

		criteria.createAlias("promotionRedemption", "promotionRedemption");
		// (Restrictions.eq("promotionRedemption.status",Constant.PromotionRedemptionStatus.CANCEL));
	}

	
	public List getPromotionRedemptions(Date fromDate, Date toDate) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
//		criteria.add(Restrictions.in("promotionRedemptionId", new String[]{"000023"}));
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));
		criteria.addOrder(Order.asc("redemptionDate"));
		List ret = getHibernateTemplate().findByCriteria(criteria);
		
//		if(ret != null && ret.size() > 0 ){
//			for (int index = 0; index < ret.size(); index++) {
//				PromotionRedemption redemption = (PromotionRedemption) ret.get(index);
//				getHibernateTemplate().initialize(redemption.getSalesTransactions());
//				getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
//			}
//		}
	
		if(ret != null && !ret.isEmpty()){
			PromotionRedemption redemption = (PromotionRedemption) ret.get(0);
			getHibernateTemplate().initialize(redemption.getSalesTransactions());
			getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
		}
		
		return ret;   
	}	
	
	public List getPromotionRedemptions_redempDetail(Date fromDate, Date toDate , String storeId,String promotionTypeId , String promotionId) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
//		criteria.add(Restrictions.in("promotionRedemptionId", new String[]{"000023"}));
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));
		if (storeId != null && !"".equals(storeId)) {
			if(!"all".equals(storeId)){
				criteria.createAlias("store", "store");
				criteria.add(Restrictions.eq("store.storeId", storeId));
			}
		}
		
		criteria.createAlias("promotionPremiumRedemptions", "promotionPremiumRedemptions");
		if(promotionTypeId != null && !"".equals(promotionTypeId.trim())){
			promotionTypeId = promotionTypeId + "%";
			criteria.add(Restrictions.like("promotionPremiumRedemptions.promotionId", promotionTypeId));
		}
		
		if(promotionId != null && !"".equals(promotionId.trim())){
			criteria.add(Restrictions.eq("promotionPremiumRedemptions.promotionId",promotionId ));
		}
		criteria.addOrder(Order.asc("redemptionDate"));
		List ret = getHibernateTemplate().findByCriteria(criteria);

//		if(ret != null && ret.size() > 0 ){
//			for (int index = 0; index < ret.size(); index++) {
//				PromotionRedemption redemption = (PromotionRedemption) ret.get(index);
//				getHibernateTemplate().initialize(redemption.getSalesTransactions());
//				getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
//			}
//		}
	
		if(ret != null && !ret.isEmpty()){
			PromotionRedemption redemption = (PromotionRedemption) ret.get(0);
			getHibernateTemplate().initialize(redemption.getSalesTransactions());
			getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
		}
		
		return ret;   
	}	
	
	//	 ** __ Pawan Add Method for report Premium __ ** \\ 
	public List getPromotionRedemptions_premium(Date fromDate, Date toDate) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));
		criteria.addOrder(Order.asc("redemptionDate"));
		List ret = getHibernateTemplate().findByCriteria(criteria);
		
		if(ret != null && ret.size() > 0 ){
			for (int index = 0; index < ret.size(); index++) {
				PromotionRedemption redemption = (PromotionRedemption) ret.get(index);
//				getHibernateTemplate().initialize(redemption.getSalesTransactions());
				getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
			}
		}
		return ret;   
	}	
	
//	 ** __ Pawan Add Method for **NEW** report Premium __ 10/06/2010** \\ 
	public List getPromotionRedemptions_premium(Date fromDate, Date toDate,String promotionTypeId , String promotionId,String storeId) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));
		
		// Pawan Add for Test
//		criteria.add(Restrictions.eq("promotionRedemptionId", "002902"));
		
		criteria.createAlias("promotionPremiumRedemptions", "promotionPremiumRedemptions");
		if(promotionTypeId != null && !"".equals(promotionTypeId.trim())){
			promotionTypeId = promotionTypeId + "%";
			criteria.add(Restrictions.like("promotionPremiumRedemptions.promotionId", promotionTypeId));
		}
		
		if(promotionId != null && !"".equals(promotionId.trim())){
			criteria.add(Restrictions.eq("promotionPremiumRedemptions.promotionId",promotionId ));
		}
		
		if (storeId != null && !"".equals(storeId)) {
			if(!"all".equals(storeId)){
				criteria.createAlias("store", "store");
				criteria.add(Restrictions.eq("store.storeId", storeId));
			}
		}
		
		criteria.addOrder(Order.asc("redemptionDate"));
		List ret = getHibernateTemplate().findByCriteria(criteria);
		
		if(ret != null && ret.size() > 0 ){
			PromotionRedemption redemption = (PromotionRedemption) ret.get(0);
//			getHibernateTemplate().initialize(redemption.getSalesTransactions());
			getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
			
//			for (int index = 0; index < ret.size(); index++) {
//				PromotionRedemption redemption = (PromotionRedemption) ret.get(index);
//				getHibernateTemplate().initialize(redemption.getSalesTransactions());
//				getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
//			}
		}
		return ret;   
	}	
	
	//ADD
	public List getPromotionRedemptions(Date fromDate, Date toDate,String promotionTypeId) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.ge("redemptionDate", fromDate));
		criteria.add(Restrictions.le("redemptionDate", toDate));   
	
		if(promotionTypeId!=null && !promotionTypeId.equals("")){
			criteria.createAlias("promotionPremiumRedemptions", "promotionPremiumRedemptions");
			criteria.add(Restrictions.ilike("promotionPremiumRedemptions.promotionId", promotionTypeId, MatchMode.START));
		}  
		
		criteria.addOrder(Order.asc("redemptionDate"));
		
		
		List ret = getHibernateTemplate().findByCriteria(criteria);
		for (int index = 0; index < ret.size(); index++) {
			PromotionRedemption redemption = (PromotionRedemption) ret.get(index);
			getHibernateTemplate().initialize(redemption.getSalesTransactions());
			getHibernateTemplate().initialize(redemption.getPromotionPremiumRedemptions());
		}
		return ret;
	}	
	
	public PromotionRedemption getPromotionRedemption(String storeId, String promotionRedemptionId, Date transactionDate) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.eq("promotionRedemptionId", promotionRedemptionId));
		criteria.add(Restrictions.eq("redemptionDate", transactionDate));

		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));

		List ret = getHibernateTemplate().findByCriteria(criteria);

		if (ret == null)
			return null;
		else if (ret.size() != 1)
			return null;
		else {
			PromotionRedemption redemption = (PromotionRedemption) ret.get(0);
			initialData(redemption);
			return redemption;
		}
	}
	
	public List getSalesTrnByPrmtnRdptnPos(long prmtnRdptnPosId , Date trnDate) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransaction.class);
		
//		criteria.createAlias("PromotionRedemption", "PromotionRedemption");
//		criteria.add(Restrictions.eq("PromotionRedemption.posPromotionRedemption",  new Long(prmtnRdptnPosId)));
		
		criteria.createAlias("posPromotionRedemption", "posPromotionRedemption");
		criteria.add(Restrictions.eq("posPromotionRedemption.objectId", new Long(prmtnRdptnPosId))); 
		
		criteria.add(Restrictions.eq("transactionDate", trnDate));
		
//		criteria.createAlias("posPromotionRedemption", "posPromotionRedemption");
//		criteria.add(Restrictions.eq("posPromotionRedemption.objectId", prmtnRdptnPosId)); 
		
		List ret = getHibernateTemplate().findByCriteria(criteria);
		
		if(ret == null){
			return null;
		}else{
			return ret;
		}
	}
	public List getPromotionSalesByPromotionRedemptionIdOID(long promotionRedemptionOID) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromotionSales.class);

		criteria.createAlias("promotionRedemption", "promotionRedemption");
		criteria.add(Restrictions.eq("promotionRedemption.objectId",new Long(promotionRedemptionOID)));


		List ret = getHibernateTemplate().findByCriteria(criteria);
		for (int index = 0; index < ret.size(); index++) {
			PromotionSales proSales = (PromotionSales) ret.get(index);
			getHibernateTemplate().initialize(proSales.getPromotionSalesItems());
		}
		return ret;
	}
	
	public PromotionRedemption getPromotionTransaction(String promotionRedemptionId,Date redemptionDate,char redemptionType, String storeId)throws DataAccessException{
        DetachedCriteria criteria = DetachedCriteria.forClass(PromotionRedemption.class);
		criteria.add(Restrictions.eq("promotionRedemptionId", promotionRedemptionId));
		criteria.add(Restrictions.eq("redemptionType", new Character(redemptionType)));
		if(redemptionDate != null){
			criteria.add(Restrictions.eq("redemptionDate", redemptionDate));
		}
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			PromotionRedemption promotion = (PromotionRedemption) result.get(0);
			getHibernateTemplate().initialize(promotion.getPromotionDiscountRedemptions());
			getHibernateTemplate().initialize(promotion.getPromotionSales());
			List salesitem = (List) promotion.getPromotionSales();
			if(salesitem != null && !salesitem.isEmpty()){
				for (int x = 0; x < salesitem.size(); x++) {
					PromotionSales item = (PromotionSales) salesitem.get(x);
					getHibernateTemplate().initialize(item.getPromotionSalesItems());
				}
			}
			getHibernateTemplate().initialize(promotion.getPromotionPremiumRedemptions());
			getHibernateTemplate().initialize(promotion.getPromotionNoReceiveRedemptions());
			
			
			return promotion;
		} else {
			return null;
		}
	}
}
