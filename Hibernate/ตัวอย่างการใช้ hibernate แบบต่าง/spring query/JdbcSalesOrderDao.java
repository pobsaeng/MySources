package com.ie.icon.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.hp.www.common.util.ConnectionUtil;
import com.ie.icon.common.util.PropertyUtil;
import com.ie.icon.constant.Constant;
import com.ie.icon.dao.SalesOrderJdbcDao;
import com.ie.icon.dao.jdbc.JdbcCashierTransactionAdjustDao.CreditCaseONMapper;
import com.ie.icon.domain.Sequence;
import com.ie.icon.domain.authentication.User;
import com.ie.icon.domain.csh.form.OverUnderReport;
import com.ie.icon.domain.monitor.Ejournal;
import com.ie.icon.domain.sale.form.ZSOForm;
import com.ie.icon.domain.so.CollectSalesOrder;

public class JdbcSalesOrderDao extends JdbcDaoSupport implements SalesOrderJdbcDao{
	
	private SimpleDateFormat sf;
	public JdbcSalesOrderDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public boolean updateSalesOrder(Long saleOrderOid,String sapSoNo,String salesOrderStsId,Timestamp lastUpdateDate , String lastUpdateBy){
		
		String sqlString = "UPDATE SALES_ORDER SET SAP_SO_NO = ? , SALES_ORDER_STS_ID = ? , LAST_UPD_DTTM = ?, LAST_UPD_USER = ? WHERE SALES_ORDER_OID = ?";
		JdbcTemplate jt = getJdbcTemplate();
		Object[] args = new Object[] {sapSoNo ,salesOrderStsId,lastUpdateDate,lastUpdateBy,saleOrderOid};
		return jt.update(sqlString,args) > 0;

	}   
	
	public boolean updateSalesOrder(Long saleOrderOid,String sapSoNo,String salesOrderStsId,String salesOrderTypeId,Timestamp lastUpdateDate , String lastUpdateBy){
		
		String sqlString = "UPDATE SALES_ORDER SET SAP_SO_NO = ? , SALES_ORDER_STS_ID = ? , SALES_ORDER_TYP_ID = ? , LAST_UPD_DTTM = ?, LAST_UPD_USER = ? WHERE SALES_ORDER_OID = ?";
		JdbcTemplate jt = getJdbcTemplate();
		Object[] args = new Object[] {sapSoNo ,salesOrderStsId,salesOrderTypeId,lastUpdateDate,lastUpdateBy,saleOrderOid};
		return jt.update(sqlString,args) > 0;

	}
	
	public boolean updateSalesOrderItem(Long salesOrderItemOid,String sapSeqNo){
		String sqlString = "UPDATE SALES_ORDER_ITEM SET SAP_SEQ_NO = ? WHERE SALES_ORDER_ITEM_OID = ?";
		JdbcTemplate jt = getJdbcTemplate();
		Object[] args = new Object[] {sapSeqNo ,salesOrderItemOid};
		return jt.update(sqlString,args) > 0;
	}
	
	public CollectSalesOrder getMaxCollectSaleOrder(Date transactionDate){
		CollectSalesOrder result = new CollectSalesOrder();
		String sqlString ="SELECT a.coll_sales_order_oid , a.coll_sales_order_no"
//						 +" , a.store_id,a.trn_dt, a.create_dttm, a.create_user_id, a.create_user_nm,"
//						 +" a.num_items, a.sales_order_typ_id, a.sales_order_sts_id,"
//						 +" a.sold_to_cust_oid, a.bill_to_cust_oid, a.last_upd_dttm,"
//						 +" a.last_upd_user, a.sap_so_no,"
//						 +" a.sales_channel, a.contact_nm,"
//						 +" a.contact_tel, a.qt_no, a.version_id"
						 +" FROM coll_sales_order a"
						 +" where a.trn_dt = to_date('"+sf.format(transactionDate)+"','dd/MM/yyyy')"
						 +" order by a.coll_sales_order_no";
		
		JdbcTemplate jt = getJdbcTemplate();
		
		List resultList = jt.query(sqlString, new RowMapperResultReader(new CollectSalesOrderMapper()));  
		if(resultList != null && resultList.size() > 0){
			result = (CollectSalesOrder)resultList.get(0);
		}
		return result;
	}
	
