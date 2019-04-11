Declare
  @DateSt as varchar(10) = '2014-01-01',
  @DateEn as varchar(10) = '2014-02-01';
select 
  rank() over (order by ha.employeeid) No
  ,m.CREATE_BY+' : '+cr.fname+' '+cr.lname CREATE_BY
  ,m.CREATE_DATE
  ,ha.employeeid
  ,f.TDESC+m.fname+' '+m.lname Th_Name
  ,f.EDESC+m.efname+' '+m.elname En_Name
  ,ha.emp_position+' : '+p.tdesc Position
  ,ha.workarea+' : '+w.tdesc Store
  ,ha.EFF_DATE Start_Date
  ,cast(dbo.Base64Decoder(ha.salary) as DECIMAL) as salary
  ,mb.ACCOUNTID
  ,b.EDESC Bank_Name
  ,m.ID_PEOPLE
  ,m.BIRTHDAY
  ,m.salatype +' : '+
  (case m.SALATYPE
      when  'Y1' then 'รายชั่วโมง'
      when  'Y2' then 'รายวัน'
      when  'Y3' then 'รายเดือน Parttime'
      when  'Y4' then 'รายเดือนประจำ(Yum)'
      when  'Y5' then 'รายเดือนประจำ(RSC)'
      when  'Y6' then 'รายเดือนบริหาร'
      when  'Y7' then 'นักศึกษา'
      when  'Y8' then 'EXPAT'
      else 'Unknown' end) as salatype 
  ,ha.REMARKS
from HADJPOSITION ha
  left outer join MEMPLOYEE m on ha.EMPLOYEEID = m.EMPLOYEEID
  left outer join MPREFIX f on m.EMP_PREFIX = f.PREFIXID
  left outer join mposition p on ha.emp_position = p.positionid
  left outer join mworkarea w on ha.workarea = w.workareaid
  left outer join MEMPL_BANK mb on ha.EMPLOYEEID = mb.EMPLOYEEID
  left outer join MBANK b on mb.BANKID = b.BANKID
  left outer join MEMPLOYEE cr on cr.EMPLOYEEID = m.EMPLOYEEID
where ha.ADJ_TYPE = '10'
and ha.WORKAREA = 'RSC'
and ISNUMERIC(ha.EMPLOYEEID) = 1
and ha.EFF_DATE between @DateSt and @DateEn
order by ha.employeeid;