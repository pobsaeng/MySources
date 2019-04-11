package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.HirePurchaseOfferingDao;
import com.ie.icon.domain.promotion.HirePurchaseOffering;

public class HibernateHirePurchaseOfferingDao extends HibernateCommonDao implements HirePurchaseOfferingDao{

	public List getHirePurchaseOfferByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOffering.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(HirePurchaseOffering.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(HirePurchaseOffering hirePurchaseOffering) throws DataAccessException {
		getHibernateTemplate().update(hirePurchaseOffering);
	}

	public void createOrUpdate(HirePurchaseOffering hirePurchaseOfferings) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(hirePurchaseOfferings);
	}

	public void deleteHirePurchaseOfferingItem(final HirePurchaseOffering hirePurchaseOfferings) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "delete from HirePurchaseOfferingItem " +
						" where hirePurchaseOffering = ?" ;
				Query query = session.createQuery(queryString);
				query.setLong(0, hirePurchaseOfferings.getObjectId());
				session.close();
				return null;
			}
		});
	}
}
