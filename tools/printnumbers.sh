#!/bin/bash

sleep 3s
for (( i=$1; i<=$2; i++))
do
  echo $i
  xdotool key #BackSpace #BackSpace
  xdotool type $i
  xdotool key Down Left Left #Down Down Down Left Left
#  xdotool key Down  Left Left Left Left
done
