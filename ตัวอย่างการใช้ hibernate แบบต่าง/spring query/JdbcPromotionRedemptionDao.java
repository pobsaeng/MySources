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
import com.ie.icon.dao.PromotionRedemptionJDBCDao;
import com.ie.icon.domain.Store;
import com.ie.icon.domain.promotion.PromotionPremiumCACS;
import com.ie.icon.domain.promotion.PromotionPremiumDetail;
import com.ie.icon.domain.promotion.PromotionPremiumRedemption;
import com.ie.icon.domain.promotion.PromotionPremiumSummary;
import com.ie.icon.domain.promotion.PromotionRedemption;
import com.ie.icon.domain.promotion.PromotionSales;
import com.ie.icon.domain.promotion.PromotionSalesByCustomer;
import com.ie.icon.domain.promotion.VendorCupon;
import com.ie.icon.domain.promotion.form.PromotionMCSummaryForm;
import com.ie.icon.domain.promotion.form.PromotionPremiumDetailForm;
import com.ie.icon.domain.promotion.form.RdmtSummaryPercentForm;
import com.ie.icon.domain.promotion.form.SlipSummaryForm;
import com.ie.icon.domain.sale.SalesTransaction;

public class JdbcPromotionRedemptionDao extends JdbcDaoSupport implements PromotionRedemptionJDBCDao {
	private SimpleDateFormat sf;	
	
	public JdbcPromotionRedemptionDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	protected String replace(String param) {
	    if ( param == null )
	        return param;
	    
	    return param.replace('*', '%');
	}
  
	
	
