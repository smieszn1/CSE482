select Name, avg(TP) as avg_tp from Lake join WaterQuality on (Lake.LakeID = WaterQuality.LakeID)
sort by avg_tp desc
limit 5;
