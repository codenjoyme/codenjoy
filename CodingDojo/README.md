Codenjoy
==============

Вступление
--------------
[Codenjoy](http://codenjoy.com) - это CodingDojo фреймворк для разработчиков. Цель его fun'овые
тимбилдинговые мероприятия и/или обучение кодингу.
Уже сейчас [на борту есть ряд игрушек](http://codenjoy.com/codenjoy-contest). 
И ты можешь написать еще одну свою.

Установка окружения для разработки
--------------
Все, что тебе понадобится для разработки игры - jdk7, maven3, git-клиент и IDE Idea.

- установи себе git клиент, например [tortoise git](https://code.google.com/p/tortoisegit/) 
- зарегай себе аккаунт на [github](http://github.com) или [bitbucket](http://bitbucket.org)
- сделай fork (или просто копию проекта sample) из [текущего репозитория](https://github.com/codenjoyme/codenjoy-game)
- затем сделай pull проекта на компьютер
- установи [maven3](https://maven.apache.org/download.cgi) (скачай архив и распакуй его в `c:\java`)
- пропиши переменную окружения `M2_HOME`, указывающую на корень папки `c:\java\apache-maven-3.x.x`
- добавь в конец переменной `Path` строчку `;%M2_HOME%\bin`
- установи jdk7 если не установлена (тоже в папку `c:\java`)
- пропиши переменную окружения `JAVA_HOME`, указывающую на корень папки `c:\java\jdk1.7.x_xx`
- добавь в конец переменной Path строчку `;%JAVA_HOME%\bin`
- проверь что все сделано правильно выполнив cmd.exe а в нем команду `mvn -version`. 
Если установилось - ты будешь видеть вывод команды: версию maven и java, а не что "команда не найдена"
```
C:\Users\user>mvn -version
Apache Maven 3.x.x
Maven home: C:\java\apache-maven-3.x.x
Java version: 1.7.x_x, vendor: Oracle Corporation
Java home: C:\java\jdk1.7.x_xx\jre
Default locale: xxxxx, platform encoding: xxxxxxx
OS name: "xxxxxxxxxx", version: "xxx", arch: "xxxxx", family: "xxxxxxx"
C:\Users\user>
```
- скачай и установи [IntelliJ IDEA Community version](https://www.jetbrains.com/idea/download/)

Запуск игры c помощью Codenjoy-builder
--------------

Для сборки проекта с заданной игрой необходимо выполнить следующие дествия:

- выкачать проект из [основного репозитория codenjoy|https://github.com/codenjoyme/codenjoy]
- в файле `\CodingDojo\builder\pom.xml` указать какие игры хочется оставить, для этого
- добавляем в блок `dependency` зависимость на определенную игру
```
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>sample-engine</artifactId>
    <version>${project.version}</version>
</dependency>
```
- в плагин maven-dependency-plugin в раздел executions\execution\configuration\artifactItems добавить
```
<artifactItem>
    <groupId>${project.groupId}</groupId>
    <artifactId>a2048-engine</artifactId>
    <version>${project.version}</version>
    <type>jar</type>
    <overWrite>true</overWrite>
    <outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>
    <includes>resources/**/*</includes>
</artifactItem>
```
- если игра новая, для нее можно сделать копию проекта `\CodingDojo\builder`
- так же новую игру можно добавить в родительский проект `\CodingDojo\pom.xml` в раздел modules, либо вести ее независимо
```
<modules>
        <module>games/engine</module>

		<module>games/sample</module>
        ...
        <module>games/your-game</module>

        <module>server</module>
        <module>builder</module>
    </modules>
```
- сконфигурировать codenjoy изменяя настройки в файле `\CodingDojo\server\src\main\resources\com\codenjoy\dojo\server\codenjoy.properties`
- а именно поставить `email.verification=false` чтобы не тербовалось подтверждение по почте при регистрации
- выполнить команду `mvn clean install` в проекте `\CodingDojo\games\engine`, чем проинсталлировать интерфейсную часть
- выполнить команду `mvn clean install` в корне проекте, чем проинсталлировать все остальные компоненты
- выполнить команду `mvn -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true jetty:run-war` в проекте `\CodingDojo\builder` чем запустить игру
- так же более просто игру можно запустить с помощью скрипта в корне проекта `\CodingDojo\start-server.bat`
- зайти в браузер по [http://127.0.0.1:8080/codenjoy-contest|http://127.0.0.1:8080/codenjoy-contest] и зарегистрировать игрока
- описание любой игры можно почитать на страничке помощи [http://127.0.0.1:8080/codenjoy-contest/help|http://127.0.0.1:8080/codenjoy-contest/help]
- если что-то не получается - написать в скайп Александру Баглаю `alexander.baglay`

Рабработка игры
--------------

Более детально о создании игры можно [почитать тут|https://github.com/codenjoyme/codenjoy-game]

[Команда Codenjoy](http://codenjoy.com/portal/?page_id=51)