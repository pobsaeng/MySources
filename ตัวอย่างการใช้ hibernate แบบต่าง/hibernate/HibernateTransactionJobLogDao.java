package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.TransactionJobLogDao;
import com.ie.icon.domain.log.TransactionJobLog;


public class HibernateTransactionJobLogDao extends HibernateCommonDao implements TransactionJobLogDao {

	public void create(TransactionJobLog log) throws DataAccessException {
		getHibernateTemplate().save(log);
		
	}

	public void delete(TransactionJobLog log) throws DataAccessException {
		getHibernateTemplate().delete(log);
		
	}
	
	public void saveOrUpdate(TransactionJobLog log) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(log);
		
	}

	public TransactionJobLog getTransactionJobLogByTrnDtJobNm(Date trnDate, String jobName) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(TransactionJobLog.class);
		
		criteria.add(Restrictions.eq("TransactionJobLogId.transactionDateId", trnDate));
		criteria.add(Restrictions.eq("TransactionJobLogId.jobNameId", jobName));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (TransactionJobLog)result.get(0);
	}

	public void update(TransactionJobLog log) throws DataAccessException {
		getHibernateTemplate().update(log);
		
	}

}
