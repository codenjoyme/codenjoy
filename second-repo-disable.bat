attrib -h .git_
ren .git_ .git
attrib +h .git
ren .gitignore_ .gitignore

cd CodingDojo/repo
attrib -h .git
ren .git .git_
attrib +h .git_
ren .gitignore .gitignore_
cd ../..

cd CodingDojo/games
attrib -h .git
ren .git .git_
attrib +h .git_
ren .gitignore .gitignore_