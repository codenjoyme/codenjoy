package com.codenjoy.dojo.lunolet.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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
import org.json.JSONObject;

public class Single implements Game {

    private Printer<JSONObject> printer;
    private Player player;
    private LunoletGame game;

    public Single(LunoletGame game, EventListener listener, PrinterFactory factory) {
        this.game = game;
        this.player = new Player(listener);

        //NOTE: We're using our custom printer here
        this.printer = new LunoletPrinter(game, player);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Joystick getJoystick() {
        return player.getHero();
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
        return !player.getHero().isAlive();
    }

    @Override
    public void newGame() {
        game.newGame(player);
    }

    @Override
    public Object getBoardAsString() {
        return printer.print();
    }

    @Override
    public void destroy() {
        game.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public HeroData getHero() {
        return GameMode.heroOnTheirOwnBoard(); //TODO: pass level
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
