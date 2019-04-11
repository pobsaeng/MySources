--20131127 SQL for Report Check Transfer / Promote before 25th
Declare
  @DateSt as varchar(10) = '2013-11-01',
  @DateEn as varchar(10) = '2013-11-25',
  @DateEffSt as varchar(10) = '2013-12-01',
  @DateEffEn as varchar(10) = '2013-12-15';
with wk_data as (
select a.wf_id, a.COMPANYID
          ,a.RUNNO
          ,a.wf_status
          ,(case wf_status 
            when '1' then '�͡��͹��ѵ�'
            when '3' then '¡��ԡ���͹��ѵ�'
            when '4' then '͹��ѵ�����'
            when '5' then '���͹��ѵ�'
            when '6' then '����� HR'
            when '7' then '�觡�Ѻ'
            when '8' then '¡��ԡ�͡���'
            when '9' then '����ѵ�'
            else 'Unknown' end) as "WF_STATUS Desc."
		  ,dbo.ParseString(a.screen_value,'%#__wf__adj_type,%', '#__wf__adj_type,', '#') as "Adj_Type"
		  ,(case dbo.ParseString(a.screen_value,'%#__wf__adj_type,%', '#__wf__adj_type,', '#')
				when '10' then '��ҧҹ����'
				when '24' then '��Ѻ�Թ��͹'
				when '26' then '��Ѻ���˹�/��Ѻ�Թ��͹'
				when '30' then '����Ҿ'
				when '211' then 'Transfer Section,�͹����Ἱ�'
				when '221' then 'Transfer Section and Promote (RSC) , �͹��ҹἹ� �������͹���˹�'
				when '261' then 'Temporary to Full Time , ��Ѻ��Ҿ Temporary �� ��ѡ�ҹ��Ш������͹'
				when '252' then 'Full Time to Temporary , ��Ѻ��Ҿ ��ѡ�ҹ��Ш������͹� Temporary'
				when '212' then 'Transfer Store,�͹���� (��ҹ�Ң�)'
				when '222' then 'Position Change (Store) , �͹���µ��˹� [Same Level]'
				when '251' then 'Promote (Store) ,����͹���˹� (Stroe) [Diff. Level]'
				when '251' then 'Promote (RSC) ,����͹���˹� [Diff. Level]'
				when '223' then 'Transfer Store and Promote (Store) ,�͹���� (��ҹ�Ң�) �������͹���˹�'
				when '224' then 'Transfer Store and Position Change (Store) , �͹��ҹ (��ҹ�Ң�) ����͹���µ��˹�'
				when '262' then 'Staff Conversion to Full Time , ��Ѻ��Ҿ Part Time �� Full Time'
				when '263' then 'Full Time Conversion to Part Time , ��Ѻ��Ҿ Full Time �� Part Time'
				when '264' then 'Parttime (day hour) to Parttime (day hour) , ��Ѻ��Ҿ Part Time (����ѹ ��ª������) �� Part Time (����ѹ ��ª������)'
				else 'Unknown' end
		   ) as "AdjTypeDesc"
          ,a.INITIATOR,a.DOC_NO
          ,dbo.ParseString(a.screen_value,'%#__wf__employeeid,%','#__wf__employeeid,', '#') as "EMPID"
          ,dbo.ParseString(a.screen_value,'%#__wf__emp_position,%','#__wf__emp_position,', '#') as "EMPPOS"
          ,dbo.ParseString(a.screen_value,'%#__wf__emp_type,%','#__wf__emp_type,', '#') as "EMPTYPE"
          ,dbo.ParseString(a.screen_value,'%#__wf__salatype,%','#__wf__salatype,', '#') as "SALATYPE"
          ,dbo.ParseString(a.screen_value,'%#__wf__workarea,%', '#__wf__workarea,', '#') as "WORKAREA"
          ,dbo.ParseString(a.screen_value,'%#__wf__bu2,%', '#__wf__bu2,', '#') as "BU2"
          ,dbo.ParseString(a.screen_value,'%#__wf__old_emp_position,%','#__wf__old_emp_position,', '#') as "OLDEMPPOS"
          ,dbo.ParseString(a.screen_value,'%#__wf__old_emp_type,%','#__wf__old_emp_type,', '#') as "OLDEMPTYPE"
          ,dbo.ParseString(a.screen_value,'%#__wf__old_salatype,%','#__wf__old_salatype,', '#') as "OLDSALATYPE"
          ,dbo.ParseString(a.screen_value,'%#__wf__old_workarea,%', '#__wf__old_workarea,', '#') as "OLDWORKAREA"
          ,dbo.ParseString(a.screen_value,'%#__wf__old_bu2,%', '#__wf__old_bu2,', '#') as "OLDBU2"
		  ,CONCAT(SUBSTRING(SUBSTRING(a.screen_value,PATINDEX('%#__wf__eff_date,%',a.screen_value)+len('#__wf__eff_date,'),10),7,4),'-'
				 ,SUBSTRING(SUBSTRING(a.screen_value,PATINDEX('%#__wf__eff_date,%',a.screen_value)+len('#__wf__eff_date,'),10),4,2),'-'
				 ,SUBSTRING(SUBSTRING(a.screen_value,PATINDEX('%#__wf__eff_date,%',a.screen_value)+len('#__wf__eff_date,'),10),1,2)) as "EffDate"
		  ,a.CREATE_DATE as "OperationDate"
	  ,dbo.ParseString(a.screen_value,'%#__wf__emp_every,%', '#__wf__emp_every,', '#') as "EMPEVERY"
	  ,dbo.ParseString(a.screen_value,'%#__wf__old_emp_every,%', '#__wf__old_emp_every,', '#') as "OLDEMPEVERY"
	  ,dbo.ParseString(a.screen_value,'%#__wf__salary,%', '#__wf__salary,', '#') as "SALARY"
	  ,dbo.ParseString(a.screen_value,'%#__wf__old_salary,%', '#__wf__old_salary,', '#') as "OLDSALARY"
	from yum.dbo.WORKFLOW_DATA a
	where a.WF_ID in (2022,2023,2024)
     and a.CREATE_DATE between @DateSt and @DateEn
     and a.WF_STATUS <= 4
     and CONCAT(SUBSTRING(SUBSTRING(a.screen_value,PATINDEX('%#__wf__eff_date,%',a.screen_value)+len('#__wf__eff_date,'),10),7,4),'-'
				 ,SUBSTRING(SUBSTRING(a.screen_value,PATINDEX('%#__wf__eff_date,%',a.screen_value)+len('#__wf__eff_date,'),10),4,2),'-'
				 ,SUBSTRING(SUBSTRING(a.screen_value,PATINDEX('%#__wf__eff_date,%',a.screen_value)+len('#__wf__eff_date,'),10),1,2)) between @DateEffSt and @DateEffEn
)
select
    ai.ACTOR_ID as PendingID
    ,mi.FNAME+' '+mi.LNAME as PendingApprove    
    ,pi.TDESC as PendingPosition
    ,ai.ACTOR_SEQ_NO
    ,a.EffDate as "Effective Date"
	  ,a.adj_type as "Movement"
	  ,a.AdjTypeDesc
	  ,a."WF_STATUS Desc."
    ,a.EMPID as "EmployeeID"
	  ,f.tdesc as "ThTitle"
	  ,b.fname as "ThFirstName"
	  ,b.lname as "ThLastName"
    ,a.bu2 as "StoreID",e.SIMPLIFIED_BRANCH as "StoreShortName"
	  ,a.EMPPOS as "Position ID"
	  ,c.tdesc as "Position Desc."
      ,(case a.EMPTYPE
          when '1' then 'Full Time'
          when '2' then 'Part Time'
          when '3' then '�١��ҧ���Ǥ���'
          when '4' then '�ѭ�Ҩ�ҧ'
          when '5' then '��ѡ�ҹ������'
          when '6' then '��ѡ�ҹ����ѹ'
          when '7' then '�ѡ�֡�ҽ֡�ҹ'
          when '8' then '����'
          else 'Unknown' end) as "Employee Type"
      ,(case a.SALATYPE
          when  'Y1' then '��ª������'
          when  'Y2' then '����ѹ'
          when  'Y3' then '�����͹ Parttime'
          when  'Y4' then '�����͹��Ш�(Yum)'
          when  'Y5' then '�����͹��Ш�(RSC)'
          when  'Y6' then '�����͹������'
          when  'Y7' then '�ѡ�֡��'
          else 'Unknown' end) as "Salary Type"	  
      ,a.OLDBU2 as "OLD StoreID",g.SIMPLIFIED_BRANCH as "OLD StoreShortName"
	  ,a.OLDEMPPOS as "OLD Position ID"
    ,h.tdesc as "OLD Position Desc."
    ,(case a.OLDEMPTYPE
          when '1' then 'Full Time'
          when '2' then 'Part Time'
          when '3' then '�١��ҧ���Ǥ���'
          when '4' then '�ѭ�Ҩ�ҧ'
          when '5' then '��ѡ�ҹ������'
          when '6' then '��ѡ�ҹ����ѹ'
          when '7' then '�ѡ�֡�ҽ֡�ҹ'
          when '8' then '����'
          else 'Unknown' end) as "OLD Employee Type"
    ,(case a.OLDSALATYPE
          when  'Y1' then '��ª������'
          when  'Y2' then '����ѹ'
          when  'Y3' then '�����͹ Parttime'
          when  'Y4' then '�����͹��Ш�(Yum)'
          when  'Y5' then '�����͹��Ш�(RSC)'
          when  'Y6' then '�����͹������'
          when  'Y7' then '�ѡ�֡��'
          else 'Unknown' end) as "OLD Salary Type"
    ,a.EMPEVERY as "EMP EVERY"
    ,a.OLDEMPEVERY as "OLD EMP EVERY"
    ,a.SALARY
    ,a.OLDSALARY as "OLD SALARY"
	  ,a.OperationDate as "OperationDate"
    ,a.Doc_No
