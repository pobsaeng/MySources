package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.CreditCardDao;
import com.ie.icon.domain.CreditCard;
import com.ie.icon.domain.CreditCardRange;

public class HibernateCreditCardDao extends HibernateCommonDao implements 
		CreditCardDao {
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CreditCardDao#getCreditCardRanges()
     */
    public List getCreditCardRanges() {
        DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
        
        criteria.addOrder(Order.asc("creditCardRangeId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CreditCardDao#getAllCardType()
     */
    public List getAllCardTypes() {
        DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
        
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("cardType")));
        
        criteria.addOrder(Order.asc("cardType"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }    
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CreditCardDao#getAllCardVendor()
     */
    public List getAllCardVendors() {
        DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
        
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("cardVendor")));
        
        criteria.addOrder(Order.asc("cardVendor"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }    
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.CreditCardDao#getAllCardLevel()
     */
    public List getAllCardLevels() {
        DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
        
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("cardLevel")));
        
        criteria.addOrder(Order.asc("cardLevel"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }    
    
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CreditCardDao#getCreditCardRangeById(java.lang.String)
	 */
	public CreditCardRange getCreditCardRangeById(String creditCardRangeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
		
		criteria.add(Restrictions.eq("creditCardRangeId", creditCardRangeId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1)
			return null;
		else {
			CreditCardRange creditCardRange = (CreditCardRange)result.get(0);
//			getHibernateTemplate().initialize(creditCardRange.getCreditCards());
			return creditCardRange;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CreditCardDao#getCreditCardRangeById(java.lang.String)
	 */
	public List getCreditCardRanges(String cardId, String cardType, String cardVendor, String cardLevel ) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
		if ( cardId != null && !"".equals(cardId) )
			criteria.add(Restrictions.eq("creditCard.creditCardId", cardId));
		if ( cardType != null && !"".equals(cardType) )
			criteria.add(Restrictions.eq("cardType", cardType));
		if ( cardVendor != null && !"".equals(cardVendor) )
			criteria.add(Restrictions.eq("cardVendor", cardVendor));
		if ( cardLevel != null && !"".equals(cardLevel) )
			criteria.add(Restrictions.eq("cardLevel", cardLevel));
		criteria.addOrder(Order.asc("creditCard.creditCardId"));
		criteria.addOrder(Order.asc("cardType"));
		criteria.addOrder(Order.asc("cardVendor"));
		criteria.addOrder(Order.asc("cardLevel"));
		return getHibernateTemplate().findByCriteria(criteria);
	}	
	
	public List getCreditCardRanges(CreditCardRange creditCardRangeCriterai) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
		if(creditCardRangeCriterai.getCreditCard()!=null) {
			if ( creditCardRangeCriterai.getCreditCard().getCreditCardId() != null && !"".equals(creditCardRangeCriterai.getCreditCard().getCreditCardId()) )
				criteria.add(Restrictions.eq("creditCard.creditCardId", creditCardRangeCriterai.getCreditCard().getCreditCardId()));
		}
		if ( creditCardRangeCriterai.getCardType() != null && !"".equals(creditCardRangeCriterai.getCardType() ) )
			criteria.add(Restrictions.eq("cardType", creditCardRangeCriterai.getCardType() ));
		if ( creditCardRangeCriterai.getCardVendor()  != null && !"".equals(creditCardRangeCriterai.getCardVendor()) )
			criteria.add(Restrictions.eq("cardVendor", creditCardRangeCriterai.getCardVendor()));
		if ( creditCardRangeCriterai.getCardLevel() != null && !"".equals(creditCardRangeCriterai.getCardLevel()) )
			criteria.add(Restrictions.eq("cardLevel", creditCardRangeCriterai.getCardLevel()));
		if ( creditCardRangeCriterai.getCreditCardRangeId() != null && !"".equals(creditCardRangeCriterai.getCreditCardRangeId()) )
			criteria.add(Restrictions.eq("creditCardRangeId", creditCardRangeCriterai.getCreditCardRangeId()));
		
		
		criteria.addOrder(Order.asc("creditCard.creditCardId"));
		criteria.addOrder(Order.asc("cardType"));
		criteria.addOrder(Order.asc("cardVendor"));
		criteria.addOrder(Order.asc("cardLevel"));
		return getHibernateTemplate().findByCriteria(criteria);
	}	
	
	//...Boizz(+) : #30 (HomePro Malay) Check Duplicate Card Range @ 08-Jan-14
	public CreditCardRange getCreditCardRanges(String creditCardId, String creditCardRangeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
		criteria.add(Restrictions.eq("creditCard.creditCardId", creditCardId));
		criteria.add(Restrictions.eq("creditCardRangeId", creditCardRangeId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result != null && !result.isEmpty()) {
			CreditCardRange creditCardRange = (CreditCardRange)result.get(0);
			return creditCardRange;
		}
			
		return null;
	}
	//....End(+)
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CreditCardDao#create(com.ie.icon.domain.CreditCardRange)
	 */
	public void create(CreditCardRange creditCardRange) {
		getHibernateTemplate().save(creditCardRange);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CreditCardDao#update(com.ie.icon.domain.CreditCardRange)
	 */
	public void update(CreditCardRange creditCardRange) {
		getHibernateTemplate().update(creditCardRange);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CreditCardDao#createOrUpdate(com.ie.icon.domain.CreditCardRange)
	 */
	public void createOrUpdate(CreditCardRange creditCardRanges)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(creditCardRanges);

	}


	public List getCreditCards() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CreditCard.class);
		criteria.addOrder(Order.asc("name"));
		return getHibernateTemplate().findByCriteria(criteria);
	}


	public CreditCard getCreditCard(String creditCardId) throws DataAccessException {
		return (CreditCard)getHibernateTemplate().get(CreditCard.class, creditCardId);
	}   


	public List getCreditCards(String name) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CreditCard.class);
		criteria.add(Restrictions.like("name", replace(name)).ignoreCase());
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void create(CreditCard creditCard) throws DataAccessException {
		getHibernateTemplate().save(creditCard);
		
	}

	public void createOrUpdate(CreditCard creditCard) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(creditCard);
		
	}

	public List getListCreditCardByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
DetachedCriteria criteria = DetachedCriteria.forClass(CreditCard.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowCreditCardByUpdDttmGtPubDttm() throws DataAccessException {
DetachedCriteria criteria = DetachedCriteria.forClass(CreditCard.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(CreditCard creditCard) throws DataAccessException {
		getHibernateTemplate().update(creditCard);
		
	}
	public List getCreditCardRangeShowOnTicket(String isShowOnTicket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CreditCardRange.class);
		
		criteria.add(Restrictions.eq("isShowOnTicket", isShowOnTicket));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}	
}
