package com.codenjoy.dojo.pong.client.ai;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.pong.client.Board;
import com.codenjoy.dojo.pong.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.List;
import java.util.Random;

public class PongSolver implements Solver<Board> {

    int previousBallPosition;
    int verticalBallDirection;

    public PongSolver(Dice dice) {

    }

    @Override
    public String get(final Board board) {
        Point ball = board.getBall();

        Random random = new Random();
        int rateCoefficient = random.nextInt(2);

        if (ball != null) {
            verticalBallDirection = ball.getY() - previousBallPosition + rateCoefficient;
            previousBallPosition = ball.getY();
            List<Point> me = board.getMe();
            String direction = getDirectionString(ball, me);
            return direction;
        }
        return "";
    }

    private String getDirectionString(Point ball, List<Point> me) {
        String direction = "";
        int ballY = ball.getY();
        Point myPosition;
        if (verticalBallDirection > 0) {
            myPosition = me.get(0);
        } else if (verticalBallDirection < 0){
            myPosition = me.get(me.size() -1);
        } else {
            myPosition = me.get(me.size()/2);
        }
        int myY = myPosition.getY();
        if (myY < ballY) {
            direction =  Direction.DOWN.toString();
        } else if (myY > ballY) {
            direction =  Direction.UP.toString();
        }
        return direction;
    }

    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new PongSolver(new RandomDice()),
                new Board());
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new PongSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