from wk_data a
     left outer join yum.dbo.memployee b
	 on a.EMPID = b.employeeid
	 left outer join yum.dbo.mposition c
	 on a.EMPPOS = c.positionid
	 left outer join yum.dbo.mbu2 d 
	 on a.bu2 = d.bu2id
	 left outer join yum.dbo.mworkarea e
	 on a.bu2 = e.workareaid
	 left outer join yum.dbo.mprefix f
	 on b.emp_prefix = f.prefixid
	 left outer join yum.dbo.mworkarea g
	 on a.OLDBU2 = g.workareaid
	 left outer join yum.dbo.mposition h
	 on a.OLDEMPPOS = h.positionid
   inner join ACTOR_INCIDENT ai 
   on a.RUNNO = ai.WF_SEQ_NO  
      and a.WF_ID = ai.WF_ID
      and ai.ACTOR_SEQ_NO = (select max(am.ACTOR_SEQ_NO ) from ACTOR_INCIDENT am where am.WF_ID = ai.wf_id and am.wf_seq_no = ai.wf_seq_no)
   left outer join MEMPLOYEE mi
   on ai.actor_id = mi.EMPLOYEEID
   left outer join mposition pi
	 on mi.EMP_POSITION = pi.positionid
where a.COMPANYID = '100'
order by a.EffDate,a.adj_type,a.EMPID
