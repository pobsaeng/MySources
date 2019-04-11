package com.ie.icon.dao.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.constant.Constant;
import com.ie.icon.constant.PosAppContext;
import com.ie.icon.constant.PromotionConstant;
import com.ie.icon.constant.SystemConfigConstant;
import com.ie.icon.dao.PromotionJDBCDao;
import com.ie.icon.domain.promotion.PromotionSalesRPT;
import com.ie.icon.domain.promotion.RPTResult;

public class JdbcPromotionDao extends JdbcDaoSupport implements PromotionJDBCDao {
	private SimpleDateFormat sf;	
	
	public JdbcPromotionDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	protected String replace(String param) {
	    if ( param == null )
	        return param;
	    
	    return param.replace('*', '%');
	}
  
		public List getPromotion_JDBC(Map map) throws Exception {			 
			List list = null;
			List list_result = new ArrayList();
 			list = getPrmtnid(map);
 			
 			String sql = null;
 			PromotionSalesRPT obj = null;
 		    List list_obj = null;  
 			
 			for (int i=0;i<list.size();i++){
 				
 				sql= " select p.prmtn_oid, p.prmtn_id   , p.prmtn_nm "
 				+" ,case when substr(p.prmtn_id,1,2)  = 'CO' then 'Corporate'" 
 				+" when substr(p.prmtn_id,1,2)  = 'CA' then 'Category'" 
 				+" when substr(p.prmtn_id,1,2)  = 'AR' then 'Article'" 
 				+" when substr(p.prmtn_id,1,2)  = 'TD' then 'Tender'"
 				+" else '' end as  prmtn_typ_id"	
 				+" ,p.prmtn_cvrge "
 				+" ,  "
 				+" ( SELECT LTRIM(SYS_CONNECT_BY_PATH(cols, ',  '),',') col_name  "
 				+" FROM ("
 				+"       SELECT cols, ROW_NUMBER() OVER (order by cols) rownumber, COUNT(*) OVER () cnt"
 				+"       FROM ("
 				+"     SELECT PC.partner_prmtn_id ||'-'||pr.prmtn_nm as cols  FROM prmtn_cvrge pc ,promotion  pr   where  "
 				+"      pc.prmtn_oid  =  '"+list.get(i)+"'  "
 				+"     and pc.prmtn_oid = pr.prmtn_oid   ";
 				if(map.get("promotionStatus") != null && !map.get("promotionStatus").equals("")){
 					sql += " and pr.is_active = '"+map.get("promotionStatus")+"'";
 				}
 				
 				sql+="       ) "
 				+"       "
 				+" ) data  "
 				+" WHERE rownumber = cnt "
 				+" START WITH rownumber = 1"
 				+" CONNECT BY PRIOR rownumber = rownumber-1) as partner_prmtn_id"
 				+" ,p.is_sales_ticket_prnt ,p.prmtn_dsc "
 				+" ,p.start_dt ,p.end_dt"
 				+" ,p.is_every_day ,p.is_monday, p.is_tuesday, p.is_wednesday, p.is_thursday, p.is_friday, p.is_saturday, p.is_sunday"
 				+" ,p.is_all_day,to_char(p.start_tm,'HH24:MI AM') as start_tm, to_char(p.end_tm,'HH24:MI AM') as  end_tm"
 				+" ,p.store_cvrge"
 				+" ,  "
 				+" ( SELECT LTRIM(SYS_CONNECT_BY_PATH(cols, ', '),',') col_name"
 				+" FROM ("
 				+"       SELECT cols, ROW_NUMBER() OVER (order by cols) rownumber, COUNT(*) OVER () cnt"
 				+"       FROM ("
 				+" select sc.store_id ||'-'|| s.store_nm as cols from store_cvrge sc ,store s "
 				+" where  sc.store_id  = s.store_id "
 				+" and  sc.prmtn_oid =  '"+list.get(i)+"' "
 				+"     )"
 				+" ) data"
 				+" WHERE rownumber = cnt"
 				+" START WITH rownumber = 1"
 				+" CONNECT BY PRIOR rownumber = rownumber-1) as store_cvrge_name"
 				+" ,p.mbr_card_typ_cvrge "
 				+" ,  "
 				+" ( SELECT LTRIM(SYS_CONNECT_BY_PATH(cols, ', '),',') col_name"
 				+" FROM ("
 				+"       SELECT cols, ROW_NUMBER() OVER (order by cols) rownumber, COUNT(*) OVER () cnt"
 				+"       FROM ("
 				+"  select mctc.card_typ_id||'-'|| mct.mbr_card_typ_nm as cols from mbr_card_typ_cvrge mctc ,mbr_card_typ mct where "
 				+"  mctc.card_typ_id = mct.mbr_card_typ_id"
 				+"  and mctc.prmtn_oid = '"+list.get(i)+"'"
 				+"     )"
 				+" ) data"
 				+" WHERE rownumber = cnt"
 				+" START WITH rownumber = 1"
 				+" CONNECT BY PRIOR rownumber = rownumber-1) as mbr_card_typ_nm"
 				
 				+" , p.block_code_cvrge "
 				+" , "
 				+" ( SELECT LTRIM(SYS_CONNECT_BY_PATH(cols, ', '),',') col_name"
 				+" FROM ("
 				+"       SELECT cols, ROW_NUMBER() OVER (order by cols) rownumber, COUNT(*) OVER () cnt"
 				+"       FROM ("
 				+"  select bc.block_code_id||'-'||bc.block_code_dsc as cols from BLOCK_CODE_CVRGE bcc , block_code bc "
 				+"  where  bc.block_code_id = bcc.block_code_id  and bcc.prmtn_oid ='2525'   "
 				+"     )"
 				+" ) data"
 				+" WHERE rownumber = cnt"
 				+" START WITH rownumber = 1"
 				+" CONNECT BY PRIOR rownumber = rownumber-1) as block_code_dsc"

 				
 				+" ,p.tender_cvrge"
 				+" ,  "
 				+" ( SELECT LTRIM(SYS_CONNECT_BY_PATH(cols, ', '),',') col_name"
 				+" FROM ("
 				+"       SELECT cols, ROW_NUMBER() OVER (order by cols) rownumber, COUNT(*) OVER () cnt"
 				+"       FROM ("
 				+" select tc.tender_id ||'-'|| t.tender_nm as cols from tender_cvrge  tc ,tender t  where "
 				+" tc.tender_id = t.tender_id "
 				+" and tc.prmtn_oid ='"+list.get(i)+"'  "
 				+"     )"
 				+" ) data"
 				+" WHERE rownumber = cnt"
 				+" START WITH rownumber = 1"
 				+" CONNECT BY PRIOR rownumber = rownumber-1) as mbr_card_typ_nm"

 				+" , (select tp.tender_prmtn_typ  from tender_prmtn tp where   tp.prmtn_oid = '"+list.get(i)+"') as tender_prmtn_typ"

 				+" , (select   c.first_nm FROM hire_purchase_comp  hp ,customer c  , tender_prmtn tp "
 				+"  where hp.cust_oid = c.cust_oid and hp.hire_purchase_comp_oid = tp.hire_purchase_comp_oid "
 				+"  and  tp.prmtn_oid = '"+list.get(i)+"') as cfirst_nm"
 				 
 				+" , (select pg.price_grp_nm from tender_prmtn tp ,price_grp pg "
 				+"   where  tp.price_grp_id = pg.price_grp_id  and  tp.prmtn_oid = '"+list.get(i)+"')  as price_grp_nm"
 				+" ,p.cr_card_cvrge " 
 				
 				+" ,(select ccp.min_buying_amt  from corp_cat_prmtn ccp  where   ccp.prmtn_oid = '"+list.get(i)+"' ) as min_buying_amt"
 				+" ,(select ccp.is_incld_sap_prmtn_price  from corp_cat_prmtn ccp  where   ccp.prmtn_oid = '"+list.get(i)+"' ) as is_incld_sap_prmtn_price"
 				+" ,(select ccp.brand_cvrge  from corp_cat_prmtn ccp  where   ccp.prmtn_oid = '"+list.get(i)+"') as brand_cvrge"
 				+" ,"
 				+" ( SELECT LTRIM(SYS_CONNECT_BY_PATH(cols, ', '),',') col_name"
 				+" FROM ("
 				+"       SELECT cols, ROW_NUMBER() OVER (order by cols) rownumber, COUNT(*) OVER () cnt"
 				+"       FROM ("
 				+" select bc.brand_id as cols from brand_cvrge  bc where  bc.prmtn_oid = '"+list.get(i)+"'   "
 				+"     )"
 				+" ) data" 
 				+" WHERE rownumber = cnt"
 				+" START WITH rownumber = 1"
 				+" CONNECT BY PRIOR rownumber = rownumber-1) as brand_id"
 				+" ,(select ccp.vendor_cvrge from corp_cat_prmtn ccp  where ccp.prmtn_oid = '"+list.get(i)+"') as vendor_cvrge"
 				
 				+" ,(select  ap.buying_limit_qty_per_bill  FROM artc_prmtn ap where ap.prmtn_oid ='"+list.get(i)+"') as buying_limit_qty_per_bill"
 				+" ,(select  ap.is_prmtn_set FROM artc_prmtn ap where ap.prmtn_oid ='"+list.get(i)+"') as is_prmtn_set"
 				+" ,(select count(psa.artc_id) from   prmtn_set_artc psa where prmtn_oid = '"+list.get(i)+"') as artc_id"
 				
 				+" ,p.buy_cond_typ "
 				
 				+" from promotion  p    "
 				+" where  p.prmtn_oid ='"+list.get(i)+"' ";
 				if(map.get("promotionStatus") != null && !map.get("promotionStatus").equals("")){
 					sql += " and p.is_active = '"+map.get("promotionStatus")+"'";
 				}
 				
 			System.out.println("sql getPromotion_JDBC :"+sql);
 			 list_obj=  getJdbcTemplate().query(sql, new RowMapperResultReader(new PromotionSalesRPT_Mapper())); 
 			 if(list_obj!=null && !list_obj.isEmpty()){  
 				 obj = (PromotionSalesRPT) list_obj.get(0) ;
 				 obj.setRecordNo(1+i);
 				 list_result.add(obj);     
 			 }
 			}			
			
			
 			return list_result;
		}		
		class PromotionSalesRPT_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				PromotionSalesRPT obj = new PromotionSalesRPT();
				int columnIndex = 1;
				obj.setRecordNo(index+1);
				obj.setPrmtn_oid(rs.getString(columnIndex++));
				obj.setPrmtn_id(rs.getString(columnIndex++));
				obj.setPrmtn_nm(rs.getString(columnIndex++));
				obj.setPrmtn_typ_id(rs.getString(columnIndex++));
				obj.setPrmtn_cvrge(rs.getString(columnIndex++));
				obj.setPartner_prmtn_id(rs.getString(columnIndex++));
				obj.setIs_sales_ticket_prnt(rs.getString(columnIndex++));
				obj.setPrmtn_dsc(rs.getString(columnIndex++));
				obj.setStart_dt(rs.getDate(columnIndex++));
				obj.setEnd_dt(rs.getDate(columnIndex++));
				obj.setIs_every_day(rs.getString(columnIndex++));
				obj.setIs_monday(rs.getString(columnIndex++));
				obj.setIs_tuesday(rs.getString(columnIndex++));
				obj.setIs_wednesday(rs.getString(columnIndex++));
				obj.setIs_thursday(rs.getString(columnIndex++));
				obj.setIs_friday(rs.getString(columnIndex++));
				obj.setIs_saturday(rs.getString(columnIndex++));
				obj.setIs_sunday(rs.getString(columnIndex++));
				obj.setIs_all_day(rs.getString(columnIndex++));
				obj.setStart_tm(rs.getString(columnIndex++));
 				obj.setEnd_tm(rs.getString(columnIndex++)); 
 				
