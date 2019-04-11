package com.ie.icon.dao.hibernate;

import com.ie.icon.dao.TempZSOListDao;
import com.ie.icon.domain.TempZSOList;

public class HibernateTempZSOListDao extends HibernateCommonDao implements TempZSOListDao{

	public void create(TempZSOList tempZSOList) {
		getHibernateTemplate().save(tempZSOList);
		
	}

	public void delete(TempZSOList tempZSOList) {
		getHibernateTemplate().delete(tempZSOList);
		
	}

	public void update(TempZSOList tempZSOList) {
		getHibernateTemplate().update(tempZSOList);
		
	}


}
