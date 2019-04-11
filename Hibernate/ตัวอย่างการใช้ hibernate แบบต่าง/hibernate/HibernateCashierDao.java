package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.CashierDao;
import com.ie.icon.domain.cashier.CashMonitor;
import com.ie.icon.domain.cashier.CashierSession;
import com.ie.icon.domain.cashier.CashierTransaction;
import com.ie.icon.domain.cashier.CashierTransactionType;
import com.ie.icon.domain.cashier.ClosingDeclaration;
import com.ie.icon.domain.cashier.ClosingDeclarationDetail;

public class HibernateCashierDao extends HibernateCommonDao implements
		CashierDao {

	public void create(CashierSession session) {
		getHibernateTemplate().save(session);

	}

	public void update(CashierSession session) {
		getHibernateTemplate().update(session);

	}

	public void create(CashierTransaction cashierTransaction) {
		getHibernateTemplate().save(cashierTransaction);

	}

	public void update(CashierTransaction cashierTransaction) {
		getHibernateTemplate().update(cashierTransaction);

	}

	public CashierSession getCashierSessionByUserId(String userId, Date trnDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierSession.class);
		
		criteria.add(Restrictions.eq("userId", userId));
		if (trnDate!=null) {
			criteria.add(Restrictions.eq("transactionDate", trnDate));
		}
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (CashierSession)result.get(0);
	}

	public CashierTransactionType getCashierTransactionType(String typeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierTransactionType.class);
		
		criteria.add(Restrictions.eq("cashierTransactionTypeId", typeId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (CashierTransactionType)result.get(0);
	}

    public List getCashierSessionsByStatus(char status) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CashierSession.class);
        
        criteria.add(Restrictions.eq("status", new Character(status)));
        
        return getHibernateTemplate().findByCriteria(criteria);
    }

	public List getSummaryTender(final char param, final String userId, final Date trnDate) {
		class ReturnValue  {
			List value;
		}		
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				//String queryString = "select new com.ie.icon.domain.sale.SalesSummary(tender.name, sum(cashier.transactionAmount), count(distinct tran.objectId)) from " +
				String queryString = "select new com.ie.icon.domain.sale.SalesSummary(tender.tenderId, sum(cashier.transactionAmount), count(distinct tran.objectId)) from " +
						"CashierTransaction cashier left join " +
						"cashier.salesTransaction tran join " +
						"cashier.tender tender where " +						
						"tran.typeId like ? and " +
						"cashier.userId like ? and " +
						"cashier.transactionDate = ? and " +
						"cashier.cashierType.cashierTransactionTypeId <> '" + Constant.CashierTransactionType.EXCESS + "' " +
						"group by tender.tenderId";
				Query query = session.createQuery(queryString);
				query.setCharacter(0, param);
				query.setString(1, userId);
				query.setDate(2, trnDate);
				rv.value = query.list();
				return null;
			}
		});

		return rv.value;
	}
	public List getSummaryTenderSO_POS(final String userId, final Date trnDate) {
		class ReturnValue  {
			List value;
		}		
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				//String queryString = "select new com.ie.icon.domain.sale.SalesSummary(tender.name, sum(cashier.transactionAmount), count(distinct tran.objectId)) from " +
				String queryString = "select new com.ie.icon.domain.sale.SalesSummary(tender.id, sum(cashier.transactionAmount), count(distinct tran.objectId)) from " +
						"CashierTransaction cashier left join " +
						"cashier.salesTransaction tran join " +
						"cashier.tender tender where " +						
						" ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and " +
						"cashier.userId like ? and " +
						"cashier.transactionDate = ? and " +
						"cashier.cashierType.cashierTransactionTypeId <> '" + Constant.CashierTransactionType.EXCESS + "' " +
						"group by tender.id";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				rv.value = query.list();
				return null;
			}
		});

		return rv.value;
	}
	public List getCashierCash(Date trnDate) {
		String queryString = "select user.userId, user.name, userRole.name, user.posNo, sum(cashier.transactionAmount), profile.limitAmount from " +
				"User user join " +
				"user.userRole userRole join " +
				"userRole.limitProfile.profileLimits profile," +
				"CashierTransaction cashier where " +
				"cashier.tender.tenderId = 'CASH' and " +
				"cashier.userId = user.userId and " +
				"profile.limitType.objectId = 2 and " +
				"cashier.transactionDate = :trnDate " +
				"group by user.userId, user.name, userRole.name, user.posNo, profile.limitAmount";
		
		List list = getHibernateTemplate().findByNamedParam(queryString, "trnDate", trnDate);
		
		List result = new ArrayList();
		for ( int i=0; i<list.size(); i++ ) {
			Object[] object = (Object[])list.get(i);
			CashMonitor cashMonitor = new CashMonitor();
			
			cashMonitor.setUserId(object[0].toString());
			cashMonitor.setUserName(object[1].toString());
			cashMonitor.setUserRole(object[2].toString());
			if ( object[3] != null )
				cashMonitor.setPosId(object[3].toString());
			cashMonitor.setCashOnHand((BigDecimal)object[4]);
			cashMonitor.setPickupLimit((BigDecimal)object[5]);
			
			result.add(cashMonitor);
		}
		
		return result;
	}
	public List getCashierDetail(String userId, Date trnDate) {
	    String queryString = "select cashier.tender.tenderId, cashier.tender.name, cashier.cashierType.name, count(*), sum(cashier.transactionAmount), cashier.cashierType.cashierTransactionTypeId from " +
	    		"User user, " +
	    		"CashierTransaction cashier where " +
	    		"cashier.userId = user.userId and " +
	    		"cashier.userId like :userId and " +
	    		"cashier.transactionDate = :trnDate " +
	    		"group by cashier.tender.tenderId, cashier.tender.tenderNo, cashier.tender.name, cashier.cashierType.name, cashier.cashierType.cashierTransactionTypeId " +
	    		"order by cashier.tender.tenderNo";
		List list = getHibernateTemplate().findByNamedParam(queryString, new String[] { "userId", "trnDate"}, new Object[] {userId, trnDate});

		List result = new ArrayList();
		String tmp = "";
		ClosingDeclaration closing = null;
		for ( int i=0; i<list.size(); i++ ) {
			Object[] obj = (Object[])list.get(i);
			
			if ( !tmp.equals(obj[0].toString()) ) {
				tmp = obj[0].toString();
				closing = new ClosingDeclaration();
				closing.setTenderId(obj[0].toString());
				closing.setTenderName(obj[1].toString());
                result.add(closing);
			}
			
			if ( obj[5].toString().equals(Constant.CashierTransactionType.CLOSE) ) {
				closing.setAmount(((BigDecimal)obj[4]).negate());  // convert to positive value (+).
			} else {
				ClosingDeclarationDetail det = new ClosingDeclarationDetail();
				det.setName(obj[2].toString());
				det.setNum(Integer.parseInt(obj[3].toString()));
				det.setAmount((BigDecimal)obj[4]);				
				closing.getDetails().add(det);
			}
		}
	    
	    return result;
	}	
	public List getCashierBalance(String userId, Date trnDate, boolean isStore) {
	    String queryString = "select cashier.tender.tenderNo, cashier.tender.name, cashier.cashierType.name, count(*), sum(cashier.transactionAmount), cashier.cashierType.cashierTransactionTypeId, cashier.cashierType.sequence ";
	    
	    if ( !isStore )
	    	queryString += ", cashier.userId, user.name ";
	    
	    queryString += "from User user, " +
	    		"CashierTransaction cashier where " +
	    		"cashier.userId = user.userId and " +
	    		"cashier.userId like :userId and " +
	    		"cashier.transactionDate = :trnDate " +
	    		"group by ";
	    if ( !isStore )
	    	queryString += " cashier.userId, user.name, ";
	    queryString += "cashier.tender.tenderNo, cashier.tender.name, cashier.cashierType.name, cashier.cashierType.cashierTransactionTypeId, cashier.cashierType.sequence " +
						" order by ";
	    if ( !isStore )
	    	queryString += " cashier.userId, ";
	    queryString += " cashier.tender.tenderNo, cashier.cashierType.sequence";
	    
		List list = getHibernateTemplate().findByNamedParam(queryString, new String[] { "userId", "trnDate"}, new Object[] {userId, trnDate});

		List result = new ArrayList();
		String tmp = "";
		ClosingDeclaration closing = null;
		for ( int i=0; i<list.size(); i++ ) {
			Object[] obj = (Object[])list.get(i);
			
			if ( !tmp.equals(obj[0].toString()) ) {
				tmp = obj[0].toString();
				closing = new ClosingDeclaration();
				closing.setTenderId(obj[0].toString());
				closing.setTenderName(obj[1].toString());
				if ( !isStore )
					closing.setUserId(obj[7].toString() + "-" + obj[8].toString());
                result.add(closing);
			}
			
			if ( obj[5].toString().equals(Constant.CashierTransactionType.CLOSE) )
				closing.setAmount((BigDecimal)obj[4]);  // convert to positive value (+).
			
			ClosingDeclarationDetail det = new ClosingDeclarationDetail();
			det.setName(obj[2].toString());
			det.setNum(Integer.parseInt(obj[3].toString()));
			det.setAmount((BigDecimal)obj[4]);
			det.setSequence(Integer.parseInt(obj[6].toString()));
			
			closing.getDetails().add(det);
		}
	    
	    return result;
	}


	public ClosingDeclaration getTotalCashierDetail(String userId, Date trnDate) {
	    String queryString = "select cashier.cashierType.name, count(*), sum(cashier.transactionAmount), cashier.cashierType.cashierTransactionTypeId from " +
	    	"User user, " +
	    	"CashierTransaction cashier where " +
	    	"cashier.userId = user.userId and " +
	    	"cashier.userId = :userId and " +
	    	"cashier.transactionDate = :trnDate " +
	    	"group by cashier.cashierType.name, cashier.cashierType.cashierTransactionTypeId";
		List list = getHibernateTemplate().findByNamedParam(queryString, new String[] { "userId", "trnDate"}, new Object[] {userId, trnDate});
		
		ClosingDeclaration closing = new ClosingDeclaration();
		closing.setTenderName("Total");
		for ( int i=0; i<list.size(); i++ ) {
			Object[] obj = (Object[])list.get(i);
			
			if ( obj[3].toString().equals(Constant.CashierTransactionType.CLOSE) ) {
				closing.setAmount(((BigDecimal)obj[2]).negate());  // convert to positive value (+).
			} else {
				ClosingDeclarationDetail det = new ClosingDeclarationDetail();
				det.setName(obj[0].toString());
				det.setNum(Integer.parseInt(obj[1].toString()));
				det.setAmount((BigDecimal)obj[2]);
				closing.getDetails().add(det);
			}			
		}
		
		return closing;
	}
	public List getSummaryTenderBurnPoint(final String userId, final Date trnDate,final String tenderId) {
		class ReturnValue  {
			List value;
		}		
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "select new com.ie.icon.domain.sale.SalesSummary(tender.id, sum(cashier.transactionAmount), count(distinct tran.objectId)) from " +
						"CashierTransaction cashier left join " +
						"cashier.salesTransaction tran join " +
						"cashier.tender tender where " +						
						" ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and " +
						"cashier.userId like ? and " +
						"cashier.transactionDate = ? and " +
	        			 "cashier.tender.tenderId = ? and " +
						"cashier.cashierType.cashierTransactionTypeId <> '" + Constant.CashierTransactionType.EXCESS + "' " +
						"group by tender.id";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				query.setString(2, tenderId);
				rv.value = query.list();
				return null;
			}
		});

		return rv.value;
	}	
	
	
	public List getSummaryManyTender(final String userId, final Date trnDate,final String tenderId1 , final String tenderId2 , final String tenderId3 ,final String tenderId4) {
		class ReturnValue  { 
			List value;
		}		
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "select new com.ie.icon.domain.sale.SalesSummary(tender.id, sum(cashier.transactionAmount), count(distinct tran.objectId)) from " +
						"CashierTransaction cashier left join " +
						"cashier.salesTransaction tran join " +
						"cashier.tender tender where " +						
						" ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and " +
						"cashier.userId like ? and " +
						"cashier.transactionDate = ? and " +
	        			 "( (cashier.tender.tenderId = ? ) OR (cashier.tender.tenderId = ?) OR (cashier.tender.tenderId = ?) OR (cashier.tender.tenderId = ?)) and " +
						"cashier.cashierType.cashierTransactionTypeId <> '" + Constant.CashierTransactionType.EXCESS + "' " +
						"group by tender.id";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				query.setString(2, tenderId1);
				query.setString(3, tenderId2);
				query.setString(4, tenderId3);
				query.setString(5, tenderId4);
				rv.value = query.list();
				return null;
			}
		});

		return rv.value;
	}	
