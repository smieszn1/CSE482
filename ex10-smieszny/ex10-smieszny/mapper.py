#!/usr/bin/python3

import sys

counties = ["Clinton","Eaton","Ingham","Kent","Macomb","Shiawassee","Wayne"]

for line in sys.stdin:
    values = line.split(',')
    for i in range(1, len(values)):
        key = counties[i-1]
        num = ''
        for char in values[i]:
            if char.isdigit():
                num += char
        print(key + ', ' + num)
