package com.codenjoy.dojo.loderunner.game;

import com.codenjoy.dojo.games.loderunner.Element;
import com.codenjoy.dojo.loderunner.TestSettings;
import com.codenjoy.dojo.loderunner.model.Hero;
import com.codenjoy.dojo.loderunner.model.Loderunner;
import com.codenjoy.dojo.loderunner.model.Player;
import com.codenjoy.dojo.loderunner.model.items.Brick;
import com.codenjoy.dojo.loderunner.model.items.enemy.EnemyJoystick;
import com.codenjoy.dojo.loderunner.model.levels.Level;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractGameTest {

    protected Loderunner game;

    private List<Hero> heroes = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private List<Joystick> enemies = new LinkedList<>();

    protected Dice dice = mock(Dice.class);
    protected PrinterFactory<Element, Player> printer = new PrinterFactoryImpl<>();
    protected GameSettings settings = new TestSettings();

    protected EventListener listener;
    protected EventsListenersAssert events = new EventsListenersAssert(() -> Arrays.asList(listener), Events.class);

    @Before
    public void setup() {
        Brick.DRILL_TIMER = 13;
    }

    @After
    public void tearDown() {
        events.verifyNoEvents();
    }

    protected void givenFl(String map) {
        settings.string(LEVEL_MAP, map);

        Level level = settings.level();
        settings.integer(GOLD_COUNT_YELLOW, level.getYellowGold().size())
                .integer(GOLD_COUNT_GREEN, level.getGreenGold().size())
                .integer(GOLD_COUNT_RED, level.getRedGold().size())
                .integer(SHADOW_PILLS_COUNT, level.getPills().size())
                .integer(PORTALS_COUNT, level.getPortals().size())
                .integer(ENEMIES_COUNT, level.getEnemies().size());

        List<Hero> levelHeroes = level.getHeroes();
        if (levelHeroes.isEmpty()) {
            throw new IllegalStateException("Нет героя!");
        }

        game = new Loderunner(dice, settings);
        listener = mock(EventListener.class);

        for (Hero hero : levelHeroes) {
            dice(hero.getX(), hero.getY());
            Player player = new Player(listener, settings);
            players.add(player);
            game.newGame(player);
            heroes.add(player.getHero());
            player.getHero().setDirection(hero.getDirection());
        }
        reloadAllEnemies();

        dice(0); // всегда дальше выбираем нулевой индекс
    }

    protected void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    protected Hero hero() {
        return heroes.get(0);
    }

    protected Hero hero(int index) {
        return heroes.get(index);
    }

    protected Player player() {
        return players.get(0);
    }

    protected Joystick enemy() {
        return enemies.get(0);
    }

    protected Joystick enemy(int index) {
        return enemies.get(index);
    }

    protected void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player()).print());
    }

    protected void reloadAllHeroes() {
        players = game.players();
        heroes = game.heroes().all();
    }

    protected void reloadAllEnemies() {
        enemies = game.enemies().stream()
                .map(EnemyJoystick::new)
                .collect(Collectors.toList());
    }
}
