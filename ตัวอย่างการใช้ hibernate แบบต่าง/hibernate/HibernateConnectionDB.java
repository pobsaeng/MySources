package com.ie.icon.dao.hibernate;

import com.ie.icon.dao.ConnectionDB;

public class HibernateConnectionDB extends HibernateCommonDao implements
		ConnectionDB {

	public boolean isConnect() {
		return isDatabaseConnected();
	}

}
