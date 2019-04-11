package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.PasswordPolicyDao;
import com.ie.icon.domain.authentication.PasswordPolicy;


public class HibernatePasswordPolicyDao extends HibernateCommonDao implements
        PasswordPolicyDao {

    /* (non-Javadoc)
     * @see com.ie.icon.dao.PasswordPolicyDao#update(com.ie.icon.domain.authentication.PasswordPolicy)
     */
    public void update(PasswordPolicy passwordPolicy) {
        getHibernateTemplate().update(passwordPolicy);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.PasswordPolicyDao#getPasswordPolicy()
     */
    public PasswordPolicy getPasswordPolicy() {
        DetachedCriteria criteria = DetachedCriteria.forClass(PasswordPolicy.class);
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return (PasswordPolicy)result.get(0);
    }

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PasswordPolicyDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PasswordPolicy.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PasswordPolicyDao#getPasswordPolicyByUpdDttmGtPubDttm(int, int)
	 */
	public List getPasswordPolicyByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(PasswordPolicy.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.PasswordPolicyDao#createOrUpdate(com.ie.icon.domain.authentication.PasswordPolicy)
	 */
	public void createOrUpdate(PasswordPolicy passwordPolicy)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(passwordPolicy);

	}
}
