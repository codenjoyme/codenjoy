package com.codenjoy.dojo.pong.model;

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

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

public class Ball extends PointImpl implements Tickable, State<Elements, Player> {

    private Field field;
    private BallDirection direction;
    private int speed = 1;

    public Ball(Point pt) {
        super(pt);
    }

    public Ball(int x, int y) {
        super(x, y);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BALL;
    }

    @Override
    public void tick() {
        // навсякий запомним откуда прилетели (как улетать назад)
        BallDirection inverted = direction.invert();
        Point to;
        for (int index = 0; index < speed; index++) {
            // летим в направлении инерции
            to = direction.change(this);
            // если на пути стоит барьер
            if (field.isBarrier(to)) {
                // мы должны сменить направлени отразившись от него
                direction = direction.reflectedFrom(field.getBarrier(to));
                // и полететь в отраженном направлении
                to = direction.change(this);
                // но если и там барьер
                if (field.isBarrier(to)) {
                    // мы должны вернуться откуда прилетели
                    direction = inverted;
                    to = direction.change(this);
                }
            }
            this.move(to);
        }
    }

    public void setDirection(BallDirection direction) {
        this.direction = direction;
    }

    public void init(Field field) {
        this.field = field;
    }

    public BallDirection getDirection() {
        return direction;
    }
}
