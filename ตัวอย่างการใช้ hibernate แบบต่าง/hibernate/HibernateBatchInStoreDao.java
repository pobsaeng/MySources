package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.dao.BatchInStoreDao;
import com.ie.icon.domain.BatchInStore;
import com.ie.icon.domain.temp.TmpBatchInStore;
import com.ie.icon.domain.temp.TmpBatchInStore.Id;

public class HibernateBatchInStoreDao extends HibernateCommonDao implements BatchInStoreDao {

	public void update(BatchInStore batchInStore) {
		getHibernateTemplate().update(batchInStore);
	}
	
	public List getBatchInStore() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BatchInStore.class);
		criteria.addOrder(Order.asc("batchInStoreId.storeId"));
		criteria.addOrder(Order.asc("batchInStoreId.articleNo"));
		criteria.addOrder(Order.asc("batchInStoreId.batch"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}	
	
	public List getActiveBatchInStore() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BatchInStore.class);
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("createDateTime"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result; 
	}

	public void createOrUpdate(BatchInStore batchInStore) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(batchInStore);
		//getHibernateTemplate().save(batchInStore);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BatchInStore.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public List getBatchInStoreByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BatchInStore.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		List result = getHibernateTemplate().findByCriteria(criteria,first, max);
		
		if(result.size()==0)
			return null;
		else
			return result;
	}

	public int getTmpRowCount() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(TmpBatchInStore.class);
		criteria.setProjection(Projections.rowCount());
		List result = getHibernateTemplate().findByCriteria(criteria);
		return ((Integer) result.get(0)).intValue();
	}	
	
	public List getTmpBatchInStore() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(TmpBatchInStore.class);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("tmpBatchInStoreId.storeId"));
		projectionList.add(Projections.property("tmpBatchInStoreId.articleNo"));
		projectionList.add(Projections.property("tmpBatchInStoreId.batch"));
		criteria.setProjection(Projections.distinct(projectionList));
		
		criteria.addOrder(Order.asc("tmpBatchInStoreId.storeId"));
		criteria.addOrder(Order.asc("tmpBatchInStoreId.articleNo"));
		criteria.addOrder(Order.asc("tmpBatchInStoreId.batch"));

		List result = getHibernateTemplate().findByCriteria(criteria);
		List tmpBatchList = new ArrayList();
		if (result != null && !result.isEmpty()) {
			for (int i=0; i<result.size(); i++) {
				Object[] obj = (Object[])result.get(i);
				TmpBatchInStore tmpBatchInStore = new TmpBatchInStore();
				Id tmpBatchInStoreId = new Id();
				tmpBatchInStoreId.setStoreId(obj[0].toString());
				tmpBatchInStoreId.setArticleNo(obj[1].toString());
				tmpBatchInStoreId.setBatch(obj[2].toString());
				tmpBatchInStore.setTmpBatchInStoreId(tmpBatchInStoreId);
				tmpBatchList.add(tmpBatchInStore);
				    
			}
		} else {
			return null;
		}
		
		return tmpBatchList;
	}
	
	public List getTmpBatchInStoreGroupByStore() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(TmpBatchInStore.class);
		
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("tmpBatchInStoreId.storeId")));
		criteria.addOrder(Order.asc("tmpBatchInStoreId.storeId"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
	
	public List getTmpBatchInStoreFindByStoreId(String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(TmpBatchInStore.class);
		
		criteria.add(Restrictions.eq("tmpBatchInStoreId.storeId", storeId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if(result.size()==0)
			return null;
		else
			return result;
	}

	public List getBatchInStore(String storeId, String articleNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BatchInStore.class);
		criteria.add(Restrictions.eq("batchInStoreId.storeId", storeId));
		criteria.add(Restrictions.eq("batchInStoreId.articleNo", articleNo));
		criteria.add(Restrictions.eq("isActive", new Boolean(true)));
		criteria.addOrder(Order.asc("batchInStoreId.batch"));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		if(result.size()==0)
			return null;
		else
			return result;
	}

	public void deleteAllTmpBatchInStore(String storeId) throws DataAccessException {
		final String storeIdLocal = storeId;
		/*
		for (int i=0; i<tmpBatchInStoreList.size(); i++) {
			BatchInStore b = (BatchInStore)tmpBatchInStoreList.get(i);
			TmpBatchInStore tmpBatchInStore = new TmpBatchInStore();
			Id tmpBatchInStoreId = new Id();
			tmpBatchInStoreId.setStoreId(b.getBatchInStoreId().getStoreId());
			tmpBatchInStoreId.setArticleNo(b.getBatchInStoreId().getArticleNo());
			tmpBatchInStoreId.setBatch(b.getBatchInStoreId().getBatch());
			tmpBatchInStore.setTmpBatchInStoreId(tmpBatchInStoreId);
			getHibernateTemplate().delete(tmpBatchInStore);
		}
		*/
		
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "delete from tmp_batch_in_store where store_id = '" + storeIdLocal + "'" ;
				Query query = session.createQuery(queryString);
				//query.setString(1, storeIdLocal);
				session.close();
				return null;
			}
		});
	}

	public List getBatchInStoreFindByStoreId(String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(BatchInStore.class);
		
		criteria.add(Restrictions.eq("batchInStoreId.storeId", storeId));
		
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if(result.size()==0)
			return null;
		else
			return result;
	}	
}
