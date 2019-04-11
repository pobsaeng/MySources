package com.ie.icon.dao.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.common.util.DateTimeUtil;
import com.ie.icon.dao.VendorCouponJdbcDao;
import com.ie.icon.dao.jdbc.JdbcSalesReportDao.Bacth_Dummy_Mapper;
import com.ie.icon.domain.Store;
import com.ie.icon.domain.Vendor;
import com.ie.icon.domain.vendorcoupon.SumVendorCoupon;
import com.ie.icon.domain.vendorcoupon.SumVendorCouponItem;
import com.ie.icon.domain.vendorcoupon.TransactionVendorCoupon;
import com.ie.icon.domain.vendorcoupon.TransactionVendorCouponItem;

public class JdbcVendorCouponDao extends JdbcDaoSupport implements VendorCouponJdbcDao{
	private SimpleDateFormat sf, year, month;
	public JdbcVendorCouponDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
		year = new SimpleDateFormat("yyyy");
		month = new SimpleDateFormat("MM");
	}
	public List trnVendorCouponItem(String year , String month){
		List resultList = null;
		TransactionVendorCoupon trnVC = new TransactionVendorCoupon();
//		String sqlString = "select (svc.vendor_id ||'-'|| svc.vendor_nm) as vendor , " +
		String sqlString = "select * " +
							" from trn_vendor_coupon_item  "+
							" where physical_year = '"+year+"' "+
							" and physical_month = '"+month+"'";
		JdbcTemplate jt = getJdbcTemplate();
		resultList = jt.query(sqlString, new RowMapperResultReader(new VendorCouponItemMapper()));
		return resultList;
		
		
	}
	
	public List transactionVendorCoupon(String year , String month){
		List resultList = null;
		String sqlString = "select * "+
		" from trn_vendor_coupon tvc   "+
		" where  tvc.physical_year = '"+year+"' "+
		" and tvc.physical_month = '"+month+"'";
		JdbcTemplate jt = getJdbcTemplate();
		resultList = jt.query(sqlString, new RowMapperResultReader(new VendorCouponMapper()));
		
		return resultList;
		
	}
	
	class VendorCouponMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			TransactionVendorCoupon trnVc = new TransactionVendorCoupon();
			//trnVc.setVendorDesc(rs.getString(1));
			trnVc.setVendorId(rs.getString(3));
			trnVc.setVendorName(rs.getString(8));
			trnVc.setStoreId(new Store());
			trnVc.getStoreId().setStoreId(rs.getString(4));
			trnVc.setCouponAmount(rs.getBigDecimal(6));
			trnVc.setDocumentNo(rs.getString(7));
			trnVc.setPhysicalYear(rs.getString(1));
			trnVc.setPhysicalMonth(rs.getString(2));
			trnVc.setPostingDate(rs.getDate(5));
			trnVc.setInterfaceDate(rs.getDate(13));
			trnVc.setInterfaceUserId(rs.getString(14));
			trnVc.setTypeId(rs.getString(15));
			return trnVc;
			
		}
	}
	
	class VendorCouponItemMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			TransactionVendorCouponItem trnVci = new TransactionVendorCouponItem();
			trnVci.setPhysicalYear(rs.getString(1));
			trnVci.setPhysicalMonth(rs.getString(2));
			trnVci.setVendorId(rs.getString(3));
			trnVci.setStoreId(new Store());
			trnVci.getStoreId().setStoreId(rs.getString(4));
			trnVci.setPostingDate(rs.getDate(5));
			trnVci.setStoreItemId(new Store());
			trnVci.getStoreItemId().setStoreId(rs.getString(6));
			trnVci.setCouponItemAmount(rs.getBigDecimal(7));
			trnVci.setTypeId(rs.getString(8));
			trnVci.setItemNo(rs.getString(9));
			
			return trnVci;
			
		}
	}
	
	public boolean deleteTransactionVendorCoupon(String month , String year){
		String sqlString = "delete from trn_vendor_coupon where physical_year = ? , physical_month = ?";
		JdbcTemplate jt = getJdbcTemplate();
		Object[] args = new Object[] {month,year};
		return jt.update(sqlString,args) > 0;
	}
	
	public boolean deleteSumVendorCoupon(Date fromDate , Date toDate){
		String sqlString = "delete from sum_vendor_coupon where trn_dt >= TO_DATE ('"+sf.format(fromDate)+"', 'DD-MM-YYYY') and trn_dt <= TO_DATE ('"+sf.format(toDate)+"', 'DD-MM-YYYY')";
		JdbcTemplate jt = getJdbcTemplate();
		//Object[] args = new Object[] {fromDate,toDate};
		return jt.update(sqlString) > 0;
	}
	
	public boolean deleteSumVendorCouponItem(Date fromDate , Date toDate){
		String sqlString = "delete from sum_vendor_coupon_item where trn_dt >= TO_DATE ('"+sf.format(fromDate)+"', 'DD-MM-YYYY') and trn_dt <= TO_DATE ('"+sf.format(toDate)+"', 'DD-MM-YYYY')";
		JdbcTemplate jt = getJdbcTemplate();
		//Object[] args = new Object[] {fromDate,toDate};
		return jt.update(sqlString) > 0;
	}
	
	public int createSumVendorCoupon(List SumVendorCouponList , List SumVendorCouponItemList) throws CannotGetJdbcConnectionException, SQLException{
		Connection connection = getConnection();
		int ret = 0;
		
		

		
		
//		for (int i = 0; i < SumVendorCouponList.size(); i++) {
//			String error = "";
//			try {
//				SumVendorCoupon sumVC = (SumVendorCoupon) SumVendorCouponList
//						.get(i);
//				if (sumVC.getStore().getStoreId() == null
//						|| sumVC.getTransactionDate() == null
//						|| sumVC.getVendorId() == null
//						|| sumVC.getPromotionId() == null
//						|| sumVC.getTicketNo() == null
//						|| sumVC.getPosId() == null) {
//					continue;
//				}
//				error = sumVC.getTransactionDate()+":"+sumVC.getStore().getStoreId()
//				+":"+sumVC.getVendorId()+":"+sumVC.getPromotionId()+":"+sumVC.getTicketNo()
//				+":"+sumVC.getPosId()+":"+sumVC.getArticleId();
//				PreparedStatement pre = null;
//				String sqlString = "insert into sum_vendor_coupon "
//						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, null, null, ?, null, ?, ?, ?, ?)";
//				pre = connection.prepareStatement(sqlString);
//				pre.setDate(1, (java.sql.Date) sumVC.getTransactionDate());
//				pre.setString(2, sumVC.getStore().getStoreId());
//				pre.setString(3, sumVC.getVendorId());
//				pre.setString(4, sumVC.getPromotionId());
//				pre.setString(5, sumVC.getTicketNo());
//				pre.setString(6, sumVC.getPosId());
//				pre.setString(7, sumVC.getVendorName());
//				pre.setString(8, sumVC.getPromotionName());
//				pre.setBigDecimal(9, sumVC.getCouponAmount());
//				pre.setString(10, sumVC.getTypeId());
//				pre.setString(11, sumVC.getArticleId());
//				pre.setString(12, sumVC.getArticleDescription());
//				pre.setBigDecimal(13, sumVC.getEligibleQty());
//				pre.setBigDecimal(14, sumVC.getNetTransactionAmount());
//				ret += pre.executeUpdate();
//				pre.close();
//				
//			
//			
//			} catch (Exception e) {
//				e.printStackTrace();
//				
//				System.out.println("ER"+error);
//			} 
//			
//		}

		//getConnection().commit();
//		if(ret>0){
//			for (int i = 0; i < SumVendorCouponItemList.size(); i++) {
//				SumVendorCouponItem svi = new SumVendorCouponItem();
//				PreparedStatement pre = null;
//				String sqlString = " insert into sum_vendor_coupon_item "+
//				" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//				
//				pre = getConnection().prepareStatement(sqlString);
//				pre.setDate(1, new java.sql.Date(svi.getTransactionDate().getTime()));
//				pre.setString(2, svi.getStore().getStoreId());
//				pre.setString(3, svi.getVendorId());
//				pre.setString(4, svi.getPromotionId());
//				pre.setString(5, svi.getTicketNo());
//				pre.setString(6, svi.getPosId());
//				pre.setString(7, svi.getItemNo());
//				pre.setString(8, svi.getArticleId());
//				pre.setString(9, svi.getArticleDescription());
//				pre.setBigDecimal(10, svi.getEligibleQty());
//				pre.setBigDecimal(11, svi.getNetTransactionAmount());
//				ret = pre.executeUpdate(); 
//			}
//		}
		
		connection.close();
		return ret;
		
		
	}
	public List getSumVendorCoupon(String vendorId, Date fromDate, Date toDate) {
		String sql =
			"(select distinct ps.prmtn_id as prmtn_id, " +
			"       ps.prmtn_nm, " + 
			"       pdr.vendor_id, vd.vendor_nm as vendor, " + 
			"       p.rdptn_dt, " + 
			"       p.rdptn_dt as trn_dt, " + 
			"       p.store_id, " + 
			"       ps.trn_ref as trn_ref " + 
			"       , 'DR' as typ_id " + 
			"      ,a.artc_id, a.artc_dsc , " + 
			"       SUM(nvl(psi.eligible_qty, 0)) as eligible_qty, " + 
			"       SUM(nvl(psi.net_amt, 0)) as net_amt, " + 
			"       SUM(nvl(pdr.dscnt_amt, 0)) as dscnt_amt " + 
			" " + 
			"  from prmtn_rdptn       p, " + 
			"       prmtn_dscnt_rdptn pdr, " + 
			"       prmtn_sales       ps, " + 
			"       prmtn_sales_item  psi, " +
			"		vendor vd, " + 
			"       artc              a " + 
			" where p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid " + 
			"   and p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid " + 
			"   and pdr.prmtn_id = ps.prmtn_id " +
			"	and pdr.vendor_id = '"+vendorId+"' " + 
			"	AND vd.vendor_id = pdr.vendor_id" +
			"   and ps.prmtn_sales_oid = psi.prmtn_sales_oid " + 
			"   and psi.artc_id = a.artc_id(+) " + 
			"   and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"   and p.rdptn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " + 
			"	AND pdr.dscnt_typ           = 'COVN' " +
			"	group by ps.prmtn_id, ps.prmtn_nm, pdr.vendor_id, vd.vendor_nm , " +
			"	p.rdptn_dt, p.store_id, ps.trn_ref , a.artc_id, a.artc_dsc"+
			" " + 
			"union " + 
			" " + 
			"select distinct pdr.prmtn_id as prmtn_id, " + 
			"       ps.prmtn_nm as prmtn_nm, " + 
			"       pdr.vendor_id , vd.vendor_nm, " + 
			"       p.rdptn_dt, " + 
			"       ct.trn_dt as trn_dt, " + 
			"       p.store_id, " + 
			"       ps.trn_ref as trn_ref " + 
			"       , 'DG' as typ_id " + 
			"       ,'CN' as artc_id " +
			"		,'' as artc_dsc, " + 
			"		SUM(0) as eligible_qty, " + 
			"		SUM(nvl(ct.trn_amt, 0)) as net_amt, " + 
			"		SUM(nvl(ct.trn_amt, 0)) as dscnt_amt " + 
			" " + 
			"  from prmtn_rdptn       p, " + 
			"       prmtn_dscnt_rdptn pdr, " + 
			"       sales_trn         st, " + 
			"       csh_trn           ct, " + 
			"		vendor vd, " + 
			"       prmtn_sales       ps " + 
			" where p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid " + 
			"   and p.prmtn_rdptn_oid = st.pos_prmtn_rdptn_oid " + 
			"   and st.sales_trn_oid = ct.sales_trn_oid " + 
			"   and pdr.prmtn_id = ct.prmtn_id " + 
			"	and pdr.vendor_id = '"+vendorId+"' " + 
			"	AND vd.vendor_id = pdr.vendor_id" +
			"   and pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+) " + 
			"   and pdr.prmtn_id = ps.prmtn_id(+) " + 
			"   and ct.trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"   and ct.trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " + 
			"   and ct.csh_trn_typ_id in ('CNP', 'CNS') " +
			"	AND ct.tender_id = 'COVN' " + 
			"	group by pdr.prmtn_id, ps.prmtn_nm , pdr.vendor_id , vd.vendor_nm, " +
			"	p.rdptn_dt, ct.trn_dt, p.store_id, ps.trn_ref, 'DG', 'CN' )" +
			" " + 
			"   order by typ_id, vendor_id";

		
		JdbcTemplate jt = getJdbcTemplate();
		List sumVendorCoupon = jt.query(sql, new RowMapperResultReader(new Sum_Vendor_Coupon_Mapper()));

		return sumVendorCoupon;
	}
	
	class Sum_Vendor_Coupon_Mapper implements RowMapper{

		public Object mapRow(ResultSet rs, int i) throws SQLException {
			SumVendorCoupon svc = new SumVendorCoupon();
			svc.setPromotionId(rs.getString(1));
			svc.setPromotionName(rs.getString(2));
			svc.setVendorId(rs.getString(3));
			svc.setVendorName(rs.getString(4));
			svc.setTransactionDate(rs.getDate(6));
			svc.setStore(new Store());
			svc.getStore().setStoreId(rs.getString(7));
			svc.setPosId(rs.getString(8).substring(0, 3));
			svc.setTicketNo(rs.getString(8).substring(4));
			svc.setTypeId(rs.getString(9));
			svc.setArticleId(rs.getString(10));
			svc.setArticleDescription(rs.getString(11));
			svc.setEligibleQty(rs.getBigDecimal(12));
			svc.setNetTransactionAmount(rs.getBigDecimal(13));
			svc.setCouponAmount(rs.getBigDecimal(14));
			return svc;
		}
		
	}

	public List getSumVendorCouponItem(String vendorId, Date fromDate, Date toDate) {
		String sql = "   select" + 
			"       ps.prmtn_id as prmtn_id," + 
			"       pdr.vendor_id," + 
			"       p.rdptn_dt," + 
			"       p.store_id," + 
			"       ps.trn_ref as trn_ref," + 
			"       a.artc_id, a.artc_dsc ," + 
			"       nvl(psi.eligible_qty, 0) as eligible_qty," + 
			"       nvl(psi.net_amt, 0) as net_amt" + 
			" " + 
			"  from prmtn_rdptn       p," + 
			"       prmtn_dscnt_rdptn pdr," + 
			"       prmtn_sales       ps," + 
			"       prmtn_sales_item  psi," + 
			"       artc              a" + 
			" where p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid" + 
			"   and p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid" + 
			"   and pdr.prmtn_id = ps.prmtn_id" + 
			"	and pdr.vendor_id = '"+vendorId+"' " + 
			"   and ps.prmtn_sales_oid = psi.prmtn_sales_oid" + 
			"   and psi.artc_id = a.artc_id(+)" + 
			"   and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY')" + 
			"   and p.rdptn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY')" + 
			"	AND pdr.dscnt_typ = 'COVN' " +
			" union" + 
			" " + 
			" select" + 
			"       pdr.prmtn_id as prmtn_id," + 
			"       pdr.vendor_id, " + 
			"       p.rdptn_dt, " + 
			"       p.store_id, " + 
			"       ps.trn_ref as trn_ref, " + 
			"       'CN' as artc_id, " + 
			"       'CN' as artc_dsc, " + 
			"       0 as eligible_qty, " + 
			"       nvl(ct.trn_amt, 0) as net_amt " + 
			" " + 
			"  from prmtn_rdptn       p, " + 
			"       prmtn_dscnt_rdptn pdr, " + 
			"       sales_trn         st, " + 
			"       csh_trn           ct, " + 
			"       prmtn_sales       ps " + 
			" where p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid " + 
			"   and p.prmtn_rdptn_oid = st.pos_prmtn_rdptn_oid " + 
			"   and st.sales_trn_oid = ct.sales_trn_oid " + 
			"   and pdr.prmtn_id = ct.prmtn_id " + 
			"	and pdr.vendor_id = '"+vendorId+"' " + 
			"   and pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+) " + 
			"   and pdr.prmtn_id = ps.prmtn_id(+) " + 
			"   and ct.trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"   and ct.trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " + 
			"   and ct.csh_trn_typ_id in ('CNP', 'CNS')" +
			"	AND ct.tender_id = 'COVN' " ;

		
		JdbcTemplate jt = getJdbcTemplate();
		List sumVendorCouponItem = jt.query(sql, new RowMapperResultReader(new Sum_Vendor_Coupon_Item_Mapper()));

		return sumVendorCouponItem;
	}
	
	class Sum_Vendor_Coupon_Item_Mapper implements RowMapper{

		public Object mapRow(ResultSet rs, int i) throws SQLException {
			SumVendorCouponItem vi = new SumVendorCouponItem();
			vi.setPromotionId(rs.getString(1));
			vi.setVendorId(rs.getString(2));
			vi.setTransactionDate(rs.getDate(3));
			vi.setStore(new Store());
			vi.getStore().setStoreId(rs.getString(4));
			vi.setPosId(rs.getString(5).substring(0, 3));
			vi.setTicketNo(rs.getString(5).substring(4));
			vi.setArticleId(rs.getString(6));
			vi.setArticleDescription(rs.getString(7));
			vi.setEligibleQty(rs.getBigDecimal(8));
			vi.setNetTransactionAmount(rs.getBigDecimal(9));
			
			return vi;
		}
		
	}

	public List inquireTrnVendorCoupom(List vendorList, Date fromDate, Date toDate, Date postDate) throws SQLException {
		ArrayList vendorCouponList = new ArrayList();
		String vendorId = "";
		String sql = null;
		JdbcTemplate jt = getJdbcTemplate();
		for (int i = 0; i < vendorList.size(); i++) {
			Vendor vendor = (Vendor) vendorList.get(i);
			sql = "select year, month, vendor_id, vendor_nm, store_id, sum(amt) as amt " +
			"from (select  distinct  '"+year.format(fromDate)+"' as year, " + 
			"      '"+month.format(fromDate)+"' as month, " + 
			"      vendor_id, " + 
			"      vendor_nm, " + 
			"      'other' as store_id, " + 
			"      svc.ticket_no, " + 
			"      coupon_amt as amt " + 
			" from sum_vendor_coupon svc " + 
			"where trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"  and trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " +
			"	and vendor_id in ("+vendor.getVendorId()+")" +
			"   and store_id not like 'XP%' " + 
			") group by year, month, vendor_id, vendor_nm, store_id";
			
			
			vendorCouponList.addAll(jt.query(sql, new RowMapperResultReader(new TrnVendorCouponMapper())));
			
			sql = "select year, month, vendor_id, vendor_nm, store_id, sum(amt) as amt " +
			"from (select  distinct  '"+year.format(fromDate)+"' as year, " + 
			"      '"+month.format(fromDate)+"' as month, " + 
			"      vendor_id, " + 
			"      vendor_nm, " + 
			"      'expo' as store_id, " + 
			"      svc.ticket_no, " + 
			"      coupon_amt as amt " + 
			" from sum_vendor_coupon svc " + 
			"where trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"  and trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " +
			"	and vendor_id  in ("+vendor.getVendorId()+")" +
			"   and store_id like 'XP%' " + 
			" ) group by year, month, vendor_id, vendor_nm, store_id";
	
			jt = getJdbcTemplate();
			vendorCouponList.addAll(jt.query(sql, new RowMapperResultReader(new TrnVendorCouponMapper())));
			
			vendorId += "'"+vendor.getVendorId()+"'";
			if(i<(vendorList.size()-1)) vendorId += ", ";
		}
		

		sql = "select year, month, vendor_id, vendor_nm, store_id, sum(amt) as amt " +
		"from (select  distinct  '"+year.format(fromDate)+"' as year, " + 
		"      '"+month.format(fromDate)+"' as month, " + 
		"      vendor_id, " + 
		"      vendor_nm, " + 
		"      'other' as store_id, " + 
		"      svc.ticket_no, " + 
		"      coupon_amt as amt " + 
		" from sum_vendor_coupon svc " + 
		"where trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
		"  and trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') ";
		if(vendorId!=null&&vendorId.trim()!=""){
			sql += "	and vendor_id not in ("+vendorId+") ";
		}
		sql += ") group by year, month, vendor_id, vendor_nm, store_id";

		
//		 "select  '"+year.format(fromDate)+"' as year, '"+month.format(fromDate)+"' as month, " +
//		"	vendor_id, vendor_nm,'other' as store_id, sum(coupon_amt) as amt " +
//		"   from sum_vendor_coupon svc " + 
//		"   where trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
//		"   and trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') ";
//		if(vendorId!=null&&vendorId.trim()!=""){
//			sql += "	and vendor_id not in ("+vendorId+") ";
//		}
//		"   group by vendor_id, vendor_nm";

		jt = getJdbcTemplate();
		vendorCouponList.addAll(jt.query(sql, new RowMapperResultReader(new TrnVendorCouponMapper())));
		
		
		return vendorCouponList;
		
	}
	
	class TrnVendorCouponMapper implements RowMapper{

		public Object mapRow(ResultSet rs, int i) throws SQLException {
			TransactionVendorCoupon tvc = new TransactionVendorCoupon();
			tvc.setPhysicalYear(rs.getString(1));
			tvc.setPhysicalMonth(rs.getString(2));
			tvc.setVendorId(rs.getString(3));
			tvc.setVendorName(rs.getString(4));
			tvc.setStoreId(new Store());
			tvc.getStoreId().setStoreId(rs.getString(5));
			tvc.setCouponAmount(rs.getBigDecimal(6));
			if(tvc.getCouponAmount().compareTo(new BigDecimal(0))>=0){
				tvc.setTypeId("DR");
			}else{
				tvc.setTypeId("DG");
			}
			
			return tvc;
		}
		
	}

	public List inquireTrnVendorCouponItem(List vendorList, Date fromDate,
			Date toDate, Date postDate) throws CannotGetJdbcConnectionException, SQLException {
		ArrayList vendorCouponItemList = new ArrayList();
		String vendorId = "";
		String sql = null;
		JdbcTemplate jt = getJdbcTemplate();
		for (int i = 0; i < vendorList.size(); i++) {
			Vendor vendor = (Vendor) vendorList.get(i);
			
			sql = "select year, month," +
			"		vendor_id," +
			"       vendor_nm," + 
			"       'other' as store_id," + 
			"       sum(amt) as amt," + 
			"       store_id as store_item_id"+
			"  from (select distinct '"+year.format(fromDate)+"' as year, " + 
			"                        '"+month.format(fromDate)+"' as month, " + 
			"                        vendor_id, " + 
			"                        vendor_nm, " + 
			"                        store_id, " + 
			"                        svc.ticket_no, " + 
			"                        coupon_amt as amt " + 
			"          from sum_vendor_coupon svc " + 
			"         where trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"           and trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') "  +
			"	and vendor_id = '"+vendor.getVendorId()+"' " + 
			"   and store_id not like 'XP%' " + 
			" ) group by year, month, vendor_id, vendor_nm, store_id " + 
			" order by vendor_id";
			
			
			vendorCouponItemList.addAll(jt.query(sql, new RowMapperResultReader(new TrnVendorCouponItemMapper())));
			
			sql = "select year, month," +
			"		vendor_id," +
			"       vendor_nm," + 
			"       'expo' as store_id," + 
			"       sum(amt) as amt," + 
			"       store_id as store_item_id"+
			"  from (select distinct '"+year.format(fromDate)+"' as year, " + 
			"                        '"+month.format(fromDate)+"' as month, " + 
			"                        vendor_id, " + 
			"                        vendor_nm, " + 
			"                        store_id, " + 
			"                        svc.ticket_no, " + 
			"                        coupon_amt as amt " + 
			"          from sum_vendor_coupon svc " + 
			"         where trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"           and trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') "  +
			"	and vendor_id = '"+vendor.getVendorId()+"' " + 
			"   and store_id like 'XP%' " + 
			" ) group by year, month, vendor_id, vendor_nm, store_id " + 
			" order by vendor_id";
	
			jt = getJdbcTemplate();
			vendorCouponItemList.addAll(jt.query(sql, new RowMapperResultReader(new TrnVendorCouponItemMapper())));
			
			vendorId += "'"+vendor.getVendorId()+"'";
			if(i<(vendorList.size()-1)) vendorId += ", ";
		}
		
		sql = "select year, month," +
		"		vendor_id," +
		"       vendor_nm," + 
		"       'other' as store_id," + 
		"       sum(amt) as amt," + 
		"       store_id as store_item_id"+
			"  from (select distinct '"+year.format(fromDate)+"' as year, " + 
			"                        '"+month.format(fromDate)+"' as month, " + 
			"                        vendor_id, " + 
			"                        vendor_nm, " + 
			"                        store_id, " + 
			"                        svc.ticket_no, " + 
			"                        coupon_amt as amt " + 
			"          from sum_vendor_coupon svc " + 
			"         where trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"           and trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " ;
			if(vendorId!=null&&vendorId.trim()!=""){
				sql += "	and vendor_id not in ("+vendorId+") ";
			}
			sql +=" ) group by year, month, vendor_id, vendor_nm, store_id " + 
			" order by vendor_id, store_item_id";



		jt = getJdbcTemplate();
		vendorCouponItemList.addAll(jt.query(sql, new RowMapperResultReader(new TrnVendorCouponItemMapper())));
		
		return vendorCouponItemList;
		
	}
	
	class TrnVendorCouponItemMapper implements RowMapper{

		public Object mapRow(ResultSet rs, int i) throws SQLException {
			TransactionVendorCouponItem tvc = new TransactionVendorCouponItem();
			tvc.setPhysicalYear(rs.getString(1));
			tvc.setPhysicalMonth(rs.getString(2));
			tvc.setVendorId(rs.getString(3));
			tvc.setStoreId(new Store());
			tvc.getStoreId().setStoreId(rs.getString(5));
			tvc.setCouponItemAmount(rs.getBigDecimal(6));
			if(tvc.getCouponItemAmount().compareTo(new BigDecimal(0))>=0){
				tvc.setTypeId("DR");
			}else{
				tvc.setTypeId("DG");
			}
			tvc.setStoreItemId(new Store());
			tvc.getStoreItemId().setStoreId(rs.getString(7));
			
			return tvc;
		}
		
	}

	public boolean insertTrnVendorCoupon(TransactionVendorCoupon tvc, Date postDate) {
		int ret = 0;
		int retitem = 0;
		Calendar cal = Calendar.getInstance(Locale.US);
		//for (int i = 0; i < trnVendorCouponList.size(); i++) {
			
			//TransactionVendorCoupon tvc =  (TransactionVendorCoupon) trnVendorCouponList.get(i);
			
//			PreparedStatement pre ;
//			String sqlString =  "insert into trn_vendor_coupon "+
//			" values (?, ?, ?, ?, ?, ?, null, ?, ?, null, null, null, null, null, ?)";

			try {
			String sqlString =  "insert into trn_vendor_coupon "+
			" values ('"+tvc.getPhysicalYear()+"'" +
					",'"+tvc.getPhysicalMonth()+"'" +
					", '"+tvc.getVendorId()+"'" +
					", '"+tvc.getStoreId().getStoreId()+"'" +
					", to_date('"+sf.format(postDate)+"', 'DD/MM/YYYY')" +
					", '"+tvc.getCouponAmount()+"'" +
					", null" +
					", '"+tvc.getVendorName()+"'" +
					", to_date('"+sf.format(cal.getTime())+"', 'DD/MM/YYYY')" +
					", null, null, null, null, null" +
					", '"+tvc.getTypeId()+"'" +
					",null,null)";
			
			JdbcTemplate jt = getJdbcTemplate();
			ret = jt.update(sqlString);
			
			
//				pre = getConnection().prepareStatement(sqlString);
//				pre.setString(1, tvc.getPhysicalYear());
//				pre.setString(2, tvc.getPhysicalMonth());
//				pre.setString(3, tvc.getVendorId());
//				pre.setString(4, tvc.getStoreId().getStoreId());
//				pre.setDate(5, new java.sql.Date(postDate.getTime()));
//				pre.setBigDecimal(6, tvc.getCouponAmount());
//				pre.setString(7, tvc.getVendorName());
//				pre.setDate(8, new java.sql.Date(new DateTimeUtil().getCurrentDateTime().getTime()));
//				pre.setString(9, tvc.getTypeId());
//				//pre.setDate(11, null);
//				ret += pre.executeUpdate(); 
//				pre.close();
				
		

			if(ret>0){
				for (int j = 0; j < tvc.getTransactionVendorCouponItem().size(); j++) {
					
					TransactionVendorCouponItem tvci =  (TransactionVendorCouponItem) tvc.getTransactionVendorCouponItem().get(j);
					
					sqlString =  "insert into trn_vendor_coupon_item "+
					" values ('"+tvci.getPhysicalYear()+"'" +
					", '"+tvci.getPhysicalMonth()+"'" +
					", '"+tvci.getVendorId()+"'" +
					", '"+tvci.getStoreId().getStoreId()+"'" +
					", to_date('"+sf.format(postDate)+"', 'DD/MM/YYYY')" +
					", '"+tvci.getStoreItemId().getStoreId()+"'" +
					", '"+tvci.getCouponItemAmount()+"'" +
					", '"+tvci.getTypeId()+"'" +
					", '"+tvci.getItemNo()+"')";
				
					jt = getJdbcTemplate();
					ret = jt.update(sqlString);
					
	//				pre = getConnection().prepareStatement(sqlString);
	//				pre.setString(1, tvci.getPhysicalYear());
	//				pre.setString(2, tvci.getPhysicalMonth());
	//				pre.setString(3, tvci.getVendorId());
	//				pre.setString(4, tvci.getStoreId().getStoreId());
	//				pre.setDate(5, new java.sql.Date(postDate.getTime()));
	//				pre.setString(6, tvci.getStoreItemId().getStoreId());
	//				pre.setBigDecimal(7, tvci.getCouponItemAmount());
	//				pre.setString(8, tvci.getTypeId());
	//				pre.setString(9, tvci.getItemNo());
	//				retitem = pre.executeUpdate(); 
	//				pre.close();
					
					
					
					}
				}
			} catch (CannotGetJdbcConnectionException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		//}
		System.out.println(ret+" : "+retitem);
		
		if(ret>0){
			return false;
		}else{
			return true;
		}
	}
	public int updateTrnVendorCoupon(TransactionVendorCoupon tvc) throws CannotGetJdbcConnectionException, SQLException {
		int ret = 0;
	
		String sqlString = "update trn_vendor_coupon " +
			"set inf_user_id = '"+tvc.getInterfaceUserId()+"' " +
			", inf_dttm = to_date('"+sf.format(tvc.getInterfaceDate())+"', 'DD-MM-YYYY') ";
			if(tvc.getDocumentNo()!=null){
				sqlString += ", document_no = '"+tvc.getDocumentNo()+"' ";
			}
			if(tvc.getInterfaceStatus()!=null){
				sqlString += ", inf_sts = '"+tvc.getInterfaceStatus()+"' ";
			}
			if(tvc.getInterfaceMessage()!=null){
				sqlString += ", inf_msg_log = '"+tvc.getInterfaceMessage()+"' ";
			}
			
			 
			sqlString += "where physical_year = '"+tvc.getPhysicalYear()+"' " + 
			"and physical_month = '"+tvc.getPhysicalMonth()+"' " + 
			"and vendor_id = '"+tvc.getVendorId()+"' " + 
			"and store_id = '"+tvc.getStoreId().getStoreId()+"' " + 
			"and typ_id = '"+tvc.getTypeId()+"'";

		JdbcTemplate jt = getJdbcTemplate();
		ret = jt.update(sqlString);
		
			
		return ret;
		
	}
	public void updateSumVendorCoupon(TransactionVendorCoupon tvc) {
		int ret = 0;
		String year = tvc.getPhysicalYear();
		String month = tvc.getPhysicalMonth();
		Integer x = Integer.valueOf(tvc.getPhysicalMonth());
		int y = x.intValue()+1;
		String beforeMonth = String.valueOf(y);
		if(beforeMonth.length()==1) beforeMonth = "0"+beforeMonth;
		
		for (int i = 0; i < tvc.getTransactionVendorCouponItem().size(); i++) {
			TransactionVendorCouponItem tvci = (TransactionVendorCouponItem) tvc.getTransactionVendorCouponItem().get(i);
			String sqlString = "update sum_vendor_coupon " +
			"set document_no = '"+tvc.getDocumentNo()+"' " +
			", inf_dttm = to_date('"+sf.format(tvc.getInterfaceDate())+"', 'DD-MM-YYYY') " +
			"where trn_dt >= to_date('01/"+month+"/"+year+"', 'DD/MM/YYYY') "+
			"and trn_dt < to_date('01/"+beforeMonth+"/"+year+"', 'DD/MM/YYYY') "+
			"and vendor_id = '"+tvc.getVendorId()+"' " + 
			"and store_id = '"+tvci.getStoreItemId().getStoreId()+"' ";
			
		JdbcTemplate jt = getJdbcTemplate();
		ret = jt.update(sqlString);
		}
		
		
	}
	public List getVendorIdList(Date fromDate, Date toDate) {
		String sql = "SELECT DISTINCT vendor_id " +
		"FROM prmtn_rdptn p, " +
		"  prmtn_dscnt_rdptn pdr " +
		"WHERE p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid " +
		"AND pdr.dscnt_typ           = 'COVN' " +
		"   and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
		"   and p.rdptn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') ";
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(sql,new RowMapper() {
	        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	            return rs.getString(1);
	        }
	    });
	}
	public List getSumVendorCoupon(Date fromDate, Date toDate) {
		String sql =
			"select distinct ps.prmtn_id as prmtn_id, " +
			"       ps.prmtn_nm, " + 
			"       pdr.vendor_id, pdr.vendor_nm as vendor, " + 
			"       p.rdptn_dt, " + 
			"       null as trn_dt, " + 
			"       p.store_id, " + 
			"       ps.trn_ref as trn_ref " + 
			"       , 'DR' as typ_id " + 
			"      ,a.artc_id, a.artc_dsc , " + 
			"       nvl(psi.eligible_qty, 0) as eligible_qty, " + 
			"       nvl(psi.net_amt, 0) as net_amt, " + 
			"       nvl(pdr.dscnt_amt, 0) as dscnt_amt " + 
			" " + 
			"  from prmtn_rdptn       p, " + 
			"       prmtn_dscnt_rdptn pdr, " + 
			"       prmtn_sales       ps, " + 
			"       prmtn_sales_item  psi, " + 
			"       artc              a " + 
			" where p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid " + 
			"   and p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid " + 
			"   and pdr.prmtn_id = ps.prmtn_id " + 
			"   and ps.prmtn_sales_oid = psi.prmtn_sales_oid " + 
			"   and psi.artc_id = a.artc_id(+) " + 
			"   and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"   and p.rdptn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " + 
			" " + 
			" " + 
			"union " + 
			" " + 
			"select distinct pdr.prmtn_id as prmtn_id, " + 
			"       ps.prmtn_nm as prmtn_nm, " + 
			"       pdr.vendor_id , pdr.vendor_nm, " + 
			"       p.rdptn_dt, " + 
			"       ct.trn_dt as trn_dt, " + 
			"       p.store_id, " + 
			"       ps.trn_ref as trn_ref " + 
			"       , 'DG' as typ_id " + 
			"       ,'CN' as artc_id " +
			"		,'' as artc_dsc, " + 
			"		0 as eligible_qty, " + 
			"		nvl(ct.trn_amt, 0) as net_amt, " + 
			"		nvl(ct.trn_amt, 0) as dscnt_amt " + 
			" " + 
			"  from prmtn_rdptn       p, " + 
			"       prmtn_dscnt_rdptn pdr, " + 
			"       sales_trn         st, " + 
			"       csh_trn           ct, " + 
			"       prmtn_sales       ps " + 
			" where p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid " + 
			"   and p.prmtn_rdptn_oid = st.pos_prmtn_rdptn_oid " + 
			"   and st.sales_trn_oid = ct.sales_trn_oid " + 
			"   and pdr.prmtn_id = ct.prmtn_id " + 
			"   and pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+) " + 
			"   and pdr.prmtn_id = ps.prmtn_id(+) " + 
			"   and ct.trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
			"   and ct.trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " + 
			"   and ct.csh_trn_typ_id in ('CNP', 'CNS') " +
			"	AND ct.tender_id = 'COVN' " + 
			" " + 
			"   order by typ_id, vendor_id";

		
		JdbcTemplate jt = getJdbcTemplate();
		List sumVendorCoupon = jt.query(sql, new RowMapperResultReader(new Sum_Vendor_Coupon_Mapper()));

		return sumVendorCoupon;
	}
	public int createSumVendorCoupon(Date fromDate, Date toDate) {
		String sqlString = "INSERT " +
		"INTO " +
		"  SUM_VENDOR_COUPON " +
		"  ( " +
		"    TRN_DT, " +
		"    STORE_ID, " +
		"    VENDOR_ID, " +
		"    PRMTN_ID, " +
		"    TICKET_NO, " +
		"    POS_ID, " +
		"    VENDOR_NM, " +
		"    PRMTN_NM, " +
		"    COUPON_AMT, " +
		"    TYP_ID, " +
		"    ARTC_ID, " +
		"    ARTC_DSC, " +
		"    ELIGIBLE_QTY, " +
		"    NET_AMT " +
		"  ) " +
		"  ( " +
		"    SELECT DISTINCT " +
		"      p.rdptn_dt, " +
		"      p.store_id, " +
		"      pdr.vendor_id, " +
		"      ps.prmtn_id, " +
		"      SUBSTR(ps.trn_ref, 5,10), " +
		"      SUBSTR(ps.trn_ref, 1,3), " +
		"      vd.vendor_nm, " +
		"      prmtn.prmtn_nm, " +
		"      SUM(NVL(pdr.dscnt_amt, 0)), " +
		"      'DR' AS typ_id, " +
		"      a.artc_id, " +
		"      a.artc_dsc , " +
		"      SUM(NVL(psi.eligible_qty, 0) )AS eligible_qty, " +
		"      SUM(NVL(psi.net_amt, 0) )     AS net_amt " +
		"    FROM " +
		"      prmtn_rdptn p, " +
		"      prmtn_dscnt_rdptn pdr, " +
		"      prmtn_sales ps, " +
		"      prmtn_sales_item psi, " +
		"      artc a, " +
		"      vendor vd, " +
		"	   promotion prmtn " +
		"    WHERE " +
		"      p.prmtn_rdptn_oid    = pdr.prmtn_rdptn_oid " +
		"    AND p.prmtn_rdptn_oid  = ps.prmtn_rdptn_oid " +
		"    AND pdr.prmtn_id       = ps.prmtn_id " +
		"    AND pdr.vendor_id      = vd.vendor_id " +
		"    AND ps.prmtn_sales_oid = psi.prmtn_sales_oid " +
		"    AND psi.artc_id        = a.artc_id(+) " +
		"   and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
		"   and p.rdptn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " + 
		"    AND pdr.dscnt_typ      = 'COVN' " +
		"	AND ps.prmtn_id = prmtn.prmtn_id " +
		"	AND prmtn.is_active = 'Y'" +
		"    GROUP BY " +
		"      ps.prmtn_id, " +
		"      prmtn.prmtn_nm, " +
		"      pdr.vendor_id, " +
		"      vd.vendor_nm , " +
		"      p.rdptn_dt, " +
		"      p.store_id, " +
		"      ps.trn_ref , " +
		"      a.artc_id, " +
		"      a.artc_dsc " +
		"    UNION " +
		"    SELECT DISTINCT " +
		"      ct.trn_dt , " +
		"      p.store_id, " +
		"      pdr.vendor_id, " +
		"      ps.prmtn_id, " +
		"      SUBSTR(ps.trn_ref, 5,10), " +
		"      SUBSTR(ps.trn_ref, 1,3), " +
		"      vd.vendor_nm, " +
		"      prmtn.prmtn_nm, " +
		"      SUM( NVL(ct.trn_amt, 0) )AS dscnt_amt, " +
		"      'DG'                     AS typ_id, " +
		"      'CN'                     AS artc_id , " +
		"      ''                       AS artc_dsc, " +
		"      SUM( 0 )                 AS eligible_qty, " +
		"      SUM( NVL(ct.trn_amt, 0)) AS net_amt " +
		"    FROM " +
		"      prmtn_rdptn p, " +
		"      prmtn_dscnt_rdptn pdr, " +
		"      sales_trn st, " +
		"      csh_trn ct, " +
		"      prmtn_sales ps, " +
		"      vendor vd," +
		"	   promotion prmtn " +
		"    WHERE " +
		"      p.prmtn_rdptn_oid     = pdr.prmtn_rdptn_oid " +
		"    AND p.prmtn_rdptn_oid   = st.pos_prmtn_rdptn_oid " +
		"    AND st.sales_trn_oid    = ct.sales_trn_oid " +
		"    AND pdr.prmtn_id        = ct.prmtn_id " +
		"    AND pdr.vendor_id       = vd.vendor_id " +
		"    AND pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+) " +
		"    AND pdr.prmtn_id        = ps.prmtn_id(+) " +
		"   and ct.trn_dt >= to_date('"+sf.format(fromDate)+"', 'DD-MM-YYYY') " + 
		"   and ct.trn_dt <= to_date('"+sf.format(toDate)+"', 'DD-MM-YYYY') " + 
		"    AND ct.csh_trn_typ_id  IN ('CNP', 'CNS') " +
		"    AND ct.tender_id        = 'COVN' " +
		"	AND ps.prmtn_id = prmtn.prmtn_id " +
		"	AND prmtn.is_active = 'Y'" +
		"    GROUP BY " +
		"      ps.prmtn_id, " +
		"      prmtn.prmtn_nm, " +
		"      pdr.vendor_id, " +
		"      vd.vendor_nm , " +
		"      ct.trn_dt, " +
		"      p.store_id, " +
		"      ps.trn_ref , " +
		"      'DG', " +
		"      'CN' " +
		"  ) " +
		"ORDER BY " +
		"  typ_id, " +
		"  vendor_id";
		
		JdbcTemplate jt = getJdbcTemplate();
		int ret = jt.update(sqlString);
		return ret;
	}
}
