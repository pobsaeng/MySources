-- Check Data in MEMPL_RESIGN and HADJPOSITION
select r.EMPLOYEEID, r.RESIGNDATE, 'MEMPL_RESIGN not in HADJPOSITION' Cause, r.EDIT_BY, r.EDIT_DATE
from MEMPL_RESIGN r 
  left outer join HADJPOSITION ha on r.EMPLOYEEID = ha.EMPLOYEEID 
                                  and r.RESIGNDATE = ha.EFF_DATE 
                                  and ha.ADJ_TYPE = '30'
where ha.adj_type is null
union all
select ha.EMPLOYEEID, ha.EFF_DATE, 'HADJPOSITION not in MEMPL_RESIGN' Cause, ha.EDIT_BY, ha.EDIT_DATE
from HADJPOSITION ha
  left outer join MEMPL_RESIGN r on ha.EMPLOYEEID = r.EMPLOYEEID
                                  and ha.EFF_DATE = r.RESIGNDATE
where ha.ADJ_TYPE = '30'
and r.EMPLOYEEID is null
order by 1,3