package com.ie.icon.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.domain.sale.SpecialCharPeriod;
import com.ie.icon.domain.sale.SpecialCharPeriodItem;
import com.ie.icon.domain.sale.SpecialCharPeriodItemId;

public class JdbcSpecialCharPeriodDao extends JdbcDaoSupport implements
		com.ie.icon.dao.SpecialCharPeriodDao {

	private SimpleDateFormat sf;

	public JdbcSpecialCharPeriodDao() {
		sf = new SimpleDateFormat("dd/MM/yyyy");
	}

	public List getSpecialCharPeriod(String condition, String keyword,
			String status, Date frmDate, Date toDate, String store, Date lastUpdate) {

		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct sp.spc_char_period_oid,sp.description ");
		sql.append(" ,sp.spc_char,sp.is_active,sp.start_dt,sp.end_dt,sp.SPC_CHAR_ID ");
		sql.append(" ,sp.CREATE_DTTM,sp.CREATE_USER_ID,sp.LAST_UPD_DTTM,sp.LAST_UPD_USER_ID ");
		sql.append(" ,sp.LAST_PUB_DTTM,sp.REF_PUB_ID,sp.STORE_CVRGE,sp.SPC_DESC ");
		sql.append(" from spc_char_period sp ");
		sql.append(" left join spc_char_period_item item  ");
		sql.append(" on sp.spc_char_period_oid = item.spc_char_period_oid ");
		sql.append(" where 1=1 ");

		if (status != null && !status.equals("")) {
			sql.append(" and sp.is_active = '").append(status).append("' ");
		}
		//POR Begin add check Last Update
		if (lastUpdate != null ) {
			sql.append(" and TO_CHAR(sp.LAST_UPD_DTTM, 'DD/MM/YYYY') = '@3' ");
		}
		//POR End add check Last Update
		
		if (condition.equals("ARTICLE") || condition.equals("MCH")) {
			sql.append(" and upper(sp.SPC_CHAR_ID) = '").append(keyword.toUpperCase()).append("' ");
		} else if (condition.equals("SP")) {
			sql.append(" and upper(sp.spc_char) = '").append(keyword.toUpperCase()).append("' ");
		} else if (condition.equals("SPC_DESC")){
			if(keyword.startsWith("*")||keyword.endsWith("*")){
				String keyTemp = keyword;
				if(keyword.startsWith("*"))
					keyTemp = "%" + keyTemp.substring(1);
				if(keyword.endsWith("*"))
					keyTemp = keyTemp.substring(0,keyTemp.length()-1) +"%";
				
				sql.append(" and lower(sp.SPC_DESC) like '").append(keyTemp.trim().toLowerCase()).append("' ");
			}else{
				sql.append(" and lower(sp.SPC_DESC) = '").append(keyword.trim().toLowerCase()).append("' ");
			}
		}
		if(condition.equals("LASTEDIT")){
			if (frmDate != null && toDate != null) {
				sql.append(" and LAST_UPD_DTTM between to_date('@1','DD/MM/YYYY') and to_date('@2','DD/MM/YYYY') ");
			}
		}else{
			if (frmDate != null && toDate != null) {
				// (start1 <= end2 and start2 <= end1) If TRUE, then overlap
				// end2 >= start1 and start2 <= end1
				sql.append(" and ( to_date('@1','DD/MM/YYYY') <= sp.end_dt ");
				sql.append(" and sp.start_dt <= to_date('@2','DD/MM/YYYY') ) ");
			}
		}
		if (!store.equals("ALL")) {
			sql.append(" and ( item.store_id = '").append(store).append("' or ");
			sql.append(" sp.STORE_CVRGE = 'A' ) ");
		}
		sql.append(" order by SPC_CHAR_PERIOD_OID asc ");
		String query = sql.toString();
		if (frmDate != null && toDate != null) {
			query = query.replaceAll("@1", sf.format(frmDate));
			query = query.replaceAll("@2", sf.format(toDate));
		}
		if (lastUpdate != null ) {
			query = query.replaceAll("@3", sf.format(lastUpdate));
		}

		System.out.println(query);

		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(query, new RowMapperResultReader(
				new SpecialCharPeriodMapper()));

	}

	public List getSpecialCharPeriod(String condition, String keyword) {
		StringBuffer sql = new StringBuffer();

		if (condition.equals("MCH"))
			sql.append(" select distinct m.mch_id spc_char_id,m.mch_nm description, ");
		else
			sql.append(" select distinct m.artc_id spc_char_id,m.artc_dsc description, ");

		sql.append(" sp.spc_char,sp.is_active,sp.start_dt,sp.end_dt,sp.SPC_CHAR_PERIOD_OID ");
		sql.append(" ,sp.CREATE_DTTM,sp.CREATE_USER_ID,sp.LAST_UPD_DTTM,sp.LAST_UPD_USER_ID ");
		sql.append(" ,sp.LAST_PUB_DTTM,sp.REF_PUB_ID,sp.STORE_CVRGE,sp.SPC_DESC ");

		if (condition.equals("MCH"))
			sql.append(" from mch m left join spc_char_period sp ");
		else
			sql.append(" from artc m left join spc_char_period sp ");

		if (condition.equals("MCH")) {
			sql.append(" on m.mch_id=sp.spc_char_id where m.is_active = 'Y' ");
			sql.append(" and m.mch_id = '").append(keyword.trim().toUpperCase()).append("'");
		} else {
			sql.append(" on m.artc_id=sp.spc_char_id where m.is_active = 'Y' ");
			sql.append(" and m.artc_id = '").append(keyword).append("'");
		}
		sql.append(" order by SPC_CHAR_PERIOD_OID asc ");

		System.out.println(sql.toString());

		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(sql.toString(), new RowMapperResultReader(
				new SpecialCharPeriodMapper()));

	}

	public List getSpecialCharPeriodItemByOid(long oid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select SPC_CHAR_PERIOD_OID,STORE_ID from SPC_CHAR_PERIOD_ITEM ");
		sql.append(" where SPC_CHAR_PERIOD_OID = ").append(oid).append(" ");

		System.out.println(sql.toString());

		JdbcTemplate jt = getJdbcTemplate();
		return jt.query(sql.toString(), new SpecialCharPeriodItemMapper());
	}

	class SpecialCharPeriodItemMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			SpecialCharPeriodItem item = new SpecialCharPeriodItem();
			if (rs != null) {
				SpecialCharPeriodItemId id = new SpecialCharPeriodItemId();
				id.setSpcCharPeriodOid(rs.getLong("SPC_CHAR_PERIOD_OID"));
				id.setStoreId(rs.getString("STORE_ID"));
				item.setId(id);
			}
			return item;
		}
	}

	public void deleteSpecialCharItem(String id) {
		StringBuffer sql = new StringBuffer();

		sql.append(" delete from spc_char_period_item ");
		sql.append(" where SPC_CHAR_PERIOD_OID = '").append(id).append("'");

		JdbcTemplate jt = getJdbcTemplate();
		jt.update(sql.toString());
	}

	class SpecialCharPeriodMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			SpecialCharPeriod item = new SpecialCharPeriod();
			if (rs != null) {
				if (rs.getString("is_active") != null) {
					item.setIsActiveValue(rs.getString("is_active"));
					if (rs.getString("is_active").equalsIgnoreCase("Y")) {
						item.setIsActive(true);
						item.setStatus("Active");
					} else {
						item.setIsActive(false);
						item.setStatus("In-Active");
					}
				}
				if (rs.getString("spc_char_period_oid") != null)
					item.setSpcCharPeriodOid(rs.getLong("spc_char_period_oid"));
				if (rs.getString("description") != null)
					item.setDescription(rs.getString("description"));
				if (rs.getString("start_dt") != null)
					item.setStartDt(rs.getDate("start_dt"));
				if (rs.getString("end_dt") != null)
					item.setEndDt(rs.getDate("end_dt"));
				if (rs.getString("spc_char") != null)
					item.setSpcChar(rs.getString("spc_char"));
				if (rs.getString("spc_char_id") != null){
					item.setSpcCharId(rs.getString("spc_char_id"));
					item.setHierarchyLevel(item.getSpcCharId().replaceFirst("^0+(?!$)", ""));
				}
				
				if (rs.getString("CREATE_DTTM") != null)
					item.setCreateDttm(rs.getDate("CREATE_DTTM"));
				if (rs.getString("CREATE_USER_ID") != null)
					item.setCreateUserId(rs.getString("CREATE_USER_ID"));
				if (rs.getString("LAST_UPD_DTTM") != null)
					item.setLastUpdDttm(rs.getDate("LAST_UPD_DTTM"));
				if (rs.getString("LAST_UPD_USER_ID") != null)
					item.setLastUpdUserId(rs.getString("LAST_UPD_USER_ID"));
				if (rs.getString("LAST_PUB_DTTM") != null)
					item.setLastPubDttm(rs.getDate("LAST_PUB_DTTM"));
				if (rs.getString("REF_PUB_ID") != null)
					item.setRefPubId(rs.getString("REF_PUB_ID"));
				if (rs.getString("STORE_CVRGE") != null){
					item.setStoreCvrge(rs.getString("STORE_CVRGE"));
				}
				if (rs.getString("SPC_DESC") != null)
					item.setSpcDesc(rs.getString("SPC_DESC"));
			}
			return item;
		}
	}
	
	public int getNextSeqValue(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select spc_seq.nextval from dual ");

		JdbcTemplate jt = getJdbcTemplate();
		return jt.queryForInt(sql.toString());
	}

	public SpecialCharPeriodItem getSpecialCharPeriodItemById(SpecialCharPeriodItemId id) {
		// TODO Auto-generated method stub
		return null;
	}

	public SpecialCharPeriod getSpecialCharPeriodInRange(String oid, Date dateRange) {
		// TODO Auto-generated method stub
		return null;
	}

	public SpecialCharPeriod getSpecialCharPeriodInRangeLikeMch(String spcCharId, Date dateRange) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRowByUpdDttmGtPubDttm() throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	public List getSpecialCharPeriodByUpdDttmGtPubDttm(int first, int max)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateSpecialCharPeriod(SpecialCharPeriod obj) {
		// TODO Auto-generated method stub
		
	}

	public void createOrUpdate(SpecialCharPeriod sp) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	public List getActivePeriod(Date frmDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSpecialCharPeriodInRange(String spchrId, Date frmDate,
			Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}


}