package com.codenjoy.dojo.spacerace.client.ai;

import java.util.List;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.spacerace.client.Board;
import com.codenjoy.dojo.spacerace.model.Elements;
import com.codenjoy.dojo.spacerace.services.GameRunner;

/**
 * Это алгоритм твоего бота. Он будет запускаться в игру с первым
 * зарегистрировавшимся игроком, чтобы ему не было скучно играть самому.
 * Реализуй его как хочешь, хоть на Random.
 * Для его запуска воспользуйся методом {@see ApofigSolver#main}
 */
public class ApofigSolver implements Solver<Board> {

    private DeikstraFindWay way;
    private int delay = 0;
    private boolean vpravo = true;

    public ApofigSolver(Dice dice) {
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(final Board board) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();
                if (board.isBarrierAt(x, y)) return false;

                Point newPt = where.change(from);
                int nx = newPt.getX();
                int ny = newPt.getY();

                if (board.isOutOfField(nx, ny)) return false;

                if (board.isBarrierAt(nx, ny)) return false;
                if (board.isBombAt(nx, ny)) return false;

                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                return true;
            }
        };
    }

    @Override
    public String get(final Board board) {
//        if (board.isGameOver()) return "";
//        List<Direction> result = getDirections(board);
//        if (result.isEmpty()) return "";
//        return result.get(0).toString();

        String result = "";
        int x = board.getMe().getX();

		if (vpravo && (x < board.size() - 5)||(x < 5)){
            result = Direction.RIGHT.toString();
            vpravo = true;
        }else {
            vpravo = false;
            result = Direction.LEFT.toString();
        }

        delay++;
        if(delay >= 3){
            result += Direction.UP.ACT.toString();
            delay = 0;
        }

        return result;
    }

    public List<Direction> getDirections(Board board) {
        int size = board.size();
        Point from = board.getMe();
        List<Point> to = board.get(Elements.GOLD);
        DeikstraFindWay.Possible map = possible(board);
        return way.getShortestWay(size, from, to, map);
    }

    /**
     * Метод для запуска игры с текущим ботом. Служит для отладки.
     */
    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new ApofigSolver(new RandomDice()),
                new Board());
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
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
