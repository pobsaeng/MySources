package com.ie.icon.dao.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.constant.MessageLogConstant;
import com.ie.icon.dao.MessageLogDao;
import com.ie.icon.domain.log.ErrorMessageLog;
import com.ie.icon.domain.log.MessageLog;
import com.ie.icon.domain.log.WSLog;

public class HibernateMessageLogDao extends HibernateCommonDao implements MessageLogDao {

    public void create(MessageLog log) throws DataAccessException {
        getHibernateTemplate().save(log);
    }

    public void delete(MessageLog log) throws DataAccessException {
        getHibernateTemplate().delete(log);
    }

    public void update(MessageLog log) throws DataAccessException {
        getHibernateTemplate().update(log);
    }
    
    public List getMessageLogByTrnDt(Date trnDate) throws DataAccessException {
    	Date start = null;
    	Date end = null;
    	Calendar c = Calendar.getInstance();
    	
    	c.setTime(trnDate);
    	c.set(Calendar.HOUR, 0);
    	c.set(Calendar.MINUTE, 0);
    	c.set(Calendar.SECOND, 0);
    	c.set(Calendar.AM_PM, Calendar.AM);
    	start = c.getTime();
    	
    	c.set(Calendar.HOUR, 11);
    	c.set(Calendar.MINUTE, 59);
    	c.set(Calendar.SECOND, 59);
    	c.set(Calendar.AM_PM, Calendar.PM);
    	end = c.getTime();
    	
    	String queryString = "select log.storeId, log.messageType, count(*) from " +
    			"MessageLog log where " +
    			"log.messageDateTime > :start and log.messageDateTime < :end " +
    			"group by log.storeId, log.messageType";
    	return getHibernateTemplate().findByNamedParam(queryString, new String[] {"start", "end"}, new Object[] {start, end});
    }
    
    public List getMessageLogByMessageType(String messageType, String storeId, Date trnDate) throws DataAccessException {
    	Date start = null;
    	Date end = null;
    	Calendar c = Calendar.getInstance();
    	
    	c.setTime(trnDate);
    	c.set(Calendar.HOUR, 0);
    	c.set(Calendar.MINUTE, 0);  
    	c.set(Calendar.SECOND, 0);
    	c.set(Calendar.AM_PM, Calendar.AM);
    	start = c.getTime();
    	
    	c.set(Calendar.HOUR, 11);
    	c.set(Calendar.MINUTE, 59);
    	c.set(Calendar.SECOND, 59);
    	c.set(Calendar.AM_PM, Calendar.PM);
    	end = c.getTime();
    	
    	String queryString = "select log.messageDateTime, log.reference1 from " +
				"MessageLog log where " +
				"log.storeId = :storeId and " +
				"log.messageType = :messageType and " +
				"log.messageDateTime > :start and log.messageDateTime < :end ";
    	return getHibernateTemplate().findByNamedParam(queryString, new String[] {"storeId", "messageType", "start", "end"}, new Object[] {storeId, messageType, start, end});
    }   
    
