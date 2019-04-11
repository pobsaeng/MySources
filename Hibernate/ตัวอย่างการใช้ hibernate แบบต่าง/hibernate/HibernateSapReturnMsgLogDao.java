package com.ie.icon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SapReturnMsgLogDao;
import com.ie.icon.domain.so.SapReturnMsgLog;

public class HibernateSapReturnMsgLogDao  extends HibernateCommonDao implements SapReturnMsgLogDao{
	
	public void create(SapReturnMsgLog sapReturnMsgLog) throws DataAccessException{
		getHibernateTemplate().save(sapReturnMsgLog);
	}

	public List getSapReturnMsgLogByTranNoTranDate(String tranNo , Date tranDate){
		DetachedCriteria criteria = DetachedCriteria.forClass(SapReturnMsgLog.class);
		criteria.add(Restrictions.eq("tranDate", tranDate));
		criteria.add(Restrictions.eq("tranNo", tranNo.trim()));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
}
