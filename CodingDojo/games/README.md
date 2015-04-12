Создай свою Codenjoy игру
==============

Вступление
--------------
[Codenjoy](http://codenjoy.com) CodingDojo фреймворк для разработчиков. Цель его fun'овые 
мероприятия и обучение кодингу. Уже сейчас на борту есть ряд игрушек. Ты можешь написать 
еще одну свою.

Установка окружения для разработки
--------------
Все, что тебе понадобится для разработки игры - jdk7, maven3, git-клиент и IDE Idea.

- установи себе git клиент, например [tortoise git](https://code.google.com/p/tortoisegit/) 
- зарегай себе аккаунт на [github](http://github.com) или [bitbucket](http://bitbucket.org)
- сделай fork (или просто копию проекта sample) из [текущего репозитория](https://bitbucket.org/codenjoy/codenjoy-game)
- затем сделай pull проекта на компьютер уже со своего репозитория
- установи maven3 (скачай архив и распакуй его в c:\java)
- пропиши переменную окружения M2_HOME, указывающую на корень папки maven
- добавь в конец переменной Path строчку ;%M2_HOME%\bin
- установи jdk7 если не установлена (тоже в папку c:\java)
- пропиши переменную окружения JAVA_HOME
- добавь в конец переменной Path строчку ;%JAVA_HOME%\bin
- проверь что все сделано правильно выполнив cmd.exe а в нем команду "mvn -version" 
(если установилось - ты будешь видеть вывод команды - версию maven и java, а не что "команда не найдена")
- скачай и установи idea community version

Установка codenjoy engine
--------------

Для нормальной работы тебе необходимо установить зависимость Codenjoy Engine. 
Она расположена в папке engine. Будь внимателен, версия может поменяться и тебе прийдется 
обновить ее и исходники своей игры. Для этого:

- зайди в папку engine
- запусти setup.bat
- проследи, чтобы все установилось успешно - зависимость должна быть 
установлена в C:\Users\<Твой юзер>\\.m2\repository\com\codenjoy\engine

Рабработка игры
--------------

Придумай/вспомни игру, которая была бы либо интересна (но не сложная по правилам) либо
знакома всем из детства. Игра может быть идентичной оригинальной по правилам, а может
и отличаться от оригинала в большей или меньшей мере. Например - в морской бой обычно
играют двое, но ты можешь реализовать морской бой для неограниченного количества игроков.
Тут стоит проверить, чтобы писать алгоритм AI был интересно играющему (не очень просто
и не очень сложно). Смело [пиши нам](http://codenjoy.com/portal/?page_id=51)
если затрудняешься с выбором - поможем.

После этого приступай к написанию модельки этой игры. В следующем разделе описано подробно, 
как это сделать. [Вот пример](http://apofig.blogspot.com/2011/10/9-tdd.html) того, как была
написана моделька змейки. Очень желательно чтобы она была покрыта юнит тестами.
Еще лучше, чтобы код был написан по TDD, если не знаешь как [посмотри это видео](https://vimeo.com/54862036)
а потом напиши нам. Code review если надо организуем. А после того, как моделька
будет готова интегрируем ее в наш фреймворк и поможем с организацией твоего первого
codenjoy-ивента. 

Вот репозиторий [https://bitbucket.org/codenjoy/codenjoy-game](https://bitbucket.org/codenjoy/codenjoy-game).
Тебе предстоит разобраться в том, как форкнуть себе проект, как происходит коммит в git.

Пример игры - Sample
--------------

Sample - это пример однобордовой игры со всеми необходимыми артефактами. Изучи как работает проект.

- импортируй проект sample как maven project в idea 
- запусти все тесты, они должны проходить ("зеленая полоса")
- зайди на [sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java?at=master) 
и посмотри, как пишутся тесты для игры.
- зайди на [sample/src/main/java/com/codenjoy/dojo/sample](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/main/java/com/codenjoy/dojo/sample/?at=master) 
и посмотри что надо написать минимально для новой игры. 
- тут в пакете [sample/src/main/java/com/codenjoy/dojo/sample/client](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b/sample/src/main/java/com/codenjoy/dojo/sample/client/?at=master)
расположен клиенсткий код, часть из которого будет отправлен игроку как шаблон для игры
- в пакете [sample/src/main/java/com/codenjoy/dojo/sample/client/ai](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/main/java/com/codenjoy/dojo/sample/client/ai/?at=master)
расположен твой AI алгоритм, который будет автоматически подключаться к игроку и играть с ним
- для реализации новой игры стоит разобраться с основными интерфейсами и классами фреймворка. 
Все они расположены в engine зависимости. Тебя должны интересовать такие:
- src/main/java/com/codenjoy/dojo/services/Game.java
- src/main/java/com/codenjoy/dojo/services/Joystick.java
- src/main/java/com/codenjoy/dojo/services/Printer.java
- src/main/java/com/codenjoy/dojo/services/GameType.java
- все это базовые интерфейсы/классы, с их помощью происходит интеграция новой игры в
основной фреймворк (как картридж в приставку dendy).
- изучи их опсиание в джавадоках к интерфейсам и классам engine зависимости и sample проекта

Разработка новой игры
--------------

Для разработки своей игры писать все классы с нуля не надо - возьми за основу Sample проект.

- скопируй содержимое sample папки в mygame папку (имя любое) и переименуй классы Sample в MyGame.
- твоя цель - хорошее покрытие кода тестами, которому мы сможем доверять, 
потому игру стоит разрабатывать по TDD, если не знаком с ним - 
[рекомендуем книгу Кента Бека](http://www.ozon.ru/context/detail/id/1501671/).
- собери для клиента в zip его исходники для клиента как это сделано вот тут
[sample/src/main/webapp/resources/user/sample-servers.zip](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/main/webapp/resources/user/?at=master)
- напиши мануал к игре, подобно тому как мы сделали вот тут 
[sample/src/main/webapp/resources/help/sample.html](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/main/webapp/resources/help/sample.html?at=master)
- следующий шаг - нарисовать спрайты. Это такие квадратненькие рисунки, 
на основе которых будет рисоваться игра в браузере. Обычно они есть в свободном
доступе в сети. Png файлы со спрайтами содержатся в папке [sample/src/main/webapp/resources/sprite/sample/](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/main/webapp/resources/sprite/sample/?at=master)
Важно! Имена спрайтов выбираются не произвольно, а в связке с полями enum
[sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java?at=master).
Все имена должны быть lover case
- далее реализуй своего бота по аналогии с [sample/src/main/java/com/codenjoy/dojo/sample/client/ai/ApofigSolver.java](https://bitbucket.org/codenjoy/codenjoy-game/src/dc6c939a587b397ef6ac8849756b53a351969154/sample/src/main/java/com/codenjoy/dojo/sample/client/ai/ApofigSolver.java?at=master)
- запусти этот код, чтобы проверить как твой бот играет в игру

    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new YourSolver(new RandomDice()),
                new Board());
    }
- собери jar-file, для этого выполни команду "mvn package" в корне папки sample
- jar будет находиться в sample\target\sample-engine.jar 
- вышли его нам на apofig@gmail.com с темой "Новая игра для codenjoy"

Спасибо!

Команда Codenjoy