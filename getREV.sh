#!/bin/sh
echo -n "`git shortlog | grep -E '^[ ]+\w+' | wc -l`"