	public void insertEjournal(Ejournal ejournal) throws SQLException{
		
		PreparedStatement pre = null;
		String sqlString = "INSERT INTO USRECM.EJOURNAL(EJOURNAL_OID,REF_OID,DOC_TYP_ID,BRANCH_ID,POS_ID,CREATE_DT,TICKET_NO,TRN_DT) "
			+ " VALUES(USRECM.ejournal_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?)";
  
		pre = getConnection().prepareStatement(sqlString);
		//pre.setString(1, ejournal.getReference());   
		pre.setLong(1, 1);
		pre.setString(2,ejournal.getDocTypeId());  
		pre.setString(3,ejournal.getBranchId());
		pre.setString(4, ejournal.getPosId());
		pre.setString(5, ejournal.getTicketNo());
		pre.setDate(6,new java.sql.Date(ejournal.getTrnDate().getTime()));

		int ret = pre.executeUpdate(); 
		
	}

	public List getZSalesOrderList(String storeId, String userId, Date date) {
		
		String sql = 
			"select st.store_id || SUBSTR(to_char(st.trn_dt,'DD/MM/YY'), 7, 2) || SUBSTR(to_char(st.trn_dt,'DD/MM/YY'), 4, 2) " +
			"		|| SUBSTR(to_char(st.trn_dt,'DD/MM/YY'), 1, 2) ||  st.pos_id || st.ticket_no as ticket, " +
			"		st.store_id || SUBSTR(to_char(st.ref_trn_dt,'DD/MM/YY'), 7, 2) || SUBSTR(to_char(st.ref_trn_dt,'DD/MM/YY'), 4, 2) " +
			"		|| SUBSTR(to_char(st.ref_trn_dt,'DD/MM/YY'), 1, 2) ||  st.ref_pos_id || st.ref_ticket_no as ref_ticket, " +
			"       sd.sales_doc_sts_id, " + 
			"       sdi.sales_order_no, " + 
			"       csold.first_nm || ' ' || csold.last_nm as soldto, " + 
			"       cship.first_nm || ' ' || cship.last_nm as shipto, " + 
			"       cship.number_addr || ' ' || cship.soi || ' ' || cship.street || ' ' || " + 
			"       cship.sub_district || ' ' || cship.district || ' ' || cship.province as address, " + 
			"		td.map_nm, " +
			"		td.plot_map_nm, " +
			"       sdg.delivery_qno, " + 
			"       sdg.jobtype, " + 
			"		TO_CHAR(SDG.DELIVERY_DT,'DD/MM/YYYY') AS delivery_dt , "+
			"       sdg.shipping_point_id, " +
			"		sdg.delivery_site, " + 
			"       cf.con_typ_desc, " + 
			"       sdg.conf_typ_desc, " + 
			"       sdi.seq_no, " + 
			"       a.block_code_id, " + 
			"       sti.mc9, " + 
			"       sti.artc_no, " + 
			"       sti.sales_trn_item_dsc, " + 
			"       sti.qty, " + 
			"       sti.ret_qty, " + 
			"       itd.dscnt_cond_typ_id, " + 
			"       sti.unit, " + 
			"       vc.description, " + 
			"       sdi.remark, " + 
			" 		cship.village, cship.number_addr, " +
			"		cship.floor_num,  cship.moo, cship.soi, cship.street, " +
			"		cship.sub_district, cship.district, cship.province, cship.zipcode, " +
			" 		ains.VENDOR_ID " +
			
			"  from sales_doc       sd, " + 
			"       sales_doc_item  sdi, " + 
			"       sales_doc_grp   sdg, " + 
			"       sales_trn       st, " + 
			"       sales_trn_item  sti, " + 
			"       item_dscnt      itd, " + 
			//"      -- block_code      bc, " + 
			"       artc            a, " + 
			"       valuation_class vc, " + 
			"       customer        csold, " + 
			"       customer        cship, " + 
			"		trans_data td, " +
			"       m_ds_conftype   cf, " + 
			//"       job_typ         jt " + 
			"  		ARTC_IN_STORE   ains " +
			
			// Pook add new sql 27/11/2013
			
			" WHERE sd.sales_doc_oid = sdg.sales_doc_oid " +
			" AND sdg.sales_doc_grp_oid = sdi.sales_doc_grp_oid " +
        	" AND sd.sales_doc_oid = sdi.sales_doc_oid " +
        	" AND sd.trn_dt = st.trn_dt    " +
        	" AND sd.store_id = st.store_id " +    
        	" AND sd.ticket_no = st.ticket_no " +
        	" AND sd.pos_id = st.pos_id " +
        	" AND st.sales_trn_oid = sti.sales_trn_oid " +       
        	" AND sti.sales_trn_item_oid = itd.sales_trn_item_oid(+) " +
        	" AND sdi.sale_seq_no = sti.seq_no " +
        	" AND sti.artc_no = a.artc_id " +
        	" AND a.valuation_class = vc.valuation_class(+) " +
        	" AND sd.sold_to_cust_oid = csold.cust_oid(+) " +
        	" AND sdg.ship_to_cust_oid = cship.cust_oid(+) " +
        	" AND cship.trans_data_oid = td.trans_data_oid(+) " +
        	" AND sdg.conf_typ_no = cf.conf_typ_no(+) " +
        	" AND sti.artc_no = ains.artc_id " +
        	" AND st.store_id = ains.store_id " +
        	" AND sd.trn_dt = to_date('"+sf.format(date)+"', 'DD/MM/YYYY') " +
        	" AND sd.store_id = '"+ storeId +"' ";
			if(userId!=null && userId.length()>0){
				sql += "	and sd.create_user_id = '"+ userId +"' ";
			}
			// Pook comment for new SQL 27/11/2013
		
			/*" where st.sales_trn_oid = sti.sales_trn_oid " + 
			"   and itd.sales_trn_item_oid(+) = sti.sales_trn_item_oid " + 
//			"   and st.sales_doc_no = sd.sales_doc_no " +
			"	and st.ticket_no = sd.ticket_no " + 
			"	and st.pos_id = sd.pos_id " + 
			"	and st.trn_dt = sd.trn_dt " + 
			"   and sd.sales_doc_oid = sdg.sales_doc_oid " + 
			"   and sdg.sales_doc_grp_oid = sdi.sales_doc_grp_oid " + 
			"   and sdi.sales_doc_oid = sd.sales_doc_oid " + 
			"   and sdi.sale_seq_no = sti.seq_no " + 
			//"   and a.block_code_id = bc.block_code_id(+) " + 
			"   and a.valuation_class = vc.valuation_class(+) " + 
			"   and a.artc_id = sti.artc_no " + 
			"   and csold.cust_oid(+) = sd.sold_to_cust_oid " + 
			"   and cship.cust_oid(+) = sdg.ship_to_cust_oid " + 
			"   and td.trans_data_oid(+) = cship.trans_data_oid" +
			"   and cf.conf_typ_no(+) = sdg.conf_typ_no " + 
			//"   and jt.job_typ_d(+) = sdg.jobtype " + 
			"	and sd.trn_dt = to_date('"+sf.format(date)+"', 'DD/MM/YYYY') " +
			" 	AND a.ARTC_ID=ains.ARTC_ID ";
			if(userId!=null && userId.length()>0){
				sql += "	and sd.create_user_id = '"+userId+"' ";
			} */
			sql += " order by ticket";
			
			
			System.out.println("sql getZSalesOrderList :"+sql);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(sql, new RowMapperResultReader(new ZSOMapper()));
		
	}
	

}
	class ZSOMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ZSOForm z = new ZSOForm();
			int i = 1;
			z.setTicketNo(rs.getString(i++));
			z.setRefTicketNo(rs.getString(i++));
			z.setSalesDocStatus(rs.getString(i++));
			z.setSalesOrderNo(rs.getString(i++));
			z.setSoldToName(rs.getString(i++));
			z.setShipToName(rs.getString(i++));
			z.setAddress(rs.getString(i++));
			z.setScanMap(rs.getString(i++)); 
			z.setPlotMap(rs.getString(i++));
			z.setQueueNo(rs.getString(i++));
			z.setJobtype(rs.getString(i++));
			z.setDeliveryDate(rs.getString(i++));
			z.setShippingPoint(rs.getString(i++));
			z.setDeliverySite(rs.getString(i++));
			z.setComfirm(rs.getString(i++));
			z.setTel(rs.getString(i++));
			z.setItem(rs.getString(i++));
			z.setInfoCode(rs.getString(i++));
			z.setMc(rs.getString(i++));
			z.setArticleNo(rs.getString(i++));
			z.setArticleDiscription(rs.getString(i++));
			z.setQty(rs.getString(i++));
			z.setReturnQty(rs.getString(i++));
			z.setDisconttype(rs.getString(i++));
			z.setUnit(rs.getString(i++));
			z.setValuationClass(rs.getString(i++));
			z.setNote(rs.getString(i++));
			String address = "";
			String village = rs.getString(i++);
			String numberAddress = rs.getString(i++);
			String floorNum = rs.getString(i++);
			String moo = rs.getString(i++);
			String soi = rs.getString(i++);
			String street = rs.getString(i++);
			String subDistrict = rs.getString(i++);
			String district = rs.getString(i++);
			String province = rs.getString(i++);
			if(village!=null && village.trim().length()>0){
				address+= PropertyUtil.getProperty(Constant.LabelPropFile, "village")+" "+village+" ";
			}
			if(numberAddress!=null && numberAddress.trim().length()>0){
				address+= PropertyUtil.getProperty(Constant.LabelPropFile, "numberaddress")+" "+numberAddress+" ";
			}
			if(floorNum!=null && floorNum.trim().length()>0){
				address+= PropertyUtil.getProperty(Constant.LabelPropFile, "floornum")+" "+floorNum+" ";
			}
			if(moo!=null && moo.trim().length()>0){
				address+= PropertyUtil.getProperty(Constant.LabelPropFile, "moo")+" "+moo+" ";
			}
			if(soi!=null && soi.trim().length()>0){
				address+= PropertyUtil.getProperty(Constant.LabelPropFile, "soi")+" "+soi+" ";
			}
			if(street!=null && street.trim().length()>0){
				address+= PropertyUtil.getProperty(Constant.LabelPropFile, "street")+" "+street+" ";
			}
			if(subDistrict!=null && subDistrict.trim().length()>0){
				address+= subDistrict+" ";
			}
			if(district!=null && district.trim().length()>0){
				address+= district+" ";
			}
			if(province!=null && province.trim().length()>0){
				address+= province+" ";
				address += rs.getString(i++);
			}
			
			z.setAddress(address);
			z.setVendorNo(rs.getString(i++));
			return z;
		}
	}
	class CollectSalesOrderMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CollectSalesOrder collectSalesOrderln = new CollectSalesOrder();
			collectSalesOrderln.setObjectId(rs.getLong(1));
			collectSalesOrderln.setCollectSalesOrderNo(rs.getString(2));
