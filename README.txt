Setup steps:
- install Java - JDK 7 (
- create JAVA_HOME environment variable
- install Maven3
- create M2_HOME environment variable
- add
    ;%JAVA_HOME%/bin;%M2_HOME%/bin
  to the Path environment variable
- import project as maven
- run
    mvn clean install
- go to builder project
- run
    mvn clean jetty:run-war
- open in browser http://127.0.0.1:8080/codenjoy-contest
- codenjoy!