lakes = LOAD './lake_info.csv' USING PigStorage(',') AS (lake_id,lake_name,latitude,longitude,state);
myLake = FILTER lakes BY lake_id ==  189913;
STORE myLake INTO 'q1a' using PigStorage(',');
