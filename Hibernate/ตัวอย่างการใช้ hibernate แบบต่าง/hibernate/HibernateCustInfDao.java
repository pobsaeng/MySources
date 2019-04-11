package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.CustInfDao;
import com.ie.icon.domain.customer.CustInf;

public class HibernateCustInfDao extends HibernateCommonDao implements
		CustInfDao {

	public void create(CustInf custInf) {
		// TODO Auto-generated method stub
		getHibernateTemplate().save(custInf);
	}
	
	public void update(CustInf custInf) {
		// TODO Auto-generated method stub
        getHibernateTemplate().update(custInf);
	}

	public void delete(CustInf custInf) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(custInf);
	}

	public CustInf getCustInfByOID(String custInfOID) {
		// TODO Auto-generated method stub
		return (CustInf)getHibernateTemplate().get(CustInf.class, custInfOID);
	}

	public List getCustInfList(CustInf custInf, Order[] orderArr) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(CustInf.class);
		if(custInf!=null){
			if(custInf.getCustInfTyp()!=null && !"".equalsIgnoreCase(custInf.getCustInfTyp().trim())){
				criteria.add(Restrictions.eq("custInfTyp", custInf.getCustInfTyp().trim()));
			}
			if(custInf.getCustInfDate()!=null){
				criteria.add(Restrictions.eq("custInfDate", custInf.getCustInfDate()));
			}
			if(custInf.getCustNo()!=null && !"".equalsIgnoreCase(custInf.getCustNo().trim())){
				criteria.add(Restrictions.eq("custNo", custInf.getCustNo().trim()));
			}
			if(custInf.getCustName()!=null && !"".equalsIgnoreCase(custInf.getCustName().trim())){
				criteria.add(Restrictions.eq("custName", custInf.getCustName().trim()));
			}
			if(custInf.getNewUid()!=null && !"".equalsIgnoreCase(custInf.getNewUid().trim())){
				criteria.add(Restrictions.eq("newUid", custInf.getNewUid().trim()));
			}
			if(custInf.getCreateUserId()!=null && !"".equalsIgnoreCase(custInf.getCreateUserId().trim())){
				criteria.add(Restrictions.eq("createUserId", custInf.getCreateUserId().trim()));
			}
			if(custInf.getCreateUserNm()!=null && !"".equalsIgnoreCase(custInf.getCreateUserNm().trim())){
				criteria.add(Restrictions.eq("createUserNm", custInf.getCreateUserNm().trim()));
			}
			if(custInf.getRefPubId()!=null && !"".equalsIgnoreCase(custInf.getRefPubId().trim())){
				criteria.add(Restrictions.eq("refPubId", custInf.getRefPubId().trim()));
			}
			
			if(orderArr!=null && orderArr.length>0){
				for(int i=0;i<orderArr.length;i++){
					criteria.addOrder(orderArr[i]);
				}
			}
			
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}


}
