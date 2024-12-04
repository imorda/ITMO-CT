#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/\((.*?)\)/()/xg;
    print ;
}
