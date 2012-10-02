package com;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.Direction.*;
import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:10 AM
 */
public class DirectionTest {

    private static final boolean WITHOUT_BODY = false;
    private static final boolean WITH_BODY = true;

    // этот тест проверяет что если спереди яблочко по направлению движения, то змейка не свернет
    @Test
    public void shouldGoByInertion() {
        assertWay(
                "******"+
                "*    *"+
                "*@ #0*"+
                "*    *"+
                "*    *"+
                "******", path(LEFT, LEFT));

        assertWay(
                "******"+
                "*    *"+
                "*0# @*"+
                "*    *"+
                "*    *"+
                "******", path(RIGHT, RIGHT));

        assertWay(
                "******"+
                "*  0 *"+
                "*  # *"+
                "*    *"+
                "*  @ *"+
                "******", path(DOWN, DOWN));

        assertWay(
                "******"+
                "*  @ *"+
                "*    *"+
                "*  # *"+
                "*  0 *"+
                "******", path(UP, UP));
    }

    private void assertWay(String boardString, Path path) {
        System.out.println("next assert --------------------------------");
        Board board = new Board(boardString);
        Point from = board.getHead();
        Point to = board.getApple();
        String direction = board.getSnakeDirection();
        List<Point> stones = board.getStones();
        List<Point> snake = board.getSnake();

        assertWay(path, from, to, direction, snake, stones);
    }


    // тест проверяет, что где-то в поле зрения змейки по прямой линии есть яблочко, но нужно повернуться
    @Test
    public void shouldOneRotate() {
        assertWay(
                "******"+
                "*    *"+
                "*   0*"+
                "* @ #*"+
                "*    *"+
                "******", path(LEFT, LEFT));

        assertWay(
                "******"+
                "*    *"+
                "*    *"+
                "* @ #*"+
                "*   0*"+
                "******", path(LEFT, LEFT));

        assertWay(
                "******"+
                "*    *"+
                "*0   *"+
                "*# @ *"+
                "*    *"+
                "******", path(RIGHT, RIGHT));

        assertWay(
                "******"+
                "*    *"+
                "*# @ *"+
                "*0   *"+
                "*    *"+
                "******", path(RIGHT, RIGHT));

        assertWay(
                "******"+
                "* @  *"+
                "*    *"+
                "*0#  *"+
                "*    *"+
                "******", path(UP, UP));

        assertWay(
                "******"+
                "*  @ *"+
                "*    *"+
                "*  #0*"+
                "*    *"+
                "******", path(UP, UP));

        assertWay(
                "******"+
                "*    *"+
                "*0#  *"+
                "*    *"+
                "* @  *"+
                "******", path(DOWN, DOWN));

        assertWay(
                "******"+
                "*  #0*"+
                "*    *"+
                "*  @ *"+
                "*    *"+
                "******", path(DOWN, DOWN));
    }

    // тест проверяет, что яблочко есть где-то по диагонали, так что надо повернуть,
    // потом доехать до того места, где оно будет
    // в области прямой видимости и потом развернуться еще раз
    @Test
    public void shouldTwoRotate() {
        assertWay(
                "******"+
                "* @  *"+
                "*   0*"+
                "*   #*"+
                "*    *"+
                "******", path(LEFT, LEFT, UP, UP));

        assertWay(
                "******"+
                "*  @ *"+
                "*0   *"+
                "*#   *"+
                "*    *"+
                "******", path(RIGHT, RIGHT, UP, UP));

        assertWay(
                "******"+
                "* @  *"+
                "* 0  *"+
                "* #  *"+
                "*    *"+
                "******", path(RIGHT, UP, UP, LEFT));
        //----------------

        assertWay(
                "******"+
                "*    *"+
                "*   #*"+
                "*   0*"+
                "* @  *"+
                "******", path(LEFT, LEFT, DOWN, DOWN));

        assertWay(
                "******"+
                "*    *"+
                "*#   *"+
                "*0   *"+
                "*  @ *"+
                "******", path(RIGHT, RIGHT, DOWN, DOWN));

        assertWay(
                "******"+
                "*    *"+
                "* #  *"+
                "* 0  *"+
                "* @  *"+
                "******", path(RIGHT, DOWN, DOWN, LEFT));
        // ----------------------

        assertWay(
                "******"+
                "*    *"+
                "*@0# *"+
                "*    *"+
                "*    *"+
                "******", path(UP, LEFT, LEFT, DOWN));

        assertWay(
                "******"+
                "*    *"+
                "*@   *"+
                "*    *"+
                "* 0# *"+
                "******", path(UP, UP, LEFT, LEFT));

        assertWay(
                "******"+
                "* 0# *"+
                "*    *"+
                "*@   *"+
                "*    *"+
                "******", path(DOWN, DOWN, LEFT, LEFT));
        // ----------------------

        assertWay(
                "******"+
                "*    *"+
                "* #0@*"+
                "*    *"+
                "*    *"+
                "******", path(UP, RIGHT, RIGHT, DOWN));

        assertWay(
                "******"+
                "*    *"+
                "*   @*"+
                "*    *"+
                "* #0 *"+
                "******", path(UP, UP, RIGHT, RIGHT));

        assertWay(
                "******"+
                "* #0 *"+
                "*    *"+
                "*   @*"+
                "*    *"+
                "******", path(DOWN, DOWN, RIGHT, RIGHT));
        // ----------------------
    }

