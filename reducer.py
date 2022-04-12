#!/usr/bin/python3

import sys
import math

def entropy(attributes, classes):
    cls = {}
    
    for i in range(len(classes)):
        if not cls.get(classes[i]):
            cls[classes[i]] = []
        cls[classes[i]].append(attributes[i])

    entropy = 0

    for key, values in cls.items():
        ats = {}
        if not ats.get(attributes[i]):
            ats[attributes[i]] = []
        ats[attributes[i]].append(i)
        for key2, values2 in ats.items():
            entropy -= (len(values2)/len(values)) * math.log2(len(values2)/len(values))

    return entropy

    


columns = {}

for line in sys.stdin:
    values = line.split(" ")
    key, val = values
    if not columns.get(key):
        columns[key] = []
    columns[key].append(val)

for key, value in columns.items():
    if key != 'class':
        result = entropy(value, columns.get('class'))
        print(key + ",", result)
