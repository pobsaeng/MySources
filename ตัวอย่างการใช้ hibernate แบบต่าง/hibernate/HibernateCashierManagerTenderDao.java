package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.CashierManagerTenderDao;
import com.ie.icon.domain.cashier.CashierManagerTender;

public class HibernateCashierManagerTenderDao extends HibernateCommonDao implements
		CashierManagerTenderDao {

	public void create(CashierManagerTender cshManager)  throws DataAccessException{
		getHibernateTemplate().save(cshManager);

	}

	public void update(CashierManagerTender cshManager) throws DataAccessException {
		getHibernateTemplate().update(cshManager);

	}
	
	public void delete(CashierManagerTender cshManager) throws DataAccessException{
		getHibernateTemplate().delete(cshManager);
	}

	public List getCashierManageTender(String storeId,Date fromDate,Date toDate,String cashManageType, String tenderId) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierManagerTender.class);
		criteria.createAlias("store", "store");
		if ( storeId != null && !"".equals(storeId) && !storeId.equals(SystemConfigConstant.CENTER_CODE) ) {
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		if( fromDate!=null && !"".equals(fromDate) && toDate!=null && !"".equals(toDate)){
			criteria.add(Restrictions.between("transactionDate", fromDate, toDate));
		}
		
		if( cashManageType!=null && !"".equals(cashManageType) && !cashManageType.equals("ALL")){
			criteria.add(Restrictions.eq("cashierManageType", cashManageType)); 
		}
		criteria.createAlias("tender", "tender");
		if(tenderId != null && !tenderId.equals("") && !tenderId.equals("all")){
			criteria.add(Restrictions.eq("tender.tenderId", tenderId));
		}
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		criteria.addOrder(Order.asc("transactionDate"));
		criteria.addOrder(Order.asc("store.storeId"));
		criteria.addOrder(Order.asc("cashierManageType"));		
		
		criteria.addOrder(Order.asc("tender.tenderId"));	
		criteria.addOrder(Order.asc("rate"));	
		criteria.addOrder(Order.asc("numMonth"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getCshMngTenderList(String storeId, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierManagerTender.class);
		if(storeId != null && !"".equals(storeId))
		{
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		
		if(trnDate != null && !"".equals(trnDate))
		{
			criteria.add(Restrictions.eq("transactionDate", trnDate));
		}
		
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		List temp = getHibernateTemplate().findByCriteria(criteria);
		if(temp != null)
		{
			return temp;
		}
		else
		{
			return null;
		}
		
	}
	
	public List getCshManageTenderList(String storeId, Date trnDate, String tenderId, String cshManageType) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierManagerTender.class);
		
		
		if(storeId != null && !"".equals(storeId))
		{
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		
		if(trnDate != null && !"".equals(trnDate))
		{
			criteria.add(Restrictions.eq("transactionDate", trnDate));
		}
		
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		if(tenderId!=null && !tenderId.equals("") && !tenderId.equals("all")){
			criteria.createAlias("tender", "tender");
			criteria.add(Restrictions.eq("tender.tenderId", tenderId));
		}
		
		if(cshManageType!=null && !cshManageType.equals("") && !cshManageType.equals("all")){
			criteria.add(Restrictions.eq("cashierManageType", cshManageType));
		}
		
		criteria.addOrder(Order.asc("transactionDate"));
		criteria.addOrder(Order.asc("cashierManageType"));		
		criteria.addOrder(Order.asc("tender.tenderId"));	
		criteria.addOrder(Order.asc("rate"));	
		criteria.addOrder(Order.asc("numMonth"));
		
		List temp = getHibernateTemplate().findByCriteria(criteria);
		
		if(temp != null)
		{
			return temp;
		}
		else
		{
			return null;
		}
	}
	
	public List getCshManageTenderList(String storeId, Date trnDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierManagerTender.class);
		
		
		if(storeId != null && !"".equals(storeId))
		{
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		
		if(trnDate != null && !"".equals(trnDate))
		{
			criteria.add(Restrictions.eq("transactionDate", trnDate));
		}
		
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		criteria.addOrder(Order.asc("transactionDate"));
		criteria.addOrder(Order.asc("cashierManageType"));		
		criteria.addOrder(Order.asc("tender.tenderId"));	
		criteria.addOrder(Order.asc("rate"));	
		criteria.addOrder(Order.asc("numMonth"));
		
		List temp = getHibernateTemplate().findByCriteria(criteria);
		
		if(temp != null)
		{
			return temp;
		}
		else
		{
			return null;
		}
	}
	
	public List getCshMngTenderByRefPub(String refPub, String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierManagerTender.class);
		criteria.add(Restrictions.eq("refPubId", refPub));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		return getHibernateTemplate().findByCriteria(criteria);
		
	}
	
}
