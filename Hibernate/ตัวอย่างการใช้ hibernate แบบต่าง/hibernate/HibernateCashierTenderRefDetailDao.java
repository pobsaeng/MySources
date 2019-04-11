package com.ie.icon.dao.hibernate;

import com.ie.icon.dao.CashierTenderRefDetailDao;
import com.ie.icon.domain.cashier.CashierTenderRefDetail;

public class HibernateCashierTenderRefDetailDao extends HibernateCommonDao implements CashierTenderRefDetailDao {
	public void create(CashierTenderRefDetail cashierTenderRefDetail) throws Exception {
		getHibernateTemplate().saveOrUpdate(cashierTenderRefDetail);
	}
}
