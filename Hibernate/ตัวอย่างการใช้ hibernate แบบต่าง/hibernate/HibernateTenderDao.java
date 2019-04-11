package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.TenderDao;
import com.ie.icon.domain.Tender;
import com.ie.icon.domain.TenderGroup;

public class HibernateTenderDao extends HibernateCommonDao implements TenderDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TenderDao#create(com.ie.icon.domain.Tender)
	 */
	public void create(Tender tender) {
		 getHibernateTemplate().save(tender);
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TenderDao#update(com.ie.icon.domain.Tender)
	 */
	public void update(Tender tender) {
		getHibernateTemplate().update(tender);
	}

    /* (non-Javadoc)
     * @see com.ie.icon.dao.TenderDao#createOrUpdate(com.ie.icon.domain.Tender)
     */
    public void createOrUpdate(Tender tender) {
        getHibernateTemplate().saveOrUpdate(tender);
    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.TenderDao#getTenderById(java.lang.String)
     */
    public Tender getTenderById(String tenderId) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
//		criteria.add(Restrictions.eq("tenderId", tenderId));
		criteria.add(Restrictions.and(Restrictions.eq("tenderId", tenderId),
				 Restrictions.or(Restrictions.ne("isPos", Boolean.FALSE), Restrictions.ne("isSo", Boolean.FALSE))));       	
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if (result!=null && !result.isEmpty()){
			Tender tender = (Tender)result.get(0);
			getHibernateTemplate().initialize(tender.getTenderDenominations());
			return tender;			
		} else {
			return null;
		}
	}
    public Tender getTenderPOSById(String tenderId) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
		criteria.add(Restrictions.and(Restrictions.eq("tenderId", tenderId),
				Restrictions.eq("isPos", Boolean.TRUE)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if (result!=null && !result.isEmpty()){
			Tender tender = (Tender)result.get(0);
			getHibernateTemplate().initialize(tender.getTenderDenominations());
			return tender;			
		} else {
			return null;
		}
	}
    public Tender getTenderSOById(String tenderId) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
		criteria.add(Restrictions.and(Restrictions.eq("tenderId", tenderId),
				Restrictions.eq("isSo", Boolean.TRUE)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1)
			return null;
		else {
			Tender tender = (Tender)result.get(0);
			getHibernateTemplate().initialize(tender.getTenderDenominations());
			return tender;
		}
	}    
	/* (non-Javadoc)
     * @see com.ie.icon.dao.TenderDao#getTenders()
     */
    public List getTenders() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
        criteria.add(Restrictions.le("startDate", today()));
		criteria.add(Restrictions.ge("endDate", today()));
        criteria.addOrder(Order.asc("tenderId"));
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public List getTendersOrderByTenderName() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
        criteria.addOrder(Order.asc("tenderNo"));
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.TenderDao#getTenderByNo(java.lang.String)
     */
    public Tender getTenderByNo(String tenderNo) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
    	
		criteria.add(Restrictions.and(Restrictions.eq("tenderNo", tenderNo),
				 Restrictions.or(Restrictions.ne("isPos", Boolean.FALSE), Restrictions.ne("isSo", Boolean.FALSE))));    	
    	
    	//criteria.add(Restrictions.eq("tenderNo", tenderNo));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		return (result!=null && !result.isEmpty()) ? (Tender)result.get(0) : null ;
			
	}     
    public Tender getTenderPOSByNo(String tenderNo) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
		criteria.add(Restrictions.and(Restrictions.eq("tenderNo", tenderNo),
				Restrictions.eq("isPos", Boolean.TRUE)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		return (result!=null && !result.isEmpty()) ? (Tender)result.get(0) : null ;
	}
    public Tender getTenderSOByNo(String tenderNo) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
		criteria.add(Restrictions.and(Restrictions.eq("tenderNo", tenderNo),
				Restrictions.eq("isSo", Boolean.TRUE)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		return (result!=null && !result.isEmpty()) ? (Tender)result.get(0) : null ;
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TenderDao#getTendersByUpdDttmLtPubDttm()
	 */
	public List getTendersByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		criteria.setFetchMode("tenderDenominations", FetchMode.JOIN);
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
    /* (non-Javadoc)
     * @see com.ie.icon.dao.TenderDao#getRowByUpdDttmGtPubDttm()
     */
    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
    
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TenderDao#getPosTenders()
	 */
	public List getPosTenders() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
		criteria.add(Restrictions.eq("isPos", Boolean.TRUE));
		criteria.addOrder(Order.asc("tenderId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.TenderDao#getSoTenders()
	 */
	public List getSoTenders() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
		criteria.add(Restrictions.eq("isSo", Boolean.TRUE));
		criteria.addOrder(Order.asc("tenderId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getSoTendersNotHirepurchase() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		
		criteria.add(Restrictions.eq("isSo", Boolean.TRUE));
		criteria.add(Restrictions.isNull("hirepurchasecompoid"));
		criteria.addOrder(Order.asc("tenderId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getTendersByIsHpcAndType(boolean isHPC, int type) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);	
		criteria.add(Restrictions.eq("isHPC", new Boolean(isHPC)));
		criteria.add(Restrictions.eq("type", new Integer(type)));
		criteria.addOrder(Order.asc("tenderId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getTenders(String code) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		criteria.add(Restrictions.like("tenderId", replace(code)).ignoreCase());
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getTendersByType(int type) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		criteria.add(Restrictions.eq("type", new Integer(type)));
		criteria.addOrder(Order.asc("tenderNo"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public Tender getTenderByTenderId(String tenderId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		criteria.add(Restrictions.eq("tenderId", tenderId));
		criteria.addOrder(Order.asc("tenderNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1)
			return null;
		else {
			Tender tender = (Tender)result.get(0);
			return tender;
		}
	}
	
	public List getTendersByGroupId(String getTendersByGroupId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		if(getTendersByGroupId!=null && !getTendersByGroupId.equals("") && !getTendersByGroupId.equals("all")){
			criteria.add(Restrictions.eq("tenderGrpId", getTendersByGroupId));
		}
		criteria.add(Restrictions.or(Restrictions.eq("isPos", new Boolean(true)),(Restrictions.eq("isSo", new Boolean(true)))));
		criteria.addOrder(Order.asc("tenderNo"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getTenderGrp()throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TenderGroup.class);
		criteria.addOrder(Order.asc("tenderGrpId.grpId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public int getTenderType(String tenderId)throws Exception{

		DetachedCriteria criteria = DetachedCriteria.forClass(Tender.class);
		criteria.add(Restrictions.eq("tenderId", tenderId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if ( result.size() != 1)
			return 0;
		else {
			Tender tender = (Tender)result.get(0);
			return tender.getType();
		}
	}
}
