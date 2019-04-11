package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.DistrictDao;
import com.ie.icon.domain.District;

public class HibernateDistrictDao extends HibernateCommonDao implements DistrictDao{
	
	public void create(District district) {
        getHibernateTemplate().save(district);
    }
	
	public District getDistrictByDistrictId(String districtId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(District.class);
		
		criteria.add(Restrictions.eq("districtId", districtId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() != 1 )
			return null;
		else
			return (District)result.get(0);
	}
	
	public List getDistricts() {
		DetachedCriteria criteria = DetachedCriteria.forClass(District.class);
		criteria.addOrder( Order.asc("name") );
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	
	public List getAllDistrict(String orderBy){
		DetachedCriteria criteria = DetachedCriteria.forClass(District.class);
		criteria.addOrder( Order.asc(orderBy) );
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getDistricts(String provinceOid) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(District.class);
		criteria.add(Restrictions.eq("provinceZipcodeId.provinceOid",
				provinceOid));
		criteria.addOrder(Order.asc("name"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.ie.icon.dao.DistrictDao#createOrUpdate(com.ie.icon.domain.District)
	 */
	public void createOrUpdate(District district) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(district);

	}
	public List getDistrictByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(District.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(District.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public void update(District district) throws DataAccessException {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(district);
	}
	
	public List getDistrictsUnknow() {
		DetachedCriteria criteria = DetachedCriteria.forClass(District.class);
		String disId = "9999";
		criteria.add(Restrictions.eq("districtId", disId));
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
}
