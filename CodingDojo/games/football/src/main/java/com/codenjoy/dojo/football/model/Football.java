package com.codenjoy.dojo.football.model;

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

import com.codenjoy.dojo.football.model.elements.Ball;
import com.codenjoy.dojo.football.model.elements.Goal;
import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.model.elements.Wall;
import com.codenjoy.dojo.football.services.Events;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.BoardUtils.NO_SPACE;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class Football implements Field {

    private List<Wall> walls;
    private List<Goal> topGoals;
    private List<Goal> bottomGoals;
    private List<Player> players;

    private final int size;
    private Dice dice;

    private List<Ball> balls;

    public Football(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        size = level.getSize();
        players = new LinkedList<>();
        balls = level.getBalls();
        topGoals = level.getTopGoals();
        bottomGoals = level.getBottomGoals();

        for (Ball ball : balls) {
            ball.init(this);
        }
    }

    @Override
    public void tick() {
        for (Ball ball : balls) {
            for (Goal goal : topGoals) {
                if (goal.itsMe(ball)) {
                    for (Player player : players) {
                        player.event(Events.TOP_GOAL);
                    }
                    return;
                }
            }

            for (Goal goal : bottomGoals) {
                if (goal.itsMe(ball)) {
                    for (Player player : players) {
                        player.event(Events.BOTTOM_GOAL);
                    }
                    return;
                }
            }
        }

        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
        }

        for (Ball ball : balls) {
            ball.tick();
            for (Player player : players) {
                Hero hero = player.getHero();
                if (hero.itsMe(ball)) {
                    hero.setBall(ball);
                } else {
                    hero.setBall(null);
                }
            }

            for (Goal goal : topGoals) {
                if (goal.itsMe(ball)) {
                    goal.setBall(ball);
                }
            }

            for (Goal goal : bottomGoals) {
                if (goal.itsMe(ball)) {
                    goal.setBall(ball);
                }
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getHeroes().contains(pt);
    }

    @Override
    public Point getFreeRandomOnMyHalf(Player player) {
        Point result = BoardUtils.getFreeRandom(size, dice,
                pt -> isFreeAndOnMyHalf(pt, player));

        if (!result.equals(NO_SPACE)) {
            return result;
        }

        return BoardUtils.getFreeRandom(size, dice,
                pt -> isFree(pt));
    }

    private boolean isFreeAndOnMyHalf(Point pt, Player player) {
        return isOnMyHalf(pt, player) &&
                !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    private boolean isOnMyHalf(Point pt, Player player) {
        if (player.getMyGoal() == Elements.TOP_GOAL) {
            return pt.getY() > (size / 2);
        } else {
            return pt.getY() < (size / 2);
        }
    }

    @Override
    public boolean isFree(Point pt) {
        return !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<>(players.size());
        for (Player player : players) {
            result.add(player.getHero());
        }
        return result;
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            if (players.size() % 2 == 1) {
                player.setTeam("Team 1");
                player.setMyGoal(Elements.BOTTOM_GOAL);
            } else {
                player.setTeam("Team 2");
                player.setMyGoal(Elements.TOP_GOAL);
            }
        }
        player.newHero(this);

        for (Ball ball : balls) {

            ball.setImpulse(0);
            for (Goal goal : topGoals) {
                if (goal.itsMe(ball)) {
                    goal.setBall(null);
                    ball.move(size / 2, size / 2);
                }
            }
            for (Goal goal : bottomGoals) {
                if (goal.itsMe(ball)) {
                    goal.setBall(null);
                    ball.move(size / 2, size / 2);
                }
            }
            for (Goal goal : bottomGoals) {
                if (goal.itsMe(ball)) {
                    goal.setBall(null);
                    ball.move(size / 2, size / 2);
                }
            }
        }
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    public List<Wall> getWalls() {
        return walls;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Football.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(Football.this.getWalls());
                    addAll(Football.this.getHeroes());
                    addAll(Football.this.topGoals);
                    addAll(Football.this.bottomGoals);
                    addAll(Football.this.balls);
                }};
            }
        };
    }

    @Override
    public boolean isBall(int x, int y) {
        Point pt = pt(x, y);
        return balls.contains(pt);
    }

    @Override
    public Ball getBall(int x, int y) {
        Point pt = pt(x, y);
        for (Ball ball : balls) {
            if (ball.itsMe(pt)) {
                return ball;
            }
        }
        return null;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    @Override
    public boolean isHero(int x, int y) {
        Point pt = pt(x, y);
        return getHeroes().contains(pt);
    }

    @Override
    public boolean isWall(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || getWalls().contains(pt);
    }
}
