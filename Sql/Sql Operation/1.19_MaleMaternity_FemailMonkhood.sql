-- SQL for Male Maternity Leave - Female Monkhood Leave
declare
  @DateSt as varchar(10) = '2013-10-01', --Start Period
  @DateEn as varchar(10) = '2013-10-15'; --End Period
with leave as 
  (
  select 
   mev.tdesc as leavedesc
  , tls.employeeid
  , mp.tdesc as prefix ,e.fname,e.lname
  , tls.workarea
  , tls.create_date
  , tls.dateid
  , tls.lv_bg_date
  , tls.lv_en_date
  , datediff(day,dbo.todate(tls.lv_bg_date ,'YMD'),dbo.todate(tls.lv_en_date ,'YMD') )+1 leavedays
  , sum(datediff(day,dbo.todate(tls.lv_bg_date ,'YMD'),dbo.todate(tls.lv_en_date ,'YMD') )+1) over (partition by tls.employeeid, tls.lv_type) SumEmpID
  from tleave_summary tls
    , meventgrp mev
    , memployee e 
    , mprefix mp 
  where tls.lv_type = mev.eventgrpid
  and ((tls.lv_type = 'C' and e.sex = '1') or (tls.lv_type = 'D' and e.sex = '2'))
  and tls.employeeid = e.employeeid
  and e.emp_prefix = mp.prefixid
  and ((tls.lv_bg_date between @DateSt and @DateEn) or (tls.lv_en_date between @DateSt and @DateEn))
  )
select a.* 
from leave a;