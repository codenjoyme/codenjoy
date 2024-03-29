Важный дисклеймер. Не все игры соответствуют правилам описанным тут. Но 
исправления происходят, и рано или поздно это случится со всеми играми.

Каждая игра, это отдельный maven проект с отдельным репозиторием. Все игры 
наследуют `./games/pom.xml` как родительский кофигурационный файл. Все игры 
используют как dependency `./games/engine` модуль, который в свою очередь 
зависит от `./clients/java` модуля.

В модуле `engine` содержатся все основные классы и интерфейсы, выделенные из 
множества игр для устранения дублирования. Их много, и они будут описаны 
отдельно ниже.

В модуле `clients/java` содержатся все базовые классы необходимые для участия 
в игре. Участник подключается по web-socket из клиента, содержащего базовые 
классы для написания AI алгоритма. Они находятся в этом модуле сразу для всех 
игр. А именно:
1. Element - enum c перечнем вариантов отображений объектов на поле в 
текстовом представлении.
2. Board - класс позволяющий парсить текстовое представление поля и предоставлять 
высокоуровневые методы доступа.
3. YourSolver - собственно AI игрока.

Все игры делятся на 2 типа: графические и текстовые. Эталоны игр - игра 
(модуль) `./games/sample` для графических и `./games/sample-text` для текстовых.
Все архитектурные изменения начинаются вначале с этих игр, а потом разносятся
по всем остальным в порядке: `sample`, `mollymage`, `clifford`, `verland`, 
`rawelbbub`, `namdreab`. Пока что это игры в которых реализованы все архитектурные
новшества. 

Структура проекта изображена на картинке ниже.

![sample-structure.png](sample-structure.png)

Проект состоит из production кода (1), тестов игры (2), и ресурсов (3), 
web-ресурсов (4) и прикорневых файлов (5): maven кофигурации проекта, файла 
описания клиента игры для игрока, gitignore. 

В игре самое главное место - реализация абстрактной фабрики класс GameRunner (6).
Этот класс инстанциирует все важные компоненты игры и связывает их вместе. 
Игровой сервер знает все про игру через этот класс. 

Каждая игра содержит ряд настроек (7), которые Сенсей (фасилитатор ивента) может
менять до начала или во время игры через свою админку. Настройки могут касаться 
режимов работы игры: появления на поле определенных объектов в заданном количестве,
значений очков за определенные достижения и штрафов, режимов подсчета очков, режимов 
мультипользовательской игры, конфигураций уровней и так далее. Каждая игра 
самостоятельно реализует свои настройки, но есть базовые для всех настройки описанные в
`AllSettings`, который находится в модуле `engine`. Некоторые игры, которые не 
включают все архитектурные новшества могут реализовывать часть настроек, предложенных
в `AllSettings`.
```
public class GameSettings extends SettingsImpl implements AllSettings<GameSettings> {
```

Следующий по значимости класс `Sample` (8) (его название соответствует названию игры). 
В этом классе сосредоточена большая часть логики самой игры, она отвечает за поведение 
объектов на поле, отрисовку их и так далее. Этот класс и есть само игровое поле. 
В самом классе игры `sample` достаточно подробно описаны все артефакты. Приглашаю 
отправиться в путешествие по исходному коду.

Достаточно большая часть логики содержится в классе `Hero` (9). Он инкапсулирует поведение 
героя на поле и способы управления им от имени участника. Для этого он реализует интерфейс
`Joystick`. Обрати внимание, что могут использоваться различные helper абстрактные классы 
и интерфейсы, находящиеся `engine` модуле. Сделано это для удобства написания игры и 
устранения дублирования между играми. От игры к игре коибинация наследования/реализации 
разных helper компонентов может отличаться. Смотри документацию к каждому такому 
классу/интерфейсу.

Игрока представляет объект `Player` (10). Чаще всего он неразрывно связан с объектом `Hero` (9) и 
полем (8), на котором он инициализирован. Для исключения цикличной зависимости между
`Player`/`Hero` и `Sample` (8) используется интерфейс `Field` (8b). Одна из основных
ролей класса игрока - отправлять события `Event` (14). 

События в игре могут быть разных типов: от банального `Enum` до объекта с набором параметров.
События могут приводить к генерации очков, что происходит в классе `Score` (15). 

