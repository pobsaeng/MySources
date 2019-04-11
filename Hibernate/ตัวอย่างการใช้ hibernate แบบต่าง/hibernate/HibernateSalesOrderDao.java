package com.ie.icon.dao.hibernate;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.common.util.DateTimeUtil;
import com.ie.icon.constant.Constant;
import com.ie.icon.constant.SalesOrderConstant;
import com.ie.icon.dao.SalesOrderDao;
import com.ie.icon.domain.mch.Article;
import com.ie.icon.domain.mch.ArticleInStore;
import com.ie.icon.domain.mch.MainUPC;
import com.ie.icon.domain.sale.DiscountConditionType;
import com.ie.icon.domain.sale.PriceConditionType;
import com.ie.icon.domain.sale.TaxInvoice;
import com.ie.icon.domain.sale.TaxInvoiceItem;
import com.ie.icon.domain.so.DsPatTime;
import com.ie.icon.domain.so.DsPatTimeDet;
import com.ie.icon.domain.so.DsSpecWork;
import com.ie.icon.domain.so.DsVendor;
import com.ie.icon.domain.so.DsVendorBrand;
import com.ie.icon.domain.so.DsVendorGroup;
import com.ie.icon.domain.so.Installer;
import com.ie.icon.domain.so.JobProductInstall;
import com.ie.icon.domain.so.JobProductType;
import com.ie.icon.domain.so.POType;
import com.ie.icon.domain.so.SalesOrder;
import com.ie.icon.domain.so.SalesOrderGroup;
import com.ie.icon.domain.so.SalesOrderItem;
import com.ie.icon.domain.so.SalesOrderSetItem;
import com.ie.icon.domain.so.SalesOrderStatus;
import com.ie.icon.domain.so.SalesOrderType;
import com.ie.icon.domain.so.ShippingPoint;

public class HibernateSalesOrderDao extends HibernateCommonDao implements SalesOrderDao {

	public void create(SalesOrder salesOrder) throws DataAccessException {
		getHibernateTemplate().save(salesOrder);
		System.out.println("########## Create : SalesOrder ########## ");
	}

	public void update(SalesOrder salesOrder) throws DataAccessException {
		getHibernateTemplate().update(salesOrder);
	}
	
