package com.ie.icon.dao.hibernate;

import com.ie.icon.dao.InterfaceErrorLogDao;
import com.ie.icon.domain.log.InterfaceErrorLog;

public class HibernateInterfaceErrorLogDao extends HibernateCommonDao implements InterfaceErrorLogDao {
	public void create(InterfaceErrorLog interfaceErrorLog) {
		getHibernateTemplate().save(interfaceErrorLog);
	}
}
