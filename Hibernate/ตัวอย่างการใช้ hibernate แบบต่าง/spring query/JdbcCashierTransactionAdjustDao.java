package com.ie.icon.dao.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.CashierTransactionAdjustJDBCDao;
import com.ie.icon.domain.cashier.CashierAdjustType;
import com.ie.icon.domain.cashier.CashierTransactionAdjust;
import com.ie.icon.domain.cashier.CashierTransactionAdjustItem;
import com.ie.icon.domain.csh.form.AdjustEmployee;
import com.ie.icon.domain.csh.form.CreditCaseON;
import com.ie.icon.domain.csh.form.NormalRate;
import com.ie.icon.domain.csh.form.OverUnderReport;
import com.ie.icon.domain.csh.form.ReceiveMnyCshChequeReport;
import com.ie.icon.domain.csh.form.ViewCreditRptByDetail;
import com.ie.icon.domain.csh.form.ViewCreditRptByType;
import com.ie.icon.domain.csh.form.ZipZapReport;
import com.ie.icon.domain.sale.CashierBalanceAdjust;
import com.ie.icon.domain.sale.CashierBalanceTransactionAdjust;
import com.ie.icon.domain.temp.TmpDayEndDiscountTotal;
import com.ie.icon.domain.temp.TmpDayEndRetailTotal;
import com.ie.icon.domain.temp.TmpDayEndTaxTotal;
import com.ie.icon.domain.temp.TmpDayEndTenderTotal;
import com.ie.icon.domain.temp.TmpDayEndTransaction;

public class JdbcCashierTransactionAdjustDao extends JdbcDaoSupport implements CashierTransactionAdjustJDBCDao {
	private SimpleDateFormat sf;

	public JdbcCashierTransactionAdjustDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	public List getClosingAndOverUnderAdjust(String storeId, Date tranDate, long cshTrnAdjOid) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append(" SELECT TENDERID AS TENDERID, SUM (NEWCLOSING) AS SUMNEWCLOSING,");
		buf.append(" SUM (OVERUNDERADJUST) AS SUMOVERUNDERADJUST");
		buf.append(" FROM ((SELECT CI.TENDER_ID AS TENDERID,");
		buf.append(" SUM (CASE ");
		buf.append(" WHEN CI.CSH_ADJ_TYP_ID = 'CLOSE'");
		buf.append(" THEN CI.TRN_AMT");
		buf.append(" ELSE 0");
		buf.append(" END");
		buf.append(" ) AS NEWCLOSING,");
		buf.append(" SUM (CASE ");
		buf.append(" WHEN CI.CSH_ADJ_TYP_ID <> 'CLOSE'");
		buf.append(" THEN CI.TRN_AMT * CX.SIGN_ADJ");
		buf.append(" ELSE 0");
		buf.append(" END");
		buf.append(" ) AS OVERUNDERADJUST");
		buf.append(" FROM CSH_TRN_ADJ C, CSH_TRN_ADJ_ITEM CI, CSH_ADJ_TYP CX");
		buf.append(" WHERE C.CSH_TRN_ADJ_OID = CI.CSH_TRN_ADJ_OID");
		buf.append(" AND CX.CSH_ADJ_TYP_ID = CI.CSH_ADJ_TYP_ID");
		buf.append(" AND C.TRN_DT = TO_DATE ('@1', 'DD/MM/YYYY')");
		buf.append(" AND C.STORE_ID = '@2'");
		if(cshTrnAdjOid==0){
			buf.append(" AND C.STS_ID <> 'C'");
		}
		else{
			buf.append(" AND C.CSH_TRN_ADJ_OID = '@3'");
			buf.append(" AND C.STS_ID <> 'C'");
		}		
		buf.append(" GROUP BY CI.TENDER_ID)");
		
		buf.append(" UNION ALL ");
		
