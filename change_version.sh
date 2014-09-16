
[ -z "$1" ] && exit

for i in pom.xml */pom.xml
do
    V=`head -20 $i | grep "<version>" | head -1 | cut -d ">" -f2 | cut -d "<" -f1`
    perl -pi -e "s;<version>$V</version>;<version>$1</version>;" $i
    echo $V $i
done

perl -pi -e "s;<gQueryVersion>.+</gQueryVersion>;<gQueryVersion>$1</gQueryVersion>;" \
     archetype/src/main/resources/archetype-resources/pom.xml
