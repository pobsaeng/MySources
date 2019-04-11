with TTIME_T1_ONLY as 
( select distinct t.EMPLOYEEID, t.SWIPEDATE, t.WORKAREA,t.WF_STATUS 
  from TTIMETEMP t  
  where t.SWIPEDATE between '2013-12-01' and '2013-12-31'    
  and t.SWIPETYPE = '1'    
  AND T.WF_STATUS <> '9'    
  --AND ((select count(workarea) from APPEND_MONEY_WORKAREA) = 0 or t.WORKAREA IN (SELECT WORKAREA FROM APPEND_MONEY_WORKAREA))    
  and not exists ( select TT.EMPLOYEEID from TTIMETEMP tt       
                    where tt.SWIPEDATE between '2013-12-01' and '2013-12-31'      
                    and tt.SWIPEDATE = t.SWIPEDATE       
                    AND tt.EMPLOYEEID=T.EMPLOYEEID       
                    and tt.SWIPETYPE = '4'      
                    AND tt.WF_STATUS <> '9'    )  )
, 
TTIME_T1_OVER_T4 as (
  select distinct t.EMPLOYEEID, t.SWIPEDATE, t.WORKAREA,t.WF_STATUS from TTIMETEMP t      
  where t.SWIPEDATE between '2013-12-01' and '2013-12-31'    
  and t.SWIPETYPE = '1'    
  AND T.WF_STATUS <> '9'    
 -- AND ((select count(workarea) from APPEND_MONEY_WORKAREA) = 0 or t.WORKAREA IN (SELECT WORKAREA FROM APPEND_MONEY_WORKAREA))    
  and  exists ( select TT.EMPLOYEEID from TTIMETEMP tt       
                    where tt.SWIPEDATE between '2013-12-01' and '2013-12-31'      
                    and tt.SWIPEDATE = t.SWIPEDATE       
                    AND tt.EMPLOYEEID=T.EMPLOYEEID       
                    and tt.SWIPETYPE = '4'      
                    AND tt.WF_STATUS <> '9'    )  
  and t.SWIPETIME > ( select MAX(TT.SWIPETIME) from TTIMETEMP tt       
                      where tt.SWIPEDATE between '2013-12-01' and '2013-12-31'      
                      and tt.SWIPEDATE = t.SWIPEDATE   
                      AND tt.EMPLOYEEID=T.EMPLOYEEID       
                      and tt.SWIPETYPE = '4'      
                      AND tt.WF_STATUS <> '9'    )  )
, 
TTIME_T4_ONLY as (
  select distinct t.EMPLOYEEID, t.SWIPEDATE, t.WORKAREA,t.WF_STATUS from TTIMETEMP t     
  where t.SWIPEDATE between '2013-12-02' and '2014-01-01'    
  and t.SWIPETYPE = '4'    
  AND T.WF_STATUS <> '9'    
  --AND ((select count(workarea) from APPEND_MONEY_WORKAREA) = 0 or t.WORKAREA IN (SELECT WORKAREA FROM APPEND_MONEY_WORKAREA))    
  and not exists (select tt.EMPLOYEEID from TTIMETEMP tt       
                  where tt.SWIPEDATE between '2013-12-02' and '2014-01-01'      
                  and tt.SWIPEDATE = t.SWIPEDATE      
                  AND tt.EMPLOYEEID=T.EMPLOYEEID       
                  and tt.SWIPETYPE = '1'      
                  AND tt.WF_STATUS <> '9'    )  )
