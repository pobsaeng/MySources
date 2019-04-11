package com.ie.icon.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.ArticleJdbcDao;
import com.ie.icon.domain.ArticlePricingGroup;
import com.ie.icon.domain.ArticleVendorSku;
import com.ie.icon.domain.mch.Article;
import com.ie.icon.domain.mch.ArticlePricing;

public class JdbcArticleDao extends JdbcDaoSupport implements ArticleJdbcDao {

	public List getArticleStore(String articleName, String brandName,
			String vendorId) {
		
		String sql = "select artc_id from artc where ";
		if (articleName.equals("*")) {
			boolean brand =false;
			if(brandName!=null&&brandName.trim().length()>0){
				sql += " brand = '"+brandName+"' "; 
				brand = true;
			}
			if(vendorId!=null && vendorId.trim().length()>0){
				if(brand){
					sql+= " and ";
				}
				sql += " vendor_id = '"+vendorId+"' ";
			}
				sql += "order by artc_dsc";
		}else{
			sql += " upper(artc_dsc) like upper('%"+ articleName+"%') ";
			if(brandName!=null&&brandName.trim().length()>0){
				sql += "and brand = '"+brandName+"' "; 
			}		
			if(vendorId!=null&&vendorId.trim().length()>0){
				sql += "and vendor_id = '"+vendorId+"' ";
			}
				sql += "order by artc_dsc";
		}	
		List list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Article_Mapper()));
			
		return list;
	}
	
	
	class Article_Mapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			String articleId = "";
			return articleId = rs.getString(1);
			
				 
			}
		}
	public HashMap getArticleValuationClass(List articleNo) {
		
		String condition = new String();
		for(int i=0;i<articleNo.size();i++){
			String artId = (String)articleNo.get(i);
			
			condition += "'"+artId+"'";
			if(i<articleNo.size()-1){
				condition +=",";
			}
		}
		String sql = "select artc_id , decode(valuation_class,'Z200','PAS','NORMAL') as valuation_class from artc  where ";
		sql += " artc_id in ("+condition+")"; 
		
		System.out.println("sql :: "+sql);
		List list = getJdbcTemplate().query(sql,new RowMapperResultReader(new Article_Valuation_Class_Mapper()));
		HashMap artcMap = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Article artc = (Article) list.get(i);
			artcMap.put(artc.getArticleId(), artc.getValuationClass());
		}
		
			
		return artcMap;
	}
	
	class Article_Valuation_Class_Mapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			Article artc = new Article();
			artc.setArticleId(rs.getString(1));
			artc.setValuationClass(rs.getString(2));
			return artc;
			
				 
			}
		}
	
	public ArticleVendorSku getSkuBookArticle(String article){
		StringBuffer sql = new StringBuffer();
		sql.append(" select to_number(a.artc_id) artc_id, a.artc_dsc description,a.is_active status,a.mch_id mc, a.brand, ");
		sql.append(" v.vendor_id || ' : '||v.vendor_nm vendor,a.vendor_partno vendor_part ");
		sql.append(" from artc_in_store ais inner join artc a on (ais.artc_id=a.artc_id) ");
		sql.append(" left join vendor v on ais.vendor_id = v.vendor_id ");
		sql.append(" where ais.artc_id = '").append(article).append("' ");
		
		System.out.println("sql :: "+sql.toString());
		
		List list = getJdbcTemplate().query(sql.toString(),
				new RowMapperResultReader(new SkuBookMapper()));
		
		if(list!=null&&list.size()>0){
			return (ArticleVendorSku) list.get(0);
		}
		
		return null;
	}
	
	class SkuBookMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ArticleVendorSku artc = new ArticleVendorSku();
			artc.setArtc_id(rs.getString("artc_id"));
			artc.setDescription(rs.getString("description"));
			artc.setStatus(rs.getString("status"));
			artc.setMc(rs.getString("mc"));
			artc.setBrand(rs.getString("brand"));
			artc.setVendor(rs.getString("vendor"));
			artc.setVendorPart(rs.getString("vendor_part"));
			return artc; 
		}
	}
	
	//...Boizz(+) : Find Article Pricing for Stock Tile
	
	
	class Article_Pricing_Class_Mapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ArticlePricing articlePricing = null;
			
			if(rs.getBigDecimal("unit_price") != null) {
				articlePricing = new ArticlePricing();
				articlePricing.setSellUnit(rs.getString("sell_unit"));
				articlePricing.setUnitPrice(rs.getBigDecimal("unit_price"));
				articlePricing.setCreateDate(rs.getDate("create_dttm"));
				articlePricing.setStartDate(rs.getDate("start_dt"));
				articlePricing.setEndDate(rs.getDate("end_dt"));
			}
				return articlePricing;
			}
	}
	
	public ArticlePricingGroup getArticlePricing(String storeId, String articleId, String sellUnit) {
		ArticlePricingGroup apg = new ArticlePricingGroup();
		
		ArticlePricing normalPrice = getPrice(storeId, articleId, sellUnit, Constant.PriceConditionType.NORMAL_PRICE);
		if (normalPrice != null) {
			apg.setNormalPrice(normalPrice);
		}
		
		ArticlePricing promotionPrice = getPrice(storeId, articleId, sellUnit, Constant.PriceConditionType.PROMOTION_PRICE);
		if (promotionPrice != null) {
			apg.setPromotionPrice(promotionPrice);
		}
		
		return apg;
	}
	
	private ArticlePricing getPrice(String storeId, String articleId, String sellUnit, String priceConditionType) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select store_id, sell_unit, unit_price, ap.create_dttm, ap.start_dt, ap.end_dt ");
		sql.append(" from artc_pricing ap ");
		sql.append(" where ap.artc_id   = '").append(articleId).append("' ");
		sql.append(" and ap.sell_unit = '").append(sellUnit).append("' ");
		sql.append(" and ap.price_cond_typ_id = '").append(priceConditionType).append("' ");
		sql.append(" and ap.start_dt <= sysdate and ap.end_dt >= sysdate ");
		sql.append(" and ap.store_id  = '").append(storeId).append("' ");
		sql.append(" and rownum = 1 ");
		sql.append(" order by ap.create_dttm desc ");
		
		//System.out.println("--- articleId          :: " + articleId);
		//System.out.println("--- priceConditionType :: " + priceConditionType);
		//System.out.println("--- SQL >> getArticlePricing() :: "+sql.toString());
		
		try {
			List list = getJdbcTemplate().query(sql.toString(),
					new RowMapperResultReader(new Article_Pricing_Class_Mapper()));
			
			if (list != null && list.size()>0){
				return (ArticlePricing) list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//...End(+)
}
