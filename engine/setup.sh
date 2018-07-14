mvn install:install-file -Dfile=engine-1.0.25.jar -Dsources=engine-1.0.25-sources.jar -DpomFile=engine-1.0.25-pom.xml -DgroupId=com.codenjoy -DartifactId=engine -Dversion=1.0.25 -Dpackaging=jar
mvn install:install-file -Dfile=games-1.0.25-pom.xml -DpomFile=games-1.0.25-pom.xml -DgroupId=com.codenjoy -DartifactId=games -Dversion=1.0.25 -Dpackaging=pom
