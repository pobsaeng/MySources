package com.ie.icon.dao.hibernate;

import org.springframework.dao.DataAccessException;
import com.ie.icon.dao.MessageSalesTranLogDao;
import com.ie.icon.domain.log.MessageSalesTranLog;

public class HibernateMessageSalesTranLogDao extends HibernateCommonDao implements MessageSalesTranLogDao{
	public void create(MessageSalesTranLog log) throws DataAccessException {
		
		if(log.getException1()!=null && !"".equals(log.getException1())){
			 if(log.getException1().length() > 4000) {
				 log.setException1(log.getException1().substring(0,3999));
			 }
		}
		getHibernateTemplate().saveOrUpdate(log);
	}
}