 				obj.setStore_cvrge(rs.getString(columnIndex++));
 				obj.setStore_cvrge_name(rs.getString(columnIndex++));
 				obj.setMbr_card_typ_cvrge(rs.getString(columnIndex++));
 				obj.setMbr_card_typ_nm(rs.getString(columnIndex++));
 				obj.setBlock_code_cvrge(rs.getString(columnIndex++));
 				obj.setBlock_code_dsc(rs.getString(columnIndex++));
 				obj.setTender_cvrge(rs.getString(columnIndex++));
 				obj.setTender_nm(rs.getString(columnIndex++));
 				obj.setTender_prmtn_typ(rs.getString(columnIndex++));
 				obj.setCfirst_nm(rs.getString(columnIndex++));
 				obj.setPrice_grp_nm(rs.getString(columnIndex++));
 				obj.setCr_card_cvrge(rs.getString(columnIndex++)); 				 
				obj.setCard_list(getCr_card(obj.getPrmtn_oid()));	 				
				 
				obj.setMin_buying_amt(rs.getBigDecimal(columnIndex++));
				obj.setIs_incld_sap_prmtn_price(rs.getString(columnIndex++));
				obj.setBrand_cvrge(rs.getString(columnIndex++));
				obj.setBrand_id(rs.getString(columnIndex++));
				obj.setVendor_cvrge(rs.getString(columnIndex++));		
				obj.setVendor_cvr_list(getVendor_cvr(obj.getPrmtn_oid()));
				obj.setHierarchy_cvr_list(getHierarchy_cvr(obj.getPrmtn_oid()));
				
