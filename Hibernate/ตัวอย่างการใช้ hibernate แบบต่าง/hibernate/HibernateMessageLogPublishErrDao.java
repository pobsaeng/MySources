package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ie.icon.dao.MessageLogPublishErrDao;
import com.ie.icon.domain.log.MessageLogPublishErr;

public class HibernateMessageLogPublishErrDao extends HibernateCommonDao
		implements MessageLogPublishErrDao {

	public Long create(MessageLogPublishErr obj) {
		// TODO Auto-generated method stub
		return (Long)getHibernateTemplate().save(obj);
	}

	public void update(MessageLogPublishErr obj) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(obj);
	}
	
	public void delete(MessageLogPublishErr obj) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(obj);
	}

	public MessageLogPublishErr getMessageLogPublishErrByOID(String oid) {
		// TODO Auto-generated method stub
		return (MessageLogPublishErr)getHibernateTemplate().get(MessageLogPublishErr.class, oid);
	}

	public List getMessageLogPublishErrList(MessageLogPublishErr obj, Order[] orderArr) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(MessageLogPublishErr.class);
		if(obj!=null){
			if(obj.getMsgTyp()!=null && !"".equalsIgnoreCase(obj.getMsgTyp().trim())){
				criteria.add(Restrictions.eq("msgTyp", obj.getMsgTyp().trim()));
			}
			if(obj.getRefPubId()!=null && !"".equalsIgnoreCase(obj.getRefPubId().trim())){
				criteria.add(Restrictions.eq("refPubId", obj.getRefPubId().trim()));
			}
			if(obj.getRef1()!=null && !"".equalsIgnoreCase(obj.getRef1().trim())){
				criteria.add(Restrictions.eq("ref1", obj.getRef1().trim()));
			}
			if(obj.getRef2()!=null && !"".equalsIgnoreCase(obj.getRef2().trim())){
				criteria.add(Restrictions.eq("ref2", obj.getRef2().trim()));
			}
			if(obj.getRef3()!=null && !"".equalsIgnoreCase(obj.getRef3().trim())){
				criteria.add(Restrictions.eq("ref3", obj.getRef3().trim()));
			}
			if(obj.getRef4()!=null && !"".equalsIgnoreCase(obj.getRef4().trim())){
				criteria.add(Restrictions.eq("ref4", obj.getRef4().trim()));
			}
			if(obj.getCreateDttm()!=null){
				criteria.add(Restrictions.eq("createDttm", obj.getCreateDttm()));
			}
			if(obj.getMsgSts()!=null && !"".equalsIgnoreCase(obj.getMsgSts().trim())){
				criteria.add(Restrictions.eq("msgSts", obj.getMsgSts().trim()));
			}
			if(obj.getDescription()!=null && !"".equalsIgnoreCase(obj.getDescription().trim())){
				criteria.add(Restrictions.eq("description", obj.getDescription().trim()));
			}
			if(obj.getMsgError()!=null && !"".equalsIgnoreCase(obj.getMsgError().trim())){
				criteria.add(Restrictions.like("msgError", "%" + obj.getMsgError().trim() + "%"));
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