Остальная часть логики игры находится в пакете `model.items` (11). В нем содержатся все объекты,
которые могут находиться на поле. Каждый объект содержит свою координату на поле и может 
быть представлен в текстовом виде. Для этого в методе `state` используется `Enum` `Element` расположенный 
в `clients/java` модуле. Чаще всего объекты примитивны и за них отвечает `Sample` (8), но 
если объект имеет сложное поведение и перемещается на поле - тут уже может быть 
собственная реализация.

Класс `Level` (12) отвечает за загрузку уровня из текстового представления. А в классе
`Levels` (13) содержится список всех уровней игры загружаемый в `GameSettings` (7).

Класс `AISolver` (16) реализует AI, который будет бегать по полю с первым зарегистрировавшимся
игроком от имени автора игры. Стоит позаботиться, чтобы производительность этого класса
была высока. Показатели производительности игры и всех ее обитателей проверяются в тесте 
`PerformanceTest` (22). 

Пакет `runner` (17) содержит два класса `Dry` для запуска игры в консоли для беглой проверки 
как все устроено, и класса `Main` необходимого для запуска игры в упрощенном варианте без 
сервера. 

Дальше следуют пакет с основными тестами игры в пакете `model` (18) наследующимися от 
асбтрактного класса `AbstractGameTest` (23). Эти тесты хоть и используются как unit
(главное свойство - ранаться быстро) но являются интеграционными и 
функциональными. Почему был выбран такой подход читай в мануале `docs/testing/smart-assert.md`.


В пакете `service` (19) можно найти несколько юнит тестов компонентов. 

Базовые настройки игры для всех тестов задаются в `TestGameSettings` (20). 
Каждый test class может их переопределять в своем @Before методе или методе перегружающем 
получения settings объекта. Так же любой тест может задавать свои конкретные настройки, 
если нужно подсветить их взаимосвязть с тестовой ситуацией.

`SmokeTest` (21) использует Approvals подход и является максимально интеграционным тестом игры.
Он прогоняет на поле их обитателей псевдо-рендомно достаточно большое 
количество (порядка 1000) тиков. При этом записываются в плоский файл все состояния борд 
и все события. Этот тест хрупкий и результаты его работы после любого изменения в игре должны 
быть перезаписаны. Не стоит руками менять файл `SmokeTest.data` (21b) - новую версию, после 
визуальной валидации их отличий через diff тулу ide стоит копировать из `target` на место (21b).
Этот тест - последний рубеж тестирования, и он помогает в сложных рефакторингах, когда нужно
точно убедиться что ничего не поломано, но остальных тестов может оказаться недостаточно (т.к.
code coverage хоть и на высоком уровне - порядка 80-90% для игры, но все же не 100%).  

Отправляемся к ресурсам игры. Как говорилось раньше их есть два типа ресурсы игры (3) - 
то что будет использовано классами и web-ресурсы (4) - то как игра (или ее аспекты) 
будут воспроизводиться в браузере. В `sample` игре нет полноценного джаваскриптового движка, 
а потому тут все очень примитивно. Сейчас есть как минимум две игры `icancode` и `kata`
содержащие полноценный javascript UI. И за него отвечают файлы в папке `js` (29), а так 
же стили в `css` (29b), изображения в `img` (29c) и возможно другие ресурсы, которые можно 
добавить на этот уровень(31). 

Отдельно стоит сказать про спрайты `sprite` (30), благодаря которым поле игры отрисовывается 
графически на canvas компоненте браузера. Название `png` файлов соответствует названию
`Element` `Enum` в `clients/java` модуле.

Так же особняком стоит `help` (24) папка, в которой происходит магия сборки мануалов для игры.
Из-за большого количества дублирующейся информации в похожих играх и необходимости поддерживать
это на как минимум 2х языках, а так же в двух вариациях (community версия и версия для dojorena) -
было решено разделить мануалы на разделы, каждый из которых может быть переопределен 
игрой индивидуально. Все мануалы после генерации соберутся в результирующие файлы (27). 
А для комьюнити версии они будут конвертированы в html с использованием `header` 
и `footer` темплейтов (28). 

Очень важно обратить внимание на properties файлы `info.properties` (25) и (26) - в них
содержится локализированные названия элементов игры `Elements` а так же описание настроек
`GameSettings` (7). `Elements` для других клиентов игры расположенных в `./clients`
кроме `java` генерируются автоматически используя `java` `Elements` а так же 
`info.properties` для комментариев к константам.

Фух. Вот и все. Теперь можно приступать к разработке игры.

Приятного аппетита!