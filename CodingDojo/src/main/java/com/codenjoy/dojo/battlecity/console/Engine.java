package com.codenjoy.dojo.battlecity.console;

import com.codenjoy.dojo.battlecity.model.AITank;
import com.codenjoy.dojo.battlecity.model.Construction;
import com.codenjoy.dojo.battlecity.model.SingleTanks;
import com.codenjoy.dojo.battlecity.model.Tanks;
import com.codenjoy.dojo.battlecity.model.levels.DefaultBorders;
import com.codenjoy.dojo.services.*;

import java.util.Arrays;

public class Engine {
    public final static int BATTLE_FIELD_SIZE = 13;

    public static void main(String[] args) {
        Tanks tanks = new Tanks(BATTLE_FIELD_SIZE, Arrays.asList(
                new Construction(1, 1), new Construction(1, 2),
                new Construction(1, 3), new Construction(1, 4),
                new Construction(1, 5), new Construction(1, 6),
                new Construction(1, 7), new Construction(1, 8),
                new Construction(1, 9), new Construction(1, 10),
                new Construction(1, 11), new Construction(2, 10),
                new Construction(3, 2), new Construction(2, 3)),
                new DefaultBorders(BATTLE_FIELD_SIZE).get(),
                new AITank(5, 5, Direction.DOWN));
        Game game = new SingleTanks(tanks, null, new RandomDice());
        game.newGame();
        Console console = new ConsoleImpl();

        new Runner(game, console).playGame();
    }
}