		buf.append(" (SELECT CI.CREDIT_TENDER_ID, 0 AS NEWCLOSING,");
		buf.append(" SUM (CI.TRN_AMT * CX.SIGN_ADJ) * (-1) AS OVERUNDERADJUST");
		buf.append(" FROM CSH_TRN_ADJ C, CSH_TRN_ADJ_ITEM CI, CSH_ADJ_TYP CX");
		buf.append(" WHERE C.CSH_TRN_ADJ_OID = CI.CSH_TRN_ADJ_OID");
		buf.append(" AND CX.CSH_ADJ_TYP_ID = CI.CSH_ADJ_TYP_ID");
		buf.append(" AND CI.CREDIT_TENDER_ID IS NOT NULL");
		buf.append(" AND CI.CSH_ADJ_TYP_ID = 'ALTNATE'");
		buf.append(" AND C.TRN_DT = TO_DATE ('@1', 'DD/MM/YYYY')");
		buf.append(" AND C.STORE_ID = '@2'");
		if(cshTrnAdjOid==0){
			buf.append(" AND C.STS_ID <> 'C'");
		}
		else{
			buf.append(" AND C.CSH_TRN_ADJ_OID = '@3'");
			buf.append(" AND C.STS_ID <> 'C'");
		}
		buf.append(" GROUP BY CI.CREDIT_TENDER_ID))");
		buf.append(" GROUP BY TENDERID ");
		String query = buf.toString();
		query = query.replaceAll("@1", sf.format(tranDate));
		query = query.replaceAll("@2", storeId);
		query = query.replaceAll("@3", String.valueOf(cshTrnAdjOid));
		
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CashierBalanceAdjustMapper()));
	}

	public List getTenderAdjustDetail(String storeId, Date tranDate, String tenderID) throws Exception {
		String queryString = "  ( " + "  	select ci.csh_adj_typ_id  " + "  ,cx.description  " + "  , ci.tender_id as TenderID " + "  , t2.tender_no || '-' || ci.credit_tender_id as CreditTender " + "  ,t.tender_no || '-' || t.tender_nm as AdjTender  	 " + "  	, ci.trn_amt  " + "  	, ci.pos_id , ci.ticket_no , ci.user_id , ci.ref_info " + " " + "  " + "  	from  csh_trn_adj c, csh_trn_adj_item ci , csh_adj_typ cx , tender t ,tender t2	 " + "  	where c.csh_trn_adj_oid = ci.csh_trn_adj_oid "
				+ "  	and cx.csh_adj_typ_id = ci.csh_adj_typ_id " + "  	and ci.tender_id = t.tender_id " + "  	and c.trn_dt = TO_DATE('" + sf.format(tranDate) + "', 'DD/MM/YYYY') " + "  	and c.store_id = '" + storeId + "' " + "  	and ci.tender_id =  '" + tenderID + "' " + " 		and t2.tender_id (+)= ci.credit_tender_id " + "  	) " + "  UNION " + "  	( " + "  select ci.csh_adj_typ_id  " + "  ,cx.description " + "  ,  ci.credit_tender_id as TenderID "
				+ "  , t2.tender_no || '-' || t2.tender_nm as CreditTender " + "  ,t.tender_no || '-' || t.tender_nm as AdjTender " + "  , ci.trn_amt  " + "  , ci.pos_id , ci.ticket_no , ci.user_id , ci.ref_info " + "  	from  csh_trn_adj c, csh_trn_adj_item ci , csh_adj_typ cx, tender t, tender t2 " + "  	where c.csh_trn_adj_oid = ci.csh_trn_adj_oid " + "  	and cx.csh_adj_typ_id = ci.csh_adj_typ_id " + "  	and ci.credit_tender_id = t2.tender_id " + "  	and ci.tender_id = t.tender_id "
				+ "  	and ci.credit_tender_id is not null " + "  	and ci.csh_adj_typ_id = 'ALTNATE' " + "  	and c.trn_dt = TO_DATE('" + sf.format(tranDate) + "', 'DD/MM/YYYY') " + "  	and c.store_id = '" + storeId + "'	 " + "  	and ci.credit_tender_id =  '" + tenderID + "'  " + "  ) ";
		System.out.println("queryString:"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new CashierBalanceAdjustMapper2()));
	}
	
	public List getCashierBalanceTransactionAdjust(String storeId, Date tranDate) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append(" SELECT a.TENDER_ID AS TENDER_ID , ");
		buf.append(" SUM (CASE  WHEN a.csh_trn_typ_id = 'INIT' THEN a.TRN_AMT ELSE 0 END ) AS INITIAL_DECLARATION,");
		buf.append(" (SUM (CASE  WHEN a.csh_trn_typ_id in ('POS','SO') THEN a.TRN_AMT ELSE 0 END )+SUM(CASE  WHEN a.csh_trn_typ_id in ('CNP') THEN a.TRN_AMT ELSE 0 END )) AS SALES_TRANSACTION,");
		buf.append(" SUM (CASE  WHEN a.csh_trn_typ_id = 'LOAN' THEN a.TRN_AMT ELSE 0 END ) AS LOAN, ");
		buf.append(" SUM (CASE  WHEN a.csh_trn_typ_id = 'PICK' THEN a.TRN_AMT ELSE 0 END ) AS PICK,");
		buf.append(" SUM (CASE  WHEN a.csh_trn_typ_id = 'EXCESS' THEN a.TRN_AMT ELSE 0 END ) AS EXCESS_TRANSACTION,");
		buf.append(" SUM (CASE  WHEN a.csh_trn_typ_id = 'CNP' THEN a.TRN_AMT ELSE 0 END ) AS RETURN_TRANSACTION,");
		buf.append(" SUM (CASE  WHEN a.csh_trn_typ_id = 'CLOSE' THEN -a.TRN_AMT ELSE 0 END ) AS CLOSING_DECLARATION,");
		buf.append(" (SELECT SUM(b.TRN_AMT) FROM CSH_TRN_ADJ_ITEM b,CSH_TRN_ADJ c  ");
		buf.append(" WHERE b.csh_adj_typ_id = 'CLOSE' AND b.csh_trn_adj_oid = c.csh_trn_adj_oid AND C.trn_dt =  TO_DATE ('@1', 'DD/MM/YYYY') ");
		buf.append(" AND c.store_id = '@2'  ) AS ADD_CLOSE, ");
		buf.append(" (SELECT SUM(d.trn_amt*e.sign_adj) FROM CSH_TRN_ADJ_ITEM d,CSH_ADJ_TYP e WHERE D.csh_adj_typ_id = E.csh_adj_typ_id AND  D.csh_adj_typ_id IN ('OVER','UNDER','UNDEREN','ALTNATE','STAYOVR','STAYUND')) AS OVER_UNDER");
		buf.append(" FROM csh_trn a");
		buf.append(" WHERE a.trn_dt =  TO_DATE ('@3', 'DD/MM/YYYY')   ");
		buf.append(" AND a.STORE_ID = '@4'");
		buf.append(" GROUP BY a.TENDER_ID");
		
		String query = buf.toString();
		query = query.replaceAll("@1", sf.format(tranDate));
		query = query.replaceAll("@2", storeId);
		query = query.replaceAll("@3", sf.format(tranDate));
		query = query.replaceAll("@4", storeId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CashierBalanceAdjustMapper3()));
	}
	
	public List getDisplayOverUnderReport(Date fromDate, Date toDate)throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append(" select result.trn_dt ");
		buf.append(" ,a.user_id || ' - ' || a.user_nm ");
		buf.append(" ,result.SUM_OVER as SUM_OVER ");
		buf.append(" ,result.SUM_UNDER_ENUNDER as SUM_UNDER_ENUNDER ");
		buf.append(" ,result.SUM_NET as SUM_NET ");
		buf.append(" from usr a ");
		buf.append(" ,( ");

		buf.append(" select bb.trn_dt, bb.user_id, aa.SUM_OVER, aa.SUM_UNDER_ENUNDER,aa.SUM_NET ");
		buf.append(" from (  ");

		buf.append(" select id.trn_dt, id.user_id, sum(id.SUM_OVER)as SUM_OVER ");
		buf.append(" ,sum(id.SUM_UNDER_ENUNDER) as SUM_UNDER_ENUNDER ");
		buf.append(" ,sum(id.SUM_NET)as SUM_NET ");
		buf.append(" from (  ");
		buf.append("     select a.trn_dt,b.user_id ");
		buf.append("     ,sum(decode(b.csh_adj_typ_id,'OVER',trn_amt,0)) as SUM_OVER ");
		buf.append("     ,(sum(decode(b.csh_adj_typ_id,'UNDER',trn_amt,'ENUNDER',trn_amt,0))*-1) as SUM_UNDER_ENUNDER ");
		buf.append("     ,sum(decode(b.csh_adj_typ_id,'OVER',trn_amt,0)) + (sum(decode(b.csh_adj_typ_id,'UNDER',trn_amt,'ENUNDER',trn_amt,0))*-1) as SUM_NET ");
		buf.append("     from csh_trn_adj a, csh_trn_adj_item b ");
		buf.append("     where a.trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append("     and a.trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		buf.append("     and a.sts_id in('S','N') ");
		buf.append("     and b.csh_adj_typ_id in ('UNDER','ENUNDER','OVER') ");
		buf.append("     and a.csh_trn_adj_oid = b.csh_trn_adj_oid ");
		buf.append("     group by a.trn_dt,b.user_id ");
		        
		buf.append("     union ");
		        
		buf.append("     select trn_dt, create_user_id as user_id, 0 as SUM_OVER, 0 as SUM_UNDER_ENUNDER, 0 as SUM_NET ");
		buf.append("     from sales_trn ");
		buf.append("     where trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append("     and trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		buf.append("     and sts_id = 'P' ");
		buf.append("     ) id ");
		buf.append(" group by id.trn_dt, id.user_id ");
		buf.append(" ) aa ");
		buf.append(" ,( ");
		buf.append("     select trn_dt,user_id ");
		buf.append("     from usr ");
		buf.append("     ,(select distinct trn_dt  ");
		buf.append("         from sales_trn ");
		buf.append("         where trn_dt>= to_Date('@1','DD/MM/YYYY') ");
		buf.append("         and trn_dt<= to_Date('@2','DD/MM/YYYY') ");
		buf.append("     ) dt ");
		buf.append("     where usr.is_active = 'Y' ");
		buf.append("     and usr.user_role_oid in (5,6,7,8) ");
		buf.append(" ) bb ");
		buf.append(" where bb.user_id = aa.user_id(+) ");
		buf.append(" and bb.trn_dt = aa.trn_dt(+) ");
		buf.append(" )result ");
		buf.append(" where a.user_id = result.user_id ");
		buf.append(" order by result.trn_dt, result.user_id ");
		
		String query = buf.toString();		
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CshAdjExOverUnderMapper()));
	}
	
	public List getDisplayCTOverUnderReport(String storeId, Date fromDate, Date toDate)throws Exception {
		StringBuffer buf = new StringBuffer();
		
		buf.append(" select result.trn_dt ");
		buf.append(" ,st.store_id || ' - ' || st.abb_nm as store_id  ");
		buf.append(" ,result.SUM_OVER as SUM_OVER ");
		buf.append(" ,result.SUM_UNDER_ENUNDER as SUM_UNDER_ENUNDER ");
		buf.append(" ,result.SUM_NET as SUM_NET ");
		buf.append(" from ");
		buf.append(" ( ");
		buf.append(" select a.trn_dt, a.store_id ");
		buf.append(" ,sum(decode(b.csh_adj_typ_id,'OVER',trn_amt,0)) as SUM_OVER ");
		buf.append(" ,(sum(decode(b.csh_adj_typ_id,'UNDER',trn_amt,'ENUNDER',trn_amt,0))*-1) as SUM_UNDER_ENUNDER ");
		buf.append(" ,sum(decode(b.csh_adj_typ_id,'OVER',trn_amt,0)) + (sum(decode(b.csh_adj_typ_id,'UNDER',trn_amt,'ENUNDER',trn_amt,0))*-1) as SUM_NET ");
		buf.append(" from csh_trn_adj a, csh_trn_adj_item b ");
		buf.append(" where a.trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		buf.append(" and a.sts_id in('S','N') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@3' ");
		}
		buf.append(" and b.csh_adj_typ_id in ('UNDER','ENUNDER','OVER') ");
		buf.append(" and a.csh_trn_adj_oid = b.csh_trn_adj_oid ");
		buf.append(" group by a.trn_dt,a.store_id ");
		buf.append(" )result  ");
		buf.append(" , store st ");
		buf.append(" where result.store_id = st.store_id ");
		buf.append(" order by result.trn_dt, result.store_id ");
		
		String query = buf.toString();		
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		query = query.replaceAll("@3", storeId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CshAdjExOverUnderMapper()));
	}
	
	public List getDisplayOPMReport(String storeId, Date fromDate, Date toDate)throws Exception {
		StringBuffer buf = new StringBuffer();
	
		buf.append(" select result.trn_dt ");
		buf.append(" ,st.store_id || ' - ' || st.abb_nm as store_id ");
		buf.append(" ,result.trn_amt as trn_amt ");
		buf.append(" ,result.num_trns as num_trns ");
		buf.append(" From( ");
		buf.append(" select trn_dt ");
		buf.append(" ,store_id ");
		buf.append(" ,sum(trn_amt) as trn_amt ");
		buf.append(" ,nvl(sum(num_trns),0) as num_trns ");
		buf.append(" from csh_manage_tender ");
		buf.append(" where csh_manage_typ = 'OPM' ");
		buf.append(" and is_active = 'Y' ");
		buf.append(" and trn_dt >= to_date('@1','DD/MM/YYYY') ");
		buf.append(" and trn_dt <= to_date('@2','DD/MM/YYYY') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and store_id = '@3' ");
		}
		buf.append(" group by trn_dt, store_id ");
		buf.append(" )result ");
		buf.append(" , store st ");
		buf.append(" where result.store_id = st.store_id ");
		buf.append(" order by result.trn_dt, result.store_id ");
		
		
		String query = buf.toString();		
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		query = query.replaceAll("@3", storeId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new OPMMapper()));
	}	
		
	public List getDisplayCHRReport(String storeId, Date fromDate, Date toDate)throws Exception {
		StringBuffer buf = new StringBuffer();
	
		buf.append(" select result.trn_dt ");
		buf.append(" ,st.store_id || ' - ' || st.abb_nm as store_id ");
		buf.append(" ,result.trn_amt as trn_amt ");
		buf.append(" ,result.num_trns as num_trns ");
		buf.append(" From( ");
		buf.append(" select trn_dt ");
		buf.append(" ,store_id ");
		buf.append(" ,sum(round((trn_amt*rate),2)) as trn_amt ");
		buf.append(" ,nvl(sum(num_trns),0) as num_trns ");
		buf.append(" from csh_manage_tender ");
		buf.append(" where csh_manage_typ = 'CHR' ");
		buf.append(" and is_active = 'Y' ");
		buf.append(" and trn_dt >= to_date('@1','DD/MM/YYYY') ");
		buf.append(" and trn_dt <= to_date('@2','DD/MM/YYYY') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and store_id = '@3' ");
		}
		buf.append(" group by trn_dt, store_id ");
		buf.append(" )result ");
		buf.append(" , store st ");
		buf.append(" where result.store_id = st.store_id ");
		buf.append(" order by result.trn_dt, result.store_id ");
		
		
		String query = buf.toString();		
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		query = query.replaceAll("@3", storeId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new OPMMapper()));
	}	
	
	public List getDisplayAdjustEmployeeReport(Date fromDate, Date toDate)throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append(" select result.trn_dt ");
		buf.append(" ,a.user_id || ' - ' || a.user_nm as FullName ");
		buf.append(" ,result.SUM_UNDER_ENUNDER as SUM_UNDER_ENUNDER ");
		buf.append(" from usr a ");
		buf.append(" ,( ");

		buf.append(" select bb.trn_dt, bb.user_id ");
		buf.append(" ,aa.SUM_UNDER_ENUNDER ");
		buf.append(" from (  ");

		buf.append(" select id.trn_dt, id.user_id ");
		buf.append(" ,sum(id.SUM_UNDER_ENUNDER) as SUM_UNDER_ENUNDER ");
		buf.append(" from (  ");
		buf.append("     select a.trn_dt,b.user_id ");
		buf.append("     ,(sum(decode(b.csh_adj_typ_id,'UNDER',trn_amt,'ENUNDER',trn_amt,0))*-1) as SUM_UNDER_ENUNDER ");
		buf.append("     from csh_trn_adj a, csh_trn_adj_item b ");
		buf.append("     where a.trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append("     and a.trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		buf.append("     and a.sts_id in('S','N') ");
		buf.append("     and b.csh_adj_typ_id = 'ENUNDER' ");
		buf.append("     and a.csh_trn_adj_oid = b.csh_trn_adj_oid ");
		buf.append("     group by a.trn_dt,b.user_id ");
		        
		buf.append("     union ");
		        
		buf.append("     select trn_dt, create_user_id as user_id ");
		buf.append("     , 0 as SUM_UNDER_ENUNDER ");
		buf.append("     from sales_trn ");
		buf.append("     where trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append("     and trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		buf.append("     and sts_id = 'P' ");
		buf.append("     ) id ");
		buf.append(" group by id.trn_dt, id.user_id ");
		buf.append(" ) aa ");
		buf.append(" ,( ");
		buf.append("     select trn_dt,user_id ");
		buf.append("     from usr ");
		buf.append("     ,(select distinct trn_dt  ");
		buf.append("         from sales_trn ");
		buf.append("         where trn_dt>= to_Date('@1','DD/MM/YYYY') ");
		buf.append("         and trn_dt<= to_Date('@2','DD/MM/YYYY') ");
		buf.append("     ) dt ");
		buf.append("     where usr.is_active = 'Y' ");
		buf.append("     and usr.user_role_oid in (5,6,7,8) ");
		buf.append(" ) bb ");
		buf.append(" where bb.user_id = aa.user_id(+) ");
		buf.append(" and bb.trn_dt = aa.trn_dt(+) ");
		buf.append(" )result ");
		buf.append(" where a.user_id = result.user_id ");
		buf.append(" order by result.trn_dt, result.user_id ");
		
		String query = buf.toString();		
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new AdjustEmployeeMapper()));
	}
	
	public List displayRptZipzap(String storeId, Date fromDate, Date toDate, String tenderId)throws Exception {
		StringBuffer buf = new StringBuffer();
		
		buf.append(" select (c.store_id || ' - ' || c.abb_nm) as storeId, a.trn_dt, a.tender_id, a.cr_card_no,a.csh_manage_typ ");
		buf.append(" , a.trn_amt ");
		buf.append(" , a.num_month ");
		buf.append(" , decode(a.rate_payment,'A',round(a.trn_amt*a.rate,2),0) as TRN_AMT_A ");
		buf.append(" , 0 as normalRat ");
		buf.append(" , a.ref_info  ");
		buf.append(" from csh_manage_tender a, tender b, store c ");
		buf.append(" where a.is_active = 'Y' ");
		buf.append(" and a.trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		buf.append(" and a.csh_manage_typ in ('ZZP','CRV') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@3' ");
		}
		if(tenderId!=null && !tenderId.equals("") && !tenderId.equals("all")){
			buf.append(" and a.tender_Id = '@4' ");
		}
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" and a.store_id = c.store_id ");
		buf.append(" order by a.store_id, a.trn_dt,b.tender_no ");
			
		String query = buf.toString();		
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		query = query.replaceAll("@3", storeId);
		query = query.replaceAll("@4", tenderId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new ZipzapMapper()));
	}
	
	public List displayDetialNormalRate(Date fromDate, Date toDate)throws Exception {
		StringBuffer buf = new StringBuffer();
		
		buf.append(" select c.tender_id as tender_id, c.rate ");
		buf.append(" from csh_rate_tender c,  ");
		buf.append(" ( ");
		buf.append(" select tender_id, max(last_upd_dttm) as last_upd_dttm ");
		buf.append(" from csh_rate_tender ");
		buf.append(" where is_active = 'Y' ");
		buf.append(" and rate_typ = 'N' ");
		buf.append(" and rate_From <= to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and rate_To >= to_Date('@2','DD/MM/YYYY') ");
		buf.append(" group by tender_id ");
		buf.append(" ) grp_c ");
		buf.append(" where c.tender_id = grp_c.tender_id ");
		buf.append(" and c.last_upd_dttm = grp_c.last_upd_dttm ");
		
		String query = buf.toString();		
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new NormalRateMapper()));
	}
	public List displayRptViewCreditByType(String storeId, Date fromDate, Date toDate)throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append(" select result.trn_dt ");
		buf.append(" ,sum(SUM_01) as SUM_01 ");
		buf.append(" ,sum(SUM_02) as SUM_02 ");
		buf.append(" ,sum(SUM_03) as SUM_03 ");
		buf.append(" ,sum(SUM_04) as SUM_04 ");
		buf.append(" ,sum(SUM_05) as SUM_05 ");
		buf.append(" ,sum(SUM_06) as SUM_06 ");
		buf.append(" ,sum(SUM_01)+sum(SUM_02)+sum(SUM_03)+sum(SUM_04)+sum(SUM_05)+sum(SUM_06) as TOTAL_SUM ");
		buf.append(" from ( ");
		buf.append(" select trn_dt ");
		buf.append(" ,sum(decode(b.tender_grp_id,'01',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_01 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'02',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_02 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'03',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_03 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'04',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_04 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'05',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_05 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'06',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_06 ");
		buf.append(" from  ");
		buf.append(" ( ");
		buf.append(" select a.trn_dt as trn_dt ,a.tender_id as tender_id ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'CLOSE',a.trn_amt*-1,0)) as SUM_CLOSE  ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'PICK',a.trn_amt,0)) as SUM_PICK ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'LOAN',a.trn_amt,0)) as SUM_LOAN  ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'INIT',a.trn_amt,0)) as SUM_INIT ");
		buf.append(" From csh_trn a ");
		buf.append(" where a.trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@3' ");
		}
		buf.append(" and a.csh_trn_typ_id in ('CLOSE','PICK','LOAN','INIT') ");
		buf.append(" group by a.trn_dt,a.tender_id ");
		buf.append(" ) a, tender b ");
		buf.append(" where a.tender_id = b.tender_id ");
		buf.append(" group by trn_dt ");
		buf.append(" union ");
		buf.append(" select trn_dt ");
		buf.append(" ,sum(decode(b.tender_grp_id,'01',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_01 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'02',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_02 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'03',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_03 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'04',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_04 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'05',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_05 ");
		buf.append(" ,sum(decode(b.tender_grp_id,'06',SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT,0)) as SUM_06 ");
		buf.append(" from ");
		buf.append(" ( ");
		buf.append(" select a.trn_dt as trn_dt ,a.tender_id as tender_id ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'CLOSE',a.adj_amt,0)) as SUM_CLOSE ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'PICK',a.adj_amt,0)) as SUM_PICK ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'LOAN',a.adj_amt,0)) as SUM_LOAN ");
		buf.append(" ,sum(decode(a.csh_trn_typ_id,'INIT',a.adj_amt,0)) as SUM_INIT ");
		buf.append(" From csh_trn_adj_ex a ");
		// credit group ?
		buf.append(" where a.trn_dt >= to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.trn_dt <= to_Date('@2','DD/MM/YYYY') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@3' ");
		}
		buf.append(" and a.is_active = 'Y' ");
		buf.append(" and a.csh_trn_typ_id in ('CLOSE','PICK','LOAN','INIT') ");
		buf.append(" group by a.trn_dt,a.tender_id ");
		buf.append(" ) a, tender b ");
		buf.append(" where a.tender_id = b.tender_id ");
		buf.append(" group by trn_dt ");
		buf.append(" ) result ");
		buf.append(" group by result.trn_dt ");
		
		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		query = query.replaceAll("@3", storeId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new ViewCreditRptByTypeMapper()));
	} 
	
	public List displayRptViewCreditByDetail(String storeId, Date fromDate, Date toDate, String tenderGrpId)throws Exception {
		StringBuffer buf = new StringBuffer();
	
		buf.append(" select result.trn_dt  ");
		buf.append(" ,result.tender_id AS tender_id  ");
		buf.append(" ,sum(result.SUM_TOTAL) AS SUM_TOTAL  ");
		buf.append(" from  ");
		buf.append(" (  ");
		buf.append("     select 'one',a.trn_dt  ");
		buf.append("     ,a.tender_id as tender_id  ");
		buf.append("     ,sum(SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT) as SUM_TOTAL  ");
		buf.append("     from   ");
		buf.append("         (  ");
		buf.append("             select a.trn_dt as trn_dt ,a.tender_id as tender_id   ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'CLOSE',a.trn_amt*-1,0)) as SUM_CLOSE   ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'PICK',a.trn_amt,0)) as SUM_PICK  ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'LOAN',a.trn_amt,0)) as SUM_LOAN   ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'INIT',a.trn_amt,0)) as SUM_INIT  ");
		buf.append("             From csh_trn a, tender b  ");
		buf.append("             where a.trn_dt >= to_Date('@1','DD/MM/YYYY')  ");
		buf.append("             and a.trn_dt <= to_Date('@2','DD/MM/YYYY')  ");
		if(storeId !=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append("             and a.store_id = '@3'  ");
		}	
		buf.append("             and a.csh_trn_typ_id in ('CLOSE','PICK','LOAN','INIT')  ");
		if(tenderGrpId!=null && !tenderGrpId.equals("")){
			buf.append("             and b.tender_grp_id = '@4'  ");
		}
		buf.append("  and (b.is_pos = 'Y' or b.is_so = 'Y')  ");
		buf.append("             and a.tender_id = b.tender_id  ");
		buf.append("             group by a.trn_dt,a.tender_id  ");
		buf.append("         ) a  ");
		buf.append("     group by a.trn_dt, a.tender_id  ");

		buf.append("     union  ");
		buf.append("       ");
		buf.append("     select 'two',a.trn_dt  ");
		buf.append("     ,a.tender_id as tender_id  ");
		buf.append("     ,sum(SUM_CLOSE-SUM_PICK-SUM_LOAN-SUM_INIT) as SUM_TOTAL  ");
		buf.append("     from   ");
		buf.append("         (  ");
		buf.append("             select a.trn_dt as trn_dt ,a.tender_id as tender_id   ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'CLOSE',a.adj_amt,0)) as SUM_CLOSE   ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'PICK',a.adj_amt,0)) as SUM_PICK  ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'LOAN',a.adj_amt,0)) as SUM_LOAN   ");
		buf.append("             ,sum(decode(a.csh_trn_typ_id,'INIT',a.adj_amt,0)) as SUM_INIT   ");
		buf.append("             From csh_trn_adj_ex a, tender b  ");
		buf.append("             where a.trn_dt >= to_Date('@1','DD/MM/YYYY')  ");
		buf.append("             and a.trn_dt <= to_Date('@2','DD/MM/YYYY')  ");
		if(storeId !=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append("             and a.store_id = '@3'  ");
		}	
		buf.append("  and (b.is_pos = 'Y' or b.is_so = 'Y')  ");
		buf.append("             and a.csh_trn_typ_id in ('CLOSE','PICK','LOAN','INIT')  ");
		buf.append("             and a.is_active = 'Y'  ");
		if(tenderGrpId!=null && !tenderGrpId.equals("")){
			buf.append("             and b.tender_grp_id = '@4'  ");
		}
		buf.append("             and a.tender_id = b.tender_id  ");
		buf.append("             group by a.trn_dt,a.tender_id  ");
		buf.append("         ) a  ");
		buf.append("     group by a.trn_dt, a.tender_id  ");
		buf.append(" )result  ");
		buf.append(" group by result.trn_dt, result.tender_id  ");
		buf.append(" order by result.trn_dt asc, result.tender_id asc  ");
		
		
		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", sf.format(toDate));
		query = query.replaceAll("@3", storeId);
		query = query.replaceAll("@4", tenderGrpId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new ViewCreditRptByDetailMapper()));
	} 
	
	public List displayCtRptReceiveMnyCshCheque(String storeId, Date fromDate)throws Exception{
		StringBuffer buf = new StringBuffer();
		
		buf.append(" select st.store_id || ' - ' || st.abb_nm as store_id   ");
		buf.append(" ,result.SUM_CASH  ");
		buf.append(" ,result.SUM_CHEQUE  ");
		buf.append(" ,result.OTHER_CASH  ");
		buf.append(" ,result.OTHER_CHEQUE  ");
		buf.append(" ,result.SUM_TOTAL  ");
		buf.append(" from (  ");

		buf.append(" select result.store_Id  ");
		buf.append(" ,sum(SUM_CASH) as SUM_CASH  ");
		buf.append(" ,sum(SUM_CHEQUE) as SUM_CHEQUE  ");
		buf.append(" ,sum(OTHER_CASH) as OTHER_CASH  ");
		buf.append(" ,sum(OTHER_CHEQUE) as OTHER_CHEQUE  ");
		buf.append(" ,sum(SUM_CASH)+sum(SUM_CHEQUE)+sum(OTHER_CASH)+sum(OTHER_CHEQUE) as SUM_TOTAL  ");
		buf.append(" from (  ");
		buf.append(" select a.store_id as store_id  ");
		buf.append(" ,sum(decode(b.tender_grp_id,'01',a.trn_amt,0)) as SUM_CASH   ");
		buf.append(" ,sum(decode(b.tender_grp_id,'02',a.trn_amt,0)) as SUM_CHEQUE  ");
		buf.append(" ,0 as OTHER_CASH  ");
		buf.append(" ,0 as OTHER_CHEQUE  ");
		buf.append(" From csh_trn a, tender b  ");
		buf.append(" where a.trn_dt = to_Date('@1','DD/MM/YYYY')  ");
		buf.append(" and a.csh_trn_typ_id in ('POS','SO','CNP','CNS')  ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_Id = '@2'  ");
		}
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id  ");

		buf.append(" union  ");

		buf.append(" select result.store_id  ");
		buf.append(" ,sum(SUM_CASH) as SUM_CASH  ");
		buf.append(" ,sum(SUM_CHEQUE) as SUM_CASH  ");
		buf.append(" , 0 as OTHER_CASH  ");
		buf.append(" , 0 as OTHER_CHEQUE     ");
		buf.append(" from   ");
		buf.append(" (  ");
		buf.append(" select header.store_id  ");
		buf.append(" ,sum(decode(tender.tender_grp_id,'01',item.trn_amt*typ.sign_adj,0)) as SUM_CASH  ");
		buf.append(" ,sum(decode(tender.tender_grp_id,'02',item.trn_amt*typ.sign_adj,0)) as SUM_CHEQUE  ");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, csh_adj_typ typ, tender tender  ");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY')  ");
		buf.append(" and header.sts_id in ('S','N')   ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_Id = '@2'  ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid  ");
		buf.append(" and item.csh_adj_typ_id = typ.csh_adj_typ_id  ");
		buf.append(" and item.tender_id = tender.tender_id  ");
		buf.append(" group by header.store_id  ");

		buf.append(" union  ");

		buf.append(" select header.store_id  ");
		buf.append(" ,sum(decode(tender.tender_grp_id,'01',item.trn_amt*-1,0)) as SUM_CASH   ");
		buf.append(" ,sum(decode(tender.tender_grp_id,'02',item.trn_amt*-1,0)) as SUM_CHEQUE  ");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, tender tender  ");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY')  ");
		buf.append(" and header.sts_id in ('S','N')   ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_Id = '@2'  ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid  ");
		buf.append(" and item.credit_tender_id = tender.tender_id  ");
		buf.append(" group by header.store_id  ");
		buf.append(" ) result  ");
		buf.append(" group by result.store_id  ");

		buf.append(" union  ");

		buf.append(" select a.store_id as store_id  ");
		buf.append(" ,0 as SUM_CASH   ");
		buf.append(" ,0 as SUM_CHEQUE  ");
		buf.append(" ,sum(decode(b.tender_grp_id,'01',a.trn_amt,0)) as OTHER_CASH  ");
		buf.append(" ,sum(decode(b.tender_grp_id,'02',a.trn_amt,0)) as OTHER_CHEQUE  ");
		buf.append(" from csh_manage_tender a, tender b  ");
		buf.append(" where a.trn_dt = to_Date('@1','DD/MM/YYYY')  ");
		buf.append(" and a.is_active = 'Y'" );
		buf.append(" and a.csh_manage_typ = 'INC'  ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_Id = '@2'  ");
		}
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id  ");
		buf.append(" )  ");
		buf.append(" result  ");
		buf.append(" group by result.store_Id  ");
		buf.append(" order by result.store_id asc  ");
		buf.append(" ) result , store st  ");
		buf.append(" where result.store_id = st.store_id  ");


		
		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", storeId);
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new ReceiveMnyCshChequeReportMapper()));
	}
	
	public List displayCreditCaseEY(String storeId, Date fromDate) throws Exception{
		StringBuffer buf = new StringBuffer();
		
		buf.append(" select A.STORE_ID||' - '|| st.ABB_NM as STORE_ID, a.tender_id, a.AMT  from (   ");
		
		buf.append(" select   ");
		buf.append(" resultToTal.store_id as store_id ");
		buf.append(" ,resultToTal.tender_id as tender_id ");
		buf.append(" ,resultToTal.EDC - resultToTal.SRATE - round(((nvl(resultToTal.EDC,0)-resultToTal.DCC) * nvl(resultToTal.NRATE,0)),2) - round(((nvl(resultToTal.SRATE,0)+((nvl(resultToTal.EDC,0)-resultToTal.DCC)*nvl(resultToTal.NRATE,0)))*0.07),2) - resultToTal.DCC as AMT   ");
		buf.append(" from ");
		buf.append(" ( ");

		buf.append(" select result.store_id ,result.tender_id  ");
		buf.append(" ,sum(EDC) as EDC ");
		buf.append(" ,sum(SRATE) as SRATE ");
		buf.append(" ,sum(DCC) as DCC ");
		buf.append(" ,sum(NRATE) as NRATE ");
		buf.append(" from ");
		buf.append(" ( ");
		//buf.append(" ----EDC ");
		buf.append(" select result.store_id ");
		buf.append(" ,result.tender_id   ");
		buf.append(" , sum(AMT) AS EDC ");
		buf.append(" , 0 AS SRATE ");
		buf.append(" , 0 AS DCC ");
		buf.append(" , 0 as NRATE ");
		buf.append(" from  ");
		buf.append(" ( ");
		//buf.append(" --Sum Credit Card ");
		//buf.append(" ------ All Store All Tender ");
		buf.append(" select s.store_id, td.tender_id, 0 as amt ,'x0'");
		buf.append(" from tender td, store s ");
		buf.append(" where td.tender_grp_id = '03'  ");
		buf.append(" and (td.is_pos = 'Y' or td.is_so = 'Y')  ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and s.store_id = '@2' ");
		}
		
		buf.append(" group by s.store_id, td.tender_id ");
		//buf.append(" ------ All Store All Tender ");
		buf.append(" union ");

		buf.append(" select b.store_id, ");
		buf.append(" a.tender_id ");
		buf.append(" ,sum(b.trn_amt) as AMT ,'x1'");
		buf.append(" From tender a, CSH_TRN b ");
		buf.append(" where a.tender_grp_id = '03' ");
		buf.append(" and b.trn_dt = to_date('@1','DD/MM/YYYY') ");
		buf.append(" and b.csh_trn_typ_id in ('POS','SO','CNP','CNS') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and b.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by b.store_id, a.tender_id ");

		buf.append(" union  ");

		buf.append(" select header.store_id  ");
		buf.append(" ,tender.tender_id ");
		buf.append(" ,sum(item.trn_amt*typ.sign_adj) as AMT ,'x2'");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, csh_adj_typ typ, tender tender");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and header.sts_id in ('S','N')  ");
		buf.append(" and tender.tender_grp_id = '03' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_id = '@2' ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid ");
		buf.append(" and item.csh_adj_typ_id = typ.csh_adj_typ_id ");
		buf.append(" and item.tender_id = tender.tender_id ");
		buf.append(" group by header.store_id ,tender.tender_id ");

		buf.append(" union ");

		buf.append(" select header.store_id, ");
		buf.append(" tender.tender_id ");
		buf.append(" ,sum(item.trn_amt)*-1 as AMT ,'x3'");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, csh_adj_typ typ, tender tender ");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and header.sts_id in ('S','N')  ");
		buf.append(" and tender.tender_grp_id = '03' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_id = '@2' ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid ");
		buf.append(" and item.csh_adj_typ_id = typ.csh_adj_typ_id ");
		buf.append(" and item.credit_tender_id = tender.tender_id ");
		buf.append(" group by header.store_id,tender.tender_id ");
		//buf.append(" --Sum Credit Card ");

		buf.append(" union ");

		//buf.append(" --ZipZap, CR  ");
		buf.append(" select a.store_id ");
		buf.append(" ,a.tender_id ");
		buf.append(" ,sum(a.trn_amt*-1) as AMT ,'x4'");
		buf.append(" From csh_manage_tender a, tender b ");
		buf.append(" where a.csh_manage_typ in ('ZZP','CRV') ");
		buf.append(" and b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.is_active = 'Y' ");
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id, a.tender_id ");
		//buf.append(" --ZipZap, CR ");

		buf.append(" union ");
		//buf.append(" --INC, OTH ");
		buf.append(" select a.store_id ");
		buf.append(" ,a.tender_id ");
		buf.append(" ,sum(a.trn_amt) as AMT ,'x5'");
		buf.append(" From csh_manage_tender a, tender b ");
		buf.append(" where a.csh_manage_typ in ('INC','OTH') ");
		buf.append(" and b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id,a.tender_id ");
		//buf.append(" --INC, OTH ");
		buf.append(" ) result ");
		buf.append(" group by result.store_id,result.tender_Id ");
		//buf.append(" ----EDC ");

		buf.append(" union ");

		//buf.append(" --Calculate Additional fee ");
		buf.append(" select a.store_id,a.tender_id ");
		buf.append(" ,0 as EDC ");
		buf.append(" ,round(sum(nvl(a.trn_amt,0)*nvl(a.rate,0)),2) as SRATE ");
		buf.append(" ,0 AS DCC ");
		buf.append(" ,0 as NRATE ");
		buf.append(" from csh_manage_tender a, tender b ");
		buf.append(" where b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.rate_payment = 'S' ");
		buf.append(" and a.csh_manage_typ IN ('INC','CHR','OTH') ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.store_id,a.tender_id ");
		//buf.append(" --Calculate Additional fee  ");

		buf.append(" union ");

		//buf.append(" --OPM ");
		buf.append(" select a.store_id, a.tender_id ");
		buf.append(" ,0 as EDC ");
		buf.append(" ,0 as SRATE ");
		buf.append(" ,sum(trn_amt) as DCC ");
		buf.append(" ,0 as NRATE ");
		buf.append(" from csh_manage_tender a, tender b ");
		buf.append(" where b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		//buf.append(" --and a.rate_payment = 'S' ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.csh_manage_typ = 'DCC' ");
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.store_id,a.tender_id ");
		//buf.append(" --OPM ");
		buf.append(" union ");


		//buf.append(" --Normal Rate ");
		buf.append(" select result.store_id, result.tender_id ");
		buf.append(" ,0 as EDC ");
		buf.append(" ,0 as SRATE ");
		buf.append(" ,0 as DCC ");
		buf.append(" ,sum(NRATE) as NRATE ");
		buf.append(" from ");
		buf.append(" ( ");
		buf.append("     select s.store_id, td.tender_id, 0 as NRATE ");
		buf.append("     from tender td, store s ");
		buf.append("     where td.tender_grp_id = '03'  ");
		//ADD By Bum 07/05/2013 Where  AND (is_pos = 'Y' OR is_so = 'Y')
		buf.append("     and (td.is_pos = 'Y' or td.is_so = 'Y') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append("     and s.store_id = '@2' ");
		}
		buf.append("     group by s.store_id, td.tender_id ");

		buf.append("     union ");

		buf.append("     select s.store_id,a.tender_id ");
		buf.append("     ,nrate.rate as NRATE ");
		buf.append("     from tender a, store s ");
		buf.append("     ,(select c.tender_id as tender_id, c.rate ");
		buf.append("         from csh_rate_tender c,  ");
		buf.append("         ( ");
		buf.append("             select tender_id, max(last_upd_dttm) as last_upd_dttm ");
		buf.append("             from csh_rate_tender ");
		buf.append("             where is_active = 'Y' ");
		buf.append("             and rate_typ = 'N' ");
		buf.append("             and rate_From <= to_Date('@1','DD/MM/YYYY') ");
		buf.append("             and rate_To >= to_Date('@1','DD/MM/YYYY') ");
		buf.append("             group by tender_id ");
		buf.append("         ) grp_c ");
		buf.append("         where c.tender_id = grp_c.tender_id ");
		buf.append("         and c.last_upd_dttm = grp_c.last_upd_dttm ");
		buf.append("     )nrate ");
		buf.append("     where a.tender_id = nrate.tender_id ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append("     and s.store_id = '@2' ");
		}
		buf.append(" )result ");
		buf.append(" group by result.store_id, result.tender_id ");
		//buf.append(" --Normal Rate ");
		buf.append(" )result ");
		buf.append(" group by result.store_id,result.tender_id ");
		buf.append(" )resultToTal ");
		buf.append(" ) a, store st  Where A.STORE_ID = ST.STORE_ID ");
		
		buf.append(" order by store_id, tender_id ");
		
		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(fromDate));
		query = query.replaceAll("@2", storeId);		
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CreditCaseONMapper()));
	}
	
	public List displayCreditCaseEN(String storeId, Date fromDate)throws Exception{
		StringBuffer buf = new StringBuffer();
		buf.append(" select A.STORE_ID||' - '|| st.ABB_NM as STORE_ID, a.tender_id, a.AMT  from (   ");
		
		buf.append(" select   ");
		buf.append(" resultToTal.store_id as store_id ");
		buf.append(" ,resultToTal.tender_id as tender_id ");
		buf.append(" ,resultToTal.EDC-resultToTal.DCC AS AMT ");
		buf.append(" from( ");
		buf.append(" select result.store_id,result.tender_id ");
		buf.append(" ,sum(result.EDC) as EDC ");
		buf.append(" ,sum(result.DCC) as DCC ");
		buf.append(" from ");
		buf.append(" ( ");
		//buf.append(" ----EDC ");
		buf.append(" select result.store_id ");
		buf.append(" ,result.tender_id   ");
		buf.append(" , sum(AMT) AS EDC ");
		buf.append(" , 0 AS DCC ");
		buf.append(" from  ");
		buf.append(" ( ");
		//buf.append(" --Sum Credit Card  ");
		//buf.append(" ------ All Store All Tender ");
		buf.append(" select s.store_id, td.tender_id, 0 as amt  ,'x0'");
		buf.append(" from tender td, store s ");
		buf.append(" where td.tender_grp_id = '03'  ");
		//ADD By Bum 07/05/2013 Where  AND (is_pos = 'Y' OR is_so = 'Y')
		buf.append("     and (td.is_pos = 'Y' or td.is_so = 'Y') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and s.store_id = '@2' ");
		}
		
		buf.append(" group by s.store_id, td.tender_id ");
		//buf.append(" ------ ");
		buf.append(" union ");

		buf.append(" select b.store_id, ");
		buf.append(" a.tender_id ");
		buf.append(" ,sum(b.trn_amt) as AMT  ,'x1'");
		buf.append(" From tender a, CSH_TRN b ");
		buf.append(" where a.tender_grp_id = '03' ");
		buf.append(" and b.trn_dt = to_date('@1','DD/MM/YYYY') ");
		buf.append(" and b.csh_trn_typ_id in ('POS','SO','CNP','CNS') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and b.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by b.store_id, a.tender_id ");

		buf.append(" union  ");

		buf.append(" select header.store_id  ");
		buf.append(" ,tender.tender_id ");
		buf.append(" ,sum(item.trn_amt*typ.sign_adj) as AMT ,'x2'");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, csh_adj_typ typ, tender tender ");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and header.sts_id in ('S','N')  ");
		buf.append(" and tender.tender_grp_id = '03' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_id = '@2' ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid ");
		buf.append(" and item.csh_adj_typ_id = typ.csh_adj_typ_id ");
		buf.append(" and item.tender_id = tender.tender_id ");
		buf.append(" group by header.store_id ,tender.tender_id ");

		buf.append(" union ");

		buf.append(" select header.store_id, ");
		buf.append(" tender.tender_id ");
		buf.append(" ,sum(item.trn_amt)*-1 as AMT ,'x3'");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, csh_adj_typ typ, tender tender ");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and header.sts_id in ('S','N')  ");
		buf.append(" and tender.tender_grp_id = '03' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_id = '@2' ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid ");
		buf.append(" and item.csh_adj_typ_id = typ.csh_adj_typ_id ");
		buf.append(" and item.credit_tender_id = tender.tender_id ");
		buf.append(" group by header.store_id,tender.tender_id ");
		//buf.append(" --Sum Credit Card  ");

		buf.append(" union ");

		//buf.append(" --ZipZap, CR  ");
		buf.append(" select a.store_id ");
		buf.append(" ,a.tender_id ");
		buf.append(" ,sum(a.trn_amt*-1) as AMT ,'x4'");
		buf.append(" From csh_manage_tender a, tender b ");
		buf.append(" where a.csh_manage_typ in ('ZZP','CRV') ");
		buf.append(" and b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.is_active = 'Y' ");
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id, a.tender_id ");
		//buf.append(" --ZipZap, CR  ");

		buf.append(" union ");
		//buf.append(" --INC, OTH ");
		buf.append(" select a.store_id ");
		buf.append(" ,a.tender_id ");
		buf.append(" ,sum(a.trn_amt) as AMT ,'x5'");
		buf.append(" From csh_manage_tender a, tender b ");
		buf.append(" where a.csh_manage_typ in ('INC','OTH') ");
		buf.append(" and b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id,a.tender_id ");
		//buf.append(" --INC, OTH ");
		buf.append(" ) result ");
		buf.append(" group by result.store_id,result.tender_Id ");
		//buf.append(" ----EDC ");
		buf.append(" union ");
		//buf.append(" --OPM ");
		buf.append(" select a.store_id, a.tender_id ");
		buf.append(" ,0 as EDC ");
		buf.append(" ,sum(trn_amt) as DCC ");
		buf.append(" from csh_manage_tender a, tender b ");
		buf.append(" where b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		//buf.append(" --and a.rate_payment = 'S' ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.csh_manage_typ = 'DCC' ");
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.store_id,a.tender_id ");
		//buf.append(" --OPM ");
		buf.append(" )result ");
		buf.append(" group by result.store_id,result.tender_id ");
		buf.append(" )resultToTal ");
		buf.append(" ) a, store st  Where A.STORE_ID = ST.STORE_ID ");
		
		String query = buf.toString();	
		query = query.replaceAll("@2", storeId);
		query = query.replaceAll("@1", sf.format(fromDate));
		
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CreditCaseONMapper()));
	}
	
	public List displayCreditCaseOY(String storeId, Date fromDate)throws Exception{
		StringBuffer buf = new StringBuffer();
		
		buf.append(" select A.STORE_ID||' - '|| st.ABB_NM as STORE_ID, a.tender_id, a.AMT  from (   ");
		buf.append(" select   ");
		buf.append(" resultToTal.store_id as store_id ");
		buf.append(" ,resultToTal.tender_id as tender_id ");
		buf.append(" ,resultToTal.DCC - resultToTal.SRATE - round((nvl(resultToTal.DCC,0) * nvl(resultToTal.NRATE,0)),2) - round(((nvl(resultToTal.SRATE,0)+(nvl(resultToTal.DCC,0)*nvl(resultToTal.NRATE,0)))*0.07),2) as AMT ");
		buf.append(" from ");
		buf.append(" ( ");

		buf.append(" select result.store_id ,result.tender_id  ");
		buf.append(" ,sum(EDC) as EDC ");
		buf.append(" ,sum(SRATE) as SRATE ");
		buf.append(" ,sum(DCC) as DCC ");
		buf.append(" ,sum(NRATE) as NRATE ");
		buf.append(" from ");
		buf.append(" ( ");

		buf.append(" select result.store_id ");
		buf.append(" ,result.tender_id   ");
		buf.append(" , sum(AMT) AS EDC ");
		buf.append(" , 0 AS SRATE ");
		buf.append(" , 0 AS DCC ");
		buf.append(" , 0 as NRATE ");
		buf.append(" from  ");
		buf.append(" ( ");

		buf.append(" select s.store_id, td.tender_id, 0 as amt ");
		buf.append(" from tender td, store s ");
		buf.append(" where td.tender_grp_id = '03'  ");
		buf.append(" and (td.is_pos = 'Y' or td.is_so = 'Y') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and s.store_id = '@2' ");
		}
		
		buf.append(" group by s.store_id, td.tender_id ");
		
		buf.append(" union ");

		buf.append(" select result.*  ");
		buf.append(" from ( ");
		buf.append(" select b.store_id, ");
		buf.append(" a.tender_id ");
		buf.append(" ,sum(b.trn_amt) as AMT ");
		buf.append(" From  ");
		buf.append(" ( ");
		buf.append(" select a.tender_id from csh_manage_tender a, tender b ");
		buf.append(" where b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");

		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.csh_manage_typ = 'DCC' ");
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.tender_id ");
		buf.append(" ) a, CSH_TRN b ");
		buf.append(" where b.trn_dt = to_date('@1','DD/MM/YYYY') ");
		buf.append(" and b.csh_trn_typ_id in ('POS','SO','CNP','CNS') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and b.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by b.store_id, a.tender_id ");

		buf.append(" union  ");

		buf.append(" select header.store_id  ");
		buf.append(" ,tender.tender_id ");
		buf.append(" ,sum(item.trn_amt) as AMT ");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, csh_adj_typ typ, tender tender ");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and header.sts_id in ('S','N')  ");
		buf.append(" and tender.tender_grp_id = '03' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_id = '@2' ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid ");
		buf.append(" and item.csh_adj_typ_id = typ.csh_adj_typ_id ");
		buf.append(" and item.tender_id = tender.tender_id ");
		buf.append(" group by header.store_id ,tender.tender_id ");

		buf.append(" union ");

		buf.append(" select header.store_id, ");
		buf.append(" tender.tender_id ");
		buf.append(" ,sum(item.trn_amt)*-1 as AMT ");
		buf.append(" from csh_trn_adj header, csh_trn_adj_item item, csh_adj_typ typ, tender tender ");
		buf.append(" where header.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and header.sts_id in ('S','N')  ");
		buf.append(" and tender.tender_grp_id = '03' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and header.store_id = '@2' ");
		}
		buf.append(" and header.csh_trn_adj_oid = item.csh_trn_adj_oid ");
		buf.append(" and item.csh_adj_typ_id = typ.csh_adj_typ_id ");
		buf.append(" and item.credit_tender_id = tender.tender_id ");
		buf.append(" group by header.store_id,tender.tender_id ");


		buf.append(" union ");

		buf.append(" select a.store_id ");
		buf.append(" ,a.tender_id ");
		buf.append(" ,sum(a.trn_amt*-1) as AMT ");
		buf.append(" From csh_manage_tender a, tender b ");
		buf.append(" where a.csh_manage_typ in ('ZZP','CRV') ");
		buf.append(" and b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.is_active = 'Y' ");
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id, a.tender_id ");

		buf.append(" union ");
		buf.append(" select a.store_id ");
		buf.append(" ,a.tender_id ");
		buf.append(" ,sum(a.trn_amt) as AMT ");
		buf.append(" From csh_manage_tender a, tender b ");
		buf.append(" where a.csh_manage_typ in ('INC','OTH') ");
		buf.append(" and b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id  ");
		buf.append(" group by a.store_id,a.tender_id ");

		buf.append(" )result  ");
		buf.append(" ,( ");
		buf.append(" select a.tender_id from csh_manage_tender a, tender b ");
		buf.append(" where b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.csh_manage_typ = 'DCC' ");
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.tender_id ");
		buf.append(" )DCC ");
		buf.append(" where result.tender_id = DCC.tender_id ");
		buf.append("   ");

		buf.append(" ) result ");
		buf.append(" group by result.store_id,result.tender_Id ");


		buf.append(" union ");

		buf.append(" select a.store_id,a.tender_id ");
		buf.append(" ,0 as EDC ");
		buf.append(" ,round(sum(nvl(a.trn_amt,0)*nvl(a.rate,0)),2) as SRATE ");
		buf.append(" ,0 AS DCC ");
		buf.append(" ,0 as NRATE ");
		buf.append(" from csh_manage_tender a ");
		buf.append(" ,( ");
		buf.append(" select a.tender_id from csh_manage_tender a, tender b ");
		buf.append(" where b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");

		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.csh_manage_typ = 'DCC' ");
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.tender_id ");
		buf.append(" )b ");
		buf.append(" where a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.rate_payment = 'S' ");
		buf.append(" and a.csh_manage_typ IN ('INC','CHR','OTH') ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.store_id,a.tender_id ");

		buf.append(" union ");

		buf.append(" select a.store_id, a.tender_id ");
		buf.append(" ,0 as EDC ");
		buf.append(" ,0 as SRATE ");
		buf.append(" ,sum(trn_amt) as DCC ");
		buf.append(" ,0 as NRATE ");
		buf.append(" from csh_manage_tender a, tender b ");
		buf.append(" where b.tender_grp_id = '03' ");
		buf.append(" and a.trn_dt = to_Date('@1','DD/MM/YYYY') ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@2' ");
		}
		buf.append(" and a.csh_manage_typ = 'DCC' ");
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" group by a.store_id,a.tender_id ");

		buf.append(" union ");

		buf.append(" select result.store_id, result.tender_id ");
		buf.append(" ,0 as EDC ");
		buf.append(" ,0 as SRATE ");
		buf.append(" ,0 as DCC ");
		buf.append(" ,sum(NRATE) as NRATE ");
		buf.append(" from ");
		buf.append(" ( ");
		buf.append("     select s.store_id, td.tender_id, 0 as NRATE ");
		buf.append("     from tender td, store s ");
		buf.append("     where td.tender_grp_id = '03'  ");
		buf.append("     and (td.is_pos = 'Y' or td.is_so = 'Y') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append("     and s.store_id = '@2' ");
		}
		buf.append("     group by s.store_id, td.tender_id ");

		buf.append("     union ");

		buf.append("     select s.store_id,a.tender_id ");
		buf.append("     ,nrate.rate as NRATE ");
		buf.append("     from tender a, store s ");
		buf.append("     ,(select c.tender_id as tender_id, c.rate ");
		buf.append("         from csh_rate_tender c,  ");
		buf.append("         ( ");
		buf.append("             select tender_id, max(last_upd_dttm) as last_upd_dttm ");
		buf.append("             from csh_rate_tender ");
		buf.append("             where is_active = 'Y' ");
		buf.append("             and rate_typ = 'N' ");
		buf.append("             and rate_From <= to_Date('@1','DD/MM/YYYY') ");
		buf.append("             and rate_To >= to_Date('@1','DD/MM/YYYY') ");
		buf.append("             group by tender_id ");
		buf.append("         ) grp_c ");
		buf.append("         where c.tender_id = grp_c.tender_id ");
		buf.append("         and c.last_upd_dttm = grp_c.last_upd_dttm ");
		buf.append("     )nrate ");
		buf.append("     where a.tender_id = nrate.tender_id ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append("     and s.store_id = '@2' ");
		}
		buf.append(" )result ");
		buf.append(" group by result.store_id, result.tender_id ");

		buf.append(" )result ");
		buf.append(" group by result.store_id,result.tender_id ");
		buf.append(" )resultToTal ");
		
		buf.append(" ) a, store st  Where A.STORE_ID = ST.STORE_ID ");
		
		String query = buf.toString();	
		query = query.replaceAll("@2", storeId);
		query = query.replaceAll("@1", sf.format(fromDate));
		
		System.out.println("query:"+query);
		
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CreditCaseONMapper()));
	}
	
	public List displayCreditCaseON(String storeId, Date fromDate)throws Exception{
		StringBuffer buf = new StringBuffer();
		
		buf.append(" select st.store_id || ' - ' || st.abb_nm as store_id ");
		buf.append(" , result.tender_id as tender_id ");
		buf.append(" , result.SUM_TOTAL as SUM_TOTAL ");

		buf.append(" from store st ");
		buf.append(" ,( ");
		buf.append(" select result.store_id as store_id, result.tender_Id as tender_id, sum(SUM_TOTAL) as SUM_TOTAL ");
		buf.append(" from ");
		buf.append(" ( ");
		buf.append(" select s.store_id as store_id, td.tender_id ");
		buf.append(" , 0 as SUM_TOTAL ");
		buf.append(" from tender td, store s ");
		buf.append(" where td.tender_grp_id = '03'  ");
		//ADD By Bum 07/05/2013 Where  AND (is_pos = 'Y' OR is_so = 'Y')
		buf.append(" and (td.is_pos = 'Y' or td.is_so = 'Y') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and s.store_id = '@1'  ");
		}
		buf.append(" group by s.store_id, td.tender_id ");

		buf.append(" union ");

		buf.append(" select a.store_id as store_id, a.tender_Id as tender_id ");
		buf.append(" ,sum(a.trn_amt) as SUM_TOTAL ");
		buf.append(" from csh_manage_tender a, tender b ");
		buf.append(" where a.csh_manage_typ = 'DCC' ");
		buf.append(" and a.trn_dt = to_date('@2','DD/MM/YYYY') ");
		buf.append(" and a.tender_id = b.tender_id ");
		buf.append(" and a.is_active = 'Y' ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
			buf.append(" and a.store_id = '@1'  ");
		}
		buf.append(" group by a.store_id, a.tender_Id ");
		buf.append(" ) result ");
		buf.append(" group by result.store_id, result.tender_Id ");
		buf.append(" ) result ");
		buf.append(" where result.store_id = st.store_id ");

		
		String query = buf.toString();	
		query = query.replaceAll("@1", storeId);
		query = query.replaceAll("@2", sf.format(fromDate));
		
		System.out.println("query:"+query);
		
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CreditCaseONMapper()));
	}
	
	public List getDiscountTotal(Date tranDate)throws Exception{
		List resultList = null;
		
		// EDIT VAT ***
		String sqlString = 	" SELECT A.POS_ID, A.MSG_VAL, SUM(A.DSCNT_AMT), SUM(A.CNT) "
			+" FROM  "   
			+" ( "
	
			+" SELECT 'SALE' AS TYP, "
			+" S.POS_ID, "
			+" CASE WHEN D.MSG_VAL = 'YI32' THEN 'YI10'  "
			+" WHEN D.MSG_VAL = 'YI33' AND S.MBR_DSCNT_TYP_ID = 'M4' THEN 'YI21' "
			+" WHEN D.MSG_VAL = 'YI33' AND S.MBR_DSCNT_TYP_ID <> 'M4' THEN 'YI20' "
			+" WHEN D.MSG_VAL = 'YI03' THEN 'YI10' "
			+" WHEN D.MSG_VAL = 'YI37' THEN 'YI21'  "
			+" WHEN D.DSCNT_COND_TYP_ID = 'YI57' THEN 'YI21'  "
			+" ELSE D.MSG_VAL END AS MSG_VAL "
			+" , SUM( CASE WHEN SI.TAX_CODE_ID ='VX' THEN NVL(ID.DSCNT_AMT,0) ELSE ROUND((NVL(ID.DSCNT_AMT,0)/(100 + (100 * TC.TAX_RATE)))*100, 2) END )* (-1) AS DSCNT_AMT, COUNT(DISTINCT SI.SALES_TRN_ITEM_OID) AS CNT "
			+" FROM SALES_TRN S, SALES_TRN_ITEM SI, ITEM_DSCNT ID, DSCNT_COND_TYP D , TAX_CODE TC "
			+" WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID  "
			+" AND SI.SALES_TRN_ITEM_OID = ID.SALES_TRN_ITEM_OID "
			+" AND ID.DSCNT_COND_TYP_ID = D.DSCNT_COND_TYP_ID "
			+" AND SI.TAX_CODE_ID = TC.TAX_CODE_ID " 
			+" AND S.STS_ID = 'P' ";
			if(tranDate!=null && !tranDate.equals("")){
				sqlString += " AND S.TRN_DT = TO_DATE('"+sf.format(tranDate)+ "','DD/MM/YYYY') "; 
			}
			  
			sqlString +=" GROUP BY S.POS_ID, D.MSG_VAL, S.MBR_DSCNT_TYP_ID, D.DSCNT_COND_TYP_ID "
			+" UNION "
			+" SELECT 'CN' AS TYP, "
			+" '000' as POS_ID, "
			+" CASE WHEN D.MSG_VAL = 'YI32' THEN 'YI10'  "
			+" WHEN D.MSG_VAL = 'YI33' AND S.MBR_DSCNT_TYP_ID = 'M4' THEN 'YI21' "
			+" WHEN D.MSG_VAL = 'YI33' AND S.MBR_DSCNT_TYP_ID <> 'M4' THEN 'YI20' "
			+" WHEN D.MSG_VAL = 'YI03' THEN 'YI10' "
			+" WHEN D.MSG_VAL = 'YI37' THEN 'YI21'  "
			+" WHEN D.DSCNT_COND_TYP_ID = 'YI57' THEN 'YI21'  "
			+" ELSE D.MSG_VAL END AS MSG_VAL "
			+" , SUM( CASE WHEN TI.TAX_CODE ='VX' THEN NVL(ID.DSCNT_AMT,0) ELSE ROUND((NVL(ID.DSCNT_AMT,0)/(100 + (100 * TC.TAX_RATE)))*100, 2) END ) AS DSCNT_AMT , COUNT(DISTINCT TI.TAX_INV_ITEM_OID) AS CNT "
			+" FROM TAX_INV T, TAX_INV_ITEM TI, TAX_ITEM_DSCNT ID, DSCNT_COND_TYP D, SALES_TRN S, TAX_CODE TC "
			+" WHERE T.TAX_INV_OID = TI.TAX_INV_OID "
			+" AND TI.TAX_INV_ITEM_OID = ID.TAX_INV_ITEM_OID "
			+" AND ID.DSCNT_COND_TYP_ID = D.DSCNT_COND_TYP_ID "  
			+" AND TI.TAX_CODE = TC.TAX_CODE_ID "  
			+" AND T.TAX_INV_TYP_ID = 'C' ";
			if(tranDate!=null && !tranDate.equals("")){
				sqlString +=" AND T.ISSUE_DT = TO_DATE('"+sf.format(tranDate)+ "','DD/MM/YYYY') ";
			}
	
			sqlString +=" AND T.STORE_ID = S.STORE_ID "
			+" AND T.TRN_DT = S.TRN_DT "
			+" AND T.POS_ID = T.POS_ID "
			+" AND T.TICKET_NO = S.TICKET_NO "
			+" GROUP BY T.POS_ID, D.MSG_VAL, S.MBR_DSCNT_TYP_ID, D.DSCNT_COND_TYP_ID  "
			+" )A "
			+" GROUP BY A.POS_ID, A.MSG_VAL ";     
			  
		System.out.println("sqlString:" + sqlString);
		JdbcTemplate jt = getJdbcTemplate();
		
		try{
		     resultList = jt.query(sqlString, new RowMapperResultReader(new DiscountTotalMapper()));  
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return resultList;
	}
	
	public List getRetailTotal(Date tranDate)throws Exception{
		StringBuffer buf = new StringBuffer();
		
//		buf.append(" SELECT 'POS' AS TYP, S.POS_ID, NVL(SUM(SI.NET_ITEM_AMT),0) AS NET_AM, COUNT(SI.SALES_TRN_ITEM_OID) AS CNT ");
//		buf.append(" FROM SALES_TRN S, SALES_TRN_ITEM SI ");
//		buf.append(" WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID  ");
//		buf.append(" AND S.STS_ID = 'P' ");
//		if(tranDate!=null && !tranDate.equals("")){
//			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD/MM/YYYY') ");
//		}
//		
//		buf.append(" AND S.TYP_ID = 'P' ");
//		buf.append(" GROUP BY S.POS_ID ");
//		buf.append(" UNION ");
//		buf.append(" SELECT 'SO' AS TYP, S.POS_ID, NVL(SUM(SI.NET_ITEM_AMT),0) AS NET_AMT, COUNT(SI.SALES_TRN_ITEM_OID) AS CNT ");
//		buf.append(" FROM SALES_TRN S, SALES_TRN_ITEM SI ");
//		buf.append(" WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID  ");
//		buf.append(" AND S.STS_ID = 'P' ");
//		if(tranDate!=null && !tranDate.equals("")){
//			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD/MM/YYYY') ");
//		}
//
//		buf.append(" AND S.TYP_ID = 'S' ");
//		buf.append(" GROUP BY S.POS_ID ");
//		buf.append(" UNION ");
//		buf.append(" SELECT 'CNP' AS TYP, T.POS_ID, NVL(SUM(TI.NET_ITEM_AMT),0)*(-1) AS NET_AMT, COUNT(TI.TAX_INV_ITEM_OID) AS CNT ");
//		buf.append(" FROM TAX_INV T, TAX_INV_ITEM TI ");
//		buf.append(" WHERE T.TAX_INV_OID = TI.TAX_INV_OID ");
//		buf.append(" AND T.TAX_INV_TYP_ID = 'C' ");
//		buf.append(" AND T.SALES_TRN_TYP = 'P' ");
//		if(tranDate!=null && !tranDate.equals("")){
//			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD/MM/YYYY') ");
//		}
//		buf.append(" GROUP BY T.POS_ID ");
//
//		buf.append(" UNION ");
//		buf.append(" SELECT 'CNS' AS TYP, T.POS_ID, NVL(SUM(TI.NET_ITEM_AMT),0)*(-1) AS NET_AMT, COUNT(TI.TAX_INV_ITEM_OID) AS CNT ");
//		buf.append(" FROM TAX_INV T, TAX_INV_ITEM TI ");
//		buf.append(" WHERE T.TAX_INV_OID = TI.TAX_INV_OID ");
//		buf.append(" AND T.TAX_INV_TYP_ID = 'C' ");
//		buf.append(" AND T.SALES_TRN_TYP = 'S' ");
//		if(tranDate!=null && !tranDate.equals("")){
//			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD/MM/YYYY') ");
//		}
//		buf.append(" GROUP BY T.POS_ID ");
		
		// EDIT VAT ***
		buf.append("SELECT 'POS' AS TYP, ");
		buf.append("  S.POS_ID, ");
		buf.append("  SUM( ");
		buf.append("  CASE ");
		buf.append("    WHEN SI.TAX_CODE_ID ='VX' ");
		buf.append("    THEN NVL(SI.NET_ITEM_AMT,0) ");
		buf.append(" ELSE ");
		buf.append("  ROUND((NVL(SI.NET_ITEM_AMT, 0) / (100 + (100 * TC.TAX_RATE))) * 100, 2) ");
		buf.append("  END )                        AS NET_AM, ");
		buf.append("  COUNT(SI.SALES_TRN_ITEM_OID) AS CNT ");
		buf.append("FROM SALES_TRN S, ");
		buf.append("  SALES_TRN_ITEM SI , TAX_CODE TC ");
		buf.append("WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID ");
		buf.append("AND SI.TAX_CODE_ID = TC.TAX_CODE_ID ");
		buf.append("AND S.STS_ID          = 'P' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD/MM/YYYY') ");
		}
		buf.append("AND S.TYP_ID          = 'P' ");
		buf.append("GROUP BY S.POS_ID ");
		buf.append("UNION ");
		buf.append("SELECT 'SO' AS TYP, ");
		buf.append("  S.POS_ID, ");
		buf.append("  SUM( ");
		buf.append("  CASE ");
		buf.append("    WHEN SI.TAX_CODE_ID ='VX' ");
		buf.append("    THEN NVL(SI.NET_ITEM_AMT,0) ");
		buf.append(" ELSE ");
		buf.append("  ROUND((NVL(SI.NET_ITEM_AMT, 0) / (100 + (100 * TC.TAX_RATE))) * 100, 2) ");
		buf.append("  END )                        AS NET_AMT, ");
		buf.append("  COUNT(SI.SALES_TRN_ITEM_OID) AS CNT ");
		buf.append("FROM SALES_TRN S, ");
		buf.append("  SALES_TRN_ITEM SI, TAX_CODE TC ");
		buf.append("WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID ");
		buf.append("AND SI.TAX_CODE_ID = TC.TAX_CODE_ID ");
		buf.append("AND S.STS_ID          = 'P' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD/MM/YYYY') ");
		}
		buf.append("AND S.TYP_ID          = 'S' ");
		buf.append("GROUP BY S.POS_ID ");
		buf.append("UNION ");
		buf.append("SELECT 'CNP' AS TYP, ");
		buf.append("  '000' as POS_ID, ");
		buf.append("  SUM( ");
		buf.append("  CASE ");
		buf.append("    WHEN TI.TAX_CODE ='VX' ");
		buf.append("    THEN NVL(TI.NET_ITEM_AMT,0) ");
		buf.append("    ELSE ROUND((NVL(TI.NET_ITEM_AMT,0)/ (100 + (100 * TC.TAX_RATE)) )*100, 2) ");
		buf.append("  END )                               *(-1) AS NET_AMT, ");
		buf.append("  COUNT(TI.TAX_INV_ITEM_OID)                AS CNT ");
		buf.append("FROM TAX_INV T, ");
		buf.append("  TAX_INV_ITEM TI , TAX_CODE TC ");
		buf.append("WHERE T.TAX_INV_OID  = TI.TAX_INV_OID ");
		buf.append("AND TI.TAX_CODE = TC.TAX_CODE_ID ");
		buf.append("AND T.TAX_INV_TYP_ID = 'C' ");
		buf.append("AND T.SALES_TRN_TYP  = 'P' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD/MM/YYYY') ");
		}
		//buf.append("GROUP BY T.POS_ID ");
		buf.append("UNION ");
		buf.append("SELECT 'CNS' AS TYP, ");
		buf.append("  '000' as POS_ID, ");
		buf.append("  SUM( ");
		buf.append("  CASE ");
		buf.append("    WHEN TI.TAX_CODE ='VX' ");
		buf.append("    THEN NVL(TI.NET_ITEM_AMT,0) ");
		buf.append("    ELSE ROUND((NVL(TI.NET_ITEM_AMT,0)/ (100 + (100 * TC.TAX_RATE)) )*100, 2) ");
		buf.append("  END)                                *(-1) AS NET_AMT, ");
		buf.append("  COUNT(TI.TAX_INV_ITEM_OID)                AS CNT ");
		buf.append("FROM TAX_INV T, ");
		buf.append("  TAX_INV_ITEM TI, TAX_CODE TC ");
		buf.append("WHERE T.TAX_INV_OID  = TI.TAX_INV_OID ");
		buf.append("AND TI.TAX_CODE = TC.TAX_CODE_ID ");
		buf.append("AND T.TAX_INV_TYP_ID = 'C' ");
		buf.append("AND T.SALES_TRN_TYP  = 'S' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD/MM/YYYY') ");
		}
		//buf.append("GROUP BY T.POS_ID");
		
		
		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(tranDate));
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		
		return jt.query(query, new RowMapperResultReader(new RetailTotalMapper()));
	}
	
	public List getTaxTotal(Date tranDate)throws Exception{
		StringBuffer buf = new StringBuffer();
		
		// EDIT VAT ***
		buf.append(" SELECT POS_ID, SUM(VAT) AS VAT, SUM(CNT) AS CNT ");
		buf.append(" FROM ");
		buf.append(" (  ");
		buf.append(" SELECT 'POS' AS TYP, S.POS_ID, COUNT(SI.SALES_TRN_ITEM_OID) AS CNT ");
		buf.append(" , ((TC.TAX_RATE * 100) * NVL(SUM(SI.NET_ITEM_AMT),0)) / (100 + (TC.TAX_RATE * 100)) AS VAT ");
		buf.append(" FROM SALES_TRN S, SALES_TRN_ITEM SI, TAX_CODE TC ");
		buf.append(" WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID  ");
		buf.append(" AND SI.TAX_CODE_ID = TC.TAX_CODE_ID  ");
		buf.append(" AND S.STS_ID = 'P' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD/MM/YYYY') "); 
		}
		buf.append(" AND S.TYP_ID = 'P' ");
		buf.append(" AND SI.TAX_CODE_ID <> 'VX' ");
		buf.append(" GROUP BY S.POS_ID, SI.TAX_CODE_ID, TC.TAX_RATE ");
		buf.append(" UNION ");
		buf.append(" SELECT 'SO' AS TYP, S.POS_ID, COUNT(SI.SALES_TRN_ITEM_OID) AS CNT  ");
		buf.append(" , ((TC.TAX_RATE * 100) * NVL(SUM(SI.NET_ITEM_AMT),0)) / (100 + (TC.TAX_RATE * 100)) AS VAT ");
		buf.append(" FROM SALES_TRN S, SALES_TRN_ITEM SI, TAX_CODE TC ");
		buf.append(" WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID ");
		buf.append(" AND SI.TAX_CODE_ID = TC.TAX_CODE_ID  ");
		buf.append(" AND S.STS_ID = 'P' ");  
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD/MM/YYYY') ");
		}
		buf.append(" AND S.TYP_ID = 'S' ");
		buf.append(" AND SI.TAX_CODE_ID <> 'VX' ");
		buf.append(" GROUP BY S.POS_ID, SI.TAX_CODE_ID, TC.TAX_RATE ");
		buf.append(" UNION ");
		buf.append(" SELECT 'CNP' AS TYP, '000' as POS_ID, COUNT(TI.TAX_INV_ITEM_OID)*(-1) AS CNT ");
		buf.append(" , ((TC.TAX_RATE * 100) * NVL(SUM(TI.NET_ITEM_AMT),0)) / (100 + (TC.TAX_RATE * 100)) *(-1) AS VAT ");
		buf.append(" FROM TAX_INV T, TAX_INV_ITEM TI, TAX_CODE TC ");
		buf.append(" WHERE T.TAX_INV_OID = TI.TAX_INV_OID ");
		buf.append(" AND TI.TAX_CODE = TC.TAX_CODE_ID  ");
		buf.append(" AND T.TAX_INV_TYP_ID = 'C' ");
		buf.append(" AND T.SALES_TRN_TYP = 'P' ");
		buf.append(" AND TI.TAX_CODE <> 'VX' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD/MM/YYYY') ");
		}
		buf.append(" GROUP BY T.POS_ID, TI.TAX_CODE, TC.TAX_RATE ");
		buf.append(" UNION ");
		buf.append(" SELECT 'CNS' AS TYP, '000' as POS_ID, COUNT(TI.TAX_INV_ITEM_OID)*(-1) AS CNT ");
		buf.append(" , ((TC.TAX_RATE * 100) * NVL(SUM(TI.NET_ITEM_AMT),0)) / (100 + (TC.TAX_RATE * 100)) *(-1) AS VAT ");
		buf.append(" FROM TAX_INV T, TAX_INV_ITEM TI, TAX_CODE TC ");
		buf.append(" WHERE T.TAX_INV_OID = TI.TAX_INV_OID ");
		buf.append(" AND TI.TAX_CODE = TC.TAX_CODE_ID  ");
		buf.append(" AND T.TAX_INV_TYP_ID = 'C' ");
		buf.append(" AND T.SALES_TRN_TYP = 'S' ");
		buf.append(" AND TI.TAX_CODE <> 'VX' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD/MM/YYYY') ");
		}
		buf.append(" GROUP BY T.POS_ID, TI.TAX_CODE, TC.TAX_RATE ");
		buf.append(" ) A ");
		buf.append(" GROUP BY POS_ID ");

		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(tranDate));
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new TaxTotalMapper()));
	}
	
	
	public List getTendorTotal(Date tranDate)throws Exception{
		StringBuffer buf = new StringBuffer();  
		
		buf.append(" SELECT POS_ID, POS_COND_TYP, SUM(TRN_AMT) AS TRN_AMT, SUM(CNT) AS CNT ");
		buf.append(" FROM ");
		buf.append(" (  ");
		buf.append(" SELECT 'POS' AS TYP, S.POS_ID, T.POS_COND_TYP, SUM(C.TRN_AMT)AS TRN_AMT, COUNT(C.CSH_TRN_OID) AS CNT ");
		buf.append(" FROM SALES_TRN S, CSH_TRN C, TENDER T ");
		buf.append(" WHERE S.SALES_TRN_OID = C.SALES_TRN_OID  ");
		buf.append(" AND C.TENDER_ID = T.TENDER_ID ");
		buf.append(" AND S.STS_ID = 'P'  ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD-MM-YYYY') "); 
		}
		buf.append(" AND S.TYP_ID = 'P' ");
		buf.append(" AND C.CSH_TRN_TYP_ID = 'POS' ");
		buf.append(" GROUP BY S.POS_ID, T.POS_COND_TYP ");
		buf.append(" UNION");
		buf.append(" SELECT 'SO' AS TYP, S.POS_ID, T.POS_COND_TYP, SUM(C.TRN_AMT)AS TRN_AMT, COUNT(C.CSH_TRN_OID) AS CNT ");
		buf.append(" FROM SALES_TRN S, CSH_TRN C, TENDER T ");
		buf.append(" WHERE S.SALES_TRN_OID = C.SALES_TRN_OID  ");
		buf.append(" AND C.TENDER_ID = T.TENDER_ID ");
		buf.append(" AND S.STS_ID = 'P' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD-MM-YYYY') ");
		}
		buf.append(" AND S.TYP_ID = 'S' ");
		buf.append(" AND C.CSH_TRN_TYP_ID = 'SO' ");
		buf.append(" GROUP BY S.POS_ID, T.POS_COND_TYP ");
		buf.append(" UNION ");
		buf.append(" SELECT 'CNP' AS TYP, '000' as POS_ID, TD.POS_COND_TYP, SUM(IT.TENDER_AMT) AS TRN_AMT, COUNT(IT.INV_TENDER_OID) AS CNT ");
		buf.append(" FROM TAX_INV T, INV_TENDER IT, TENDER TD ");
		buf.append(" WHERE T.TAX_INV_OID = IT.TAX_INV_OID ");
		buf.append(" AND IT.TENDER_ID = TD.TENDER_ID ");
		buf.append(" AND T.TAX_INV_TYP_ID = 'C' ");
		buf.append(" AND T.SALES_TRN_TYP = 'P' ");
		if(tranDate!=null && !tranDate.equals("")){
			//buf.append(" AND T.ISSUE_DT = TO_DATE('19-07-2011','DD-MM-YYYY')  ");
			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD-MM-YYYY')  ");
		}
		buf.append(" GROUP BY T.POS_ID, TD.POS_COND_TYP ");
		buf.append(" UNION ");
		buf.append(" SELECT 'CNS' AS TYP, '000' as POS_ID, TD.POS_COND_TYP, SUM(IT.TENDER_AMT) AS TRN_AMT, COUNT(IT.INV_TENDER_OID) AS CNT ");
		buf.append(" FROM TAX_INV T, INV_TENDER IT, TENDER TD ");
		buf.append(" WHERE T.TAX_INV_OID = IT.TAX_INV_OID ");
		buf.append(" AND IT.TENDER_ID = TD.TENDER_ID ");
		buf.append(" AND T.TAX_INV_TYP_ID = 'C' ");
		buf.append(" AND T.SALES_TRN_TYP = 'S' ");
		buf.append(" AND T.SALES_TRN_TYP = 'S' ");
		if(tranDate!=null && !tranDate.equals("")){
			//buf.append(" AND T.ISSUE_DT = TO_DATE('19-07-2011','DD-MM-YYYY')  ");
			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD-MM-YYYY')  ");
		}
		buf.append(" GROUP BY T.POS_ID, TD.POS_COND_TYP ");
		buf.append(" ) A ");
		buf.append(" GROUP BY POS_ID, POS_COND_TYP ");

		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(tranDate));
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new TendorTotalMapper()));
	}
	
	public List getTransactionTotal(Date tranDate)throws Exception{
		StringBuffer buf = new StringBuffer();  
		
		buf.append(" SELECT POS_ID, NVL(MIN(MIN_CREATE_DT),SYSDATE) AS MIN_CREATE_DT, NVL(MAX(MAX_CREATE_DT),SYSDATE) AS MAX_CREATE_DT ");
		buf.append(" FROM ");
		buf.append(" (  ");
		buf.append(" SELECT 'SALE' AS TYP, S.POS_ID, MIN(S.START_DT) AS MIN_CREATE_DT, MAX(S.START_DT) AS MAX_CREATE_DT ");
		buf.append(" FROM SALES_TRN S, SALES_TRN_ITEM SI ");
		buf.append(" WHERE S.SALES_TRN_OID = SI.SALES_TRN_OID   ");
		buf.append(" AND S.STS_ID = 'P' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND S.TRN_DT = TO_DATE('@1','DD-MM-YYYY') "); 
		}
		buf.append(" GROUP BY S.POS_ID ");
		
		buf.append(" UNION");
		buf.append(" SELECT 'CNP' AS TYP, '000' as POS_ID, MIN(T.CREATE_DTTM)AS MIN_CREATE_DT, MAX(T.CREATE_DTTM) AS MAX_CREATE_DT ");
		buf.append(" FROM TAX_INV T, TAX_INV_ITEM TI ");
		buf.append(" WHERE T.TAX_INV_OID = TI.TAX_INV_OID  ");
		buf.append(" AND T.TAX_INV_TYP_ID = 'C' ");
		if(tranDate!=null && !tranDate.equals("")){
			buf.append(" AND T.ISSUE_DT = TO_DATE('@1','DD-MM-YYYY') ");
		}
		buf.append(" GROUP BY T.POS_ID ");
		buf.append(" ) A ");
		buf.append(" GROUP BY POS_ID ");

		String query = buf.toString();	
		query = query.replaceAll("@1", sf.format(tranDate));
		System.out.println("query:"+query);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new TransactionTotalMapper()));
	}
	
	public List getCashierTransactionAdjusts(String storeId, Date fromDate,Date toDate) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT a.trn_dt,a.store_id,a.sts_id,a.document_no,a.cancel_document_no,a.jv_no,a.cancel_jv_no,a.inf_dttm,a.create_dttm,a.last_upd_dttm,a.update_user_id,a.CSH_TRN_ADJ_OID FROM CSH_TRN_ADJ a ");
		buf.append("WHERE a.STS_ID in ('S', 'C') ");
		if(storeId!=null && !storeId.equals("") && !storeId.equals("S999")){
			buf.append("AND a.STORE_ID='@store' ");
		}	
		buf.append("AND a.trn_dt>=TO_DATE('@date1', 'DD/MM/YYYY') ");
		buf.append("AND a.trn_dt<=TO_DATE('@date2', 'DD/MM/YYYY') ");
		buf.append("Order by a.trn_dt,a.store_id,a.document_no ASC");
		String query = buf.toString();	
		
		query = query.replaceAll("@store", storeId);
		query = query.replaceAll("@date1", sf.format(fromDate));
		query = query.replaceAll("@date2", sf.format(toDate));
		
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CashierTransactionAdjustsMapper()));
	}
	
	public List displayCashierTransactionAdjustItems(String storeId,Date fromDate, Date toDate, String type) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT C.TRN_DT,C.STORE_ID,D.POS_ID,D.TICKET_NO,D.CN_NO,D.CREDIT_CARD_NO,D.TENDER_ID,D.CREDIT_TENDER_ID,D.TRN_AMT,D.USER_ID,D.USER_NM," +
				"D.CAUSE_GRP_DESC,D.REF_INFO ");
		buf.append("FROM CSH_TRN_ADJ C INNER JOIN CSH_TRN_ADJ_ITEM D ON C.CSH_TRN_ADJ_OID = D.CSH_TRN_ADJ_OID ");
		buf.append("INNER JOIN csh_adj_typ t ON  t.csh_adj_typ_id=d.csh_adj_typ_id ");
		buf.append("WHERE ");
		buf.append("C.STS_ID='S' "); 
		if(storeId!=null && !storeId.equals("") && !storeId.equals("S999"))
		{
			buf.append("AND C.STORE_ID='@store' "); 
		}
		buf.append("AND c.trn_dt>=TO_DATE('@date1', 'dd/MM/yyyy') ");
		buf.append("AND c.trn_dt<=TO_DATE('@date2', 'dd/MM/yyyy') ");
		buf.append("AND t.CSH_ADJ_TYP_ID='@adjid'");	
		buf.append("ORDER BY c.trn_dt,C.STORE_ID,c.DOCUMENT_NO ASC");
		String query = buf.toString();	
		query = query.replaceAll("@store", storeId);
		query = query.replaceAll("@date1", sf.format(fromDate));
		query = query.replaceAll("@date2", sf.format(toDate));
		query = query.replaceAll("@adjid", type);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CashierTransactionAdjustItems()));
	}
	
	public List getCashierTransactionAdjustsItem(long adjoid) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT CSH_TRN_ADJ_ITEM_OID,TENDER_ID,CSH_ADJ_TYP_ID,CREDIT_TENDER_ID," +
	       		"TRN_AMT,POS_ID,TICKET_NO,USER_ID,USER_NM,REF_INFO,BU,CAUSE_GRP_DESC,CREDIT_CARD_NO,CN_NO ");
		buf.append("FROM CSH_TRN_ADJ_ITEM ");
		buf.append("WHERE CSH_TRN_ADJ_OID='@oid'");
		String query = buf.toString();	
		query = query.replaceAll("@oid", adjoid+"");
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CashierTranAdjustItems()));
}
	
	public CashierAdjustType getCashierAdjustType(String ID){
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT CSH_ADJ_TYP_ID,DESCRIPTION,SIGN_ADJ,IS_ACC_DEBIT,CSH_ACC_ID_DEBIT,IS_ACC_CREDIT,CSH_ACC_ID_CREDIT," +
				"IS_COST_DEBIT,COST_CENTER_DEBIT,IS_COST_CREDIT,COST_CENTER_CREDIT,IS_ACTIVE,IS_INTERFACE_SAP," +
				"IS_FIX_TENDER,IS_REQ_USER_ID,IS_REQ_TICKET,LAST_UPD_DTTM,UPDATE_USER_ID,LAST_PUB_DTTM,REF_PUB_ID,IS_REQ_BU ");
		buf.append("FROM CSH_ADJ_TYP ");
		buf.append("WHERE CSH_ADJ_TYP_ID='@ID'");
		String query = buf.toString();	
		query = query.replaceAll("@ID", ID);
		JdbcTemplate jt = getJdbcTemplate();
		List type=jt.query(query,new RowMapperResultReader(new CashierAdjustTypeMap()));
		if( type.isEmpty() ){
			return null;
		}
		return (CashierAdjustType)type.get(0);
	}
	
	class CashierAdjustTypeMap implements RowMapper{
		public Object mapRow(ResultSet rs, int arg1) throws SQLException {
			CashierAdjustType type=new CashierAdjustType();
			type.setCashierAdjTypId(rs.getString(1));
			type.setDescription(rs.getString(2));
			type.setSignAdj(rs.getInt(3));
			type.setIsAccDebit(rs.getString(4).equals("Y") ? true : false);
			type.setCashierAccIdDebit(rs.getString(5));
			type.setIsAccCredit(rs.getString(6).equals("Y") ? true : false);
			type.setCashierAccIdCredit(rs.getString(7));
			type.setIsCostDebit(rs.getString(8).equals("Y") ? true : false);
			type.setCostCenterDebit(rs.getString(9));
			type.setIsCostCredit(rs.getString(10).equals("Y") ? true : false);
			type.setCostCenterCredit(rs.getString(11));
			type.setIsActive(rs.getString(12).equals("Y") ? true : false);
			type.setIsInterFaceSap(rs.getString(13).equals("Y") ? true : false);
			type.setIsFixTender(rs.getString(14).equals("Y") ? true : false);
			type.setIsReqUserId(rs.getString(15).equals("Y") ? true : false);
			type.setIsReqTicket(rs.getString(16).equals("Y") ? true : false);
			type.setLastUpdDttm(rs.getDate(17));
			type.setUpdateUserId(rs.getString(18));
			type.setLastPubDttm(rs.getDate(19));
			type.setRefPubId(rs.getString(20));		
			type.setIsReqBu(rs.getString(21).equals("Y") ? Boolean.TRUE : Boolean.FALSE);
			return type;
		}
	}
	
	class CashierTransactionAdjustItems implements RowMapper{

		public Object mapRow(ResultSet rs, int arg1) throws SQLException {
			
			CashierTransactionAdjust cashierTransactionAdjust=new CashierTransactionAdjust();
			cashierTransactionAdjust.setTransactionDate(rs.getDate(1));
			cashierTransactionAdjust.setStoreId(rs.getString(2));
			
			CashierTransactionAdjustItem cashierTransactionAdjustItem = new CashierTransactionAdjustItem();

			cashierTransactionAdjustItem.setCashierTransactionAdjust(cashierTransactionAdjust);
			cashierTransactionAdjustItem.setPosId(rs.getString(3));
			cashierTransactionAdjustItem.setTicketNo(rs.getString(4));
			cashierTransactionAdjustItem.setCnNo(rs.getString(5));
			cashierTransactionAdjustItem.setCreditCardNo(rs.getString(6));
			cashierTransactionAdjustItem.setTenderId(rs.getString(7));
			cashierTransactionAdjustItem.setCreditTenderId(rs.getString(8));
			cashierTransactionAdjustItem.setTranAmt(rs.getBigDecimal(9));
			cashierTransactionAdjustItem.setUserId(rs.getString(10));
			cashierTransactionAdjustItem.setUserNm(rs.getString(11));
			cashierTransactionAdjustItem.setCauseGroupDesc(rs.getString(12));
			cashierTransactionAdjustItem.setRefInfo(rs.getString(13));

			return cashierTransactionAdjustItem;
		}	
		
	}
	

	
	class CashierTransactionAdjustsMapper implements RowMapper{

		public Object mapRow(ResultSet rs, int arg1) throws SQLException {
			CashierTransactionAdjust ct=new CashierTransactionAdjust();
			
			ct.setTransactionDate(rs.getDate(1));
			ct.setStoreId(rs.getString(2));
			ct.setStsId(rs.getObject(3).toString().charAt(0));
			ct.setDocumentNo(rs.getString(4));
			ct.setCancelDocumentNo(rs.getString(5));
			ct.setJvno(rs.getString(6));
			ct.setCancelJvno(rs.getString(7));
			ct.setInfDttm(rs.getDate(8));
			ct.setCreateDttm(rs.getDate(9));
			ct.setLastUpdDttm(rs.getDate(10));
			ct.setUpdateUserId(rs.getString(11));	
			ct.setObjectId(rs.getLong(12));
			return ct;
		}
		
		
	}
	
	class CashierBalanceAdjustMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CashierBalanceAdjust cashierBalanceAdjust = new CashierBalanceAdjust();
			cashierBalanceAdjust.setTenderId(rs.getString(1));
			cashierBalanceAdjust.setCatClose(rs.getBigDecimal(2));
			cashierBalanceAdjust.setCatNotClose(rs.getBigDecimal(3));
			return cashierBalanceAdjust;
		}
	}
	class CashierBalanceAdjustMapper2 implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CashierTransactionAdjustItem cashItem = new CashierTransactionAdjustItem();

			CashierAdjustType cashierAdjType = new CashierAdjustType();
			cashierAdjType.setCashierAdjTypId(rs.getString(1));
			cashItem.setCashierAdjustType(cashierAdjType);
			cashItem.getCashierAdjustType().setDescription(rs.getString(2));
			// cashItem.setTenderId(rs.getString(3));
			cashItem.setCreditTenderId(rs.getString(4));
			cashItem.setTenderId(rs.getString(5));
			cashItem.setTranAmt(rs.getBigDecimal(6));
			cashItem.setPosId(rs.getString(7));
			cashItem.setTicketNo(rs.getString(8));
			cashItem.setUserId(rs.getString(9));
			cashItem.setRefInfo(rs.getString(10));
			return cashItem;
		}
	}
	class CashierBalanceAdjustMapper3 implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CashierBalanceTransactionAdjust cashierBalanceTransactionAdjust = new CashierBalanceTransactionAdjust();
			cashierBalanceTransactionAdjust.setTenderId(rs.getString(1));
			cashierBalanceTransactionAdjust.setInitial_declaration(rs.getDouble(2));
			cashierBalanceTransactionAdjust.setSales_transaction(rs.getDouble(3));
			cashierBalanceTransactionAdjust.setLoan(rs.getDouble(4));
			cashierBalanceTransactionAdjust.setPickup(rs.getDouble(5));
			cashierBalanceTransactionAdjust.setExcess_transaction(rs.getDouble(6));
			cashierBalanceTransactionAdjust.setReturn_transaction(rs.getDouble(7));
			cashierBalanceTransactionAdjust.setClosing_declaration(rs.getDouble(8));
			cashierBalanceTransactionAdjust.setItemclosing(rs.getDouble(9));
			cashierBalanceTransactionAdjust.setItemover_under_adjust(rs.getDouble(10));
			return cashierBalanceTransactionAdjust;
		}
	}
	
	class CshAdjExOverUnderMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			OverUnderReport newItem = new OverUnderReport();
			newItem.setTrnDt(rs.getDate(1)); 
			newItem.setFullUser(rs.getString(2));
			newItem.setSumOver(rs.getBigDecimal(3));
			newItem.setSumUnder(rs.getBigDecimal(4));
			newItem.setSumNet(rs.getBigDecimal(5));
			return newItem;
		}
	}
	
	class OPMMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			OverUnderReport newItem = new OverUnderReport();
			newItem.setTrnDt(rs.getDate(1)); 
			newItem.setFullUser(rs.getString(2));
			newItem.setSumOver(rs.getBigDecimal(3));
			newItem.setSumUnder(rs.getBigDecimal(4));
			return newItem;
		}
	}
	
	
	class AdjustEmployeeMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			AdjustEmployee newItem = new AdjustEmployee();
			newItem.setTrnDt(rs.getDate(1)); 
			newItem.setFullUser(rs.getString(2));
			newItem.setSumUnder(rs.getBigDecimal(3));
			return newItem;
		}
	}
	
	class ZipzapMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ZipZapReport newItem = new ZipZapReport();
			newItem.setStoreId(rs.getString(1));
			newItem.setTrnDt(rs.getDate(2));
			newItem.setTenderId(rs.getString(3));
			newItem.setCreditCardNo(rs.getString(4));
			newItem.setCshManageType(rs.getString(5));
			newItem.setTrnAmt(rs.getBigDecimal(6));
			newItem.setNumOfMonth(rs.getString(7));
			newItem.setAmtFeeSurcharge(rs.getBigDecimal(8));
			newItem.setNormalFee(rs.getBigDecimal(9));
			newItem.setRefInfo(rs.getString(10));
			return newItem;
		}
	}
	
	class NormalRateMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			NormalRate newItem = new NormalRate();
			newItem.setTenderId(rs.getString(1));
			newItem.setNormalFee(rs.getBigDecimal(2));
			return newItem;
		}
	}
	
	class ViewCreditRptByTypeMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ViewCreditRptByType newItem = new ViewCreditRptByType();
			newItem.setTrnDt(sf.format(rs.getDate(1)).toString());
			newItem.setSum01(rs.getBigDecimal(2));
			newItem.setSum02(rs.getBigDecimal(3));
			newItem.setSum03(rs.getBigDecimal(4));
			newItem.setSum04(rs.getBigDecimal(5));
			newItem.setSum05(rs.getBigDecimal(6));
			newItem.setSum06(rs.getBigDecimal(7));
			newItem.setSumTotal(rs.getBigDecimal(8));
			return newItem;
		}
	}
	
	class ViewCreditRptByDetailMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ViewCreditRptByDetail newItem = new ViewCreditRptByDetail();
			newItem.setTrnDt(sf.format(rs.getDate(1)).toString());
			newItem.setTenderId(rs.getString(2));
			newItem.setTotal(rs.getBigDecimal(3));
			return newItem;
		}
	}  
	
	
	class ReceiveMnyCshChequeReportMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ReceiveMnyCshChequeReport newItem = new ReceiveMnyCshChequeReport();
			newItem.setStoreId(rs.getString(1));
			newItem.setSumCash(rs.getBigDecimal(2));
			newItem.setSumCheque(rs.getBigDecimal(3));
			newItem.setSumOtherCash(rs.getBigDecimal(4));
			newItem.setSumOtherCheque(rs.getBigDecimal(5));
			newItem.setSumTotal(rs.getBigDecimal(6));
			return newItem;
		}  
	}
	
	class CreditCaseONMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CreditCaseON newItem = new CreditCaseON();
			newItem.setStoreId(rs.getString(1));
			newItem.setTenderId(rs.getString(2));
			newItem.setAmt(rs.getBigDecimal(3));
			return newItem;
		}
	}
	
	class DiscountTotalMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			TmpDayEndDiscountTotal discountTotal = new TmpDayEndDiscountTotal();
			if(rs!=null){
				discountTotal.setPosId(rs.getString(1));
				discountTotal.setDiscountTypeCode(rs.getString(2));
				discountTotal.setDiscountAmount(String.valueOf(rs.getBigDecimal(3)));
				discountTotal.setDiscountItemCount(rs.getInt(4));
			}
			return discountTotal;
		}
	}
	
	class RetailTotalMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			TmpDayEndRetailTotal retailTotal = new TmpDayEndRetailTotal();
			if(rs!=null){
				retailTotal.setRetailTypeCode(rs.getString(1));
				retailTotal.setPosId(rs.getString(2));
				retailTotal.setSalesAmount(String.valueOf(rs.getBigDecimal(3)));
				retailTotal.setItemCount(rs.getInt(4));
			}
			return retailTotal;
		}
	}
	
	class TaxTotalMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			TmpDayEndTaxTotal taxTotal = new TmpDayEndTaxTotal();
			if(rs!=null){
				taxTotal.setPosId(rs.getString(1));
				taxTotal.setTaxAmount(String.valueOf(rs.getBigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP )));
				taxTotal.setTaxItemCount(rs.getInt(3));
			}
			return taxTotal;
		}
	}
	
	class TendorTotalMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			TmpDayEndTenderTotal tendorTotal = new TmpDayEndTenderTotal();
			if(rs!=null){
				tendorTotal.setPosId(rs.getString(1));
				tendorTotal.setTenderTypeCode(rs.getString(2));
				tendorTotal.setTenderAmount(String.valueOf(rs.getBigDecimal(3)));
				tendorTotal.setTenderCount(rs.getInt(4));
			}
			return tendorTotal;
		}
	}
	
	class TransactionTotalMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			TmpDayEndTransaction transactionTotal = new TmpDayEndTransaction();
			if(rs!=null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				transactionTotal.setPosId(rs.getString(1));
				transactionTotal.setBeginDate(new SimpleDateFormat("yyyyMMddHHmmss").format(rs.getTimestamp(2)));
				transactionTotal.setEndDate(new SimpleDateFormat("yyyyMMddHHmmss").format(rs.getTimestamp(3)));
			}
			return transactionTotal;
		}
	}
	public void deleteCashierAdjustTypeCauseGroup(String cashierAdjustTypeId,String cashierAdjustCauseGroupId) {
		StringBuffer sql = new StringBuffer();

		sql.append(" delete from csh_adj_typ_cause_grp ");
		sql.append(" where csh_adj_typ_id = '").append(cashierAdjustTypeId).append("'");
		sql.append(" and csh_adj_cause_grp_id = '").append(cashierAdjustCauseGroupId).append("'");

		JdbcTemplate jt = getJdbcTemplate();
		jt.update(sql.toString());
	}
	public List getCashierTransactionAdjustsItem(Long adjoid) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT CSH_TRN_ADJ_ITEM_OID,TENDER_ID,CSH_ADJ_TYP_ID,CREDIT_TENDER_ID," +
	       		"TRN_AMT,POS_ID,TICKET_NO,USER_ID,USER_NM,REF_INFO,BU,CAUSE_GRP_DESC,CREDIT_CARD_NO,CN_NO ");
		buf.append("FROM CSH_TRN_ADJ_ITEM ");
		buf.append("WHERE CSH_TRN_ADJ_OID='@oid'");
		String query = buf.toString();	
		query = query.replaceAll("@oid", adjoid.toString());
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new CashierTranAdjustItems()));
	}
	class CashierTranAdjustItems implements RowMapper{

		public Object mapRow(ResultSet rs, int arg1) throws SQLException {
			
			CashierTransactionAdjustItem cashierTransactionAdjustItem = new CashierTransactionAdjustItem();

			cashierTransactionAdjustItem.setObjectId(rs.getLong(1));
			cashierTransactionAdjustItem.setTenderId(rs.getString(2));	
			cashierTransactionAdjustItem.setCashierAdjustType(getCashierAdjustType(rs.getString(3)));
			cashierTransactionAdjustItem.setCreditTenderId(rs.getString(4));
			cashierTransactionAdjustItem.setTranAmt(rs.getBigDecimal(5));
			cashierTransactionAdjustItem.setPosId(rs.getString(6));
			cashierTransactionAdjustItem.setTicketNo(rs.getString(7));
			cashierTransactionAdjustItem.setUserId(rs.getString(8));
			cashierTransactionAdjustItem.setUserNm(rs.getString(9));
			cashierTransactionAdjustItem.setRefInfo(rs.getString(10));
			cashierTransactionAdjustItem.setBusinessArea(rs.getString(11));
			cashierTransactionAdjustItem.setCauseGroupDesc(rs.getString(12));
			cashierTransactionAdjustItem.setCreditCardNo(rs.getString(13));	
			cashierTransactionAdjustItem.setCnNo(rs.getString(14));			
		    return cashierTransactionAdjustItem;
		}	
		
	}


}