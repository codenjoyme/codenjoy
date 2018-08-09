package com.codenjoy.dojo.football.model.elements;

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

import com.codenjoy.dojo.football.model.Elements;
import com.codenjoy.dojo.football.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт Ворота на поле
 */
public class Goal extends PointImpl implements State<Elements, Player> {

    private Elements type;
    private Ball ball;
    
    public Goal(int x, int y, Elements type) {
        super(x, y);
        this.type = type;
    }

    public Goal(Point point, Elements type) {
        this(point.getX(), point.getY(), type);
    }
    
    public boolean isWithBall() {
        if (ball == null) {
            return false;
        } else {
            return true;
        }
    }
    
    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        
        if (player.getMyGoal() == type) {
            if (isWithBall()) {
                return Elements.HITED_MY_GOAL;
            } else {
                return Elements.MY_GOAL;
            }
        } else if (player.getMyGoal() != type) {
            if (isWithBall()) {
                return Elements.HITED_GOAL;
            } else {
                return Elements.ENEMY_GOAL;
            }
        } else {
            if (isWithBall()) {
                return Elements.HITED_GOAL;
            } else {
                return type;
            }
        }
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }
    
}
