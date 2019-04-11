 package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.constant.HirePurchaseConstant;
import com.ie.icon.dao.HirePurchaseDao;
import com.ie.icon.domain.Bank;
import com.ie.icon.domain.Store;
import com.ie.icon.domain.customer.HirePurchaseCompany;
import com.ie.icon.domain.mch.MCH;
import com.ie.icon.domain.promotion.HirePurchaseFileImport;
import com.ie.icon.domain.promotion.HirePurchaseOffering;
import com.ie.icon.domain.promotion.HirePurchaseOfferingItem;
import com.ie.icon.domain.promotion.HirePurchaseOfferingReject;

public class HibernateHirePurchaseDao extends HibernateCommonDao implements HirePurchaseDao {

	public void create(HirePurchaseFileImport hirePurchaseFileImport) throws DataAccessException {
		getHibernateTemplate().save(hirePurchaseFileImport);
	}
	
	public void update(HirePurchaseFileImport hirePurchaseFileImport) throws DataAccessException {
		getHibernateTemplate().update(hirePurchaseFileImport);
	}

	public void create(HirePurchaseOffering hirePurchaseOffering) throws DataAccessException {
		getHibernateTemplate().save(hirePurchaseOffering);
	}

	public void saveOrUpdate(HirePurchaseOffering hirePurchaseOffering) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(hirePurchaseOffering);
	}	
	
	public void update(HirePurchaseOffering hirePurchaseOffering) throws DataAccessException {
		getHibernateTemplate().update(hirePurchaseOffering);
	}

	public void update(HirePurchaseOfferingItem item) throws DataAccessException {
		getHibernateTemplate().update(item);
	}
	
	public void delete(HirePurchaseOffering hirePurchaseOffering) throws DataAccessException {
		getHibernateTemplate().delete(hirePurchaseOffering);
	}
		
	public void delete(HirePurchaseOfferingItem hirePurchaseOfferingItem) throws DataAccessException {
		getHibernateTemplate().delete(hirePurchaseOfferingItem);

	}
	
	public List getHirePurchaseOfferingItems(int condition, String conditionValue, String storeId, BigDecimal interestRate, Date fromDate, Date toDate,int condKey,String condOrder,int mchLevel) throws DataAccessException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(toDate);
		calendar.add(Calendar.DATE, 1);
		toDate = calendar.getTime();

		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOfferingItem.class);
		criteria.createAlias("hirePurchaseOffering", "hirePurchaseOffering");
		criteria.createAlias("article", "article");
		
		if ( condition == HirePurchaseConstant.RefferenceNo ) {
			//criteria.add(Restrictions.like("hirePurchaseOffering.offeringReference", replace(conditionValue)));
			//edit
			criteria.add(Restrictions.ilike("hirePurchaseOffering.offeringReference", conditionValue,MatchMode.ANYWHERE));
		} else
		if ( condition == HirePurchaseConstant.ArticleNo ) {
			criteria.add(Restrictions.like("article.articleId", replace(conditionValue)));
		} else
		if ( condition == HirePurchaseConstant.HpType ) {
			criteria.add(Restrictions.like("hirePurchaseOffering.hirePurchaseType", replace(conditionValue)).ignoreCase());
		} else 
		if ( condition == HirePurchaseConstant.Interest ) {
			criteria.add(Restrictions.eq("interestPercent", new BigDecimal(conditionValue)));
		} else
		if ( condition == HirePurchaseConstant.BANK){
			criteria.add(Restrictions.like("hirePurchaseOffering.bank",replace(conditionValue)).ignoreCase());
		} else
		if(condition == HirePurchaseConstant.MC){
			if (mchLevel == Constant.MCHLevel.MC) {
				criteria.add(Restrictions.like("article.mch.mchId",replace(conditionValue)));
				criteria.createAlias("article.mch", "mch");
				criteria.add(Restrictions.eq("mch.level", new Integer(mchLevel)));
			} else if (mchLevel == Constant.MCHLevel.MCH1) {
				criteria.createAlias("article.mch", "mch");
				criteria.add(Restrictions.like("mch.parentMCH.mchId",replace(conditionValue)));
				criteria.add(Restrictions.eq("mch.level", new Integer(0)));
			} else if (mchLevel == Constant.MCHLevel.MCH3) {
				criteria.add(Restrictions.ilike("article.mch.mchId", conditionValue, MatchMode.START));
				criteria.createAlias("article.mch", "mch");
				criteria.add(Restrictions.eq("mch.level", new Integer(0)));
			}
		}else
		if(condition == HirePurchaseConstant.VendorUPC){
			criteria.createAlias("article.vendorUPCs", "vendor");
			criteria.add(Restrictions.like("vendor.vendorUPC", replace(conditionValue)));
		}else
		if(condition == HirePurchaseConstant.MainUPC){
			criteria.createAlias("article.mainUPCs", "mainupc");
			criteria.add(Restrictions.like("mainupc.mainUPC", replace(conditionValue)));
		}
		
		if ( interestRate != null ) {
			criteria.add(Restrictions.eq("interestPercent", interestRate));
		}

		criteria.add(Restrictions.or(Restrictions.and(Restrictions.le("hirePurchaseOffering.effectiveDate", fromDate), Restrictions.ge("hirePurchaseOffering.endDate", fromDate)), 
				Restrictions.between("hirePurchaseOffering.effectiveDate", fromDate, toDate)));
		
		criteria.add(Restrictions.eq("isActive",new Boolean(true)));
		
		if(condKey == 0 || condKey == HirePurchaseConstant.RefferenceNo && condOrder.equals("asc")){
			criteria.addOrder(Order.asc("hirePurchaseOffering.offeringReference"));
			criteria.addOrder(Order.asc("hirePurchaseOffering.card"));
			criteria.addOrder(Order.asc("hirePurchaseOffering.store.storeId"));
			criteria.addOrder(Order.asc("interestPercent"));
			criteria.addOrder(Order.asc("termInMonth"));
			criteria.addOrder(Order.asc("article.articleId"));
		}else if(condKey == HirePurchaseConstant.RefferenceNo && condOrder.equals("desc")){
			criteria.addOrder(Order.desc("hirePurchaseOffering.offeringReference"));
			criteria.addOrder(Order.desc("hirePurchaseOffering.card"));
			criteria.addOrder(Order.desc("hirePurchaseOffering.store.storeId"));
			criteria.addOrder(Order.desc("interestPercent"));
			criteria.addOrder(Order.desc("termInMonth"));
			criteria.addOrder(Order.desc("article.articleId"));
		}else if(condKey == HirePurchaseConstant.ArticleNo && condOrder.equals("asc")){
			criteria.addOrder(Order.asc("article.articleId"));
			criteria.addOrder(Order.asc("hirePurchaseOffering.effectiveDate"));
			criteria.addOrder(Order.asc("hirePurchaseOffering.endDate"));
		}else if(condKey == HirePurchaseConstant.ArticleNo && condOrder.equals("desc")){
			criteria.addOrder(Order.desc("article.articleId"));
			criteria.addOrder(Order.desc("hirePurchaseOffering.effectiveDate"));
			criteria.addOrder(Order.desc("hirePurchaseOffering.endDate"));
		}
		
	

		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if ( storeId != null ) {
			if ( "all".equals(storeId) ) {
				for ( int i=0; i<list.size(); i++ ) {
					HirePurchaseOfferingItem item = (HirePurchaseOfferingItem)list.get(i);
					HirePurchaseOffering offering = item.getHirePurchaseOffering();
					if ( offering.getStore() != null )
						list.remove(i--);
				}
			} else {
				for ( int i=0; i<list.size(); i++ ) {
					HirePurchaseOfferingItem item = (HirePurchaseOfferingItem)list.get(i);
					HirePurchaseOffering offering = item.getHirePurchaseOffering();
					if ( offering.getStore() != null && !storeId.equals(offering.getStore().getStoreId()) )
						list.remove(i--);
				}
			}
		}
		
		if(list!=null){
		    if(condition == HirePurchaseConstant.MC && mchLevel == Constant.MCHLevel.MCH2) {  
				DetachedCriteria criteriaMch = DetachedCriteria.forClass(MCH.class);
			    criteriaMch.add(Restrictions.eq("parentMCH.mchId", replace(conditionValue)));
				criteriaMch.add(Restrictions.eq("level", new Integer(1)));
				
				List ret = getHibernateTemplate().findByCriteria(criteriaMch);
			    HashMap childMap = new HashMap();
			    if(ret.size()>0) {
			    	for(int i=0;i<ret.size();i++){
				    	MCH mch = (MCH)ret.get(i);
				    	if(mch.getChildMCHs().size()>0){
				    		for(int index=0;index<mch.getChildMCHs().size();index++){
				    			MCH mchChild = (MCH)mch.getChildMCHs().get(index);
				    			childMap.put(mchChild.getMchId(), mchChild.getMchId());
					    	}
				    	}
				    }
			    	for (int n=0;n<list.size();n++){
						HirePurchaseOfferingItem item = (HirePurchaseOfferingItem)list.get(n);
						if(childMap.get(item.getArticle().getMch().getMchId())==null){
							list.remove(n--);
						}
					}
			    }
			}
		}
		
		return list;
	}

	public List getHirePurchaseFileImports(Date fromDate, Date toDate) throws DataAccessException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(toDate);
		calendar.add(Calendar.DATE, 1);
		toDate = calendar.getTime();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseFileImport.class);
		criteria.add(Restrictions.between("importDateTime", fromDate, toDate));
		criteria.addOrder(Order.asc("importDateTime"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
		
	public Bank getBank(String bankId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bank.class);
		criteria.add(Restrictions.like("bankId", replace(bankId)).ignoreCase());
		List list = getHibernateTemplate().findByCriteria(criteria);
		if ( list.size() != 0 )
			return (Bank)list.get(0);
		
		return null;
	}

	public void create(HirePurchaseOfferingItem item) throws DataAccessException {
		getHibernateTemplate().save(item);
	}

	public HirePurchaseOffering getHirePurchaseOffering(String reference) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOffering.class);
		criteria.add(Restrictions.eq("offeringReference", reference));
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if ( list.size() != 0 )
			return (HirePurchaseOffering)list.get(0);
		
		return null;
	}
	
    public HirePurchaseOfferingItem getHirePurchaseOfferinItem(String articleId, String reference) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOfferingItem.class);
		criteria.createAlias("article", "article");
		criteria.createAlias("hirePurchaseOffering", "hirePurchaseOffering");
		criteria.add(Restrictions.eq("article.articleId", articleId));
		criteria.add(Restrictions.eq("hirePurchaseOffering.offeringReference", reference));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if ( list.size() != 0 )
			return (HirePurchaseOfferingItem)list.get(0);
		
		return null;
	}
	
	public HirePurchaseOffering getHirePurchaseOffering(String reference,
			String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOffering.class);
		criteria.add(Restrictions.eq("offeringReference", reference));
		
		if (!storeId.equals("")){ 
			Store store = new Store();
			store.setStoreId(storeId);
			
			criteria.add(Restrictions.eq("store", store));			
		}else{
			criteria.addOrder(Order.desc("store"));
		}
	
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if ( list.size() != 0 ){
			HirePurchaseOffering hpo = (HirePurchaseOffering)list.get(0);
			Hibernate.initialize(hpo.getFileImport());
			if (storeId.equals("") && (hpo.getStore() != null))
				return null;
			else
				return hpo;
		}else{			
			return null;
		}
	}
	
	public HirePurchaseOffering getHirePurchaseOffering(String hpcType,String storeId,String offRef,String cardTyp) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOffering.class);
		
		if(hpcType!=null && !hpcType.equals("")){
			criteria.add(Restrictions.eq("hirePurchaseType", hpcType));
		}
		if(storeId!=null && !storeId.equals("")){
			Store store = new Store();
			store.setStoreId(storeId);
			criteria.add(Restrictions.eq("store", store));		
		}
		if(offRef!=null && !offRef.equals("")){
			criteria.add(Restrictions.eq("offeringReference", offRef));
		}
		if(cardTyp!=null && !cardTyp.equals("")){
			criteria.add(Restrictions.eq("card", cardTyp));
		}
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if(list.size() != 0){
			HirePurchaseOffering hpOffering = (HirePurchaseOffering)list.get(0);
			Hibernate.initialize(hpOffering.getFileImport());
			return hpOffering;
		}
		return null;
	}

	public HirePurchaseOfferingItem getHirePurchaseOfferingItemByObjectId(
			String articleId, String objectId)
			throws DataAccessException {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOfferingItem.class);
		criteria.createAlias("article", "article");
		criteria.createAlias("hirePurchaseOffering", "hirePurchaseOffering");
		criteria.add(Restrictions.eq("article.articleId", articleId));
		criteria.add(Restrictions.eq("hirePurchaseOffering.objectId", new Long(objectId)));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if ( list.size() != 0 ){
			HirePurchaseOfferingItem hpoItem = (HirePurchaseOfferingItem)list.get(0);
			return hpoItem;
		}
		return null;
		
	}
	public void create(HirePurchaseOfferingReject hirePurchaseOfferingReject) throws DataAccessException {
		getHibernateTemplate().save(hirePurchaseOfferingReject);
	}
	
	public void update(HirePurchaseOfferingReject hirePurchaseOfferingReject) throws DataAccessException {
		getHibernateTemplate().update(hirePurchaseOfferingReject);
	}
	
	public HirePurchaseOfferingItem getHirePurchaseOfferingItemByKey(String articleId, BigDecimal intPercent,int termInMonth,String offRngID)
			throws DataAccessException {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOfferingItem.class);
		
		if(articleId!=null && !articleId.equals("")){
			criteria.createAlias("article", "article");
			criteria.add(Restrictions.eq("article.articleId", articleId));
		}
		if(intPercent!=null && !intPercent.equals("")){
			criteria.add(Restrictions.eq("interestPercent", intPercent));
		}
		
		criteria.add(Restrictions.eq("termInMonth",new Integer( termInMonth)));
		criteria.createAlias("hirePurchaseOffering", "hirePurchaseOffering");
		criteria.add(Restrictions.eq("hirePurchaseOffering.objectId", offRngID));
		criteria.add(Restrictions.eq("isActive",new Boolean(true)));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if ( list.size() != 0 ){
			HirePurchaseOfferingItem hpoItem = (HirePurchaseOfferingItem)list.get(0);
			return hpoItem;
		}
		return null;
		
	}
	
	public List getHirePurchaseOfferingRejectByStatus(String fileImportOID)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOfferingReject.class);
		criteria.add(Restrictions.eq("fileImport.objectId", new Long(fileImportOID)));
		criteria.add(Restrictions.or(Restrictions.eq("status", new Character(HirePurchaseOfferingReject.Failed)),Restrictions.eq("status", new Character(HirePurchaseOfferingReject.NotFound))));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if ( list.size() != 0 ){
			//HirePurchaseOfferingReject hpoReject = (HirePurchaseOfferingReject)list.get(0);
			return list;
		}
		return null;
	}
	
	public int getCountHirePurchaseOfferingRejectByStatus(String fileImportOID,String status)
		throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOfferingReject.class);
		
		criteria.add(Restrictions.eq("fileImport.objectId", new Long(fileImportOID)));
		criteria.add(Restrictions.eq("status",status));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if ( list.size() != 0 ){
			return  list.size();
		}
		return 0;
	}
	

	public HirePurchaseFileImport getHirePurchaseFileImportsByID(String fileImportOID) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseFileImport.class);
		criteria.add(Restrictions.eq("objectId", new Long(fileImportOID)));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if ( list.size() != 0 ){
			HirePurchaseFileImport hpoImport = (HirePurchaseFileImport)list.get(0);
			return hpoImport;
		}
		return null;
	}
	
	public HirePurchaseCompany getHirePurchaseCompanyByOid(String hirePurchaseOid){
		return getHirePurchaseCompanyByOid(hirePurchaseOid);
	}
	
	
	
		
}
