/*
 * Created on Sep 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.AddressTypeDao;
import com.ie.icon.domain.customer.AddressType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author visawee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateAddressTypeDao extends HibernateCommonDao implements
		AddressTypeDao {

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.AddressTypeDao#getAllAddressTypes()
	 */
	public List getAllAddressTypes() {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(AddressType.class);
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.AddressTypeDao#getAddressTypeByID(java.lang.String)
	 */
	public AddressType getAddressTypeByID(String addressTypeID) {
		// TODO Auto-generated method stub
		try {
			Logger.getLogger(" ").log(Level.WARNING,"AddressType ID="+addressTypeID);
			DetachedCriteria criteria = DetachedCriteria.forClass(AddressType.class);

			criteria.add(Restrictions.eq("addressTypeId",addressTypeID));

			List addressTypeList = getHibernateTemplate().findByCriteria(criteria);
			
			if (addressTypeList.size() == 1) {
				return (AddressType) addressTypeList.get(0);
			} else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}


}
