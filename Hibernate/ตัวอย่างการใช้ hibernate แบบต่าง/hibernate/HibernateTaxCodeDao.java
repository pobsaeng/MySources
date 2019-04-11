package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.TaxCodeDao;
import com.ie.icon.domain.TaxCode;

public class HibernateTaxCodeDao extends HibernateCommonDao implements
		TaxCodeDao {

	public void create(TaxCode taxCode) throws DataAccessException {
		getHibernateTemplate().save(taxCode);
	}

	public void update(TaxCode taxCode) throws DataAccessException {
		getHibernateTemplate().update(taxCode);
	}

	public void delete(TaxCode taxCode) throws DataAccessException {
		getHibernateTemplate().delete(taxCode);
	}

	public TaxCode getTaxCode(String taxCodeId) {
		return (TaxCode)getHibernateTemplate().get(TaxCode.class, taxCodeId);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TaxCodeDao#createOrUpdate(com.ie.icon.domain.TaxCode)
	 */
	public void createOrUpdate(TaxCode taxCode) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(taxCode);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TaxCodeDao#getTaxCodes()
	 */
	public List getTaxCodes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxCode.class);
		return getHibernateTemplate().findByCriteria(criteria);	
	}
	public List getTaxCodeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxCode.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxCode.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        return ((Integer)result.get(0)).intValue();
	}

}
