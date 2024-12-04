#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/(\w)(\g1)+/$1/xg;
    print ;
}
