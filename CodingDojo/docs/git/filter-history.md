```bash
# на примере scala клиента (который размещался в tetris) 
# способ вытаскивания истории с чисткой ненужных коммитов 
#(файлов, которые не должны войти в результирующий репо).

# cклонировали весь проект
git clone https://github.com/codenjoyme/codenjoy.git codenjoy-scala-client
cd codenjoy-scala-client
git checkout develop
git pull origin develop

# с помощью плагина выделяем папку в которой содержатся все файлы будущего проекта, а так же некоторый мусор, от которого мы избавимся позже
git filter-branch --subdirectory-filter ./CodingDojo/games/tetris/ -- --all

# сообщение WARNING игнорируем
# WARNING: git-filter-branch has a glut of gotchas generating mangled history--all
#          rewrites.  Hit Ctrl-C before proceeding to abort, then use an
#          alternative filtering tool such as 'git filter-repo'
#          (https://github.com/newren/git-filter-repo/) instead.  See the
#          filter-branch manual page for more details; to squelch this warning,
#          set FILTER_BRANCH_SQUELCH_WARNING=1.

# удаляю все теги
git tag | xargs git tag -d

# удаляю все ветки лишние, оставляю две рабочие
git checkout master
git merge develop
git branch --list | xargs git branch -d
git branch --list

# [Optional]
# фильтрую каждый коммит удаляя в нем файлы и папки по 
# паттерну, если при это коммит останется пустой - он удалится из итории
git filter-branch -f --prune-empty --tree-filter 'rm -rf ./src/main/go 1>/dev/null 2>/dev/null; rm -rf ./src/main/java 1>/dev/null 2>/dev/null; rm -rf ./src/main/javascript 1>/dev/null 2>/dev/null; rm -rf ./src/main/resources 1>/dev/null 2>/dev/null; rm -rf ./src/main/ruby 1>/dev/null 2>/dev/null; rm -rf ./src/main/webapp 1>/dev/null 2>/dev/null; rm -rf ./src/test/java 1>/dev/null 2>/dev/null; rm -rf ./src/test/resources 1>/dev/null 2>/dev/null; true' @

# возможно я захочу помержить некоторые коммиты между собой
git rebase --interactive

# обновляю origin 
git remote remove origin
git remote add origin https://github.com/codenjoyme/codenjoy-scala-client.git

# пушаю
git push origin master

# иду в основной репозиторий
cd <codenjoy>

# удаляю старую папку и подключаю новый сабрепо
git checkout develop
git pull origin develop
rm -rf ./CodingDojo/clients/scala
git submodule add https://github.com/codenjoyme/codenjoy-scala-client.git ./CodingDojo/clients/scala
```