	public void saveOrUpdate(SalesOrder salesOrder) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(salesOrder);
	}	 
	
	public void delete(List salesOrdeList)throws DataAccessException{
		for(int i=0;i<salesOrdeList.size();i++){
			SalesOrder salesOrders = (SalesOrder)salesOrdeList.get(i);
			getHibernateTemplate().delete(salesOrders);      
		}
	}
	
	public void updateStatus(SalesOrder salesOrder) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("salesOrderNo", salesOrder.getSalesOrderNo()));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", salesOrder.getStore().getStoreId()));
		if (salesOrder.getTransactionDate() != null) {
			Calendar toDate = Calendar.getInstance();
			toDate.setTime(salesOrder.getTransactionDate());
			toDate.add(Calendar.DATE, 1);
			criteria.add(Restrictions.between("transactionDate", salesOrder.getTransactionDate(), toDate.getTime()));
		}    

		List list = getHibernateTemplate().findByCriteria(criteria);
		SalesOrderStatus  salesOrderStatus = new SalesOrderStatus();
		salesOrderStatus.setSalesOrderStatusId(SalesOrderConstant.CANCELLED_STATUS);
		
		for(int i=0;i<list.size();i++){
			SalesOrder salesOrders = (SalesOrder)list.get(i);
			salesOrders.setSalesOrderStatus(salesOrderStatus);
			salesOrders.setLastUpdateDate(DateTimeUtil.getCurrentDateTime());
			getHibernateTemplate().update(salesOrders);
		}
	}
	
	public void update(List salesOrdeList,String status) throws DataAccessException {
		SalesOrderStatus  salesOrderStatus = new SalesOrderStatus();
		if(status!=null){
			salesOrderStatus.setSalesOrderStatusId(status);
		}
		
		for(int i=0;i<salesOrdeList.size();i++){
			SalesOrder salesOrders = (SalesOrder)salesOrdeList.get(i);
			salesOrders.setSalesOrderStatus(salesOrderStatus);
			salesOrders.setLastUpdateDate(DateTimeUtil.getCurrentDateTime());
			getHibernateTemplate().update(salesOrders);
		}
	}
	
	public void update(SalesOrderGroup salesOrderGroup) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderGroup.class);
		criteria.add(Restrictions.eq("salesOrder", new Long(salesOrderGroup.getSalesOrder().getObjectId())));
		criteria.add(Restrictions.eq("shippingPointId", salesOrderGroup.getShippingPointId()));
		criteria.add(Restrictions.eq("objectId", new Long(salesOrderGroup.getObjectId())));

		getHibernateTemplate().update(salesOrderGroup);
	}

	public void delete(Long objectId) throws DataAccessException {
		SalesOrder salesOrder = (SalesOrder) getHibernateTemplate().load(SalesOrder.class, objectId);
		getHibernateTemplate().delete(salesOrder);
	}

	public void delete(String salesOrderNo, String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		List list = getHibernateTemplate().findByCriteria(criteria);
		getHibernateTemplate().deleteAll(list);
	}

	public void createOrUpdate(SalesOrder salesOrder) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(salesOrder);
	}

	public DiscountConditionType getDiscountConditionType(String discountConditionTypeId) throws DataAccessException {
		return (DiscountConditionType) getHibernateTemplate().get(DiscountConditionType.class, discountConditionTypeId);
	}

	public List getDiscountConditionTypes(int discountType, boolean isPercentDiscount) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
		criteria.add(Restrictions.eq("discountType", new Integer(discountType)));
		criteria.add(Restrictions.eq("isPercentDiscount", new Boolean(isPercentDiscount)));
		criteria.add(Restrictions.eq("isSo", new Boolean(true)));
		criteria.addOrder(Order.asc("discountConditionTypeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getDistricts(String shippingPointId, String provinceId) throws DataAccessException {
		String query = "select distinct district from DsDistrict district, ProvinceDistrict pd where district.districtId = pd.provinceDistrictId.districtId and " + "pd.provinceDistrictId.shippingPointId = :shippingPointId and " + "pd.provinceDistrictId.provinceId = :provinceId order by district.description";
		return getHibernateTemplate().findByNamedParam(query, new String[]{"shippingPointId", "provinceId"}, new Object[]{shippingPointId, provinceId});
	}
		
	public List getDistrictsByVendor(String shippingPointId, String provinceId,String vendorId) throws DataAccessException {
		String query = "select distinct district from DsDistrict district, ProvinceDistrict pd where district.districtId = pd.provinceDistrictId.districtId and " + 
		"pd.provinceDistrictId.shippingPointId = :shippingPointId and " + 
		"pd.provinceDistrictId.provinceId = :provinceId and " + 
		"pd.provinceDistrictId.data1 = :vendorId " +
		"order by district.description";
		return getHibernateTemplate().findByNamedParam(query, new String[]{"shippingPointId", "provinceId","vendorId"}, new Object[]{shippingPointId, provinceId,vendorId});
	}
	public List getInstallers() throws DataAccessException {
		return getHibernateTemplate().loadAll(Installer.class);   
	}

	public List getJobTypes(String shippingPointId) throws DataAccessException {
		String query = "select distinct jobType from JobType jobType, JobProductType jpt where jobType.jobTypeId = jpt.jobProductTypeId.jobTypeId and " + "jpt.jobProductTypeId.shippingPointId = :shippingPointId and jpt.status = 'Y' order by jobType.jobTypeId";
		return getHibernateTemplate().findByNamedParam(query, "shippingPointId", shippingPointId);
	}

	public List getJobTypesCN(String shippingPointId) throws DataAccessException {
		String query = "select distinct jobType from JobType jobType, JobProductType jpt where jobType.jobTypeId = jpt.jobProductTypeId.jobTypeId and " + "jpt.jobProductTypeId.shippingPointId = :shippingPointId and jobType.jobTypeId like 'C%' and jpt.status = 'Y' order by jobType.jobTypeId";
		return getHibernateTemplate().findByNamedParam(query, "shippingPointId", shippingPointId);
	}

	public List getJobTypesNotInstall(String shippingPointId) throws DataAccessException {
		String query = "select distinct jobType from JobType jobType, JobProductType jpt where jobType.jobTypeId = jpt.jobProductTypeId.jobTypeId and " + "jpt.jobProductTypeId.shippingPointId = :shippingPointId and jpt.status = 'Y' and jpt.jobProductTypeId.jobTypeId != 'I' order by jobType.jobTypeId";
		return getHibernateTemplate().findByNamedParam(query, "shippingPointId", shippingPointId);
	}

	public List getMainProductTypes(String shippingPointId, String jobTypeId) throws DataAccessException {
		String query = "select distinct mainProductType from MainProductType mainProductType, JobProductType jpt where mainProductType.mainProductTypeId = jpt.jobProductTypeId.mainProductTypeId and " + "jpt.jobProductTypeId.shippingPointId = :shippingPointId and " + "jpt.jobProductTypeId.jobTypeId = :jobTypeId and jpt.status = 'Y' order by mainProductType.description";

		return getHibernateTemplate().findByNamedParam(query, new String[]{"shippingPointId", "jobTypeId"}, new Object[]{shippingPointId, jobTypeId});
	}

	public List getMainProductTypesVendor(String shippingPointId, String jobTypeId, String data1) throws DataAccessException {
		String query = "select distinct mainProductType from MainProductType mainProductType, JobProductType jpt,DsVendor vendor where mainProductType.mainProductTypeId = jpt.jobProductTypeId.mainProductTypeId and vendor. vendorId = jpt.jobProductTypeId.data1 and " + "jpt.jobProductTypeId.shippingPointId = :shippingPointId and " + "jpt.jobProductTypeId.data1 = :data1 and " + "jpt.jobProductTypeId.jobTypeId = :jobTypeId and jpt.status = 'Y' order by mainProductType.description";

		return getHibernateTemplate().findByNamedParam(query, new String[]{"shippingPointId", "jobTypeId", "data1"}, new Object[]{shippingPointId, jobTypeId, data1});
	}

	public List getProvinces(String shippingPointId, String data1) throws DataAccessException {
		String query = "select distinct province from DsProvince province, ProvinceDistrict pd where province.provinceId = pd.provinceDistrictId.provinceId and " + "pd.provinceDistrictId.shippingPointId = :shippingPointId and pd.provinceDistrictId.data1 = :data1 and pd.status = 'Y' order by province.description";

		return getHibernateTemplate().findByNamedParam(query, new String[]{"shippingPointId", "data1"}, new Object[]{shippingPointId, data1});
	}

	public List getSalesOrderTypes() throws DataAccessException {
		return getHibernateTemplate().loadAll(SalesOrderType.class);
	}

	public List getShippingPoints() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ShippingPoint.class);
		criteria.addOrder(Order.asc("shippingPointId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getTimeFrames(String shippingPointId) throws DataAccessException {
		String query = "select timeframe from DsTimeFrame timeframe, ShippingTimeFrame stf where timeframe.timeFrameId = stf.shippingTimeFrameId.timeFrameId and " + "stf.shippingTimeFrameId.shippingPointId = :shippingPointId and stf.status = 'Y' order by timeframe.timeFrameId";
		return getHibernateTemplate().findByNamedParam(query, "shippingPointId", shippingPointId);
	}

	public List getVendors(String vendorName, String brand) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorBrand.class);
		criteria.createAlias("vendor", "vendor");
		criteria.add(Restrictions.eq("status", new Character('Y')));

		if (vendorName != null && !"".equals(vendorName)) {
			criteria.add(Restrictions.ilike("vendor.description", replace(vendorName)));
		} else if (brand != null && !"".equals(brand)) {
			criteria.add(Restrictions.ilike("id.brand", replace(brand)));
		}

		criteria.addOrder(Order.asc("vendor.description"));
		criteria.addOrder(Order.asc("id.brand"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getVendors(String vendorName) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendor.class);
		criteria.add(Restrictions.eq("status", new Character('Y')));

		if (vendorName != null && !"".equals(vendorName)) {
			criteria.add(Restrictions.ilike("description", vendorName));
		}
		criteria.addOrder(Order.asc("description"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPoTypes() throws DataAccessException {
		return getHibernateTemplate().loadAll(POType.class);
	}

	public List getDiscountConditionTypes(boolean isSo, boolean isItem) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
		criteria.add(Restrictions.eq("isSo", new Boolean(true)));
		if (isItem) {
			criteria.add(Restrictions.like("discountConditionTypeId", Constant.Reason.YI, MatchMode.START));
			criteria.add(Restrictions.ne("discountType", new Integer(Constant.DiscountConditionType.ITEM_AUTO)));
			criteria.add(Restrictions.ne("discountType", new Integer(Constant.DiscountConditionType.HEADER_AUTO)));
			criteria.add(Restrictions.ne("discountType", new Integer(Constant.DiscountConditionType.ROUND_UP_DIFF)));
		} else {
			criteria.add(Restrictions.like("discountConditionTypeId", Constant.Reason.YH, MatchMode.START));
			criteria.add(Restrictions.ne("discountType", new Integer(Constant.DiscountConditionType.ITEM_AUTO)));
			criteria.add(Restrictions.ne("discountType", new Integer(Constant.DiscountConditionType.HEADER_AUTO)));
			criteria.add(Restrictions.ne("discountType", new Integer(Constant.DiscountConditionType.ROUND_UP_DIFF)));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getSalesOrders(String salesOrderNo, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		if (trnDate != null) {
			criteria.add(Restrictions.eq("transactionDate", trnDate));
		}    
		criteria.add(Restrictions.isNull("quotationId"));
		criteria.addOrder(Order.desc("salesOrderNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() > 0) {    
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);   
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
		}
		return list;
	}
	
	public List getSalesOrders(String salesOrderNo,String storeId, Date trnDate,String status ) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));

		if (trnDate != null) {
			Calendar toDate = Calendar.getInstance();
			toDate.setTime(trnDate);
			toDate.add(Calendar.DATE, 1);
			criteria.add(Restrictions.between("transactionDate", trnDate, toDate.getTime()));
		}    
		
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}

		criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", status));
		criteria.addOrder(Order.desc("salesOrderNo"));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if (list != null && list.size() > 0) {    
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);   
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
		}
		return list;
	}
	
	public SalesOrder getSalesOrderCredit(String storeid, String saleOrderNo, Date trnDate) throws DataAccessException{
		System.out.println("**************** HibernateSalesOrder *****************");
		System.out.println("************ storeid ************" + storeid);
		System.out.println("************ saleOrderNo ************" + saleOrderNo);
		System.out.println("************ trnDate ************" + trnDate);
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		if (storeid != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeid));
		}
		
		criteria.add(Restrictions.eq("salesOrderNo", saleOrderNo));
		
		if (trnDate != null) {
			Calendar toDate = Calendar.getInstance();
			toDate.setTime(trnDate);
			toDate.add(Calendar.DATE, 1);
			criteria.add(Restrictions.between("transactionDate", trnDate, toDate.getTime()));
		}
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		System.out.println("************ list ************" + list.size());
		
		SalesOrder salesOrder = new SalesOrder();
		if (list != null && list.size() > 0) {    
			for (int i = 0; i < list.size(); i++) {
				salesOrder = (SalesOrder) list.get(i);   
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
		}
		else{
			return null;
		}
		
		return salesOrder;
	}
	
	public List getSalesOrderPeriod(String startSalesOrderNo,String endSalesOrderNo,Date transactionDate,String salesOrderTypeId,String[] salesOrderExcept) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		if (startSalesOrderNo != null && !startSalesOrderNo.equals("")){
			criteria.add(Restrictions.ge("salesOrderNo", startSalesOrderNo));
		}
		if(endSalesOrderNo != null && !endSalesOrderNo.equals("")){
			criteria.add(Restrictions.le("salesOrderNo", endSalesOrderNo));
		}
		
		if (transactionDate != null) {
			Calendar toDate = Calendar.getInstance();
			toDate.setTime(transactionDate);
			toDate.add(Calendar.DATE, 1);
			criteria.add(Restrictions.between("transactionDate", transactionDate, toDate.getTime()));
		}       
		  
		if(salesOrderExcept != null && salesOrderExcept.length > 0){   
			criteria.add(Restrictions.not(Restrictions.in("salesOrderNo", salesOrderExcept)));
		}
		
		if(salesOrderTypeId != null && !"".equals(salesOrderTypeId)){
			criteria.createAlias("salesOrderType","salesOrderType");
			criteria.add(Restrictions.eq("salesOrderType.salesOrderTypeId", salesOrderTypeId));
		}
		
		criteria.createAlias("salesOrderStatus", "salesOrderStatus");
		criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", SalesOrderConstant.CREATED_STATUS));
		
		criteria.addOrder(Order.asc("salesOrderNo"));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() > 0) {    
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
			
		}
		return list;
	}
	
	
	public List getSalesOrders(String salesOrderNo, String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		criteria.addOrder(Order.desc("salesOrderNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list.size() > 0) {
			SalesOrder salesOrder = (SalesOrder) list.get(0);
			getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
			getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
			for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
				SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
				getHibernateTemplate().initialize(group.getSalesOrderItems());
			}
			for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
				SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
				getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
			}
			return list;
		} else {
			return null;
		}

	}
	
	public List getSalesOrdersBySales(String salesOrderNo, String storeId, Date trDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));

		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		criteria.add(Restrictions.eq("transactionDate", trDate));

		criteria.addOrder(Order.desc("salesOrderNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list.size() > 0) {
			SalesOrder salesOrder = (SalesOrder) list.get(0);

			getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
			getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());

			for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
				SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
				getHibernateTemplate().initialize(group.getSalesOrderItems());
			}

			for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
				SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
				getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
			}

			return list;
		} else {
			return null;
		}

	}
	
	public List getSalesOrders(String salesOrderNo, String storeId,String presalesStatus) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
    
		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));

		if (storeId != null) {
			criteria.createAlias("store", "store");    
			criteria.add(Restrictions.eq("store.storeId", storeId));   
		}
		   
		if(presalesStatus != null){
			criteria.createAlias("salesOrderStatus", "salesOrderStatus");
			criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", presalesStatus));
		}

		criteria.addOrder(Order.asc("salesOrderNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if (list.size() > 0) {
			SalesOrder salesOrder = (SalesOrder) list.get(0);

			getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
			getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
			// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());

			for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
				SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
				getHibernateTemplate().initialize(group.getSalesOrderItems());
			}

			for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
				SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
				getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
			}

			return list;
		} else {
			return null;
		}

	}
	
	public List getSalesOrders(String salesOrderNo, String storeId, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));

		if (trnDate != null) {
			Calendar toDate = Calendar.getInstance();  
			toDate.setTime(trnDate);
			toDate.add(Calendar.DATE, 1);
			criteria.add(Restrictions.between("transactionDate", trnDate, toDate.getTime()));
		}
   
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}

		criteria.addOrder(Order.desc("salesOrderNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list.size() > 0) {
			SalesOrder salesOrder = (SalesOrder) list.get(0);

			getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
			getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
			// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());

			for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
				SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
				getHibernateTemplate().initialize(group.getSalesOrderItems());
			}

			for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
				SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
				getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
			}

			return list;
		} else {   
			return null;
		}

	}
	
	public List getSalesOrderRefund(String salesOrderNo, String storeId, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		if(trnDate != null){
			criteria.add(Restrictions.eq("transactionDate", trnDate));  
		}
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}

		criteria.addOrder(Order.desc("salesOrderNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list.size() > 0) {
			SalesOrder salesOrder = (SalesOrder) list.get(0);

			getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
			getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
			// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());

			for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
				SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
				getHibernateTemplate().initialize(group.getSalesOrderItems());
			}

			for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
				SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
				getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
			}

			return list;
		} else {   
			return null;
		}
	}
	
	public List getSalesOrdersByKey(String salesOrderNo, String storeId, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));

		if (trnDate != null) {
			criteria.add(Restrictions.eq("transactionDate", trnDate));   
		}
   
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));   
		}

		criteria.addOrder(Order.asc("salesOrderNo"));   
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				SalesOrder salesOrder = (SalesOrder) list.get(j);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());
				for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
			return list;
		} else {
			return null;
		}

	}   
	
	
	public List getSalesOrdersByKeyAndStatus(String salesOrderNo, String storeId, Date trnDate , String status) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		if (salesOrderNo != null)
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));

		if (trnDate != null) {
			criteria.add(Restrictions.eq("transactionDate", trnDate));   
		}
   
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));   
		}
		
		if(status != null){
			criteria.createAlias("salesOrderStatus", "salesOrderStatus");
			criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", status));	
		}

		criteria.addOrder(Order.asc("salesOrderNo"));   
		List list = getHibernateTemplate().findByCriteria(criteria);  
		if (list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				SalesOrder salesOrder = (SalesOrder) list.get(j);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());
				for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
			return list;
		} else {
			return null;
		}

	}   

	public SalesOrder getSalesOrdersUpdateStatus(String storeid, String salesOrderNo) throws DataAccessException {
		
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		criteria.addOrder(Order.asc("salesOrderNo"));

		try{
			List list = getHibernateTemplate().findByCriteria(criteria);
			if (list.size() > 0) {
				SalesOrder salesOrder = (SalesOrder) list.get(0);
				try {
					if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
						getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());
				if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
					for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
						SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
						getHibernateTemplate().initialize(group.getSalesOrderItems());
					}
				}
	
				for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
				
				//update status
				int count = 0;
				for(int i=0;i<list.size();i++){
					SalesOrder sales = (SalesOrder)list.get(i);
					if(sales.getSalesOrderStatus().equals(SalesOrderConstant.COLL_STATUS)){
						count++;
					}
				}
		
				return salesOrder;
			} else {
				return null;
			}
		}catch(Exception e){
			return null;
		}
	
	}

	public SalesOrder getSalesOrder(String storeid, String salesOrderNo) throws DataAccessException {
		
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		criteria.addOrder(Order.asc("salesOrderNo"));

		try{
			List list = getHibernateTemplate().findByCriteria(criteria);
			if (list.size() > 0) {
				SalesOrder salesOrder = (SalesOrder) list.get(0);
				try {
					if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
						getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());
				if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
					for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
						SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
						getHibernateTemplate().initialize(group.getSalesOrderItems());
					}
				}
	
				for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
				return salesOrder;
			} else {
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}
	
	public SalesOrder getSalesOrder(String storeid, String salesOrderNo,Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		criteria.add(Restrictions.eq("transactionDate", trnDate));   
		criteria.createAlias("salesOrderStatus", "salesOrderStatus");
		criteria.add(Restrictions.ne("salesOrderStatus.salesOrderStatusId", Constant.SaleOrderMessage.CANCELEDSTS));
		criteria.addOrder(Order.asc("salesOrderNo"));
		try
		{
			List list = getHibernateTemplate().findByCriteria(criteria);
			if (list.size() > 0) {
				SalesOrder salesOrder = (SalesOrder) list.get(0);
				try {
					if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
						getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
					for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
						SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
						getHibernateTemplate().initialize(group.getSalesOrderItems());
					}
				}
				for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
				return salesOrder;
			} else {
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}

	public SalesOrder getSalesOrder(long objectId) throws DataAccessException {
		SalesOrder salesOrder = (SalesOrder) getHibernateTemplate().get(SalesOrder.class, new Long(objectId));

		if (salesOrder == null)
		{
			return null;
		}
			
		getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
		getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());

		for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
			SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
			getHibernateTemplate().initialize(group.getSalesOrderItems());
		}
		for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
			SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
			getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
		}
		return salesOrder;
	}

	public SalesOrderStatus getSalesOrderStatus(String salesOrderStatusId) throws DataAccessException {
		return (SalesOrderStatus) getHibernateTemplate().get(SalesOrderStatus.class, salesOrderStatusId);
	}

	public DsVendorGroup getVendorGroup(String vdno, String hpvdno) throws DataAccessException {
		return (DsVendorGroup) getHibernateTemplate().get(DsVendorGroup.class, new DsVendorGroup.Id(vdno, hpvdno));
	}

	public PriceConditionType getPriceConditionType(String priceConditionTypeId) throws DataAccessException {
		return (PriceConditionType) getHibernateTemplate().get(PriceConditionType.class, priceConditionTypeId);
	}  

	public List getSalesOrders(String storeId, String presalesType, String presalesStatus, Date fromDate, Date toDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		if (storeId != null && !"".equals(storeId.trim())) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		if (presalesType != null && !"".equals(presalesType.trim())) {
			criteria.createAlias("salesOrderType", "salesOrderType");
			criteria.add(Restrictions.eq("salesOrderType.salesOrderTypeId", presalesType));
		}
		if (presalesStatus != null && !"".equals(presalesStatus.trim())) {
			criteria.createAlias("salesOrderStatus", "salesOrderStatus");
			criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", presalesStatus));
		}
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(toDate);
		endDate.add(Calendar.DATE, 1);
		criteria.add(Restrictions.between("createDateTime", fromDate, endDate.getTime()));
		criteria.addOrder(Order.desc("salesOrderNo"));

		List ret = getHibernateTemplate().findByCriteria(criteria);
		if (ret != null) {
			/** initial item */
			for (int index = 0; index < ret.size(); index++) {
				SalesOrder salesOrder = (SalesOrder) ret.get(index);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
			}
		}
		return ret;
	}

	public List getSalesOrderItems(String storeId, String presalesType, String presalesStatus, Date fromDate, Date toDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderItem.class);

		criteria.createAlias("salesOrder", "salesOrder");
		if (storeId != null) {
			criteria.createAlias("salesOrder.store", "store");
			criteria.add(Restrictions.eq("salesOrder.store.storeId", storeId));
		}
		if (presalesType != null && !"".equals(presalesType.trim())) {
			criteria.createAlias("salesOrder.salesOrderType", "salesOrderType");
			criteria.add(Restrictions.eq("salesOrder.salesOrderType.salesOrderTypeId", presalesType));
		}
		if (presalesStatus != null && !"".equals(presalesStatus.trim())) {
			criteria.createAlias("salesOrder.salesOrderStatus", "salesOrderStatus");
			criteria.add(Restrictions.eq("salesOrder.salesOrderStatus.salesOrderStatusId", presalesStatus));
		}
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(toDate);
		endDate.add(Calendar.DATE, 1);
		criteria.add(Restrictions.between("salesOrder.createDateTime", fromDate, endDate.getTime()));

		criteria.addOrder(Order.desc("salesOrder.salesOrderNo"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getSalesOrderItem(String storeId, String tenderOrPriceGroup, Date fromDate, Date toDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderItem.class);

		criteria.add(Restrictions.eq("isDownPayment", new Boolean(false)));

		criteria.createAlias("salesOrder", "salesOrder");
		criteria.add(Restrictions.between("salesOrder.transactionDate", fromDate, toDate));
		if (storeId != null && !"".equals(storeId.trim())) {
			criteria.createAlias("salesOrder.store", "store");
			criteria.add(Restrictions.eq("salesOrder.store.storeId", storeId));
		}
		
//		if (tenderOrPriceGroup != null && !"".equals(tenderOrPriceGroup.trim())) {
//			criteria.createAlias("salesOrder.hpcTender", "hpcTender");
//			criteria.add(Restrictions.or(Restrictions.eq("salesOrder.hpcTender.tenderId", tenderOrPriceGroup), Restrictions.eq("salesOrder.priceGroup", tenderOrPriceGroup)));
//		}

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public boolean getIsShowInstall(String shippingpointOID, String jobTypeOID) {
		DetachedCriteria criteria = DetachedCriteria.forClass(JobProductInstall.class);
		criteria.add(Restrictions.eq("jobProductInstallId.shippingpointOID", shippingpointOID));
		criteria.add(Restrictions.eq("jobProductInstallId.jobTypeOID", jobTypeOID));
		List result = getHibernateTemplate().findByCriteria(criteria);

		boolean flag = false;
		if (result != null && result.size() > 0) {
			JobProductInstall jobInstall = (JobProductInstall) result.get(0);
			flag = jobInstall.getIsShowInstall();
		}
		return flag;
	}

	public HashMap getVendorFromArticleNo(HashMap salesItemMap) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		DetachedCriteria criteriaVendorGroup = DetachedCriteria.forClass(DsVendorGroup.class);
		DetachedCriteria criteriaVendor = DetachedCriteria.forClass(DsVendor.class);
		DetachedCriteria criteriaArticleInstore = DetachedCriteria.forClass(ArticleInStore.class);
		List articleResult = null;
		List vendorList = null;
		String vendorIdCompare = null;
		// String vendorText = null;
		String msg = null;
		HashMap resultMap = new HashMap();
		DsVendor vendor = null;

		if (salesItemMap.get("salesorderItem") != null) {
			List resultList = (List) salesItemMap.get("salesorderItem");
			//for (int i = 0; i < resultList.size(); i++) {
				SalesOrderItem soItem = (SalesOrderItem) resultList.get(0);
				if (soItem.getArticleNo() != null) {
					criteria.add(Restrictions.eq("articleId", soItem.getArticleNo()));
					articleResult = getHibernateTemplate().findByCriteria(criteria);   
					if (articleResult.size() > 0) {
						Article article = (Article) articleResult.get(0);
						criteriaArticleInstore.add(Restrictions.eq("articleInStoreId.articleId",article.getArticleId()));
						criteriaArticleInstore.add(Restrictions.eq("articleInStoreId.storeId",soItem.getSalesOrder().getStore().getStoreId()));
						List articleInsList = getHibernateTemplate().findByCriteria(criteriaArticleInstore);
						if(articleInsList.size()>0){
							ArticleInStore articleInStore = (ArticleInStore)articleInsList.get(0);
							criteriaVendorGroup.add(Restrictions.eq("vendorGroupId.hpVendorId", articleInStore.getVendor().getVendorId()));
							List vendorGroupResult = getHibernateTemplate().findByCriteria(criteriaVendorGroup);
							if (vendorGroupResult.size() > 0) {
								
								for(int i =0 ;i<vendorGroupResult.size() ;i++){
									DsVendorGroup vendorGroup = (DsVendorGroup) vendorGroupResult.get(i);
									if(vendorGroup.getDsVendor().getStatus() =='Y'){
										criteriaVendor.add(Restrictions.eq("vendorId", vendorGroup.getVendorGroupId().getDsVendorId()));
										
										criteriaVendor.add(Restrictions.eq("status","Y"));
										
										vendorList = getHibernateTemplate().findByCriteria(criteriaVendor);
										if (vendorList.size() > 0) {
											vendor = (DsVendor) vendorList.get(0);
											if (vendorIdCompare != null) {
												if (vendorIdCompare.equals(vendor.getVendorId())) {
												} else {
													msg = "so.over.vendor";
												}
											}
											vendorIdCompare = vendor.getVendorId();
										} else {
											msg = "so.notfound.vendor";
											//break;
										}
									}
								}
								
//								DsVendorGroup vendorGroup = (DsVendorGroup) vendorGroupResult.get(0);
//								criteriaVendor.add(Restrictions.eq("vendorId", vendorGroup.getVendorGroupId().getDsVendorId()));
//								criteriaVendor.add(Restrictions.eq("status","Y"));
//								vendorList = getHibernateTemplate().findByCriteria(criteriaVendor);
//								if (vendorList.size() > 0) {
//									vendor = (DsVendor) vendorList.get(0);
//									if (vendorIdCompare != null) {
//										if (vendorIdCompare.equals(vendor.getVendorId())) {
//											// vendorText = vendor.getDescription();
//										} else {
//											msg = "so.over.vendor";
//											//break;
//										}
//									}
//									vendorIdCompare = vendor.getVendorId();
//								} else {
//									msg = "so.notfound.vendor";
//									//break;
//								}
							}
						}else{
							msg = "so.notfound.vendor";
						}
					} else {
						msg = "so.notfound.vendor";
						//break;
					}
				} else {
					msg = "so.notfound.vendor";
					//break;
				}
//			}// for
		} else {
			msg = "so.notfound.vendor";
		}
	
		resultMap.put("vendorBean", vendor);
		resultMap.put("message", msg);

		return resultMap;
	}
	public List getVendorFromArticleNoRefund(HashMap salesItemMap) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		DetachedCriteria criteriaVendorGroup = DetachedCriteria.forClass(DsVendorGroup.class);
		DetachedCriteria criteriaVendor = DetachedCriteria.forClass(DsVendor.class);
		DetachedCriteria criteriaArticleInstore = DetachedCriteria.forClass(ArticleInStore.class);
		List articleResult = null;
		List vendorList = null;
		String vendorIdCompare = null;
		String msg = null;
		HashMap resultMap = new HashMap();
		DsVendor vendor = null;

		if (salesItemMap.get("salesorderItem") != null) {
			List resultList = (List) salesItemMap.get("salesorderItem");
				SalesOrderItem soItem = (SalesOrderItem) resultList.get(0);
				if (soItem.getArticleNo() != null) {
					criteria.add(Restrictions.eq("articleId", soItem.getArticleNo()));
					articleResult = getHibernateTemplate().findByCriteria(criteria);   
					if (articleResult.size() > 0) {
						Article article = (Article) articleResult.get(0);
						criteriaArticleInstore.add(Restrictions.eq("articleInStoreId.articleId",article.getArticleId()));
						criteriaArticleInstore.add(Restrictions.eq("articleInStoreId.storeId",soItem.getSalesOrder().getStore().getStoreId()));
						List articleInsList = getHibernateTemplate().findByCriteria(criteriaArticleInstore);
						if(articleInsList.size()>0){
							ArticleInStore articleInStore = (ArticleInStore)articleInsList.get(0);
							criteriaVendorGroup.add(Restrictions.eq("vendorGroupId.hpVendorId", articleInStore.getVendor().getVendorId()));
							List vendorGroupResult = getHibernateTemplate().findByCriteria(criteriaVendorGroup);
							if (vendorGroupResult.size() > 0) {  
								for(int i =0 ;i<vendorGroupResult.size() ;i++){
									DsVendorGroup vendorGroup = (DsVendorGroup) vendorGroupResult.get(i);
									if(vendorGroup.getDsVendor().getStatus() =='Y'){
										criteriaVendor.add(Restrictions.eq("vendorId", vendorGroup.getVendorGroupId().getDsVendorId()));
										criteriaVendor.add(Restrictions.eq("status","Y"));
										vendorList = getHibernateTemplate().findByCriteria(criteriaVendor);
									}
								}
								
							}
						}
						
					} 
				}

		} 
	
		resultMap.put("vendorBean", vendor);
		resultMap.put("message", msg);

		return vendorList;
	}

	public List getSalesOrderStatus() throws DataAccessException {
		return getHibernateTemplate().loadAll(SalesOrderStatus.class);
	}

	public List getSalesOrders(Date startDate, Date endDate, String type, String status, String salesNo, Boolean isNotReject) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		Calendar c = Calendar.getInstance();
		c.setTime(endDate);
		c.add(Calendar.DATE, 1);
		c.add(Calendar.MILLISECOND, -1);

		criteria.add(Restrictions.between("createDateTime", startDate, c.getTime()));
		if (type != null && !"".equals(type.trim())) {
			criteria.add(Restrictions.eq("salesOrderType.salesOrderTypeId", type));
		}
		if (status != null && !"".equals(status.trim())) {
			criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", status));
		}
		if (salesNo != null && !"".equals(salesNo.trim())) {
			criteria.add(Restrictions.eq("createUserId", salesNo));
		}
		if (isNotReject != null) {
			criteria.add(Restrictions.eq("isNotReject", isNotReject));
		}
		
		criteria.addOrder(Order.desc("createDateTime"));
		criteria.addOrder(Order.desc("salesOrderNo"));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		return list ; 
	}

	public SalesOrderItem getSalesOrderItem(Long objectId, String articleNo, String mainUPC) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderItem.class);
		criteria.add(Restrictions.eq("salesOrder.objectId", objectId));
		criteria.add(Restrictions.eq("articleNo", articleNo));
		criteria.add(Restrictions.eq("mainUPC", mainUPC));
		criteria.addOrder(Order.asc("seqNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);

		if (list != null && list.size() > 0) {
			SalesOrderItem result = (SalesOrderItem)list.get(0);
			return result;
//			return (SalesOrderItem) list.get(0);
		} else {
			return null;
		}
	}
	
	public SalesOrderItem getSalesOrderItemBySeqNo(Long objectId, String articleNo, String mainUPC,String seqNo ) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderItem.class);
