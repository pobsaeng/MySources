package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.BlockCodeDao;
import com.ie.icon.domain.mch.BlockCode;

public class HibernateBlockCodeDao extends HibernateCommonDao implements
		BlockCodeDao {

	public void create(BlockCode blockCode) throws DataAccessException {
		getHibernateTemplate().save(blockCode);
	}

	public void update(BlockCode blockCode) throws DataAccessException {
		getHibernateTemplate().update(blockCode);
	}

	public void delete(BlockCode blockCode) throws DataAccessException {
		getHibernateTemplate().delete(blockCode);
	}

	public BlockCode getBlockCode(String blockCodeId)
			throws DataAccessException {
		return (BlockCode)getHibernateTemplate().get(BlockCode.class, blockCodeId);
	}

	public List getBlockCodes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(BlockCode.class);
        criteria.addOrder(Order.asc("blockCodeId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.BlockCodeDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BlockCode.class);
        
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.BlockCodeDao#createOrUpdate(com.ie.icon.domain.mch.BlockCode)
	 */
	public void createOrUpdate(BlockCode blockCode) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(blockCode);

	}
	public List getBlockCodeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BlockCode.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
	public int getRowBlockCodeByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(BlockCode.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

}
