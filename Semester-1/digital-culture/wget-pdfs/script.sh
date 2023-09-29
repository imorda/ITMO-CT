#!/bin/bash

wget -q -P downloaded -i wget-pdfs
result=0
for i in downloaded/*; do
  if [ -n "`file -b $i | grep "PDF document"`" ]; then
    ((result+=`stat -c %s $i`))
  fi
done
echo $result
