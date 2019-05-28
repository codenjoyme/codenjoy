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

import com.codenjoy.dojo.football.model.Actions;
import com.codenjoy.dojo.football.model.Elements;
import com.codenjoy.dojo.football.model.Field;
import com.codenjoy.dojo.football.model.Player;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import org.apache.commons.lang3.StringUtils;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private Ball ball;
    private Direction direction;
    private String team;

    public Hero(Point xy) {
        super(xy);
        direction = null;
    }

    public void init(Field field) {
        super.init(field);
        this.ball = field.getBall(x, y);
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        direction = Direction.UP;
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        int action = 0;
        int param1 = 0;
        //int param2 = 0;
        if(p.length > 0) {
            action = p[0];
        }
        if(p.length > 1) {
            param1 = p[1];
        }
        /*if(p.length > 1) {
            param2 = p[2];
        }*/
        //for(int action : p) {
            
            if(action == Actions.HIT_RIGHT.getValue()) {
                if (ball != null) {
                    //ball.setImpulse(COUNTER);
                    if(param1 == 0) param1 = 1;
                    ball.right(param1);
                }
            } else if(action == Actions.HIT_UP.getValue()) {
                if (ball != null) {
                    //ball.setImpulse(COUNTER);
                    if(param1 == 0) param1 = 1;
                    ball.up(param1);
                }
            } else if(action == Actions.HIT_LEFT.getValue()) {
                if (ball != null) {
                    //ball.setImpulse(COUNTER);
                    if(param1 == 0) param1 = 1;
                    ball.left(param1);
                }
            } else if(action == Actions.HIT_DOWN.getValue()) {
                if (ball != null) {
                    if(param1 == 0) param1 = 1;
                    ball.down(param1);
                }
            } else if(action == Actions.STOP_BALL.getValue()) {
                if (ball != null) {
                    ball.stop();
                }
            }
    }

    @Override
    public void tick() {
        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
            
        }
        direction = null;
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
        Hero playersHero = player.getHero();
        if (playersHero == this){
            if (isWithBall()) {
                return Elements.HERO_W_BALL;
            } else {
                return Elements.HERO;
            }
        } else if (StringUtils.equals(playersHero.getTeam(), team)){
            if (!isWithBall()) {
                return Elements.TEAM_MEMBER;
            } else {
                return Elements.TEAM_MEMBER_W_BALL;
            }
        } else {//if (playersHero.getTeam() != team){
            if (!isWithBall()) {
                return Elements.ENEMY;
            } else {
                return Elements.ENEMY_W_BALL;
            }
        }
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

}
