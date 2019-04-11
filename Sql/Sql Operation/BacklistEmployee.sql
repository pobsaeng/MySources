--SQL for Backlist Employee
Declare
  @DateSt as varchar(10) = '2013-11-01', --Start Period
  @DateEn as varchar(10) = '2013-11-15'; --End Period
select 
  row_number() over (order by
  case when m.STATUS = 'A' then cast((select max(ad.eff_date )  
                                        from hadjposition ad 
                                        where ad.employeeid = m.employeeid 
                                          and ad.adj_type = '30') as varchar)
      else m.RESIGNDATE
    end , m.EMPLOYEEID ) as No
  , m.EMPLOYEEID+' '+pf.TDESC+' '+m.FNAME+' '+m.lname as EmpName
  , m.ID_PEOPLE
  , m.RESIGNREASON+' '+r.TDESC as ResignReason
  , r.REMARKS
  , m.STATUS +' '+
  case m.STATUS when 'A' then 'ทำงาน'
                when 'F' then 'ไล่ออก'
                when 'X' then 'ปลดออก'
                when 'Y' then 'ลาออก'
                when 'Z' then 'เกษียณ'
  end STATUS
  , case when m.STATUS = 'A' then cast((select max(ad.eff_date )  
                                        from hadjposition ad 
                                        where ad.employeeid = m.employeeid 
                                          and ad.adj_type = '30') as varchar)
      else m.RESIGNDATE
    end resigndate
from MEMPLOYEE m
  , mprefix pf
  , mresignreason r
where m.RESIGNREASON = r.RESIGNREASONID
and m.EMP_PREFIX = pf.PREFIXID
and r.REASON_STATUS = 1
and case when m.STATUS = 'A' then cast((select max(ad.eff_date )  
                                        from hadjposition ad 
                                        where ad.employeeid = m.employeeid 
                                          and ad.adj_type = '30') as varchar)
      else m.RESIGNDATE
    end between @DateSt and @DateEn
order by 7,2