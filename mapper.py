#!/usr/bin/python3

import sys

col_names = ["outlook","temperature","construction","class"]

for line in sys.stdin:
    line = line.replace('\n', '')
    values = line.split(',')
    for i in range(len(values)):
        if values[i] == '':
            break
        print(col_names[i],values[i])
