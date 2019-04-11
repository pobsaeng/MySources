/*
 * Created on Sep 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.SmsPrmtnDao;
import com.ie.icon.domain.Dummy;
import com.ie.icon.domain.log.MessageLog;
import com.ie.icon.domain.promotion.MemberCoverage;
import com.ie.icon.domain.promotion.SmsMbrCardTypCvrge;
import com.ie.icon.domain.promotion.SmsPrmtn;
import com.ie.icon.domain.promotion.SmsPrmtnSeq;
import com.ie.icon.domain.promotion.SmsStoreCvrge;
import com.ie.icon.domain.promotion.SmsTrn;
import com.ie.icon.domain.sale.SalesTransaction;

/**
 * @author visawee
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateSmsPrmtnDao extends HibernateCommonDao implements SmsPrmtnDao {

	private SimpleDateFormat sf;

	public HibernateSmsPrmtnDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
	}
	
	public void update(SmsPrmtn smsPrmtn) {
		getHibernateTemplate().update(smsPrmtn);
	}

	private int getSmsPromotionObjectNextVal(){
		Object result = getHibernateTemplate().execute(
			new HibernateCallback() {
				public Object doInHibernate(Session sess)
						throws HibernateException, SQLException {
					SQLQuery query = sess.createSQLQuery("select SMS_PRMTN_SEQ.nextval from dual");
					query.addScalar("nextval",Hibernate.INTEGER);
	
					return query.uniqueResult();
				}
			});
		return Integer.parseInt(result.toString());
	}
	
	private int getSmsPromotionIdNextVal(){
		Object result = getHibernateTemplate().execute(
			new HibernateCallback() {
				public Object doInHibernate(Session sess)
						throws HibernateException, SQLException {
					SQLQuery query = sess.createSQLQuery("select SMS_PRMTN_ID_SEQ.nextval from dual");
					query.addScalar("nextval",Hibernate.INTEGER);
	
					return query.uniqueResult();
				}
			});
		return Integer.parseInt(result.toString());
	}
	
	// create with select seq manual
	public void createSmsPrmtn(SmsPrmtn smsPrmtn,List storeList,List mbrCardCoveragesList) {
		int nextSmsOid = getSmsPromotionObjectNextVal();
		long nextSmsId = getSmsPromotionIdNextVal();
		
		DecimalFormat f = new DecimalFormat("00000");
		
		smsPrmtn.setObjectId(nextSmsOid);
		smsPrmtn.setSmsPrmtnId("SMS"+f.format(nextSmsId));
		getHibernateTemplate().save(smsPrmtn);
		
		Iterator it = storeList.iterator();
		while(it.hasNext()){
			SmsStoreCvrge store = (SmsStoreCvrge)it.next();
		
			store.getId().setSmsPrmtnOid(smsPrmtn.getObjectId());
			store.setIsActive(true);
			getHibernateTemplate().save(store);
		}
		it = mbrCardCoveragesList.iterator();
		while(it.hasNext()){
			MemberCoverage memberCard = (MemberCoverage)it.next();
			
			SmsMbrCardTypCvrge smsMbr = new SmsMbrCardTypCvrge();
			smsMbr.getId().setSmsPrmtnOid(smsPrmtn.getObjectId());
			smsMbr.getId().setCardTypId(memberCard.getMemberCardType().getMemberCardTypeId());
			smsMbr.setIsActive(true);
			getHibernateTemplate().save(smsMbr);
		}
	}
	
	public SmsPrmtn getSmsPrmtnForUpdate(long pk){
		SmsPrmtn findSms = (SmsPrmtn)getHibernateTemplate().get(SmsPrmtn.class, new Long(pk));
		if(findSms!=null){
			getHibernateTemplate().initialize(findSms.getSmsStroeCvrge());
			getHibernateTemplate().initialize(findSms.getSmsMbrCardTypCvrge());
		}
		return findSms;
	}
	
	public SmsPrmtn updateSmsPrmtnWithReActive(SmsPrmtn smsPrmtn,List storeList,List mbrCardCoveragesList) {
		SmsPrmtn updateObject = (SmsPrmtn)getHibernateTemplate().get(SmsPrmtn.class, new Long(smsPrmtn.getObjectId()));
		Iterator iterator = updateObject.getSmsStroeCvrge().iterator();
		while(iterator.hasNext()){
			SmsStoreCvrge smsStore = (SmsStoreCvrge)iterator.next();
			smsStore.setIsActive(false);
		}
		Iterator iterator2 = updateObject.getSmsMbrCardTypCvrge().iterator();
		while(iterator2.hasNext()){
			SmsMbrCardTypCvrge mbrCard = (SmsMbrCardTypCvrge)iterator2.next();
			mbrCard.setIsActive(false);
		}
		updateObject.setIsActive(false);
		updateObject.setLastPubDttm(null);
		updateObject.setLastUpdateDate(new Date());
		updateObject.setLastUpdateUser(smsPrmtn.getCreateUserId());
		getHibernateTemplate().update(updateObject);
		
		SmsPrmtn cloneObject = new SmsPrmtn();
		int nextSmsOid = getSmsPromotionObjectNextVal();
		cloneObject.setObjectId(nextSmsOid);
		cloneObject.setSmsPrmtnId(smsPrmtn.getSmsPrmtnId());
		cloneObject.setSmsPrmtnDesc(smsPrmtn.getSmsPrmtnDesc());
		cloneObject.setIvr(smsPrmtn.getIvr());
		cloneObject.setCashPerSms(smsPrmtn.getCashPerSms());
		cloneObject.setStartDt(smsPrmtn.getStartDt());
		cloneObject.setEndDt(smsPrmtn.getEndDt());
		cloneObject.setSendEndDt(smsPrmtn.getSendEndDt());
		cloneObject.setFooter1(smsPrmtn.getFooter1());
		cloneObject.setFooter2(smsPrmtn.getFooter2());
		cloneObject.setIsActive(true);
		cloneObject.setCreateDttm(new Date());
		cloneObject.setCreateUserId(smsPrmtn.getCreateUserId());
		cloneObject.setStoreCvrge(smsPrmtn.getStoreCvrge());
		
		getHibernateTemplate().save(cloneObject);

		Iterator it = storeList.iterator();
		while(it.hasNext()){
			SmsStoreCvrge store = (SmsStoreCvrge)it.next();
			
			store.getId().setSmsPrmtnOid(cloneObject.getObjectId());
			store.setIsActive(true);
			getHibernateTemplate().merge(store);
		}
		it = mbrCardCoveragesList.iterator();
		while(it.hasNext()){
			MemberCoverage memberCard = (MemberCoverage)it.next();
			
			SmsMbrCardTypCvrge smsMbr = new SmsMbrCardTypCvrge();
			smsMbr.getId().setSmsPrmtnOid(cloneObject.getObjectId());
			smsMbr.getId().setCardTypId(memberCard.getMemberCardType().getMemberCardTypeId());
			smsMbr.setIsActive(true);
			getHibernateTemplate().merge(smsMbr);
		}
		
		return cloneObject;
	
	}
	
	public List getActivePeriod( final Date frmDate, final Date toDate){

		List result = (List) getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session sess)throws HibernateException, SQLException {
						Filter filter = sess.enableFilter("activeStroeCvrgeFilter");
						filter.setParameter("activeParam", new Boolean(true));
						Filter filter2 = sess.enableFilter("activeMbrCardFilter");
						filter2.setParameter("activeParam", new Boolean(true));
						Criteria criteria = sess.createCriteria(SmsPrmtn.class);
						criteria.add(Expression.ge("sendEndDt",frmDate));
						criteria.add(Expression.le("startDt",toDate));
						criteria.add(Restrictions.eq("isActive", new Boolean(true)));
						
						List result = criteria.list();
						if(result.size()==0)
							return null;
						else
						{
							for (int i = 0; i < result.size(); i++) {
								SmsPrmtn smsPrmtn = (SmsPrmtn)result.get(i);
								getHibernateTemplate().initialize(smsPrmtn.getSmsStroeCvrge());
								getHibernateTemplate().initialize(smsPrmtn.getSmsMbrCardTypCvrge());
							}
							return result;
						}
					}
				});
		return result;
	}

	public List getForSearch(String condition, String keyword, Date frmDate, Date toDate, Date sendEndDate, String store,String status) {

		StringBuffer sqlbuffer = new StringBuffer();
		sqlbuffer.append(" select distinct sp.SMS_PRMTN_OID,sp.SMS_PRMTN_ID,sp.SMS_PRMTN_DESC, ");
		sqlbuffer.append(" sp.CASH_PER_SMS,sp.START_DT,sp.END_DT,sp.SEND_END_DT, ");
		sqlbuffer.append(" sp.IS_ACTIVE,sp.STORE_CVRGE ");
		sqlbuffer.append(" ,sp.FOOTER1,sp.FOOTER2,sp.LAST_UPD_DTTM,sp.CREATE_DTTM ");
		sqlbuffer.append(" from SMS_PRMTN sp ");
		sqlbuffer.append(" left join SMS_STORE_CVRGE s ");
		sqlbuffer.append(" on sp.SMS_PRMTN_OID=s.SMS_PRMTN_OID and s.IS_ACTIVE = 'Y' ");
		sqlbuffer.append(" where 1 = 1 ");

		if (status != null && !status.equals("")) {
			sqlbuffer.append(" and sp.IS_ACTIVE = '").append(status).append("' ");
		}
		//sqlbuffer.append(" and s.IS_ACTIVE = 'Y' ");
		keyword = keyword.replaceAll("\\*", "%");
		
		if (condition.equals("CODE")) {
			sqlbuffer.append(" and sp.SMS_PRMTN_ID LIKE '").append(keyword.toUpperCase()).append("' ");
		} else if (condition.equals("DETAIL")) {
			sqlbuffer.append(" and sp.SMS_PRMTN_DESC LIKE '").append(keyword).append("' ");
		}
		
		if (!store.equals("ALL")) {
			sqlbuffer.append(" and (");
			sqlbuffer.append(" s.STORE_ID = '").append(store).append("' or ");
			sqlbuffer.append(" sp.STORE_CVRGE = 'A' ) ");
			sqlbuffer.append(" and sp.STORE_CVRGE != 'E' ");
		}
		if (frmDate != null && toDate != null) {
			// end2 >= start1 and start2 <= end1
			sqlbuffer.append(" and ( to_date('@1','DD/MM/YYYY') <= sp.END_DT ");
			sqlbuffer.append(" and sp.START_DT <= to_date('@2','DD/MM/YYYY') ) ");
		}
		if (sendEndDate != null) {
			sqlbuffer.append(" and sp.SEND_END_DT = to_date('@3','DD/MM/YYYY') ");
		}
		sqlbuffer.append(" order by sp.SMS_PRMTN_ID,sp.LAST_UPD_DTTM ");
		String sql = sqlbuffer.toString();
		if (frmDate != null && toDate != null) {
			sql = sql.replaceAll("@1", sf.format(frmDate));
			sql = sql.replaceAll("@2", sf.format(toDate));
		}
		if (sendEndDate != null) {
			sql = sql.replaceAll("@3", sf.format(sendEndDate));
		}
		final String sqlFinal = sql;
		
		System.out.println(sql);
		List result_list = new ArrayList();

		List result = (List) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session sess)
							throws HibernateException, SQLException {
						SQLQuery query = sess.createSQLQuery(sqlFinal);
						query.addScalar("SMS_PRMTN_OID",Hibernate.LONG);
						query.addScalar("SMS_PRMTN_ID",Hibernate.STRING);
						query.addScalar("SMS_PRMTN_DESC",Hibernate.STRING);
						query.addScalar("CASH_PER_SMS",Hibernate.BIG_DECIMAL);
						query.addScalar("START_DT",Hibernate.DATE);
						query.addScalar("END_DT", Hibernate.DATE);
						query.addScalar("SEND_END_DT", Hibernate.DATE);
						query.addScalar("IS_ACTIVE",Hibernate.YES_NO);
						query.addScalar("STORE_CVRGE",Hibernate.STRING);
						query.addScalar("FOOTER1",Hibernate.STRING);
						query.addScalar("FOOTER2",Hibernate.STRING);
						query.addScalar("LAST_UPD_DTTM",Hibernate.TIMESTAMP);
						query.addScalar("CREATE_DTTM",Hibernate.TIMESTAMP);
						
						return query.list();
					}
				});
		if (result.size() != 0) {
			
			Iterator it = result.iterator();
			
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				int i = 0;
				SmsPrmtn smsPrmtn = new SmsPrmtn();
				smsPrmtn.setObjectId(Long.parseLong(row[i++].toString()));
				smsPrmtn.setSmsPrmtnId(row[i++].toString());
				smsPrmtn.setSmsPrmtnDesc(row[i++].toString());
				smsPrmtn.setCashPerSms(new BigDecimal(row[i++].toString()));
				smsPrmtn.setStartDt((Date) row[i++]);
				smsPrmtn.setEndDt((Date) row[i++]);
				smsPrmtn.setSendEndDt((Date) row[i++]);
				smsPrmtn.setIsActive(new Boolean( row[i++].toString()).booleanValue());
				smsPrmtn.setStoreCvrge( row[i++].toString());
				if(row[i]!=null)
					smsPrmtn.setFooter1(row[i].toString());
				else
					smsPrmtn.setFooter1("");
				if(row[++i]!=null)
					smsPrmtn.setFooter2(row[i].toString());
				else
					smsPrmtn.setFooter2("");
				
				smsPrmtn.setLastUpdateDate((Date)row[++i]);
				smsPrmtn.setCreateDttm((Date)row[++i]);

				result_list.add(smsPrmtn);
			}
			
		}
		
		if( !store.equals("ALL")){
			List exceptList = getForSearchExcept(condition, keyword, frmDate, toDate, store, status);
			if(exceptList!=null && exceptList.size()>0){
				Iterator exceptIt = exceptList.iterator();
				while(exceptIt.hasNext()){
					SmsPrmtn smsPrmtn = (SmsPrmtn) exceptIt.next();
					List storeList = getStoreByPrmtnOid(smsPrmtn.getObjectId());
					if(storeList!=null && storeList.size()>0){
						Iterator storeIt = storeList.iterator();
						boolean isPass = true;
						while(storeIt.hasNext()){
							SmsStoreCvrge smsStore = (SmsStoreCvrge)storeIt.next();
							if(smsStore.getId().getStoreId().equals(store)){
								isPass = false;
							}
						}
						if(isPass){
							result_list.add(smsPrmtn);
						}
					}
				}
			}
		}
		return result_list;
	}
	
	private List getForSearchExcept(String condition, String keyword, Date frmDate,Date toDate, String store,String status) {
		StringBuffer sqlbuffer = new StringBuffer();
		sqlbuffer.append(" select distinct sp.SMS_PRMTN_OID,sp.SMS_PRMTN_ID,sp.SMS_PRMTN_DESC, ");
		sqlbuffer.append(" sp.CASH_PER_SMS,sp.START_DT,sp.END_DT, ");
		sqlbuffer.append(" sp.IS_ACTIVE,sp.STORE_CVRGE ");
		sqlbuffer.append(" ,sp.FOOTER1,sp.FOOTER2,sp.LAST_UPD_DTTM,sp.CREATE_DTTM ");
		sqlbuffer.append(" from SMS_PRMTN sp ");
		sqlbuffer.append(" left join SMS_STORE_CVRGE s ");
		sqlbuffer.append(" on sp.SMS_PRMTN_OID=s.SMS_PRMTN_OID ");
		sqlbuffer.append(" where 1 = 1 ");

		if (status != null && !status.equals("")) {
			sqlbuffer.append(" and sp.IS_ACTIVE = '").append(status).append("' ");
		}
		
		if (condition.equals("CODE")) {
			sqlbuffer.append(" and sp.SMS_PRMTN_ID = '").append(keyword.toUpperCase()).append("' ");
		} else if (condition.equals("DETAIL")) {
			sqlbuffer.append(" and sp.SMS_PRMTN_DESC = '").append(keyword).append("' ");
		}
		if (!store.equals("ALL")) {
			sqlbuffer.append(" and sp.STORE_CVRGE = 'E' ");
		}
		if (frmDate != null && toDate != null) {
			// end2 >= start1 and start2 <= end1
			sqlbuffer.append(" and ( to_date('@1','DD/MM/YYYY') <= sp.END_DT ");
			sqlbuffer.append(" and sp.START_DT <= to_date('@2','DD/MM/YYYY') ) ");
		}
		sqlbuffer.append(" order by sp.SMS_PRMTN_OID ");
		String sql = sqlbuffer.toString();
		if (frmDate != null && toDate != null) {
			sql = sql.replaceAll("@1", sf.format(frmDate));
			sql = sql.replaceAll("@2", sf.format(toDate));
		}
		final String sqlFinal = sql;
		
		System.out.println(sql);

		List result = (List) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session sess)
							throws HibernateException, SQLException {
						SQLQuery query = sess.createSQLQuery(sqlFinal);
						query.addScalar("SMS_PRMTN_OID",Hibernate.LONG);
						query.addScalar("SMS_PRMTN_ID",Hibernate.STRING);
						query.addScalar("SMS_PRMTN_DESC",Hibernate.STRING);
						query.addScalar("CASH_PER_SMS",Hibernate.BIG_DECIMAL);
						query.addScalar("START_DT",Hibernate.DATE);
						query.addScalar("END_DT", Hibernate.DATE);
						query.addScalar("IS_ACTIVE",Hibernate.YES_NO);
						query.addScalar("STORE_CVRGE",Hibernate.STRING);
						query.addScalar("FOOTER1",Hibernate.STRING);
						query.addScalar("FOOTER2",Hibernate.STRING);
						query.addScalar("LAST_UPD_DTTM",Hibernate.TIMESTAMP);
						query.addScalar("CREATE_DTTM",Hibernate.TIMESTAMP);
						
						return query.list();
					}
				});
		if (result.size() == 0) {
			return null;
		} else {
			List result_list = new ArrayList();
			Iterator it = result.iterator();
			
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				int i = 0;
				SmsPrmtn smsPrmtn = new SmsPrmtn();
				smsPrmtn.setObjectId(Long.parseLong(row[i++].toString()));
				smsPrmtn.setSmsPrmtnId(row[i++].toString());
				smsPrmtn.setSmsPrmtnDesc(row[i++].toString());
				smsPrmtn.setCashPerSms(new BigDecimal(row[i++].toString()));
				smsPrmtn.setStartDt((Date) row[i++]);
				smsPrmtn.setEndDt((Date) row[i++]);
				smsPrmtn.setIsActive(new Boolean( row[i++].toString()).booleanValue());
				smsPrmtn.setStoreCvrge( row[i++].toString());
				if(row[i]!=null)
					smsPrmtn.setFooter1(row[i].toString());
				else
					smsPrmtn.setFooter1("");
				if(row[++i]!=null)
					smsPrmtn.setFooter2(row[i].toString());
				else
					smsPrmtn.setFooter2("");
				
				smsPrmtn.setLastUpdateDate((Date)row[++i]);
				smsPrmtn.setCreateDttm((Date)row[++i]);

				result_list.add(smsPrmtn);
			}
			return result_list;
		}
	}

	public List getStoreByPrmtnOid(long prmtnOid) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SmsStoreCvrge.class);
		criteria.createAlias("smsPrmtn", "sp");
		criteria.add(Restrictions.eq("sp.objectId",new Long(prmtnOid)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			return result;
		}
	}
	
	public SmsPrmtn getSmsPrmtnWithActiveStore(long promotionOid){
		DetachedCriteria criteria = DetachedCriteria.forClass(SmsPrmtn.class);
		criteria.add(Restrictions.eq("objectId", new Long(promotionOid)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() == 0) {
			return null;
		} else {
			SmsPrmtn sms = (SmsPrmtn) result.get(0);
			getHibernateTemplate().initialize(sms.getSmsStroeCvrge());
			getHibernateTemplate().initialize(sms.getSmsMbrCardTypCvrge());
			List removeList = new ArrayList();
			Iterator iterator = sms.getSmsStroeCvrge().iterator();
			while(iterator.hasNext()){
				SmsStoreCvrge smsStore = (SmsStoreCvrge)iterator.next();
				if(!smsStore.getIsActive()){
					removeList.add(smsStore);
				}
			}
			if(removeList.size()!=0){
				sms.getSmsStroeCvrge().removeAll(removeList);
			}
			removeList = new ArrayList();
			iterator = sms.getSmsMbrCardTypCvrge().iterator();
			while(iterator.hasNext()){
				SmsMbrCardTypCvrge member = (SmsMbrCardTypCvrge)iterator.next();
				if(!member.getIsActive()){
					removeList.add(member);
				}
			}
			if(removeList.size()!=0){
				sms.getSmsMbrCardTypCvrge().removeAll(removeList);
			}

			return sms;
		}
	}

	public void createOrUpdate(SmsPrmtn smsPrmtn) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(smsPrmtn);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SmsPrmtn.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getSmsPrmtnByUpdDttmGtPubDttm(int first, int max)throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SmsPrmtn.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPubDttm"), Restrictions.isNull("lastPubDttm")));
		
		List result = getHibernateTemplate().findByCriteria(criteria,first, max);
		if(result.size()==0)
			return null;
		else
		{
			for (int i = 0; i < result.size(); i++) {
				SmsPrmtn sms = (SmsPrmtn)result.get(i);
				getHibernateTemplate().initialize(sms.getSmsStroeCvrge());
				getHibernateTemplate().initialize(sms.getSmsMbrCardTypCvrge());
			}
			return result;
		}
	}

	public void createSmsTrn(SmsTrn smsTrn){
		getHibernateTemplate().save(smsTrn);
	}
	
	public void createSmsPrmtnSeq(SmsPrmtnSeq smsPrmtnSeq){
		getHibernateTemplate().merge(smsPrmtnSeq);
	}
	
	public void getInitSmsTrn(SmsTrn smsTrn){
		DetachedCriteria criteria = DetachedCriteria.forClass(SmsTrn.class);
		criteria.setProjection(Projections.max("objectId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		// set maxid
		long maxId = 1;
		// set nextSeq
		long nextSeq = 1;
		
		if(result.size()!=0 && result.get(0)!=null){
			maxId = Long.parseLong(result.get(0).toString());
		}
		
		Object object = getHibernateTemplate().get(SmsTrn.class,new Long( maxId));
		SmsTrn lastSmsTrn = null;
		if(object!=null)
			lastSmsTrn = (SmsTrn)object;
		
		if(lastSmsTrn!=null){
			SmsPrmtnSeq latSeq = getSmsPrmtnSeqByIdForPos(lastSmsTrn.getSmsPrmtnId());
			if (latSeq != null ) {
				if(lastSmsTrn.getSmsPrmtnId().equals(smsTrn.getSmsPrmtn().getSmsPrmtnId())){
					nextSeq = latSeq.getPrmtnSeq() + 1;
				}
			}
		}
		
		smsTrn.getSmsPrmtnSeq().setSmsPrmtnId(smsTrn.getSmsPrmtn().getSmsPrmtnId());
		smsTrn.getSmsPrmtnSeq().setPrmtnSeq(nextSeq);
	}

	private SmsPrmtnSeq getSmsPrmtnSeqByIdForPos(String smsPrmtnId){
		DetachedCriteria criteria = DetachedCriteria.forClass(SmsPrmtnSeq.class);
		
		criteria.add(Restrictions.eq("smsPrmtnId", smsPrmtnId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result==null || result.size()==0){
			return null;
		}else{
			return (SmsPrmtnSeq)result.get(0);
		}
	}
	
	public SmsTrn getSmsTrn(long oid){
		return (SmsTrn)getHibernateTemplate().get(SmsTrn.class, new Long(oid) );
	}
	
	public SmsTrn getSMS(String storeID, String ticketNo, String posID, Date trnDate) throws DataAccessException{
		System.out.println("<< SMS storeId >>" + storeID);
		System.out.println("<< SMS ticketNo >>" + ticketNo);
		System.out.println("<< SMS posID >>" + posID);
		System.out.println("<< SMS trnDate >>" + trnDate);
		
		SmsTrn sms = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(SmsTrn.class);
		criteria.add(Restrictions.eq("storeId", storeID));
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.eq("posId", posID));
		criteria.add(Restrictions.eq("trnDt", trnDate));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		System.out.println("<< result >>" + result.size());
		if (result != null && result.size() > 0) {
			sms = (SmsTrn) result.get(0);
		}
		return sms;
	}
}
