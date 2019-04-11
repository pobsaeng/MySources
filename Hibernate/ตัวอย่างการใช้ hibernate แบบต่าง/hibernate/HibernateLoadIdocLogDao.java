package com.ie.icon.dao.hibernate;

import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.LoadIdocLogDao;
import com.ie.icon.domain.log.LoadIdocLog;

public class HibernateLoadIdocLogDao  extends HibernateCommonDao implements LoadIdocLogDao{

	public void create(LoadIdocLog log) throws DataAccessException {
		getHibernateTemplate().save(log);
	}

	public void delete(LoadIdocLog log) throws DataAccessException {
		getHibernateTemplate().delete(log);
	}

	public void update(LoadIdocLog log) throws DataAccessException {
		getHibernateTemplate().update(log);
	}

}
