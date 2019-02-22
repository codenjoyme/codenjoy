package com.codenjoy.dojo.snake.client.ai;

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
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.client.Board;
import com.codenjoy.dojo.snake.model.Elements;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

public class AISolverTest {

    // этот тест проверяет что если спереди яблочко по направлению движения, то змейка не свернет
    @Test
    public void shouldGoByInertion() {
        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼☺ ◄═☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(LEFT, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼═► ☺☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(RIGHT, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ║ ☼"+
                "☼  ▼ ☼"+
                "☼    ☼"+
                "☼  ☺ ☼"+
                "☼☼☼☼☼☼", path(DOWN, DOWN));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ☺ ☼"+
                "☼    ☼"+
                "☼  ▲ ☼"+
                "☼  ║ ☼"+
                "☼☼☼☼☼☼", path(UP, UP));
    }

    private void assertWay(String boardString, Path path) {
        System.out.println("next assert --------------------------------");
        Board board = (Board) new Board().forString(boardString);
        Point from = board.getHead();
        Point to = board.getApples().get(0);
        Direction direction = board.getSnakeDirection();
        List<Point> stones = board.getStones();
        List<Point> snake = board.getSnake();
        List<Point> walls = board.getWalls();

        assertWay(board, path, from, to, direction, snake, merge(stones, walls));
    }


    // тест проверяет, что где-то в поле зрения змейки по прямой линии есть яблочко, но нужно повернуться
    @Test
    public void shouldOneRotate() {
        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼   ║☼"+
                "☼ ☺ ▼☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(LEFT, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼ ☺ ▲☼"+
                "☼   ║☼"+
                "☼☼☼☼☼☼", path(LEFT, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼║   ☼"+
                "☼▼ ☺ ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(RIGHT, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼▲ ☺ ☼"+
                "☼║   ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(RIGHT, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ☺  ☼"+
                "☼    ☼"+
                "☼═►  ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(UP, UP));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ☺ ☼"+
                "☼    ☼"+
                "☼  ◄═☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(UP, UP));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼═►  ☼"+
                "☼    ☼"+
                "☼ ☺  ☼"+
                "☼☼☼☼☼☼", path(DOWN, DOWN));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ◄═☼"+
                "☼    ☼"+
                "☼  ☺ ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(DOWN, DOWN));
    }

