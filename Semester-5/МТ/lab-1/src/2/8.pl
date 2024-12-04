#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/(\d+)0\b/$1/xg;
    print ;
}
