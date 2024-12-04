#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    print if /(?: (\() ((?0) | (([^\w\(\)]*\w+)+([^\w\(\)]*)*) ) (\)) )/x;
}
