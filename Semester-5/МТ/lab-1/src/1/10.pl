#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    print if /\b( (\w+) \g-1 )\b/x;
}
