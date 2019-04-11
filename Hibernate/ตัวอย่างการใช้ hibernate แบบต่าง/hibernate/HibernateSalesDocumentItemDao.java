package com.ie.icon.dao.hibernate;

import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SalesDocumentItemDao;
import com.ie.icon.domain.sale.SalesDocumentItem;

public class HibernateSalesDocumentItemDao extends HibernateCommonDao implements SalesDocumentItemDao{
	public void update(SalesDocumentItem salesdocItem) throws DataAccessException {		
		try {
			getHibernateTemplate().update(salesdocItem);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}		
	}   
}
