package com.codenjoy.dojo.services.multiplayer;

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

    private Printer printer;
    private GamePlayer player;
    private GameField field;
    private MultiplayerType multiplayerType;

    public Single(GameField field, GamePlayer player, PrinterFactory factory) {
        this(field, player, factory, MultiplayerType.SINGLE);
    }

    public Single(GameField field, GamePlayer player, PrinterFactory factory, MultiplayerType multiplayerType) {
        this.field = field;
        this.player = player;
        this.multiplayerType = multiplayerType;
        this.printer = factory.getPrinter(field.reader(), player);
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
    public int getMaxScore() {
        return player.getMaxScore();
    }

    @Override
    public int getCurrentScore() {
        return player.getScore();
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
        return printer.print();
    }

    @Override
    public void destroy() {
        field.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
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
        return null;
    }

    @Override
    public void tick() {
        field.tick();
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public GameField getField() {
        return field;
    }

}
