package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.snake.model.artifacts.*;

import java.util.LinkedList;
import java.util.List;

public class Snake implements Field, Game {

	private Hero snake;
    private Walls walls;
	private Stone stone;
	private int size;
	private Apple apple;
    private SnakeFactory snakeFactory;
    private ArtifactGenerator generator;
    private int maxLength;
    private Printer printer;

    public Snake(ArtifactGenerator generator, Walls walls, int size, PrinterFactory factory) {
        this(generator, new SnakeFactory() {
            @Override
            public Hero create(int x, int y) {
                return new Hero(x, y);
            }
        }, walls, size, factory);
    }

    public Snake(ArtifactGenerator generator, SnakeFactory snakeFactory, Walls walls, int size, PrinterFactory factory) {
	    this.generator = generator;
	    this.snakeFactory = snakeFactory;
        if (size%2 == 0) {
            size++;
        }
	    this.size = size;
        this.walls = walls;
        this.printer = factory.getPrinter(this.getReader(), null);

        newGame();
	}

    /**
	 * метод настраивает систему так, что при съедании одного яблока автоматически появляется другое.
	 */
	private void generateNewApple() {
		apple = generator.generateApple(snake, apple, stone, walls, size);
		apple.onEat(new Runnable() {
            @Override
            public void run() {
                generateNewApple();
            }
        });
	}

    // аналогично с камнем - только схели - он сразу появился в другом месте.
    private void generateNewStone() {
        stone = generator.generateStone(snake, apple, walls, size);
        stone.onEat(new Runnable() {
            @Override
            public void run() {
                generateNewStone();
            }
        });
    }

    @Override
	public Stone getStone() {  		
		return stone;				
	}

    @Override
    public Walls getWalls() {
        return walls;
    }

    @Override
	public Element getAt(Point point) {
		if (stone.itsMe(point)) {
			return stone; 
		}
		
		if (apple.itsMe(point)) {
			return apple;
		}
		
		// получается я свой хвост немогу укусить, потому как я за ним двинусь а он отползет
		// вроде логично
		if (snake.itsMyTail(point)) {
			return new EmptySpace(point);
		}		
		
		if (snake.itsMyBody(point)) {
			return snake;
			// TODO тут если поменять на
			// return new EmptySpace(point);
            // можно будет наезжать на себя не умирая
		}
		
		if (isWall(point)) {
			return new Wall(point);
            // TODO тут если поменять на
            // return new EmptySpace(point);
            // можно будет наезжать на стенку не умирая
		}
		
		return new EmptySpace(point);
	}

    @Override
    public void newGame() {
        int position = (size - 1)/2;
        snake = snakeFactory.create(position, position);
        generateNewStone();
        generateNewApple();
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    private boolean isWall(Point point) {
		return walls.itsMe(point);
	}

    @Override
    public Joystick getJoystick() {
        return snake;
    }

    @Override
    public int getMaxScore() {
        return maxLength;
    }

    @Override
    public int getCurrentScore() {
        return snake.getLength();
    }

    @Override
    public boolean isGameOver() {
		return !snake.isAlive();
	}

    @Override
	public Apple getApple() {
		return apple;
	}

    @Override
	public int getSize() {
		return size;
	}

    @Override
    public Hero getSnake() {
        return snake;
    }

    @Override
    public void tick() {
        if (!snake.isAlive()) return;

        snake.walk(this);
        maxLength = Math.max(maxLength, snake.getLength());
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void clearScore() { // TODO test me
        maxLength = 0;
    }

    @Override
    public Point getHero() {
        return snake.getHead();
    }

    public BoardReader getReader() {
        return new BoardReader() {
            private int size = Snake.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();

                for (Wall wall : walls) {
                    result.add(wall);
                }

                for (Tail tail : snake) {
                    result.add(tail);
                }
                result.add(apple);
                result.add(stone);

                return result;
            }
        };
    }
}