    // тест проверяет, что яблочко есть где-то по диагонали, так что надо повернуть,
    // потом доехать до того места, где оно будет
    // в области прямой видимости и потом развернуться еще раз
    @Test
    public void shouldTwoRotate() {
        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ☺  ☼"+
                "☼   ║☼"+
                "☼   ▼☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(LEFT, UP, UP, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ☺ ☼"+
                "☼║   ☼"+
                "☼▼   ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(RIGHT, UP, UP, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ☺  ☼"+
                "☼ ║  ☼"+
                "☼ ▼  ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(LEFT, UP, UP, RIGHT));
        //----------------

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼   ▲☼"+
                "☼   ║☼"+
                "☼ ☺  ☼"+
                "☼☼☼☼☼☼", path(LEFT, DOWN, DOWN, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼▲   ☼"+
                "☼║   ☼"+
                "☼  ☺ ☼"+
                "☼☼☼☼☼☼", path(RIGHT, DOWN, DOWN, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼ ▲  ☼"+
                "☼ ║  ☼"+
                "☼ ☺  ☼"+
                "☼☼☼☼☼☼", path(LEFT, DOWN, DOWN, RIGHT));
        // ----------------------

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼☺═► ☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(UP, LEFT, DOWN, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼☺   ☼"+
                "☼    ☼"+
                "☼ ═► ☼"+
                "☼☼☼☼☼☼", path(UP, UP, LEFT, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ═► ☼"+
                "☼    ☼"+
                "☼☺   ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(DOWN, DOWN, LEFT, LEFT));
        // ----------------------

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼ ◄═☺☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(UP, RIGHT, DOWN, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼   ☺☼"+
                "☼    ☼"+
                "☼ ◄═ ☼"+
                "☼☼☼☼☼☼", path(UP, UP, RIGHT, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ◄═ ☼"+
                "☼    ☼"+
                "☼   ☺☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(DOWN, DOWN, RIGHT, RIGHT));
        // ----------------------
    }

    @Test
    public void test2() {
        assertWay(
            "☼☼☼☼☼☼"+
            "☼    ☼"+
            "☼ ◄═ ☼"+
            "☼  ☻ ☼"+
            "☼  ☺ ☼"+
            "☼☼☼☼☼☼", path(DOWN, DOWN, RIGHT));
    }

    // Тест проверяет, что если на пути следования змейки по инерции встречается камень, она его обойдет.
    @Test
    public void shouldGetRoundIfBarrier() {
        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼☺☻◄═☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(UP, LEFT, LEFT, DOWN));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼═►☻☺☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(UP, RIGHT, RIGHT, DOWN));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ║ ☼"+
                "☼  ▼ ☼"+
                "☼  ☻ ☼"+
                "☼  ☺ ☼"+
                "☼☼☼☼☼☼", path(LEFT, DOWN, DOWN, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ☺ ☼"+
                "☼  ☻ ☼"+
                "☼  ▲ ☼"+
                "☼  ║ ☼"+
                "☼☼☼☼☼☼", path(LEFT, UP, UP, RIGHT));
    }

    // Тест так же выбирает более оптимальный путь приусловии как в прошлом тесте shouldGetRoundIfBarrier,
    // но так же выберет другую сторону для поворота, если препятствий больше
    @Test
    public void shouldGetRoundIfTwoBarriers() {
        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼  ☻ ☼"+
                "☼☺☻◄═☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(DOWN, LEFT, LEFT, UP));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼═►☻☺☼"+
                "☼ ☻  ☼"+
                "☼☼☼☼☼☼", path(UP, RIGHT, RIGHT, DOWN));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ║ ☼"+
                "☼ ☻▼ ☼"+
                "☼  ☻ ☼"+
                "☼  ☺ ☼"+
                "☼☼☼☼☼☼", path(RIGHT, DOWN, DOWN, LEFT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼  ☺ ☼"+
                "☼  ☻ ☼"+
                "☼  ▲☻☼"+
                "☼  ║ ☼"+
                "☼☼☼☼☼☼", path(LEFT, UP, UP, RIGHT));
    }

    // тест проверяет, что при условии как в тесте shouldTwoRotate если на пути
    // встретится камень после первого поворота, то змейка его обойдет
    @Test
    public void shouldGetRoundStone() {
        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ☻  ☼"+
                "☼ ◄═☺☼"+
                "☼    ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(DOWN, RIGHT, UP, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼    ☼"+
                "☼ ☻ ☺☼"+
                "☼    ☼"+
                "☼ ◄═ ☼"+
                "☼☼☼☼☼☼", path(UP, RIGHT, UP, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼   ☺☼"+
                "☼ ☻  ☼"+
                "☼ ◄═ ☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(DOWN, RIGHT, UP, UP, UP, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ◄═ ☼"+
                "☼    ☼"+
                "☼ ☻ ☺☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(DOWN, RIGHT, DOWN, RIGHT));

        assertWay(
                "☼☼☼☼☼☼"+
                "☼ ◄═ ☼"+
                "☼ ☻  ☼"+
                "☼   ☺☼"+
                "☼    ☼"+
                "☼☼☼☼☼☼", path(LEFT, DOWN, DOWN, RIGHT, RIGHT, RIGHT));
        // ----------------------

    }

    @Test
    public void shouldNotEatBoard() {
        assertWay(
                "☼☼☼☼☼☼" +
                "☼  ☺ ☼" +
                "☼    ☼" +
                "☼║   ☼" +
                "☼▼ ☻ ☼" +
                "☼☼☼☼☼☼", path(RIGHT, UP, UP, UP, RIGHT));
    }

//    @Test
//    public void shouldNotEatMyself() {
//        assertWay(
//                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
//                "☼               ☼\n" +
//                "☼               ☼\n" +
//                "☼               ☼\n" +
//                "☼               ☼\n" +
//                "☼               ☼\n" +
//                "☼      ☺        ☼\n" +
//                "☼   ╘══════╗    ☼\n" +
//                "☼      ▲   ║    ☼\n" +
//                "☼      ║   ║    ☼\n" +
//                "☼      ║   ║    ☼\n" +
//                "☼      ║   ║    ☼\n" +
//                "☼      ║   ║    ☼\n" +
//                "☼      ║   ║    ☼\n" +
//                "☼      ╚═══╝  ☻ ☼\n" +
//                "☼               ☼\n" +
//                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", path(RIGHT, UP, UP, UP, RIGHT));        // TODO
//    }

    Path path(Direction... directions) {
        return new Path(directions);
    }

    class Path implements Iterable<Direction> {
        private List<Direction> directions;

        public Path(Direction ... directions) {
            this.directions = Arrays.asList(directions);
        }

        @Override
        public Iterator<Direction> iterator() {
            return directions.iterator();
        }

        @Override
        public String toString() {
            return directions.toString();
        }
    }

    private void assertWay(Board board, Path expectedPath, Point from, Point to, Direction direction, List<Point> snake, List<Point> barriers) {
        List<Point> allBarriers = board.getBarriers();
        List<Direction> actualPath = new LinkedList<Direction>();
        System.out.println("Now Snake at: " + snake.toString());
        do {
            Direction actual = Direction.valueOf(new AISolver(null).get(board));
            actualPath.add(actual);

            from = actual.change(from);

            // в ходе пережвижения тело за собой надо тащить если оно есть
            for (Point tail : snake) {
                board.set(tail.getX(), tail.getY(), Elements.NONE.ch());
            }
            board.set(from.getX(), from.getY(), Elements.HEAD_UP.ch());
            board.set(snake.get(0).getX(), snake.get(0).getY(), Elements.TAIL_VERTICAL.ch());
            snake = board.getSnake();

            allBarriers = merge(snake, barriers);
            System.out.println("Move: " + direction);
            System.out.println("Now Snake at: " + snake.toString());

            direction = actual;
        } while (!from.equals(to) && !from.isOutOf(20));

        assertEquals(expectedPath.toString(), actualPath.toString());
    }

    private LinkedList<Point> merge(List<Point> snake, List<Point> barriers) {
        LinkedList<Point> allBarriers = new LinkedList<Point>();
        allBarriers.addAll(barriers);
        allBarriers.addAll(snake);
        return allBarriers;
    }

    private Point to(int x, int y) {
        return pt(x, y);
    }

    private Point from(int x, int y) {
        return to(x, y);
    }
}
