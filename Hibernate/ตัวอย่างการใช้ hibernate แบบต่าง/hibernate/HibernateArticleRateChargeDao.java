package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ArticleRateChargeDao;
import com.ie.icon.domain.mch.ArticleRateCharge;

public class HibernateArticleRateChargeDao extends HibernateCommonDao implements ArticleRateChargeDao{

	public List getArticleRateChargeByCondition(Integer hierarchyLv,String hierarchyId, Date fromDate, Date endDate) {
		System.out.println("--- Begin getArticleRateChargeByCondition() ----");
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleRateCharge.class);
		
		if(hierarchyLv!=null && !hierarchyLv.equals("")){
			if (hierarchyLv.equals(new Integer(9))) {
				
			}else{
				if(hierarchyId!=null && !hierarchyId.equals("")){
					criteria.add(Restrictions.eq("hierarchyId", hierarchyId));
				}
			}
		}
		
		if(fromDate!=null && !"".equals(fromDate)){
			criteria.add(Restrictions.le("startDate", fromDate));
		}
		if(endDate!=null && !"".equals(endDate)){
			criteria.add(Restrictions.ge("endDate", endDate));
		}
		
		criteria.add(Restrictions.eq("isActive","Y"));
		criteria.addOrder(Order.asc("objectId"));
		
		System.out.println("--- criteria.getAlias() = " + criteria.getAlias());
		
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for(int i=0; i< result.size(); i++){
				ArticleRateCharge articleRateCharge = (ArticleRateCharge)result.get(i);
				getHibernateTemplate().initialize(articleRateCharge.getArticleRateCharges());
			}
		}
		System.out.println("--- End getArticleRateChargeByCondition() ----");
		return result;
	}

	public void create(ArticleRateCharge articleRateCharge) throws DataAccessException {
		getHibernateTemplate().save(articleRateCharge);
	}
	
	public void update(ArticleRateCharge articleRateCharge)throws DataAccessException {
		getHibernateTemplate().update(articleRateCharge);
	}
	
	/* (non-Javadoc)
     * @see com.ie.icon.dao.ArticleRateChargeDao#getRowByUpdDttmGtPubDttm()
     */
    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ArticleRateCharge.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDateTimeStamp", "lastPublishDateTimeStamp"), 
                Restrictions.isNull("lastPublishDateTimeStamp")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
    
    /* (non-Javadoc)
	 * @see com.ie.icon.dao.ArticleRateChargeDao#getArticleRateChargeByUpdDttmLtPubDttm()
	 */
	public List getArticleRateChargeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArticleRateCharge.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDateTimeStamp", "lastPublishDateTimeStamp"), 
                Restrictions.isNull("lastPublishDateTimeStamp")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
	public void saveOrUpdate(ArticleRateCharge articleRateCharge) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(articleRateCharge);
	}

}