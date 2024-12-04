#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/\b(\w*)(\W*)(\w*)\b/$3$2$1/x;
    print ;
}
