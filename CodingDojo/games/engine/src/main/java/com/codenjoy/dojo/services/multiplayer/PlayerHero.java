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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.joystick.NoMessageJoystick;
import org.json.JSONObject;

/**
 * Герой на поле, которым может управлять игрок должен быть представлен наследником этого класса.
 * Благодаря {@see PointImpl} игрок имеет свою координату и может двигаться.
 * Благодаря {@see Joystick} игрок может управлять героем.
 * Благодаря {@see Tickable} часть логики GameField.tick() которая по SRP больше к герою относится может быть перенесена сюда.
 * @param <F> Поле {@see GameField} по которому "бегает" герой.
 *           Герой пользуется этим полем, чтобы уточнить что-то про игру во время своих телодвижений.
 *           Связь циклическая (герой знает про поле, а поле про героя) но так и задумано
 */
public abstract class PlayerHero<F extends GameField> extends PointImpl implements Joystick, NoMessageJoystick, Tickable {

    protected F field;

    public PlayerHero(int x, int y) {
        super(x, y);
    }

    // TODO используется для безкординатных героев, подумать над этим
    public PlayerHero() {
        super(-1, -1);
    }

    public PlayerHero(Point point) {
        super(point);
    }

    public PlayerHero(JSONObject json) {
        super(json);
    }

    public void init(F field) {
        this.field = field;
    }

    public F field() {
        return field;
    }
}