	public List getPromotionPremiumRedemptions(String storeId, Date fromDate, Date toDate, String redemptionStatus,String cbbPromotionType,String promotionId)  {
		/*Pook edit code from home pro to Mega for bug duplicate 
		 * record For IR 208394 :03/01/2014
		 */

		StringBuffer queryString  = new StringBuffer();
		queryString.append("select * from (" );
		
		 if( cbbPromotionType !=null &&  !cbbPromotionType.equals(Constant.PromotionType.TENDER_PROMOTION)){ 
		 
			 queryString.append(" SELECT DISTINCT ");
			 queryString.append(" p.prmtn_rdptn_oid,  ");
			 queryString.append(" p.rdptn_dt, ");
			 queryString.append(" p.store_id, ");
			 queryString.append(" S.RWD_CARD_NO, ");
			 queryString.append("   ");
			 
			 queryString.append("  decode(ps.trn_ref,null,decode( ");
			 queryString.append(" (select ss.trn_ref from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1),null,s.pos_id||' - '||s.ticket_no, ");
			 queryString.append(" (select ss.trn_ref from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1) ");
			 queryString.append(" ),ps.trn_ref) AS trn_ref, ");
			 
//			 queryString.append(" CASE WHEN ps.trn_ref IS NOT NULL THEN ps.trn_ref  ");
//			 queryString.append("     ELSE (select ss.trn_ref from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 ) ");
//			 queryString.append(" END AS trn_ref, ");
			 
			 queryString.append("   ");
			 queryString.append(" NVL (pp.recv_from, 'CA') AS recv_from, ");
			 queryString.append(" p.prmtn_rdptn_id, ");
			 queryString.append(" p.cust_first_nm, ");
			 queryString.append(" p.cust_last_nm, ");
			 queryString.append(" p.cust_contact, ");
			 queryString.append(" CASE ");
			 queryString.append("    WHEN (pp.eligible_qty - changed_qty - cancelled_qty) = 0 ");
			 queryString.append("    THEN ");
			 queryString.append("       'Cancel' ");
			 queryString.append("    WHEN (  pp.eligible_qty ");
			 queryString.append("          - redeemed_qty ");
			 queryString.append("          - changed_qty ");
			 queryString.append("          - cancelled_qty) = 0 ");
			 queryString.append("    THEN ");
			 queryString.append("       'Complete' ");
			 queryString.append("    ELSE ");
			 queryString.append("       'Remain' ");
			 queryString.append(" END ");
			 queryString.append("    AS status, ");
			 queryString.append(" pp.prmtn_premium_rdptn_oid, ");
			 queryString.append(" pp.prmtn_id, ");
			 queryString.append(" pp.prmtn_nm, ");
			 queryString.append(" CASE ");
			 queryString.append("    WHEN LENGTH (pp.prmtn_artc_id) = 18 THEN pp.prmtn_artc_id ");
			 queryString.append("    ELSE NULL ");
			 queryString.append(" END ");
			 queryString.append("    AS prmtn_artc_id, ");
			 queryString.append(" pp.prmtn_artc_dsc, ");
			 queryString.append("     ");
			 
			 queryString.append(" decode(ps.trn_ref,null,decode( ");
			 queryString.append(" (select ss.net_trn_amt from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 ),null,S.NET_TRN_AMT, ");
			 queryString.append(" (select ss.net_trn_amt from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 ) ");
			 queryString.append(" ),ps.net_trn_amt)  AS net_amt, ");
			 
//			 queryString.append(" CASE WHEN ps.trn_ref IS NOT NULL THEN ps.net_trn_amt ");
//			 queryString.append("    ELSE (select ss.net_trn_amt from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 ) ");
//			 queryString.append(" END AS net_amt,    ");
			 
			 queryString.append("     ");
			 queryString.append(" ps.net_amt AS cal_net_amt, ");
			 queryString.append(" pp.eligible_qty - changed_qty - cancelled_qty ");
			 queryString.append("    AS receive_quantity, ");
			 queryString.append(" pp.redeemed_qty, ");
			 queryString.append(" pp.eligible_qty - redeemed_qty - changed_qty - cancelled_qty ");
			 queryString.append("    AS remain_quantity, ");
			 queryString.append(" pp.next_rdptn_dt, ");
			 queryString.append(" pp.remark, ");
			 queryString.append(" CASE ");
			 queryString.append("    WHEN history.auth_id IS NOT NULL ");
			 queryString.append("    THEN ");
			 queryString.append("       history.auth_id || ' - ' || history.auth_nm ");
			 queryString.append("    ELSE ");
			 queryString.append("       NULL ");
			 queryString.append(" END ");
			 queryString.append("    AS approvedBy ");
			 queryString.append("           FROM  prmtn_rdptn p, ");
			 queryString.append(" prmtn_premium_rdptn pp, ");
			 queryString.append(" prmtn_sales ps, ");
			 queryString.append(" sales_trn s, ");
			 queryString.append(" item_rdptn_his history ");
			 queryString.append("          WHERE     p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid ");
			 queryString.append(" AND pp.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+) ");
			 queryString.append(" AND pp.prmtn_id = ps.prmtn_id(+) ");
			 queryString.append(" AND pp.prmtn_premium_rdptn_oid = history.prmtn_premium_rdptn_oid(+) ");
			 queryString.append(" AND p.rdptn_dt >= TO_DATE ('"+sf.format(fromDate)+"', 'DD-MM-YYYY') ");
			 queryString.append(" AND p.rdptn_dt <= TO_DATE ('"+sf.format(toDate)+"', 'DD-MM-YYYY') ");
			 queryString.append(" AND (S.PRMTN_RDPTN_OID = P.PRMTN_RDPTN_OID OR S.POS_PRMTN_RDPTN_OID = P.PRMTN_RDPTN_OID) ");
			 queryString.append(" AND p.rdptn_typ IN ('P', 'A') ");
			 queryString.append(" AND p.prmtn_rdptn_oid = s.prmtn_rdptn_oid(+) ");
			 queryString.append(" AND s.trn_dt(+) >= TO_DATE ('"+sf.format(fromDate)+"', 'DD-MM-YYYY') ");
			 queryString.append(" AND s.trn_dt(+) <= TO_DATE ('"+sf.format(toDate)+"', 'DD-MM-YYYY') ");
			 queryString.append(" AND pp.prmtn_id NOT LIKE 'TD%' ");
			 queryString.append(" AND ps.prmtn_id(+) NOT LIKE 'TD%' ");
		
		
		 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals("S999")){
//			 queryString.append(" and p.store_id = '"+ storeId+"' ");
			 
			 String[] stArr = storeId.split(",");
			 queryString.append(" and p.store_id in (");
			 for(int i=0;i<stArr.length;i++){
				 if(i>0){
					 queryString.append(",");
				 }
				 queryString.append("'"+stArr[i]+"'");
			 }
			 queryString.append(")");
		 }	 
			    
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.CORPORATE_PROMOTION)){
			 queryString.append(" and pp.prmtn_id like '"+Constant.PromotionType.CORPORATE_PROMOTION+"%' ");
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.CATEGORY_PROMOTION)){
			 queryString.append(" and pp.prmtn_id like '"+Constant.PromotionType.CATEGORY_PROMOTION+"%' ");
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.ARTICLE_PROMOTION)){
			 queryString.append(" and pp.prmtn_id like '"+Constant.PromotionType.ARTICLE_PROMOTION+"%' ");
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.TENDER_PROMOTION)){
			 queryString.append(" and pp.prmtn_id like '"+Constant.PromotionType.TENDER_PROMOTION+"%' ");
		 }
		 if(promotionId != null && !promotionId.trim().equals("")){
			 queryString.append(" and pp.prmtn_id = '"+promotionId+"' ");
		 }	       
		   
		 queryString.append(" AND NVL (s.RWD_CARD_NO, '1') = ");
		 queryString.append("      NVL ( (SELECT MIN (s2.RWD_CARD_NO) ");
		 queryString.append("       FROM sales_trn s2 ");
		 queryString.append("      WHERE s2.trn_dt = s.trn_dt ");
		 queryString.append("    AND S2.PRMTN_RDPTN_OID = S.PRMTN_RDPTN_OID ");
		 queryString.append("    AND s2.store_id = s.store_id ");
		 queryString.append("    ), '1' ) ");
		 queryString.append("        AND NVL (history.auth_id, '1') = ");
		 queryString.append("      NVL ( (SELECT MIN (history2.auth_id) ");
		 queryString.append("       FROM item_rdptn_his history2, ");
		 queryString.append("    prmtn_premium_rdptn pp2 ");
		 queryString.append("      WHERE pp2.prmtn_premium_rdptn_oid = history2.prmtn_premium_rdptn_oid(+) ");
		 queryString.append("    AND pp2.prmtn_premium_rdptn_oid = pp.prmtn_premium_rdptn_oid), ");
		 queryString.append("   '1' ) ");
		 queryString.append("    ");
		 queryString.append(" GROUP BY p.prmtn_rdptn_oid,p.prmtn_rdptn_oid, ");
		 queryString.append("  p.rdptn_dt, ");
		 queryString.append("  p.store_id, ");
		 queryString.append("  S.NET_TRN_AMT, ");
		 queryString.append("  S.RWD_CARD_NO, ");
		 queryString.append("  ps.trn_ref, ");
		 queryString.append("  pp.recv_from, ");
		 queryString.append("  p.prmtn_rdptn_id, ");
		 queryString.append("  p.cust_first_nm, ");
		 queryString.append("  p.cust_last_nm, ");
		 queryString.append("  p.cust_contact, ");
		 queryString.append("  pp.prmtn_premium_rdptn_oid, ");
		 queryString.append("  pp.prmtn_id, ");
		 queryString.append("  pp.prmtn_nm, ");
		 queryString.append("  pp.prmtn_artc_id, ");
		 queryString.append("  pp.prmtn_artc_dsc, ");
		 queryString.append("  pp.eligible_qty, ");
		 queryString.append("  pp.redeemed_qty, ");
		 queryString.append("  pp.lastest_redeemed_qty, ");
		 queryString.append("  pp.returned_qty, ");
		 queryString.append("  pp.changed_qty, ");
		 queryString.append("  pp.cancelled_qty, ");
		 queryString.append("  pp.next_rdptn_dt, ");
		 queryString.append("  pp.remark, ");
		 queryString.append("  s.pos_id, ");
		 queryString.append("  s.ticket_no, ");
		 queryString.append("  ps.net_trn_amt, ");
		 queryString.append("  ps.net_amt,  ");
		 queryString.append("  history.auth_id, ");
		 queryString.append("  history.auth_nm ");
         
		 } 
		 if(cbbPromotionType.equals(Constant.PromotionType.ALL)){
			 queryString.append(" union ");
		 }
    //PromotionType     
      if(cbbPromotionType.equals(Constant.PromotionType.ALL) || cbbPromotionType.equals(Constant.PromotionType.TENDER_PROMOTION)){
    	  queryString.append(" SELECT DISTINCT ");
    	  queryString.append(" p.prmtn_rdptn_oid, ");
    	  queryString.append(" p.rdptn_dt, ");
    	  queryString.append(" p.store_id, ");
    	  queryString.append(" S.RWD_CARD_NO, ");
    	  
//    	  queryString.append(" CASE WHEN ps.trn_ref IS NOT NULL THEN ps.trn_ref  ");
//    	  queryString.append("  ELSE  (select ss.trn_ref from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 )  ");
//    	  queryString.append(" END AS trn_ref, ");
    	  
    	  queryString.append("  decode(ps.trn_ref,null,decode( ");
		  queryString.append(" (select ss.trn_ref from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1),null,s.pos_id||' - '||s.ticket_no, ");
		  queryString.append(" (select ss.trn_ref from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1) ");
		  queryString.append(" ),ps.trn_ref) AS trn_ref, ");
    	  
    	  queryString.append(" NVL (pp.recv_from, 'CA') AS recv_from, ");
    	  queryString.append(" p.prmtn_rdptn_id, ");
    	  queryString.append(" p.cust_first_nm, ");
    	  queryString.append(" p.cust_last_nm, ");
    	  queryString.append(" p.cust_contact, ");
    	  queryString.append(" CASE ");
    	  queryString.append("    WHEN (pp.eligible_qty - changed_qty - cancelled_qty) = 0 ");
    	  queryString.append("    THEN ");
    	  queryString.append("       'Cancel' ");
    	  queryString.append("    WHEN (  pp.eligible_qty ");
    	  queryString.append("          - redeemed_qty ");
    	  queryString.append("          - changed_qty ");
    	  queryString.append("          - cancelled_qty) = 0 ");
    	  queryString.append("    THEN ");
    	  queryString.append("       'Complete' ");
    	  queryString.append("    ELSE ");
    	  queryString.append("       'Remain' ");
    	  queryString.append(" END ");
    	  queryString.append("    AS status, ");
    	  queryString.append(" pp.prmtn_premium_rdptn_oid, ");
    	  queryString.append(" pp.prmtn_id, ");
    	  queryString.append(" pp.prmtn_nm, ");
    	  queryString.append(" CASE ");
    	  queryString.append("    WHEN LENGTH (pp.prmtn_artc_id) = 18 THEN pp.prmtn_artc_id ");
    	  queryString.append("    ELSE NULL ");
    	  queryString.append(" END ");
    	  queryString.append("    AS prmtn_artc_id, ");
    	  queryString.append(" pp.prmtn_artc_dsc, ");
    	  queryString.append("     ");
    	  
    	  queryString.append(" decode(ps.trn_ref,null,decode( ");
		  queryString.append(" (select ss.net_trn_amt from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 ),null,S.NET_TRN_AMT, ");
		  queryString.append(" (select ss.net_trn_amt from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 ) ");
		  queryString.append(" ),ps.net_trn_amt)  AS net_amt, ");
    	  
//    	  queryString.append(" CASE WHEN ps.trn_ref IS NOT NULL THEN ps.net_trn_amt  ");
//    	  queryString.append("    ELSE (select ss.net_trn_amt from prmtn_sales ss where ss.prmtn_rdptn_oid = p.prmtn_rdptn_oid and rownum = 1 ) ");
//    	  queryString.append(" END AS net_amt,  ");
    	  
    	  queryString.append("  ");
    	  queryString.append(" ps.net_amt AS cal_net_amt, ");
    	  queryString.append(" pp.eligible_qty - changed_qty - cancelled_qty ");
    	  queryString.append("    AS receive_quantity, ");
    	  queryString.append(" pp.redeemed_qty, ");
    	  queryString.append(" pp.eligible_qty - redeemed_qty - changed_qty - cancelled_qty ");
    	  queryString.append("    AS remain_quantity, ");
    	  queryString.append(" pp.next_rdptn_dt, ");
    	  queryString.append(" pp.remark, ");
    	  queryString.append(" CASE ");
    	  queryString.append("    WHEN history.auth_id IS NOT NULL ");
    	  queryString.append("    THEN ");
    	  queryString.append("       history.auth_id || ' - ' || history.auth_nm ");
    	  queryString.append("    ELSE ");
    	  queryString.append("       NULL ");
    	  queryString.append(" END ");
    	  queryString.append("    AS approvedBy ");
    	  queryString.append("           FROM prmtn_rdptn p, ");
    	  queryString.append(" prmtn_premium_rdptn pp, ");
    	  queryString.append(" prmtn_sales ps, ");
    	  queryString.append(" sales_trn s, ");
    	  queryString.append(" item_rdptn_his history ");
    	  queryString.append("          WHERE     p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid ");
    	  queryString.append(" AND pp.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+) ");
    	  queryString.append(" AND pp.prmtn_id = ps.prmtn_id(+) ");
    	  queryString.append(" AND pp.prmtn_premium_rdptn_oid = history.prmtn_premium_rdptn_oid(+) ");
    	  queryString.append(" AND p.rdptn_dt >= TO_DATE ('"+sf.format(fromDate)+"', 'DD-MM-YYYY') ");
    	  queryString.append(" AND p.rdptn_dt <= TO_DATE ('"+sf.format(toDate)+"', 'DD-MM-YYYY') ");
    	  queryString.append(" AND p.rdptn_typ IN ('P', 'A') ");
    	  queryString.append(" AND p.prmtn_rdptn_oid = s.prmtn_rdptn_oid(+) ");
    	  queryString.append(" AND s.trn_dt(+) >= TO_DATE ('"+sf.format(fromDate)+"', 'DD-MM-YYYY') ");
    	  queryString.append(" AND s.trn_dt(+) <= TO_DATE ('"+sf.format(toDate)+"', 'DD-MM-YYYY') ");
    	  queryString.append(" AND (S.PRMTN_RDPTN_OID = P.PRMTN_RDPTN_OID OR S.POS_PRMTN_RDPTN_OID = P.PRMTN_RDPTN_OID) ");
    	  queryString.append(" AND pp.prmtn_id LIKE 'TD%' ");
         
  // store id
		 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals("S999")){
			 String[] stArr = storeId.split(",");
			 queryString.append(" and p.store_id in (");
			 for(int i=0;i<stArr.length;i++){
				 if(i>0){
					 queryString.append(",");
				 }
				 queryString.append("'"+stArr[i]+"'");
			 }
			 queryString.append(")");
//			 queryString.append(" and p.store_id = '"+ storeId+"'");
			 }	 
  // promotion id	   
		 if(promotionId != null && !promotionId.trim().equals("")){
			 queryString.append(" and pp.prmtn_id = '"+promotionId+"'");
		 }	 
		 
		 //-- 06/09/2013 by Aon : Fix Same Row of CS&CA
		 queryString.append(" AND NVL (s.RWD_CARD_NO, '1') = ");
		 queryString.append("  NVL ( (SELECT MIN (s2.RWD_CARD_NO) ");
		 queryString.append(" FROM sales_trn s2 ");
		 queryString.append("  WHERE s2.trn_dt = s.trn_dt ");
		 queryString.append("  AND S2.PRMTN_RDPTN_OID = S.PRMTN_RDPTN_OID ");
		 queryString.append("  AND s2.store_id = s.store_id), ");
		 queryString.append(" '1' ");
		 queryString.append("  ) ");
		 queryString.append("  AND NVL (history.auth_id, '1') = ");
		 queryString.append("  NVL ( (SELECT MIN (history2.auth_id) ");
		 queryString.append(" FROM item_rdptn_his history2, ");
		 queryString.append("  prmtn_premium_rdptn pp2 ");
		 queryString.append("  WHERE pp2.prmtn_premium_rdptn_oid = ");
		 queryString.append(" history2.prmtn_premium_rdptn_oid(+) ");
		 queryString.append("  AND pp2.prmtn_premium_rdptn_oid = ");
		 queryString.append("  pp.prmtn_premium_rdptn_oid), ");
		 queryString.append(" '1' ");
		 queryString.append("  ) ");
		 queryString.append(" GROUP BY p.prmtn_rdptn_oid, ");
		 queryString.append("  p.rdptn_dt, ");
		 queryString.append("  p.store_id, ");
		 queryString.append("  S.NET_TRN_AMT, ");
		 queryString.append("  S.RWD_CARD_NO, ");
		 queryString.append("  ps.trn_ref, ");
		 queryString.append("  pp.recv_from, ");
		 queryString.append("  p.prmtn_rdptn_id, ");
		 queryString.append("  p.cust_first_nm, ");
		 queryString.append("  p.cust_last_nm, ");
		 queryString.append("  p.cust_contact, ");
		 queryString.append("  pp.prmtn_premium_rdptn_oid, ");
		 queryString.append("  pp.prmtn_id, ");
		 queryString.append("  pp.prmtn_nm, ");
		 queryString.append("  pp.prmtn_artc_id, ");
		 queryString.append("  pp.prmtn_artc_dsc, ");
		 queryString.append("  pp.eligible_qty, ");
		 queryString.append("  pp.redeemed_qty, ");
		 queryString.append("  pp.lastest_redeemed_qty, ");
		 queryString.append("  pp.returned_qty, ");
		 queryString.append("  pp.changed_qty, ");
		 queryString.append("  pp.cancelled_qty, ");
		 queryString.append("  pp.next_rdptn_dt, ");
		 queryString.append("  pp.remark, ");
		 queryString.append("  s.pos_id, ");
		 queryString.append("  s.ticket_no, ");
		 queryString.append("  ps.net_trn_amt, ");
		 queryString.append("  ps.net_amt,  ");
		 queryString.append("  history.auth_id, ");
		 queryString.append("  history.auth_nm ");
		 queryString.append(" ORDER BY rdptn_dt, store_id, prmtn_id, trn_ref ");
         }           
			 
      	queryString.append(" ) result ");
         if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.TRUE)){
        	 queryString.append(" where result.status ='Complete' ");
         }else if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.FALSE)){
        	 queryString.append(" where result.status ='Remain' ");
         }else if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.CANCEL)){
        	 queryString.append(" where result.status ='Cancel' ");
         }     
         queryString.append(" order by result.rdptn_dt, result.store_id, result.prmtn_id, result.trn_ref  ");  
         System.out.println("sql:"+queryString.toString());
         JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString.toString(), new RowMapperResultReader(new PromotionRedemptionCACSMapper()));   
	}

	//CS&CA DETAIL
	public List getPromotionRedemptionReportCSCADetail(String storeId, Date fromDate, Date toDate, String redemptionStatus,String cbbPromotionType,String promotionId) throws Exception{
		String queryString  = "";
		 queryString  = "select * from ("  ;
		 if( cbbPromotionType !=null &&  !cbbPromotionType.equals(Constant.PromotionType.TENDER_PROMOTION)){ 
		 
			queryString +=" select"
			+" distinct "
			+" p.rdptn_dt, p.store_id"
			+"  , case when ps.trn_ref is not null " 
            +"        then ps.trn_ref " 
            +"        else s.pos_id ||'-'|| s.ticket_no " 
            +"    end as trn_ref "
			+" , nvl(pp.recv_from,'CA') as recv_from"
			+" , p.prmtn_rdptn_id, p.cust_first_nm,p. cust_last_nm, p.cust_contact"
			+" , case when (pp.eligible_qty - changed_qty - cancelled_qty) = 0 then 'Cancel' "
			+"     when (pp.eligible_qty - redeemed_qty - changed_qty - cancelled_qty) = 0 then 'Complete'"
			+"     else 'Remain' end as status"
			+" , pp.prmtn_premium_rdptn_oid"
			+" , pp.prmtn_id, pp.prmtn_nm"
			+" , case when length(pp.prmtn_artc_id) = 18 then pp.prmtn_artc_id else null end as prmtn_artc_id"
			+" , pp.prmtn_artc_dsc"
			+"  , case when ps.trn_ref is not null " 
            +"        then ps.net_trn_amt " 
            +"        else s.net_trn_amt " 
            +"    end as net_amt "
			+" , ps.net_amt AS cal_net_amt "
			+" , case when t.tender_typ = 2 then substr(c.ref_info,1,16) else null end as credit_card"
			+" , c.trn_amt "
			+" , case when max(s.dscnt_card_no) is not null then max(s.dscnt_card_no) else max(s.dscnt_card_no2) end as dscnt_cardno"
			+" , s.rwd_card_no AS rwd_card_no"
			+" , c.cr_card_appr_cd as approve_code"
			
			+" , pp.eligible_qty - changed_qty - cancelled_qty as receive_quantity"
			+" , pp.redeemed_qty"
			+" , pp.eligible_qty - redeemed_qty - changed_qty - cancelled_qty as remain_quantity"
			+" , pp.next_rdptn_dt, pp.remark"
			+"  , case when history.auth_id is not null " 
            +"        then history.auth_id || ' - ' || history.auth_nm " 
            +"        else null " 
            +"    end as approvedBy "
			// BEGIN TOEY APPEND TENDER_NM
			+" ,t.tender_nm ";
			// END TOEY APPEND TENDER_NM
			
		queryString+= " from prmtn_rdptn p, prmtn_premium_rdptn pp, prmtn_sales ps, sales_trn s, csh_trn c, tender t, item_rdptn_his history"
			 +" where p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid"
			 +" and pp.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+)"
			 +" and pp.prmtn_id = ps.prmtn_id(+)"
			 +" and pp.prmtn_premium_rdptn_oid = history.prmtn_premium_rdptn_oid(+)"
			 +" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			 +" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
			 +" and p.rdptn_typ IN ('P','A')"
			 +" and p.prmtn_rdptn_oid = s.prmtn_rdptn_oid(+)"
			 +" and s.sales_trn_oid = c.sales_trn_oid (+)"
	         +" and c.tender_id = t.tender_id(+)"
	         
			 +" and s.trn_dt(+) >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			 +" and s.trn_dt(+) <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')" ;
		
		 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
		 queryString+= " and p.store_id = '"+ storeId+"'";
		 }	 
			    
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.CORPORATE_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.CORPORATE_PROMOTION+"%'";
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.CATEGORY_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.CATEGORY_PROMOTION+"%'";
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.ARTICLE_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.ARTICLE_PROMOTION+"%'";
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.TENDER_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.TENDER_PROMOTION+"%'";
		 }
		 if(promotionId != null && !promotionId.trim().equals("")){
			 queryString+= " and pp.prmtn_id = '"+promotionId+"'";
		 }	       
		   
	     queryString+= " and pp.prmtn_id not like 'TD%' and ps.prmtn_id NOT LIKE 'TD%'";

         queryString+= " group by p.prmtn_rdptn_oid"
			 +",p.rdptn_dt, p.store_id"
			 +", ps.trn_ref"
			 +", pp.recv_from"
			 +", p.prmtn_rdptn_id, p.cust_first_nm,p. cust_last_nm, p.cust_contact"
			 +", pp.prmtn_premium_rdptn_oid"
			 +", pp.prmtn_id, pp.prmtn_nm, pp.prmtn_artc_id, pp.prmtn_artc_dsc"
			 +", pp.eligible_qty, pp.redeemed_qty"
			 +", c.ref_info ,t.tender_typ , s.rwd_card_no ,c.cr_card_appr_cd , c.trn_amt "
			 
			 +", pp.lastest_redeemed_qty, pp.returned_qty"
			 +", pp.changed_qty, pp.cancelled_qty, pp.next_rdptn_dt, pp.remark"
			 +", case when ps.trn_ref is not null then ps.trn_ref else  s.pos_id ||'-'|| s.ticket_no end "
			 +", case when ps.trn_ref is not null then ps.net_trn_amt else  s.net_trn_amt  end "
			 +", ps.net_amt"
			 +", case when history.auth_id is not null then history.auth_id || ' - ' || history.auth_nm else null end "
         	// BEGIN TOEY APPEND TENDER_NM
			+" ,t.tender_nm ";
			// END TOEY APPEND TENDER_NM
         
		 } 
		 if(cbbPromotionType.equals(Constant.PromotionType.ALL)){
			 queryString += " union ";
		 }
    //PromotionType     
      if(cbbPromotionType.equals(Constant.PromotionType.ALL) || cbbPromotionType.equals(Constant.PromotionType.TENDER_PROMOTION)){
         queryString +=" select"        	 
         +" distinct "
         +" p.rdptn_dt, p.store_id"  
         +"  , case when ps.trn_ref is not null " 
         +"        then ps.trn_ref " 
         +"        else s.pos_id ||'-'|| s.ticket_no " 
         +"    end as trn_ref "
         +" , nvl(pp.recv_from,'CA') as recv_from"
         +" , p.prmtn_rdptn_id, p.cust_first_nm,p. cust_last_nm, p.cust_contact"
         +" , case when (pp.eligible_qty - changed_qty - cancelled_qty) = 0 then 'Cancel' "
         +"     when (pp.eligible_qty - redeemed_qty - changed_qty - cancelled_qty) = 0 then 'Complete'"
         +"     else 'Remain' end as status"
         +" , pp.prmtn_premium_rdptn_oid"
         +" , pp.prmtn_id, pp.prmtn_nm"
         +" , case when length(pp.prmtn_artc_id) = 18 then pp.prmtn_artc_id else null end as prmtn_artc_id"
         +" , pp.prmtn_artc_dsc"
         +"  , case when ps.trn_ref is not null " 
         +"        then ps.net_trn_amt " 
         +"        else s.net_trn_amt " 
         +"    end as net_amt "
         +" , ps.net_amt AS cal_net_amt"
         +" , case when t.tender_typ = 2 then substr(c.ref_info,1,16) else null end as credit_card"
         +" , c.trn_amt "
         +" , case when max(s.dscnt_card_no) is not null then max(s.dscnt_card_no) else max(s.dscnt_card_no2) end as dscnt_cardno"
		 +" , s.rwd_card_no AS rwd_card_no"
		 +" , c.cr_card_appr_cd as approve_code"
         
         +" , pp.eligible_qty - changed_qty - cancelled_qty as receive_quantity"  
         +" , pp.redeemed_qty"
         +" , pp.eligible_qty - redeemed_qty - changed_qty - cancelled_qty as remain_quantity"
         +" , pp.next_rdptn_dt, pp.remark"
         +"  , case when history.auth_id is not null "
         +"   		then history.auth_id || ' - ' || history.auth_nm "
         +"         else null "
         +"    end as approvedBy"
         // BEGIN TOEY APPEND TENDER_NM
		 +" ,t.tender_nm "
		 // END TOEY APPEND TENDER_NM

         +" from prmtn_rdptn p, prmtn_premium_rdptn pp, prmtn_sales ps"
         +" , sales_trn s, csh_trn c, tender t, item_rdptn_his history"
         +" where p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid"
         +" and pp.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+)"
         +" and pp.prmtn_id = ps.prmtn_id(+)"
         +" and pp.prmtn_premium_rdptn_oid = history.prmtn_premium_rdptn_oid(+)"
		 +" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
		 +" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
         +" and p.rdptn_typ IN ('P','A')"
         +" and p.prmtn_rdptn_oid = s.prmtn_rdptn_oid(+)"
		 +" and s.sales_trn_oid = c.sales_trn_oid (+)"
         +" and c.tender_id = t.tender_id(+)"
         
		 +" and s.trn_dt(+) >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
		 +" and s.trn_dt(+) <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"    
         +" and pp.prmtn_id like 'TD%'";
         
  // store id
		 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
			 queryString+= " and p.store_id = '"+ storeId+"'";
			 }	 
  // promotion id	   
		 if(promotionId != null && !promotionId.trim().equals("")){
			 queryString+= " and pp.prmtn_id = '"+promotionId+"'";
		 }	 

		 queryString+=" group by p.prmtn_rdptn_oid"     
         +" ,p.rdptn_dt, p.store_id"
         +" , ps.trn_ref"
         +" , pp.recv_from"
         +" , p.prmtn_rdptn_id, p.cust_first_nm,p. cust_last_nm, p.cust_contact"
         +" , pp.prmtn_premium_rdptn_oid"
         +" , pp.prmtn_id, pp.prmtn_nm, pp.prmtn_artc_id, pp.prmtn_artc_dsc"
         +" , pp.eligible_qty, pp.redeemed_qty"
         +", c.ref_info ,t.tender_typ , s.rwd_card_no ,c.cr_card_appr_cd , c.trn_amt "
	     +" , pp.lastest_redeemed_qty, pp.returned_qty"
         +" , pp.changed_qty, pp.cancelled_qty, pp.next_rdptn_dt, pp.remark"
         +" , case when ps.trn_ref is not null then ps.trn_ref else  s.pos_id ||'-'|| s.ticket_no end "
         +" , case when ps.trn_ref is not null then ps.net_trn_amt else  s.net_trn_amt  end "
         +" , ps.net_amt"
         +" , case when history.auth_id is not null then history.auth_id || ' - ' || history.auth_nm else NULL end"
         // BEGIN TOEY APPEND TENDER_NM
		 +" ,t.tender_nm "
		 // END TOEY APPEND TENDER_NM
         +" order by rdptn_dt, store_id, prmtn_id, trn_ref";
         }           
			 
         queryString+= " ) result ";
         if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.TRUE)){
            queryString+= " where result.status ='Complete'";
         }else if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.FALSE)){
             queryString+= " where result.status ='Remain'";
         }else if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.CANCEL)){
             queryString+= " where result.status ='Cancel'";
         }     
         queryString+= " order by result.rdptn_dt, result.store_id, result.prmtn_id, result.trn_ref "  ; 
         
         System.out.println("sql getPromotionRedemptionReportCSCADetail :"+queryString);
         
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new PromotionRedemptionCSCADetailMapper()));   
	}
	
	class PromotionRedemptionCACSMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			PromotionPremiumCACS obj = new PromotionPremiumCACS(); 
			int i = 1;
			obj.setRecordNo(index+1);
			obj.setPrmtn_oid(rs.getLong(i++));
 		    obj.setRdptn_dt(rs.getDate(i++));
		    obj.setStore_id(rs.getString(i++));
		    // Pawan Comment 14/11/2012
