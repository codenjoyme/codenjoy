attrib -h .git
ren .git .git_
attrib +h .git_
ren .gitignore .gitignore_
cd..
cd..
attrib -h .git_
ren .git_ .git
attrib +h .git