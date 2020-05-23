package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.junit.Test;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;

public class RoundBattleSingleTest extends AbstractSingleTest {

    public static final int DEFAULT_COUNT = 3;
    private int minTicksForWin = 1;
    private int timePerRound = 10;
    private int timeForWinner = 2;
    private int timeBeforeStart = 5;
    private int roundsPerMatch = 3;

    @Override
    protected RoundSettingsWrapper getRoundSettings() {
        return new RoundSettingsWrapper() {

            @Override
            public Parameter<Integer> timeBeforeStart() {
                return v(timeBeforeStart);
            }

            @Override
            public Parameter<Integer> roundsPerMatch() {
                return v(roundsPerMatch);
            }

            @Override
            public Parameter<Integer> minTicksForWin() {
                return v(minTicksForWin);
            }

            @Override
            public Parameter<Integer> timePerRound() {
                return v(timePerRound);
            }

            @Override
            public Parameter<Integer> timeForWinner() {
                return v(timeForWinner);
            }

            @Override
            public Parameter<Boolean> roundsEnabled() {
                return new SimpleParameter<>(true);
            }
        };
    }

    // во время старта игры, когда не прошло timeBeforeStart тиков,
    // все игроки неактивны (видно их трупики)
    @Test
    public void shouldAllPlayersOnBoardIsInactive_whenStart() {
        playersPerRoom.update(DEFAULT_COUNT);

        givenBoard(DEFAULT_COUNT);

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "♣Ѡ   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "♣♣   \n", game(2));
    }

    // после старта идет отсчет обратного времени
    @Test
    public void shouldCountdownBeforeRound_whenTicksOnStart() {
        shouldAllPlayersOnBoardIsInactive_whenStart();

        verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");

        tick();

        verifyAllEvents(
                "listener(0) => [[....4....]]\n" +
                "listener(1) => [[....4....]]\n" +
                "listener(2) => [[....4....]]\n");

        tick();

        verifyAllEvents(
                "listener(0) => [[...3...]]\n" +
                "listener(1) => [[...3...]]\n" +
                "listener(2) => [[...3...]]\n");

        tick();

        verifyAllEvents(
                "listener(0) => [[..2..]]\n" +
                "listener(1) => [[..2..]]\n" +
                "listener(2) => [[..2..]]\n");

        tick();

        verifyAllEvents(
                "listener(0) => [[.1.]]\n" +
                "listener(1) => [[.1.]]\n" +
                "listener(2) => [[.1.]]\n");
    }

    // пока идет обратный отсчет я не могу ничего предпринимать, а герои отображаются на карте как трупики
    // но после объявления раунда я могу начать играть
    @Test
    public void shouldActiveAndCanMove_afterCountdown() {
        shouldCountdownBeforeRound_whenTicksOnStart();

        // пока еще не активны
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "♣Ѡ   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "♣♣   \n", game(2));

        // и я не могу ничего поделать с ними
        hero(0).up();
        hero(1).right();
        hero(2).up();

        tick();

        // после сообщения что раунд начался
        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        // можно играть - игроки видны как активные
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "☺♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "♥☺   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "♥♥   \n", game(2));

