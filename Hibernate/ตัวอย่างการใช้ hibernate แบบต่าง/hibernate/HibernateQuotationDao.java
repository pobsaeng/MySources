package com.ie.icon.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.ie.icon.dao.QuotationDao;
import com.ie.icon.domain.quotation.Quotation;
import com.ie.icon.domain.so.CollectSalesOrder;


public class HibernateQuotationDao extends HibernateCommonDao implements QuotationDao{

	public List getLastVersionOfQuotation(String quotationNo){
		DetachedCriteria criteria = DetachedCriteria.forClass(Quotation.class);
		criteria.add(Restrictions.eq("quotationId", quotationNo)); 
		criteria.add(Restrictions.ne("status", "CANCELLED"));
		criteria.addOrder(Order.desc("versionId"));
		List quotationList = getHibernateTemplate().findByCriteria(criteria);
		if(quotationList != null && quotationList.size()>0){
			Quotation newQT = (Quotation)quotationList.get(0);
			getHibernateTemplate().initialize(newQT.getQuotationItems());
		}
		
		return quotationList;
	}
	
	public List getQuotation(String quotationNo, String version){
		DetachedCriteria criteria = DetachedCriteria.forClass(Quotation.class);
		criteria.add(Restrictions.eq("quotationId", quotationNo)); 
		if(version!=null){
			criteria.add(Restrictions.eq("versionId", version)); 
		}
		criteria.add(Restrictions.ne("status", "CANCELLED"));
		criteria.addOrder(Order.desc("versionId"));
		
		List quotationList = getHibernateTemplate().findByCriteria(criteria);
		if(quotationList != null && quotationList.size()>0){
			Quotation newQT = (Quotation)quotationList.get(0);
			getHibernateTemplate().initialize(newQT.getQuotationItems());
		}
		
		return quotationList;
	}
	
	public void createQuotation(Quotation qt){
		getHibernateTemplate().save(qt);
	}
	
	public void update(Quotation qt) {
        getHibernateTemplate().update(qt);

    }

	public List getQuotation(Date fromDate, Date toDate, String quotationFrom, String quotationTo){
		DetachedCriteria criteria = DetachedCriteria.forClass(Quotation.class);
		criteria.add(Restrictions.between("quotationDate", fromDate, toDate));
		
		if(!quotationFrom.equals("") && !quotationTo.equals("")){
			criteria.add(Restrictions.between("quotationId", quotationFrom, quotationTo)); 
		}
		else if(!quotationFrom.equals("")){
			criteria.add(Restrictions.eq("quotationId", quotationFrom));
		}
		else if(!quotationTo.equals("")){
			criteria.add(Restrictions.eq("quotationId", quotationTo));
		}
		criteria.add(Restrictions.ne("status", "CANCELLED"));
		criteria.addOrder(Order.asc("quotationId"));
		List quotationList = getHibernateTemplate().findByCriteria(criteria);
		if(quotationList != null && quotationList.size()>0){
			Quotation newQT = (Quotation)quotationList.get(0);
			getHibernateTemplate().initialize(newQT.getQuotationItems());
		}
		
		return quotationList;
	}
	
	public List getQuotationManageDetail(Date fromDate, Date toDate, String quotationFrom, String quotationTo){
//		String query = "select a.quotationId, a.versionId, a.status, d.ticketNo, a.transDate , b.collectSalesOrderNo, d.salesOrderNo, sum(e.netItemAmount) as so_amt "
//		String query = "select a.quotationId "
		
		String query = "select a.quotationId from Quotation a, CollectSalesOrder b where a.quotationId = b.quotationId ";
//			+ "from Quotation a, CollectSalesOrder b, CollectSalesOrderItem c, SalesOrder d, SalesOrderItem e "
//			+ "from Quotation a left outer join CollectSalesOrder b on a.quotationId = b.quotationId ";
//			+ "from Quotation as a left outer join CollectSalesOrder as b "
//			+ "a.quotationId = b.quotationId and a.versionId = b.versionId "
//			+ "where a.quotationId = b.quotationId " 
//			+ "and a.versionId = b.versionId "
//			+ "and b.objectId = c.collectSalesOrder.objectId(+) "
//			+ "and c.salesOrderNo = d.salesOrderNo(+) "
//			+ "and c.storeId = d.store.storeId(+) "
//			+ "and c.salesTransactionDate = d.transactionDate(+) "
//			+ "and d.objectId = e.salesOrder.objectId(+) "
//			+ "where a.quotationId = :quotationId ";
//			+ "group by a.quotationId, a.versionId, a.status, d.ticketNo, a.transDate , b.collectSalesOrderNo, d.salesOrderNo "
//			+ "order by a.transDate , a.quotationId, a.versionId ";	
		//JdbcTemplate jt = getJdbcTemplate();
		//List aa =  getHibernateTemplate().findByNamedParam(query, "quotationId", quotationFrom);
		getHibernateTemplate().find(query);
		return new ArrayList();
	}

	public Quotation getUpdateQuotation(String quotationId, String versionId,String store, Date transDate) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Quotation.class);
		
		criteria.add(Restrictions.eq("quotationId", quotationId));
		criteria.add(Restrictions.eq("versionId", versionId));
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("transDate", transDate));
		Quotation quotation = null;
		try{
			List quotationList = getHibernateTemplate().findByCriteria(criteria);
			if(quotationList != null && quotationList.size() > 0 ){
				quotation = (Quotation) quotationList.get(0);
				getHibernateTemplate().initialize(quotation.getQuotationItems());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quotation;
	}
	
}
