REGISTER '/usr/lib/pig/lib/piggybank.jar';
lakes = LOAD './lake_data.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage(',', 'NO_MULTILINE', 'UNIX', 'SKIP_INPUT_HEADER') AS (sampledate,lake_id,total_nitrogen,total_phosphorous);
groups = GROUP lakes BY lake_id;
average = FOREACH groups GENERATE group, AVG(lakes.total_nitrogen), AVG(lakes.total_phosphorous);
STORE average INTO 'q1c' using PigStorage(',');
