Чтобы настроить клиент сделай следующее:
- установи Java7
- установи Maven3
- запусти .\libs\setup.bat
- импортируй текущий проект как maven в Intellij Idea (или Eclipse)
- в классе .\src\main\java\com\codenjoy\dojo\<game_package>\client\YourSolver.java
    = впиши имейл с которым ты регистрировался в константу USER_NAME
    = напиши свою логику в методе
        public String get(Board board) {
    = запусти YourSolver класс как main метод
    = загляни на http://codenjoy.com/codenjoy-contest/board?gameName=<game_name>
      твой бот должен был начать двигаться
    = перезапусти процесс, если сделал изменения
    = внимание! только один YourSolver за раз можно запустить - следи за этим
- в классе .\src\main\java\com\codenjoy\dojo\<game_package>\client\Board.java
    = можешь дополнять воспомагательные методы для парсинга борды из строки
- В тестовом пакете .\src\test\java\com\codenjoy\dojo\sudoku\client
    = можешь размещать свои тесты
- codenjoy!