package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.hero.GameMode;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;

import static com.codenjoy.dojo.bomberman.model.Elements.BOMBERMAN;
import static com.codenjoy.dojo.bomberman.model.Elements.MEAT_CHOPPER_PLAYER;
import static java.lang.String.valueOf;

/**
 * User: sanja
 * Date: 16.04.13
 * Time: 21:43
 */
public class Single implements Game {

    private Player player;
    private Bomberman game;

    private Printer<String> printer;

    public Single(Bomberman game, EventListener listener, PrinterFactory factory) {
        this.game = game;
        player = new Player(listener);
        printer = factory.getPrinter(game.reader(), player);
    }

    public Single(Bomberman game, EventListener listener, PrinterFactory factory, boolean bot) {
        this.game = game;
        player = new Player(listener, bot);
        printer = factory.getPrinter(game.reader(), player);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Joystick getJoystick() {
        return player.getBomberman();
    }

    @Override
    public int getMaxScore() {
        return player.getMaxScore();
    }

    @Override
    public int getCurrentScore() {
        return player.getScore();
    }

    @Override
    public boolean isGameOver() {
        return !player.getBomberman().isAlive();
    }

    @Override
    public void newGame() {
        game.newGame(player);
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    @Override
    public Object wrapScreen(Object map) {
        if (!player.isBot() || !(map instanceof String)) {
            return map;
        }
        return ((String) map).replace(valueOf(BOMBERMAN.ch()), valueOf(MEAT_CHOPPER_PLAYER.ch()));
    }

    @Override
    public void destroy() {
        game.getLevel().remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public HeroData getHero() {
        return GameMode.allHeroesOnSingeBoard(player.getBomberman());
    }

    @Override
    public String getSave() {
        return null;
    }

    @Override
    public void tick() {
        game.tick();
    }
}
