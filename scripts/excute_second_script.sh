#!/bin/bash

scriptname=$1
filename=$2
while read line; do
./$scriptname --target=$line
done < $filename