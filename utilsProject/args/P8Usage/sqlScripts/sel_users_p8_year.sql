select [Object Store],count(*) 'Total', 
sum(case when cnt <120 then 1 else 0 end) 'Under 120 entries users count', 
sum(case when cnt <120 then 0 else 1 end) 'Upper 120 entries users count'
from (
	select creator,COUNT(*) cnt, 'Common' 'Object Store' from (
		select distinct e.creator,MONTH(e.create_date) m,DAY(e.create_date) d,
		(datepart(HOUR,e.create_date)*60+datepart(MINUTE,e.create_date))/15+1 q_no
		from obj.dbo.Event e, obj.dbo.classdefinition c
		where e.create_date between DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE())-12, 0) 
		   and DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()), 0)
		and e.creator not like '_Svc%' and e.creator != 'U545'
		and e.object_class_id = c.object_id
		and c.symbolic_name not in ('UpdateSecurityEvent','GetObjectEvent')
		)b
		group by creator
	union all
	select creator,COUNT(*) cnt, 'HR' 'Object Store'
	 from (
		select distinct e.creator,MONTH(e.create_date) m,DAY(e.create_date) d,
		(datepart(HOUR,e.create_date)*60+datepart(MINUTE,e.create_date))/15+1 q_no
		from obj1.dbo.Event e, obj1.dbo.classdefinition c
		where e.create_date between DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE())-12, 0) 
		   and DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()), 0)
		and e.creator not like '_Svc%' and e.creator != 'U545'
		and e.object_class_id = c.object_id
		and c.symbolic_name not in ('UpdateSecurityEvent','GetObjectEvent')
		)b
		group by creator
	union all
	select creator,COUNT(*) cnt,'Snifit' 'Object Store' from (
		select distinct e.creator,MONTH(e.create_date) m,DAY(e.create_date) d,
		(datepart(HOUR,e.create_date)*60+datepart(MINUTE,e.create_date))/15+1 q_no
		from obj2.dbo.Event e, obj2.dbo.classdefinition c
		where e.create_date between DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE())-12, 0) 
		   and DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()), 0)
		and e.creator not like '_Svc%' and e.creator != 'U545'
		and e.object_class_id = c.object_id
		and c.symbolic_name not in ('UpdateSecurityEvent','GetObjectEvent')
		)b
		group by creator
	union all
	select creator,COUNT(*) cnt, 'Special' 'Object Store'  from (
		select distinct e.creator,MONTH(e.create_date) m,DAY(e.create_date) d,
		(datepart(HOUR,e.create_date)*60+datepart(MINUTE,e.create_date))/15+1 q_no
		from objs3.dbo.Event e, objs3.dbo.classdefinition c
		where e.create_date between DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE())-12, 0) 
		   and DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()), 0)
		and e.creator not like '_Svc%' and e.creator != 'U545'
		and e.object_class_id = c.object_id
		and c.symbolic_name not in ('UpdateSecurityEvent','GetObjectEvent')
		)b
		group by creator
	) a
group by [Object Store]
