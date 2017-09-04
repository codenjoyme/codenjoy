Codenjoy steps:

- install Java - JDK 8 (
- create JAVA_HOME environment variable
- install Maven3
- create M2_HOME environment variable
- add
    ;%JAVA_HOME%/bin;%M2_HOME%/bin
  to the Path environment variable
- check that command should return version of java 
    java -version
- check that command should return version of maven and java
    mvn -version
- open in browser http://server:8080/codenjoy-contest
- register your game (you should select 'Expansion Contest' on registration)
- import this project as maven to intellij idea
- change console font to 'Courier New': File -> Settings -> Console font -> Scheme save as 'Default copy' -> Primary font Select 'Courier New' -> Apply
- change ide font to 'Courier New': File -> Settings -> Console font -> Scheme save as 'Default copy' -> Primary font Select 'Courier New' -> Apply
- go to YourSoljer.java (or YourSolver.kt for Kotlin)
- set your email in the
    start("your@email.com", "server:8080", new YourSolver());
- run YourSolver as main application
- check in the console that client connected
- do your changes in the YourSolver method
    Command whatToDo(Board board)
- write your unit tests
- read help at http://server:8080/codenjoy-contest/resources/expansion/landing-expansion-contest.html
- restart java process when you want
- when finish run build-client.bat and send folder .\out\ to the DojoSensei
- for starting client from cmd after previous step you can run .\out\run-client.bat
- Codenjoy!