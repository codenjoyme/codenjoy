package com.codenjoy.dojo.battlecity.console;

import com.codenjoy.dojo.battlecity.model.Tanks;
import com.codenjoy.dojo.services.Console;
import com.codenjoy.dojo.services.ConsoleImpl;
import com.codenjoy.dojo.services.Runner;

public class Engine {

    public static void main(String[] args) {
        Tanks game = new Tanks();
        Console console = new ConsoleImpl();

        new Runner(game, console).playGame();
    }
}
