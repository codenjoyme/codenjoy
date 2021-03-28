package com.codenjoy.dojo.loderunner;

import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.loderunner.services.GameRunner;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.ENEMIES_COUNT;
import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.mock;

public class PerformanceTest {

    private GameRunner runner;
    private GameSettings settings;
    private GameField field;
    private List<EventListener> listeners;
    private PrinterFactoryImpl printerFactory;
    private List<Game> games;
    private List<Joystick> heroes;
    private Dice dice;
    private List<GamePlayer> players;

    @Test
    public void test() {
        dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 20000);
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printDice = false;

        int ticks = 100;
        int playersCount = 100;
        int enemies = 20;

        runner = new GameRunner();
        settings = runner.getSettings()
                .integer(ENEMIES_COUNT, enemies);
        field = runner.createGame(0, settings);
        printerFactory = new PrinterFactoryImpl();
        listeners = new LinkedList<>();
        heroes = new LinkedList<>();
        players = new LinkedList<>();

        games = Stream.generate(() -> createGame())
                .limit(playersCount).collect(toList());

        for (int i = 0; i < ticks; i++) {
            heroes.forEach(hero -> act(move(hero)));
            field.tick();
            games.stream()
                    .filter(game -> game.isGameOver())
                    .forEach(game -> game.newGame());
            List<String> boards = games.stream()
                    .map(game -> (String)game.getBoardAsString())
                    .collect(toList());
            System.out.println(boards.get(0));
        }
    }

    private Joystick act(Joystick joystick) {
        if (dice.next(2) == 0) {
            joystick.act();
        }
        return joystick;
    }

    private Joystick move(Joystick joystick) {
        switch (Direction.random(dice)) {
            case UP: joystick.up(); break;
            case DOWN: joystick.down(); break;
            case LEFT: joystick.left(); break;
            case RIGHT: joystick.right(); break;
        }
        return joystick;
    }

    public Single createGame() {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);
        GamePlayer player = runner.createPlayer(listener,
                String.valueOf(listeners.size()), settings);
        players.add(player);
        Single single = new Single(player, printerFactory);
        single.on(field);
        single.newGame();
        heroes.add(player.getHero());
        return single;
    }
}
