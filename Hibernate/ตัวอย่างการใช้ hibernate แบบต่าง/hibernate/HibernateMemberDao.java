package com.ie.icon.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ie.icon.common.util.DateTimeUtil;
import com.ie.icon.constant.Constant;
import com.ie.icon.dao.MemberDao;
import com.ie.icon.domain.customer.CustomerMemberCard;
import com.ie.icon.domain.customer.MemberCardPrivilege;
import com.ie.icon.domain.customer.MemberCardType;
import com.ie.icon.domain.customer.MemberDiscountPrivilege;
import com.ie.icon.domain.customer.MemberDiscountType;
import com.ie.icon.domain.customer.MemberRewardType;
import com.ie.icon.domain.promotion.MemberCardCoverage;

public class HibernateMemberDao extends HibernateCommonDao implements MemberDao {

	public CustomerMemberCard getCustomerMemberCard(String cardNumber) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);

		criteria.add(Restrictions.eq("cardNumber", cardNumber));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else
			return (CustomerMemberCard) result.get(0);
	}
	
	public CustomerMemberCard getCustomerMemberCardAndType(String cardNumber) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);

		criteria.add((Restrictions.eq("cardNumber", cardNumber)));
		
		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else{
			CustomerMemberCard custMbrCard = (CustomerMemberCard)result.get(0);
			getHibernateTemplate().initialize(custMbrCard.getMemberCardType());
			return custMbrCard;
		}
		
	}

	public MemberCardPrivilege getMemberCardPrivilege(String memberCardTypeId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardPrivilege.class);
		criteria.add(Restrictions.le("startDate", today()));
		criteria.add(Restrictions.ge("endDate", today()));

		DetachedCriteria memberCardTypeCri = criteria.createCriteria("memberCardType");
		memberCardTypeCri.add(Restrictions.eq("memberCardTypeId", memberCardTypeId));

		criteria.addOrder(Order.desc("createDate"));

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() == 0)
			return null;
		else
			return (MemberCardPrivilege) result.get(0);
	}

	public void create(MemberDiscountType memberDiscountType) throws DataAccessException {
		getHibernateTemplate().save(memberDiscountType);
	}

	public void create(MemberRewardType memberRewardType) throws DataAccessException {
		getHibernateTemplate().save(memberRewardType);
	}

	public void delete(MemberDiscountType memberDiscountType) throws DataAccessException {
		getHibernateTemplate().delete(memberDiscountType);
	}
	public void delete(MemberRewardType memberRewardType) throws DataAccessException {
		getHibernateTemplate().delete(memberRewardType);
	}

	public MemberDiscountType getMemberDiscountType(String memberDiscountTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountType.class);

		criteria.add(Restrictions.eq("memberDiscountTypeId", memberDiscountTypeId));
		criteria.setFetchMode("memberDiscountPrivileges", FetchMode.JOIN);

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else
			return (MemberDiscountType) result.get(0);
	}

	public List getMemberDiscountTypes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountType.class);

		criteria.addOrder(Order.asc("memberDiscountTypeId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public MemberDiscountType getMemberDiscountTypeById(String memberDiscountTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountType.class);

		criteria.add(Restrictions.eq("memberDiscountTypeId", memberDiscountTypeId));
		criteria.setFetchMode("memberDiscountPrivileges", FetchMode.JOIN);

		List result = getHibernateTemplate().findByCriteria(criteria);

		return (MemberDiscountType) result.get(0);
	}
	
	public MemberRewardType getMemberRewardType(String memberRewardTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberRewardType.class);

		criteria.add(Restrictions.eq("memberRewardTypeId", memberRewardTypeId));
		criteria.setFetchMode("memberRewardPrivileges", FetchMode.JOIN);

		List result = getHibernateTemplate().findByCriteria(criteria);

		if (result.size() != 1)
			return null;
		else
			return (MemberRewardType) result.get(0);
	}  
	
	public List getMemberRewardTypes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberRewardType.class);

		criteria.addOrder(Order.asc("memberRewardTypeId"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	public void update(MemberDiscountType memberDiscountType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(memberDiscountType);
	}

	public void update(MemberRewardType memberRewardType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(memberRewardType);
	}

	public void update(MemberDiscountPrivilege memberDiscountPrivilege) throws DataAccessException {
		getHibernateTemplate().update(memberDiscountPrivilege);
	}
	
	public void delete(MemberDiscountPrivilege memberDiscountPrivilege) throws DataAccessException {
		getHibernateTemplate().delete(memberDiscountPrivilege);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#getCustomerMemberCardsByUpdDttmGtPubDttm()
	 */
	public List getCustomerMemberCardsByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#getMemberCardTypesByUpdDttmGtPubDttm()
	 */
	public List getMemberCardTypesByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setFetchMode("memberCardPrivileges", FetchMode.JOIN);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		List result = getHibernateTemplate().findByCriteria(criteria, first, max);
				
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#getmemberDiscountTypeByUpdDttmGtPubDttm()
	 */
	public List getMemberDiscountTypeByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
//		criteria.setFetchMode("memberDiscountPrivileges", FetchMode.JOIN);

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#create(com.ie.icon.domain.customer.CustomerMemberCard)
	 */
	public void create(CustomerMemberCard card) throws DataAccessException {
		getHibernateTemplate().save(card);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#delete(com.ie.icon.domain.customer.CustomerMemberCard)
	 */
	public void delete(CustomerMemberCard card) throws DataAccessException {
		getHibernateTemplate().delete(card);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#update(com.ie.icon.domain.customer.CustomerMemberCard)
	 */
	public void update(CustomerMemberCard card) throws DataAccessException {
		getHibernateTemplate().update(card);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#create(com.ie.icon.domain.customer.MemberCardType)
	 */
	public void create(MemberCardType cardType) throws DataAccessException {
		getHibernateTemplate().save(cardType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#delete(com.ie.icon.domain.customer.MemberCardType)
	 */
	public void delete(MemberCardType cardType) throws DataAccessException {
		getHibernateTemplate().delete(cardType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#update(com.ie.icon.domain.customer.MemberCardType)
	 */
	public void update(MemberCardType cardType) throws DataAccessException {
		getHibernateTemplate().update(cardType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#getCustomerMemberCardRowByUpdDttmGtPubDttm()
	 */
	public int getCustomerMemberCardRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#getMemberCardTypeRowByUpdDttmGtPubDttm()
	 */
	public int getMemberCardTypeRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ie.icon.dao.MemberDao#getMemberDiscountTypeRowByUpdDttmGtPubDttm()
	 */
	public int getMemberDiscountTypeRowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountType.class);

		criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), Restrictions.isNull("lastPublishedDateTime")));
		criteria.setProjection(Projections.rowCount());

		List result = getHibernateTemplate().findByCriteria(criteria);

		return ((Integer) result.get(0)).intValue();
	}

	public void createOrUpdate(CustomerMemberCard card) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(card);
	}

	public void createOrUpdate(MemberCardType cardType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(cardType);
	}

	public void createOrUpdate(MemberDiscountType memberDiscountType) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(memberDiscountType);
	}

	public List getMemberDiscountPrivileges(String memberDiscountTypeId, String storeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountPrivilege.class);

		criteria.add(Restrictions.or(Restrictions.eq("storeId", storeId), Restrictions.isNull("storeId")));
		criteria.createAlias("memberDiscountType", "memberDiscountType");
		criteria.add(Restrictions.eq("memberDiscountType.memberDiscountTypeId", memberDiscountTypeId));
		criteria.addOrder(Order.asc("storeId"));

		return getHibernateTemplate().findByCriteria(criteria);

	}
	
	public List getMemberDiscountPrivileges(String memberDiscountTypeId, String storeId, Date date, String mch3MchId, String mch2MchId, String mch1MchId, String mcMchId, String articleId, String mainUPC) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountPrivilege.class);

		criteria.add(Restrictions.or(Restrictions.eq("storeId", storeId), Restrictions.isNull("storeId")));

		Date truncatedDate = DateTimeUtil.truncate(date);
		
		criteria.add(Restrictions.le("startDate", truncatedDate));
		criteria.add(Restrictions.ge("endDate", truncatedDate));
		
		criteria.createAlias("memberDiscountType", "memberDiscountType");
		criteria.add(Restrictions.eq("memberDiscountType.memberDiscountTypeId", memberDiscountTypeId));

		criteria.createAlias("productHierarchy", "productHierarchy");

		Criterion criLevel9 = Restrictions.eq("productHierarchy.level", new Integer(9));
		Criterion criLevel8 = Restrictions.and(Restrictions.eq("productHierarchy.level", new Integer(8)), Restrictions.eq("hierarchyId", mch3MchId));
		Criterion criLevel7 = Restrictions.and(Restrictions.eq("productHierarchy.level", new Integer(7)), Restrictions.eq("hierarchyId", mch2MchId));
		Criterion criLevel6 = Restrictions.and(Restrictions.eq("productHierarchy.level", new Integer(6)), Restrictions.eq("hierarchyId", mch1MchId));
		Criterion criLevel5 = Restrictions.and(Restrictions.eq("productHierarchy.level", new Integer(5)), Restrictions.eq("hierarchyId", mcMchId));
		Criterion criLevel2 = Restrictions.and(Restrictions.eq("productHierarchy.level", new Integer(2)), Restrictions.eq("hierarchyId", articleId));
		Criterion criLevel1 = Restrictions.and(Restrictions.eq("productHierarchy.level", new Integer(1)), Restrictions.eq("hierarchyId", mainUPC));

		criteria.add(Restrictions.or(criLevel9, Restrictions.or(criLevel8, Restrictions.or(criLevel7, Restrictions.or(criLevel6, Restrictions.or(criLevel5, Restrictions.or(criLevel2, criLevel1)))))));

		criteria.addOrder(Order.asc("productHierarchy.level"));
		criteria.addOrder(Order.asc("storeId"));

		return getHibernateTemplate().findByCriteria(criteria);

	}
	
	public List getMemberDiscountPrivileges(String memberDiscountTypeId) throws DataAccessException{
		System.out.println("memberDiscountTypeId ---- >>>> "+memberDiscountTypeId);
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberDiscountPrivilege.class);

		criteria.createAlias("memberDiscountType", "memberDiscountType");
		criteria.add(Restrictions.eq("memberDiscountType.memberDiscountTypeId", memberDiscountTypeId));
		
		criteria.createAlias("productHierarchy", "productHierarchy");
		criteria.addOrder(Order.desc("productHierarchy.objectId"));
		criteria.addOrder(Order.asc("storeId"));
		criteria.addOrder(Order.asc("hierarchyId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List getMemberCardTypes() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardType.class);
		criteria.addOrder(Order.asc("memberCardTypeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List getMemberCardTypesRewardCards() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardType.class);
		criteria.add(Restrictions.eq("isRewardCard", new Boolean(true)));
		criteria.addOrder(Order.asc("memberCardTypeId"));
		return getHibernateTemplate().findByCriteria(criteria);
	}	
	public List getCustomerMemberCardsBySapId(String sapId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		criteria.add(Restrictions.eq("sapId", sapId));
		criteria.add(Restrictions.eq("status", new Character(Constant.CustomerMemberCardStatus.ACTIVE)));
		criteria.addOrder(Order.asc("cardNumber"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public MemberCardType getMemberCardTypesById(String memberCardTypeId) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardType.class);
		criteria.add(Restrictions.eq("memberCardTypeId", memberCardTypeId));
		criteria.addOrder(Order.asc("memberCardTypeId"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() != 1)
			return null;
		else
			return (MemberCardType) result.get(0);
	}

	public MemberCardCoverage getMemberCardCoverageById(String cardNo) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardCoverage.class);
		criteria.add(Restrictions.eq("cardNo", cardNo));
		criteria.addOrder(Order.asc("cardNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() != 1)
			return null;
		else   
			return (MemberCardCoverage) result.get(0);	
	}
	
	public MemberCardCoverage getMemberCardCoverage(String cardNo,long promotionOid) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemberCardCoverage.class);
		criteria.createAlias("promotion","promotion");
		criteria.add(Restrictions.eq("promotion.objectId", new Long(promotionOid)));
		criteria.add(Restrictions.eq("cardNo", cardNo));
		criteria.addOrder(Order.asc("cardNo"));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() != 1)
			return null;
		else
			return (MemberCardCoverage) result.get(0);
	}

	public void deleteMemberCardPrivilege(final MemberCardType cardType) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString = "delete from MemberCardPrivilege " +
						" where memberCardType = ?" ;
				Query query = session.createQuery(queryString);
				query.setString(0, cardType.getMemberCardTypeId());
				
//				 int rowCount = query.executeUpdate();
				session.close();
				
				return null;
			}
		});
	}

	public void deleteMemberDiscountPrivileges(final MemberDiscountType memberDiscountType) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback(){				
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String queryString = "delete from MemberDiscountPrivilege " +
						" where memberDiscountType = ?" ;
				Query query = session.createQuery(queryString);
				query.setString(0, memberDiscountType.getMemberDiscountTypeId());
				
			//	 int rowCount = query.executeUpdate();
				session.close();
				
				return null;
			}
		});
	}
	
	public int getCustomerMemberCardM4RowByUpdDttmGtPubDttm() throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.createAlias("memberCardType", "memberCardType");
        criteria.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.CustomerGroupId.M4));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}
	
	public List getCustomerMemberCardsM4ByUpdDttmGtPubDttm(int first, int max) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.createAlias("memberCardType", "memberCardType");
        criteria.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.CustomerGroupId.M4));
		
		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
	public List getCustomerMemberCardsByCustNoCardNo(String sapNo,
			String cardNo) throws DataAccessException {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		
		criteria.add(Restrictions.eq("cardNumber",cardNo));
        /*criteria.add(Restrictions.and(Restrictions.eq("sapId", sapNo), 
                Restrictions.eq("cardNumber",cardNo)));*/
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public CustomerMemberCard getCustomerMemberRewardCardsByCardNo(String cardNo) throws DataAccessException {
		System.out.print("++ getCustomerMemberRewardCardsByCardNo : cardNo = "+cardNo);
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		  
		criteria.add(Restrictions.eq("cardNumber",cardNo));
		criteria.createAlias("memberCardType", "memberCardType");
        criteria.add(Restrictions.eq("memberCardType.isRewardCard", new Boolean(true)));
		
        List result = getHibernateTemplate().findByCriteria(criteria);
		if (result.size() != 1){
			return null;   
		}else{
			CustomerMemberCard custMemberCard = (CustomerMemberCard)result.get(0);
			if(custMemberCard.getMemberCardType()!=null){
				getHibernateTemplate().initialize(custMemberCard.getMemberCardType());
			}
			return (CustomerMemberCard) result.get(0);
		}
	}
	
	public int getCustomerMemberCardMemberRowByUpdDttmGtPubDttm() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
        
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.createAlias("memberCardType", "memberCardType");
        criteria.add(Restrictions.disjunction().add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.CustomerGroupId.M8))
        										.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.MemberCardType.HomeCard))
        										.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.MemberCardType.ProCard)));
        criteria.setProjection(Projections.rowCount());
        
        List result = getHibernateTemplate().findByCriteria(criteria);
        
        return ((Integer)result.get(0)).intValue();
	}

	public List getCustomerMemberCardsMemberByUpdDttmGtPubDttm(int first, int max) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		
        criteria.add(Restrictions.or(Restrictions.gtProperty("lastUpdateDate", "lastPublishedDateTime"), 
                Restrictions.isNull("lastPublishedDateTime")));
        criteria.createAlias("memberCardType", "memberCardType");
        criteria.add(Restrictions.disjunction().add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.CustomerGroupId.M8))
				.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.MemberCardType.HomeCard))
				.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.MemberCardType.ProCard)));

		return getHibernateTemplate().findByCriteria(criteria, first, max);
	}
	
	
	 public List getCustomerMemberCardsLikeCardNo(String custNo,String[] grpList,String[] prefixList) throws DataAccessException{
		 DetachedCriteria criteria = DetachedCriteria.forClass(CustomerMemberCard.class);
		 //	criteria.add(Restrictions.like("cardNumber", "%"+custNo));
		    for(int i =0;i<prefixList.length;i++){
		    	prefixList[i] = prefixList[i]+custNo;
		    	System.out.println("Card No : "+prefixList[i]);
		    }
		 	criteria.add(Restrictions.in("cardNumber", prefixList));
		 	criteria.add(Restrictions.eq("status", new Character(Constant.CustomerMemberCardStatus.ACTIVE)));
	        criteria.createAlias("memberCardType", "memberCardType");
	        criteria.add(Restrictions.in("memberCardType.memberCardTypeId", grpList));
	   
	        /*
	        criteria.add(Restrictions.disjunction().add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.CustomerGroupId.M8))
					.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.MemberCardType.HomeCard))
					.add(Restrictions.eq("memberCardType.memberCardTypeId", Constant.MemberCardType.ProCard)));
	        */
	        
	    	return getHibernateTemplate().findByCriteria(criteria);
	 }
	
}
