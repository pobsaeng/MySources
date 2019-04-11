/*
 * Created on Aug 24, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.dao.ServletSequenceDao;

/**
 * @author amaritk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JdbcServletSequenceDao extends JdbcDaoSupport implements ServletSequenceDao{

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.ServletSequenceDao#getNextValue(java.lang.String)
	 */
	public long getNextValue(String sequenceName) throws Exception {
		String queryString = "select "+sequenceName+".NEXTVAL from dual ";
		JdbcTemplate jt = getJdbcTemplate();
		
		return jt.queryForLong(queryString);
	}
}
