package com.codenjoy.dojo.sudoku.model;

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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.hero.HeroDataImpl;
import com.codenjoy.dojo.services.joystick.ActJoystick;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.sudoku.services.GameSettings;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Player extends GamePlayer<PlayerHero, Field> {

    private Field field;

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    public static final int SIZE = 9;

    private boolean valid(int i) {
        return i >= 1 && i <= SIZE;
    }

    @Override
    public HeroData getHeroData() {
        return new HeroDataImpl(field.level(),
                MultiplayerType.SINGLE.isSingle());
    }


    public static int fix(int x) {
        return x + Math.abs((x - 1) / 3);
    }

    @Override
    public Joystick getJoystick() {
        return (ActJoystick) p -> {
            if (field.isGameOver()) return;

            if (p.length == 1 && p[0] == 0) {
                field.gameOver();
                return;
            }

            if (p.length != 3) {
                return;
            }

            if (!valid(p[0])) return;
            if (!valid(p[1])) return;
            // командой 0 мы очищаем поле, если поставили туда не то значение
            if (p[2] != 0 && !valid(p[2])) return;

            int x = fix(p[0]);
            int y = fix(SIZE + 1 - p[1]);
            Point pt = pt(x, y);

            field.set(pt, p[2]);
        };
    }

    @Override
    public PlayerHero getHero() {
        return null;
    }

    @Override
    public void newHero(Field field) {
        this.field = field;
    }

    @Override
    public boolean isAlive() {
        return !field.isGameOver();
    }

    @Override
    public boolean isWin() {
        return field.isWin();
    }
}
