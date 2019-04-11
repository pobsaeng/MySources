Declare
  @DateSt as varchar(10) = '',
  @DateEn as varchar(10) = '';
select 
  rank() over (order by ha.employeeid) No
  ,ha.CREATE_BY
  ,ha.CREATE_DATE
  ,ha.employeeid
  ,f.TDESC+m.fname+' '+m.lname Th_Name
  ,f.EDESC+m.efname+' '+m.elname En_Name
  ,ha.emp_position+' : '+p.tdesc Position
  --,ha.bu1
  --,ha.bu2
  ,ha.workarea+' : '+w.tdesc Store
  ,ha.EFF_DATE Start_Date
  ,r.RESIGNDATE Last_ResignDate
  ,dbo.Base64Decoder(ha.salary) salary
  ,mb.ACCOUNTID
  ,b.EDESC Bank_Name
  ,m.ID_PEOPLE
  ,m.BIRTHDAY
  ,ha.REMARKS
from HADJPOSITION ha
  left outer join MEMPLOYEE m on ha.EMPLOYEEID = m.EMPLOYEEID
  left outer join MPREFIX f on m.EMP_PREFIX = f.PREFIXID
  left outer join mposition p on ha.emp_position = p.positionid
  left outer join mworkarea w on ha.workarea = w.workareaid
  left outer join MEMPL_BANK mb on ha.EMPLOYEEID = mb.EMPLOYEEID
  left outer join MBANK b on mb.BANKID = b.BANKID
  left outer join MEMPL_RESIGN r on ha.EMPLOYEEID = r.EMPLOYEEID and r.RESIGNDATE = (select max(r1.RESIGNDATE) from MEMPL_RESIGN r1 where r1.EMPLOYEEID = ha.EMPLOYEEID and r1.COMPANYID = '100')
where ha.ADJ_TYPE = '10'
and ha.WORKAREA = 'RSC'
and ISNUMERIC(ha.EMPLOYEEID) = 1
and ha.EFF_DATE between @DateSt and @DateEn
order by ha.employeeid;