package com.ie.icon.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.SalesTransactionJdbcDao;
import com.ie.icon.domain.report.Member_sale;
import com.ie.icon.domain.report.TaxInvReport;
import com.ie.icon.domain.report.TrnReport;

public class JdbcSalesTransactionDao extends JdbcDaoSupport implements SalesTransactionJdbcDao{
	private SimpleDateFormat sf;
	public JdbcSalesTransactionDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public boolean updateSalesTransaction(Long salesTransactionOid,String sapBillingNo,Timestamp RecordDate){
		
		String sqlString = "UPDATE SALES_TRN SET SAP_BILL_NO = ?,RECORD_DTTM = ?  WHERE SALES_TRN_OID = ?";
		JdbcTemplate jt = getJdbcTemplate();
		Object[] args = new Object[] {sapBillingNo,RecordDate,salesTransactionOid};
		return jt.update(sqlString,args) > 0;

	}
	
	public boolean updateSalesTransactionItem(Long salesTransactionItemOid,String sapBillingItemNo){
		
		String sqlString = "UPDATE SALES_TRN_ITEM SET SAP_BILL_ITEM_NO = ?  WHERE SALES_TRN_ITEM_OID = ?";
		JdbcTemplate jt = getJdbcTemplate();
		Object[] args = new Object[] {sapBillingItemNo,salesTransactionItemOid};
		return jt.update(sqlString,args) > 0;

	}

	public List getMemberSale(String fromStoreId, String toStoreId, Date fromDate, Date toDate,
			String cardNo) {
		String sql ="select s.trn_dt, " +
		"		s.store_id ||' - '|| st.store_nm, " +
		"       s.pos_id, " + 
		"       s.ticket_no, " + 
		"       s.net_trn_amt, " + 
		"       s.rwd_card_no, " + 
		"       c.first_nm || '  ' || c.last_nm, " + 
		"       u.user_id || '-' || u.user_nm ," +
		"		c.CREATE_DTTM "+
		" from sales_trn s, customer c, store st, usr u " + 
		" where is_manual_rwd_card = 'Y' ";
		if (fromStoreId != null && !fromStoreId.equals(SystemConfigConstant.CENTER_CODE) && (toStoreId == null||toStoreId.equals(SystemConfigConstant.CENTER_CODE)) ) {
			sql += " and s.store_id = '" + fromStoreId + "'";
		} else if(!fromStoreId.equals(SystemConfigConstant.CENTER_CODE) && !toStoreId.equals(SystemConfigConstant.CENTER_CODE)){
				sql += " and s.store_id between '" + fromStoreId + "'" + " and '" + toStoreId + "'";
		}
		if (cardNo != null && cardNo.length() == 10) {
			sql += " and rwd_card_no = " + cardNo;
		}		
		sql += " and s.trn_dt between to_date('"+sf.format(fromDate)+"','DD/MM/YYYY') " +
		" and to_date('"+sf.format(toDate)+"','DD/MM/YYYY')"+
		//Pook comment 06/01/2014 
		//" and s.cust_oid = c.cust_oid " +
		" and s.rwd_cust_oid = c.cust_oid " +
		" and st.store_id = s.store_id " +
		" and u.user_id = s.last_upd_user " +
		" order by s.trn_dt, s.rwd_card_no ";
		
		JdbcTemplate jt = getJdbcTemplate();
		
		return jt.query(sql,  new RowMapperResultReader(new MemberSaleMapper()));
	}

	public List getSalesTransactionByStoreId(String storeId, Date date) {
		String sql ="select s.pos_id, " + 
		"       s.ticket_no, " +
		"		s.register_no," + 
		"       s.net_trn_amt, " + 
		"       s.vat_trn_amt, " +
		"		s.tot_vat_trn_amt, " + 
		"		s.store_id "+
		"  		from sales_trn s " + 
		" 		where s.store_id = '"+storeId+"' " +
				"and s.sts_id = 'P' " +
		"		and s.trn_dt = to_date('"+sf.format(date)+"', 'DD/MM/YYYY')";
		
		JdbcTemplate jt = getJdbcTemplate();
		
		return jt.query(sql,  new RowMapperResultReader(new SalesTransactionMapper()));
	}