//			Store store = new Store();
//			store.setStoreId(rs.getString(3));
//			collectSalesOrderln.setStore(store);
//			collectSalesOrderln.setTransactionDate(rs.getDate(4));
//			collectSalesOrderln.setCreateDateTime(rs.getDate(5));
//			collectSalesOrderln.setCreateUserId(rs.getString(6));
//			collectSalesOrderln.setCreateUserName(rs.getString(7));
//			collectSalesOrderln.setNumOfItems(rs.getInt(8));
//			SalesOrderType salesOrderType = new SalesOrderType();
//			salesOrderType.setSalesOrderTypeId(rs.getString(9));
//			collectSalesOrderln.setSalesOrderType(salesOrderType);
//			SalesOrderStatus salesOrderStatus = new SalesOrderStatus();
//			salesOrderStatus.setSalesOrderStatusId(rs.getString(10));
//			collectSalesOrderln.setSalesOrderStatus(salesOrderStatus);
//			Customer soidToCustomer = new Customer();
//			soidToCustomer.setObjectId(rs.getLong(11));
//			collectSalesOrderln.setSoldToCustomer(soidToCustomer);
//			Customer billToCustomer = new Customer();
//			billToCustomer.setObjectId(rs.getLong(12));
//			collectSalesOrderln.setBillToCustomer(billToCustomer);
//			collectSalesOrderln.setLastUpdateDate(rs.getDate(13));
//			collectSalesOrderln.setLastUpdateUser(rs.getString(14));
//			collectSalesOrderln.setSapSoNo(rs.getString(15));
//			collectSalesOrderln.setSalesChannel(rs.getString(16));
//			collectSalesOrderln.setContactName(rs.getString(17));
//			collectSalesOrderln.setContactPhone(rs.getString(18));
//			collectSalesOrderln.setQuotationId(rs.getString(19));
//			collectSalesOrderln.setVersionId(rs.getString(20));
			return collectSalesOrderln;
		}
	}
	
	class SequenceMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			Sequence sequenceln = new Sequence();
			sequenceln.setSequenceId(rs.getLong(1));
			sequenceln.setSequenceName(rs.getString(2));
			sequenceln.setIncrement(rs.getInt(3));
			sequenceln.setMinValue(rs.getInt(4));
			sequenceln.setMaxValue(rs.getInt(5));
			sequenceln.setStart(rs.getInt(6));
			sequenceln.setCache(rs.getInt(7));
			sequenceln.setVersion(rs.getInt(8));
			return sequenceln;
		}
	}

