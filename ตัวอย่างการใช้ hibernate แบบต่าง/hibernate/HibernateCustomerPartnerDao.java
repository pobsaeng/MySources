/*
 * Created on Sep 25, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import com.ie.icon.dao.CustomerPartnerDao;
import com.ie.icon.domain.customer.CustomerPartner;

/**
 * @author visawee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateCustomerPartnerDao extends HibernateCommonDao implements
		CustomerPartnerDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.CustomerPartnerDao#create(com.ie.icon.domain.customer.CustomerPartner)
	 */
	public void create(CustomerPartner customerPartner) {
		// TODO Auto-generated method stub
	       getHibernateTemplate().save(customerPartner);
	}

    public void update(CustomerPartner customerPartner) {
        getHibernateTemplate().update(customerPartner);

    }
    
    public void createOrUpdate(CustomerPartner customerPartner) {
        getHibernateTemplate().saveOrUpdate(customerPartner);
    }
}