    @Test
    public void test2() {
        assertWay(
            "******"+
            "*    *"+
            "* #0 *"+
            "*  X *"+
            "*  @ *"+
            "******", path(DOWN, DOWN, RIGHT));
    }

    // Тест проверяет, что если на пути следования змейки по инерции встречается камень, она его обойдет.
    @Test
    public void shouldGetRoundIfBarrier() {
        assertWay(
                "******"+
                "*    *"+
                "*    *"+
                "*@X#0*"+
                "*    *"+
                "******", path(UP, LEFT, LEFT, DOWN));

        assertWay(
                "******"+
                "*    *"+
                "*    *"+
                "*0#X@*"+
                "*    *"+
                "******", path(DOWN, RIGHT, RIGHT, UP));

        assertWay(
                "******"+
                "*  0 *"+
                "*  # *"+
                "*  X *"+
                "*  @ *"+
                "******", path(LEFT, DOWN, DOWN, RIGHT));

        assertWay(
                "******"+
                "*  @ *"+
                "*  X *"+
                "*  # *"+
                "*  0 *"+
                "******", path(RIGHT, UP, UP, LEFT));
    }

    // Тест так же выбирает более оптимальный путь приусловии как в прошлом тесте shouldGetRoundIfBarrier,
    // но так же выберет другую сторону для поворота, если препятствий больше
    @Test
    public void shouldGetRoundIfTwoBarriers() {
        assertWay(
                "******"+
                "*    *"+
                "*  X *"+
                "*@X#0*"+
                "*    *"+
                "******", path(DOWN, LEFT, LEFT, UP));

        assertWay(
                "******"+
                "*    *"+
                "*    *"+
                "*0#X@*"+
                "* X  *"+
                "******", path(UP, RIGHT, RIGHT, DOWN));

        assertWay(
                "******"+
                "*  0 *"+
                "* X# *"+
                "*  X *"+
                "*  @ *"+
                "******", path(RIGHT, DOWN, DOWN, LEFT));

        assertWay(
                "******"+
                "*  @ *"+
                "*  X *"+
                "*  #X*"+
                "*  0 *"+
                "******", path(LEFT, UP, UP, RIGHT));
    }

    // тест проверяет, что при условии как в тесте shouldTwoRotate если на пути
    // встретится камень после первого поворота, то змейка его обойдет
    @Test
    public void shouldGetRoundStone() {
        assertWay(
                "******"+
                "* X  *"+
                "* #0@*"+
                "*    *"+
                "*    *"+
                "******", path(DOWN, RIGHT, RIGHT, UP));

        assertWay(
                "******"+
                "*    *"+
                "* X @*"+
                "*    *"+
                "* #0 *"+
                "******", path(UP, RIGHT, RIGHT, UP));

        assertWay(
                "******"+
                "*   @*"+
                "* X  *"+
                "* #0 *"+
                "*    *"+
                "******", path(DOWN, RIGHT, RIGHT, UP, UP, UP));

        assertWay(
                "******"+
                "* #0 *"+
                "*    *"+
                "* X @*"+
                "*    *"+
                "******", path(DOWN, LEFT, DOWN, DOWN, RIGHT, RIGHT, RIGHT, UP));

        assertWay(
                "******"+
                "* #0 *"+
                "* X  *"+
                "*   @*"+
                "*    *"+
                "******", path(LEFT, DOWN, DOWN, RIGHT, RIGHT, RIGHT));
        // ----------------------

    }

    Path path(String... directions) {
        return new Path(directions);
    }

    class Path implements Iterable<String> {
        private List<String> directions;

        public Path(String ... directions) {
            this.directions = Arrays.asList(directions);
        }

        @Override
        public Iterator<String> iterator() {
            return directions.iterator();
        }

        @Override
        public String toString() {
            return directions.toString();
        }
    }

    private void assertWay(Path expectedPath, Point from, Point to, String direction, List<Point> snake, List<Point> barriers) {
        LinkedList<Point> allBarriers = merge(snake, barriers);
        List<String> actualPath = new LinkedList<String>();
        System.out.println("Now Snake at: " + snake.toString());
        do {
            String actual = new Direction(from, to, direction).get(allBarriers);
            actualPath.add(actual);

            if (actual.equals("")) {
                actual = direction;
            }
            from = update(from, actual);

            // в ходе пережвижения тело за собой надо тащить если оно есть
            LinkedList<Point> newSake = new LinkedList<Point>();
            newSake.add(from);
            newSake.add(snake.get(0));
            snake = newSake;

            allBarriers = merge(snake, barriers);
            System.out.println("Move: " + direction);
            System.out.println("Now Snake at: " + snake.toString());

            direction = actual;
        } while (!from.equals(to) && !from.isBad(20));

        assertEquals(expectedPath.toString(), actualPath.toString());
    }

    private LinkedList<Point> merge(List<Point> snake, List<Point> barriers) {
        LinkedList<Point> allBarriers = new LinkedList<Point>();
        allBarriers.addAll(barriers);
        allBarriers.addAll(snake);
        return allBarriers;
    }

    private Point update(Point from, String actual) {
        if (RIGHT.equals(actual)) {
            return new Point(from.x + 1, from.y);
        } else if (LEFT.equals(actual)) {
            return new Point(from.x - 1, from.y);
        } else if (UP.equals(actual)) {
            return new Point(from.x, from.y + 1);
        } else {
            return new Point(from.x, from.y - 1);
        }
    }

    private Point to(int x, int y) {
        return new Point(x, y);
    }

    private Point from(int x, int y) {
        return to(x, y);
    }
}
