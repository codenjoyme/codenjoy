Создай свою Codenjoy игру
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
- пропиши переменную окружения `JAVA_HOME`, указывающую на корень папки `c:\java\jdk1.8.x_xx`
- добавь в конец переменной Path строчку `;%JAVA_HOME%\bin`
- проверь что все сделано правильно выполнив cmd.exe а в нем команду `mvn -version`.
Если установилось - ты будешь видеть вывод команды: версию maven и java, а не что "команда не найдена"
```
C:\Users\user>mvn -version
Apache Maven 3.x.x
Maven home: C:\java\apache-maven-3.x.x
Java version: 1.8.x_x, vendor: Oracle Corporation
Java home: C:\java\jdk1.8.x_xx\jre
Default locale: xxxxx, platform encoding: xxxxxxx
OS name: "xxxxxxxxxx", version: "xxx", arch: "xxxxx", family: "xxxxxxx"
C:\Users\user>
```
- скачай и установи [IntelliJ IDEA Community version](https://www.jetbrains.com/idea/download/)

Установка codenjoy engine
--------------

Для нормальной работы тебе необходимо установить зависимость `engine`. Она расположена
в одноименной папке `engine`. Будь внимателен, ее версия может поменяться и тебе прийдется
обновить и ее и исходники своей игры. Для этого:

- зайди в папку 'engine'
- запусти 'setup.bat'
- проследи, чтобы все установилось успешно - зависимость должна быть
установлена в `C:\Users\<Твой_юзер>\.m2\repository\com\codenjoy\engine`

Рабработка игры
--------------

Придумай/вспомни игру, которая была бы интересна (но не сложная по правилам) и
знакома всем из детства. Игра может быть идентичной оригинальной по правилам, а может
и отличаться от оригинала в большей или меньшей мере. Например - в морской бой обычно
играют двое, но ты можешь реализовать морской бой для неограниченного количества игроков.
Тут стоит проверить, чтобы писать алгоритм AI был интересно играющему (не очень просто
и не очень сложно). Смело [пиши нам](http://codenjoy.com/portal/?page_id=51)
если затрудняешься с выбором - поможем.

После этого приступай к написанию модельки выбранной тобой игры. В следующем разделе описано подробно,
как это сделать. [Вот пример](http://apofig.blogspot.com/2011/10/9-tdd.html) того, как была
написана моделька змейки. Очень желательно чтобы она была покрыта юнит тестами.
Еще лучше, чтобы код был написан по TDD, если не знаешь как [посмотри это видео](https://vimeo.com/54862036)
а потом напиши нам. Code review если надо организуем. А после того, как моделька
будет готова интегрируем ее в наш фреймворк и поможем с организацией твоего первого
codenjoy-ивента.

Вот репозиторий [https://github.com/codenjoyme/codenjoy-game](https://github.com/codenjoyme/codenjoy-game).
Тебе предстоит разобраться в том, как форкнуть себе проект, как происходит коммит в git.

Пример игры - Sample
--------------

Sample - это пример однобордовой игры со всеми необходимыми артефактами. Изучи как работает проект.

- импортируй проект `sample` как `maven project` в idea
- запусти все тесты, они должны проходить - ты должен наблюдать зеленую полосу
- зайди на [sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java)
и посмотри, как пишутся тесты для игры.
- зайди на [sample/src/main/java/com/codenjoy/dojo/sample](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample)
и посмотри что надо написать минимально для новой игры.
- тут в пакете [sample/src/main/java/com/codenjoy/dojo/sample/client](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client)
расположен клиенсткий код, часть из которого будет отправлен игроку как шаблон для игры
- в пакете [sample/src/main/java/com/codenjoy/dojo/sample/client/ai](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client/ai)
расположен твой AI алгоритм, который будет автоматически подключаться к игроку и играть с ним
- все остальное - движок игры.
- для реализации новой игры стоит разобраться с основными интерфейсами и классами фреймворка.
Все они расположены в engine зависимости. Тебя должны интересовать такие:
* `src/main/java/com/codenjoy/dojo/services/Game.java`
* `src/main/java/com/codenjoy/dojo/services/Joystick.java`
* `src/main/java/com/codenjoy/dojo/services/Printer.java`
* `src/main/java/com/codenjoy/dojo/services/GameType.java`
* `src/main/java/com/codenjoy/dojo/services/Tick.java`
* `src/main/java/com/codenjoy/dojo/services/PlayerScores.java`
* `src/main/java/com/codenjoy/dojo/services/Point.java`
* `src/main/java/com/codenjoy/dojo/services/Printer.java`
* `src/main/java/com/codenjoy/dojo/services/GamePrinter.java`
* `src/main/java/com/codenjoy/dojo/services/EventListener.java`
* `src/main/java/com/codenjoy/dojo/services/Dice.java`
* `src/main/java/com/codenjoy/dojo/services/CharElements.java`
* `src/main/java/com/codenjoy/dojo/services/Direction.java`
* `src/main/java/com/codenjoy/dojo/services/Settings.java`
* `src/main/java/com/codenjoy/dojo/client/AbstractBoard.java`
* `src/main/java/com/codenjoy/dojo/client/Solver.java`
* `src/main/java/com/codenjoy/dojo/client/Direction.java`
- все это базовые интерфейсы/классы, с их помощью происходит интеграция новой игры в
основной фреймворк (как картридж в приставку dendy).
- изучи их опсиание в джавадоках к интерфейсам и классам engine зависимости и sample проекта

Разработка новой игры
--------------

Для разработки своей игры писать все классы с нуля не надо - возьми за основу `sample` проект.

- скопируй содержимое `sample` папки в `mygame` папку (имя любое) и переименуй классы `Sample` в `MyGame`.
- твоя цель - хорошее покрытие кода тестами, которому мы сможем доверять,
потому игру стоит разрабатывать по TDD, если не знаком с ним -
[рекомендуем книгу Кента Бека](http://www.ozon.ru/context/detail/id/1501671/).
- исходники для игры для клиента maven соберет автоматически в zip, как это сделано тут
[sample/src/main/webapp/resources/user/sample-servers.zip](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/user)
Обрати внимание, что в `pom.xml` файле есть раздел `maven-antrun-plugin`,
где ant'ом собирается этот zip. В него попадают сам `pom.xml`,
класс `Elements` из пакета model и все из пакета `client` за исключением пакета `ai`.
- напиши мануал к игре, подобно тому как мы сделали вот тут
[sample/src/main/webapp/resources/help/sample.html](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/help/sample.html)
- нарисуй спрайты - это такие квадратненькие рисунки, на основе которых будет
рисоваться игра в браузере. Обычно они есть в свободном доступе в сети.
Png файлы со спрайтами содержатся в папке [sample/src/main/webapp/resources/sprite/sample/](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/sprite/sample).
Важно! Имена спрайтов выбираются не произвольно, а в связке с полями enum
[sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java).
Все имена должны быть lover case
- далее реализуй своего бота по аналогии с [sample/src/main/java/com/codenjoy/dojo/sample/client/ai/ApofigSolver.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client/ai/ApofigSolver.java)
- запусти этот код, чтобы проверить как твой бот играет в игру
```
    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new YourSolver(new RandomDice()),
                new Board());
    }
```
- собери jar-file, для этого выполни команду `mvn package` в корне папки sample
- jar будет находиться в `sample\target\sample-engine.jar`
- вышли его нам на [apofig@gmail.com](mailto:apofig@gmail.com) с темой `Новая игра для codenjoy`

Спасибо!

Другие материалы
--------------
Больше [деталей тут](https://github.com/codenjoyme/codenjoy)

[Команда Codenjoy](http://codenjoy.com/portal/?page_id=51)
===========