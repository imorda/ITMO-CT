#!/bin/bash

unzip find-exe.zip -d find-exe > /dev/null
echo $(basename `find find-exe -perm -u+x -type f`)