    public List getStoreMsgLog(String storeId,String msgFormat,String msgType,String ref1,String ref2,Date transDate) throws DataAccessException {
    	SimpleDateFormat sfDate = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = sfDate.format(transDate); 
		Date dateUtil = null;
		try {
			dateUtil = sfDate.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

    	String msgClass = MessageLogConstant.MsgClass.OUTB;
    	
    	String queryString = " SELECT log.objectId,log.messageClass,log.storeId ,log.messageFormat , log.messageType ,log.reference1 , log.reference2  ,log.transactionDate,log.messageDateTime " +
    	  " FROM MessageLog log  " +
		  " WHERE log.messageClass = :msgClass " +
		  " AND log.storeId = :storeId " +
		  " AND log.messageFormat = :msgFormat " +
		  " AND log.messageType = :msgType " +
		  " AND log.reference1 = :ref1 " ;
		  if(ref2!=null && !"".equals(ref2) && !"null".equals(ref2)) {
			  queryString+=" AND log.reference2 = :ref2 " ;
		  }
		  queryString+=" AND log.transactionDate = :transDate "+
    	  " order by log.messageDateTime desc"; 

		List result = null;
		if(ref2!=null && !"".equals(ref2) && !"null".equals(ref2)) {
			result =  (List)getHibernateTemplate().findByNamedParam(queryString, new String[] {"msgClass", "storeId", "msgFormat", "msgType","ref1","ref2","transDate"}, 
	    			new Object[] {msgClass, storeId, msgFormat, msgType,ref1,ref2, dateUtil});
		} else {
			result =  (List)getHibernateTemplate().findByNamedParam(queryString, new String[] {"msgClass", "storeId", "msgFormat", "msgType","ref1","transDate"}, 
    			new Object[] {msgClass, storeId, msgFormat, msgType,ref1,dateUtil});
		}
		List msgLogList = new ArrayList(); 
		if(result != null && result.size()>0){
			for(int i = 0; i< result.size(); i++){   
				MessageLog msgLog = new MessageLog();
				Object [] array = (Object[]) result.get(i);
				if(array[0] != null){msgLog.setObjectId(Long.parseLong(array[0].toString()));}
				if(array[1] != null){msgLog.setMessageClass(array[1].toString());}
				if(array[2] != null){msgLog.setStoreId(array[2].toString());}
				if(array[3] != null){msgLog.setMessageFormat(array[3].toString());}
				if(array[4] != null){msgLog.setMessageType(array[4].toString());}		
				if(array[5] != null){msgLog.setReference1(array[5].toString());}	
				if(array[6] != null){msgLog.setReference2(array[6].toString());}	
				if(array[7] != null){
					Object ob7 = array[7];
					Date trnDate = (Date)ob7;
					msgLog.setTransactionDate(trnDate);
				}
				if(array[8] != null){
					Object ob8 = array[8];
					Date trnDate = (Date)ob8;
					msgLog.setMessageDateTime(trnDate);
				}
				msgLogList.add(msgLog);
			}
		}
		return  msgLogList;
    }

	public void createWSLog(WSLog wsLog) {
		if(wsLog.getMessage1()!=null && wsLog.getMessage1().length()>40){
			wsLog.setMessage1(wsLog.getMessage1().substring(0, 39));
		}
		if(wsLog.getXML()!=null && wsLog.getXML().length()>4000){
			wsLog.setXML(wsLog.getXML().substring(0, 3999));
		}
		getHibernateTemplate().save(wsLog);
	}
	
	public void createErrorMessageLog(ErrorMessageLog log) throws DataAccessException {
		if( log.getErrorMessage()!=null && !log.getErrorMessage().equals("")){
			if(log.getErrorMessage().length()>4000){
			   log.setErrorMessage(log.getErrorMessage().substring(0, 3999));
			}
			getHibernateTemplate().save(log);
		}
	}
	
	public ErrorMessageLog getErrorMessageLog(String messageLogId) throws DataAccessException {
		return (ErrorMessageLog)getHibernateTemplate().get(ErrorMessageLog.class, messageLogId);
	}

	public List getErrorMessageLogs(String storeId, String msgFormat, String msgType, String ref1, String ref2, Date transDate) throws DataAccessException {
		SimpleDateFormat sfDate = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = sfDate.format(transDate); 
		Date dateUtil = null;
		try {
			dateUtil = sfDate.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String queryString = " SELECT log.objectId, log.storeId, log.reference1, log.reference2, log.messageFormat, log.messageType, log.errorType, log.messageDateTime, log.transactionDate, log.errorMessage " +
			" FROM ErrorMessageLog log  " +
			" WHERE log.storeId = :storeId " +
			" AND log.messageFormat = :msgFormat " +
			" AND log.messageType = :msgType " +
			" AND log.reference1 = :ref1 " +
			" AND log.reference2 = :ref2 " +
			" AND log.transactionDate = :transDate ";
		
		List result = (List)getHibernateTemplate().findByNamedParam(
				queryString,
				new String[] {"storeId", "msgFormat", "msgType","ref1","ref2","transDate"},
				new Object[] {storeId, msgFormat, msgType,ref1,ref2, dateUtil});
		
		List list = new ArrayList(); 
		if(result != null && result.size()>0){
			for(int i = 0; i< result.size(); i++){   
				ErrorMessageLog errorMessageLog = new ErrorMessageLog();
				Object [] array = (Object[]) result.get(i);
				if(array[0] != null){errorMessageLog.setObjectId(Long.parseLong(array[0].toString()));}
				if(array[1] != null){errorMessageLog.setStoreId(array[1].toString());}
				if(array[2] != null){errorMessageLog.setReference1(array[2].toString());}
				if(array[3] != null){errorMessageLog.setReference2(array[3].toString());}
				if(array[4] != null){errorMessageLog.setMessageFormat(array[4].toString());}
				if(array[5] != null){errorMessageLog.setMessageType(array[5].toString());}
				if(array[6] != null){errorMessageLog.setErrorType(array[6].toString());}
				if(array[7] != null){errorMessageLog.setMessageDateTime((Date)array[7]);}
				if(array[8] != null){errorMessageLog.setTransactionDate((Date)array[8]);}
				if(array[9] != null){errorMessageLog.setErrorMessage(array[9].toString());}
				list.add(errorMessageLog);
			}
		}
		return  list;
	}
	
	public MessageLog getMessageLog(String storeId, String msgType, String ref1, String ref2, Date transDate) throws DataAccessException{
		System.out.println("StoreId: " + storeId + " msgType: " + msgType + " ref1: " + ref1 + "ref2: " + ref2 + "TransDate: " + transDate);
		MessageLog mslog = null;
		char status = 'S';
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MessageLog.class);
		criteria.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.eq("messageType", msgType));
		
		if(ref1 != null && !ref1.equals("")){
			criteria.add(Restrictions.eq("reference1", ref1));
		}
		else{
			criteria.add(Restrictions.isNull("reference1"));
		}
		
		//criteria.add(Restrictions.eq("reference1", ref1));
		if(ref2 != null && !ref2.equals("")){
			criteria.add(Restrictions.eq("reference2", ref2));
		}
		else{
			criteria.add(Restrictions.isNull("reference2"));
		}
		//criteria.add(Restrictions.eq("reference2", ref2));
		
		criteria.add(Restrictions.eq("status", status));
		criteria.add(Restrictions.eq("transactionDate", transDate));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		System.out.println("<< result >>" + result.size());
		if (result != null && result.size() > 0) {
			mslog = (MessageLog) result.get(0);
		}
		
		return mslog;
	}
	
