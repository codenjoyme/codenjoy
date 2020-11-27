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
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.hero.HeroDataImpl;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.json.JSONObject;

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
    private LevelProgress progress;

    public Single(GamePlayer player, PrinterFactory factory) {
        this(player, factory, MultiplayerType.SINGLE);
    }

    public Single(GamePlayer player, PrinterFactory factory, MultiplayerType multiplayerType) {
        this.player = player;
        this.multiplayerType = multiplayerType;
        this.progress = multiplayerType.progress();
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

    @Override
    public void setProgress(LevelProgress progress) {
        this.progress = progress;
    }

    @Override
    public LevelProgress getProgress() {
        return progress;
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
    public boolean isWin() {
        return player.isWin();
    }

    @Override
    public boolean shouldLeave() {
        return player.shouldLeave();
    }

    @Override
    public void newGame() {
        field.newGame(player);
    }

    @Override
    public void loadSave(JSONObject save) {
        if (save.has("levelProgress")) {
            save.remove("levelProgress");
        }
        field.loadSave(save);
    }

    @Override
    public Object getBoardAsString() {
        if (printer == null) {
            throw new IllegalStateException("No board for this player");
        }

        Object data = printer.print();

        return multiplayerType.postProcessBoard(data, this);
    }

    @Override
    public void close() {
        if (field != null) {
            field.remove(player);
        }
        on(null);
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
    public JSONObject getSave() {
        JSONObject save = (field == null) ? null : field.getSave();
        return multiplayerType.postProcessSave(save, this);
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
