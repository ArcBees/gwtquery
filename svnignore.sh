[ -z "$1" ] && exit


p=`pwd`

while [ -n "$1" ]
do
   for i in `find . -name $1`
   do
      d=`dirname $i`
      cd $p
      cd $d
      svn propget svn:ignore . > /tmp/$$
      echo $1 >> /tmp/$$
      svn propset svn:ignore -F /tmp/$$ .
   done
   shift
done


