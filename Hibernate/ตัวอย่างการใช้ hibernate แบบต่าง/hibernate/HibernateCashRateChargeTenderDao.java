package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.CashRateChargeTenderDao;
import com.ie.icon.domain.Tender;
import com.ie.icon.domain.cashier.CashRateChargeTender;

public class HibernateCashRateChargeTenderDao extends HibernateCommonDao implements CashRateChargeTenderDao{

	public List getCashRateChargeTender(CashRateChargeTender cashRateChargeTenderCriteria) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashRateChargeTender.class);
		
		if(cashRateChargeTenderCriteria!=null){
			if(!("All").equals(cashRateChargeTenderCriteria.getTenderId()))
			{
				if(cashRateChargeTenderCriteria.getTenderId()!=null && !"".equals(cashRateChargeTenderCriteria.getTenderId())){
					criteria.add(Restrictions.eq("tenderId", cashRateChargeTenderCriteria.getTenderId()));
				}
			}
			if(cashRateChargeTenderCriteria.getRateFrom()!=null && !"".equals(cashRateChargeTenderCriteria.getRateFrom())){
				criteria.add(Restrictions.le("rateFrom", cashRateChargeTenderCriteria.getRateFrom()));
			}
			if(cashRateChargeTenderCriteria.getRateTo()!=null && !"".equals(cashRateChargeTenderCriteria.getRateTo())){
				criteria.add(Restrictions.ge("rateTo", cashRateChargeTenderCriteria.getRateTo()));
			}
			if(cashRateChargeTenderCriteria.getRateType()!=null && !"".equals(cashRateChargeTenderCriteria.getRateType())){
				criteria.add(Restrictions.eq("rateType", cashRateChargeTenderCriteria.getRateType()));
			}
			//...Boizz(+)
			criteria.add(Restrictions.eq("isActive", new Boolean(cashRateChargeTenderCriteria.getIsActive())));
		}
		criteria.addOrder(Order.asc("tenderId"));
		criteria.addOrder(Order.desc("lastUpdateDate"));
		
		List temp = getHibernateTemplate().findByCriteria(criteria);
		if(temp != null)
			return temp;
		else
			return null;
	}

	
	public void create(CashRateChargeTender cashRateChargeTender) throws DataAccessException {
		getHibernateTemplate().save(cashRateChargeTender);
	}
	
	
	public void updateCashRateChargeTender(CashRateChargeTender cashRateChargeTender)throws Exception{
		getHibernateTemplate().update(cashRateChargeTender);
	}
	
	/* (non-Javadoc)
     * @see com.ie.icon.dao.CashRateChargeTenderDao#getRowByUpdDttmGtPubDttm()
     */
    public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(CashRateChargeTender.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
    }
    
    /* (non-Javadoc)
	 * @see com.ie.icon.dao.CashRateChargeTenderDao#getCashRateChargeTenderByUpdDttmLtPubDttm()
	 */
	public List getCashRateChargeTenderByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashRateChargeTender.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
	public void saveOrUpdate(CashRateChargeTender cashRateChargeTender) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(cashRateChargeTender);
	}
	
	
		
	
}