package com.globallogic.snake.model.artifacts;

import java.util.Random;

import com.globallogic.snake.model.Direction;
import com.globallogic.snake.model.Snake;

public class RandomArtifactGenerator implements ArtifactGenerator {

	@Override
	public Stone generateStone(Snake snake, Apple apple, int boardSize) {
		int x;
		int y;
        boolean noSoGoodPlace;
        do {
			y = random(boardSize);
			x = random(boardSize);

            boolean onSnake = snake.itsMe(x, y);
            boolean onApple = (apple != null) && apple.itsMe(x, y);

            boolean onSnakeWayWhenGoRight = (snake.getX() + 1) <= x && x <= boardSize && y == snake.getY() && snake.getDirection().equals(Direction.RIGHT);
            boolean onSnakeWayWhenGoLeft =0 <= x && x <= (snake.getX() - 1) && y == snake.getY() && snake.getDirection().equals(Direction.LEFT);
            boolean onSnakeWayWhenGoDown = (snake.getY() + 1) <= y && y <= boardSize && x == snake.getX() && snake.getDirection().equals(Direction.DOWN);
            boolean onSnakeWayWhenGoUp = 0 <= y && y <= (snake.getY() - 1) && x == snake.getX() && snake.getDirection().equals(Direction.UP);
            boolean onSnakeWay = onSnakeWayWhenGoRight || onSnakeWayWhenGoLeft || onSnakeWayWhenGoDown || onSnakeWayWhenGoUp;

            boolean whenStandstill = isStandstill(apple, new Point(x, y), boardSize);

            noSoGoodPlace = onSnake || onSnakeWay || onApple || whenStandstill;
		} while (noSoGoodPlace);

		return new Stone(x, y);
	}

    private boolean isStandstill(Point apple, Point stone, int boardSize) {
        if (apple == null) {
            return false;
        }

        int LEFT = 0;
        int TOP = 0;
        int RIGHT = boardSize - 1;
        int BOTTOM = boardSize - 1;

        boolean whenLeftTopStandstill = apple.itsMe(LEFT, TOP) && (stone.itsMe(LEFT, TOP + 1) || stone.itsMe(LEFT + 1, TOP));
        boolean whenLeftBottomStandstill = apple.itsMe(LEFT, BOTTOM) && (stone.itsMe(LEFT, BOTTOM - 1) || stone.itsMe(LEFT + 1, BOTTOM));
        boolean whenRightTopStandstill = apple.itsMe(RIGHT, TOP) && (stone.itsMe(RIGHT, TOP + 1) || stone.itsMe(RIGHT - 1, TOP));
        boolean whenRightBottomStandstill = apple.itsMe(RIGHT, BOTTOM) && (stone.itsMe(RIGHT, BOTTOM - 1) || stone.itsMe(RIGHT - 1, BOTTOM));

        return whenLeftTopStandstill || whenLeftBottomStandstill || whenRightTopStandstill || whenRightBottomStandstill;
    }

    private int random(int boardSize) {
		return new Random().nextInt(boardSize);
	}

	@Override
	public Apple generateApple(Snake snake, Stone stone, int boardSize) {
		int x;
		int y;
        boolean noSoGoodPlace;
		do {
			x = random(boardSize);
			y = random(boardSize);

            boolean onSnake = snake.itsMe(x, y);
            boolean onStone = stone.itsMe(x, y);

            boolean whenStandstill = isStandstill(new Point(x, y), stone, boardSize);

            noSoGoodPlace = onSnake || onStone || whenStandstill;
        } while (noSoGoodPlace);
		
		return new Apple(x, y);
	}

}
