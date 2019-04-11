package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.PasswordHistoryDao;
import com.ie.icon.domain.authentication.PasswordHistory;


public class HibernatePasswordHistoryDao extends HibernateCommonDao implements
        PasswordHistoryDao {

    /* (non-Javadoc)
     * @see com.ie.icon.dao.PasswordHistoryDao#create(com.ie.icon.domain.authentication.PasswordHistory)
     */
    public void create(PasswordHistory passwordHistory) {
        getHibernateTemplate().save(passwordHistory);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.PasswordHistoryDao#upadate(com.ie.icon.domain.authentication.PasswordHistory)
     */
    public void upadate(PasswordHistory passwordHistory) {
        getHibernateTemplate().update(passwordHistory);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.PasswordHistoryDao#delete(com.ie.icon.domain.authentication.PasswordHistory)
     */
    public void delete(PasswordHistory passwordHistory) {
        getHibernateTemplate().delete(passwordHistory);

    }

    /* (non-Javadoc)
     * @see com.ie.icon.dao.PasswordHistoryDao#getByUserId(java.lang.String)
     */
    public List getLastestByUserId(String userId, int num) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PasswordHistory.class);
        
        DetachedCriteria userCri = criteria.createCriteria("user");
        userCri.add(Restrictions.eq("userId", userId));
        
        criteria.addOrder(Order.desc("startDate"));
        
        return getHibernateTemplate().findByCriteria(criteria, 0, num);
    }

}