        // ... и когда я муваю героев, они откликаются
        hero(0).up();
        hero(1).right();
        hero(2).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                " ♥   \n" +
                "☺    \n" +
                "  ♥  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                " ♥   \n" +
                "♥    \n" +
                "  ☺  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                " ☺   \n" +
                "♥    \n" +
                "  ♥  \n", game(2));
    }

    // если один игрок вынесет другого но на поле есть едще игроки,
    // то тот, которого вынесли появится в новом месте в виде трупика
    @Test
    public void shouldMoveToInactive_whenKillSomeone() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1; // TODO а что будет если тут 0 игра хоть начнется?

        givenBoard(DEFAULT_COUNT);

        tick();

        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "☺♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "♥☺   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "♥♥   \n", game(2));

        // когда я выношу одного игрока
        hero(2).act();
        tick();

        hero(2).right();
        tick();

        hero(2).up();
        tick();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "  ☺  \n" +
                " 1   \n" +
                "♥♥   \n", game(2));

        // игрок активный и живой
        assertEquals(true, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());
        assertEquals(true, player(1).wantToStay());
        assertEquals(false, player(1).shouldLeave());

        tick();

        // игрок активный но неживой (cервер ему сделает newGame)
        assertEquals(true, hero(1).isActive());
        assertEquals(false, hero(1).isAlive());
        // тут без изменений
        assertEquals(true, player(1).wantToStay());
        assertEquals(false, player(1).shouldLeave());


        asrtBrd("     \n" +
                "     \n" +
                " ҉☺  \n" +
                "҉҉҉  \n" +
                "♥♣   \n", game(2));

        tick();

        dice(heroDice, 3, 4); // новые координаты для героя
        newGame(1); // это сделоает сервер в ответ на isAlive = false

        // игрок уже живой но неактивный до начала следующего раунда
        assertEquals(false, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());
        // тут без изменений
        assertEquals(true, player(1).wantToStay());
        assertEquals(false, player(1).shouldLeave());

        asrtBrd("   ♣ \n" +
                "     \n" +
                "  ☺  \n" +
                "     \n" +
                "♥    \n", game(2));
    }

    // если один игрок вынесет обоих, то должен получить за это очки
    @Test
    public void shouldGetWinRoundScores_whenKillAllEnemies() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1;

        dice(heroDice,
                1, 1, // первый игрок
                0, 1, // второй
                1, 0); // третий

        givenBoard(DEFAULT_COUNT);

        tick();

        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "♥☺   \n" +
                " ♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                " ♥   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "♥♥   \n" +
                " ☺   \n", game(2));

        // когда я выношу одного игрока
        hero(0).act();
        tick();

        hero(0).right();
        tick();

        hero(0).up();
        tick();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "  ☺  \n" +
                "♥1   \n" +
                " ♥   \n", game(0));

        tick();

        asrtBrd("     \n" +
                "     \n" +
                " ҉☺  \n" +
                "♣҉҉  \n" +
                " ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                " ҉♥  \n" +
                "Ѡ҉҉  \n" +
                " ♣   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                " ҉♥  \n" +
                "♣҉҉  \n" +
                " Ѡ   \n", game(2));

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, KILL_OTHER_HERO, WIN_ROUND]\n" +
                "listener(1) => [DIED]\n" +
                "listener(2) => [DIED]\n");
    }

    // если на поле трое, и один игрок имеет преимущество по очкам за вынос другого игрока
    // то по истечении таймаута раунда он получит очки за победу в раунде
    @Test
    public void shouldGetWinRoundScores_whenKillOneEnemyAdvantage_whenRoundTimeout() {
        int count = 3;

        playersPerRoom.update(count);
        timeBeforeStart = 1;

        dice(heroDice,
                1, 1, // первый игрок
                0, 2, // второй - его не накроет волной
                1, 0); // третий - его накроет волной

        givenBoard(count);

        tick();

        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                " ☺   \n" +
                " ♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                " ♥   \n" +
                " ♥   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                " ♥   \n" +
                " ☺   \n", game(2));

        // когда я выношу одного игрока
        hero(0).act();
        tick();

        hero(0).right();
        tick();

        hero(0).up();
        tick();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "♥ ☺  \n" +
                " 1   \n" +
                " ♥   \n", game(0));

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "♥҉☺  \n" +
                "҉҉҉  \n" +
                " ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "☺҉♥  \n" +
                "҉҉҉  \n" +
                " ♣   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "♥҉♥  \n" +
                "҉҉҉  \n" +
                " Ѡ   \n", game(2));

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(1) => []\n" +
                "listener(2) => [DIED]\n");

        // затем пройдет еще некоторое количество тиков, до общего числа = timePerRound
        tick();
        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "♥ ☺  \n" +
                "     \n" +
                " ♣   \n", game(0));

        verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");

        // вот он последний тик раунда, тут все и случится
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" +
                "listener(1) => [[Time is over]]\n" +
                "listener(2) => []\n");
    }

    // если на поле группа игроков, два из них активны и расставляют бомбы
    // так вот после окончания таймаута раунда тот из них победит,
    // кто большее количество игроков вынес
    @Test
    public void shouldGetWinRoundScores_whenKillsAdvantage_whenRoundTimeout() {
        int count = 5;

        playersPerRoom.update(count);
        timeBeforeStart = 1;

        dice(heroDice,
                1, 1, // первый активный игрок - будет победителем
                3, 3, // второй активный игрок - будет проигравшим
                1, 0, // жертва первого
                0, 1, // жертва первого
                3, 4); // единственная жертва второго, потому он проиграет по очкам

        givenBoard(count);

        tick();

        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n" +
                "listener(3) => [START_ROUND, [Round 1]]\n" +
                "listener(4) => [START_ROUND, [Round 1]]\n");

        assertBoards(
                "game(0)\n" +
                "   ♥ \n" +
                "   ♥ \n" +
                "     \n" +
                "♥☺   \n" +
                " ♥   \n" +
                "\n" +
                "game(1)\n" +
                "   ♥ \n" +
                "   ☺ \n" +
                "     \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(2)\n" +
                "   ♥ \n" +
                "   ♥ \n" +
                "     \n" +
                "♥♥   \n" +
                " ☺   \n" +
                "\n" +
                "game(3)\n" +
                "   ♥ \n" +
                "   ♥ \n" +
                "     \n" +
                "☺♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(4)\n" +
                "   ☺ \n" +
                "   ♥ \n" +
                "     \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n");

        // пошла движуха
        hero(0).act();
        hero(1).act();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        tick();

        asrtBrd("   ♥ \n" +
                " ♥ 1 \n" +
                "     \n" +
                "♥1 ☺ \n" +
                " ♥   \n", game(0));

        tick();

        assertBoards(
                "game(0)\n" +
                "   ♣ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉☺ \n" +
                " ♣   \n" +
                "\n" +
                "game(1)\n" +
                "   ♣ \n" +
                " ☺҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(2)\n" +
                "   ♣ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉♥ \n" +
                " Ѡ   \n" +
                "\n" +
                "game(3)\n" +
                "   ♣ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "Ѡ҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(4)\n" +
                "   Ѡ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, KILL_OTHER_HERO]\n" +
                "listener(1) => [KILL_OTHER_HERO]\n" +
                "listener(2) => [DIED]\n" +
                "listener(3) => [DIED]\n" +
                "listener(4) => [DIED]\n");

        // затем пройдет еще некоторое количество тиков, до общего числа = timePerRound
        tick();
        tick();
        tick();
        tick();

        asrtBrd("   ♣ \n" +
                " ♥   \n" +
                "     \n" +
                "♣  ☺ \n" +
                " ♣   \n", game(0));

        verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n");

        // вот он последний тик раунда, тут все и случится
        dice(heroDice, 0, 0); // размещаем всех возле левого нижнего угла
        tick();

        asrtBrd("   ♣ \n" +
                "     \n" +
                "     \n" +
                "♣♣   \n" +
                "Ѡ♣   \n", game(0));

        verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" +
                "listener(1) => [[Time is over]]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n");
    }

    // если на поле группа игроков, два из них активны и расставляют бомбы
    // и даже уничтожили одинаковое количество игроков
    // так вот после окончания таймаута раунда тот из них победит,
    // кто большее очков заработал во время своего экшна (в данном случае коробку)
    @Test
    public void shouldGetWinRoundScores_whenKillsAdvantagePlusOneBox_whenRoundTimeout() {
        int count = 6;

        playersPerRoom.update(count);
        timeBeforeStart = 1;

        dice(heroDice,
                1, 1, // первый активный игрок - будет проигравшим
                3, 3, // второй активный игрок - будет победителем, потому как снесет еще корбку
                1, 0, // жертва первого
                0, 1, // жертва первого
                3, 4, // жертва второго
                4, 3); // жертва второго

        givenWalls(new DestroyWall(3, 2));
        givenBoard(count);

        tick();

        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n" +
                "listener(3) => [START_ROUND, [Round 1]]\n" +
                "listener(4) => [START_ROUND, [Round 1]]\n" +
                "listener(5) => [START_ROUND, [Round 1]]\n");

        assertBoards(
                "game(0)\n" +
                "   ♥ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "♥☺   \n" +
                " ♥   \n" +
                "\n" +
                "game(1)\n" +
                "   ♥ \n" +
                "   ☺♥\n" +
                "   # \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(2)\n" +
                "   ♥ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "♥♥   \n" +
                " ☺   \n" +
                "\n" +
                "game(3)\n" +
                "   ♥ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "☺♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(4)\n" +
                "   ☺ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(5)\n" +
                "   ♥ \n" +
                "   ♥☺\n" +
                "   # \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n");

        // пошла движуха
        hero(0).act();
        hero(1).act();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        tick();

        asrtBrd("   ♥ \n" +
                " ♥ 1♥\n" +
                "   # \n" +
                "♥1 ☺ \n" +
                " ♥   \n", game(0));

        tick();

        assertBoards(
                "game(0)\n" +
                "   ♣ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉☺ \n" +
                " ♣   \n" +
                "\n" +
                "game(1)\n" +
                "   ♣ \n" +
                " ☺҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(2)\n" +
                "   ♣ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " Ѡ   \n" +
                "\n" +
                "game(3)\n" +
                "   ♣ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "Ѡ҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(4)\n" +
                "   Ѡ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(5)\n" +
                "   ♣ \n" +
                " ♥҉҉Ѡ\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, KILL_OTHER_HERO]\n" +
                "listener(1) => [KILL_DESTROY_WALL, KILL_OTHER_HERO, KILL_OTHER_HERO]\n" +
                "listener(2) => [DIED]\n" +
                "listener(3) => [DIED]\n" +
                "listener(4) => [DIED]\n" +
                "listener(5) => [DIED]\n");

        // затем пройдет еще некоторое количество тиков, до общего числа = timePerRound
        tick();
        tick();
        tick();
        tick();

        asrtBrd("   ♣ \n" +
                " ♥  ♣\n" +
                "     \n" +
                "♣  ☺ \n" +
                " ♣   \n", game(0));

        verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n");

        // вот он последний тик раунда, тут все и случится
        dice(heroDice, 0, 0); // размещаем всех возле левого нижнего угла
        tick();

        asrtBrd("   ♣ \n" +
                "    ♣\n" +
                "     \n" +
                "♣♣   \n" +
                "Ѡ♣   \n", game(0));

        verifyAllEvents(
                "listener(0) => [[Time is over]]\n" +
                "listener(1) => [WIN_ROUND]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n");
    }

}
