package com.codenjoy.dojo.loderunner.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.loderunner.client.Board;
import com.codenjoy.dojo.loderunner.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.*;

public class ApofigSolver implements Solver<Board> {

    private DeikstraFindWay way;

    public ApofigSolver(Dice dice) {
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(final Board board) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();
                if (board.aWall(x, y)) return false;

                Point newPt = where.change(from);
                int nx = newPt.getX();
                int ny = newPt.getY();

                if (board.isOutOfField(nx, ny)) return false;

                if (board.aWall(nx, ny)) return false;

                if (where == Direction.UP && !board.aLadder(x, y)) return false;

                int yd = Direction.DOWN.changeY(y);
                if (where != Direction.DOWN &&
                        !new PointImpl(x, yd).isOutOf(board.size()) &&
                        !board.aWall(x, yd) &&
                        !board.aLadder(x, yd) &&
                        !board.aLadder(x, y) &&
                        !board.aPipe(x, y)) return false;

                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                if (board.isEnemyAt(atWay.getX(), atWay.getY())) return false;
                if (board.isOtherHeroAt(atWay.getX(), atWay.getY())) return false;
                return true;
            }
        };
    }

    @Override
    public String get(final Board board) {
        if (board.isGameOver()) return "";
        List<Direction> result = getDirections(board);
        if (result.isEmpty()) return "";
        return result.get(0).toString();
    }

    public List<Direction> getDirections(Board board) {
        int size = board.size();
        Point from = board.getMe();
        List<Point> to = board.get(Elements.GOLD);
        DeikstraFindWay.Possible map = possible(board);
        return way.getShortestWay(size, from, to, map);
    }

    public DeikstraFindWay getWay() {
        return way;
    }

    public static void main(String[] args) {
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new ApofigSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
