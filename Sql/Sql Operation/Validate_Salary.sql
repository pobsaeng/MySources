--Validate salary From P'Yos
with compare_salary as (
select a.employeeid,a.adj_type,a.salatype as "salatype1",a.eff_date,a.status as "st1",b.status as "st2",a.edit_date
      ,a.emp_position as "emp_position1",d.pl as "PL1",d.tdesc as "posname1"
      ,b.emp_position as "emp_position2",e.pl as "PL2",e.tdesc as "posname2"
      ,a.EMP_EVERY as "empev1",b.EMP_EVERY as "empev2"
      ,a.TIME0 as "shif1",b.TIME0 as "shif2"
      ,yum.dbo.Base64Decoder(a.salary) as "sal1"
      ,yum.dbo.Base64Decoder(a.old_salary) as "oldsal1"
      ,yum.dbo.Base64Decoder(b.salary) as "sal2"
      ,yum.dbo.Base64Decoder(b.salary) as "sal21"
      ,yum.dbo.Base64Decoder(b.oldsalary) as "oldsa2"
      ,(case a.EMP_EVERY
        when 'H' then c.SALARY_HOUR
        when 'D' then c.SALARY_DAY
        when 'M' then c.SALARY_MONTH
        else '0.00' end) as "sal3"
      ,(case a.EMP_EVERY
        when 'H' then c.SALARY_HOUR
        when 'D' then c.SALARY_DAY
        when 'M' then c.SALARY_MONTH
        else '0.00' end) as "sal31"
      ,a.bu1 as "BU11"
      ,b.bu1 as "BU12"
      ,b.SALATYPE as "salatype2"
from yum.dbo.hadjposition a
     inner join yum.dbo.memployee b
     on a.employeeid = b.employeeid
     left outer join yum.dbo.BASE_SALARY c
     on b.bu1 = c.BU1 and a.EMP_POSITION = c.POSITIONID
     inner join yum.dbo.mposition d
     on a.EMP_POSITION = d.POSITIONID
     inner join yum.dbo.mposition e
     on b.EMP_POSITION = e.POSITIONID
where --b.emp_position = '1106021'
   (a.SALARY is null
   or a.SALARY =''
   or cast(yum.dbo.Base64Decoder(a.salary) as float) = 0)  --*/
   and a.eff_date = (select max(x.eff_date)
                    from yum.dbo.hadjposition x
                    where x.employeeid=a.employeeid
                      --and x.eff_date < '2013-11-01'
                    )
   and isnumeric(a.employeeid)=1 
   and a.adj_type <> '30'
--   and a.emp_position<>a.old_emp_position
)
   select a.employeeid,a.edit_date,a.adj_type,a.salatype1,a.eff_date,a.emp_position1,a.PL1,a.posname1,a.st1,a.st2
         ,a.salatype2,a.emp_position2,a.PL2,a.posname2,a.empev1,a.empev2,a.shif1,a.shif2
         ,a.sal1,a.oldsal1,cast(cast(a.sal21  as decimal(12,2)) as varchar(20)) as "sal21",a.oldsa2,a.sal31
         ,cast(cast((SELECT MAX(Vsalary) FROM (VALUES(a.sal2),(a.sal3)) AS T(Vsalary)) as decimal(12,2)) as varchar(20)) as "gsal"
         ,a.bu11,a.bu12
   from compare_salary a
   --where isnumeric(employeeid)=1 and adj_type <> '30' --and emp_position1 = '1106021'
   

