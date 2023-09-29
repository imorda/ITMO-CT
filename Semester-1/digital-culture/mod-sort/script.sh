#!/bin/bash
unzip mod-sort.zip -d mod-sort > /dev/null
echo $(cat `ls -trd1 $PWD/mod-sort/*` | sha256sum | head -c 64)
