cd .\server
..\mvnw clean spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring-boot.run.profiles=sqlite,debug -Dcontext=/codenjoy-contest -DallGames
#..\mvnw clean spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring-boot.run.profiles=sqlite,debug -Dcontext=/codenjoy-contest -Psnake,bomberman,sample
