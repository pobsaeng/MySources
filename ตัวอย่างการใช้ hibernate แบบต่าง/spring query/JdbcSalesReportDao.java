package com.ie.icon.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.common.util.PropertyUtil;
import com.ie.icon.constant.Constant;
import com.ie.icon.constant.ErrorConstant;
import com.ie.icon.constant.ErrorConstant.HirePurchase;
import com.ie.icon.dao.SalesAnalysisDao;
import com.ie.icon.domain.Store;
import com.ie.icon.domain.authentication.User;
import com.ie.icon.domain.customer.Customer;
import com.ie.icon.domain.quotation.QuotationManageDetail;
import com.ie.icon.domain.sale.CashierBalance;
import com.ie.icon.domain.sale.CashierBalanceAdjust;
import com.ie.icon.domain.sale.Clearance;
import com.ie.icon.domain.sale.SalesAnalysis;
import com.ie.icon.domain.sale.SalesTransaction;
import com.ie.icon.domain.sale.SalesTransactionItem;
import com.ie.icon.domain.so.SalesOrder;
import com.ie.icon.domain.so.SalesOrderItem;
import com.ie.icon.domain.so.SalesOrderStatus;
import com.ie.icon.domain.so.SalesOrderType;
import com.ie.icon.domain.so.form.HirePurchaseSalesForm;

public class JdbcSalesReportDao extends JdbcDaoSupport implements SalesAnalysisDao {
	private SimpleDateFormat sf;

	public JdbcSalesReportDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public List getSalsAnalysis(String storeId, Date fromDate, Date toDate) {
		
		// EDIT VAT ***
		String sqlString = "SELECT SUBSTR(item.mc9, 0, 2) as Material_Group " + " , mch.mch_nm as Description " + " , sales.typ_id " + " , SUM(item.net_item_amt) AS RevenueAmt " + " , COUNT(CONCAT(sales.ticket_no, sales.pos_id)) AS transNo " + " , SUM(item.qty) as soldItems " + " , SUM(( select NVL(sum(disc.dscnt_amt), 0) from item_dscnt disc, dscnt_cond_typ discType" + "	  where item.SALES_TRN_ITEM_OID = disc.SALES_TRN_ITEM_OID" + "		and disc.DSCNT_COND_TYP_ID = discType.DSCNT_COND_TYP_ID"
				+ "		and discType.dscnt_typ not in ( " + Constant.DiscountConditionType.LOW_PRICE + ", " + Constant.DiscountConditionType.PRICE_DIFF + " )" + "	)) as discountType " + " , SUM(( select NVL(sum(disc.dscnt_amt), 0) from item_dscnt disc, dscnt_cond_typ discType" + "	  where item.SALES_TRN_ITEM_OID = disc.SALES_TRN_ITEM_OID" + "		and disc.DSCNT_COND_TYP_ID = discType.DSCNT_COND_TYP_ID" + "		and discType.dscnt_typ in ( " + Constant.DiscountConditionType.LOW_PRICE + ", "
				+ Constant.DiscountConditionType.PRICE_DIFF + " )" + "	)) as priceOverride ,SUM(decode(item.TAX_CODE_ID,'VX', 0, item.NET_ITEM_AMT)) SaleVAT " + ",SUM(decode(item.TAX_CODE_ID,'VX',item.NET_ITEM_AMT,0)) SaleNonVAT" + " from sales_trn_item item, mch, sales_trn sales " + " where substr(item.mc9, 0, 2) = mch.mch_id " + "   and sales.sales_trn_oid = item.sales_trn_oid " + "   and sales.trn_dt >= TO_DATE('" + sf.format(fromDate) + "', 'DD/MM/YYYY')" + "   and sales.trn_dt <= TO_DATE('"
				+ sf.format(toDate) + "', 'DD/MM/YYYY')" + "   and sales.sts_id in ('P')" + "	and sales.store_id = '" + storeId + "' " + " GROUP BY sales.typ_id, SUBSTR(item.mc9, 0, 2), mch.mch_nm" + " UNION ALL " + " SELECT NVL(SUBSTR(upc.mch_id, 0, 2), 'XX') AS Material_Group" + "	, NVL(mch.mch_nm, 'Not found MainUpc or VenderUpc') AS Description" + "	, cn.SALES_TRN_TYP typ_id " + "	, SUM(-1 * cnItem.net_item_amt) AS RevenueAmt" + "	, COUNT(CONCAT(cn.ticket_no, cn.pos_id)) AS transNo"
				+ "	, SUM(-1 * cnitem.qty) AS soldItems" + " , SUM(( select NVL(sum(-1 * disc.dscnt_amt), 0) from tax_item_dscnt disc, dscnt_cond_typ discType" + "	  where cnitem.TAX_INV_ITEM_OID = disc.TAX_INV_ITEM_OID" + "		and disc.DSCNT_COND_TYP_ID = discType.DSCNT_COND_TYP_ID" + "		and discType.dscnt_typ not in ( " + Constant.DiscountConditionType.LOW_PRICE + ", " + Constant.DiscountConditionType.PRICE_DIFF + " )" + "	)) as discountType "
				+ " , SUM(( select NVL(sum(-1 * disc.dscnt_amt), 0) from tax_item_dscnt disc, dscnt_cond_typ discType" + "	  where cnitem.TAX_INV_ITEM_OID = disc.TAX_INV_ITEM_OID" + "		and disc.DSCNT_COND_TYP_ID = discType.DSCNT_COND_TYP_ID" + "		and discType.dscnt_typ in ( " + Constant.DiscountConditionType.LOW_PRICE + ", " + Constant.DiscountConditionType.PRICE_DIFF + " )" + "	)) as priceOverride ,SUM(decode(cnitem.TAX_CODE,'VX', 0,-1 *cnitem.NET_ITEM_AMT)) SaleVAT "
				+ " ,SUM(decode(cnitem.TAX_CODE,'VX',-1 *cnitem.NET_ITEM_AMT,0)) SaleNonVAT" + " FROM tax_inv_item cnitem LEFT JOIN vw_item_upc upc" + " ON cnItem.item_upc = upc.item_upc " + " LEFT JOIN mch " + " ON SUBSTR(upc.mch_id, 0, 2) = mch.mch_id " + " , tax_inv cn " + " WHERE cn.tax_inv_oid = cnItem.tax_inv_oid" + "	AND cn.tax_inv_typ_id in ('C')" + "	AND cn.issue_dt >= TO_DATE('" + sf.format(fromDate) + "', 'DD/MM/YYYY')" + "	AND cn.issue_dt <= TO_DATE('" + sf.format(toDate)
				+ "', 'DD/MM/YYYY')" + "	AND cn.store_id = '" + storeId + "' " + " GROUP BY cn.SALES_TRN_TYP, SUBSTR(upc.mch_id, 0, 2), mch.mch_nm" + " ORDER BY Material_Group";

		JdbcTemplate jt = getJdbcTemplate();
		System.out.println("sqlString :"+sqlString);
		return jt.query(sqlString, new RowMapperResultReader(new SalesAnalysisRowMapper()));
	}

