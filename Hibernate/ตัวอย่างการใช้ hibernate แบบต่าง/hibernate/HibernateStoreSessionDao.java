package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.StoreSessionDao;
import com.ie.icon.domain.StoreSession;


public class HibernateStoreSessionDao extends HibernateCommonDao implements
        StoreSessionDao {

    /* (non-Javadoc)
     * @see com.ie.icon.dao.StoreSessionDao#create(com.ie.icon.domain.StoreSession)
     */
    public void create(StoreSession session) {
        getHibernateTemplate().save(session);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.StoreSessionDao#update(com.ie.icon.domain.StoreSession)
     */
    public void update(StoreSession session) {
        getHibernateTemplate().update(session);

    }

    public List getStoreSessionByStatus(char status) {
        DetachedCriteria criteria = DetachedCriteria.forClass(StoreSession.class);
        
         criteria.add(Restrictions.eq("status", new Character(status)));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public Date getTransactionDate(){
    	DetachedCriteria criteria = DetachedCriteria.forClass(StoreSession.class);
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.max("transactionDate"));
        criteria.setProjection(projList);
        List resultList =  getHibernateTemplate().findByCriteria(criteria);

        if(resultList!=null && resultList.size()>0){
        	Date  trnDate = (Date)resultList.get(0);
        	return trnDate;
        }else{
        	return null;
        }
    }
    
}
