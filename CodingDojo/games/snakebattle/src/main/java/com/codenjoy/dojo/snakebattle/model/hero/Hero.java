package com.codenjoy.dojo.snakebattle.model.hero;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.round.RoundPlayerHero;
import com.codenjoy.dojo.snakebattle.model.Player;
import com.codenjoy.dojo.snakebattle.model.board.Field;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.snakebattle.model.DirectionUtils.getPointAt;
import static com.codenjoy.dojo.snakebattle.services.GameSettings.Keys.*;
import static java.util.stream.Collectors.toList;

public class Hero extends RoundPlayerHero<Field> implements State<LinkedList<Tail>, Player> {

    private static final int MINIMUM_LENGTH = 2;

    public static final boolean NOW = true;
    public static final boolean NEXT_TICK = !NOW;

    private LinkedList<Tail> elements;
    private Direction direction;
    private Direction newDirection;
    private int growBy;
    private int stonesCount;
    private int flyingCount;
    private int furyCount;
    private boolean leaveApples;
    private Point lastTailPosition;

    public Hero(Point xy) {
        this(RIGHT);
        elements.add(new Tail(xy.getX() - 1, xy.getY(), this));
        elements.add(new Tail(xy, this));
    }

    public Hero(Direction direction) {
        elements = new LinkedList<>();
        growBy = 0;
        leaveApples = false;
        this.direction = direction;
        newDirection = null;
        stonesCount = 0;
        flyingCount = 0;
        furyCount = 0;
    }

    public List<Tail> body() {
        return elements;
    }

    public List<Tail> reversedBody() {
        return new LinkedList<>(elements){{
            Collections.reverse(this);
        }};
    }

    public Point getTailPoint() {
        return elements.getFirst();
    }

    @Override
    public int getX() {
        return head().getX();
    }

