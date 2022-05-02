lakes = LOAD './lake_info.csv' USING PigStorage(',') AS (lake_id,lake_name,latitude,longitude,state);
groups = GROUP lakes ALL;
counts = FOREACH groups GENERATE COUNT(lakes);
STORE counts INTO 'q1b' using PigStorage(',');
