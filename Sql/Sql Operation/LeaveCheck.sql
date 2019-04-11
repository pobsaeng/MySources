-- SQL for Maternity Leave - Monkhood Leave
declare
  @lv_type as varchar(1) = 'C', -- Type of Leave
  @Num as int = 10, -- Flag Condition
  @DateSt as varchar(10) = '2013-10-01', --Start Period
  @DateEn as varchar(10) = '2013-10-15'; --End Period
with leave as 
  (
  select 
   mev.tdesc as leavedesc
  , tls.employeeid
  , mp.tdesc as prefix ,e.fname,e.lname
  , tls.workarea
  --, tls.lv_type
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
  and tls.lv_type = @lv_type --in ('c','d')
  and tls.employeeid = e.employeeid
  and e.emp_prefix = mp.prefixid
  and ((tls.lv_bg_date between @DateSt and @DateEn) or (tls.lv_en_date between @DateSt and @DateEn))
  )
select a.* 
, case when a.sumEmpID <= @Num then 'Please Check'
      else null
  end Flag
from leave a
--where a.SumEmpID <= @Num ;