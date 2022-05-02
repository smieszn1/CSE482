select LakeID, avg(TN), avg(TP) from WaterQuality
cluster by LakeID;
