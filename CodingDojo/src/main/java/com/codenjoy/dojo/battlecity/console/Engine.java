package com.codenjoy.dojo.battlecity.console;

import com.codenjoy.dojo.battlecity.model.AITank;
import com.codenjoy.dojo.battlecity.model.Construction;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.battlecity.model.Tanks;
import com.codenjoy.dojo.services.Console;
import com.codenjoy.dojo.services.ConsoleImpl;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Runner;

import java.util.Arrays;

public class Engine {
    public final static int BATTLE_FIELD_SIZE = 13;

    public static void main(String[] args) {
        Tanks game = new Tanks(BATTLE_FIELD_SIZE, Arrays.asList(
                new Construction(1, 1), new Construction(1, 2),
                new Construction(1, 3), new Construction(1, 4),
                new Construction(1, 5), new Construction(1, 6),
                new Construction(1, 7), new Construction(1, 8),
                new Construction(1, 9), new Construction(1, 10),
                new Construction(1, 11), new Construction(2, 10),
                new Construction(3, 2), new Construction(2, 3)),
                new Tank(3, 3, Direction.UP),
                new AITank(5, 5, Direction.DOWN));
        Console console = new ConsoleImpl();

        new Runner(game, console).playGame();
    }
}