				// article
				obj.setBuying_limit_qty_per_bill(rs.getBigDecimal(columnIndex++));
				obj.setIs_prmtn_set(rs.getString(columnIndex++));
				obj.setArtc_id(rs.getString(columnIndex++));
				obj.setArtc_list( getArtc_list(obj.getPrmtn_oid(),obj.getIs_prmtn_set()));
				
				
				// promotion tier
				obj.setBuy_cond_typ(rs.getString(columnIndex++));
				obj.setPromotion_tier(getPromotion_Tier(obj.getPrmtn_oid()));
  				
  				return obj;
 
				
 			}
 		}
		
		
		public List getPrmtnid(Map para) throws Exception {			 
			List list = null;
			String sql = null;	
			
			sql =  " select distinct  p.prmtn_oid ,p.prmtn_id  "; 		
			sql += " from promotion  p ";
			sql += " where ";
			// date
			if(para.get("promotionDateType").equals("2")){
			sql += "  p.last_upd_dttm >= to_date('"+sf.format(para.get("fromDate"))+"','DD-MM-YYYY')"
			 +" and p.last_upd_dttm <= to_date('"+sf.format(para.get("toDate"))+"','DD-MM-YYYY')";
			}else if(para.get("promotionDateType").equals("1")){
				sql += "  p.end_dt >= to_date('"+sf.format(para.get("fromDate"))+"','DD-MM-YYYY')"
				 +" and p.end_dt <= to_date('"+sf.format(para.get("toDate"))+"','DD-MM-YYYY')";
 			}else{
				sql += "  p.start_dt >= to_date('"+sf.format(para.get("fromDate"))+"','DD-MM-YYYY')"
				 +" and p.start_dt <= to_date('"+sf.format(para.get("toDate"))+"','DD-MM-YYYY')";
 			}
			
			//and a.buy_cond_typ
			if(para.get("promotionAccept") != null && !para.get("promotionAccept").equals("")){
				sql += " and p.buy_cond_typ = '"+para.get("promotionAccept")+"'";
			}
			 
			if(para.get("promotionStatus") != null && !para.get("promotionStatus").equals("")){
				sql += " and p.is_active = '"+para.get("promotionStatus")+"'";
			}
			if(para.get("promotionType") != null && !para.get("promotionType").equals("")){
				sql += " and p.prmtn_typ_id = '"+para.get("promotionType")+"'";
			}
			if(para.get("promotionStoreId") != null && !para.get("promotionStoreId").equals(SystemConfigConstant.CENTER_CODE)){
				sql += "  and p.prmtn_oid in( select distinct p1.prmtn_oid  from promotion  p1, store_cvrge sc  "
                 +"  where p1.prmtn_oid = p.prmtn_oid and  p1.prmtn_oid = sc.prmtn_oid and sc.store_id = '"+para.get("promotionStoreId")+"' )";
			}
			
			if(para.get("promotionConditionValue") != null && !para.get("promotionConditionValue").toString().trim().equals("")){		
				boolean all = false;
				String andor= "";
				if(para.get("promotionCondition").equals(PromotionConstant.All+"")){
					all = true;
					sql+= " and ( ";
					andor =" or ";
				}else{
					sql+= " and ( ";
				}
				if(all || para.get("promotionCondition").equals(PromotionConstant.PromotionNameCondition+"")){
					sql += " p.prmtn_nm like '%"+para.get("promotionConditionValue")+"%'";
				}
				if(all || para.get("promotionCondition").equals(PromotionConstant.PromotionIdCondition+"")){
					sql += andor+" p.prmtn_id like '%"+para.get("promotionConditionValue")+"%'";
				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.ArticleNoCondition+"")){
					sql +=  andor+" ( p.prmtn_oid in( select distinct  ap.prmtn_oid FROM artc_prmtn ap "
						+" where    ap.prmtn_oid = p.prmtn_oid  and  ap.artc_id  like '%"+para.get("promotionConditionValue")+"')"
						+" or"
						+" p.prmtn_oid in( select  distinct  psa.prmtn_oid  FROM prmtn_set_artc psa  "
						+" where  psa.prmtn_oid = p.prmtn_oid  and psa.artc_id like '%"+para.get("promotionConditionValue")+"')" 
						+" or  " 
						+" p.prmtn_oid in( select distinct hc.prmtn_oid from hier_cond  hc,  prod_hierarchy ph "
						+" where hc.prmtn_oid = p.prmtn_oid and hc.prod_hier_oid = ph.prod_hier_oid and ph.prod_hier_lv ='"+Constant.ProductHierarchyType.ARTICLE+"'" 
						+" and hc.prod_hier_id  like '%"+para.get("promotionConditionValue")+"')"
 						+ " ) ";
				}
				
				if(all || para.get("promotionCondition").equals(PromotionConstant.MainUpcCondition+"")){
					sql += andor+" (p.prmtn_oid in (select  distinct  psa.prmtn_oid " 
						+" FROM prmtn_set_artc psa where psa.prmtn_oid = p.prmtn_oid and psa.main_upc ='"+para.get("promotionConditionValue")+"' )"
						+" or "
						+" p.prmtn_oid in( select distinct hc.prmtn_oid from hier_cond  hc,  prod_hierarchy ph "
						+" where hc.prmtn_oid = p.prmtn_oid and hc.prod_hier_oid = ph.prod_hier_oid and ph.prod_hier_lv ='"+Constant.ProductHierarchyType.SKU+"'" 
						+" and hc.prod_hier_id  like '%"+para.get("promotionConditionValue")+"%')"
					    + " )";
				}				
				
				if(all || para.get("promotionCondition").equals(PromotionConstant.VendorUpcCondition+"")){
					sql += andor+" ( p.prmtn_oid in(  select distinct ap.prmtn_oid from vendor_upc vu ,artc_prmtn ap where "
						    +" ap.prmtn_oid = p.prmtn_oid  and ap.artc_id = vu.artc_id and vu.vendor_upc like '%"+para.get("promotionConditionValue")+"')"
						    +" or"
						    +" p.prmtn_oid in(  select distinct psa.prmtn_oid from vendor_upc vu ,prmtn_set_artc psa "
						    +" where  psa.prmtn_oid = p.prmtn_oid   and vu.artc_id = psa.artc_id and vu.vendor_upc like '%"+para.get("promotionConditionValue")+"'))";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.Mch3Condition+"")){
					sql +=  andor+" p.prmtn_oid in( select distinct hc.prmtn_oid from hier_cond  hc,  prod_hierarchy ph "
						+" where hc.prmtn_oid = p.prmtn_oid and hc.prod_hier_oid = ph.prod_hier_oid and ph.prod_hier_lv ='"+Constant.ProductHierarchyType.MCH3+"'" +
								" and hc.prod_hier_id  like '%"+para.get("promotionConditionValue")+"%')";
  				}	 	
				if(all || para.get("promotionCondition").equals(PromotionConstant.Mch2Condition+"")){
					sql +=  andor+" p.prmtn_oid in( select distinct hc.prmtn_oid from hier_cond  hc,  prod_hierarchy ph "
						+" where hc.prmtn_oid = p.prmtn_oid and hc.prod_hier_oid = ph.prod_hier_oid and ph.prod_hier_lv ='"+Constant.ProductHierarchyType.MCH2+"'" +
						" and hc.prod_hier_id  like '%"+para.get("promotionConditionValue")+"%')";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.MchCondition+"")){
					sql +=  andor+" p.prmtn_oid in( select distinct hc.prmtn_oid from hier_cond  hc,  prod_hierarchy ph "
						+" where hc.prmtn_oid = p.prmtn_oid and hc.prod_hier_oid = ph.prod_hier_oid and ph.prod_hier_lv ='"+Constant.ProductHierarchyType.MCH1+"'" +
						" and hc.prod_hier_id  like '%"+para.get("promotionConditionValue")+"%')";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.McCondition+"")){
					sql +=  andor+" p.prmtn_oid in( select distinct hc.prmtn_oid from hier_cond  hc,  prod_hierarchy ph "
						+" where hc.prmtn_oid = p.prmtn_oid and hc.prod_hier_oid = ph.prod_hier_oid and ph.prod_hier_lv ='"+Constant.ProductHierarchyType.MC+"'" +
						" and hc.prod_hier_id  like '%"+para.get("promotionConditionValue")+"%')";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.BrandCondition+"")){
					sql += andor+" p.prmtn_oid in( "
						+ "select distinct bc.prmtn_oid from brand_cvrge bc  where bc.prmtn_oid = p.prmtn_oid and bc.brand_id ='"+para.get("promotionConditionValue")+"') ";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.VendorCondition+"")){
					sql += andor+" p.prmtn_oid in(  select distinct vc.prmtn_oid   from vendor_cvrge vc  "
						+" where vc.prmtn_oid  = p.prmtn_oid  and  vc.vendor_id  like '%"+para.get("promotionConditionValue")+"%'  )  ";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.TenderCondition+"")){
					sql += andor+"p.prmtn_oid in(  "
							+" select distinct tc.prmtn_oid  from tender_cvrge tc where tc.prmtn_oid  = p.prmtn_oid and tc.tender_id ='"+para.get("promotionConditionValue")+"')";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.CreditCardCondition+"")){
					sql +=  andor+" p.prmtn_oid in ( select distinct ccc.prmtn_oid  from cr_card_CVRGE  ccc ,cr_card  cc"
							+" where   ccc.prmtn_oid = p.prmtn_oid  and   ccc.cr_card_id = cc.cr_card_id  and cc.cr_card_nm like '%"+para.get("promotionConditionValue")+"%')";
  				}				
				if(all || para.get("promotionCondition").equals(PromotionConstant.LeasingCondition+"")){
					sql +=  andor+" p.prmtn_oid in ( select distinct tp.prmtn_oid FROM hire_purchase_comp  hp ,customer c  , tender_prmtn tp "
						+" where  tp.prmtn_oid = p.prmtn_oid and hp.cust_oid = c.cust_oid and hp.hire_purchase_comp_oid = tp.hire_purchase_comp_oid "
						+" and c.first_nm like '%"+para.get("promotionConditionValue")+"%')  ";
 				}		
 					sql+= " )";
   			}

			
			 
			sql += "order by p.prmtn_id";
			System.out.println("sql getPrmtnid :"+sql);
 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Prmtnid_Mapper()));
  			return list;
		}		
		class Prmtnid_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				 String obj = new String();
				 obj = rs.getString(1);
				return obj;
 			}
 		}
		
		
		public List getPromotion_Tier(String str){				
			List list = null;
			String sql = null;		
			try{
			sql =  " SELECT  pao.tier_optn_oid ,  pat.tier_val  ,pao.optn_num ,atyp.prmtn_artc_typ_id,atyp.prmtn_artc_typ_nm "
				+" ,  paoi.artc_dsc ,paoi.limit_min_val , paoi.is_this_artc ,paoi.artc_id ,paoi.dscnt_cond_desc ,paoi.dscnt_price_val"
				+" ,paoi.is_limit_max_val ,paoi.limit_max_val , paoi.is_vendor_coupon ,paoi.vendor_id,paoi.vendor_nm"
				+" ,paoi.is_cmkt ,paoi.qty,paoi.unit,paoi.is_recv_from_cs ,paoi.prmtn_artc_id " 
				+" ,(select a.artc_dsc from artc a where a.artc_id = paoi.prmtn_artc_id) as artc_dsc_hompro " 
				+" ,(SELECT  pa.prmtn_artc_dsc  FROM prmtn_artc pa where  pa.prmtn_artc_id = paoi.prmtn_artc_id) as prmtn_artc_dsc "
				+" FROM prmtn_artc_tier pat  , prmtn_artc_optn pao  ,prmtn_artc_optn_item paoi , prmtn_artc_typ atyp"
				+" where  pat.tier_oid  = pao.tier_oid  and pao.tier_optn_oid = paoi.tier_optn_oid"
				+" and paoi.prmtn_artc_typ_id = atyp.prmtn_artc_typ_id"
				+" and pat.prmtn_oid = '"+str+"'";
 			
 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Promotion_Tier_Mapper()));
 			if(list == null) list = new ArrayList();

			}catch(Exception e){
				e.printStackTrace();
			}
  			return list;
		}		
		class Promotion_Tier_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				RPTResult obj = new RPTResult();
				int columnIndex = 1;
				// tier_optn_oid
				 obj.setS1(rs.getString(columnIndex++));
				 //tier_val
				 obj.setS2(getCurrency(rs.getString(columnIndex++))); 				 
  				 //optn_num
 				 obj.setS3(rs.getString(columnIndex++));
