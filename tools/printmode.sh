#!/bin/bash

sleep 3s
for (( i=$1; i<=$2; i++))
do
  xdotool key Return
  xdotool type "<mode modename=\"1001\">$i</mode>"
#  xdotool type "<key>$i</key>"
  xdotool key Down
  xdotool keydown Control d
  xdotool keyup d Control
  xdotool key Down Down End
done
