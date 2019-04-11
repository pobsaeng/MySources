/**
 * 
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.ProvinceZipcodeDao;
import com.ie.icon.domain.ProvinceZipcode;

/**
 * @author pawans
 */
public class HibernateProvinceZipcodeDao extends HibernateCommonDao implements ProvinceZipcodeDao {

	public List getZipcodes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceZipcode.class);
		criteria.addOrder(Order.asc("provinceZipcodeId.zipcode"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getZipcodes(String provinceOid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceZipcode.class);
		criteria.add(Restrictions.eq("provinceZipcodeId.provinceOid", provinceOid));
		criteria.addOrder(Order.asc("provinceZipcodeId.zipcode"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getZipcodesByDistrict(String districtId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceZipcode.class);
		criteria.add(Restrictions.eq("provinceZipcodeId.districtId", districtId));
		criteria.addOrder(Order.asc("provinceZipcodeId.zipcode"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getZipcodesByDistrictAndProvince(String districtId,String province) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceZipcode.class);
		criteria.add(Restrictions.eq("provinceZipcodeId.districtId", districtId));
		criteria.add(Restrictions.eq("provinceZipcodeId.provinceOid", province));
		criteria.addOrder(Order.asc("provinceZipcodeId.zipcode"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	// public List getDistrictByProvince(String provinceOid) {
	// DetachedCriteria criteria = DetachedCriteria
	// .forClass(ProvinceZipcode.class).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("provinceOid"))
	// .add(Projections.property("districtId"))));
	// criteria.add(Restrictions.eq("provinceZipcodeId.provinceOid",
	// provinceOid));
	// criteria.addOrder(Order.asc("provinceZipcodeId.districtId"));
	// return getHibernateTemplate().findByCriteria(criteria);
	// }

	public List getDistrictByProvince(String provinceId) throws DataAccessException {
		String query = "select distinct  pd.provinceZipcodeId.districtId ,dt.name " + " from District dt , ProvinceZipcode pd " + " where dt.districtId = pd.district " + " and pd.province =  :provinceId  order by dt.name ";

		return getHibernateTemplate().findByNamedParam(query, "provinceId", provinceId);
	}
	
	public List getDistrictEngByProvince(String provinceId) throws DataAccessException {
		String query = "select distinct  pd.provinceZipcodeId.districtId ,dt.nameEng " + " from District dt , ProvinceZipcode pd " + " where dt.districtId = pd.district " + " and pd.province =  :provinceId  order by dt.nameEng  ";

		return getHibernateTemplate().findByNamedParam(query, "provinceId", provinceId);
	}
	
	public List getprovinceAndDistrictByZipcode(String zipcodeId) throws DataAccessException {
		String query = "select distinct  pd.provinceZipcodeId.districtId ,dt.name ,  pd.provinceZipcodeId.provinceOid  " + " from District dt , ProvinceZipcode pd " + " where dt.districtId = pd.district " + " and pd.provinceZipcodeId.zipcode =  :zipcodeId  order by dt.name ";

		return getHibernateTemplate().findByNamedParam(query, "zipcodeId", zipcodeId);
	}

	public void createOrUpdate(ProvinceZipcode provinceZipcode) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(provinceZipcode);
	}

	public List getProvinceZipcodeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceZipcode.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceZipcode.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public void update(ProvinceZipcode provinceZipcode) throws DataAccessException {
		getHibernateTemplate().update(provinceZipcode);
	}

	public List getZipcodesUnknow() {
		String zipcode = "99999";
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinceZipcode.class);
		criteria.add(Restrictions.eq("provinceZipcodeId.zipcode",zipcode ));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
}
