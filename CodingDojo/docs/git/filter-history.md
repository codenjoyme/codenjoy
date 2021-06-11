На примере scala клиента, который размещался в tetris способ вытаскивание истории с чисткой ненужных коммитов.

```
# cклонировали весь проект
git clone https://github.com/codenjoyme/codenjoy.git codenjoy-scala-client
cd codenjoy-scala-client

# с помощью плагина выделяем папку в которой содержатся все файлы будущего проекта, а так же некоторый мусор, от которого мы избавимся позже
git filter-branch --subdirectory-filter ./CodingDojo/games/tetris/ -- --all

# удаляю все теги
git tag | xargs git tag -d

# удаляю все ветки лишние
git checkout master
git branch --list | xargs git branch -d
git checkout -b master 

# фильтрую каждый коммит удаляя в нем файлы и папки по паттерну, если при это коммит останется пустой - он удалится из итории
git filter-branch -f --prune-empty --tree-filter 'rm -rf ./src/main/go 1>/dev/null 2>/dev/null; rm -rf ./src/main/java 1>/dev/null 2>/dev/null; rm -rf ./src/main/javascript 1>/dev/null 2>/dev/null; rm -rf ./src/main/resources 1>/dev/null 2>/dev/null; rm -rf ./src/main/ruby 1>/dev/null 2>/dev/null; rm -rf ./src/main/webapp 1>/dev/null 2>/dev/null; rm -rf ./src/test/java 1>/dev/null 2>/dev/null; rm -rf ./src/test/resources 1>/dev/null 2>/dev/null; true' @

# возможно я захочу помержить некоторые коммиты между собой
git rebase --interactive

# обновляю origin 
git remote remove origin
git remote add origin https://github.com/codenjoyme/codenjoy-scala-client.git```

# пушаю
git push origin master