// 				 /prmtn_artc_typ_id
				 obj.setS4(rs.getString(columnIndex++));
				 //prmtn_artc_typ_nm
				 obj.setS5(rs.getString(columnIndex++));
				 //artc_dsc
				 obj.setS6(rs.getString(columnIndex++));
				 // limit_min_val
				 obj.setS7(getCurrency(rs.getString(columnIndex++)));
				 //is_this_artc
				 obj.setS8(rs.getString(columnIndex++));
				 // artc_id
				 obj.setS9(rs.getString(columnIndex++));
				 //dscnt_cond_desc
				 obj.setS10(rs.getString(columnIndex++));
				 // dscnt_price_val
				 obj.setS11(rs.getString(columnIndex++));
				 if(obj.getS4()!= null && obj.getS4().trim().equals("001")
				    && obj.getS11()!= null && !obj.getS11().trim().equals("")){
					obj.setS11(getCurrency(new BigDecimal("100").multiply(new BigDecimal(obj.getS11())).toString()));
				 }				
				 
				 // is_limit_max_val
				 obj.setS12(rs.getString(columnIndex++));
				 // limit_max_val
				 obj.setS13(getCurrency(rs.getString(columnIndex++)));
				 // is_vendor_coupon
				 obj.setS14(rs.getString(columnIndex++));
				 // vendor_id
				 obj.setS15(rs.getString(columnIndex++));
				 // vendor_nm
				 obj.setS16(rs.getString(columnIndex++));
				 //is_cmkt
				 obj.setS17(rs.getString(columnIndex++));				 
				 //paoi.qty
				 obj.setS18(getCurrency(rs.getString(columnIndex++)));
				 //paoi.unit
				 obj.setS19(rs.getString(columnIndex++));
				 //paoi.is_recv_from_cs
				 obj.setS20(rs.getString(columnIndex++));
				 // prmtn_artc_id
				 obj.setS21(rs.getString(columnIndex++));
				 // artc_dsc_hompro
				 obj.setS22(rs.getString(columnIndex++));
				 //prmtn_artc_dsc
				 obj.setS23(rs.getString(columnIndex++));
				 
				 
				return obj;
 			}
 		}

		public String  getCurrency(String str){
			if( str != null){
			   str =  new DecimalFormat("#,###.##").format(new BigDecimal(str).doubleValue());
			}
			return str;
 		}
		
		public List getArtc_list(String str,String yn)  {			 
			List list = null;
			String sql = null;	
			if(yn == null)return null;
			try{
				if(yn!=null && yn.equals("N")){
 			  sql ="select  ap.artc_id , a.artc_dsc ,null as col1 , '' as col2 FROM artc_prmtn ap , artc a "  
 			      +" where  ap.artc_id = a.artc_id and  ap.prmtn_oid ='"+str+"'"  ;
				}
				else if (yn!=null && yn.equals("Y")){
		 		sql = "select psa.artc_id ,a.artc_dsc , psa.qty_in_set ,psa.unit  FROM prmtn_set_artc psa , artc a "
		 		+" where psa.artc_id = a.artc_id and  psa.prmtn_oid = '"+str+"'" ;
				}
			 
 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Artc_list_Mapper()));
 			if(list == null) list = new ArrayList();
			}catch(Exception e){
				e.printStackTrace();
			}

  			return list;
		}		
		class Artc_list_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				RPTResult obj = new RPTResult();
				int columnIndex = 1;
				 obj.setS1(rs.getString(columnIndex++));
				 obj.setS2(rs.getString(columnIndex++));
				 obj.setS3(rs.getString(columnIndex++));
				 obj.setS4(rs.getString(columnIndex++));
				return obj;
 			}
 		}
 
		public List getCr_card(String str)   {			 
			List list = null;
			String sql = null;
			try{
			sql = "  select distinct cc.cr_card_nm  ,ccr.credit_debit_type ,ccr.credit_card_type,ccr.credit_card_level "
			+"  from cr_card_CVRGE  ccc ,cr_card  cc , cr_card_range ccr "
			+"  where  ccc.cr_card_id = cc.cr_card_id "
			+"   and ccc.cr_card_range_oid = ccr.cr_card_range_oid"
			+"   and ccc.cr_card_id = ccr.cr_card_id"
			+"   and ccc.prmtn_oid ='"+str+"'"; 

 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Cr_card_Mapper()));
 			if(list == null) list = new ArrayList();

			}catch(Exception e){
				e.printStackTrace();
			}

  			return list;
		}		
		class Cr_card_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				RPTResult obj = new RPTResult();
				int columnIndex = 1;
				 obj.setS1(rs.getString(columnIndex++));
				 obj.setS2(rs.getString(columnIndex++));
				 obj.setS3(rs.getString(columnIndex++));
				 obj.setS4(rs.getString(columnIndex++));
				return obj;
 			}
 		}
		public List getVendor_cvr(String str){				

			List list = null;
			String sql = null;		
			try{
			sql = "select vc.vendor_id ,v.vendor_nm  from vendor_cvrge vc , vendor v  "
			  +" where vc.vendor_id = v.vendor_id  and  vc.prmtn_oid = '"+str+"'" ;

 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Vendor_cvr_Mapper()));
 			if(list == null) list = new ArrayList();

			}catch(Exception e){
				e.printStackTrace();
			}
  			return list;
		}		
		class Vendor_cvr_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				RPTResult obj = new RPTResult();
				int columnIndex = 1;
				 obj.setS1(rs.getString(columnIndex++));
				 obj.setS2(rs.getString(columnIndex++));
				return obj;
 			}
 		}

		public List getHierarchy_cvr(String str)  {			 
			List list = null;
			String sql = null;		
			try{
			sql = "SELECT hc.is_incld ,phd.prod_hier_nm  ," 
				//edit
				+" CASE WHEN LENGTH(hc.prod_hier_id) = 18 "
				+" THEN TO_NUMBER(hc.prod_hier_id)||'' "
				+" ELSE hc.prod_hier_id end as prod_hier_id ,"			
					//"hc.prod_hier_id 
				+" hc.prod_hier_dsc "
				+" FROM hier_cond hc ,prod_hierarchy phd where hc.prod_hier_oid  = phd.prod_hier_oid " 
				+" and  hc.prmtn_oid ='"+str+"'" ;

 			list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Hierarchy_Mapper()));
 			if(list == null) list = new ArrayList();

			}catch(Exception e){
				e.printStackTrace();
			}

  			return list;
		}		
		class Hierarchy_Mapper implements RowMapper{
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				RPTResult obj = new RPTResult();
				int columnIndex = 1;
				 obj.setS1(rs.getString(columnIndex++));
				 obj.setS2(rs.getString(columnIndex++));
				 obj.setS3(rs.getString(columnIndex++));
				 obj.setS4(rs.getString(columnIndex++));
				return obj;
 			}
 		}
		 //todsapon create for Start get promotion
		public List getAllPosPromotion(Date trnDate) {
			//rtrim (xmlagg (xmlelement (e, prmtn_oid || ',')).extract ('//text()').getclobval(), ',')
			String sql="select * from ( "
				+"select distinct a.prmtn_oid "
	            +"from promotion a, prmtn_artc_tier b, prmtn_artc_optn c, prmtn_artc_optn_item d "
	            +"where a.prmtn_oid = b.prmtn_oid "
	               +"and b.tier_oid = c.tier_oid "
	               +"and c.tier_optn_oid = d.tier_optn_oid "
	               +"and a.is_active = 'Y' "
	               +"and a.prmtn_typ_id IN ('ART','CAT','COR','TDR')"
	               +"and a.start_dt <=  to_date('"+sf.format(trnDate)+"','DD-MM-YYYY') "
	               +"and a.end_dt >=  to_date('"+sf.format(trnDate)+"','DD-MM-YYYY') "
	               +"and a.store_cvrge = 'A' "
	               +"and d.is_recv_from_cs = 'N' "
	               +"union all "
	               +"select distinct a.prmtn_oid "
	               +"from promotion a, prmtn_artc_tier b, prmtn_artc_optn c, prmtn_artc_optn_item d "
	               +"where a.prmtn_oid = b.prmtn_oid "
	               +"and b.tier_oid = c.tier_oid "
	               +"and c.tier_optn_oid = d.tier_optn_oid "
	               +"and a.is_active = 'Y' "
	               +"and a.prmtn_typ_id IN ('ART','CAT','COR','TDR') "
	               +"and a.start_dt <= to_date('"+sf.format(trnDate)+"','DD-MM-YYYY') "
	               +"and a.end_dt >= to_date('"+sf.format(trnDate)+"','DD-MM-YYYY') "
	               +"and a.store_cvrge = 'E' "
	               +"and a.prmtn_oid not in (select prmtn_oid from store_cvrge where store_id = '"+PosAppContext.CurrentStore.getStoreId()+"') "
	               +"and d.is_recv_from_cs = 'N' "
	               +"union all "
	               +"select distinct a.prmtn_oid "
	               +"from promotion a, prmtn_artc_tier b, prmtn_artc_optn c, prmtn_artc_optn_item d "
	               +"where a.prmtn_oid = b.prmtn_oid "
	               +"and b.tier_oid = c.tier_oid " 
	               +"and c.tier_optn_oid = d.tier_optn_oid "
	               +"and a.is_active = 'Y' "
	               +"and a.prmtn_typ_id IN ('ART','CAT','COR','TDR')"
	               +"and a.start_dt <= to_date('"+sf.format(trnDate)+"','DD-MM-YYYY') "
	               +"and a.end_dt >= to_date('"+sf.format(trnDate)+"','DD-MM-YYYY') "
	               +"and a.store_cvrge = 'O' "
	               +"and a.prmtn_oid in (select prmtn_oid from store_cvrge where store_id = '"+PosAppContext.CurrentStore.getStoreId()+"') "    
	               +"and d.is_recv_from_cs = 'N') as foo;";
			//System.out.println(sql);
			List res=getJdbcTemplate().query(sql,new RowMapper() {

				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					// TODO Auto-generated method stub
					return new Long(rs.getLong("prmtn_oid"));
				}
				
			});
			
			return res;
		}	
 }