package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.UserRoleDao;
import com.ie.icon.domain.authorization.UserRole;


public class HibernateUserRoleDao extends HibernateCommonDao implements
        UserRoleDao {

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserRoleDao#create(com.ie.icon.domain.authorization.UserRole)
     */
    public void create(UserRole userRole) {
        getHibernateTemplate().save(userRole);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserRoleDao#update(com.ie.icon.domain.authorization.UserRole)
     */
    public void update(UserRole userRole) {
        getHibernateTemplate().update(userRole);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserRoleDao#delete(com.ie.icon.domain.authorization.UserRole)
     */
    public void delete(UserRole userRole) {
        getHibernateTemplate().delete(userRole);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserRoleDao#getUserRoles()
     */
    public List getUserRoles() {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
        criteria.addOrder(Order.asc("name"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.UserRoleDao#getUserRoleByUserRoleId(java.lang.String)
	 */
	public UserRole getUserRoleByUserRoleId(String userRoleId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
		
		criteria.add(Restrictions.eq("code", userRoleId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (UserRole)result.get(0);
	}
	
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.UserRoleDao#getRowByUpdDttmGtPubDttm()
	 */
	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.UserRoleDao#getUserRoleByUpdDttmGtPubDttm(int, int)
	 */
	public List getUserRoleByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.UserRoleDao#createOrUpdate(com.ie.icon.domain.authorization.UserRole)
	 */
	public void createOrUpdate(UserRole userRole) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(userRole);

	}
}
