/**
 * 
 */
package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.DistrictSubDistrictDao;
import com.ie.icon.domain.DistrictSubDistrict;

/**   
 * @author budsaporn.j
 */
public class HibernateDistrictSubDistrictDao extends HibernateCommonDao implements DistrictSubDistrictDao {

	public List getSubDistricts(String provinceOid,String districtId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DistrictSubDistrict.class);
		criteria.add(Restrictions.eq("districtSubDistrictId.provinceOid", provinceOid));
		criteria.addOrder(Order.asc("districtSubDistrictId.districtId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getSubDistrictByDistrictAndProvince(String districtId,String provinceId) throws DataAccessException {
		//String query = "select distinct  pd.provinceZipcodeId.districtId ,dt.name " + " from District dt , ProvinceZipcode pd " + " where dt.districtId = pd.district " + " and pd.province =  :provinceId  order by dt.name ";
		String query =
		"SELECT distinct sd.subDistrictId,sd.name " +
		"FROM DistrictSubDistrict d , SubDistrict sd " +
		"WHERE d.districtSubDistrictId.subDistrictId = sd.subDistrictId " +
		"AND d.province = :provinceId " +
		"AND d.district = :districtId " +
		"AND sd.status = 'Y' " +
		"ORDER BY sd.name ";
		
		return getHibernateTemplate().findByNamedParam(query,  new String[] { "provinceId", "districtId"}, new Object[] {provinceId, districtId});
	}
	
	public List getSubDistrictEngByDistrictAndProvince(String districtId,String provinceId) throws DataAccessException {
		String query =
			"SELECT distinct sd.subDistrictId,sd.nameEng " +
			"FROM DistrictSubDistrict d , SubDistrict sd " +
			"WHERE d.districtSubDistrictId.subDistrictId = sd.subDistrictId " +
			"AND d.province = :provinceId " +
			"AND d.district = :districtId " +
			"AND sd.status = 'Y' " +
			"ORDER BY sd.nameEng ";
			
			return getHibernateTemplate().findByNamedParam(query,  new String[] { "provinceId", "districtId"}, new Object[] {provinceId, districtId});
	}
	
	public List getDistrictByZipCode(String zipCode) throws DataAccessException {
		String query = "SELECT distinct dt.districtId , dt.name , dt.nameEng " +
				       "FROM DistrictSubDistrict mdt, District dt " +
				       "WHERE mdt.districtSubDistrictId.districtId=dt.districtId " +
				       "AND mdt.zipCode = :zipCode " +
				       "AND mdt.status = 'Y' ";
		
		return getHibernateTemplate().findByNamedParam(query,  new String[] { "zipCode"}, new Object[] {zipCode});
	}
	
	
	public List getSubDistrictByDistrict(String districtId) throws DataAccessException {
		String query = "SELECT distinct ms.subDistrictId, ms.name, ms.nameEng " +
				       "FROM DistrictSubDistrict mdt, SubDistrict ms " +
				       "WHERE mdt.districtSubDistrictId.subDistrictId=ms.subDistrictId " +
				       "AND mdt.districtSubDistrictId.districtId = :districtId " +
				       "AND mdt.status = 'Y' ";
		
		return getHibernateTemplate().findByNamedParam(query,  new String[] { "districtId"}, new Object[] {districtId});
	}
	
	public List getProvinceByDistrict(String districtId) throws DataAccessException {
		String query = "SELECT distinct pv.objectId, pv.nameThai, pv.nameEng, pv.countryId " +
				       "FROM DistrictSubDistrict mdt, Province pv " +
				       "WHERE mdt.districtSubDistrictId.provinceOid=pv.objectId " +
				       "AND mdt.districtSubDistrictId.districtId = :districtId " +
				       "AND mdt.status = 'Y' ";
		
		return getHibernateTemplate().findByNamedParam(query,  new String[] { "districtId"}, new Object[] {districtId});
	}
}
