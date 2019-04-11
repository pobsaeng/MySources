package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.UserDao;
import com.ie.icon.domain.authentication.User;
import com.ie.icon.domain.authorization.UserRole;


public class HibernateUserDao extends HibernateCommonDao implements UserDao {

	/* (non-Javadoc)
     * @see com.ie.icon.dao.UserDao#create(com.ie.icon.domain.authentication.User)
     */
    public void create(User user) {
        getHibernateTemplate().save(user);
    }

    public void update(User user) {
        getHibernateTemplate().update(user);
    }
    
    /* (non-Javadoc)
     * @see com.ie.icon.dao.UserDao#getByUserId(java.lang.String)
     */
    public User getByUserId(String userId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        
        criteria.add(Restrictions.eq("userId", userId));
        
        List list = getHibernateTemplate().findByCriteria(criteria);
        
        if ( list.size() == 1 )
            return (User)list.get(0);
        else
            return null;
    }
    
    public String getSessionId(String userId) {
    	String sql = "select user.sessionId from User user where user.userId = :userId";
    	List ret = getHibernateTemplate().findByNamedParam(sql, "userId", userId);
    	
    	if ( ret.size() == 1 ) 
    		return (String)ret.get(0);
		else
			return null;
    }
    public List getUsersByStatus(char status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		
		criteria.add(Restrictions.eq("status", new Character(status)));
		criteria.addOrder(Order.asc("userId"));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

    public List getUsersByStatus(char status, boolean isWebUser) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		
		criteria.add(Restrictions.eq("status", new Character(status)));
		
		DetachedCriteria criUserRole = criteria.createCriteria("userRole");
		criUserRole.add( Restrictions.eq("isWebAccessProfile", new Boolean(isWebUser) ));
		
		criteria.addOrder(Order.asc("userId"));

		return getHibernateTemplate().findByCriteria(criteria);
    	
    }

	public List getUsers(String userId, String userName, UserRole userRole) {
        String tmpUserId = replace(userId);
        String tmpUserName = replace(userName);
        
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        
        if ( userId != null && !"".equals(userId) )
            criteria.add(Restrictions.like("userId", tmpUserId));
        if ( userName != null && !"".equals(userName) )
            criteria.add(Restrictions.like("name", tmpUserName));
        if ( userRole != null ) {
            DetachedCriteria userRoleCri = criteria.createCriteria("userRole");
            userRoleCri.add(Restrictions.eq("objectId", new Long(userRole.getObjectId())));
        }
        
        criteria.addOrder(Order.asc("userId"));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }

	public List getUsersByCashierSessionStatus(char status) {
		String stringQuery = "select user from User user left join fetch user.userRole, CashierSession session " +
				"where user.userId = session.userId and " +
				"session.status = :status";
		
		return getHibernateTemplate().findByNamedParam(stringQuery, "status", new Character(status));
			
	}

	public List getUsers() {
		return getHibernateTemplate().loadAll(User.class);
	}
	
	public List getUsersPOS() {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.createAlias("userRole", "userRole");
		String[] userRole = {Constant.UserRoleId.CA_ROLE,Constant.UserRoleId.SUP_CA_ROLE,Constant.UserRoleId.MGR_ROLE,Constant.UserRoleId.SM_ROLE};
		
		criteria.add(Restrictions.in("userRole.code", userRole));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("userId"));   
		return getHibernateTemplate().findByCriteria(criteria);
//		return getHibernateTemplate().loadAll(User.class);
	}
	
	public List getUserByUserRoleCsAndCa() throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.createAlias("userRole", "userRole");
		String[] userRole = {Constant.UserRoleId.CA_ROLE,Constant.UserRoleId.SUP_CA_ROLE,Constant.UserRoleId.CS_ROLE,Constant.UserRoleId.SUP_CS_ROLE};
		
		criteria.add(Restrictions.in("userRole.code", userRole));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("userId"));   
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
}
