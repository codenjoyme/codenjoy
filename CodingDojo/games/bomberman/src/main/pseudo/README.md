0) This works only for OS Windows 

1) Please register 

1.1) Go to https://dojorena.io/

1.2) Press SignUp

1.3) Fill registration form

1.3.1) Select Bomberman game

1.3.2) Then press Submit button

2) Setup your client

2.1) Then copy board url from browser 

https://dojorena.io/codenjoy-contest/board/player/playerId?code=12345678901234567890&gameName=bomberman

2.2) Edit 0-settings.bat file as text

2.3) Set bomberman game at first line 

set GAME_TO_RUN=bomberman

2.4) Set Board url at second line 

set BOARD_URL=https://dojorena.io/codenjoy-contest/board/player/playerId?code=12345678901234567890&gameName=bomberman

2.5) Save 0-settings.bat file and close

3) Run 3-run-client.bat script

3.1) Wait till board will come

Board:
☼☼☼☼☼☼☼
☼ &  #☼
☼ ☼#☼ ☼
☼     ☼
☼☺☼#☼ ☼
☼     ☼
☼☼☼☼☼☼☼
Answer: UP

4) Open \rules\main.rules file as text

4.1) Now you can write your script to control the hero on the map. How? It's pretty simple. 

4.2) If you change anything, just save the file and your hero will follow the new directions.

4.3) In the process of programming a hero, you can make a mistake. Don’t worry, we will let you know exactly where and why it happened.

4.3.1) For example: [ERROR] Pattern is not valid: '???wrong?☺????' at C:\bomberman-pseudo-client-portable\rules\main.rule:91

5) Есть несколько типов команд, которые ты можешь использовать в *.rules файле

5.1) Указывай, на что должно быть похоже пространство вокруг героя, а следом укажи направление движения героя

5.1.1) Твой герой обозначается символом ☺ 

5.1.2) Пространство вокруг героя (маска) должно быть квадратной! формы любого размера, при этом не важно где расположен герой (по центру маски или на ее границе)

5.1.2.1) Тут нас интересует что на 1 клеточку вокруг героя

☼  
☼☺☼
☼  

5.1.2.2) Тут нас интересует что на 2 клеточки вокруг героя

☼☼ ☼#
☼☼   
☼☼☺☼#
☼☼   
☼☼☼☼☼

5.1.2.3) Тут нас интересут, что в левом верхнем углу от героя

 ☼#
   
☺☼#

5.1.3) Следующей строкой после маски укажи направление движения - одну из команд: LEFT, RIGHT, UP, DOWN или команду ACT - поставить бомбу

5.1.3.1) Если картинка вокруг героя будет похоже на эту маску, он побежит вверх

☼  
☼☺☼
☼  
UP

5.1.3.2) А в этом случае (если герою ничего не мешает) он отправится вниз

☼ ☼
 ☺ 
☼ ☼
DOWN

5.2) Оставленная бомба взорвется через 5 тиков (1 тик - 1 секунда) и взрывной волной уничтожит всех, кого заденет. 

5.3) Ты так же можешь указать не 1 действие, а несколько разделенных запятой. Это будет значить, что при срабатывании заданной маски бдут отрабатываться указанные команды тик за тиком. Например в примере ниже, если герой увидит слева от себя бомбу, то он вначале походит вправо, а потом спрячется вверх от взрывной волны.

☼☼☼☼ 
☼ ☼  
☼5☺  
☼ ☼  
☼☼☼☼ 
RIGHT,UP

5.4) Часто бывает так, что ты не знаешь какой символ будет в этой конкретной точке, и хочешь обобщить. Для этого у тебя есть символ ? который означает любой возможный символ в этой точке. Прошлый пример будет более универсальным, если его описать так 

???? 
???  
?5☺  
???? 
???? 
RIGHT,UP

???? 
???? 
?5☺  
???  
???? 
RIGHT,DOWN

???? 
?? ? 
?5☺? 
???? 
???? 
UP

???? 
???? 
?5☺? 
?? ? 
???? 
DOWN

