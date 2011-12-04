#!/bin/sh
#if [ ! $# -eq 1 ]; then
#  echo "Usage: ./generateDeb.sh <version>"
#  echo "version should look like 0.8 (not with V and without revision!)" 
#  return 1
#fi
if [ ! -d .git ]; then 
  echo "You are not in the repository dir! Please go there ;)"
  return 2
fi

# move jar to correct location
if [ -f t10-onscreen-keyboard.jar ]; then
  echo "Will take following jar file, please check if its the current:"
  ls -l t10-onscreen-keyboard.jar
  cp t10-onscreen-keyboard.jar debian/opt/UseAcc/t10-onscreen-keyboard.jar
else
  echo "jar file should be located in repo root..."
fi

# get some values
version="`cat src/edu/dhbw/t10/SuperFelix.java | grep -E 'String.*VERSION' | sed 's/[^0-9.]//g'`"
revision="`git shortlog | grep -E '^[ ]+\w+' | wc -l`"
control="`cat debian/DEBIAN/control | grep -v Version:`"

# change version
perl -pi -w -e 's/REV[ ]*[\t]*\=[ ]*[\t]*"[0-9]*"/REV \t\t= \"'$revision'\"/g;' src/edu/dhbw/t10/SuperFelix.java
#    -e means execute the following line of code.
#    -i means edit in-place
#    -w write warnings
#    -p loop
echo "${control}\nVersion: $version-$revision">debian/DEBIAN/control

outputFile="t10-keyboard-linux-V$version-r$revision.deb"
dpkg-deb --build debian/ $outputFile
echo "OutputFile: $outputFile"
