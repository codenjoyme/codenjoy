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

import com.codenjoy.dojo.pong.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class Pong implements Field {

    public static final int BOUND_DISTANCE = 1;
    private List<Player> players;
    private int leftBound;
    private int rightBound;
    private final int size;
    private Dice dice;
    private Ball ball;
    private List<Wall> walls;

    public Pong(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        leftBound = 0 + BOUND_DISTANCE;
        rightBound = size - 1 - BOUND_DISTANCE;
        ball = level.getBall();
        ball.init(this);
        walls = level.getWalls();
        players = new LinkedList<>();
    }

    private List<Panel> getPanels() {
        return new LinkedList<Panel>(){{
            getHeroes().forEach(hero -> addAll(hero.getPanel()));
        }};
    }

    @Override
    public void tick() {
        getHeroes().forEach(Hero::tick);

        if (ballOut()) {
            resetBall();
            return;
        }

        for (Player player : players) {
            if (playerPassedBall(player)) {
                player.event(Events.LOOSE);
                allExcept(player).forEach(p -> p.event(Events.WIN));
                resetBall();
                return;
            }
        }

        ball.tick();
    }

    private List<Player> allExcept(Player player) {
        return new LinkedList<Player>(players) {{
            remove(player);
        }};
    }

    private boolean ballOut() {
        return ball.getX() < leftBound ||
                ball.getX() > rightBound;
    }

    private boolean playerPassedBall(Player player) {
        boolean heroAtLeft =
                Math.abs(player.getHero().getX() - leftBound) <
                Math.abs(player.getHero().getX() - rightBound);
        return  heroAtLeft && ball.getX() <= leftBound
                || !heroAtLeft && ball.getX() >= rightBound;
    }

    public int size() {
        return size;
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            player.newHero(this);
        }

        if (players.size() > 1) {
            resetBall();
        }
    }

    private void resetBall() {
        ball = new Ball(getBoardCenter());
        ball.init(this);
        ball.setDirection(BallDirection.getRandom(dice));
    }

    @Override
    public Point getNewHeroPosition() {
        Point center = getBoardCenter();
        if (players.size() > 0 && players.get(0).getHero() != null) {
            if (players.get(0).getHero().getX() == rightBound) {
                return pt(leftBound, center.getY());
            } else {
                return pt(rightBound, center.getY());
            }
        }
        return pt(rightBound, center.getY());
    }

    private Point getBoardCenter() {
        return pt(size/2, size/2);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Pong.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(getPanels());
                    add(ball);
                    addAll(walls);
                }};
            }
        };
    }

    public void setBallDirection(BallDirection direction) {
        ball.setDirection(direction);
    }


    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public boolean isBarrier(Point pt) {
        return walls.contains(pt) || getPanels().contains(pt);
    }

    @Override
    public Barrier getBarrier(Point pt) {
        return getBarriers().stream()
                .filter(Predicate.isEqual(pt))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Here is no barriers on this point :("));
    }

    private List<Barrier> getBarriers() {
        return new LinkedList<Barrier>(){{
            addAll(walls);
            addAll(getPanels());
        }};
    }

}
