Тут важно понимать, что в списке команд отработает первая совпавшая маска и дальше проверка осуществляться не будет.

5.5) Легенда возможных символов
	/// This is your Bomberman
    BOMBERMAN('☺'),             // this is what he usually looks like
    BOMB_BOMBERMAN('☻'),        // this is if he is sitting on own bomb
    DEAD_BOMBERMAN('Ѡ'),        // oops, your Bomberman is dead (don't worry, he will appear somewhere in next move)
                                // you're getting penalty points for each death

    /// this is other players Bombermans
    OTHER_BOMBERMAN('♥'),       // this is what other Bombermans looks like
    OTHER_BOMB_BOMBERMAN('♠'),  // this is if player just set the bomb
    OTHER_DEAD_BOMBERMAN('♣'),  // enemy corpse (it will disappear shortly, right on the next move)
                                // if you've done it you'll get score points

    /// the bombs
    BOMB_TIMER_5('5'),          // after bomberman set the bomb, the timer starts (5 ticks)
    BOMB_TIMER_4('4'),          // this will blow up after 4 tacts
    BOMB_TIMER_3('3'),          // this after 3
    BOMB_TIMER_2('2'),          // two
    BOMB_TIMER_1('1'),          // one
    BOOM('҉'),                  // Boom! this is what is bomb does, everything that is destroyable got destroyed

    /// walls
    WALL('☼'),                  // indestructible wall - it will not fall from bomb
    DESTROYABLE_WALL('#'),      // this wall could be blowed up
    DESTROYED_WALL('H'),        // this is how broken wall looks like, it will dissapear on next move
                                // if it's you did it - you'll get score points.

    /// meatchoppers
    MEAT_CHOPPER('&'),          // this guys runs over the board randomly and gets in the way all the time
                                // if it will touch bomberman - it will die
                                // you'd better kill this piece of ... meat, you'll get score points for it
    DEAD_MEAT_CHOPPER('x'),     // this is chopper corpse

    /// a void
    NONE(' ');                 // this is the only place where you can move your Bomberman

5.6) Бывают случаи, когда надо объединить несколько символов в одну группу. Для этого служит директива LET A=QWERTYUIOP, где A - символ который можно использовать в маске после команды LET, а QWERTYUIOP - символы, которые будут подставляться вместо А. Например, так можно обобщить наш прошлый пример с спасением от бомбы, чтобы герой убегал не только от бомбы с таймером 5, но и от 4 3 2 1.

LET B=54321

???? 
???  
?B☺  
???? 
???? 
RIGHT,UP

???? 
???? 
?B☺  
???  
???? 
RIGHT,DOWN

???? 
?? ? 
?B☺? 
???? 
???? 
UP

???? 
???? 
?B☺? 
?? ? 
???? 
DOWN

5.7) Так же может захотеться разместить часть сценария поведения в отдельном файле. Например, если бомбы нет - мы делаем одно действие, но если бомба появилась - мы убегаем. Для этого вместо конкретной команды LEFT, RIGHT, UP, DOWN или ACT стоит указать директиву RULE:

&☺
??
RULE runAway.rule

☺&
??
RULE runAway.rule

??
☺&
RULE runAway.rule

??
&☺
RULE runAway.rule

В этом скрипте мы сказали, что если вокруг нашего героя появится охотник, надо бежать. А как бежать - рассказано в файле runAway.rule. Внутри нового скрипта можно писать все то же что и в главном main.rule скрипте. 

5.8) Не забывай, что каждая маска должна быть квадратной формы (2x2, 3x3, 4x4, 5x5). Так же следи внимательно за символами ' ' - это пустое место на поле, которое не занято ничем. Символ ' ' легко пропустить, а потому перероверяй.

6) Все участники играют на одном поле. Побеждает тот, кто за заданое время заработает больше всего очков.

6.1) За разрущение разрушаемой стенки ты получаешь +10

6.2) Если удастся поймать охотника получишь +100

6.3) Если удастся поймать другого бомбермена получишь +1000

7) Codenjoy!