package com.ie.icon.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.dao.CollectSalesOrderJdbcDao;
import com.ie.icon.domain.so.CollectSalesOrder;

public class JdbcCollectSalesOrderDao extends JdbcDaoSupport implements CollectSalesOrderJdbcDao{
	
	private SimpleDateFormat sf;
	public JdbcCollectSalesOrderDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public boolean updateCollectSalesOrder(Long collSaleOrderOid,String salesOrderStsId,Timestamp lastUpdateDate , String lastUpdateBy){
		
//		String sqlString = "UPDATE COLL_SALES_ORDER SET SALES_ORDER_STS_ID = ? , LAST_UPD_DTTM = ?, LAST_UPD_USER = ? WHERE COLL_SALES_ORDER_OID = ?";
		String sqlString = "UPDATE COLL_SALES_ORDER SET SALES_ORDER_STS_ID = ? WHERE COLL_SALES_ORDER_OID = ?";
		JdbcTemplate jt = getJdbcTemplate();
//		Object[] args = new Object[] {salesOrderStsId,lastUpdateDate,lastUpdateBy,collSaleOrderOid};
		Object[] args = new Object[] {salesOrderStsId,collSaleOrderOid};
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
}
