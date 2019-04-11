/*
 * Created on Sep 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ProvinceDao;
import com.ie.icon.domain.Province;

/**
 * @author visawee
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class HibernateProvinceDao extends HibernateCommonDao implements
		ProvinceDao {

	public void create(Province province) {
		getHibernateTemplate().save(province);
	}

	public void delete(Province province) {
		getHibernateTemplate().delete(province);
	}

	public void update(Province province) {
		getHibernateTemplate().update(province);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.ProvinceDao#getAllProvince()
	 */
	public List getAllProvince(String orderBy) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
		criteria.addOrder( Order.asc(orderBy) );
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.ProvinceDao#getProvinceByID(java.lang.String)
	 */
	public Province getProvinceByID(long provinceID) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);

			criteria.add(Restrictions.eq("objectId",new Long(provinceID)));

			List provinceList = getHibernateTemplate().findByCriteria(criteria);
			
			if (provinceList.size() == 1) {
				return (Province) provinceList.get(0);
			} else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public Province getProvinceByThaiName(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
		
		criteria.add(Restrictions.eq("nameThai", name));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 0 )
			return null;
		else
			return (Province)result.get(0);
	}

	public void createOrUpdate(Province province) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(province);
	}

	public List getProvinceByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	public List getProvinceUnknow() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
		criteria.add(Restrictions.eq("objectId",new Long(99)));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if ( result.size() == 0 )
			return result;
		else
			return result;
	}
	
	public List getProvinceIdByProvinceName(String provinceName){
		DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
		criteria.add(Restrictions.eq("nameThai", provinceName));
		return 	getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getProvinceIdByProvinceNameEng(String provinceName){
		DetachedCriteria criteria = DetachedCriteria.forClass(Province.class);
		criteria.add(Restrictions.eq("nameEng", provinceName));
		return 	getHibernateTemplate().findByCriteria(criteria);
	}
	
}
