package com.ie.icon.dao.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.constant.Constant;
import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.RefundJDBCDao;
import com.ie.icon.domain.Tender;
import com.ie.icon.domain.report.RefundReport;
import com.ie.icon.domain.sale.InvoiceTender;
import com.ie.icon.domain.sale.Refund;
import com.ie.icon.domain.sale.SalesTransactionItem;
import com.ie.icon.domain.sale.TaxInvoice;
import com.ie.icon.domain.sale.TaxInvoiceItem;
import com.ie.icon.domain.so.RejectReason;

public class JdbcRefundDao extends JdbcDaoSupport implements RefundJDBCDao {
	private SimpleDateFormat sf;	
	
	public JdbcRefundDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	protected String replace(String param) {
	    if ( param == null )
	        return param;
	    
	    return param.replace('*', '%');
	} 
	public List getRefund_JDBC(Map map) throws Exception {
			
			List list = null;
			String sql = null;
			try{
			sql = " select a.refund_oid ,a.store_id ,a.refund_inv_id ,a.refund_typ_id ,a.issue_dt ,a.trn_dt ,a.cust_oid ,a.cust_nm " +
					",a.cust_addr ,a.tot_trn_amt ,a.tot_dscnt_amt ,a.tot_vat_trn_amt ,a.net_trn_amt ,a.vat_trn_amt ,a.prnt_count ,a.sales_trn_typ " +
					",a.pos_id ,a.ticket_no ,a.register_no ,a.remark ,a.create_user_id ,a.create_user_nm ,a.create_dttm " +
					",a.net_ret_trn_amt ,a.return_status ,t.tax_inv_id,t.issue_dt,a.return_qno from refund a ,tax_inv t where " 
					+" a.refund_inv_id = t.refund_inv_id(+)"
					+" and a.issue_dt = t.refund_dt(+)";
			
		     if(map.get("startDate") != null){
			      sql+=" and a.issue_dt = to_date('"+sf.format(map.get("startDate"))+"','DD-MM-YY') " ;
			    }
		     if(map.get("storeId") != null){
			      sql+=" and a.store_id ='"+map.get("storeId")+"'";
			   }
		     if(map.get("salesTransactionType") != null && !map.get("salesTransactionType").toString().trim().equals("")){
			      sql+=" and a.sales_trn_typ ='"+map.get("salesTransactionType")+"'";
			   }
		     sql+=" order by a.issue_dt ,a.refund_inv_id asc";	 
		     
		     System.out.println("sql:"+sql);
		     
 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Refund_Mapper()));
 			if(list == null) list = new ArrayList();
			}catch(Exception e){
				e.printStackTrace();
			}
  			return list;
		} 
		
		class Refund_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				Refund obj = new Refund();
				int columnIndex = 1;		
				obj.setObjectId(rs.getLong(columnIndex++));
				obj.setStore_str(rs.getString(columnIndex++));				
				obj.setRefundId(rs.getString(columnIndex++));
				
				if(rs.getString(columnIndex)!=null)
				obj.setTypeId(rs.getString(columnIndex).charAt(0));
				columnIndex++;
				
				
				obj.setIssueDate(rs.getDate(columnIndex++));
				obj.setTransactionDate(rs.getDate(columnIndex++));
				obj.setCust_oid(rs.getLong(columnIndex++));
				obj.setCustomerName(rs.getString(columnIndex++));
				obj.setCustomerAddress(rs.getString(columnIndex++));
				obj.setTotalTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setTotalDiscountAmount(rs.getBigDecimal(columnIndex++));
				obj.setTotalVatTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setNetTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setVatTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setPrintCount(rs.getInt(columnIndex++));
				
				if(rs.getString(columnIndex)!=null)
				obj.setSalesTransactionType(rs.getString(columnIndex).charAt(0));
				columnIndex++;
				
				obj.setPosId(rs.getString(columnIndex++));
				obj.setTicketNo(rs.getString(columnIndex++));
				obj.setPosRegisterNo(rs.getString(columnIndex++));
				obj.setRemark(rs.getString(columnIndex++));
				obj.setCreateUserId(rs.getString(columnIndex++));
				obj.setCreateUserName(rs.getString(columnIndex++));
				obj.setCreateDateTime(rs.getDate(columnIndex++));
				obj.setNetReturnedTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setReturnStatus(rs.getString(columnIndex++).charAt(0));
				obj.setTaxInvoiceId(rs.getString(columnIndex++));
				obj.setTaxInvoiceIssueDate(rs.getDate(columnIndex++));
				obj.setReturnQno(rs.getString(columnIndex++));
 				return obj;
 			}
 		}
		public List getRefund_NoCN_JDBC(Map map) throws Exception {
			List list = null;
			String sql = null;
			try{
			sql = " select a.refund_oid ,a.store_id ,a.refund_inv_id ,a.refund_typ_id ,a.issue_dt ,a.trn_dt ,a.cust_oid ,a.cust_nm " +
					",a.cust_addr ,a.tot_trn_amt ,a.tot_dscnt_amt ,a.tot_vat_trn_amt ,a.net_trn_amt ,a.vat_trn_amt ,a.prnt_count ,a.sales_trn_typ " +
					",a.pos_id ,a.ticket_no ,a.register_no ,a.remark ,a.create_user_id ,a.create_user_nm ,a.create_dttm " +
					",a.net_ret_trn_amt ,a.return_status  , a.return_qno from refund a  where " ;
			
		     if(map.get("startDate") != null){
			      sql+=" a.issue_dt >= to_date('"+map.get("startDate")+"','MM-YYYY') " ;
			      sql+=" and a.issue_dt <= last_day(to_date('"+map.get("startDate")+"','MM-YYYY')) " ;
			    }
		     if(map.get("return_status_not_R_C") != null){
			      sql+=" and return_status != '"+Constant.RefundStatus.CREDIT_NOTE+"' and return_status != '"+Constant.RefundStatus.CANCEL+"' ";
			   }
		     if(map.get("storeId") != null){
			      sql+=" and a.store_id ='"+map.get("storeId")+"'";
			   }	     
		     sql+=" order by a.issue_dt ,a.refund_inv_id asc";	  
		     
		     System.out.println("sql:"+sql);
 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Refund_NoCN_Mapper()));
 			if(list == null) list = new ArrayList();
			}catch(Exception e){
				e.printStackTrace();
			}
  			return list;
		} 
		
		class Refund_NoCN_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				Refund obj = new Refund();
				int columnIndex = 1;		
				obj.setObjectId(rs.getLong(columnIndex++));
				obj.setStore_str(rs.getString(columnIndex++));				
				obj.setRefundId(rs.getString(columnIndex++));
				
				if(rs.getString(columnIndex)!=null)
				obj.setTypeId(rs.getString(columnIndex).charAt(0));
				columnIndex++;
				
				
				obj.setIssueDate(rs.getDate(columnIndex++));
				obj.setTransactionDate(rs.getDate(columnIndex++));
				obj.setCust_oid(rs.getLong(columnIndex++));
				obj.setCustomerName(rs.getString(columnIndex++));
				obj.setCustomerAddress(rs.getString(columnIndex++));
				obj.setTotalTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setTotalDiscountAmount(rs.getBigDecimal(columnIndex++));
				obj.setTotalVatTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setNetTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setVatTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setPrintCount(rs.getInt(columnIndex++));
				
				if(rs.getString(columnIndex)!=null)
				obj.setSalesTransactionType(rs.getString(columnIndex).charAt(0));
				columnIndex++;
				
				obj.setPosId(rs.getString(columnIndex++));
				obj.setTicketNo(rs.getString(columnIndex++));
				obj.setPosRegisterNo(rs.getString(columnIndex++));
				obj.setRemark(rs.getString(columnIndex++));
				obj.setCreateUserId(rs.getString(columnIndex++));
				obj.setCreateUserName(rs.getString(columnIndex++));
				obj.setCreateDateTime(rs.getDate(columnIndex++));
				obj.setNetReturnedTransactionAmount(rs.getBigDecimal(columnIndex++));
				obj.setReturnStatus(rs.getString(columnIndex++).charAt(0));
				obj.setReturnQno(rs.getString(columnIndex++));
 				return obj;
 			}
 		}
		
		public List getCn_detail_JDBC(Map map) throws Exception {
 			
			List list = null;
			String sql = null;
			try{
			sql = " select   r.issue_dt,r.refund_inv_id,t.tax_inv_id ,r.return_status ,r.sales_trn_typ"
			+" ,r.sales_store_id, r.store_id ,si.artc_no ,si.mc9 ,ri.refund_inv_item_dsc"
			+" ,si.qty ,ri.qty ,ri.rej_reason_id ,rr.rej_reason_dsc ,r.refund_typ_id ,r.is_return_receive "
			+"  from  refund r, tax_inv t ,refund_item ri ,sales_trn s,sales_trn_item si , rej_reason rr"
			+" where "
			+"   r.refund_oid = ri.refund_oid "
			 
			+"  and r.trn_dt =  s.trn_dt "
			+"  and r.ticket_no = s.ticket_no"
			+"  and r.pos_id = s.pos_id"
			+"  and r.sales_trn_typ  =  s.typ_id"
			+"  and r.store_id = s.store_id"
			+"  "
			+"  and s.sales_trn_oid = si.sales_trn_oid "
			+"  and si.seq_no = ri.ref_seq_no "
			+"  "
			+"  and r.refund_inv_id  = t.refund_inv_id(+)"
			+"  and r.issue_dt = t.refund_dt(+)  "
 
			+"  and ri.rej_reason_id = rr.rej_reason_id(+)"
			 
			+"  and r.issue_dt >= to_date('"+sf.format(map.get("issueDate"))+"','DD-MM-YY')"
			+"  and r.issue_dt <= to_date('"+sf.format(map.get("issueDate2"))+"','DD-MM-YY')";
			
			if(!map.get("storeId").equals(SystemConfigConstant.CENTER_CODE)){
				sql+= " and r.store_id ='"+map.get("storeId")+"'";
			}
			if(map.get("typeId")!= null && !map.get("typeId").toString().equals("X")){
				sql+= " and t.bill_typ ='"+map.get("typeId")+"'";
			}
			if(map.get("returnStatus")!= null && !map.get("returnStatus").toString().equals("X")){
				if(map.get("returnStatus")!= null && !map.get("returnStatus").toString().equals("N")){
					sql+= " and r.return_status ='"+map.get("returnStatus")+"'";
				}else{
					sql+= " and r.return_status <> 'R' and r.return_status <> 'C'";
				}
			}
			if(map.get("salesTransactionType")!= null && !map.get("salesTransactionType").toString().equals("X")){
				
					sql+= " and r.sales_trn_typ ='"+map.get("salesTransactionType")+"'";
				
			}	
					    
		     sql+=" order by r.issue_dt ,r.refund_inv_id ";	  		
		     
		     System.out.println("sql : "+sql);
 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Refund_CN_Mapper()));
 			if(list == null) list = new ArrayList();
			}catch(Exception e){
			e.printStackTrace();
			}
  			return list;
		} 
		class Refund_CN_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				int columnIndex = 1;	
				TaxInvoice t = new TaxInvoice();
				TaxInvoiceItem ti = new TaxInvoiceItem();
				SalesTransactionItem si = new SalesTransactionItem();
				RejectReason rj = new RejectReason();
				t.setIssueDate(rs.getDate(columnIndex++));
				t.setRefundId(rs.getString(columnIndex++));
				t.setTaxInvoiceId(rs.getString(columnIndex++));
				
				if(rs.getString(columnIndex)  != null)
				t.setReturnStatus(rs.getString(columnIndex).charAt(0));				
				columnIndex++;
				
				if(rs.getString(columnIndex)  != null)
					t.setSalesTransactionType(rs.getString(columnIndex).charAt(0));
				columnIndex++;
				
			    t.setSales_store_id(rs.getString(columnIndex++));
			    t.setStore_id(rs.getString(columnIndex++));
			    si.setArticleNo(rs.getString(columnIndex++));
			    si.setMc9(rs.getString(columnIndex++));
			    ti.setDescription(rs.getString(columnIndex++));
			    si.setQuantity(rs.getBigDecimal(columnIndex++));
			    
			    ti.setQuantity(rs.getBigDecimal(columnIndex++));
			    rj.setRejectReasonId(rs.getString(columnIndex++));
			    rj.setDescription(rs.getString(columnIndex++));
			    
			    
				if(rs.getString(columnIndex)  != null)
					t.setTypeId(rs.getString(columnIndex).charAt(0));
				columnIndex++;	
				//pook add type 19/02/2014
				//System.out.println("Type:"+rs.getString(columnIndex++));
				if(rs.getString(columnIndex++).equals("Y")){
					si.setIsReturnReceive(true);
				} else {
					si.setIsReturnReceive(false);
				}
			    
			    ti.setTaxInvoice(t);
			    ti.setSalesTransactionItem(si);
			    ti.setRejectReason(rj);			    
  				return ti;
 			}
 		}
		
		public List getCn_summary_JDBC(Map map) throws Exception {
			
			List list = null;
			String sql = null;
			try{sql = "select t1.issue_dt,t1.refund_inv_id,t1.tax_inv_id ,t1.return_status ,t1.sales_store_id, t1.store_id " +
					" ,t1.cust_oid ,t1.cust_nm  ,t1.net_trn_amt ,t1.tender_id ,t1.tender_nm ,t1.tender_amt ,t1.refund_typ_id " +
					" ,case when (t1.so >0 and t1.pos >0) then 'MIXED SALES'"+
					" when  (t1.so >0 )then 'SO'"+
					" when  (t1.pos >0) then 'POS' else '-' end  as sale_type " +
					" ,t1.create_user_nm ,t1.trn_dt ,t1.pos_id ,t1.ticket_no "+
					" from ("
			+"   select  r.issue_dt,r.refund_inv_id,t.tax_inv_id ,r.return_status ,r.sales_trn_typ ,r.sales_store_id, r.store_id "
			+"   ,r.cust_oid ,r.cust_nm  ,r.net_trn_amt ,rt.tender_id ,ten.tender_nm ,rt.tender_amt ,r.refund_typ_id  "+
			 "  ,r.create_user_nm ,r.trn_dt ,r.pos_id ,r.ticket_no"
			+"   ,"
			+"   ( select count(si.is_so_item)   from  sales_trn s,sales_trn_item si where  "
			+"      s.trn_dt =  r.trn_dt  "
			+"    and s.ticket_no = r.ticket_no  "
			+"    and s.pos_id = r.pos_id  "
			+"    and s.typ_id  = r.sales_trn_typ  "
			+"    and s.store_id = r.store_id    "
			+"    and s.sales_trn_oid = si.sales_trn_oid   "
			+"    and si.is_so_item = 'Y'   "
			+"   )  as so"
			+"  ,( select count(si.is_so_item)   from  sales_trn s,sales_trn_item si where  "
			+"      s.trn_dt =  r.trn_dt  "
			+"    and s.ticket_no = r.ticket_no  "
			+"    and s.pos_id = r.pos_id  "
			+"    and s.typ_id  = r.sales_trn_typ  "
			+"    and s.store_id = r.store_id    "
			+"    and s.sales_trn_oid = si.sales_trn_oid   "
			+"    and si.is_so_item = 'N'   "
			+"   )  as pos"

			+"   from  refund r, tax_inv t , refund_tender rt ,tender ten "
			+"   where    "
			+"      r.refund_oid = rt.refund_oid"
			+"     and rt.tender_id = ten.tender_id(+)"
			+"     and r.refund_inv_id  = t.refund_inv_id(+)  "
			+"     and r.issue_dt = t.refund_dt(+)     "
			+"  and r.issue_dt >= to_date('"+sf.format(map.get("issueDate"))+"','DD-MM-YY')"
			+"  and r.issue_dt <= to_date('"+sf.format(map.get("issueDate2"))+"','DD-MM-YY')";
 			
			if(!map.get("storeId").equals(SystemConfigConstant.CENTER_CODE)){
				sql+= " and r.store_id ='"+map.get("storeId")+"'";
			}
			if(map.get("tender_id")!= null && !map.get("tender_id").toString().equals("")){
				sql+= " and rt.tender_id ='"+map.get("tender_id")+"'";
			} 			
			if(map.get("create_user_id")!= null && !map.get("create_user_id").toString().equals("")){
				sql+= " and r.create_user_id ='"+map.get("create_user_id")+"'";
			} 	
			
			sql+=" order by r.issue_dt ,r.refund_inv_id ";
 			sql+=" ) t1";
 			
 			if(map.get("salesTransactionType")!= null && map.get("salesTransactionType").toString().equals("S")){
				sql+= " where t1.so > 0 and  t1.pos = 0 ";
			}	 			
 			if(map.get("salesTransactionType")!= null && map.get("salesTransactionType").toString().equals("P")){
				sql+= " where t1.pos > 0 and t1.so = 0";
			}	 			
 			if(map.get("salesTransactionType")!= null && map.get("salesTransactionType").toString().equals("S")){
				sql+= " where t1.so > 0 and  t1.pos > 0 ";
			}	 			  			
		      		 
 			System.out.println("sql:"+sql);
 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Refund_CN_Summary_Mapper()));
 			if(list == null) list = new ArrayList();
			}catch(Exception e){
				e.printStackTrace();
			}
  			return list;
		} 
		class Refund_CN_Summary_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				int columnIndex = 1;	
				TaxInvoice t = new TaxInvoice();				
				InvoiceTender vt = new InvoiceTender();
				Tender td = new Tender();
				t.setIssueDate(rs.getDate(columnIndex++));
				t.setRefundId(rs.getString(columnIndex++));
				t.setTaxInvoiceId(rs.getString(columnIndex++));
				
				if(rs.getString(columnIndex)  != null)
				t.setReturnStatus(rs.getString(columnIndex).charAt(0));				
				columnIndex++;
				
			    t.setSales_store_id(rs.getString(columnIndex++));
			    t.setStore_id(rs.getString(columnIndex++));					 		  
				
				t.setCust_oid(rs.getLong(columnIndex++));
				t.setCustomerName(rs.getString(columnIndex++));
				t.setNetTransactionAmount(rs.getBigDecimal(columnIndex++));
				td.setTenderId(rs.getString(columnIndex++));
				td.setName(rs.getString(columnIndex++));
				
				vt.setTenderAmount(rs.getBigDecimal(columnIndex++));
				
				if(rs.getString(columnIndex)  != null)
					t.setTypeId(rs.getString(columnIndex).charAt(0));				
				columnIndex++;			    

				t.setSalesTransactionType_str(rs.getString(columnIndex++));
				t.setCreateUserName(rs.getString(columnIndex++));
				t.setTransactionDate(rs.getDate(columnIndex++));
				t.setPosId(rs.getString(columnIndex++));
				t.setTicketNo(rs.getString(columnIndex++));				
				 
				vt.setTender(td);
			    vt.setTaxInvoice(t);		  
			 		    
  				return vt;
 			}
 		}
		public boolean updateRefund(String refundNo , String reason , String store , String pos_id , 
				String ticket , Date transactionDate , BigDecimal amount){
			
			try{
				String sql = "UPDATE REFUND SET IS_USE_REFUND = 'Y' , REASON_USE_REFUND_ID = '"+reason+"' , USE_REFUND_STORE = '"+store+"'" +
				", USE_REFUND_POS_ID = '"+pos_id+"' , USE_REFUND_TICKET_NO = '"+ticket+"' " +
				", USE_REFUND_TRN_DT = to_date('"+sf.format(transactionDate)+"','DD/MM/YYYY') , USE_REFUND_AMT = '"+amount+"'"+
				" WHERE REFUND_INV_ID = '"+refundNo+"' ";
				
				JdbcTemplate jt = getJdbcTemplate();
				return jt.update(sql) > 0;
						
			}catch(Exception e){
			return false;
			}
			
		}	
		
		public List getRefundListByStoreTenderSalesTypeUser(Date fromDate,
				Date toDate, String storeId, String tenderId, char saleType,
				String userId) {
			String sql = "SELECT   r.issue_dt, r.refund_inv_id, t.tax_inv_id, r.return_status, " +
			"         r.sales_trn_typ,( r.create_user_id ||'-'||r.create_user_nm ) as create_user, " + 
			"         r.sales_store_id,r.trn_dt , r.pos_id , r.ticket_no ,r.store_id, " + 
			"         r.cust_nm  , t.net_trn_amt , " + 
			// POR Begin Add call WS DS for cancel Q
			//"         rt.tender_id , rt.tender_amt " +
			"         rt.tender_id , rt.tender_amt, r.IS_QUEUE_CANCEL, " +
			"         ( t.create_user_id ||'-'||t.create_user_nm ) as create_user_cn, " + 
			"          S.RWD_CARD_NO " + 
			// POR End Add call WS DS for cancel Q
			"    FROM refund r, " + 
			"         tax_inv t, " + 
			"         refund_tender rt " + 
//			 POR Begin Add call WS DS for cancel Q
			"         , SALES_TRN s "+
//			 POR End Add call WS DS for cancel Q
			"   WHERE r.refund_oid = rt.refund_oid " + 
			"     AND r.refund_inv_id = t.refund_inv_id(+) " + 
			"     AND r.issue_dt = t.refund_dt(+) " + 
			"     AND r.issue_dt >= TO_DATE ('"+sf.format(fromDate)+"', 'DD-MM-YY') " + 
			"     AND r.issue_dt <= TO_DATE ('"+sf.format(toDate)+"', 'DD-MM-YY') ";
			if(storeId!=null && !storeId.equals("") && !storeId.equals(SystemConfigConstant.CENTER_CODE)){
				sql += "     AND r.store_id = '"+storeId+"' ";
			}
			if(saleType!='X'){
				sql += "     AND r.sales_trn_typ = '"+saleType+"' ";
			}
			if(tenderId!=null && !tenderId.equals("") && tenderId.equals("All")){
				sql += "     AND rt.tender_id = '"+tenderId+"' ";
			}
			if(userId!=null && !userId.equals("")){
				sql += "     AND r.create_user_id = '"+userId+"' ";
			}
//			 POR Begin Add call WS DS for cancel Q
			sql += " and R.TICKET_NO = S.TICKET_NO(+) and R.POS_ID = S.POS_ID(+) and R.TRN_DT = S.TRN_DT(+) and  r.store_id = S.STORE_ID(+) "+
			"ORDER BY r.issue_dt, r.refund_inv_id";
			
		//	sql += "ORDER BY r.issue_dt, r.refund_inv_id";
//			 POR End Add call WS DS for cancel Q
		
			
			System.out.println("sql:"+sql);
 			List result = getJdbcTemplate().query(sql,new RowMapperResultReader(new Refund_Summary_Mapper()));
			return result;
		}
		
		class Refund_Summary_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				RefundReport rr = new RefundReport();
				int i = 1;	
				rr.setIssueDate(rs.getDate(i++));
				rr.setRefunId(rs.getString(i++));
				rr.setCreditNoteId(rs.getString(i++));
				rr.setReturnStatus(rs.getString(i++));
				rr.setSalesType(rs.getString(i++));
				rr.setUser(rs.getString(i++));
				rr.setSalesStoreId(rs.getString(i++));
				rr.setSaleDate(rs.getDate(i++));
				rr.setPos(rs.getString(i++));
				rr.setTicketNo(rs.getString(i++));
				rr.setStoreId(rs.getString(i++));
				rr.setCustName(rs.getString(i++));
				rr.setNetTrnAmt(rs.getDouble(i++));
				rr.setTenderId(rs.getString(i++));
				rr.setTenderAmt(rs.getDouble(i++));
				// POR Begin Add call WS DS for cancel Q
				if(rs.getString(i++).equals("Y")){
					rr.setIsQueueCancel(true);
				}else{
					rr.setIsQueueCancel(false);
				}
				rr.setUserCn(rs.getString(i++));
				rr.setHomeCard(rs.getString(i++));
				
				// POR End Add call WS DS for cancel Q
				return rr;
				
 			}
		}
  }