	public MessageLog getMessageLogStore(String storeId, String msgType, String ref1, String ref2, Date transDate) throws DataAccessException{
		System.out.println("StoreId: " + storeId + " msgType: " + msgType + " ref1: " + ref1 + "ref2: " + ref2 + "TransDate: " + transDate);
		MessageLog mslog = null;
		char status = 'S';
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MessageLog.class);
		criteria.add(Restrictions.eq("storeId", storeId));
		criteria.add(Restrictions.eq("messageType", msgType));
		
		if(ref1 != null && !ref1.equals("")){
			criteria.add(Restrictions.eq("reference1", ref1));
		}
		else{
			criteria.add(Restrictions.isNull("reference1"));
		}
		
		//criteria.add(Restrictions.eq("reference1", ref1));
		if(ref2 != null && !ref2.equals("")){
			criteria.add(Restrictions.eq("reference2", ref2));
		}
		else{
			criteria.add(Restrictions.isNull("reference2"));
		}
		//criteria.add(Restrictions.eq("reference2", ref2));
		
		criteria.add(Restrictions.eq("status", status));
		criteria.add(Restrictions.eq("transactionDate", transDate));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		System.out.println("<< result >>" + result.size());
		if (result != null && result.size() > 0) {
			mslog = (MessageLog) result.get(0);
		}
		
		return mslog;
	}
}