, 
TTIME_T4_UNDER_T1 as (    
  select distinct t.EMPLOYEEID, t.SWIPEDATE, t.WORKAREA,t.WF_STATUS from TTIMETEMP t     
  where t.SWIPEDATE between '2013-12-02' and '2014-01-01'    
  and t.SWIPETYPE = '4'    
  AND T.WF_STATUS <> '9'    
  --AND ((select count(workarea) from APPEND_MONEY_WORKAREA) = 0 or t.WORKAREA IN (SELECT WORKAREA FROM APPEND_MONEY_WORKAREA))    
  and exists (select tt.EMPLOYEEID from TTIMETEMP tt       
                  where tt.SWIPEDATE between '2013-12-02' and '2014-01-01'      
                  and tt.SWIPEDATE = t.SWIPEDATE      
                  AND tt.EMPLOYEEID=T.EMPLOYEEID       
                  and tt.SWIPETYPE = '1'      
                  AND tt.WF_STATUS <> '9'    )
  and t.SWIPETIME < (  select MIN(TT.SWIPETIME) from TTIMETEMP tt       
                        where tt.SWIPEDATE between '2013-12-02' and '2014-01-01'      
                        and tt.SWIPEDATE = t.SWIPEDATE       
                        AND tt.EMPLOYEEID=T.EMPLOYEEID       
                        and tt.SWIPETYPE = '1'      
                        AND tt.WF_STATUS <> '9'    )  )
, TTIME_T1 AS (    
SELECT * FROM TTIME_T1_ONLY    UNION ALL    SELECT * FROM TTIME_T1_OVER_T4  )
, TTIME_T4 AS (    
SELECT * FROM TTIME_T4_ONLY    UNION ALL    SELECT * FROM TTIME_T4_UNDER_T1  )

, TA_ALL AS (  
  SELECT distinct
  t1.EMPLOYEEID , t1.SWIPEDATE as DATEID , t1.WORKAREA
  FROM TTIME_T1 T1, TTIME_T4  T4   
  WHERE   T1.EMPLOYEEID=T4.EMPLOYEEID    
  AND cast(T4.SWIPEDATE AS DATE) = DATEADD (DAY , 1 , cast(T1.SWIPEDATE AS DATE) )  
  AND T1.WF_STATUS <> '9' AND  T4.WF_STATUS <> '9'  
  union all 
  select distinct t.EMPLOYEEID , t.SWIPEDATE , t.WORKAREA   
  from TTIMETEMP t , TTIMETEMP t4  
  where t.SWIPEDATE = t4.SWIPEDATE   
  and t.EMPLOYEEID = t4.EMPLOYEEID   
  and t.SWIPETYPE = '1' 
  and t4.SWIPETYPE = '4'  
  and t.WF_STATUS <> '9' 
  and t4.WF_STATUS <> '9'  
  and t.SWIPETIME < t4.SWIPETIME  )
, 
LD_ALL AS (  
  select tt.EMPLOYEEID, tt.DATEID, tt.WORKAREA from TTIME_CURRENT1 tt   
  where tt.DATEID between '2013-12-01' and '2013-12-31'   
  and ( tt.C_TM_BG <> '0.00' or tt.C_TM_EN <> '0.00')  
  --AND ((select count(workarea) from APPEND_MONEY_WORKAREA) = 0 or tt.WORKAREA IN (SELECT WORKAREA FROM APPEND_MONEY_WORKAREA))  
  )
, 
TA_LD_ALL AS (  
  SELECT ld.EMPLOYEEID,ld.DATEID,ld.WORKAREA FROM LD_ALL ld , TA_ALL ta   
  where ld.EMPLOYEEID=ta.EMPLOYEEID and ld.DATEID=ta.DATEID   
)

select alldate.EMPLOYEEID,alldate.DATEID ,  alldate.WORKAREA, m.FNAME+' '+ m.LNAME as emp_name 
, wk.TDESC as store_name ,wk.WORK_EMAIL as store_mail
, m_ac.FNAME+' '+ m_ac.LNAME as ac_name
, m_ac.EMAIL as ac_mail
from TA_LD_ALL alldate 
left outer join MEMPLOYEE m on m.EMPLOYEEID = alldate.EMPLOYEEID
left outer join mworkarea wk on wk.WORKAREAID = alldate.WORKAREA
left outer join MEMPLOYEE m_ac  on m_ac.EMPLOYEEID = wk.RESPORSIBLE2
where   exists (select 1 from TTIME_CURRENT t  
                where t.EMPLOYEEID=alldate.EMPLOYEEID 
                and t.DATEID=alldate.DATEID 
                and t.HOUR_D = '0.00' and LV = '0.00'  
                )  

ORDER BY alldate.WORKAREA, alldate.EMPLOYEEID, alldate.DATEID