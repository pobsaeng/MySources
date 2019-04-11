package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SalesTransactionSetItemDao;
import com.ie.icon.domain.sale.SalesTransactionSetItem;

public class HibernateSalesTransactionSetItemDao extends HibernateCommonDao implements SalesTransactionSetItemDao {
	public List displaySalesTransactionSetItem(long salestransactionitem) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesTransactionSetItem.class);		
		criteria.createAlias("salesTransactionItem", "salesTransactionItem");
		criteria.add(Restrictions.eq("salesTransactionItem.objectId",new Long(salestransactionitem)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
}
