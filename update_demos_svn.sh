#!/bin/sh

# Script to update demos deployed in svn


S=`find samples/target/ -type d -name "gwtquery-samples*" | head -1`
[ ! -d "$S" ] && echo "Do not exist folder: $S" && exit

cp -r  $S/* demos/
rm -rf demos/WEB-INF demos/META-INF

for i in `find demos -type d | grep -v .svn | sed -e 's#^demos/##g'`
do
   [ ! -d $S/$i ] && svn delete demos/$i 
done

for i in `find demos -type f | grep -v .svn | sed -e 's#^demos/##g'`
do
   [ ! -f $S/$i ] && svn delete demos/$i 
done

find demos  | grep -v .svn | xargs svn add

find demos -type f -name "*html" -exec svn propset svn:mime-type text/html '{}' ';'
find demos -type f -name "*js" -exec svn propset svn:mime-type text/javascript '{}' ';'
