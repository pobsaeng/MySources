package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.HirePurchaseCompanyDao;
import com.ie.icon.domain.customer.Customer;
import com.ie.icon.domain.customer.HirePurchaseCompany;
import com.ie.icon.domain.so.PriceGroup;

public class HibernateHirePurchaseCompanyDao extends HibernateCommonDao implements HirePurchaseCompanyDao {

	public void create(HirePurchaseCompany hirePurchaseCompany) {
		getHibernateTemplate().save(hirePurchaseCompany);

	}

	public List getHirePurchaseCompanies() {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseCompany.class);
		
		DetachedCriteria customerCri = criteria.createCriteria( "customer" );
		customerCri.addOrder(Order.asc("sapId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void update(HirePurchaseCompany hirePurchaseCompany) {
		getHibernateTemplate().update(hirePurchaseCompany);

	}

	public HirePurchaseCompany getHirePurchaseCompanyByCustomer(Customer customer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseCompany.class);
		
		criteria.add(Restrictions.eq("customer", customer));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (HirePurchaseCompany)result.get(0);
	}
	
	public List getHirePurchaseCompanyByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseCompany.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseCompany.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.HirePurchaseCompanyDao#createOrUpdate(com.ie.icon.domain.customer.HirePurchaseCompany)
	 */
	public void createOrUpdate(HirePurchaseCompany hirePurchaseCompany)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(hirePurchaseCompany);

	}

	public List getPriceGroups() throws DataAccessException {
		return getHibernateTemplate().loadAll(PriceGroup.class);
	}
	
	
	public HirePurchaseCompany getHirePurchaseCompanyByOid(String hirePurchaseOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseCompany.class);
		
		if(hirePurchaseOid != null && !hirePurchaseOid.equals("")){
			Long  hire_purchaseId = new Long(hirePurchaseOid);
			 criteria.add(Restrictions.eq("objectId", hire_purchaseId));
		}
		
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (HirePurchaseCompany)result.get(0);
	}
}
