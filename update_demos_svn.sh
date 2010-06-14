#!/bin/sh

# Script to update demos deployed in svn

V=1.0.0-SNAPSHOT

cp -r samples/target/gwtquery-samples-$V/gwtquery* demos/
cp -r samples/target/gwtquery-samples-$V/index.html demos/

for i in `find demos -type d | grep -v .svn | sed -e 's#^demos/##g'`
do
   [ ! -d samples/target/gwtquery-samples-$V/$i ] && svn delete demos/$i 
done

for i in `find demos -type f | grep -v .svn | sed -e 's#^demos/##g'`
do
   [ ! -f samples/target/gwtquery-samples-$V/$i ] && svn delete demos/$i 
done

find demos -type f | grep -v .svn | xargs svn add

find demos -type f -name "*html" -exec svn propset svn:mime-type text/html '{}' ';'
