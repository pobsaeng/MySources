package com.ie.icon.dao.hibernate;

import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SalesDocumentSetItemDao;
import com.ie.icon.domain.sale.SalesDocumentSetItem;

public class HibernateSalesDocumentSetItemDao extends HibernateCommonDao implements SalesDocumentSetItemDao{
	public void create(SalesDocumentSetItem salesdocsetitem){
		try {
			getHibernateTemplate().save(salesdocsetitem);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
}