//		    obj.setNet_trn_amount(rs.getString(i++));
		    obj.setRwd_card_no(rs.getString(i++));
		    obj.setTrn_ref(rs.getString(i++));
		    obj.setRecv_from(rs.getString(i++));
		    obj.setPrmtn_rdptn_id(rs.getString(i++));
		    obj.setCust_first_nm(rs.getString(i++));
		    obj.setCust_last_nm(rs.getString(i++));
		    obj.setCust_contact(rs.getString(i++));
		    //obj.setDscnt_cardno(rs.getString(i++));  
		    //obj.setRwd_card_no(rs.getString(i++));
		    obj.setStatus(rs.getString(i++));
		    obj.setPrmtn_premium_rdptn_oid(rs.getLong(i++));
		    obj.setPrmtn_id(rs.getString(i++));
		    obj.setPrmtn_nm(rs.getString(i++));		   
		    obj.setPrmtn_artc_id(rs.getString(i++));
		    obj.setPrmtn_artc_dsc(rs.getString(i++));
		   // obj.setNet_trn_amt(rs.getBigDecimal(i++));
		    obj.setNet_amt(rs.getBigDecimal(i++));
		    obj.setCal_net_amt(rs.getBigDecimal(i++));
		   // obj.setCredit_card(rs.getString(i++));
		   // obj.setApprove_code(rs.getString(i++));
		    obj.setReceive_quantity(rs.getBigDecimal(i++));
		    obj.setRedeemed_qty(rs.getBigDecimal(i++));
		    obj.setRemain_quantity(rs.getBigDecimal(i++));
		    obj.setNext_rdptn_dt(rs.getDate(i++));
		    obj.setRemark(rs.getString(i++));	    
		    obj.setApprove_by(rs.getString(i));
 			return obj;
		}
	}
 
	class PromotionRedemptionCSCADetailMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			PromotionPremiumCACS obj = new PromotionPremiumCACS(); 
			int i = 1;
			obj.setRecordNo(index+1);
			obj.setPrmtn_oid(rs.getLong(i++));
 		    obj.setRdptn_dt(rs.getDate(i++));
		    obj.setStore_id(rs.getString(i++));
		    obj.setTrn_ref(rs.getString(i++));
		    obj.setRecv_from(rs.getString(i++));
		    obj.setPrmtn_rdptn_id(rs.getString(i++));
		    obj.setCust_first_nm(rs.getString(i++));
		    obj.setCust_last_nm(rs.getString(i++));
		    obj.setCust_contact(rs.getString(i++));
		    obj.setStatus(rs.getString(i++));
		    obj.setPrmtn_premium_rdptn_oid(rs.getLong(i++));
		    obj.setPrmtn_id(rs.getString(i++));
		    obj.setPrmtn_nm(rs.getString(i++));		   
		    obj.setPrmtn_artc_id(rs.getString(i++));
		    obj.setPrmtn_artc_dsc(rs.getString(i++));
		    obj.setNet_amt(rs.getBigDecimal(i++));
		    obj.setCal_net_amt(rs.getBigDecimal(i++));
		    obj.setCredit_card(rs.getString(i++));
		    obj.setTender_amt(rs.getBigDecimal(i++));
		    obj.setDscnt_cardno(rs.getString(i++));  
		    obj.setRwd_card_no(rs.getString(i++));
		    obj.setApprove_code(rs.getString(i++));
		    
		    obj.setReceive_quantity(rs.getBigDecimal(i++));
		    obj.setRedeemed_qty(rs.getBigDecimal(i++));
		    obj.setRemain_quantity(rs.getBigDecimal(i++));
		    obj.setNext_rdptn_dt(rs.getDate(i++));
		    obj.setRemark(rs.getString(i++));	    
		    obj.setApprove_by(rs.getString(i));
		    // BEGIN TOEY APPEND TENDER_NM
		    obj.setTender_nm(rs.getString("tender_nm"));
		    // END TOEY APPEND TENDER_NM
		    
 			return obj;
		}
	}
 
	
	public List getPromotionPremiumRedemptions_summaryJDBC(String storeId, String redemptionStatus, Date fromDate, Date toDate, String userId , String promotionId)  {
		
		String queryString  =  "select * from ("			
			+"  select distinct st.store_id ,st.store_nm ,pp.prmtn_id ,p.rdptn_dt, ps.trn_ref ,p.prmtn_rdptn_id ,p.cust_first_nm  "
			+"  ,p.cust_last_nm ,p.cust_contact ,p.create_user_id ,p.create_user_nm"
		
			+"	,strn.NET_TRN_AMT"
			
			+"	,nvl(ps.net_amt,0)   "
			+"  ,case when (pp.eligible_qty - changed_qty - cancelled_qty) = 0 then 'Cancel'  "
			+" 	     when (pp.eligible_qty - redeemed_qty - changed_qty - cancelled_qty) = 0 then 'Complete' "
			+" 	 else 'Remain' end as status"
			+"  , nvl(t1.net_amt1,0)  as net_amt_sale_item"
			+"  from prmtn_rdptn p ,prmtn_premium_rdptn pp ,store st "
		
			+"	,sales_trn strn "
		
			+" ,prmtn_sales ps ";
		
		queryString +=" ,(select   psi.prmtn_sales_oid  "
			+"   , sum(psi.net_amt) as  net_amt1    "
			+"    from prmtn_rdptn p ,prmtn_sales ps ,prmtn_sales_item  psi,the_power_mch tpm  "
			+"         where "
			+"          p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid "
			+"          and p.rdptn_dt = to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')";
//			+"          and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";	
		// 	promotion Id
		  if(promotionId != null && !promotionId.trim().equals("")){  
				 queryString+= " and ps.prmtn_id = '"+promotionId+"'";
		   }
		// user Id		 
		  if(userId != null && !userId.trim().equals("")){
			     queryString+= " and p.create_user_id = '"+ userId+"'";
		   }
		  
		 queryString+= " and ps.prmtn_sales_oid =  psi.prmtn_sales_oid"
			+"           and psi.mch3 = tpm.mch_id ";
		//store id
		 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
			 queryString+= " and p.store_id = '"+ storeId+"'";
			 }	 		
		 queryString +=" group by  psi.prmtn_sales_oid ) t1 "
			 
			+" where "
			+"  p.store_id = st.store_id"
			+" and  p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid "
			+" and  p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid  "
			+" and  pp.prmtn_id = ps.prmtn_id "
		
			+"  AND (STRN.PRMTN_RDPTN_OID  = P.PRMTN_RDPTN_OID or STRN.POS_PRMTN_RDPTN_OID = P.PRMTN_RDPTN_OID)"
		
			+" and p.rdptn_dt = to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')";
