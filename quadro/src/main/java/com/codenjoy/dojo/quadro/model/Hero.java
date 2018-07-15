package com.codenjoy.dojo.quadro.model;

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


<<<<<<< HEAD
import com.codenjoy.dojo.services.*;
=======
>>>>>>> e88ddd11bf2be859b6168599581a05035b8248e8
import com.codenjoy.dojo.services.joystick.ActJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 * Ну и конечно же он имплементит {@see State}, а значит может быть отрисован на поле.
 * Часть этих интерфейсов объявлены в {@see PlayerHero}, а часть явно тут.
 */
public class Hero extends PlayerHero<Field> implements ActJoystick {

<<<<<<< HEAD
    private boolean color;
=======
    private boolean color = true;

//    public Hero(Point xy) {
//        super(xy);
//    }
>>>>>>> e88ddd11bf2be859b6168599581a05035b8248e8

    @Override
    public void init(Field field) {
        this.field = field;
    }

<<<<<<< HEAD

=======
>>>>>>> e88ddd11bf2be859b6168599581a05035b8248e8
    @Override
    public void act(int... p) {
        this.x = p[0];
    }

    @Override
    public void tick() {
<<<<<<< HEAD
        field.setChip(color, x, y);
=======
        field.setChip(color, x);
>>>>>>> e88ddd11bf2be859b6168599581a05035b8248e8
    }

    public boolean isAlive() {
        return true;
    }
<<<<<<< HEAD

=======
>>>>>>> e88ddd11bf2be859b6168599581a05035b8248e8
}
