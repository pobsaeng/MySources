-- SQL for check Emp_Type, SALATYPE, EMP_EVERY is blank or Null V2
select case when e.EMP_TYPE is null then 'Emp_Type Null'
            when e.SALATYPE is null then 'SALATYPE Null'
            when e.EMP_EVERY is null then 'EMP_EVERY Null'
            when e.EMP_TYPE = '' then 'Emp_Type Blank'
            when e.SALATYPE = '' then 'SALATYPE Blank'
            when e.EMP_EVERY = '' then 'EMP_EVERY Blank'
            else null
       end Cause
      , e.EMPLOYEEID
      , mp.TDESC as PREFIX ,e.fname, e.lname, e.WORKAREA
      , e.emp_type, e.salatype, e.EMP_EVERY
      , e.STATUS, e.CREATE_DATE, e.EDIT_DATE, e.EDIT_BY
from memployee e
    left outer join mprefix mp on
 e.emp_prefix = mp.prefixid
where (  (e.EMP_TYPE is null or e.SALATYPE is null or e.EMP_EVERY is null)
    or (e.EMP_TYPE = '' or e.SALATYPE = '' or e.EMP_EVERY = '') )
and isnumeric(e.EMPLOYEEID) = 1
order by e.EMPLOYEEID