//	 TOEY ADD METHOD FOR ADD TENDER 55 93
	public List getSummaryManyTender(final String userId, final Date trnDate,final String tenderId1 , final String tenderId2 ,
			final String tenderId3 ,final String tenderId4,final String tenderId5 ,final String tenderId6) {
		class ReturnValue  { 
			List value;
		}		
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "select new com.ie.icon.domain.sale.SalesSummary(tender.id, sum(cashier.transactionAmount), count(distinct tran.objectId)) from " +
						"CashierTransaction cashier left join " +
						"cashier.salesTransaction tran join " +
						"cashier.tender tender where " +						
						" ((tran.typeId = 'P' ) OR (tran.typeId = 'S')) and " +
						"cashier.userId like ? and " +
						"cashier.transactionDate = ? and " +
	        			 "( (cashier.tender.tenderId = ? ) " +
	        			 " OR (cashier.tender.tenderId = ?) " +
	        			 " OR (cashier.tender.tenderId = ?) " +
	        			 " OR (cashier.tender.tenderId = ?) " +
	        			 " OR (cashier.tender.tenderId = ?) " +
	        			 " OR (cashier.tender.tenderId = ?)) and " +
						"cashier.cashierType.cashierTransactionTypeId <> '" + Constant.CashierTransactionType.EXCESS + "' " +
						"group by tender.id";
				Query query = session.createQuery(queryString);
				query.setString(0, userId);
				query.setDate(1, trnDate);
				query.setString(2, tenderId1);
				query.setString(3, tenderId2);
				query.setString(4, tenderId3);
				query.setString(5, tenderId4);
				query.setString(6, tenderId5);
				query.setString(7, tenderId6);
				rv.value = query.list();
				return null;
			}
		});

		return rv.value;
	}	
	
	//Pook 
	public List getCashierSessionListByUserId(String userId, Date trnDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CashierSession.class);
		
		criteria.add(Restrictions.eq("userId", userId));
		if (trnDate!=null) {
			criteria.add(Restrictions.eq("transactionDate", trnDate));
		}
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return result;
	}
}
