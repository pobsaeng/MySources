declare
  @DateSt varchar(10) = '2013-12-01',
  @DateEn varchar(10) = '2013-12-31';
select ROW_NUMBER() OVER(ORDER BY T.BU1,T.WORKAREA, T.EMPLOYEEID ) AS "ลำดับที่"
  , T.EMPLOYEEID AS "รหัสพนักงาน",concat(PRE.TDESC,E.FNAME,' ',E.LNAME) AS "ชื่อ-สกุล" 
  ,W.SIMPLIFIED_BRANCH AS "ร้าน",W.WORKAREAID AS CC,P.TDESC  AS "ตำแหน่ง",ST.TDESC  AS "ประเภทพนักงาน"
  ,RIGHT(CONVERT(VARCHAR(10), DATEADD(mm, 0, T.BESTCREWSDATE), 105), 7)  AS "พนักงานดีเด่นเดือน"
  ,PERIODDATE AS "รอบ" , T.GENDATE AS "วันที่สร้าง"
  , CASE WHEN T.BESTCREWSDATE <= DATEADD(mm, -2, PERIODDATE) THEN 'ตกหล่น' ELSE 'ปกติ' END AS "ตกหล่น"
  ,CONVERT(VARCHAR(10),DATEADD(MM, -3, @DateSt), 105) test
              from MEMPL_BESTCREWS T , MEMPLOYEE E ,MPOSITION P,MBU1 B1 , MWORKAREA W,MSALATYPE0 ST , MPREFIX PRE  
              where T.EMPLOYEEID = E.EMPLOYEEID AND T.EMP_POSITION = P.POSITIONID
              AND T.BU1=B1.BU1ID AND T.WORKAREA=W.WORKAREAID   
             AND E.SALATYPE=ST.CODEID  AND E.EMP_PREFIX=PRE.PREFIXID
            and PERIODDATE between @DateSt and @DateEn --'2013-12-01' and '2014-01-31'
/*and not exists (select '1' from MEMPL_BESTCREWS b 
                where b.EMPLOYEEID = t.EMPLOYEEID 
                and b.BESTCREWSDATE between CONVERT(VARCHAR(10),DATEADD(MM, -3, @DateSt), 105) and CONVERT(VARCHAR(10),DATEADD(DD, -1, @DateSt), 105)
                )*/
              ORDER BY T.BU1,T.WORKAREA, T.EMPLOYEEID
              
