#!/usr/bin/python3

import sys

for line in sys.stdin:
    values = line.split(',')
    key = values[0]
    for i in range(1, len(values)):
        num = ''
        for char in values[i]:
            if char.isdigit():
                num += char
        print(key + ', ' + num)