//			+" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";
		//store id
			 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
				 queryString+= " and p.store_id = '"+ storeId+"'";
				}
       //promotion Id
			 if(promotionId != null && !promotionId.trim().equals("")){
				 queryString+= " and pp.prmtn_id = '"+promotionId+"'";
			 }	 
	   // user Id		 
			 if(userId != null && !userId.trim().equals("")){
				 queryString+= " and p.create_user_id = '"+ userId+"'";
			 }
		queryString +=" and ps.prmtn_sales_oid = t1.prmtn_sales_oid(+)";

		queryString +=" order by p.rdptn_dt,p.prmtn_rdptn_id ,pp.prmtn_id";
		queryString +=" ) result ";
        if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.TRUE)){
            queryString+= " where result.status ='Complete'";
         }else if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.FALSE)){
             queryString+= " where result.status ='Remain'";
         }else if(redemptionStatus != null && redemptionStatus.equals(Constant.PromotionStatus.CANCEL)){
             queryString+= " where result.status ='Cancel'";
         }       
        System.out.println("sql PremiumRedemptions_summary :"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new PromotionPremiumSummaryMapper()));   
	}
	class PromotionPremiumSummaryMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			PromotionPremiumSummary obj = new PromotionPremiumSummary(); 
			int i= 1;
			obj.setRecordNo(index+1);
			obj.setStore_id(rs.getString(i++));
			obj.setStore_nm(rs.getString(i++));
 		    obj.setPrmtn_id(rs.getString(i++));
		    obj.setRdptn_dt(rs.getDate(i++));
		    obj.setTrn_ref(rs.getString(i++));
		    obj.setPrmtn_rdptn_id(rs.getString(i++));
 		    obj.setCust_first_nm(rs.getString(i++));
 		    obj.setCust_last_nm(rs.getString(i++));
 		    obj.setCust_contact(rs.getString(i++));
 		    obj.setCreate_user_id(rs.getString(i++));
 		    obj.setCreate_user_nm(rs.getString(i++));
