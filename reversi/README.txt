Чтобы настроить клиент сделай следующее:
- установи Java (JDK 7 или 8)
- установи Maven3
- импортируй текущий проект как Maven project в Intellij Idea (Eclipse не рекомендуется)
- если нет интернета и не будет хватать engine dependency
    = на странице http://host/codenjoy-contest/help можно скачать zip с этим dependency
        ~ тут host
            = server_ip:8080 для игры с сервером поднятым в локальной сети
            = codenjoy.com для игры с общим сервером размещенным в интернете
    = там же есть инструкции по игре
- в классе .\src\main\java\com\codenjoy\dojo\<game_package>\client\YourSolver.java
    = впиши имейл с которым ты регистрировался на сервере в константу USER_NAME
    = напиши свою логику в методе
        public String get(Board board) {
    = запусти YourSolver класс как main метод
        ~ обрати внимание, что для игры с локальным сервером в main методе стоит расскоментировать строчку
            WebSocketRunner.runOnServer("192.168.1.1:8080", // to use for local server
        ~ указать IP сервера
        ~ и закомментировать строчку для подключения к удаленному серверу
            WebSocketRunner.run(WebSocketRunner.Host.REMOTE, // to use for codenjoy.com server
    = загляни на http://host/codenjoy-contest/board/game/<game_name>
      твой бот должен был начать двигаться
    = перезапусти процесс, если сделал изменения
        ~ внимание! только один YourSolver за раз можно запустить - следи за этим
- в классе .\src\main\java\com\codenjoy\dojo\<game_package>\client\Board.java
    = можешь дополнять воспомагательные методы для парсинга борды из строки
- В тестовом пакете .\src\test\java\com\codenjoy\dojo\<game_package>\client
    = можешь размещать свои тесты
- Codenjoy!