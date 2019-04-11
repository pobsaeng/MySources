/*
 * Created on Jan 16, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.MasterDeliveryServiceDao;
import com.ie.icon.domain.DistrictSubDistrict;
import com.ie.icon.domain.SubDistrict;
import com.ie.icon.domain.so.DsConfType;
import com.ie.icon.domain.so.DsDistrict;
import com.ie.icon.domain.so.DsPatTime;
import com.ie.icon.domain.so.DsPatTimeDet;
import com.ie.icon.domain.so.DsProvince;
import com.ie.icon.domain.so.DsSpecWork;
import com.ie.icon.domain.so.DsTimeFrame;
import com.ie.icon.domain.so.DsVendor;
import com.ie.icon.domain.so.DsVendorBrand;
import com.ie.icon.domain.so.DsVendorGroup;
import com.ie.icon.domain.so.JobProductType;
import com.ie.icon.domain.so.JobType;
import com.ie.icon.domain.so.MainProductType;
import com.ie.icon.domain.so.ProvinceDistrict;
import com.ie.icon.domain.so.ShippingPoint;
import com.ie.icon.domain.so.ShippingTimeFrame;

/**
 * @author amaritk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateMasterDeliveryServiceDao extends HibernateCommonDao implements MasterDeliveryServiceDao{
	

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.DsDistrict)
	 */
	public void create(DsDistrict dsDistrict) throws DataAccessException {
		getHibernateTemplate().save(dsDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.DsProvince)
	 */
	public void create(DsProvince dsProvince) throws DataAccessException {
		getHibernateTemplate().save(dsProvince);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.DsTimeFrame)
	 */
	public void create(DsTimeFrame dsTimeFrame) throws DataAccessException {
		getHibernateTemplate().save(dsTimeFrame);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.DsVendor)
	 */
	public void create(DsVendor dsVendor) throws DataAccessException {
		getHibernateTemplate().save(dsVendor);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.DsVendorBrand)
	 */
	public void create(DsVendorBrand dsVendorBrand) throws DataAccessException {
		getHibernateTemplate().save(dsVendorBrand);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.DsVendorGroup)
	 */
	public void create(DsVendorGroup dsVendorGroup) throws DataAccessException {
		getHibernateTemplate().save(dsVendorGroup);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.JobProductType)
	 */
	public void create(JobProductType jobProductType)
			throws DataAccessException {
		getHibernateTemplate().save(jobProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.JobType)
	 */
	public void create(JobType jobType) throws DataAccessException {
		getHibernateTemplate().save(jobType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.MainProductType)
	 */
	public void create(MainProductType mainProductType)
			throws DataAccessException {
		getHibernateTemplate().save(mainProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.ProvinceDistrict)
	 */
	public void create(ProvinceDistrict provinceDistrict)
			throws DataAccessException {
		getHibernateTemplate().save(provinceDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.ShippingTimeFrame)
	 */
	public void create(ShippingTimeFrame shippingTimeFrame)
			throws DataAccessException {
		getHibernateTemplate().save(shippingTimeFrame);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.DsDistrict)
	 */
	public void createOrUpdate(DsDistrict dsDistrict)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.DsProvince)
	 */
	public void createOrUpdate(DsProvince dsProvince)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsProvince);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.DsTimeFrame)
	 */
	public void createOrUpdate(DsTimeFrame dsTimeFrame)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsTimeFrame);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.DsVendor)
	 */
	public void createOrUpdate(DsVendor dsVendor) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsVendor);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.DsVendorBrand)
	 */
	public void createOrUpdate(DsVendorBrand dsVendorBrand)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsVendorBrand);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.DsVendorGroup)
	 */
	public void createOrUpdate(DsVendorGroup dsVendorGroup)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsVendorGroup);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.JobProductType)
	 */
	public void createOrUpdate(JobProductType jobProductType)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(jobProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.JobType)
	 */
	public void createOrUpdate(JobType jobType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(jobType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.MainProductType)
	 */
	public void createOrUpdate(MainProductType mainProductType)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(mainProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.ProvinceDistrict)
	 */
	public void createOrUpdate(ProvinceDistrict provinceDistrict)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(provinceDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.ShippingTimeFrame)
	 */
	public void createOrUpdate(ShippingTimeFrame shippingTimeFrame)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(shippingTimeFrame);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.DsDistrict)
	 */
	public void delete(DsDistrict dsDistrict) throws DataAccessException {
		getHibernateTemplate().delete(dsDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.DsProvince)
	 */
	public void delete(DsProvince dsProvince) throws DataAccessException {
		getHibernateTemplate().delete(dsProvince);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.DsTimeFrame)
	 */
	public void delete(DsTimeFrame dsTimeFrame) throws DataAccessException {
		getHibernateTemplate().delete(dsTimeFrame);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.DsVendor)
	 */
	public void delete(DsVendor dsVendor) throws DataAccessException {
		getHibernateTemplate().delete(dsVendor);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.DsVendorBrand)
	 */
	public void delete(DsVendorBrand dsVendorBrand) throws DataAccessException {
		getHibernateTemplate().delete(dsVendorBrand);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.DsVendorGroup)
	 */
	public void delete(DsVendorGroup dsVendorGroup) throws DataAccessException {
		getHibernateTemplate().delete(dsVendorGroup);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.JobProductType)
	 */
	public void delete(JobProductType jobProductType)
			throws DataAccessException {
		getHibernateTemplate().delete(jobProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.JobType)
	 */
	public void delete(JobType jobType) throws DataAccessException {
		getHibernateTemplate().delete(jobType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.MainProductType)
	 */
	public void delete(MainProductType mainProductType)
			throws DataAccessException {
		getHibernateTemplate().delete(mainProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.ProvinceDistrict)
	 */
	public void delete(ProvinceDistrict provinceDistrict)
			throws DataAccessException {
		getHibernateTemplate().delete(provinceDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.ShippingTimeFrame)
	 */
	public void delete(ShippingTimeFrame shippingTimeFrame)
			throws DataAccessException {
		getHibernateTemplate().delete(shippingTimeFrame);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getDsDistrictByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getDsDistrictByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsDistrict.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getDsProvinceByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getDsProvinceByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsProvince.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getDsTimeFrameByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getDsTimeFrameByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsTimeFrame.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getDsVendorBrandByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getDsVendorBrandByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorBrand.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getDsVendorByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getDsVendorByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendor.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getDsVendorGroupByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getDsVendorGroupByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorGroup.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	
        
        List list = null;
        try{
        list = getHibernateTemplate().findByCriteria(criteria, first, max);
        }
        catch (Exception exs){
        	exs.printStackTrace();
        }

		return list;
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getJobProductTypeByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getJobProductTypeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(JobProductType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getJobTypeByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getJobTypeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(JobType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getMainProductTypeByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getMainProductTypeByUpdDttmGtPubDttm(int first,	int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainProductType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getProvinceDistrictByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getProvinceDistrictByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceDistrict.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
       
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getShippingTimeFrameByUpdDttmGtPubDttm(java.lang.String, int, int)
	 */
	public List getShippingTimeFrameByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ShippingTimeFrame.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowDsDistrictByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowDsDistrictByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsDistrict.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowDsProvinceByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowDsProvinceByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsProvince.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowDsTimeFrameByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowDsTimeFrameByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsTimeFrame.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowDsVendorBrandByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowDsVendorBrandByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorBrand.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowDsVendorByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowDsVendorByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendor.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowDsVendorGroupByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowDsVendorGroupByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsVendorGroup.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowJobProductTypeByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowJobProductTypeByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(JobProductType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowJobTypeByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowJobTypeByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(JobType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowMainProductTypeByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowMainProductTypeByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MainProductType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowProvinceDistrictByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowProvinceDistrictByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceDistrict.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowShippingTimeFrameByUpdDttmGtPubDttm(java.lang.String)
	 */
	public int getRowShippingTimeFrameByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ShippingTimeFrame.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.DsDistrict)
	 */
	public void update(DsDistrict dsDistrict) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.DsProvince)
	 */
	public void update(DsProvince dsProvince) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsProvince);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.DsTimeFrame)
	 */
	public void update(DsTimeFrame dsTimeFrame) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsTimeFrame);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.DsVendor)
	 */
	public void update(DsVendor dsVendor) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsVendor);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.DsVendorBrand)
	 */
	public void update(DsVendorBrand dsVendorBrand) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsVendorBrand);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.DsVendorGroup)
	 */
	public void update(DsVendorGroup dsVendorGroup) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsVendorGroup);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.JobProductType)
	 */
	public void update(JobProductType jobProductType)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(jobProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.JobType)
	 */
	public void update(JobType jobType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(jobType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.MainProductType)
	 */
	public void update(MainProductType mainProductType)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(mainProductType);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.ProvinceDistrict)
	 */
	public void update(ProvinceDistrict provinceDistrict)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(provinceDistrict);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.ShippingTimeFrame)
	 */
	public void update(ShippingTimeFrame shippingTimeFrame)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(shippingTimeFrame);

	}
	
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#create(com.ie.icon.domain.so.ShippingPoint)
	 */
	public void create(ShippingPoint shippingPoint) throws DataAccessException {
		getHibernateTemplate().save(shippingPoint);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#createOrUpdate(com.ie.icon.domain.so.ShippingPoint)
	 */
	public void createOrUpdate(ShippingPoint shippingPoint)
			throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(shippingPoint);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#delete(com.ie.icon.domain.so.ShippingPoint)
	 */
	public void delete(ShippingPoint shippingPoint) throws DataAccessException {
		getHibernateTemplate().delete(shippingPoint);

	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getRowShippingPointByUpdDttmGtPubDttm()
	 */
	public int getRowShippingPointByUpdDttmGtPubDttm()
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ShippingPoint.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#getShippingPointByUpdDttmGtPubDttm(int, int)
	 */
	public List getShippingPointByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ShippingPoint.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.MasterDeliveryServiceDao#update(com.ie.icon.domain.so.ShippingPoint)
	 */
	public void update(ShippingPoint shippingPoint) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(shippingPoint);

	}
	public void create(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().save(dsConfType);
		
	}
	public void createOrUpdate(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsConfType);
		
	}
	public void delete(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().delete(dsConfType);
		
	}
	public List getDsConfTypeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsConfType.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	public int getRowDsConfTypeByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsConfType.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	public void update(DsConfType dsConfType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsConfType);
		
	}
	public void create(DsPatTime dsPatTime) throws DataAccessException {
		getHibernateTemplate().save(dsPatTime);
		
	}
	public void create(DsPatTimeDet dsPatTimeDet) throws DataAccessException {
		getHibernateTemplate().save(dsPatTimeDet);
		
	}
	public void createOrUpdate(DsPatTime dsPatTime) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsPatTime);
		
	}
	public void createOrUpdate(DsPatTimeDet dsPatTimeDet) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsPatTimeDet);
		
	}
	public void delete(DsPatTime dsPatTime) throws DataAccessException {
		getHibernateTemplate().delete(dsPatTime);
		
	}
	public void delete(DsPatTimeDet dsPatTimeDet) throws DataAccessException {
		getHibernateTemplate().delete(dsPatTimeDet);
		
	}
	public List getDsPatTimeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsPatTime.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	public List getDsPatTimeDetByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsPatTimeDet.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	public int getRowDsPatTimeByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsPatTime.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	public int getRowDsPatTimeDetByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsPatTimeDet.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	public void update(DsPatTime dsPatTime) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsPatTime);
		
	}
	public void update(DsPatTimeDet dsPatTimeDet) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsPatTimeDet);
		
	}
	public void create(DistrictSubDistrict districtSubDistrict) throws DataAccessException {
		getHibernateTemplate().save(districtSubDistrict);
		
	}
	public void create(DsSpecWork dsSpecWork) throws DataAccessException {
		getHibernateTemplate().save(dsSpecWork);
		
	}
	public void create(SubDistrict subDistrict) throws DataAccessException {
		getHibernateTemplate().save(subDistrict);
		
	}
	public void createOrUpdate(DistrictSubDistrict districtSubDistrict) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(districtSubDistrict);
		
	}
	public void createOrUpdate(DsSpecWork dsSpecWork) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsSpecWork);
		
	}
	public void createOrUpdate(SubDistrict subDistrict) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(subDistrict);
		
	}
	public void delete(DistrictSubDistrict districtSubDistrict) throws DataAccessException {
		getHibernateTemplate().delete(districtSubDistrict);
		
	}
	public void delete(DsSpecWork dsSpecWork) throws DataAccessException {
		getHibernateTemplate().delete(dsSpecWork);
		
	}
	public void delete(SubDistrict subDistrict) throws DataAccessException {
		getHibernateTemplate().delete(subDistrict);
		
	}
	public List getDistrictSubDistrictByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DistrictSubDistrict.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	public List getDsSpecWorkByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsSpecWork.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	public int getRowDistrictSubDistrictByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DistrictSubDistrict.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	public int getRowDsSpecWorkByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(DsSpecWork.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	public int getRowSubDistrictByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubDistrict.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	public List getSubDistrictByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubDistrict.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));	

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	public void update(DistrictSubDistrict districtSubDistrict) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(districtSubDistrict);
		
	}
	public void update(DsSpecWork dsSpecWork) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(dsSpecWork);
		
	}
	public void update(SubDistrict subDistrict) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(subDistrict);
		
	}
	
	
	
	
}
