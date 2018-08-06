Codenjoy builder
==============

Configuration
--------------
A project that brings together the Codenjoy server and several games of your choice.
You can choose which games to run with the server with command.
```
mvn clean jetty:run-war -Pgame1,game,game3
OR
mvn clean jetty:run-war -DallGames
```
This is Codenjoy Maven repository. To select existing games type the following in your pom.xml file.
```
<repositories>
    <repository>
        <id>codenjoy-releases</id>
        <url>https://github.com/codenjoyme/codenjoy-repo/raw/master/snapshots</url>
    </repository>
</repositories>
```
Choose codenjoy server/engine/games version
```
<groupId>com.codenjoy</groupId>
<artifactId>codenjoy-builder</artifactId>
<!-- version for all codenjoy dependencies here -->
<version>SERVER_VERSION</version>
```
The latest version as of now
```
<properties>
    <codenjoy.version>1.0.25</codenjoy.version>
</properties>
```
If you want to add your new game - please do thing bellow.
Add your game dependency.
```
<profiles>
    <!-- Games dependencies (put your game here - replace YOURGAME) -->
    <profile>
        <id>YOURGAME</id>
        <activation>
            <property>
                <name>allGames</name>
            </property>
        </activation>
        <properties>
            <exclude.YOURGAME>false</exclude.battlecity>
        </properties>
        <dependencies>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>YOURGAME-engine</artifactId>
                <version>${project.version}</version>
            </dependency>
        </dependencies>
    </profile>
```
Add game resources part in plugin[maven-dependency-plugin].executions tag
```
<!-- Games resources (put your game here - replace YOURGAME) -->
<execution>
    <id>unpack-YOURGAME</id>
    <phase>compile</phase>
    <goals>
        <goal>unpack</goal>
    </goals>
    <configuration>
        <skip>${exclude.YOURGAME}</skip>
        <artifactItems>
            <artifactItem>
                <groupId>${project.groupId}</groupId>
                <artifactId>YOURGAME-engine</artifactId>
                <version>${project.version}</version>
                <type>jar</type>
                <overWrite>true</overWrite>
                <outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>
                <includes>resources/**/*,WEB-INF/classes/com/codenjoy/dojo/server/*</includes>
            </artifactItem>
        </artifactItems>
    </configuration>
</execution>
```
Then
```
mvn clean jetty:run-war -Pyourgame
```
You can add multiple games at the same time, and remove existing ones

Launch your project
--------------
To launch your project, run `mvn -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true clean jetty:run-war` from the project builder's root
If you create your own game, you should have it pre-installed by running `mvn clean install` from the project root. If you use a game kit that already exists, you don't have to do anything - the builder will retrieve the games from the remote repository for you.

Other materials
--------------
Fore [more details, click here](https://github.com/codenjoyme/codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========