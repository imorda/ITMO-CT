#!/bin/bash

echo "" > result
unzip grep-logs.zip -d grep-logs > /dev/null

gzip -d grep-logs/*.gz

for i in grep-logs/*; do
  IFS=$'\n'
  for j in `cat $i`; do
    IFS=' '

    array=($j)

    #echo $j

    if [ ${array[7]} -ge 500 ] && [ ${array[7]} -lt 600 ]; then
      echo ${array[4]} >> result
    fi

    IFS=$'\n'
  done
done
IFS=' '

echo `cat result | sort | uniq | tr '\n' ' '`
