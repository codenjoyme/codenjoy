package com.codenjoy.dojo.football.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.football.client.Board;
import com.codenjoy.dojo.football.model.Actions;
import com.codenjoy.dojo.football.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.ArrayList;
import java.util.List;

/**
 * Это алгоритм твоего бота. Он будет запускаться в игру с первым
 * зарегистрировавшимся игроком, чтобы ему не было скучно играть самому.
 * Реализуй его как хочешь, хоть на Random.
 * Для его запуска воспользуйся методом {@see ApofigSolver#main}
 */
public class DefaultSolver implements Solver<Board> {

    private DeikstraFindWay way;

    public DefaultSolver(Dice dice) {
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
                //if (board.isBombAt(nx, ny)) return false;

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
        if (board.isGameOver()) return "";
        
        String resultString = "";
        
        //for (Point from : board.getMe()) {
        Point from = board.getMe();
	        
        	List<Direction> result = getDirections(board, from);
	        if (result.isEmpty()) {
	        	resultString =  resultString + ((resultString.length() == 0) ? "" : ", ") +
	        			"act("+(int)(Math.random()*100)%4+")";
	        } else {
	        	Direction direction = result.get(0);
	        	resultString =  resultString + ((resultString.length() == 0) ? "" : ", ") +
	        			direction.toString()+"";
	        			
	        	if(direction == Direction.UP) {
	        		//resultString =  resultString + ((resultString.length() == 0) ? "" : ", ") +
	        	    //    		"act(" + Actions.UP.getValue() + ", " + from.getX() + ", " + from.getY(); 	
	        		resultString = resultString + ", act("+Actions.HIT_UP.getValue()+", 3)";
	        	} else if(direction == Direction.RIGHT) {
	        		//resultString =  resultString + ((resultString.length() == 0) ? "" : ", ") +
	        		//		"act(" + Actions.RIGHT.getValue() + ", " + from.getX() + ", " + from.getY(); 	
	        		resultString = resultString + ", act("+Actions.HIT_RIGHT.getValue()+", 3)";
	        	} else if(direction == Direction.DOWN) {
	        		//resultString =  resultString + ((resultString.length() == 0) ? "" : ", ") +
	        		//		"act(" + Actions.DOWN.getValue() + ", " + from.getX() + ", " + from.getY(); 	
	        		resultString = resultString + ", act("+Actions.HIT_DOWN.getValue()+", 3)";
	        	} else if(direction == Direction.LEFT) {
	        		//resultString =  resultString + ((resultString.length() == 0) ? "" : ", ") +
	        		//		"act(" + Actions.LEFT.getValue() + ", " + from.getX() + ", " + from.getY(); 	
	        		resultString = resultString + ", act("+Actions.HIT_LEFT.getValue()+", 3)";
	        	} else {
	        		//resultString =  resultString + ((resultString.length() == 0) ? "" : ", ") +
	        		//		direction.toString(); 	
	        		resultString = resultString + ", act("+Actions.STOP_BALL.getValue()+")";
	        	}
	        }
        //}
        
    	return resultString;
    }

    public List<Direction> getDirections(Board board, Point from) {
    
        int size = board.size();
        //Point from = board.getMe();
        List<Point> to = new ArrayList<Point>();
        
        Point ball = board.getBall();
        if((ball.itsMe(from)) || (board.isBallOnMyTeam())) {
        	to.add(board.getEnemyGoal());
        } else {
        	to.add(ball);
        }
        
        DeikstraFindWay.Possible map = possible(board);
        return way.getShortestWay(size, from, to, map);
    }

    /**
     * Метод для запуска игры с текущим ботом. Служит для отладки.
     */
    public static void main(String[] args) {
        //LocalGameRunner.run(new GameRunner(),
        //        new DefaultSolver(new RandomDice()),
        //        new Board());
        
        start("apofig@gmail.com", WebSocketRunner.Host.LOCAL);
        //start("user2", WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new DefaultSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
