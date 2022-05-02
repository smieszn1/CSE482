REGISTER '/usr/lib/pig/lib/piggybank.jar';
names = LOAD './lake_info.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage(',', 'NO_MULTILINE', 'UNIX', 'SKIP_INPUT_HEADER') AS (lake_id,lake_name,latitude,longitude,state);
lakes = LOAD './lake_data.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage(',', 'NO_MULTILINE', 'UNIX', 'SKIP_INPUT_HEADER') AS (sampledate,lake_id,total_nitrogen,total_phosphorous);
temp = JOIN lakes BY lake_id, names BY lake_id;
combo = FOREACH temp GENERATE names::lake_name, lakes::total_nitrogen;
filtered = FILTER combo BY total_nitrogen > 3500;
groups = GROUP filtered BY lake_name;
names = FOREACH groups GENERATE group;
STORE names INTO 'q1e' using PigStorage(',');
