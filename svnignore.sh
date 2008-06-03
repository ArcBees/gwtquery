svn propset svn:ignore --recursive -F .svnignore .
echo "current properties:"
svn propget svn:ignore .
