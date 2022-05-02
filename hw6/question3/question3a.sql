CREATE TABLE WaterQuality (SampleDate string, LakeID double, TN double, TP double) row format delimited fields terminated by ',';
LOAD DATA LOCAL INPATH './lake_data.csv' OVERWRITE INTO TABLE WaterQuality;

CREATE TABLE Lake (LakeID double, Name string, Latitude double, Longitude double, State string) row format delimited fields terminated by ',';
LOAD DATA LOCAL INPATH './lake_info.csv' OVERWRITE INTO TABLE Lake;

