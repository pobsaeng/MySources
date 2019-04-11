package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.MainUPCDao;
import com.ie.icon.domain.mch.MainUPC;

public class HibernateMainUPCDao extends HibernateCommonDao implements MainUPCDao {
	public void create(MainUPC mainUPC) throws DataAccessException {
		//getHibernateTemplate().save(mainUPC);
		getHibernateTemplate().persist(mainUPC);
	}

	public void delete(MainUPC mainUPC) throws DataAccessException {
		getHibernateTemplate().delete(mainUPC);
		getHibernateTemplate().flush();
	}

	public void update(MainUPC mainUPC) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(mainUPC);
	}

    public void createOrUpdate(MainUPC mainUPC) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(mainUPC);
    }
    
	public MainUPC getMainUPC(String mainUPC) throws DataAccessException {
		List result = getHibernateTemplate().find("FROM MainUPC WHERE mainUPC = ?", mainUPC);
		//getHibernateTemplate().fin
//		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
//		criteria.add(Restrictions.eq("mainUPC", mainUPC));
//
//		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (MainUPC)result.get(0);
	}

	public MainUPC getDefaultMainUPC(String articleId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
		
		criteria.add(Restrictions.eq("isDefault", new Boolean(true)));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		DetachedCriteria articleCri = criteria.createCriteria("article");
		articleCri.add(Restrictions.eq("articleId", articleId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (MainUPC)result.get(0);
	}
	
	public MainUPC getDefaultMainUPCBySellUnit(String articleId, String sellUnit) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
		
		criteria.add(Restrictions.eq("isDefault", new Boolean(true)));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		DetachedCriteria articleCri = criteria.createCriteria("article");
		articleCri.add(Restrictions.eq("articleId", articleId));
		
		criteria.add(Restrictions.eq("sellUnit", sellUnit));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (MainUPC)result.get(0);
	}

	public List getAllDefaultMainUPC(String articleId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
		
		criteria.add(Restrictions.eq("isDefault", new Boolean(true)));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		DetachedCriteria articleCri = criteria.createCriteria("article");
		articleCri.add(Restrictions.eq("articleId", articleId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return result;
	}	
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MainUPCDao#getMainUPCsByUpdDttmGtPubDttm()
	 */
	public List getMainUPCsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);    
	}

    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }

	public MainUPC getMainUPC(String articleId, String sellUnit) throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
        
        criteria.add(Restrictions.eq("sellUnit", sellUnit));
		criteria.add(Restrictions.eq("isDefault", new Boolean(true)));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
        criteria.createAlias("article", "article");
        criteria.add(Restrictions.eq("article.articleId", articleId));
        
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() < 1 )
			return null;
		else
			return (MainUPC)result.get(0);
	}
	
	public List getDefaultMainUPCList(List articleIdList) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainUPC.class);
		criteria.add(Restrictions.eq("isDefault", new Boolean(true)));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		
		String[] articleIdArray = new String[articleIdList.size()];
		articleIdArray = (String[])articleIdList.toArray(new String[articleIdList.size()]);
		
		DetachedCriteria articleCri = criteria.createCriteria("article");
		articleCri.add(Restrictions.in("articleId", articleIdArray));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return result;
	}	
    
}