    @Override
    public int getY() {
        return head().getY();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof Hero)) {
            throw new IllegalArgumentException("Must be Hero!");
        }

        return o == this;
    }

    public int size() {
        return elements == null ? 0 : elements.size();
    }

    public Point head() {
        if (elements.isEmpty())
            return pt(-1, -1);
        return elements.getLast();
    }

    public Point neck() {
        if (elements.size() <= 1) {
            return pt(-1, -1);
        }
        int last = elements.size() - 1;
        return elements.get(last - 1);
    }

    @Override
    public void down() {
        setNewDirection(DOWN);
    }

    @Override
    public void up() {
        setNewDirection(UP);
    }

    @Override
    public void left() {
        setNewDirection(LEFT);
    }

    @Override
    public void right() {
        setNewDirection(RIGHT);
    }

    private void setNewDirection(Direction d) {
        if (!isActiveAndAlive()) {
            return;
        }
        newDirection = d;
    }

    @Override
    public void act(int... p) {
        // TODO только если змейка жива, если она в загоне нельзя давать эту команду выполнять
        if (p.length == 1 && p[0] == 0) {
            die();
            leaveApples = true;
            return;
        }

        if (!isActiveAndAlive()) {
            return;
        }
        if (stonesCount > 0) {
            Point to = getTailPoint();
            if (field.setStone(to)) {
                stonesCount--;
            }
        }
    }

    Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!isActiveAndAlive()) {
            return;
        }

        applyNewDirection();

        reduceIfShould();
        count();

        Point next = getNextPoint();
        if (isMe(next) && !isFlying())
            selfReduce(next);

        go(next);
    }

    private void applyNewDirection() {
        if (newDirection != null && !newDirection.equals(direction.inverted())) {
            direction = newDirection;
            newDirection = null;
        }
    }

    // Этот метод должен вызываться отдельно от tick,
    // уже после обработки столкновений с другими змейками
    public void eat() {
        if (!isActiveAndAlive()) {
            return;
        }

        Point head = head();
        if (field.isApple(head)) {
            growBy(1);
            // если не сделать этого здесь, при съедании яблока и одновременной потере части корпуса
            // яблоко будет зачтено лишь на следующий тик, что неправильно
            grow();
        }
        if (field.isStone(head) && !isFlying()) {
            stonesCount++;
            if (!isFury()) {
                reduce(settings().integer(STONE_REDUCED), NOW);
            }
        }
        if (field.isFlyingPill(head)) {
            eatFlying();
        }
        if (field.isFuryPill(head)) {
            eatFury();
        }
        if (field.isBarrier(head)) {
            die();
        }
    }

    public void eatFlying() {
        flyingCount += settings().integer(FLYING_COUNT);
    }

    public void eatFury() {
        furyCount += settings().integer(FURY_COUNT);
    }

    public void count() {
        if (isFlying()) {
            flyingCount--;
        }
        if (isFury()) {
            furyCount--;
        }
    }

    private void reduceIfShould() {
        if (growBy < 0) {
            if (growBy < -elements.size()) {
                die();
            } else {
                elements = new LinkedList<>(elements.subList(-growBy, elements.size()));
                // TODO тут тоже надо по идее lastTailPosition = getTailPoint();
            }
            growBy = 0;
        }
    }

    private void selfReduce(Point from) {
        if (from.equals(getTailPoint()))
            return;
        elements = new LinkedList<>(elements.subList(elements.indexOf(from), elements.size()));
        // TODO тут тоже надо по идее lastTailPosition = getTailPoint();
    }

    public int reduceFrom(Point from) {
        int was = size();
        lastTailPosition = from;
        elements = new LinkedList<>(elements.subList(elements.indexOf(from) + 1, elements.size()));
        if (size() < MINIMUM_LENGTH) {
            die();
            return was; // TODO я не нашел случая когда это может случиться
        } else {
            return  was - size();
        }
    }

    public int reduce(int len, boolean now) {
        int was = size();
        if (was < len + MINIMUM_LENGTH) {
            die();
            return was;
        } else {
            if (now) {
                elements = new LinkedList<>(elements.subList(len, elements.size()));
                // TODO тут тоже надо по идее lastTailPosition = getTailPoint();
            } else {
                growBy = -len;
            }
            return len;
        }
    }

    public Point getNextPoint() {
        return getPointAt(head(), direction);
    }

    private void grow() {
        growBy--;
        elements.addFirst(new Tail(lastTailPosition, this));
    }

    private void go(Point newLocation) {
        lastTailPosition = getTailPoint();
        elements.add(new Tail(newLocation, this));
        elements.removeFirst();
    }

    public boolean isHeadIntersect(Hero enemy) {
        return enemy.head().equals(head()) ||
                enemy.neck().equals(head()) ||
                neck().equals(enemy.head());
    }

    @Override
    public LinkedList<Tail> state(Player player, Object... alsoAtPoint) {
        return elements;
    }

    BodyDirection getBodyDirection(Tail curr) {
        int currIndex = getBodyIndex(curr);
        Point prev = elements.get(currIndex - 1);
        Point next = elements.get(currIndex + 1);

        BodyDirection nextPrev = orientation(next, prev);
        if (nextPrev != null) {
            return nextPrev;
        }

        if (orientation(prev, curr) == BodyDirection.HORIZONTAL) {
            boolean clockwise = curr.getY() < next.getY() ^ curr.getX() > prev.getX();
            if (curr.getY() < next.getY()) {
                return (clockwise) ? BodyDirection.TURNED_RIGHT_UP : BodyDirection.TURNED_LEFT_UP;
            } else {
                return (clockwise) ? BodyDirection.TURNED_LEFT_DOWN : BodyDirection.TURNED_RIGHT_DOWN;
            }
        } else {
            boolean clockwise = curr.getX() < next.getX() ^ curr.getY() < prev.getY();
            if (curr.getX() < next.getX()) {
                return (clockwise) ? BodyDirection.TURNED_RIGHT_DOWN : BodyDirection.TURNED_RIGHT_UP;
            } else {
                return (clockwise) ? BodyDirection.TURNED_LEFT_UP : BodyDirection.TURNED_LEFT_DOWN;
            }
        }
    }

    private BodyDirection orientation(Point curr, Point next) {
        if (curr.getX() == next.getX()) {
            return BodyDirection.VERTICAL;
        } else if (curr.getY() == next.getY()) {
            return BodyDirection.HORIZONTAL;
        } else {
            return null;
        }
    }

    TailDirection getTailDirection() {
        Point body = elements.get(1);
        Point tail = getTailPoint();

        if (body.getX() == tail.getX()) {
            return (body.getY() < tail.getY()) ? TailDirection.VERTICAL_UP : TailDirection.VERTICAL_DOWN;
        } else {
            return (body.getX() < tail.getX()) ? TailDirection.HORIZONTAL_RIGHT : TailDirection.HORIZONTAL_LEFT;
        }
    }

    boolean itsMyHead(Point point) {
        return head() == point;
    }

    boolean isMe(Point next) {
        return elements.contains(next);
    }

    boolean itsMyTail(Point point) {
        return getTailPoint() == point;
    }

    public void growBy(int val) {
        growBy += val;
    }

    public void clear() {
        List<Point> points = new LinkedList<>(elements);
        elements = new LinkedList<>();
        if (leaveApples) {
            points.forEach(e -> field.setApple(e));
            leaveApples = false;
        }
        growBy = 0;
    }

    public int getStonesCount() {
        return stonesCount;
    }

    public int getFlyingCount() {
        return flyingCount;
    }

    public int getFuryCount() {
        return furyCount;
    }

    public void removeFury() {
        furyCount = 0;
    }

    public boolean isFlying() {
        return flyingCount > 0;
    }

    public void removeFlying() {
        flyingCount = 0;
    }

    public boolean isFury() {
        return furyCount > 0;
    }

    public void addTail(Point part) {
        elements.addFirst(new Tail(part, this));
    }

    public void addTail(List<Point> tail) {
        elements.addAll(tail.stream()
                .map(pt -> new Tail(pt, this))
                .collect(toList()));
    }

    public int getBodyIndex(Point pt) {
        // возможны наложения элементов по pt, а потому надо вначале искать по ==
        for (int index = 0; index < elements.size(); index++) {
            if (elements.get(index) == pt) {
                return index;
            }
        }
        return elements.indexOf(pt);
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", head().getX(), head().getY());
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public int scores() {
        return size();
    }
}
