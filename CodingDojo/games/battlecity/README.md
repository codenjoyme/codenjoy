For NonJava languages:
- please go to .\src\main\<language>
- chose your language
- and follow README.md instructions

For Java:
- setup Java (JDK 8)
    + setup JAVA_HOME variable
- setup Maven3
    + setup M2_HOME variable
    + setup Path variable
    + open cmd and run command 'mvn -version' it should print valid java and maven location
- import this project as Maven project into Intellij Idea (Eclipse/ is not recommended)
- please install Engine dependency
    + on page http://algoritmix.dan-it.kiev.ua/codenjoy-contest/help
        * you can download zip with dependency
        * on this page you can also read game instructions
- register your hero on server http://algoritmix.dan-it.kiev.ua/codenjoy-contest/register
- in class .\src\main\java\com\codenjoy\dojo\battlecity\client\YourSolver.java
    + copy board page browser url from address bar and paste into main method
        * for example http://algoritmix.dan-it.kiev.ua/codenjoy-contest/board/player/eae1bel6klfsh5kb3veh?code=5257725287416123010  
    + implement logic inside method
        * public String get(Board board) {
    + run main method of YourSolver class
    + please check that your bot should move on board page 
    + if something changed in client sources - restart the process
        * warning! only one instance of YourSolver class you can run per player - please check this
- in class .\src\main\java\com\codenjoy\dojo\battlecity\client\Board.java
    + you can add you own methods for work with board
- in test package .\src\test\java\com\codenjoy\dojo\battlecity\client
    + you can write yor own test
- Codenjoy!