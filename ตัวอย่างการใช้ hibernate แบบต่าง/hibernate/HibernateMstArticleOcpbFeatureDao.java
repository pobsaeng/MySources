package com.ie.icon.dao.hibernate;

import java.sql.Connection;
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

import com.ie.icon.constant.Constant;
import com.ie.icon.dao.MstArticleOcpbFeatureDao;
import com.ie.icon.domain.MSTArticleOCPBFeature;

public class HibernateMstArticleOcpbFeatureDao extends HibernateCommonDao implements MstArticleOcpbFeatureDao {

	public void update(MSTArticleOCPBFeature mstArticleOcpbFeature) {
		getHibernateTemplate().update(mstArticleOcpbFeature);
	}

	public void createOrUpdate(MSTArticleOCPBFeature mstArticleOcpbFeature) throws DataAccessException {
		getHibernateTemplate().save(mstArticleOcpbFeature);
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		/*
		DetachedCriteria criteria = DetachedCriteria.forClass(MSTArticleOCPBFeature.class);
		criteria.add(Restrictions.isNull("lastPublishedDateTime"));
		criteria.add(Restrictions.isNotNull("featureDesc"));
		criteria.add(Restrictions.eq("mSTArticleOCPBFeatureId.featureTitle", "Size"));
		criteria.createAlias("article", "article");
		criteria.add(Restrictions.like("article.mch.mchId", "FC%"));
		criteria.setProjection(Projections.rowCount());
		List result = getHibernateTemplate().findByCriteria(criteria);
		return ((Integer) result.get(0)).intValue();
		*/
		class ReturnValue  {
			List value;
		}		
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback(){
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String sql = "select new com.ie.icon.domain.MSTArticleOCPBFeature(LPAD(m.mSTArticleOCPBFeatureId.articleId,18,'0') ,m.mSTArticleOCPBFeatureId.featureTitle ,m.featureDesc) " +
							" from com.ie.icon.domain.MSTArticleOCPBFeature m, com.ie.icon.domain.mch.Article a " +
							" where to_number(m.mSTArticleOCPBFeatureId.articleId) = to_number(a.articleId) " +
							" and m.mSTArticleOCPBFeatureId.featureTitle = 'Size' " +
							" and m.lastPublishedDateTime is null ";
						//	" and a.mch.mchId like 'FC%' " ;
				
				Query query = session.createQuery(sql);
				rv.value = query.list();
				return null;
			}
		});
		if(rv.value != null)
			return rv.value.size();
		else 
			return 0;
		
	}

	public List getMstArticleOcpbFeatureByUpdDttmGtPubDttm(final int first, final int max)throws DataAccessException {
//		DetachedCriteria criteria = DetachedCriteria.forClass(MSTArticleOCPBFeature.class);
//		criteria.add(Restrictions.isNull("lastPublishedDateTime"));
//		criteria.add(Restrictions.isNotNull("featureDesc"));
//		criteria.add(Restrictions.eq("mSTArticleOCPBFeatureId.featureTitle", "Size"));
//		criteria.createAlias("article", "article");
//		criteria.add(Restrictions.like("article.mch.mchId", "FC%"));
//		List result = getHibernateTemplate().findByCriteria(criteria,first, max);
//		return result;
		class ReturnValue  {
			List value;
		}		
		final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String sql = "select new com.ie.icon.domain.MSTArticleOCPBFeature(LPAD(m.mSTArticleOCPBFeatureId.articleId,18,'0') ,m.mSTArticleOCPBFeatureId.featureTitle ,m.featureDesc) " +
						" from com.ie.icon.domain.MSTArticleOCPBFeature m, com.ie.icon.domain.mch.Article a " +
						" where to_number(m.mSTArticleOCPBFeatureId.articleId) = to_number(a.articleId) " +
						" and m.mSTArticleOCPBFeatureId.featureTitle = 'Size' " +
						" and m.lastPublishedDateTime is null " +
					//	" and a.mch.mchId like 'FC%' " +
						" and rownum > ? " +
						" and rownum <= ? " + 
						" order by m.mSTArticleOCPBFeatureId.articleId , m.mSTArticleOCPBFeatureId.featureTitle ";
				
				Query query = session.createQuery(sql);
				query.setInteger(0, first);
				query.setInteger(1, max);
				rv.value = query.list();
				return null;
			}
		});

		return rv.value;
		
	}
}
