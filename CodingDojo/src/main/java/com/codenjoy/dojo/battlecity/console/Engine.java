package com.codenjoy.dojo.battlecity.console;

import com.codenjoy.dojo.battlecity.model.Construction;
import com.codenjoy.dojo.battlecity.model.Direction;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.battlecity.model.Tanks;
import com.codenjoy.dojo.services.Console;
import com.codenjoy.dojo.services.ConsoleImpl;
import com.codenjoy.dojo.services.Runner;

public class Engine {
    public final static int BATTLE_FIELD_SIZE = 13;

    public static void main(String[] args) {
        Tanks game = new Tanks(BATTLE_FIELD_SIZE, new Construction(5, 5), new Tank(1, 1, Direction.UP));
        Console console = new ConsoleImpl();

        new Runner(game, console).playGame();
    }
}
