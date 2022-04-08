#!/usr/bin/python3

import sys

counties = {}
for line in sys.stdin:
    line = line.replace(',', '')
    values = line.split(' ')
    key = values[0]
    num = int(values[1])
    if not counties.get(key):
        counties[key] = []
    counties[key].append(num)

for key, values in counties.items():
    print(key+',',  max(values))