	public List getNotBilling(String storeId, Date fromDate, Date toDate){
		String sqlString = "select * from sales_trn a where a.store_id ='S004' and a.trn_dt between to_date ('01/01/2010','DD/MM/YYYY') and to_date('10/01/2010','DD/MM/YYYY') and a.typ_id = 'S' and a.sts_id = 'P' and a.sales_doc_no is null";
		
		System.out.println("sql getNotBilling :"+sqlString);
		
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(sqlString, new RowMapperResultReader(new SalesAnalysisRowMapper()));
	}
	
	
	// this inner class is invoked for each Row om the ResultSet. It implements
	// the Spring RowMapper interface, that is used to convert ResultSet records
	// to Domain Objects. RowMappers are used when the results are not simple,
	// like in the getSalarySum example above
	class SalesAnalysisRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			SalesAnalysis sales = new SalesAnalysis();
			sales.setGroupId(rs.getString(1));
			sales.setGroupName(rs.getString(2));
			sales.setSalesTypeId(rs.getString(3));
			sales.setRevenueAmount(rs.getDouble(4));
			sales.setPercenTotal(0);
			sales.setTransNo(rs.getString(5));
			sales.setSoldItemsQuantity(rs.getDouble(6));

			if (rs.getObject(7) != null) // discount type
				sales.setDiscountAmount(rs.getDouble(7));
			else
				sales.setDiscountAmount(0);

			if (rs.getObject(8) != null) // price override type
				sales.setPriceOverride(rs.getDouble(8));
			else
				sales.setPriceOverride(0);

			if (rs.getObject(9) != null)  
				sales.setSaleVAT(rs.getDouble(9));
			else
				sales.setSaleVAT(0);

			if (rs.getObject(10) != null)
				sales.setSaleNonVAT(rs.getDouble(10));
			else
				sales.setSaleNonVAT(0);

