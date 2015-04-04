package com.codenjoy.dojo.minesweeper.client.ai.vaa25;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.Coord;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.Field;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.PathFinder;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.Person;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class PathFinderAdapter {
    private Board board;
    private Coord target;
    private Field field;

    public PathFinderAdapter(Board board, Point target) {
        this.board = board;
        this.target = new Coord(target.getX() + 1, target.getY() + 1);
    }

    List<Direction> execute() {
        Coord from = convertPointToCoord(board.getMe());
        createFieldWithBoard();
        createPersonInField(from);
        field.print();
        PathFinder pathFinder = new PathFinder(field);
        List<Coord> way = pathFinder.findPath(from, target);
        List<Direction> result = new ArrayList<Direction>();
        for (int i = 1; i < way.size(); i++) {
            Coord coord = way.get(i);
            result.add(getDirection(from, coord));
            from = coord;
        }
        return result;
    }

    private void createPersonInField(Coord from) {
        Person person = new Person(20);
        person.setCoord(from);
        field.setPerson(person);
    }

    private void createFieldWithBoard() {
        int size = board.size();
        field = new Field(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Elements element = board.getAt(i, j);
                if (element == Elements.BORDER || element == Elements.HIDDEN) {
                    field.setWall(new Coord(i, j));
                }
            }
        }
    }

    private Coord convertPointToCoord(Point point) {
        return new Coord(point.getX(), point.getY());
    }

    private Direction getDirection(Coord from, Coord to) {
        if (from.getX() == to.getX()) {
            if (to.getY() < from.getY()) return Direction.UP;
            if (to.getY() > from.getY()) return Direction.DOWN;
        }
        if (from.getY() == to.getY()) {
            if (to.getX() < from.getX()) return Direction.LEFT;
            if (to.getX() > from.getX()) return Direction.RIGHT;
        }
        return null;
    }

}
