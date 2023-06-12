On the example of the scala client (which was placed in tetris)
a way to pull out the story with cleaning up unnecessary commits
(files that should not be included in the resulting repo).

Clone the entire project.
```
git clone https://github.com/codenjoyme/codenjoy.git codenjoy-scala-client
cd codenjoy-scala-client
git checkout develop
git pull origin develop
```

Using the plugin, we select the folder in which all the files of 
the future project are located, as well as some garbage, from 
which we will get rid of later.
```
git filter-branch --subdirectory-filter ./CodingDojo/games/tetris/ -- --all
```

Warning message please ignore.
```
WARNING: git-filter-branch has a glut of gotchas generating mangled history--all
         rewrites.  Hit Ctrl-C before proceeding to abort, then use an
         alternative filtering tool such as 'git filter-repo'
         (https://github.com/newren/git-filter-repo/) instead.  See the
         filter-branch manual page for more details; to squelch this warning,
         set FILTER_BRANCH_SQUELCH_WARNING=1.
```

Delete all tags.
```
git tag | xargs git tag -d
```

Delete all unnecessary branches, leave two working.
```
git checkout master
git merge develop
git branch --list | xargs git branch -d
git branch --list
```

(Optional) filter each commit by deleting files and folders in it by
pattern, if at the same time the commit remains empty - it will be 
deleted from the story.
```
git filter-branch -f --prune-empty --tree-filter 'rm -rf ./src/main/go 1>/dev/null 2>/dev/null; rm -rf ./src/main/java 1>/dev/null 2>/dev/null; rm -rf ./src/main/javascript 1>/dev/null 2>/dev/null; rm -rf ./src/main/resources 1>/dev/null 2>/dev/null; rm -rf ./src/main/ruby 1>/dev/null 2>/dev/null; rm -rf ./src/main/webapp 1>/dev/null 2>/dev/null; rm -rf ./src/test/java 1>/dev/null 2>/dev/null; rm -rf ./src/test/resources 1>/dev/null 2>/dev/null; true' @
```

If you want to merge some commits together.
```
git rebase --interactive
```

Update origin.
```
git remote remove origin
git remote add origin https://github.com/codenjoyme/codenjoy-scala-client.git
```

Push.
```
git push origin master
```

Go to the main repository.
```
cd <codenjoy>
```

Delete the old folder and connect the new subrepo.
```
git checkout develop
git pull origin develop
rm -rf ./CodingDojo/clients/scala
git submodule add https://github.com/codenjoyme/codenjoy-scala-client.git ./CodingDojo/clients/scala
```