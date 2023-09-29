#!/bin/bash

unzip grep-logs.zip -d grep-logs > /dev/null

echo `zgrep -E 5[0-9][0-9] grep-logs/* | awk '{print $5}' | sort -u | tr '\n' ' '`
