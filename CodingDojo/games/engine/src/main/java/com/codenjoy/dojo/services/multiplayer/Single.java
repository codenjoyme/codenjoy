package com.codenjoy.dojo.services.multiplayer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.hero.HeroDataImpl;

/**
 * Этот малый является фасадом для трех объектов:
 * {@see GamePlayer} игрока, {@see GameField} борды (игры)
 * и {@see Printer} который может отрисовать игру.
 * Дальше фреймворк пользуется связкой этих трех объектов
 * инкапсулируя их в понятие Игра {@see Game}.
 */
public class Single implements Game {

    private PrinterFactory factory;
    private Printer printer;
    private GamePlayer player;
    private GameField field;
    private MultiplayerType multiplayerType;

    public Single(GamePlayer player, PrinterFactory factory) {
        this(player, factory, MultiplayerType.SINGLE);
    }

    public Single(GamePlayer player, PrinterFactory factory, MultiplayerType multiplayerType) {
        this.player = player;
        this.multiplayerType = multiplayerType;
        this.factory = factory;
    }

    public void on(GameField field) {
        this.field = field;
        if (field == null) {
            printer = null;
        } else {
            printer = factory.getPrinter(field.reader(), player);
        }
    }

    /**
     * @return Джойстик для управления героем, где бы он ни был реализован
     */
    @Override
    public Joystick getJoystick() {
        Joystick joystick = player.getJoystick();
        if (joystick != null) {
            return joystick;
        }
        return player.getHero();
    }

    @Override
    public boolean isGameOver() {
        return !player.isAlive();
    }

    @Override
    public void newGame() {
        field.newGame(player);
    }

    @Override
    public Object getBoardAsString() {
        if (printer == null) {
            throw new IllegalStateException("No board for this player");
        }

        return printer.print();
    }

    @Override
    public void close() {
        field.remove(player);
    }

    @Override
    public void clearScore() {
        field.clearScore();
    }

    /**
     * @return информация о герое, что пойдет на UI.
     */
    @Override
    public HeroData getHero() {
        HeroData heroData = player.getHeroData();
        if (heroData != null) {
            return heroData;
        }
        return new HeroDataImpl(player.getHero(),
                multiplayerType.isMultiplayer());
    }

    @Override
    public String getSave() {
        return field.getSave();
    }

    @Override
    public GamePlayer getPlayer() {
        return player;
    }

    @Override
    public GameField getField() {
        return field;
    }

}
