attrib -h .git_
ren .git_ .git
attrib -h .git
ren .gitignore_ .gitignore
cd..
cd..
attrib -h .git
ren .git .git_
attrib +h .git_