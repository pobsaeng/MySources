package com.ie.icon.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.monitor.Constant;
import com.ie.icon.dao.MessageLogPublishDao;
import com.ie.icon.domain.log.MessageLogPublish;

public class HibernateMessageLogPublishDao extends HibernateCommonDao implements
		MessageLogPublishDao {

	public Long create(MessageLogPublish obj) {
		// TODO Auto-generated method stub
		return (Long)getHibernateTemplate().save(obj);
	}

	public void update(MessageLogPublish obj) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(obj);
	}
	
	public void delete(MessageLogPublish obj) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(obj);
	}

	public MessageLogPublish getMessageLogPublishByOID(String oid) {
		// TODO Auto-generated method stub
		return (MessageLogPublish)getHibernateTemplate().get(MessageLogPublish.class, oid);
	}

	public List getMessageLogPublishList(MessageLogPublish obj, Order[] orderArr) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(MessageLogPublish.class);
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
			
			if(orderArr!=null && orderArr.length>0){
				for(int i=0;i<orderArr.length;i++){
					criteria.addOrder(orderArr[i]);
				}
			}
			
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}
	

	public List getCustInfMsgLogPublishGroupByType(String custInfType, Date startDate, Date endDate) throws DataAccessException{
		List params = new ArrayList();
//		List paramNames = new ArrayList();
		StringBuffer sql = new StringBuffer();
//		sql.append(" with view_log_temp as ( select ci.cust_inf_typ, l.* ");
//		sql.append(" from msg_log_publish l, cust_inf ci ");
//		sql.append(" where l.ref_pub_id = ci.ref_pub_id ");
//		sql.append(" and l.msg_typ = 'CustDTMPub'  ");
//		if(custInfType!=null && !"".equalsIgnoreCase(custInfType.trim())){
//			sql.append(" and ci.cust_inf_typ = ? ");
//			params.add(custInfType);
////			paramNames.add("custInfTyp");
//		}
//		if(startDate != null){
//			sql.append(" and trunc(l.create_dttm) >= trunc(?) ");
//			params.add(startDate);
////			paramNames.add("startDate");
//		}
//		if(endDate != null){
//			sql.append(" and trunc(l.create_dttm) <= trunc(?) ");
//			params.add(endDate);
////			paramNames.add("endDate");
//		}
//		sql.append(" ) ");
//		sql.append(" , view_log as ( ");
//		sql.append(" select l.cust_inf_typ, trunc(l.create_dttm) creat_date, l.msg_sts ");
//		sql.append(" from view_log_temp l ");
//		sql.append(" where l.ref_pub_id = (select max(x.ref_pub_id) ");
//		sql.append(" from view_log_temp x ");
//		sql.append(" where x.ref_1 = l.ref_1 ");
//		sql.append(" and trunc(x.create_dttm) = trunc(l.create_dttm) ");
//		sql.append(" and x.msg_typ = l.msg_typ ");
//		sql.append(" and x.cust_inf_typ = l.cust_inf_typ ) ) ");
//		sql.append(" select temp.type_name, temp.creat_date, temp.group_status ");
//		sql.append(" from ( ");
//		sql.append(" select decode(vl.cust_inf_typ,'B','text.customer_cancel.block_type_1','I','text.customer_cancel.block_type_2','O','text.customer_cancel.block_type_3','U','text.edit_uid') type_name ");
//		sql.append(" , vl.creat_date ");
//		sql.append(" , vl.msg_sts ");
//		sql.append(" ,case when exists(select 1 from view_log t1 where t1.cust_inf_typ = vl.cust_inf_typ and t1.creat_date = vl.creat_date and t1.msg_sts = 'F') then 'F' ");
//		sql.append(" when exists(select 1 from view_log t1 where t1.cust_inf_typ = vl.cust_inf_typ and t1.creat_date = vl.creat_date and t1.msg_sts = 'W') then 'W' ");
//		sql.append(" else 'S' end group_status ");
//		sql.append(" from view_log vl ) temp ");
//		sql.append(" group by temp.type_name, temp.creat_date, temp.group_status ");
//		sql.append(" order by temp.creat_date desc, temp.type_name ");
		
		sql.append(" select decode(ci.cust_inf_typ,'B','text.customer_cancel.block_type_1','I','text.customer_cancel.block_type_2','O','text.customer_cancel.block_type_3','U','text.edit_uid') type_name   ");
		sql.append(" , trunc(l.create_dttm) creat_date ");
		sql.append(" , case when count(case when l.msg_sts = 'F' then 1 else null end) > 0 then 'F' ");
		sql.append(" when count(case when l.msg_sts = 'W' then 1 else null end) > 0 then 'W' ");
		sql.append(" else 'S' end group_status ");
		sql.append(" , ci.cust_inf_typ ");
		sql.append(" from msg_log_publish l, cust_inf ci ");
		sql.append(" where l.ref_pub_id = ci.ref_pub_id  ");
		sql.append(" and l.msg_typ = 'CustDTMPub' ");
		sql.append(" and l.store_id = ?");
		params.add(Constant.Store.SAP);
		if(custInfType!=null && !"".equalsIgnoreCase(custInfType.trim())){
			sql.append(" and ci.cust_inf_typ = ? ");
			params.add(custInfType);
			System.out.println("Param custInfType : " + custInfType);
		}
		if(startDate != null){
			sql.append(" and trunc(l.create_dttm) >= trunc(?) ");
			params.add(startDate);
			System.out.println("Param startDate : " + startDate);
		}
		if(endDate != null){
			sql.append(" and trunc(l.create_dttm) <= trunc(?) ");
			params.add(endDate);
			System.out.println("Param endDate : " + endDate);
		}
		sql.append(" and l.ref_pub_id = (select max(x.ref_pub_id)  from msg_log_publish x, cust_inf y  where x.ref_pub_id = y.ref_pub_id  and x.ref_1 = l.ref_1  and trunc(x.create_dttm) = trunc(l.create_dttm)  and x.msg_typ = l.msg_typ  and y.cust_inf_typ = ci.cust_inf_typ ) ");
		sql.append(" group by ci.cust_inf_typ, trunc(l.create_dttm) ");
		sql.append(" order by trunc(l.create_dttm), ci.cust_inf_typ ");
		
		
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.addScalar("type_name", Hibernate.STRING);
		sqlQuery.addScalar("creat_date", Hibernate.TIMESTAMP);
		sqlQuery.addScalar("group_status", Hibernate.STRING);
		sqlQuery.addScalar("cust_inf_typ", Hibernate.STRING);
		for(int i=0;i<params.size();i++){
			sqlQuery.setParameter(i, params.get(i));
		}
		
		return sqlQuery.list();
	}
	

	public List getCustInfMsgLogPublishGroupStore(String custInfType, Date createdDate) throws DataAccessException{
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select l.store_id ");
		sql.append(" , count(1) c_all ");
		sql.append(" , count(case when l.msg_sts = 'S' then 1 else null end) c_success  ");
		sql.append(" from msg_log_publish l, cust_inf ci ");
		sql.append(" where l.ref_pub_id = ci.ref_pub_id  ");
		sql.append(" and l.msg_typ = 'CustDTMPub'   ");
		sql.append(" and ci.cust_inf_typ = ? ");
		params.add(custInfType);
		sql.append(" and trunc(l.create_dttm) = trunc(?) ");
		params.add(createdDate);
		sql.append(" and l.ref_pub_id = (select max(x.ref_pub_id)  from msg_log_publish x, cust_inf y  where x.ref_pub_id = y.ref_pub_id  and x.ref_1 = l.ref_1  and trunc(x.create_dttm) = trunc(l.create_dttm)  and x.msg_typ = l.msg_typ  and y.cust_inf_typ = ci.cust_inf_typ ) ");
		sql.append(" group by l.store_id ");
		sql.append(" order by l.store_id ");

		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.addScalar("store_id", Hibernate.STRING);
		sqlQuery.addScalar("c_all", Hibernate.INTEGER);
		sqlQuery.addScalar("c_success", Hibernate.INTEGER);
		for(int i=0;i<params.size();i++){
			sqlQuery.setParameter(i, params.get(i));
		}
		
		return sqlQuery.list();
	}

	public List getCustInfMsgLogPublish(String custInfType, Date createdDate, String storeId, String ref1, String ref2, String status) throws DataAccessException{
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select l.ref_1, l.ref_2, l.ref_pub_id, l.msg_sts  ");
		sql.append(" from  msg_log_publish l, cust_inf ci  ");
		sql.append(" where l.ref_pub_id = ci.ref_pub_id  ");
		sql.append(" and l.msg_typ = 'CustDTMPub'    ");
		if(custInfType!=null && !"".equalsIgnoreCase(custInfType.trim())){
			sql.append(" and ci.cust_inf_typ = ?  ");
			params.add(custInfType.trim());
		}
		if(createdDate!=null){
			sql.append(" and trunc(l.create_dttm) = trunc(?)  ");
			params.add(createdDate);
		}
		if(storeId!=null && !"".equalsIgnoreCase(storeId.trim())){
			sql.append(" and l.store_id = ?  ");
			params.add(storeId.trim());
		}
		if(ref1!=null && !"".equalsIgnoreCase(ref1.trim())){
			sql.append(" and l.ref_1 = ?  ");
			params.add(ref1.trim());
		}
		if(ref2!=null && !"".equalsIgnoreCase(ref2.trim())){
			sql.append(" and l.ref_2 like ?  ");
			params.add("%" + ref2.trim() + "%");
		}
		if(status!=null && !"".equalsIgnoreCase(status.trim())){
			sql.append(" and l.msg_sts = ?  ");
			params.add(status.trim());
		}
		sql.append(" and l.ref_pub_id = (select max(x.ref_pub_id)  from msg_log_publish x, cust_inf y  where x.ref_pub_id = y.ref_pub_id  and x.ref_1 = l.ref_1  and trunc(x.create_dttm) = trunc(l.create_dttm)  and x.msg_typ = l.msg_typ  and y.cust_inf_typ = ci.cust_inf_typ ) ");
		sql.append(" order by decode(l.msg_sts, 'F', '0', 'W', '1', '2') asc,l.ref_1 asc ");
		
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.addScalar("ref_1", Hibernate.STRING);
		sqlQuery.addScalar("ref_2", Hibernate.STRING);
		sqlQuery.addScalar("ref_pub_id", Hibernate.LONG);
		sqlQuery.addScalar("msg_sts", Hibernate.STRING);
		for(int i=0;i<params.size();i++){
			sqlQuery.setParameter(i, params.get(i));
		}
		
		return sqlQuery.list();
	}
	
	public List getCountCustInfMsgLogPublish(String custInfType, Date createdDate, String storeId) throws DataAccessException{
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select count(1) c_all  ");
		sql.append(" , count(case when l.msg_sts = 'W' then 1 else null end) c_w  ");
		sql.append(" , count(case when l.msg_sts = 'F' then 1 else null end) c_f  ");
		sql.append(" , count(case when l.msg_sts = 'S' then 1 else null end) c_s  ");
		sql.append(" from  msg_log_publish l, cust_inf ci  ");
		sql.append(" where l.ref_pub_id = ci.ref_pub_id  ");
		sql.append(" and l.msg_typ = 'CustDTMPub'    ");
		if(custInfType!=null && !"".equalsIgnoreCase(custInfType.trim())){
			sql.append(" and ci.cust_inf_typ = ?  ");
			params.add(custInfType.trim());
		}
		if(createdDate!=null){
			sql.append(" and trunc(l.create_dttm) = trunc(?)  ");
			params.add(createdDate);
		}
		if(storeId!=null && !"".equalsIgnoreCase(storeId.trim())){
			sql.append(" and l.store_id = ?  ");
			params.add(storeId.trim());
		}

		sql.append(" and l.ref_pub_id = (select max(x.ref_pub_id)  from msg_log_publish x, cust_inf y  where x.ref_pub_id = y.ref_pub_id  and x.ref_1 = l.ref_1  and trunc(x.create_dttm) = trunc(l.create_dttm)  and x.msg_typ = l.msg_typ  and y.cust_inf_typ = ci.cust_inf_typ ) ");

		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.addScalar("c_all", Hibernate.INTEGER);
		sqlQuery.addScalar("c_w", Hibernate.INTEGER);
		sqlQuery.addScalar("c_f", Hibernate.INTEGER);
		sqlQuery.addScalar("c_s", Hibernate.INTEGER);
		for(int i=0;i<params.size();i++){
			sqlQuery.setParameter(i, params.get(i));
		}
		
		return sqlQuery.list();
	}
}
