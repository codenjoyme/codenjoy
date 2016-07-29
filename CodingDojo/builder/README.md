Codenjoy builder
==============

Конфигурация
--------------
Проект, собирающий воедино сервер Codenjoy и одну или несколько игор на выбор.
Для подключения своей игры (game-name-engine) необходимо добавить зависимость
```
    <properties>
        <codenjoy.version>1.0.7</codenjoy.version>
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
А так же artifactItem ссылающийся на эту же зависимость (для извлечения ресурсов игры)
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
Возможно добвление сразу нескольких игрушек, а так же удаление существующих

Запуск проекта
--------------
Запуск проекта по команде `mvn -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true clean jetty:run-war`
При этом игра/игры должны быть предварительно установлены по команде `mvn clean install` из корня проекта каждой игры

Другие материалы
--------------
Больше [деталей тут](https://github.com/codenjoyme/codenjoy)

[Команда Codenjoy](http://codenjoy.com/portal/?page_id=51)
===========