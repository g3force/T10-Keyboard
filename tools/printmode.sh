#!/bin/bash

sleep 3s
for (( i=$1; i<=$2; i++))
do
  xdotool key Return
  xdotool type "<mode name=\"default\">$i"
  xdotool key Down
  xdotool keydown Control d
  xdotool keyup d Control
  xdotool key Down End
done
