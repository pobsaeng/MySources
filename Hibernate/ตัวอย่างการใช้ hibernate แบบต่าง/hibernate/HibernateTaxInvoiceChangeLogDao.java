package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.TaxInvoiceChangeLogDao;
import com.ie.icon.domain.sale.TaxInvoiceChangeLog;


public class HibernateTaxInvoiceChangeLogDao extends HibernateCommonDao implements TaxInvoiceChangeLogDao {
    
    public void create(TaxInvoiceChangeLog taxInvoiceChgLog) {
        getHibernateTemplate().save(taxInvoiceChgLog);
    }
    public List getTaxInvoiceChangeLog(String storeId, Date changeDate) {
	    DetachedCriteria criteria = DetachedCriteria.forClass(TaxInvoiceChangeLog.class);
	    
		DetachedCriteria taxInvCri = criteria.createCriteria("taxInvoice");
		DetachedCriteria storeCri = taxInvCri.createCriteria("store");
		storeCri.add(Restrictions.eq("storeId", storeId));
		
	    criteria.add(Restrictions.eq("changeDate", changeDate));
	    return getHibernateTemplate().findByCriteria(criteria);
    }
}
