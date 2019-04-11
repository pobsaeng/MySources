package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.FestiveDao;
import com.ie.icon.domain.promotion.FestivePoint;
import com.ie.icon.domain.promotion.FestivePointItem;
 
public class HibernateFestiveDao extends HibernateCommonDao implements FestiveDao {

	public void create(FestivePoint festivePoint) throws DataAccessException {
		getHibernateTemplate().save(festivePoint);
	}
    
	public void update(FestivePoint festivePoint) throws DataAccessException {
		getHibernateTemplate().update(festivePoint);
	}
	
	public void saveOrUpdate(FestivePoint festivePoint) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(festivePoint);
	}	 
	
	public void delete(List festiveList)throws DataAccessException{
		for(int i=0;i<festiveList.size();i++){
			FestivePoint festivePoints = (FestivePoint)festiveList.get(i);
			getHibernateTemplate().delete(festivePoints);      
		}
	}
	
	public void delete(Long objectId) throws DataAccessException {
		FestivePoint festivePoint = (FestivePoint) getHibernateTemplate().load(FestivePoint.class, objectId);
		getHibernateTemplate().delete(festivePoint);
	}  

	public void createOrUpdate(FestivePoint festivePoint) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(festivePoint);
	}

	public FestivePoint getFestivePoints(String storeId, String promotionRedemptionId,Date redemptionDate) throws DataAccessException {
		FestivePoint festivePoint = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(FestivePoint.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.eq("promotionRedemptionId", promotionRedemptionId));
		criteria.add(Restrictions.eq("redemptionDate", redemptionDate));
		List festive = getHibernateTemplate().findByCriteria(criteria);
	
		if(festive!=null && !festive.isEmpty()){
			festivePoint = (FestivePoint)festive.get(0);
			getHibernateTemplate().initialize(festivePoint.getFestivePointItems());
		}
		return festivePoint;
	}
	
	public List getFestivePoints(String storeId, Date fromDate,Date toDate) throws DataAccessException {
		FestivePoint festivePoint = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(FestivePoint.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.between("transactionDate", fromDate, toDate));
		criteria.addOrder(Order.asc("redemptionDate"));   
		criteria.addOrder(Order.asc("promotionRedemptionId"));
		List festive = getHibernateTemplate().findByCriteria(criteria);
		if(festive!=null && !festive.isEmpty()){
			for(int i=0;i<festive.size();i++){
				festivePoint = (FestivePoint)festive.get(i);   
				getHibernateTemplate().initialize(festivePoint.getFestivePointItems());
			}
		}
		return festive;

	}
	
	public FestivePointItem getFestivePointItemByCouponNo(String couponNo) throws DataAccessException {
		FestivePointItem ftpItem = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(FestivePointItem.class);
		criteria.add(Restrictions.eq("couponNo", couponNo));
		List itemList = getHibernateTemplate().findByCriteria(criteria);
		if(itemList!=null && !itemList.isEmpty()){
			ftpItem = (FestivePointItem)itemList.get(0);
		}
		return ftpItem;
	}

	public List getFestiveStep() {
		//List result = getHibernateTemplate().find("select point from festive_step");
		
		Query query = (Query) getSession().createSQLQuery("select POINT from festive_step").addScalar("POINT", Hibernate.STRING);
		
		List result = query.list();
		
		return  result;
	}

	public FestivePoint getFestivePoint(String storeId, String promotionRedemptionId, Date redemptionDate) {
		FestivePoint festivePoint = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(FestivePoint.class);
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.eq("promotionRedemptionId", promotionRedemptionId));
		criteria.add(Restrictions.eq("redemptionDate", redemptionDate));
		List festive = getHibernateTemplate().findByCriteria(criteria);
	
		if (festive != null && !festive.isEmpty()){
			return (FestivePoint) festive.get(0);
		}else{
			return null;
		}
	}


	

	
	
}