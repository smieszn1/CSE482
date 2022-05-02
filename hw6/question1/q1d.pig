REGISTER '/usr/lib/pig/lib/piggybank.jar';
names = LOAD './lake_info.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage(',', 'NO_MULTILINE', 'UNIX', 'SKIP_INPUT_HEADER') AS (lake_id,lake_name,latitude,longitude,state);
lakes = LOAD './lake_data.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage(',', 'NO_MULTILINE', 'UNIX', 'SKIP_INPUT_HEADER') AS (sampledate,lake_id,total_nitrogen,total_phosphorous);
temp = JOIN lakes BY lake_id, names BY lake_id;
combo = FOREACH temp GENERATE names::lake_name, lakes::total_phosphorous;
groups = GROUP combo BY lake_name;
average = FOREACH groups GENERATE group, AVG(combo.total_phosphorous) AS avg_phos;
sorted = ORDER average BY avg_phos DESC;
result = LIMIT sorted 5;
STORE sorted INTO 'q1d' using PigStorage(',');