#!/bin/sh

# Script to update javadoc deployed in svn

pushd gwtquery-core
mvn javadoc:javadoc
popd

cp -r gwtquery-core/target/site/apidocs/* gwtquery-core/javadoc/

for i in `find gwtquery-core/javadoc/ -type d | grep -v .svn | sed -e 's#gwtquery-core/javadoc/##g' `
do
   [ ! -d gwtquery-core/target/site/apidocs/$i ] && svn delete gwtquery-core/javadoc/$i 
done

for i in `find gwtquery-core/javadoc/ -type f | grep -v .svn | sed -e 's#gwtquery-core/javadoc/##g' `
do
   [ ! -f gwtquery-core/target/site/apidocs/$i ] && svn delete gwtquery-core/javadoc/$i 
done

find gwtquery-core/javadoc/  | grep -v .svn | xargs svn add

find gwtquery-core/javadoc/ -type f -name "*html" -exec svn propset svn:mime-type text/html '{}' ';'


svn commit -m 'updated javadocs' gwtquery-core/javadoc/

