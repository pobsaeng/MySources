package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.CashierTenderRefDao;
import com.ie.icon.domain.cashier.CashierTenderRef;
import com.ie.icon.domain.cashier.CashierTenderRefDetail;
import com.ie.icon.domain.cashier.CashierTransaction;

public class HibernateCashierTenderRefDao extends HibernateCommonDao implements CashierTenderRefDao {
	public void create(CashierTenderRef cashierTenderRef) {
		getHibernateTemplate().save(cashierTenderRef);
	}
	
	public void update(CashierTenderRef cashierTenderRef) {
		getHibernateTemplate().saveOrUpdate(cashierTenderRef);
	}

	public List getCashierTenderRefList(String[] storeId, String ticketNo, String posId, String status, Date startDate, Date endDate, Date confirmedStartDate, Date confirmedEndDate) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTenderRef.class);
		
		criteria.createAlias("cashierTransaction", "cash");
		criteria.createAlias("cash.tender", "tender");
		if (startDate != null) {
			criteria.add(Restrictions.ge("tranDate", startDate));	
		}
		
		if (endDate != null) {
			criteria.add(Restrictions.le("tranDate", endDate));
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (confirmedStartDate != null) {
			confirmedStartDate = df.parse(sdf.format(confirmedStartDate) + " 00:00:00");
			criteria.add(Restrictions.ge("confirmedDate", confirmedStartDate));
		}
		
		if (confirmedEndDate != null) {
			confirmedStartDate = df.parse(sdf.format(confirmedEndDate) + " 23:59:59");
			criteria.add(Restrictions.le("confirmedDate", confirmedStartDate));
		}
		
		if (storeId != null && storeId.length > 0) {
			criteria.add(Restrictions.in("storeId", storeId));	
		}
		
		if (ticketNo != null) {
			criteria.add(Restrictions.eq("ticketNo", ticketNo));
		}
		
		if (posId != null) {
			criteria.add(Restrictions.eq("posId", posId));
		}
		
		if (!status.equalsIgnoreCase("all")){
			criteria.add(Restrictions.eq("status", status));
		}
		criteria.addOrder(Order.desc("status"));
		criteria.addOrder(Order.asc("storeId"));
		criteria.addOrder(Order.asc("tranDate"));
		criteria.addOrder(Order.asc("posId"));
		criteria.addOrder(Order.asc("ticketNo"));
		criteria.addOrder(Order.asc("tender.tenderNo"));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}

	public void create(CashierTenderRefDetail cashierTenderRefDetail) throws Exception {
		getHibernateTemplate().saveOrUpdate(cashierTenderRefDetail);
	}

	public CashierTenderRef getCashierTenderRef(String storeId, String ticketNo, String posId, Date tranDate) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTenderRef.class);
	
		criteria.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("status", "Pending"));
		criteria.add(Restrictions.eq("tranDate", tranDate));
		criteria.addOrder(Order.asc("objectId"));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList.size() == 0) {
			return null;
		}
		
		return (CashierTenderRef)resultList.get(0);
	}
	
	public CashierTenderRef getCashierTenderRef(CashierTransaction cshTran) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTenderRef.class);
		criteria.createAlias("cashierTransaction","cashierTransaction");
		criteria.add(Restrictions.eq("cashierTransaction.objectId",new Long(cshTran.getObjectId())));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList.size() == 0) {
			return null;
		}
		
		return (CashierTenderRef)resultList.get(0);
	}

	public List getCashierTenderRefList(String storeId, String ticketNo, String posId, Date tranDate) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTenderRef.class);
		
		criteria.add(Restrictions.eq("storeId", storeId));	
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posId));
		criteria.add(Restrictions.eq("tranDate", tranDate));
		
		List resultList = getHibernateTemplate().findByCriteria(criteria);
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}
}