			return sales;
		}
	}

	public List getStoreBalance(String storeId, Date trnDate) {
		String queryString = "select cash.store_id, cash.store_id,  tender.tender_no, tender.tender_nm " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id = 'INIT' and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as INIT " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id IN ('POS')  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as POS "
				+ ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id IN ('SO')  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as SO " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id = 'CNP'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as CNP " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id = 'CNS'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as CNS "
				+ ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id = 'LOAN'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as LOAN " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id = 'PICK'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as PICK " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id = 'EXCESS'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as EXCESS "
				+ ", ( select nvl(sum(trn_amt), 0) from csh_trn where csh_trn_typ_id = 'CLOSE'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as CLOSE " + " from csh_trn cash, tender " + " where cash.trn_dt = to_date('" + sf.format(trnDate) + "', 'DD/MM/YYYY') " + " 	and cash.store_id = '" + storeId + "'" + " 	and cash.tender_id = tender.tender_id " + " group by cash.store_id, cash.trn_dt, cash.tender_id, tender.tender_no, tender.tender_nm ";
		System.out.println("sql getStoreBalance :"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new CashierBalanceMapper()));   
	}
	public List getStoreBalanceAdjust(String storeId, Date trnDate) {
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT CASH.STORE_ID, CASH.TRN_DT, CASH.TENDER_ID, TENDER.TENDER_NO, TENDER.TENDER_NM,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'INIT' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS INIT,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'POS' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS POS,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'SO' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS SO,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'CNP' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS CNP,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'CNS' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS CNS,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'LOAN' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS LOAN,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'PICK' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS PICK,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'EXCESS' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS EXCESS,");
		buf.append(" (SELECT NVL (SUM (TRN_AMT), 0) FROM CSH_TRN WHERE CSH_TRN_TYP_ID = 'CLOSE' AND TRN_DT = CASH.TRN_DT AND TENDER_ID = CASH.TENDER_ID) AS CLOSE");
		buf.append(" FROM CSH_TRN CASH, TENDER");
		buf.append(" WHERE CASH.TRN_DT = TO_DATE ('@1', 'DD/MM/YYYY') AND CASH.STORE_ID = '@2' AND CASH.TENDER_ID = TENDER.TENDER_ID");
		buf.append(" GROUP BY CASH.STORE_ID, CASH.TRN_DT, CASH.TENDER_ID, TENDER.TENDER_NO, TENDER.TENDER_NM");
		buf.append(" ORDER BY TENDER.TENDER_NO");
		
		String queryString = buf.toString();
		queryString = queryString.replaceAll("@1", sf.format(trnDate));
		queryString = queryString.replaceAll("@2", storeId);   
		
		System.out.println("sql getStoreBalanceAdjust :"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new CashierBalanceAdjustMapper()));
	}
	public List getCashierBalance(String userId, Date trnDate) {
		String queryString = "select cash.user_id, usr.user_nm,  tender.tender_no, tender.tender_nm " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'INIT' and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as INIT " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'POS'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as POS "
				+ ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'SO'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as SO " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'CNP'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as CNP "
				+ ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'CNS'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as CNS " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'LOAN'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as LOAN "
				+ ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'PICK'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as PICK " + ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'EXCESS'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as EXCESS "
				+ ", ( select nvl(sum(trn_amt), 0) from csh_trn where user_id = cash.user_id and csh_trn_typ_id = 'CLOSE'  and trn_dt = cash.trn_dt and tender_id = cash.tender_id) as CLOSE " + " from csh_trn cash, tender, usr " + " where cash.trn_dt = to_date('" + sf.format(trnDate) + "', 'DD/MM/YYYY') " + " 	and cash.user_id like '" + userId + "'" + " 	and cash.tender_id = tender.tender_id " + "	and cash.user_id = usr.user_id "
				+ " group by cash.user_id, usr.user_nm, cash.trn_dt, cash.tender_id, tender.tender_no, tender.tender_nm ";
		
		System.out.println("sql getCashierBalance :"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new CashierBalanceMapper()));
	}
	class CashierBalanceMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CashierBalance cashBln = new CashierBalance();
			cashBln.setUserId(rs.getString(1));
			cashBln.setUserName(rs.getString(2));
			cashBln.setTenderNo(rs.getString(3));
			cashBln.setTenderName(rs.getString(4));
			cashBln.setInit(rs.getDouble(5));
			cashBln.setPos(rs.getDouble(6));
			
			cashBln.setSo(rs.getDouble(7));
			cashBln.setCnp(rs.getDouble(8));
			cashBln.setCns(rs.getDouble(9));
			cashBln.setLoan(rs.getDouble(10));
			cashBln.setPick(rs.getDouble(11));
			cashBln.setExcess(rs.getDouble(12));
			cashBln.setClose(rs.getDouble(13));
			return cashBln;
		}
	}
	class CashierBalanceAdjustMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CashierBalanceAdjust cashierBalanceAdjust = new CashierBalanceAdjust();
			cashierBalanceAdjust.setStoreId(rs.getString(1));
			cashierBalanceAdjust.setTransactionDate(rs.getDate(2));
			cashierBalanceAdjust.setTenderId(rs.getString(3));
			cashierBalanceAdjust.setTenderNo(rs.getString(4));
			cashierBalanceAdjust.setTenderName(rs.getString(5));
			cashierBalanceAdjust.setInit(rs.getBigDecimal(6));
			cashierBalanceAdjust.setPos(rs.getBigDecimal(7));
			cashierBalanceAdjust.setSo(rs.getBigDecimal(8));
			cashierBalanceAdjust.setCnp(rs.getBigDecimal(9));
			cashierBalanceAdjust.setCns(rs.getBigDecimal(10));
			cashierBalanceAdjust.setLoan(rs.getBigDecimal(11));
			cashierBalanceAdjust.setPick(rs.getBigDecimal(12));
			cashierBalanceAdjust.setExcess(rs.getBigDecimal(13));
			cashierBalanceAdjust.setClose(rs.getBigDecimal(14));
			return cashierBalanceAdjust;
		}
	}
	public List getClearance(String storeId, Date fromDate, Date toDate) {
		String sqlString = "select distinct sales_trn.store_id,sales_trn.trn_dt,to_char(to_number(sales_trn_item.artc_no)) as atrc_no, " 
			+ "sales_trn_item.sales_trn_item_dsc,sales_trn_item.real_unit_price," + "sales_trn_item.net_item_amt,(sales_trn_item.real_unit_price-sales_trn_item.net_item_amt) diff, " 
		+ "sales_trn_item.unit,sales_trn_item.qty,sales_trn.ticket_no,sales_trn.pos_id,sales_trn.create_user_id,sales_trn.create_user_nm " 
		+",sales_trn_item.seq_no "
		+ "from sales_trn,sales_trn_item "
				+ "where sales_trn.sales_trn_oid = sales_trn_item.sales_trn_oid " + "and sales_trn.store_id = '" + storeId + "' " + "and sales_trn.trn_dt >= TO_DATE('" + sf.format(fromDate) + "', 'DD/MM/YYYY') " + "and sales_trn.trn_dt <= TO_DATE('" + sf.format(toDate) + "', 'DD/MM/YYYY') " + "and sales_trn_item.is_clearance_artc ='Y' " + "order by sales_trn.store_id,sales_trn.trn_dt,atrc_no";

		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(sqlString, new RowMapperResultReader(new ClearanceRowMapper()));
	}
	class ClearanceRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			Clearance clear = new Clearance();
			clear.setStoreId(rs.getString(1));
			clear.setTrnDt(rs.getDate(2));
			clear.setArtcNo(rs.getString(3));
			clear.setSaleTranItemDsc(rs.getString(4));
			clear.setRealUnitPrice(rs.getDouble(5));
			clear.setNetItemAmt(rs.getDouble(6));
			clear.setDiff(rs.getDouble(7));
			clear.setUnit(rs.getString(8));
			clear.setQty(rs.getDouble(9));
			clear.setTicketNo(rs.getString(10));
			clear.setPosId(rs.getString(11));
			clear.setCreateUserId(rs.getString(12));
			clear.setCreateUserNm(rs.getString(13));
			return clear;
		}  
	}
	
	class QuotationMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			QuotationManageDetail result = new QuotationManageDetail();
			result.setQuotationId(rs.getString(1));
			result.setVersionId(rs.getString(2)); 
			result.setStatus(rs.getString(3));
			result.setTicketNo(rs.getString(4)); 
			result.setTrnDt(rs.getDate(5));
			result.setCollectSalesOrderNo(rs.getString(6)); 
			result.setSalesOrderNo(rs.getString(7));
			result.setNetTrnAmount(rs.getBigDecimal(8));
			result.setQuotationTypeId(rs.getString(9));
			return result;   
		}  
	}
	
	public List getQuotationManageDetail(Date fromDate, Date toDate, String quotationFrom, String quotationTo, String quotationType){	
		
		String query = "select a.qt_no, a.version_id, a.qt_sts_id, d.ticket_no, a.trn_dt , b.coll_sales_order_no, d.sales_order_no, sum(e.net_item_amt) so_amt,a.qt_typ_id "
			+ "from ((( quotation a left outer join coll_sales_order b on a.qt_no = b.qt_no )"
			//" and a.version_id = b.version_id) ";
		    + "left outer join coll_sales_order_item c on b.coll_sales_order_oid = c.coll_sales_order_oid ) "
			+ "left outer join sales_order d on c.sales_order_no = d.sales_order_no and c.store_id = d.store_id and c.so_trn_dt = d.trn_dt) "
			+ "left outer join sales_order_item e on d.sales_order_oid = e.sales_order_oid " 
			+ "where a.trn_dt between " + "TO_DATE('" + sf.format(fromDate) + "', 'DD/MM/YYYY')" + "and " + "TO_DATE('" + sf.format(toDate) + "', 'DD/MM/YYYY') ";
		
		if(quotationFrom!=null && !quotationFrom.equals("") && quotationTo!=null && !quotationTo.equals("")){
			query = query + "and a.qt_no between ('"+quotationFrom+"') and ('"+quotationTo+"') ";
		}
		else if(quotationFrom!=null && !quotationFrom.equals("")){
			query = query + "and a.qt_no = ('"+quotationFrom+"') ";
		}
		if(quotationType!=null&& !quotationType.equals("")){
			query = query + "and a.qt_typ_id = ('"+quotationType+"') ";	
		}
		query = query + " and a.qt_sts_id <> 'CANCELLED' group by a.qt_no, a.version_id, a.qt_sts_id, d.ticket_no, a.trn_dt , b.coll_sales_order_no, d.sales_order_no,a.qt_typ_id "
			+ "order by a.trn_dt , a.qt_no, a.version_id ";
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(new QuotationMapper()));
	}

	public List getHirePurchaseSales(String storeId, String tenderOrPriceGroup, Date fromDate, Date toDate) {
		
		String queryString = " SELECT SALES.STORE_ID,ITEM.MC9 ,ITEM.ARTC_NO,ITEM.SALES_ORDER_ITEM_DSC,ITEM.QTY,ITEM.NET_ITEM_AMT "
			+ 	 " FROM SALES_ORDER_ITEM ITEM , SALES_ORDER SALES WHERE ITEM.SALES_ORDER_OID = SALES.SALES_ORDER_OID ";
		if (!"".equals(storeId) && storeId != null ){
			queryString += 	 " AND SALES.STORE_ID = '" + storeId + "'" ;
		}					
		queryString += " AND SALES.TRN_DT BETWEEN TO_DATE('" + sf.format(fromDate) + "','DD/MM/YYYY') "
			+    " AND TO_DATE('" + sf.format(toDate) + "','DD/MM/YYYY') AND SALES.SALES_ORDER_NO IN "
			+ 	 " ( " 
			+    " SELECT COLL_ITEM.SALES_ORDER_NO FROM COLL_SALES_ORDER COLL, COLL_SALES_ORDER_ITEM COLL_ITEM "
			+    " WHERE COLL.COLL_SALES_ORDER_OID = COLL_ITEM.COLL_SALES_ORDER_OID ";
		if (!"".equals(storeId) && storeId != null){	
			queryString +=    " AND COLL_ITEM.STORE_ID = '" + storeId + "'" ;
		}	
		queryString += " AND COLL_ITEM.SO_TRN_DT BETWEEN TO_DATE('" + sf.format(fromDate) + "','DD/MM/YYYY') "
			+    " AND TO_DATE('" + sf.format(toDate) + "','DD/MM/YYYY')  "
			+    " ) "
			+    "AND SALES.IS_HIREPURCHASE_PAYMENT = 'Y'";
		System.out.println("queryString:"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new HirePurchaseSalesMapper()));   
	}
	class HirePurchaseSalesMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			HirePurchaseSalesForm form = null;
			SalesOrderItem salesOrderItem = new SalesOrderItem();
			SalesOrder salesOrder = new SalesOrder();
			Store store = new Store();
			
			store.setStoreId(rs.getString(1));
			salesOrder.setStore(store);
			salesOrderItem.setSalesOrder(salesOrder);
			salesOrderItem.setMc9(rs.getString(2));
			salesOrderItem.setArticleNo(rs.getString(3));
			salesOrderItem.setDescription(rs.getString(4));
			salesOrderItem.setQuantity(rs.getBigDecimal(5));
			salesOrderItem.setNetItemAmount(rs.getBigDecimal(6));
			
			form = new HirePurchaseSalesForm(salesOrderItem);
			
			return form;
		}
	}
	
	public List getBacth_Dummy(Map map) {
		   
		   List result = new ArrayList();
		if(!map.get("CNType").equals("CN")){// inquary Credit Note
			if(!map.get("saleType").equals("P")){
				String  sql = " select distinct sales_order_no ,trn_dt ,net_trn_amt "
				+"  ,artc_no ,sales_trn_item_dsc ,net_item_amt ,qty ,seq_no ,pos_id ,ticket_no "
				+"  ,sales_order_sts_id  ,sales_order_typ_nm , channel , unit"
				+" from( "
				+" select sti.sales_order_no ,st.trn_dt ,st.net_trn_amt "
				+" ,sti.artc_no ,sti.sales_trn_item_dsc ,sti.net_item_amt ,sti.qty ,sti.seq_no ,st.pos_id ,st.ticket_no "
				+" ,s.sales_order_sts_id  ,sty.sales_order_typ_nm , 'SO' as channel, sti.unit "
				+" from sales_order_trn sot , sales_trn st , sales_trn_item sti ,sales_order s " 
				// +" ,sales_order_item soi " +
				+" ,sales_order_typ sty , artc art "
				+" where  st.trn_dt >= to_date('"+sf.format(map.get("trn_dt"))+"','DD-MM-YYYY')   "
				+" and st.trn_dt <= to_date('"+sf.format(map.get("trn_dt_to"))+"','DD-MM-YYYY') "
				+" and st.store_id = '"+map.get("store_id")+"' "
				+" and st.typ_id = 'S' "
				+" and st.sales_trn_oid = sti.sales_trn_oid "
				+" and st.sales_trn_oid = sot.sales_trn_oid "
				// +" and sot.sales_order_no = sti.sales_order_no "
				+" and s.sales_order_no = sot.sales_order_no "
				+" and s.trn_dt = st.trn_dt "
				+" and s.sales_order_sts_id in ('"+Constant.SaleOrderMessage.PAIDSTS+"','"+Constant.SaleOrderMessage.SALEDOCSTS+"','"+Constant.SaleOrderMessage.CONFIRM+"') "
				+" and s.trn_dt >= to_date('"+sf.format(map.get("trn_dt"))+"','DD-MM-YYYY') "
				+" and s.trn_dt <= to_date('"+sf.format(map.get("trn_dt_to"))+"','DD-MM-YYYY') "
				// +" and s.sales_order_oid = soi.sales_order_oid "
				// +" and soi.artc_no = sti.artc_no "
				+" and sti.batch = '001' "
				+" and s.sales_order_typ_id = sty.sales_order_typ_id(+) "
				+" and sti.artc_no = art.artc_id "
				+" and art.valuation_cat is null "
				+" ) a "
				+" order by  trn_dt, ticket_no , sales_order_no ,seq_no ";
				
				JdbcTemplate jt = getJdbcTemplate();
				result.addAll(jt.query(sql, new RowMapperResultReader(new Bacth_Dummy_Mapper()))); 
			}
			
			
			if(!map.get("saleType").equals("S")){
				String  sql = " select distinct sales_order_no ,trn_dt ,net_trn_amt "
					+"  ,artc_no ,sales_trn_item_dsc ,net_item_amt ,qty ,seq_no ,pos_id ,ticket_no "
					+"  ,vsales_order_sts_id  ,sales_order_typ_nm , channel, unit "
					+" from( "
					+" select '' as sales_order_no ,st.trn_dt ,st.net_trn_amt "
					+" ,sti.artc_no ,sti.sales_trn_item_dsc ,sti.net_item_amt ,sti.qty ,sti.seq_no ,st.pos_id ,st.ticket_no "
					+" , 'PAID' as vsales_order_sts_id  ,sty.sales_order_typ_nm , 'POS' as channel, sti.unit"
					+" from sales_trn st , sales_trn_item sti , sales_order_typ sty  ,artc art "
					+" where  st.trn_dt >= to_date('"+sf.format(map.get("trn_dt"))+"','DD-MM-YYYY')  "
					+" and st.trn_dt <= to_date('"+sf.format(map.get("trn_dt_to"))+"','DD-MM-YYYY') "
					+" and st.store_id = '"+map.get("store_id")+"' "
					+" and st.typ_id = 'P' "
					+" and st.sales_trn_oid = sti.sales_trn_oid "
					+" and sty.sales_order_typ_id = 'CASH' "
					+" and sti.batch = '001' "
					+" and sti.artc_no = art.artc_id "
					+" and art.valuation_cat is null "
					+" ) a "
					+" order by  trn_dt, ticket_no , sales_order_no ,seq_no ";
					
					JdbcTemplate jt = getJdbcTemplate();
					result.addAll(jt.query(sql, new RowMapperResultReader(new Bacth_Dummy_Mapper()))); 
			}
		}
		
		
		if(!map.get("CNType").equals("Sale")){// inquary Sale Tran
			if(!map.get("saleType").equals("P")){
				String sql = "select distinct t.issue_dt as trn_dt , 'SO' as channel , t.pos_id, t.ticket_no, t.refund_inv_id, t.tax_inv_id, sti.artc_no, sti.sales_trn_item_dsc, ti.qty, ti.unit " +
						" from (tax_inv_item ti join tax_inv t on t.tax_inv_oid = ti.tax_inv_oid ) " +
						" join ( sales_trn_item sti join sales_trn st on st.sales_trn_oid = sti.sales_trn_oid ) " +
						" on t.ticket_no = st.ticket_no  join artc art on sti.artc_no = art.artc_id " +
						" and t.store_id = st.store_id and t.trn_dt = st.trn_dt and ti.ref_seq_no = sti.seq_no " +
						" where t.issue_dt >= to_date('"+sf.format(map.get("trn_dt"))+"','DD-MM-YYYY') "+
						" and t.issue_dt <= to_date('"+sf.format(map.get("trn_dt_to"))+"','DD-MM-YYYY') "+
						" and tax_inv_typ_id = 'C' " +
						" and ti.batch = '001' " +
						" and art.valuation_cat is null "+
						" and sales_trn_typ = 'S'";
				JdbcTemplate jt = getJdbcTemplate();
				result.addAll(jt.query(sql, new RowMapperResultReader(new Bacth_Dummy_Mapper_CN()))); 
			}
			
			if(!map.get("saleType").equals("S")){
				String sql = "select distinct t.issue_dt as trn_dt ,'POS' as channel , t.pos_id, t.ticket_no, t.refund_inv_id, t.tax_inv_id, sti.artc_no, sti.sales_trn_item_dsc, ti.qty, ti.unit " +
						" from (tax_inv_item ti join tax_inv t on t.tax_inv_oid = ti.tax_inv_oid ) " +
						" join ( sales_trn_item sti join sales_trn st on st.sales_trn_oid = sti.sales_trn_oid ) " +
						" on t.ticket_no = st.ticket_no join artc art on sti.artc_no = art.artc_id  " +
						" and t.store_id = st.store_id and t.trn_dt = st.trn_dt and ti.ref_seq_no = sti.seq_no " +
						" where t.issue_dt >= to_date('"+sf.format(map.get("trn_dt"))+"','DD-MM-YYYY') "+
						" and t.issue_dt <= to_date('"+sf.format(map.get("trn_dt_to"))+"','DD-MM-YYYY') "+
						" and tax_inv_typ_id = 'C' " +
						" and ti.batch = '001' " +
						" and art.valuation_cat is null "+
						" and sales_trn_typ = 'P'";
				JdbcTemplate jt = getJdbcTemplate();
				result.addAll(jt.query(sql, new RowMapperResultReader(new Bacth_Dummy_Mapper_CN()))); 
			}
		}
		
		return result;
	}
	class Bacth_Dummy_Mapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
		    int columnIndex = 1;
			SalesOrderItem it = new SalesOrderItem();
			SalesOrder s = new SalesOrder();
			SalesOrderStatus sts =   new  SalesOrderStatus();
			SalesOrderType st = new SalesOrderType();		
		
			s.setSalesOrderNo(rs.getString(columnIndex++));
			s.setTransactionDate(rs.getDate(columnIndex++));			
			s.setTotalTransactionAmount(rs.getBigDecimal(columnIndex++));
			
			it.setArticleNo(rs.getBigDecimal(columnIndex++)+"");
			it.setDescription(rs.getString(columnIndex++));
			s.setNetTransactionAmount(rs.getBigDecimal(columnIndex++));
			it.setQuantity(rs.getBigDecimal(columnIndex++));
			it.setSeqNo(rs.getInt(columnIndex++));
             
			s.setTicketNo(rs.getString(columnIndex++)+"/"+rs.getString(columnIndex++)); 
			sts.setName(rs.getString(columnIndex++));			
	 	    s.setSalesOrderStatus(sts);
	 	    st.setName(rs.getString(columnIndex++));
	 	    s.setSalesOrderType(st);
	 	    s.setSalesChannel(rs.getString(columnIndex++));
	 	    s.setSalesType("Sale");
	 	    it.setUnit(rs.getString(columnIndex++));
	 	    it.setSalesOrder(s);
			return it;
		}
	}
	
	class Bacth_Dummy_Mapper_CN implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
		    int columnIndex = 1;
			SalesOrderItem it = new SalesOrderItem();
			SalesOrder s = new SalesOrder();
			SalesOrderStatus sts =   new  SalesOrderStatus();
			SalesOrderType st = new SalesOrderType();		
		
			s.setTransactionDate(rs.getDate(columnIndex++));	
			s.setSalesChannel(rs.getString(columnIndex++));
			s.setTicketNo(rs.getString(columnIndex++)+"/"+rs.getString(columnIndex++));
			s.setSapBillingNo(rs.getString(columnIndex++));
			s.setSapSONo(rs.getString(columnIndex++));
			it.setArticleNo(rs.getBigDecimal(columnIndex++)+"");
			it.setDescription(rs.getString(columnIndex++));
			it.setQuantity(rs.getBigDecimal(columnIndex++));
			s.setSalesOrderNo(null);
			s.setSalesType("CN");
	 	    it.setUnit(rs.getString(columnIndex++));
	 	    it.setSalesOrder(s);
			return it;
		}
	}
	
	public boolean savePrintSalesOrderForm(Map map){
		String storeId = (String) map.get("storeId");
		Date trnDt = (Date)map.get("trnDt");
		String fromNo = (String)map.get("fromNo");
		String toNo =(String) map.get("toNo");
		int amt = Integer.parseInt(fromNo)-Integer.parseInt(toNo);
		User user = (User)map.get("user");
		String createUserId = user.getUserId();
		String createUserName = user.getName();
		
		JdbcTemplate jt = getJdbcTemplate();
		
		String sql = "SELECT SALES_ORDER_PRINT_SEQ.NEXTVAL FROM DUAL";
		Integer oid = (Integer)jt.queryForObject(sql, Integer.class);
		
		PreparedStatement pre = null;
		String sqlString = "INSERT INTO SALES_ORDER_PRINT(SALES_ORDER_PRINT_OID,STORE_ID,TRN_DT,FROMNO,TONO,AMT,CREATE_DT,CREATE_USER_ID,CREATE_USER_NM) "
			+ " VALUES(?,?,?,?,?,?,SYSDATE,?,?)";
  
		
		Object[] args = new Object[] {oid,storeId,trnDt,fromNo,toNo,String.valueOf(amt),createUserId,createUserName};
		return jt.update(sqlString, args)>0;
	}
	
	public List getsalesTransaction_By_Customer(Map map){
		List result = new ArrayList();
		String condition = new String();
		String storeId = (String) map.get("storeId");
		Date trnDtFrom = (Date)map.get("trnDtFrom");
		String trnDtTo = (String)map.get("trnDtTo");
		String searchData = (String)map.get("searchData");
		String lastName = (String)map.get("lastName");
		String searchType = (String)map.get("searchType");
		
		char type = searchType.charAt(0);
		switch(type){
		case 'C': {
			condition = " where sap_id = '"+searchData.trim()+"'";
			}break;
		case 'P':{
			condition = " where (phone_no_1 = '"+searchData.trim()+"' or phone_no_2 = '"+searchData.trim()+"' or phone_no_3 = '"+searchData.trim()+"' "+
						" or phone_no_4 = '"+searchData.trim()+"' or fax = '"+searchData.trim()+"')";
			}break;
		case 'N':{
			condition = " where first_nm like '"+searchData+"%' and last_nm like '"+lastName+"%'";
			}
		}
		
		String  sql = " select distinct s_trn.store_id , s_trn.trn_dt , cust.sap_id , s_trn.pos_id , s_trn.ticket_no "+ 
				        " , s_item.sales_order_no , s_item.artc_no , s_item.sales_trn_item_dsc , s_item.qty "+
				        " , s_item.unit_price , s_item.net_item_amt "+
						" from sales_trn s_trn , sales_trn_item s_item , customer cust "+
						" where s_trn.sales_trn_oid = s_item.sales_trn_oid "+
						" and s_trn.cust_oid = cust.cust_oid "+
						" and s_trn.trn_dt between to_date('"+sf.format(trnDtFrom)+"','DD/MM/YYYY') "+ 
						" and to_date('"+sf.format(trnDtTo)+"','DD/MM/YYYY') "+
						" and s_trn.store_id = '"+storeId+"'"+
						" and s_trn.cust_oid in (select cust_oid from customer "+condition + 
						" )"+
						" order by  s_trn.trn_dt ,cust.sap_id , s_trn.pos_id , s_trn.ticket_no ";
		JdbcTemplate jt = getJdbcTemplate();
		result.addAll(jt.query(sql, new RowMapperResultReader(new salestransaction_Data_By_Customer()))); 
		
		return result;
	}
	
	class salestransaction_Data_By_Customer implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException{
			int columnIndex = 1;
			SalesTransaction salesTrn = new SalesTransaction();
			SalesTransactionItem salesItem = new SalesTransactionItem();
			Customer cust = new Customer();
			
			salesTrn.getStore().setStoreId(rs.getString(columnIndex++));
			salesTrn.setTransactionDate(rs.getDate(columnIndex++));	
			salesTrn.setPosId(rs.getString(columnIndex++));
			salesTrn.setTicketNo(rs.getString(columnIndex++));
			salesItem.setPreSaleNo(rs.getString(columnIndex++));
			salesItem.setArticleNo(rs.getString(columnIndex++));
			salesItem.setDescription(rs.getString(columnIndex++));
			salesItem.setQuantity(rs.getBigDecimal(columnIndex++));
			salesItem.setUnitPrice(rs.getBigDecimal(columnIndex++));
			salesItem.setNetItemAmount(rs.getBigDecimal(columnIndex++));
			cust.setSapId(rs.getString(columnIndex++));
			salesTrn.setCustomer(cust);
			salesItem.setSalesTransaction(salesTrn);
			return salesItem;
		}
	}
	
}