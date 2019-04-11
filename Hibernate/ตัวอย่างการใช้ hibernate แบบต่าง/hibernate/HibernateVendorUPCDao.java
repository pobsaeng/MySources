package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.VendorUPCDao;
import com.ie.icon.domain.mch.VendorUPC;

public class HibernateVendorUPCDao extends HibernateCommonDao implements VendorUPCDao {
	public void create(VendorUPC vendorUPC) throws DataAccessException {
		getHibernateTemplate().save(vendorUPC);
	}

	public void update(VendorUPC vendorUPC) throws DataAccessException {
		getHibernateTemplate().update(vendorUPC);
	}

	public void delete(VendorUPC vendorUPC) throws DataAccessException {
		getHibernateTemplate().delete(vendorUPC);
		getHibernateTemplate().flush();
	}

	public VendorUPC getVendorUPC(String vendorUPC) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
		criteria.add(Restrictions.eq("vendorUPC", vendorUPC));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if ( result.size() != 1 )
			return null;
		else
			return (VendorUPC)result.get(0);
	}
	
	public VendorUPC getVendorUPCStore(String vendorUPC,String articleId) throws DataAccessException{
        DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
		criteria.add(Restrictions.eq("vendorUPC", vendorUPC));
		criteria.add(Restrictions.eq("article.articleId", articleId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if ( result.size() != 1 )
			return null;
		else
			return (VendorUPC)result.get(0);
	}

	public List getVendorUPCsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        List result = getHibernateTemplate().findByCriteria(criteria);
        return ((Integer)result.get(0)).intValue();
    }
    
    public void createOrUpdate(VendorUPC vendorUPC) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(vendorUPC);
    }
    
	public List getVendorUPC(String articleId, String sellUnit) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
		criteria.add(Restrictions.eq("sellUnit", sellUnit));
		criteria.createAlias("article", "article");
		criteria.add(Restrictions.eq("article.articleId", articleId));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public VendorUPC getVendorUPCByArticleId(String articleId)throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(VendorUPC.class);
		criteria.add(Restrictions.eq("article.articleId", articleId));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		List result = getHibernateTemplate().findByCriteria(criteria);

		VendorUPC vendorUPC = null;
		if (result != null && result.size() > 0) {
			vendorUPC = (VendorUPC) result.get(0);
		}
		return vendorUPC;
	}
}
