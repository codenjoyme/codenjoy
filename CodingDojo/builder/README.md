Codenjoy builder
==============

Configuration
--------------
A project that brings together the Codenjoy server and several games of your choice.
To connect your game (game-name-engine), add the dependency
```
    <properties>
        <codenjoy.version>1.0.25</codenjoy.version>
    </properties>
    ...
    <dependencies>
        <dependency>
            <groupId>com.codenjoy</groupId>
            <artifactId>game-name-engine</artifactId>
            <version>${codenjoy.version}</version>
        </dependency>
        ...
     </dependencies>  
``` 
And artifactItem that refers to the same dependency (to retrieve the game resources)
```
    <build>
      ...
      <plugins>
        ...
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-dependency-plugin</artifactId>
          ...
          <executions>
            <execution>
              ...
              <configuration>
                <artifactItems>
                  ...
                  <artifactItem>
                    <groupId>com.codenjoy</groupId>
                    <artifactId>game-name-engine</artifactId>
                    <version>${codenjoy.version}</version>
                    <type>jar</type>
                    <overWrite>true</overWrite>
                    <outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>
                    <includes>resources/**/*,WEB-INF/classes/com/codenjoy/dojo/server/*</includes>
                  </artifactItem>
                </artifactItems>
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
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