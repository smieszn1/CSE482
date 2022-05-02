select distinct Name from Lake join WaterQuality on (Lake.LakeID = WaterQuality.LakeID)
where TN > 3500;
