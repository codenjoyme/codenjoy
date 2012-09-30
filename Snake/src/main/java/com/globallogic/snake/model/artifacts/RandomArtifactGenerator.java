package com.globallogic.snake.model.artifacts;

import java.util.Random;

import com.globallogic.snake.model.Direction;
import com.globallogic.snake.model.Snake;

public class RandomArtifactGenerator implements ArtifactGenerator {

	@Override
	public Stone generateStone(Snake snake, Apple apple, int boardSize) {
		int x = 0;
		int y = 0;
        boolean noSoGoodPlace = true;
        do {
			y = random(boardSize);
			x = random(boardSize);

            boolean onSnake = snake.itsMe(new Point(x, y));
            boolean onApple = apple.itsMe(new Point(x, y));

            boolean onSnakeWayWhenGoRight = (snake.getX() + 1) <= x && x <= boardSize && y == snake.getY() && snake.getDirection().equals(Direction.RIGHT);
            boolean onSnakeWayWhenGoLeft =0 <= x && x <= (snake.getX() - 1) && y == snake.getY() && snake.getDirection().equals(Direction.LEFT);
            boolean onSnakeWayWhenGoDown = (snake.getY() + 1) <= y && y <= boardSize && x == snake.getX() && snake.getDirection().equals(Direction.DOWN);
            boolean onSnakeWayWhenGoUp = 0 <= y && y <= (snake.getY() - 1) && x == snake.getX() && snake.getDirection().equals(Direction.UP);
            boolean onSnakeWay = onSnakeWayWhenGoRight || onSnakeWayWhenGoLeft || onSnakeWayWhenGoDown || onSnakeWayWhenGoUp;

            int LEFT = 0;
            int TOP = 0;
            int RIGHT = boardSize - 1;
            int BOTTOM = boardSize - 1;
            boolean whenLeftTopStandstill = apple.itsMe(new Point(LEFT, TOP)) && ((x == LEFT && y == TOP + 1) || (x == LEFT + 1 && y == TOP));
            boolean whenLeftBottomStandstill = apple.itsMe(new Point(LEFT, BOTTOM)) && ((x == LEFT && y == BOTTOM - 1) || (x == LEFT + 1 && y == BOTTOM));
            boolean whenRightTopStandstill = apple.itsMe(new Point(RIGHT, TOP)) && ((x == RIGHT && y == TOP + 1) || (x == RIGHT - 1 && y == TOP)) ;
            boolean whenRightBottomStandstill = apple.itsMe(new Point(RIGHT, BOTTOM)) && ((x == RIGHT && y == BOTTOM - 1) || (x == RIGHT - 1 && y == BOTTOM)) ;
            boolean whenStandstill = whenLeftTopStandstill || whenLeftBottomStandstill || whenRightTopStandstill || whenRightBottomStandstill;

            noSoGoodPlace = onSnake || onSnakeWay || onApple || whenStandstill;
		} while (noSoGoodPlace);

		return new Stone(x, y);
	}
	
	private int random(int boardSize) {
		return new Random().nextInt(boardSize);
	}

	@Override
	public Apple generateApple(Snake snake, Stone stone, int boardSize) {
		int x = 0;
		int y = 0;
		do {
			x = random(boardSize);
			y = random(boardSize);
		} while (snake.itsMe(new Point(x, y)) || stone.itsMe(new Point(x, y)));
		
		return new Apple(x, y);
	}

}
