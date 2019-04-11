package com.ie.icon.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultReader;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ie.icon.dao.CashierAdjustCauseGroupJdbcDao;
import com.ie.icon.dao.jdbc.JdbcCashierTransactionAdjustDao.CashierBalanceAdjustMapper;
import com.ie.icon.domain.cashier.CashierAdjustCauseGroup;
import com.ie.icon.domain.sale.CashierBalanceAdjust;

public class JdbcCashierAdjustCauseGroupDao  extends JdbcDaoSupport implements CashierAdjustCauseGroupJdbcDao{
	
	public String getMaxCashierAdjustCauseGroupId() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT MAX(TO_NUMBER(CSH_ADJ_CAUSE_GRP_ID))+1 AS NEXT_ID FROM CSH_ADJ_CAUSE_GRP ");
		
		JdbcTemplate jt = getJdbcTemplate();
		List list = jt.query(sql.toString(), new RowMapperResultReader(new MaxCashierAdjustCauseGroupIdMap()));
		String str = "";
		if(list != null && list.size() > 0){
			str = (String)list.get(0);
		}
		return str;
	}

	class MaxCashierAdjustCauseGroupIdMap implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			return rs.getString("NEXT_ID");
		}
	}
	
	public List getAllCashierAdjustCauseGroup() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* ,TO_NUMBER(A.CAUSE_GRP_SEQ) AS SEQ  ");
		sql.append(" FROM CSH_ADJ_CAUSE_GRP A ");
		sql.append(" ORDER BY SEQ ");
		
		JdbcTemplate jt = getJdbcTemplate();
		List list = jt.query(sql.toString(), new RowMapperResultReader(new AllCashierAdjustCauseGroupMap()));
		return list;
	}
	
	class AllCashierAdjustCauseGroupMap implements RowMapper {
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			CashierAdjustCauseGroup causeGroup = new CashierAdjustCauseGroup();
			causeGroup.setCashierAdjustCauseGroup(rs.getString("CSH_ADJ_CAUSE_GRP_ID"));
			causeGroup.setCashierGroupDesc(rs.getString("CAUSE_GRP_DESC"));
			causeGroup.setCashierGroupSeq(rs.getString("CAUSE_GRP_SEQ"));
			String isActive = rs.getString("IS_ACTIVE");
			if(isActive !=null && !isActive.trim().equals("") && isActive.trim().equalsIgnoreCase("Y")){
				causeGroup.setIsActive(new Boolean(true));
			}else{
				causeGroup.setIsActive(new Boolean(false));
			}
			causeGroup.setCreateDttm(rs.getTimestamp("CREATE_DTTM"));
			causeGroup.setLastUpdateDate(rs.getTimestamp("LAST_UPD_DTTM"));
			return causeGroup;
		}
	}
}
