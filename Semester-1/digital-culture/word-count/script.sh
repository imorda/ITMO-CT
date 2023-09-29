#!/bin/bash

unzip word-count.zip -d word-count > /dev/null
result=0
for i in $(find word-count -type f -name *`cat word-count/target.word`*); do
  ((result+=$(cat $i | wc -w)))
done
echo $result