//		int sapSeqno = 0;
//		if(seqNo != null){
//			sapSeqno = Integer.parseInt(seqNo);
//		}

		criteria.add(Restrictions.eq("salesOrder.objectId", objectId));
		criteria.add(Restrictions.eq("articleNo", articleNo));
		criteria.add(Restrictions.eq("mainUPC", mainUPC));
		if(seqNo != null){
			criteria.add(Restrictions.eq("sapSeqNo", seqNo));
		}
		
		criteria.addOrder(Order.asc("seqNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);

		if (list != null && list.size() > 0) {
			SalesOrderItem result = (SalesOrderItem)list.get(0);
			return result;
//			return (SalesOrderItem) list.get(0);
		} else {
			return null;
		}
	}
	
	public SalesOrderItem getSalesOrderItemBySequenceNo(Long objectId, String articleNo, String mainUPC,String seqNo ) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderItem.class);
		int sapSeqno = 0;
		if(seqNo != null){
			sapSeqno = Integer.parseInt(seqNo);
		}

		criteria.add(Restrictions.eq("salesOrder.objectId", objectId));
		criteria.add(Restrictions.eq("articleNo", articleNo));
		criteria.add(Restrictions.eq("mainUPC", mainUPC));
		criteria.add(Restrictions.eq("seqNo", new Integer(sapSeqno)));
		
		criteria.addOrder(Order.asc("seqNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);

		if (list != null && list.size() > 0) {
			SalesOrderItem result = (SalesOrderItem)list.get(0);
			return result;
		} else {
			return null;
		}
	}
	
	public List getSalesOrderType(String storeId , Date trnDate , String salesOrderNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.add(Restrictions.eq("store.storeId", storeId));
//		criteria.add(Restrictions.eq("transactionDate", trnDate));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public SalesOrderGroup getSalesOrderGroupBySalesOrderOID(long salesOrderOID) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderGroup.class);
		criteria.createAlias("salesOrder","salesOrder");
		criteria.add(Restrictions.eq("salesOrder.objectId", new Long(salesOrderOID)));
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if(list!=null && list.size()>0){
			SalesOrderGroup result = (SalesOrderGroup)list.get(0);
			return result;
		}else{
			return null;
		}
	}
	
	public List getSalesOrderDeposited(Date trn_date, String status, String salesOrderNo,String salesorder_ticket) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		if(!salesOrderNo.equals("")){
			criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		}
		if(!salesorder_ticket.equals("")){
			criteria.add(Restrictions.eq("ticketNo", salesorder_ticket));
		}
		criteria.add(Restrictions.eq("transactionDate", trn_date));
		criteria.add(Restrictions.eq("salesOrderStatus.salesOrderStatusId", status));
		criteria.addOrder(Order.asc("ticketNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
	
	public List getSalesOrderByTicketNumber(String ticket_no) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("ticketNo", ticket_no));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
			}
		}
		return list;
	}
	
	public List getSalesOrderBySales(String ticket_no,Date trn_date , String storeId) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("ticketNo", ticket_no));
		criteria.add(Restrictions.eq("transactionDate", trn_date));
		criteria.add(Restrictions.eq("store.storeId", storeId));
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
		}
		return list;
	}
	
	public List getSalesOrderSelect(Date trnDate , String stsId ) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("transactionDate", (trnDate)));
		criteria.add(Restrictions.or(Restrictions.eq("salesOrderStatus.salesOrderStatusId",stsId),Restrictions.eq("salesOrderStatus.salesOrderStatusId",SalesOrderConstant.PENDING_STATUS)));
		criteria.add(Restrictions.eq("isNotReject",new Boolean(false)));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getPOTypeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(POType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(POType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(POType poType){
		getHibernateTemplate().update(poType);
	}

	public void createOrUpdate(POType poType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(poType);
	}
	
	
	public SalesOrderSetItem getSalesOrderSetItem(long SalesOrderSetItemOid , String MainUpc , String MC9) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderSetItem.class);
		criteria.add(Restrictions.eq("mainUPC", (MainUpc)));
		criteria.add(Restrictions.eq("mc9", (MC9)));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if(list!=null && list.size()>0){
			SalesOrderSetItem salesOrderSetItem = (SalesOrderSetItem)list.get(0);
			return salesOrderSetItem;
		}else{
			return null;
		}
	}

	public List getDiscountCondtypeList() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountConditionType.class);
		List discountCondType = getHibernateTemplate().findByCriteria(criteria);
		return discountCondType;
	}

	public List getSalesOrderType(String salesOrderType) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderType.class);
		criteria.add(Restrictions.eq("salesOrderTypeId", (salesOrderType)));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}

	public List getSalesOrdersByTickeyNo(String ticketNo, Date trnDate, String salesOrderNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		
		criteria.add(Restrictions.eq("ticketNo", ticketNo));
		criteria.add(Restrictions.le("transactionDate", (trnDate)));
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		if (list.size() > 0) 
		{
			for(int i=0; i<list.size(); i++)
			{
				SalesOrder salesOrder = (SalesOrder) list.get(i);

				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
			
			}
			return list;
		}
		else
		{
			return null;
		}  
	}
	
	public List getSalesOrderByQuotationNoAndVersion(String quotationId, String versionId)throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("quotationId", quotationId));
		if(versionId!=null){
			criteria.add(Restrictions.eq("versionId", versionId));
		}
		SalesOrderStatus  salesOrderStatus = new SalesOrderStatus();
		salesOrderStatus.setSalesOrderStatusId(SalesOrderConstant.CANCELLED_STATUS);
		criteria.add(Restrictions.ne("salesOrderStatus", salesOrderStatus));
		criteria.addOrder(Order.asc("salesOrderNo"));
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if (list != null && list.size() > 0) {    
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);   
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
		}
		return list;
	}
	
	
	public List getInitialSalesOrder(List list){
		if (list != null && list.size() > 0) {    
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);   
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
		}
		return list;
	}
	
	public BigDecimal getTotalNetAmount(String salesOrderNo, String storeId){
		BigDecimal netTotalAmount = new BigDecimal("0.00");
		SalesOrder salesOrder = getSalesOrder(storeId, salesOrderNo);
		for(int a = 0 ; a<salesOrder.getSalesOrderItems().size() ; a++){
			SalesOrderItem salesItem = (SalesOrderItem)salesOrder.getSalesOrderItems().get(a);
			netTotalAmount = netTotalAmount.add(salesItem.getNetItemAmount());
		}
		return netTotalAmount;
	}

	public BigDecimal getTotalNetAmountForCredit(String sapId, String storeId,Date trnDate)  throws Exception{
		BigDecimal netTotalAmount = new BigDecimal("0.00");
		List soList = getSalesOrdersForCredit(sapId, storeId,trnDate);
		for(int i = 0 ; i<soList.size();i++){
			SalesOrder salesOrder = (SalesOrder)soList.get(i);
			for(int a = 0 ; a<salesOrder.getSalesOrderItems().size() ; a++){
				SalesOrderItem salesItem = (SalesOrderItem)salesOrder.getSalesOrderItems().get(a);
				netTotalAmount = netTotalAmount.add(salesItem.getNetItemAmount());
			}
		}
		return netTotalAmount;
	}

	public List getSalesOrdersForCredit(String sapId,String storeId, Date trnDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		criteria.createAlias("soldToCustomer","soldToCustomer");
		criteria.add(Restrictions.eq("soldToCustomer.sapId", sapId));
		
		if (trnDate != null) {
			Calendar toDate = Calendar.getInstance();
			toDate.setTime(trnDate);
			toDate.add(Calendar.DATE, 1);
			criteria.add(Restrictions.between("transactionDate", trnDate, toDate.getTime()));
		}    
		
		if (storeId != null) {
			criteria.createAlias("store", "store");
			criteria.add(Restrictions.eq("store.storeId", storeId));
		}
		String[] status = {SalesOrderConstant.CREATED_STATUS,SalesOrderConstant.PENDING_STATUS,SalesOrderConstant.COLL_STATUS};
		criteria.add(Restrictions.in("salesOrderStatus.salesOrderStatusId", status));
		criteria.addOrder(Order.desc("salesOrderNo"));
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		if (list != null && list.size() > 0) {    
			for (int i = 0; i < list.size(); i++) {
				SalesOrder salesOrder = (SalesOrder) list.get(i);   
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
			}
		}
		return list;
	}
	
	
	public void cancelSalesOrders(List salesOrderList){
		SalesOrderStatus  salesOrderStatus = new SalesOrderStatus();
		salesOrderStatus.setSalesOrderStatusId(SalesOrderConstant.CANCELLED_STATUS);
		for(int i=0;i<salesOrderList.size();i++){
			SalesOrder salesOrder = (SalesOrder)salesOrderList.get(i);
			salesOrder.setSalesOrderStatus(salesOrderStatus);
			getHibernateTemplate().update(salesOrder);    
		}
	}
	
	public List getDsPattern(String shippingPoint, String jobType, String prdType) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(JobProductType.class);	
		criteria.add(Restrictions.eq("jobProductTypeId.shippingPointId", shippingPoint));
		criteria.add(Restrictions.eq("jobProductTypeId.jobTypeId", jobType));
		criteria.add(Restrictions.eq("jobProductTypeId.mainProductTypeId", prdType));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getDsPatTime(String shippingPoint, String pattern, Date deliDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(DsPatTime.class);	
		criteria.add(Restrictions.eq("dsPatTimeId.shippingPointId", shippingPoint));
		criteria.add(Restrictions.eq("dsPatTimeId.patternId", pattern));
		criteria.add(Restrictions.le("dsPatTimeId.startDate", deliDate));
		criteria.add(Restrictions.ge("endDate", deliDate));
		criteria.add(Restrictions.eq("status", new Boolean(true)));
		List aaa = getHibernateTemplate().findByCriteria(criteria);
		return aaa;

	}
	
	public List getDsPatTimeDet(String shippingPoint, String pattern, Date deliDate) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(DsPatTimeDet.class);
		criteria.add(Restrictions.eq("dsPatTimeDetId.shippingPointId", shippingPoint));
		criteria.add(Restrictions.eq("dsPatTimeDetId.patternId", pattern));
		criteria.add(Restrictions.le("dsPatTimeDetId.startDate", deliDate));
		criteria.add(Restrictions.ge("endDate", deliDate));
		criteria.add(Restrictions.eq("status", new Boolean(true)));
		List aaa = getHibernateTemplate().findByCriteria(criteria);
		return aaa;
	}
	
	public List getDsSpecWork()throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(DsSpecWork.class);
		criteria.add(Restrictions.eq("isActive",  new Boolean(true)));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getSalesOrderItem(String storeId, Date trnDate , String salesOrderNo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrderItem.class);

		criteria.createAlias("salesOrder", "salesOrder");
		criteria.add(Restrictions.eq("salesOrder.transactionDate", trnDate));
		if (storeId != null && !"".equals(storeId.trim())) {
			criteria.createAlias("salesOrder.store", "store");
			criteria.add(Restrictions.eq("salesOrder.store.storeId", storeId));
		}
		
		criteria.add(Restrictions.eq("salesOrder.salesOrderNo", salesOrderNo));
		
		List ret = getHibernateTemplate().findByCriteria(criteria);
		if(ret == null){
			return null;
		}else{
			return ret ; 
		}
	}
	
	public List getSalesOrderBySalesOrderOid(long salesOrderOid) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("objectId", new Long(salesOrderOid))); 
		
		List ret = getHibernateTemplate().findByCriteria(criteria);
		if (ret != null) {
			/** initial item */
			for (int index = 0; index < ret.size(); index++) {
				SalesOrder salesOrder = (SalesOrder) ret.get(index);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
				
			}
		}
		return ret;
	}
	
	public SalesOrder getSalesOrderBySalesOrderNoByTrnDate(String salesOrderNo, Date trnDate) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo)); 
		criteria.add(Restrictions.eq("transactionDate", trnDate)); 
		
		List ret = getHibernateTemplate().findByCriteria(criteria);
		
		if (ret != null) {
			/** initial item */
			for (int index = 0; index < ret.size(); index++) {
				SalesOrder salesOrder = (SalesOrder) ret.get(index);
				getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				for (int j = 0; j < salesOrder.getSalesOrderGroups().size(); j++) {
					SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(j);
					getHibernateTemplate().initialize(group.getSalesOrderItems());
				}
				for (int k = 0; k < salesOrder.getSalesOrderItems().size(); k++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(k);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
				
			}
			
			if(ret.size()>0){
				SalesOrder salesOrder = (SalesOrder) ret.get(0);
				return salesOrder;
			}
			
		}
		return null;
	}
	
	public SalesOrder getSalesOrderForQuotation(String storeid, String salesOrderNo) throws Exception{

		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);

		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeid));
		criteria.addOrder(Order.asc("salesOrderNo"));
		criteria.createAlias("salesOrderStatus", "salesOrderStatus");
		criteria.addOrder(Order.desc("salesOrderStatus.salesOrderStatusId"));

		try{
			List list = getHibernateTemplate().findByCriteria(criteria);
			if (list.size() > 0) {
				SalesOrder salesOrder = (SalesOrder) list.get(0);
				try {
					if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
						getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderGroups());
				getHibernateTemplate().initialize(salesOrder.getSalesOrderItems());
				// getHibernateTemplate().initialize(salesOrder.getSalesOrderTransactions());
				if (salesOrder.getSalesOrderGroups() != null && !salesOrder.getSalesOrderGroups().isEmpty()) {
					for (int i = 0; i < salesOrder.getSalesOrderGroups().size(); i++) {
						SalesOrderGroup group = (SalesOrderGroup) salesOrder.getSalesOrderGroups().get(i);
						getHibernateTemplate().initialize(group.getSalesOrderItems());
					}
				}
	
				for (int i = 0; i < salesOrder.getSalesOrderItems().size(); i++) {
					SalesOrderItem salesOrderItem = (SalesOrderItem) salesOrder.getSalesOrderItems().get(i);
					getHibernateTemplate().initialize(salesOrderItem.getSalesOrderSetItems());
				}
				return salesOrder;
			} else {
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}

	public List getVendorListByTaxInvoice(TaxInvoice tin) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		DetachedCriteria criteriaVendorGroup = DetachedCriteria.forClass(DsVendorGroup.class);
		DetachedCriteria criteriaVendor = DetachedCriteria.forClass(DsVendor.class);
		DetachedCriteria criteriaArticleInstore = DetachedCriteria.forClass(ArticleInStore.class);
		DetachedCriteria criteriaMainUPC = DetachedCriteria.forClass(MainUPC.class);
		List articleResult = null;
		List vendorList = null;
		String vendorIdCompare = null;
		String msg = null;
		DsVendor vendor = null;
		HashMap resultMap = new HashMap();
		
		for (int i = 0; i < tin.getTaxInvoiceItems().size(); i++) {
			TaxInvoiceItem tit = (TaxInvoiceItem) tin.getTaxInvoiceItems().get(i);
			
			criteria = DetachedCriteria.forClass(Article.class);
			criteriaVendorGroup = DetachedCriteria.forClass(DsVendorGroup.class);
			criteriaVendor = DetachedCriteria.forClass(DsVendor.class);
			criteriaArticleInstore = DetachedCriteria.forClass(ArticleInStore.class);
			criteriaMainUPC = DetachedCriteria.forClass(MainUPC.class);
			
			if(tit.getItemUPC()!=null){
				criteriaMainUPC.add(Restrictions.eq("mainUPC", tit.getItemUPC()));
				MainUPC mainUPC =  (MainUPC) getHibernateTemplate().findByCriteria(criteriaMainUPC).get(0);
				criteria.add(Restrictions.eq("articleId", mainUPC.getArticle().getArticleId()));
				articleResult = getHibernateTemplate().findByCriteria(criteria);   
				if (articleResult.size() > 0) {
					
					Article article = (Article) articleResult.get(0);
					criteriaArticleInstore.add(Restrictions.eq("articleInStoreId.articleId",article.getArticleId()));
					criteriaArticleInstore.add(Restrictions.eq("articleInStoreId.storeId",tin.getStore().getStoreId()));
					List articleInsList = getHibernateTemplate().findByCriteria(criteriaArticleInstore);
					if(articleInsList.size()>0){
						ArticleInStore articleInStore = (ArticleInStore)articleInsList.get(0);
						criteriaVendorGroup.add(Restrictions.eq("vendorGroupId.hpVendorId", articleInStore.getVendor().getVendorId()));
						List vendorGroupResult = getHibernateTemplate().findByCriteria(criteriaVendorGroup);
						if (vendorGroupResult.size() > 0) {
							
							for(int j =0 ;j<vendorGroupResult.size() ;j++){
								DsVendorGroup vendorGroup = (DsVendorGroup) vendorGroupResult.get(j);
								if(vendorGroup.getDsVendor().getStatus() =='Y'){
									criteriaVendor.add(Restrictions.eq("vendorId", vendorGroup.getVendorGroupId().getDsVendorId()));
									
									criteriaVendor.add(Restrictions.eq("status","Y"));
									
									vendorList = getHibernateTemplate().findByCriteria(criteriaVendor);
									if (vendorList.size() > 0) {
										vendor = (DsVendor) vendorList.get(0);
										if (vendorIdCompare != null) {
											if (vendorIdCompare.equals(vendor.getVendorId())) {
											} else {
												msg = "so.over.vendor";
											}
										}
										vendorIdCompare = vendor.getVendorId();
									} else {
										msg = "so.notfound.vendor";
										//break;
									}
								}
							}

						}
					}else{
						msg = "so.notfound.vendor";
					}
				} else {
					msg = "so.notfound.vendor";
					//break;
				}
			}
		}
		resultMap.put("vendorBean", vendor);
		resultMap.put("message", msg);

		return vendorList;
	}
	
	public List getSalesOrderTypeByDate(String storeId, Date transactionDate,
			String salesOrderNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.add(Restrictions.eq("store.storeId", storeId));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	
	public BigDecimal getTotalNetAmountForCreditForCheckOrderNo(String sapId, String storeId,Date trnDate,String orderNumber,String status)  throws Exception{
		BigDecimal netTotalAmount = new BigDecimal("0.00");
		List soList = getSalesOrdersForCredit(sapId, storeId,trnDate);
		for(int i = 0 ; i<soList.size();i++){
			SalesOrder salesOrder = (SalesOrder)soList.get(i);
			if(!(orderNumber.equals(salesOrder.getSalesOrderNo())&& status.equals(salesOrder.getSalesOrderStatus().getSalesOrderStatusId())&& trnDate.equals(salesOrder.getTransactionDate()) )) {
				for(int a = 0 ; a<salesOrder.getSalesOrderItems().size() ; a++){
					SalesOrderItem salesItem = (SalesOrderItem)salesOrder.getSalesOrderItems().get(a);
					netTotalAmount = netTotalAmount.add(salesItem.getNetItemAmount());
				} 
			} 
		}
		return netTotalAmount;
	}

	
	//...Boizz(+) : Fix ID # 419 @ 17-Dec-13
	public void delete(String salesOrderNo, String storeId, Date transactionDate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class);
		criteria.add(Restrictions.eq("salesOrderNo", salesOrderNo));
		criteria.add(Restrictions.eq("transactionDate", transactionDate));
		criteria.createAlias("store", "store");
		criteria.add(Restrictions.eq("store.storeId", storeId));
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		//...Optional 1
		getHibernateTemplate().deleteAll(list);
		
		//...Optional 2
		/*for (int index=0; index<list.size(); index++) {
			SalesOrder salesOrder = (SalesOrder)list.get(index);
			getHibernateTemplate().deleteAll(salesOrder.getSalesOrderGroups());
			getHibernateTemplate().deleteAll(salesOrder.getSalesOrderItems());
			getHibernateTemplate().delete(salesOrder);
		}*/
	}
	
	public void delete(SalesOrder salesOrder) throws DataAccessException {
		getHibernateTemplate().delete(salesOrder);
	}
	//...End(+)
}