// 		    thitikarnt add 22/10/2012
 		    obj.setNet_trn_amount(rs.getString(i++));
 		    obj.setNet_amt(rs.getBigDecimal(i++));
 		    obj.setStatus(rs.getString(i++));
  		    obj.setNet_amt_sale_item(rs.getBigDecimal(i));
 		    obj.setHpamout(obj.getNet_amt().subtract(obj.getNet_amt_sale_item()));
 			return obj;
		}
	}
	
	public List getPromotionPremiumRedemptions_premiumDetailJDBC(Date fromDate, Date toDate,String cbbPromotionType , String promotionId,String storeId)  {
		
		String queryString  = " select distinct "
		+" pp.prmtn_id ,pp.prmtn_nm ,p.rdptn_dt ,p.store_id "
		+" ,ps.trn_ref ,nvl(p.cust_first_nm,' ') ,nvl(p.cust_last_nm,' ')"
		+" ,ps.cr_card_num"
		+" ,nvl(ps.net_amt,0)"
		+" ,pp.prmtn_artc_typ_id"
		+" ,pp.prmtn_artc_id,pp.prmtn_artc_dsc"
		+" ,nvl(pp.eligible_qty,0)"
		+" , nvl(pp.eligible_qty,0)-(nvl(pp.redeemed_qty,0)+nvl(pp.changed_qty,0)+nvl(pp.cancelled_qty,0)) as  remainquantity"
		+" ,pp.next_rdptn_dt ,pp.remark ,b.tender_id ,b.ref_info "
		+" from  prmtn_rdptn p,prmtn_premium_rdptn pp ,prmtn_sales ps ,sales_trn a ,csh_trn b "
		+" where   p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid"
		+" and p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid"
		+" and p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid"
		+" and pp.prmtn_id = ps.prmtn_id "
		+" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
		+" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";
		//store id
		 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
			 queryString+= " and p.store_id = '"+ storeId+"'";
			}
	       //promotion Id
		 if(promotionId != null && !promotionId.trim().equals("")){
			 queryString+= " and pp.prmtn_id = '"+promotionId+"'";
		 }	 
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.CORPORATE_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.CORPORATE_PROMOTION+"%'";
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.CATEGORY_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.CATEGORY_PROMOTION+"%'";
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.ARTICLE_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.ARTICLE_PROMOTION+"%'";
		 }
		 if(cbbPromotionType != null && cbbPromotionType.equals(Constant.PromotionType.TENDER_PROMOTION)){
			 queryString+= " and pp.prmtn_id like '"+Constant.PromotionType.TENDER_PROMOTION+"%'";
		 }
		
		 queryString+= " and pp.prmtn_artc_typ_id not in ('"+Constant.PromotionArticleType.DISCOUNT_PERCENT
		 +"','"+Constant.PromotionArticleType.DISCOUNT_BAHT
		 +"','"+Constant.PromotionArticleType.SPECIAL_PRICE+"','"+Constant.PromotionArticleType.HOMEPRO_FREEGOODS
		 +"','"+Constant.PromotionArticleType.COLLECT_PURCHASE+"','"+Constant.PromotionArticleType.REWARD+"','"
		 +Constant.PromotionArticleType.REDEEM+"','"+Constant.PromotionArticleType.FREE_SERVICE+"','"
		 +Constant.PromotionArticleType.OTHER+"')";
		 
		 queryString +=" and p.prmtn_rdptn_oid = a.prmtn_rdptn_oid(+)"
		 			 +" and p.rdptn_dt = a.trn_dt(+)"
		 			 +" and a.sales_trn_oid = b.sales_trn_oid";
		 
		 queryString+=" order by p.rdptn_dt ,pp.prmtn_id,p.store_id,ps.trn_ref" ;     
		 System.out.println("sql premiumDetail :"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new PromotionPremiumDetailMapper()));   
	}

	class PromotionPremiumDetailMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			PromotionPremiumDetail obj = new PromotionPremiumDetail(); 
			int i= 1; 
			obj.setRecordNo(index+1);
			obj.setPrmtn_id(rs.getString(i++));
			obj.setPrmtn_nm(rs.getString(i++));
			obj.setRdptn_dt(rs.getDate(i++));
			obj.setStore_id (rs.getString(i++));
			obj.setTrn_ref(rs.getString(i++));
			obj.setCust_first_nm(rs.getString(i++));
			obj.setCust_last_nm(rs.getString(i++));
			obj.setCr_card_num(rs.getString(i++));
			obj.setNet_amt(rs.getDouble(i++));
			obj.setPrmtn_artc_typ_id(rs.getString(i++));
			obj.setPrmtn_artc_id(rs.getString(i++));
			obj.setPrmtn_artc_dsc(rs.getString(i++));
			obj.setEligible_qty(rs.getLong(i++));
			obj.setRemainquantity(rs.getLong(i++)); 
			obj.setNext_rdptn_dt(rs.getDate(i++));
			obj.setRemark(rs.getString(i++));
			
			obj.setTender_id(rs.getString(i++));
			obj.setRef_info(rs.getString(i++));

			obj.setRdptn_dt_str(sf.format(obj.getRdptn_dt()));
			if(obj.getNext_rdptn_dt() != null){
			obj.setNext_rdptn_dt_str(sf.format(obj.getNext_rdptn_dt()));
			}
			obj.setCust_first_nm_last(obj.getCust_first_nm()+" "+obj.getCust_last_nm());
			
 			return obj;
		}
	}
	//getPromotionSales(String storeId, String promotionId, Date fromDate, Date toDate, char redemptionType)
	
	public List getPromotionSales_JDBC(String storeId, String promotionId, Date fromDate, Date toDate)  {
		
		String queryString = " select s1.prmtn_id ,s1.prmtn_nm ,s1.rdptn_dt ,s1.store_id ,s1.cust_grp_id ,s1.cust_grp_nm  ,MBR_CARD_TYP_NM "
		+" , count(s1.prmtn_sales_oid)"
		+" ,sum(s1.net_amt) ,sum(s1.eligible_qty) ,sum(s1.net_item_amt),sum(s1.net_item_amt_p)"
		+" from ("

		+" select  ps.prmtn_id ,ps.prmtn_nm ,p.rdptn_dt ,p.store_id ,c.cust_grp_id , CT.MBR_CARD_TYP_NM,cg.cust_grp_nm   "
		+" ,nvl(ps.net_amt,0) as net_amt,sum(psi.eligible_qty) as eligible_qty,ps.prmtn_sales_oid ,p.rdptn_typ,p.prmtn_rdptn_oid"
		+" ,( select  "
		+"   sum(sti.net_item_amt)"
		+"   from sales_trn_item sti ,sales_trn st  "
		+"   where  st.pos_prmtn_rdptn_oid  =  p.prmtn_rdptn_oid "
		+"    and st.trn_dt = p.rdptn_dt "
		+"    and st.sales_trn_oid = sti.sales_trn_oid    "
		+"    and substr(sti.mc9,1,2)  not in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
		+"   ) as net_item_amt"
		+"  ,( select  "
		+"   sum(sti.net_item_amt)"
		+"   from sales_trn_item sti ,sales_trn st  "
		+"   where  st.pos_prmtn_rdptn_oid  =  p.prmtn_rdptn_oid "
		+"    and st.trn_dt = p.rdptn_dt "
		+"    and st.sales_trn_oid = sti.sales_trn_oid    "
		+"    and substr(sti.mc9,1,2) in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
		+"   ) as net_item_amt_p"
		+"  "
		+" from prmtn_rdptn p, prmtn_sales ps "
		+" ,customer c ,cust_grp cg ,CUST_MBR_CARD CM ,prmtn_sales_item psi , mbr_card_typ ct"
		+" where  p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid "
		+" and p.cust_oid = c.cust_oid(+) and c.cust_grp_id = cg.cust_grp_id(+) "
		+" and ps.prmtn_sales_oid = psi.prmtn_sales_oid(+) "
		+" and cm.mbr_card_typ_id = ct.mbr_card_typ_id(+) "
		 +" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
		 +" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";
		
	    if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
			 queryString+= " and p.store_id = '"+ storeId+"'";
		 }	
		
		 if(promotionId != null && !promotionId.trim().equals("")){
			 queryString+= " and ps.prmtn_id = '"+promotionId+"'";
		 }  
		 queryString+= "AND CM.SAP_ID  = C.SAP_ID and p.rdptn_typ in ('A','D','M')"
		// queryString+= " and p.rdptn_typ = 'D'"
		+" group by ps.prmtn_id ,ps.prmtn_nm ,p.rdptn_dt ,p.store_id ,MBR_CARD_TYP_NM ,c.cust_grp_id ,cg.cust_grp_nm ,ps.net_amt"
		+" ,ps.prmtn_sales_oid ,p.rdptn_typ,p.prmtn_rdptn_oid   "

		+" union"

		+" select  ps.prmtn_id ,ps.prmtn_nm ,p.rdptn_dt ,p.store_id ,c.cust_grp_id ,CT.MBR_CARD_TYP_NM,cg.cust_grp_nm   "
		+" ,nvl(ps.net_amt,0) as net_amt,sum(psi.eligible_qty) as eligible_qty,ps.prmtn_sales_oid ,p.rdptn_typ,p.prmtn_rdptn_oid"
		+" ,( select  "
		+"   sum(sti.net_item_amt)"
		+"   from sales_trn_item sti ,sales_trn st  "
		+"   where  st.prmtn_rdptn_oid  =  p.prmtn_rdptn_oid "
		+"    and st.trn_dt = p.rdptn_dt "
		+"    and st.sales_trn_oid = sti.sales_trn_oid    "
		+"    and substr(sti.mc9,1,2)  not in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
		+"   ) as net_item_amt"
		+"  ,( select  "
		+"   sum(sti.net_item_amt)"
		+"   from sales_trn_item sti ,sales_trn st  "
		+"   where  st.prmtn_rdptn_oid  =  p.prmtn_rdptn_oid "
		+"    and st.trn_dt = p.rdptn_dt "
		+"    and st.sales_trn_oid = sti.sales_trn_oid    "
		+"    and substr(sti.mc9,1,2)  in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
		+"   ) as net_item_amt_p"
		+"  "
		+" from prmtn_rdptn p, prmtn_sales ps "
		+" ,customer c , CUST_MBR_CARD cm,cust_grp cg ,prmtn_sales_item psi , mbr_card_typ ct"
		+" where  p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid AND CM.SAP_ID = C.SAP_ID"
		+" and p.cust_oid = c.cust_oid(+) and c.cust_grp_id = cg.cust_grp_id(+) "
		+" and ps.prmtn_sales_oid = psi.prmtn_sales_oid(+) "
		+" and cm.mbr_card_typ_id = ct.mbr_card_typ_id(+)"
		 +" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
		 +" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";
		 
		 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
		    queryString+= " and p.store_id = '"+ storeId+"'";
		 }	
		 
		 if(promotionId != null && !promotionId.trim().equals("")){
			 queryString+= " and ps.prmtn_id = '"+promotionId+"'";
		 }  
		 queryString+= " and p.rdptn_typ ='P'"
		+" group by ps.prmtn_id ,ps.prmtn_nm ,p.rdptn_dt ,p.store_id ,CT.MBR_CARD_TYP_NM,c.cust_grp_id ,cg.cust_grp_nm ,ps.net_amt"
		+" ,ps.prmtn_sales_oid ,p.rdptn_typ,p.prmtn_rdptn_oid  "

		+" ) s1"
		+" group by s1.prmtn_id ,s1.prmtn_nm ,s1.rdptn_dt ,s1.store_id ,s1.cust_grp_id , MBR_CARD_TYP_NM,s1.cust_grp_nm";
		
		System.out.println("sql:JdbcPromotionRedemptionDao:"+queryString);


		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new PromotionSalesByCustomerMapper()));   
	}
	class PromotionSalesByCustomerMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			PromotionSalesByCustomer  obj = new PromotionSalesByCustomer(); 
			int i= 1; 
			obj.setRecordNo(index+1);
			obj.setPrmtn_id(rs.getString(i++));
			obj.setPrmtn_nm(rs.getString(i++));
			obj.setRdptn_dt(rs.getDate(i++));
			obj.setStore_id(rs.getString(i++));
			obj.setCust_grp_id(rs.getString(i++));
			obj.setCust_member_type(rs.getString(i++));
			obj.setCust_grp_nm  (rs.getString(i++));			
			obj.setSumbill(rs.getBigDecimal(i++));
			obj.setNet_trn_amt(rs.getBigDecimal(i++));			
			obj.setEligible_qty(rs.getBigDecimal(i++));			
			obj.setNet_item_amt(rs.getBigDecimal(i++));
			obj.setNet_item_amt_p(rs.getBigDecimal(i++));
 			return obj;
		}
	}
	
	public List getVendorcupon_JDBC(String storeId, String promotionId, String promotionTypeId,String vendorId
		,String vendorName, Date fromDate, Date toDate, String discountType, String mch3Id)  {
		/*************** Pook comment old SQL 21/01/2014************************/
		/*
		String queryString  = " select p.prmtn_rdptn_oid ,pdr.prmtn_dscnt_rdptn_oid ,ps.prmtn_sales_oid as sale_id,psi.prmtn_sales_item_oid as sale_item_oid"
			+" ,ps.prmtn_id as prmtn_id ,ps.prmtn_nm ,pdr.vendor_id||'-'||pdr.vendor_nm as vendor,a.mch_id as mch_id ,p.rdptn_dt ,null as trn_dt"
			+" ,p.store_id  ,ps.trn_ref as trn_ref,to_number(a.artc_id)||'-'||a.artc_dsc as artc_dsc "
			+" ,nvl(psi.eligible_qty,0) as eligible_qty ,nvl(psi.net_amt,0) as net_amt,nvl(pdr.dscnt_amt,0) as dscnt_amt  "
			+" from prmtn_rdptn p , prmtn_dscnt_rdptn pdr ,prmtn_sales ps ,prmtn_sales_item psi ,artc a "
			+" where  p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid"
			+" and p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid"
			+" and pdr.prmtn_id = ps.prmtn_id "
			+" and ps.prmtn_sales_oid = psi.prmtn_sales_oid"
			+" and psi.artc_id = a.artc_id(+)"
			 +" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			 +" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
	        +" and pdr.dscnt_typ = '"+discountType+"' ";			
			if(storeId != null && !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
				queryString  +=" and p.store_id ='"+storeId+"'";
			}
			if(promotionId != null && !promotionId.trim().equals("")){
				queryString  +=" and ps.prmtn_id ='"+promotionId+"'";
			}
			if(promotionTypeId != null && !promotionTypeId.trim().equals("")){
				queryString  +=" and ps.prmtn_id like '"+promotionTypeId+"%'";
			}else{
				queryString  +=" and (ps.prmtn_id  like('"+Constant.PromotionType.CATEGORY_PROMOTION+"%') or ps.prmtn_id  like('"+Constant.PromotionType.ARTICLE_PROMOTION+"%') )";
			}
			if(vendorId != null && !vendorId.trim().equals("")){
				queryString  +=" and pdr.vendor_id like '%"+vendorId+"'";
			}
			if((vendorId == null || vendorId.trim().equals("")) && (vendorName != null && !vendorName.trim().equals(""))){
				queryString  +=" and pdr.vendor_nm like '%"+replace(vendorName)+"%'";
			}
			if(mch3Id != null && !mch3Id.trim().equals("")){
				queryString  +=" and psi.mch3 ='"+mch3Id+"'";
			}
			
		queryString  +=" union"
			+" select p.prmtn_rdptn_oid  ,pdr.prmtn_dscnt_rdptn_oid ,st.sales_trn_oid as sale_id ,ct.csh_trn_oid as sale_item_oid "
			+" ,pdr.prmtn_id as prmtn_id ,ps.prmtn_nm as prmtn_nm ,pdr.vendor_id||'-'||pdr.vendor_nm ,'' as mch_id ,p.rdptn_dt ,ct.trn_dt as trn_dt"
			+" ,p.store_id ,ps.trn_ref as trn_ref,'CN' as artc_dsc "
			+" ,0 as eligible_qty ,nvl(ct.trn_amt,0) as net_amt ,nvl(ct.trn_amt,0) as dscnt_amt"
			+" from prmtn_rdptn p , prmtn_dscnt_rdptn pdr ,sales_trn st ,csh_trn ct ,prmtn_sales ps "
			+" where  p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid"
			+" and p.prmtn_rdptn_oid = st.pos_prmtn_rdptn_oid"
			+" and st.sales_trn_oid = ct.sales_trn_oid ";
		   queryString  +=" and pdr.prmtn_id = ct.prmtn_id";
		  
			queryString  +=" and pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+)"
			+" and pdr.prmtn_id = ps.prmtn_id(+)"
		     +" and ct.trn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			 +" and ct.trn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
			+" and pdr.dscnt_typ = '"+discountType+"'"
			+" and ct.tender_id = '"+discountType+"' "
			+" and ct.csh_trn_typ_id in ('"+Constant.CashierTransactionType.CNP+"','"+Constant.CashierTransactionType.CNS+"')";
       
		if(storeId != null && !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
			queryString  +=" and p.store_id ='"+storeId+"'";
		}
		if(promotionId != null && !promotionId.trim().equals("")){
			queryString  +=" and pdr.prmtn_id ='"+promotionId+"'";
		}
		if(promotionTypeId != null && !promotionTypeId.trim().equals("")){
			queryString  +=" and pdr.prmtn_id like '"+promotionTypeId+"%'";
		}else{
			queryString  +=" and (pdr.prmtn_id  like('"+Constant.PromotionType.CATEGORY_PROMOTION+"%') or pdr.prmtn_id  like('"+Constant.PromotionType.ARTICLE_PROMOTION+"%') )";
		}
		if(vendorId != null && !vendorId.trim().equals("")){
			queryString  +=" and pdr.vendor_id like '%"+vendorId+"'";
		}
		if((vendorId == null || vendorId.trim().equals("")) && (vendorName != null && !vendorName.trim().equals(""))){
			queryString  +=" and pdr.vendor_nm like '%"+replace(vendorName)+"%'";
		}*/
		//*************** End Old SQL ************************
		
		//Pook add new SQL 22/01/2013 SQL From Tua 
		/* BEGIN New SQL */
		String queryString  = "SELECT p.prmtn_rdptn_oid,"
		      +" pdr.prmtn_dscnt_rdptn_oid,"
		      +" ps.prmtn_sales_oid AS sale_id,"
		      +" psi.prmtn_sales_item_oid AS sale_item_oid,"
		      +" ps.prmtn_id AS prmtn_id,"
		      +" ps.prmtn_nm,"
		      +" pdr.vendor_id || '-' || pdr.vendor_nm AS vendor,"
		      +" a.mch_id AS mch_id,"
		      +" p.rdptn_dt,"
		      +" NULL AS trn_dt,"
		      +" p.store_id,"
		      +" ps.trn_ref AS trn_ref,"
		      +" TO_NUMBER (a.artc_id) || '-' || a.artc_dsc AS artc_dsc,"
		      +" NVL (psi.eligible_qty, 0) AS eligible_qty,"
		      +" NVL (psi.net_amt, 0) AS net_amt,"
		      +" NVL (pdr.dscnt_amt, 0) AS dscnt_amt"
			  +" FROM prmtn_rdptn p,"
		      +" prmtn_dscnt_rdptn pdr,"
		      +" prmtn_sales ps,"
		      +" prmtn_sales_item psi,"
		      +" artc a"
		      +" WHERE p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid"
		      +" AND p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid"
		      +" AND pdr.prmtn_id = ps.prmtn_id"
		      +" AND ps.prmtn_sales_oid = psi.prmtn_sales_oid"
		      +" AND psi.artc_id = a.artc_id(+)"
			  +" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			  +" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
		      +" and pdr.dscnt_typ = '"+discountType+"' ";

		      if(storeId != null && !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
		    	  queryString  +=" and p.store_id ='"+storeId+"'";
		      }
	/* POR fix SQL case refund not show on report
		queryString  +=" union"
			+" SELECT distinct p.prmtn_rdptn_oid,"
		    +" 0 As prmtn_dscnt_rdptn_oid,"
		    +" st.sales_trn_oid AS sale_id,"
		    +" ct.csh_trn_oid AS sale_item_oid,"
		    +" pdr.prmtn_id AS prmtn_id,"
		    +" ps.prmtn_nm AS prmtn_nm,"
		    +" pdr.vendor_id || '-' || pdr.vendor_nm,"
		    +" '' AS mch_id,"
		    +" p.rdptn_dt,"
		    +" ct.trn_dt AS trn_dt,"
		    +" p.store_id,"
		    +" ps.trn_ref AS trn_ref,"
		    +" 'CN' AS artc_dsc,"
		    +" 0 AS eligible_qty,"
		    +" NVL (ct.trn_amt, 0) AS net_amt,"
		    +" NVL (ct.trn_amt, 0) AS dscnt_amt"
			+" FROM prmtn_rdptn p,"
		    +" prmtn_dscnt_rdptn pdr,"
		    +" sales_trn st,"
		    +" csh_trn ct,"
		    +" prmtn_sales ps"
			+" WHERE  p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid"
		    +" AND p.prmtn_rdptn_oid = st.pos_prmtn_rdptn_oid"
		    +" AND st.sales_trn_oid = ct.sales_trn_oid"
		    +" AND pdr.prmtn_id = ct.prmtn_id"
		    +" AND pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+)"
		    +" AND pdr.prmtn_id = ps.prmtn_id(+)"
			+" and ct.trn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			+" and ct.trn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
		    +" and pdr.dscnt_typ = '"+discountType+"'"
			+" and ct.tender_id = '"+discountType+"' "
		    +" and ct.csh_trn_typ_id in ('"+Constant.CashierTransactionType.CNP+"','"+Constant.CashierTransactionType.CNS+"')";

		   if(storeId != null && !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
			   queryString  +=" and p.store_id ='"+storeId+"'";
		   }
		   queryString  +=" order by trn_ref ";
		  */
		   /* End New SQL */
		
		      queryString  +=" union"
		    	  +" SELECT DISTINCT p.prmtn_rdptn_oid, "
		    	  +"  	0 AS prmtn_dscnt_rdptn_oid, "
		    	  +"  	ps.prmtn_sales_oid AS sale_id, "
		    	  +"  	psi.prmtn_sales_item_oid AS sale_item_oid, "
		    	  +"  	pdr.prmtn_id AS prmtn_id, "
		    	  +"  	ps.prmtn_nm AS prmtn_nm, "
		    	  +"  	pdr.vendor_id || '-' || pdr.vendor_nm, "
		    	  +"  	'' AS mch_id, "
		    	  +"  	p.rdptn_dt, "
		    	  +"  	ct.trn_dt AS trn_dt, "
		    	  +"  	p.store_id, "
		    	  +"  	ps.trn_ref AS trn_ref, "
		    	  +"  	'CN' AS artc_dsc, "
		    	  +"  	0 AS eligible_qty, "
		    	  +"  	NVL (ct.trn_amt, 0) AS net_amt, "
		    	  +"  	NVL (ct.trn_amt, 0) AS dscnt_amt "
		    	  +" FROM CSH_TRN ct, "
		    	  +" 	prmtn_dscnt_rdptn pdr, "
		    	  +" 	prmtn_rdptn p, "
		    	  +" 	prmtn_sales ps, "
		    	  +" 	prmtn_sales_item psi "
		    	  +" WHERE pdr.prmtn_id = ct.prmtn_id "
		    	  +" 	AND p.prmtn_rdptn_oid IN "
		    	  +"	(SELECT DISTINCT P.PRMTN_RDPTN_OID "
		    	  +"		FROM SALES_TRAN_PARTNER stp, sales_trn s, prmtn_rdptn p "
		    	  +"		WHERE (STP.SALES_TRAN_OID = CT.SALES_TRN_OID "
		    	  +"     		AND STP.PARENT_SALES_OID = S.SALES_TRN_OID "
		    	  +"     		AND S.POS_PRMTN_RDPTN_OID = P.PRMTN_RDPTN_OID) "
		    	  +"    		OR (S.SALES_TRN_OID = CT.SALES_TRN_OID "
		    	  +"        	AND S.POS_PRMTN_RDPTN_OID = P.PRMTN_RDPTN_OID)) "
		    	  +" 	AND p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid "
		    	  +" 	AND pdr.prmtn_id = ps.prmtn_id(+) "
		    	  +" 	AND pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+) "
		    	  +" 	AND ps.prmtn_sales_oid = psi.prmtn_sales_oid "
				  +" 	and ct.trn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
				  +" 	and ct.trn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
				  +" 	and pdr.dscnt_typ = '"+discountType+"'"
				  +" 	and ct.tender_id = '"+discountType+"' "
				  +" 	and ct.csh_trn_typ_id in ('"+Constant.CashierTransactionType.CNP+"','"+Constant.CashierTransactionType.CNS+"')";

		      if(storeId != null && !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
		    	  queryString  +=" and p.store_id ='"+storeId+"'";
		      }
		      queryString  +=" order by trn_ref ";
		   
		System.out.println("sql vendorCoupon :"+queryString);
		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(queryString, new RowMapperResultReader(new VendorCuponMapper()));   
	}
	
	public List getVendorcupon_inf_JDBC( Date fromDate, Date toDate){
	 		
			String queryString  = " select p.prmtn_rdptn_oid ,pdr.prmtn_dscnt_rdptn_oid ,ps.prmtn_sales_oid as sale_id,psi.prmtn_sales_item_oid as sale_item_oid"
				+" ,ps.prmtn_id as prmtn_id ,ps.prmtn_nm ,pdr.vendor_id||'-'||pdr.vendor_nm as vendor,a.mch_id as mch_id ,p.rdptn_dt ,null as trn_dt"
				+" ,p.store_id  ,ps.trn_ref as trn_ref,to_number(a.artc_id)||'-'||a.artc_dsc as artc_dsc "
				+" ,nvl(psi.eligible_qty,0) as eligible_qty ,nvl(psi.net_amt,0) as net_amt,nvl(pdr.dscnt_amt,0) as dscnt_amt  "
				+" from prmtn_rdptn p , prmtn_dscnt_rdptn pdr ,prmtn_sales ps ,prmtn_sales_item psi ,artc a "
				+" where  p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid"
				+" and p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid"
				+" and pdr.prmtn_id = ps.prmtn_id "
				+" and ps.prmtn_sales_oid = psi.prmtn_sales_oid"
				+" and psi.artc_id = a.artc_id(+)"
				+" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
				 +" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";		
				
			queryString  +=" union"
				+" select p.prmtn_rdptn_oid  ,pdr.prmtn_dscnt_rdptn_oid ,st.sales_trn_oid as sale_id ,ct.csh_trn_oid as sale_item_oid "
				+" ,pdr.prmtn_id as prmtn_id ,ps.prmtn_nm as prmtn_nm ,pdr.vendor_id||'-'||pdr.vendor_nm ,'' as mch_id ,p.rdptn_dt ,ct.trn_dt as trn_dt"
				+" ,p.store_id ,ps.trn_ref as trn_ref,'CN' as artc_dsc "
				+" ,0 as eligible_qty ,nvl(ct.trn_amt,0) as net_amt ,nvl(ct.trn_amt,0) as dscnt_amt"
				+" from prmtn_rdptn p , prmtn_dscnt_rdptn pdr ,sales_trn st ,csh_trn ct ,prmtn_sales ps "
				+" where  p.prmtn_rdptn_oid = pdr.prmtn_rdptn_oid"
				+" and p.prmtn_rdptn_oid = st.pos_prmtn_rdptn_oid"
				+" and st.sales_trn_oid = ct.sales_trn_oid ";
			   queryString  +=" and pdr.prmtn_id = ct.prmtn_id";
			  
				queryString  +=" and pdr.prmtn_rdptn_oid = ps.prmtn_rdptn_oid(+)"
				+" and pdr.prmtn_id = ps.prmtn_id(+)"
				 +" and ct.trn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
				 +" and ct.trn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')"
				+" and ct.csh_trn_typ_id in ('"+Constant.CashierTransactionType.CNP+"','"+Constant.CashierTransactionType.CNS+"')";
			
			JdbcTemplate jt = getJdbcTemplate();
			return jt.query(queryString, new RowMapperResultReader(new VendorCuponMapper()));   
		}
	
	class VendorCuponMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			VendorCupon  obj = new VendorCupon(); 
			int i= 1; 
			obj.setRecordNo(index+1);
			obj.setPrmtn_rdptn_oid(rs.getString(i++));
			obj.setPrmtn_dscnt_rdptn_oid(rs.getString(i++));
			obj.setSale_id(rs.getString(i++));
			obj.setSale_item_oid(rs.getString(i++));
			obj.setPrmtn_id(rs.getString(i++));
			obj.setPrmtn_nm(rs.getString(i++));
			obj.setVendor(rs.getString(i++));
			obj.setMch_id(rs.getString(i++));
			obj.setRdptn_dt(rs.getDate(i++));
			obj.setTrn_dt(rs.getDate(i++));
			obj.setStore_id(rs.getString(i++));
			obj.setTrn_ref(rs.getString(i++));
			obj.setArtc_dsc(rs.getString(i++));
			obj.setEligible_qty(rs.getBigDecimal(i++));
			obj.setNet_amt(rs.getBigDecimal(i++));
			obj.setDscnt_amt(rs.getBigDecimal(i));		
 
 			return obj;
		}
	}
	
	public List getMCSummary_JDBC(String storeId, String promotionId, Date fromDate, Date toDate){
			String STATUS_P = "P";
			String STATUS_D = "D";
			
			String queryString  =" select ps.prmtn_id, ps.prmtn_nm , p.rdptn_dt, p.store_id, substr(psi.mch3, 1, 2) as mch3"
			+" , sum(psi.net_amt)  as net_amt "
			+" , (select sum(sti.net_item_amt) "
			+" from sales_trn st , sales_trn_item sti  "
			+" where  st.sales_trn_oid = sti.sales_trn_oid"
			+" and st.trn_dt =  p.rdptn_dt  "
			+" and st.store_id = p.store_id "
			+" and substr(sti.mc9,1,2) = substr(psi.mch3, 1, 2) "
			+" and st.sts_id in ('"+STATUS_P+"' ,'"+STATUS_D+"')"
			+"  ) as net"
			+"  from prmtn_rdptn  p ,  prmtn_sales  ps ,  prmtn_sales_item  psi  "
			+" where "
			+" p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid"
			+" and ps.prmtn_sales_oid = psi.prmtn_sales_oid "			
			+" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			+" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";
			if( promotionId != null && !promotionId.trim().equals("")){
				queryString  +=" and ps.prmtn_id = '"+promotionId+"' ";
			}
			if(storeId != null && !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)&& !storeId.trim().equals("")){
				queryString  +=" and p.store_id ='"+storeId+"'";
			}
			queryString  +=" group by ps.prmtn_id, ps.prmtn_nm, p.store_id, p.rdptn_dt, mch3"
			+" order by ps.prmtn_id, ps.prmtn_nm, p.store_id, p.rdptn_dt, mch3"; 
			System.out.println("sql:Sales_summary_mc :"+queryString);
			JdbcTemplate jt = getJdbcTemplate();
			return jt.query(queryString, new RowMapperResultReader(new PromotionMCSummaryMapper()));   
		}
		class PromotionMCSummaryMapper implements RowMapper {
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				PromotionMCSummaryForm  obj = new PromotionMCSummaryForm(); 
				int i= 1; 
				obj.setPromotionId(rs.getString(i++));
				obj.setPromotionName(rs.getString(i++));
				obj.setRedemptionDate(rs.getDate(i++));
				obj.setStoreId(rs.getString(i++));
				obj.setMc(rs.getString(i++));
				obj.setRedemptionAmount(rs.getBigDecimal(i++));
				obj.setNetTransactionAmount(rs.getBigDecimal(i++));
 	 			return obj;
			}
 		}
		public List getPromotionSlipSummary_JDBC(String storeId, String promotionId,String promotionType,Date fromDate, Date toDate){
			String queryString =" select  s1.prmtn_id ,s1.prmtn_nm ,s1.store_id , count(distinct s1.trn_ref) ,nvl(sum(s1.net_amt),0) as net_amt "
			+" ,nvl(sum(s1.net_item_amt_d),0) + nvl(sum(s1.net_item_amt_p),0)  as net_item_amt "
			+" ,nvl(sum(s1.net_item_amt_d_p),0) + nvl(sum(s1.net_item_amt_p_p),0) as net_item_amt_p"		
			+"  from ("
			+" select   ps.prmtn_id ,ps.prmtn_nm ,p.store_id ,p.prmtn_rdptn_oid ,ps.trn_ref ,ps.net_amt "
			+" ,("
			+"    select  "
			+"  sum(sti.net_item_amt)"
			+"   from sales_trn_item sti ,sales_trn st , prmtn_rdptn  pr"
			+"   where pr.prmtn_rdptn_oid  = p.prmtn_rdptn_oid  "
			+"   and pr.prmtn_rdptn_oid  = st.pos_prmtn_rdptn_oid  "
			+"   and st.sales_trn_oid = sti.sales_trn_oid    "
			+"   and pr.rdptn_typ in ('A','D','M') "
			+"   and pr.rdptn_dt = st.trn_dt"
			+"   and substr(sti.mc9,1,2)  not in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
			+" )as net_item_amt_d"
			+" , "
			+"  ("
			+"    select  "
			+"  sum(sti.net_item_amt)"
			+"   from sales_trn_item sti ,sales_trn st , prmtn_rdptn  pr"
			+"   where pr.prmtn_rdptn_oid  = p.prmtn_rdptn_oid"
			+"   and pr.prmtn_rdptn_oid  = st.pos_prmtn_rdptn_oid   "
			+"   and st.sales_trn_oid = sti.sales_trn_oid    "
			+"   and pr.rdptn_typ in ('A','D','M') and   pr.rdptn_dt = st.trn_dt"
			+"   and substr(sti.mc9,1,2)  in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
			+" )as net_item_amt_d_p"
			+"  ,"
			+"   ("
			+"    select  "
			+"  sum(sti.net_item_amt)"
			+"   from sales_trn_item sti ,sales_trn st , prmtn_rdptn  pr"
			+"   where pr.prmtn_rdptn_oid  = p.prmtn_rdptn_oid"
			+"   and pr.prmtn_rdptn_oid  = st.prmtn_rdptn_oid   "
			+"   and st.sales_trn_oid = sti.sales_trn_oid    "
			+"   and pr.rdptn_typ ='P' and   pr.rdptn_dt = st.trn_dt"
			+"   and substr(sti.mc9,1,2) not in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
			+" )as net_item_amt_p"
			+" ,  "
			+" ("
			+"    select  "
			+"  sum(sti.net_item_amt)"
			+"   from sales_trn_item sti ,sales_trn st , prmtn_rdptn  pr"
			+"   where pr.prmtn_rdptn_oid  = p.prmtn_rdptn_oid"
			+"   and pr.prmtn_rdptn_oid  = st.prmtn_rdptn_oid   "
			+"   and st.sales_trn_oid = sti.sales_trn_oid"
			+"   and pr.rdptn_typ ='P' and   pr.rdptn_dt = st.trn_dt"
			+"   and substr(sti.mc9,1,2) in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)   "
			+" )as net_item_amt_p_p"

			+" from  prmtn_rdptn  p ,  prmtn_sales  ps "
			+" where "
			+" p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid  "
			+" and p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY')"
			+" and p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY')";
 
			if(storeId != null && !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
				queryString  +=" and p.store_id ='"+storeId+"'";
			}
			if(promotionId != null && !promotionId.trim().equals("")){
				queryString  +=" and ps.prmtn_id ='"+promotionId+"'";
			}
			if(promotionType != null && !promotionType.trim().equals("") && !promotionType.trim().equals(SystemConfigConstant.CENTER_CODE)){
				queryString  +=" and ps.prmtn_id like '"+promotionType+"%'";
			}
 			queryString +="  )s1 group by  s1.prmtn_id ,s1.prmtn_nm ,s1.store_id"
			+" having count( distinct s1.trn_ref) > 1 ";
 			System.out.println("sql:getPromotionSlipSummary_JDBC :"+queryString);
			JdbcTemplate jt = getJdbcTemplate();
			return jt.query(queryString, new RowMapperResultReader(new SlipummaryMapper()));   
		}
		class SlipummaryMapper implements RowMapper {
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				SlipSummaryForm  obj = new SlipSummaryForm(); 
				int i= 1; 
			    obj.setPromotionId(rs.getString(i++));
			    obj.setPromotionName(rs.getString(i++));
			    obj.setStoreId(rs.getString(i++));
			    obj.setTicketQuantity(rs.getInt(i++));
			   // obj.setCustomerQuantity(rs.getInt(i++));
			    obj.setNetPromotionReceiveAmount(rs.getBigDecimal(i++));	
			    if(rs.getBigDecimal(i) != null && rs.getBigDecimal(i).compareTo(new BigDecimal("0"))>0)
			    obj.setNetTransactionAmount(rs.getBigDecimal(i));
			    i++;
			    if(rs.getBigDecimal(i) != null && rs.getBigDecimal(i).compareTo(new BigDecimal("0"))>0)
			    obj.setNetTransactionAmount_Power(rs.getBigDecimal(i));		
 	 			return obj;
			}
 		}		
		
		public List getRdmt_summary_percent_JDBC(Map map){
 			
		    String sql= null;
		    sql = " select  s.store_id  "
		    +" ,sum(s.net_item_amt ),sum(s.net_item_amt_p),sum(s.countslip) ,sum(net_amt)"
		    +" from ("
		    +" select distinct  p.prmtn_rdptn_oid  ,p.store_id   "
		    +" ,nvl(( select  sum(sti.net_item_amt)   from sales_trn st , sales_trn_item sti   "
		    +" where  st.pos_prmtn_rdptn_oid  =  p.prmtn_rdptn_oid  " 
		    +" and st.trn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY') " 
		  	+" and st.trn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY') "
		  	
		    +" and  sti.sales_trn_oid = st.sales_trn_oid      "
		    +" and substr(sti.mc9,1,2)  not in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)  "
		    +" ),0) as net_item_amt "
		    +"  "
		    +" ,nvl(( select  sum(sti.net_item_amt)   from sales_trn st ,sales_trn_item sti  "
		    +" where  st.pos_prmtn_rdptn_oid  =  p.prmtn_rdptn_oid   "
		    +" and st.trn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY') " 
		  	+" and st.trn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY') "

		    +" and  sti.sales_trn_oid = st.sales_trn_oid      "
		    +" and substr(sti.mc9,1,2)   in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)  "
		    +" ),0) as net_item_amt_p  "
		    +" ,(select  count(distinct st.sales_trn_oid)   from  sales_trn st ,sales_trn_item sti "
		    +"  where  st.pos_prmtn_rdptn_oid  =  p.prmtn_rdptn_oid  "
		    +" and st.trn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY') " 
		  	+" and st.trn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY') "

		    +"   and  sti.sales_trn_oid = st.sales_trn_oid  "
		    +"  ) as countslip"
		    +" ,(select sum(ps1.net_amt)  from  prmtn_sales ps1  where ps1.prmtn_rdptn_oid = p.prmtn_rdptn_oid) as net_amt"
		    +" from prmtn_rdptn p, prmtn_sales ps  "
		    +" where p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid   "
			+" and p.rdptn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY')"
			+" and p.rdptn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY')";
		    if(map.get("promotionId")!= null && !map.get("promotionId").toString().equals("")){
		    sql+= " and ps.prmtn_id ='"+map.get("promotionId")+"'";
		    }
		    if(map.get("storeId") != null && !map.get("storeId").toString().trim().equals(SystemConfigConstant.CENTER_CODE)){
		    	sql+= " and p.store_id ='"+map.get("storeId")+"'";
 		    }
		    sql+=" and p.rdptn_typ in ('A','D','M') "
		    +" union"
		    +" select distinct  p.prmtn_rdptn_oid  ,p.store_id    "
		    +" ,nvl(( select  sum(sti.net_item_amt)   from sales_trn st , sales_trn_item sti   "
		    +" where  st.prmtn_rdptn_oid  =  p.prmtn_rdptn_oid   "
		    +" and st.trn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY') " 
		  	+" and st.trn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY') "

		    +" and  sti.sales_trn_oid = st.sales_trn_oid      "
		    +" and substr(sti.mc9,1,2)  not in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)  "
		    +" ),0) as net_item_amt "
		    +"  "
		    +" ,nvl(( select  sum(sti.net_item_amt)   from sales_trn st ,sales_trn_item sti  "
		    +" where  st.prmtn_rdptn_oid  =  p.prmtn_rdptn_oid    "
		    +" and st.trn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY') " 
		  	+" and st.trn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY') "

		    +" and  sti.sales_trn_oid = st.sales_trn_oid      "
		    +" and substr(sti.mc9,1,2)   in (select substr(tpm.mch_id,1,2) from the_power_mch tpm)  "
		    +" ),0) as net_item_amt_p  "

		    +" ,(select  count(distinct st.sales_trn_oid)   from  sales_trn st ,sales_trn_item sti "
		    +"  where  st.prmtn_rdptn_oid  =  p.prmtn_rdptn_oid  "
		    +" and st.trn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY') " 
		  	+" and st.trn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY') "

		    +"   and  sti.sales_trn_oid = st.sales_trn_oid  "
		    +"  ) as countslip"
		    +" ,(select sum(ps1.net_amt)  from  prmtn_sales ps1  where ps1.prmtn_rdptn_oid = p.prmtn_rdptn_oid) as net_amt"
		    +" from prmtn_rdptn p, prmtn_sales ps  "
		    +" where p.prmtn_rdptn_oid = ps.prmtn_rdptn_oid   "
			+" and p.rdptn_dt >= to_date('"+sf.format(map.get("fromDate"))+"','DD-MM-YYYY')"
			+" and p.rdptn_dt <= to_date('"+sf.format(map.get("toDate"))+"','DD-MM-YYYY')";

		    if(map.get("promotionId")!= null && !map.get("promotionId").toString().equals("")){
			    sql+= " and ps.prmtn_id ='"+map.get("promotionId")+"'";
			   }
		    if(map.get("storeId") != null && !map.get("storeId").toString().trim().equals(SystemConfigConstant.CENTER_CODE)){
		    	sql+= " and p.store_id ='"+map.get("storeId")+"'";
 		    }
 		    sql+=" and p.rdptn_typ in ('P') "
		    +" ) s  where s.countslip > 0  group by s.store_id   ";
			
 		   System.out.println("sql:getRdmt_summary_percent_JDBC :" + sql);
			JdbcTemplate jt = getJdbcTemplate();
			return jt.query(sql, new RowMapperResultReader(new Rdmt_summary_percent_Mapper()));   
		}
		class Rdmt_summary_percent_Mapper implements RowMapper {
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				RdmtSummaryPercentForm  obj = new RdmtSummaryPercentForm(); 
				int columnIndex = 1; 
				  obj.setStore_id(rs.getString(columnIndex++));
 				  obj.setNet_item_amt(rs.getBigDecimal(columnIndex++));
				  obj.setNet_item_amt_p(rs.getBigDecimal(columnIndex++));
				  obj.setCountslip(rs.getBigDecimal(columnIndex++));
				  obj.setNet_amt(rs.getBigDecimal(columnIndex++));
  	 			return obj;
			}
 		}

		public List getOtherPremiumDetailsJDBC(String storeId, String promotionId, String promotionTypeId, Date fromDate, Date toDate) throws Exception {

			String queryString  = " SELECT "
			+" pp.prmtn_id, pp.prmtn_nm, p.rdptn_dt, s.store_id, ps.trn_ref, p.prmtn_rdptn_id, "
			+" p.cust_first_nm , p.cust_last_nm , "
			//+" p.prmtn_rdptn_id,p.cust_first_nm || ' ' || p.cust_last_nm AS customer_name, "
			+" CASE WHEN t.tender_typ = 2 THEN SUBSTR (c.ref_info, 1, 16) ELSE NULL  END AS credit_card,"
			+" c.trn_amt,ps.net_trn_amt, "
			+" pp.prmtn_artc_dsc, pp.eligible_qty, a.unit, "
			+" pp.eligible_qty,pp.redeemed_qty ,pp.changed_qty ,pp.cancelled_qty, "
			+" pp.next_rdptn_dt "
			
			+" FROM prmtn_rdptn p,prmtn_premium_rdptn pp, store s,  prmtn_artc a, "   
			+" prmtn_sales ps,sales_trn st,csh_trn c,tender t "

			+" WHERE p.prmtn_rdptn_oid = pp.prmtn_rdptn_oid(+) "
			+" AND pp.prmtn_id = ps.prmtn_id "
			+" AND s.store_id = p.store_id"
			+" AND ps.prmtn_rdptn_oid = p.prmtn_rdptn_oid(+) "
			+" AND pp.prmtn_artc_id = a.prmtn_artc_id(+)"
			+" AND pp.prmtn_artc_typ_id = a.prmtn_artc_typ_id(+) "
			+" AND c.tender_id = t.tender_id "
			+" AND c.sales_trn_oid = st.sales_trn_oid "
			+" AND st.prmtn_rdptn_oid = p.prmtn_rdptn_oid "
			+" AND p.rdptn_dt >= to_date('"+sf.format(fromDate)+"','DD-MM-YYYY') "
			+" AND p.rdptn_dt <= to_date('"+sf.format(toDate)+"','DD-MM-YYYY') ";
			//store id
			 if(storeId != null && !storeId.trim().equals("")&& !storeId.trim().equals(SystemConfigConstant.CENTER_CODE)){
				 queryString+= " AND p.store_id = '"+ storeId+"'";
				}
		       //promotion Id
			 if(promotionId != null && !promotionId.trim().equals("")){
				 queryString+= " AND pp.prmtn_id = '"+promotionId+"'";
			 }	 
			 if(promotionTypeId != null && promotionTypeId.equals(Constant.PromotionType.CORPORATE_PROMOTION)){
				 queryString+= " AND pp.prmtn_id LIKE '"+Constant.PromotionType.CORPORATE_PROMOTION+"%'";
			 }
			 if(promotionTypeId != null && promotionTypeId.equals(Constant.PromotionType.CATEGORY_PROMOTION)){
				 queryString+= " AND pp.prmtn_id LIKE '"+Constant.PromotionType.CATEGORY_PROMOTION+"%'";
			 }
			 if(promotionTypeId != null && promotionTypeId.equals(Constant.PromotionType.ARTICLE_PROMOTION)){
				 queryString+= " AND pp.prmtn_id LIKE '"+Constant.PromotionType.ARTICLE_PROMOTION+"%'";
			 }
			 if(promotionTypeId != null && promotionTypeId.equals(Constant.PromotionType.TENDER_PROMOTION)){
				 queryString+= " AND pp.prmtn_id LIKE '"+Constant.PromotionType.TENDER_PROMOTION+"%'";
			 }
			
			 queryString+= " AND pp.prmtn_artc_typ_id NOT IN ('"+Constant.PromotionArticleType.DISCOUNT_PERCENT
			 +"','"+Constant.PromotionArticleType.DISCOUNT_BAHT
			 +"','"+Constant.PromotionArticleType.SPECIAL_PRICE+"','"+Constant.PromotionArticleType.HOMEPRO_FREEGOODS
			 +"','"+Constant.PromotionArticleType.COLLECT_PURCHASE+"','"+Constant.PromotionArticleType.REWARD+"','"
			 +Constant.PromotionArticleType.REDEEM+"','"+Constant.PromotionArticleType.FREE_SERVICE+"','"
			 +Constant.PromotionArticleType.OTHER+"')";

			 
			// queryString+=" order by p.rdptn_dt ,pp.prmtn_id,p.store_id,ps.trn_ref" ;     
			 System.out.println("sql:getOtherPremiumDetailsJDBC :"+queryString);
			JdbcTemplate jt = getJdbcTemplate();  
			return jt.query(queryString, new RowMapperResultReader(new PromotionPremiumDetailFormMapper()));   
		}
		
		class PromotionPremiumDetailFormMapper implements RowMapper {
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				
				PromotionPremiumRedemption premium = new PromotionPremiumRedemption();
				PromotionRedemption redemp = new PromotionRedemption();
				Store store = new Store();
				PromotionSales sales = new PromotionSales();
				SalesTransaction salesTrn = new SalesTransaction();
				List salesTrnList = new ArrayList();
				int i= 1; 
				
				premium.setPromotionId(rs.getString(i++));
				premium.setPromotionName(rs.getString(i++));  
				redemp.setRedemptionDate(rs.getDate(i++));
				store.setStoreId(rs.getString(i++));
				redemp.setDisplayTicketNo(rs.getString(i++));
				redemp.setPromotionRedemptionId(rs.getString(i++));
				redemp.setCustomerFirstName(rs.getString(i++));
				redemp.setCustomerLastName(rs.getString(i++));
				sales.setCreditCardNo(rs.getString(i++));
				sales.setNetAmount(rs.getBigDecimal(i++));
				salesTrn.setNetTransactionAmount(rs.getBigDecimal(i++));
				premium.setDescription(rs.getString(i++));
				premium.setEligibleQuantity(rs.getBigDecimal(i++));
				premium.setUnit(rs.getString(i++));
				premium.setEligibleQuantity(rs.getBigDecimal(i++));
				premium.setRedeemedQuantity(rs.getBigDecimal(i++));
				premium.setChangedQuantity(rs.getBigDecimal(i++));
				premium.setCancelledQuantity(rs.getBigDecimal(i++));
				premium.setNextRedemptionDate(rs.getDate(i++));
				
				salesTrnList.add(salesTrn);
				redemp.setSalesTransactions(salesTrnList);
				redemp.setStore(store);
				premium.setPromotionRedemption(redemp);
				PromotionPremiumDetailForm obj = new PromotionPremiumDetailForm(premium,sales); 
				
	 			return obj;
			}
		}
		
 }