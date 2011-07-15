#!/bin/sh

v=`cat pom.xml | head -15 | grep "<version>" | head -1 | cut -d ">" -f2 | cut -d "<" -f1`
[ -z "$v" ] && exit

mvn clean package -Dmaven.test.skip javadoc:jar source:jar

for i in "" sources javadoc
do
  [ -n "$i" ] && c="-Dclassifier=$i" || c=""
  mvn gpg:sign-and-deploy-file \
    -Dfile=target/gwtquery-$v-$i.jar \
    -DrepositoryId=sonatype-nexus-staging \
    -Durl=http://oss.sonatype.org/service/local/staging/deploy/maven2 \
    -Dpackaging=jar \
    -DartifactId=gwtquery \
    -DgroupId=com.googlecode.gwtquery \
    -Dversion=$v \
    -DpomFile=pom.xml $c || exit
done