	public List getSalesTransactionByStoreIdAndMonth(String storeId, Date month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(month);
		cal.set(Calendar.DATE, 1);
		month = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		
		String sql ="select s.pos_id, " + 
		"       s.ticket_no, " +
		"		s.register_no," + 
		"       s.net_trn_amt, " + 
		"       s.vat_trn_amt, " +
		"		s.tot_vat_trn_amt, " + 
		"		s.store_id "+
		"  		from sales_trn s " + 
		" 		where s.store_id = '"+storeId+"' " +
				"and s.sts_id = 'P' " +
		"		and s.trn_dt >= to_date('"+sf.format(month)+"', 'DD/MM/YYYY') "+
		"		and s.trn_dt < to_date('"+sf.format(cal.getTime())+"', 'DD/MM/YYYY')";
		
		JdbcTemplate jt = getJdbcTemplate();
		
		return jt.query(sql,  new RowMapperResultReader(new SalesTransactionMapper()));
	}

	public List getTaxInvoiceByStoreIdAndMonth(String storeId, Date month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(month);
		cal.set(Calendar.DATE, 1);
		month = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		
		String sql ="select " +
		"		s.store_id, "+
		"		s.register_no," + 
		"		s.pos_id, " + 
		"       s.ticket_no, " +
		"		s.sales_doc_no," + 
		"		s.tax_inv_typ_id," + 
		"		s.tax_inv_id," + 
		"		s.cust_nm," + 
		"		s.issue_dt," + 
		"		s.trn_dt," + 
		"       s.net_trn_amt, " + 
		"       s.vat_trn_amt, " +
		"		s.tot_vat_trn_amt " + 
		"  		from tax_inv s " + 
		" 		where s.store_id = '"+storeId+"' " +
		"		and s.issue_dt >= to_date('"+sf.format(month)+"', 'DD/MM/YYYY') "+
		"		and s.issue_dt < to_date('"+sf.format(cal.getTime())+"', 'DD/MM/YYYY')";
		
		JdbcTemplate jt = getJdbcTemplate();
		
		return jt.query(sql,  new RowMapperResultReader(new TaxInvoiceMapper()));
	}
}

class  MemberSaleMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int index) throws SQLException {
		int i = 1;
		Member_sale ms = new Member_sale();
		ms.setTransactionDate(rs.getDate(i++));
		ms.setStore(rs.getString(i++));
		ms.setPosNo(rs.getString(i++));
		ms.setTicketNo(rs.getString(i++));
		ms.setAmt((rs.getDouble(i++)));
		ms.setCardNo(rs.getString(i++));
		ms.setCust(rs.getString(i++));
		ms.setUser(rs.getString(i++));
		ms.setCust_CreateDate(rs.getString(i++));
		return ms;
	}
}
 
class  SalesTransactionMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int index) throws SQLException {
		int i = 1;
		TrnReport trn = new TrnReport();
		trn.setPosId(rs.getString(i++));
		trn.setTicketNo(rs.getString(i++));
		trn.setRegisterNo(rs.getString(i++));
		trn.setNetTrnAmt(rs.getDouble(i++));
		trn.setVatTrnAmt(rs.getDouble(i++));
		trn.setTotVatTrnAmt(rs.getDouble(i++));
		trn.setStoreId(rs.getString(i++));
		return trn;
	}
}

class  TaxInvoiceMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int index) throws SQLException {
		int i = 1;
		TaxInvReport tiv = new TaxInvReport();
		tiv.setStoreId(rs.getString(i++));
		tiv.setRegisterNo(rs.getString(i++));
		tiv.setPosId(rs.getString(i++));
		tiv.setTicketNo(rs.getString(i++));
		tiv.setSalesDocNo(rs.getString(i++));
		tiv.setTaxInvTypId(rs.getString(i++));
		tiv.setTaxInvId(rs.getString(i++));
		tiv.setCustName(rs.getString(i++));
		tiv.setIssueDt(rs.getDate(i++));
		tiv.setTrnDt(rs.getDate(i++));
		tiv.setNetTrnAmt(rs.getDouble(i++));
		tiv.setVatTrnAmt(rs.getDouble(i++));
		tiv.setTotVatTrnAmt(rs.getDouble(i++));
		return tiv;
	}
}
