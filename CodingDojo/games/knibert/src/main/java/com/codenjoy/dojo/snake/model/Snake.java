package com.codenjoy.dojo.snake.model;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.games.snake.Element;
import com.codenjoy.dojo.snake.model.artifacts.*;
import com.codenjoy.dojo.snake.services.GameSettings;

import java.util.Arrays;
import java.util.function.Consumer;

import static com.codenjoy.dojo.snake.services.GameSettings.Keys.START_SNAKE_LENGTH;

public class Snake implements Field {

    private Walls walls;
    private Stone stone;
    private int size;
    private Apple apple;
    private ArtifactGenerator generator;
    private int startLength;
    private Player player;
    private GameSettings settings;

    public Snake(ArtifactGenerator generator, Walls walls, int size, GameSettings settings) {
        this.generator = generator;
        this.startLength = settings.integer(START_SNAKE_LENGTH);
        this.settings = settings;
        this.size = size;
        this.walls = walls;
    }

    /**
     * метод настраивает систему так, что при съедании одного яблока автоматически появляется другое.
     */
    private void generateNewApple(Hero snake) {
        apple = generator.generateApple(snake, apple, stone, walls, size);
        apple.onEat(() -> generateNewApple(snake));
    }

    // аналогично с камнем - только съели - он сразу появился в другом месте.
    private void generateNewStone(Hero snake) {
        stone = generator.generateStone(snake, apple, walls, size);
        stone.onEat(() -> generateNewStone(snake));
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
    public Affectable getAt(Point point) {
        if (stone.itsMe(point)) {
            return stone; 
        }
        
        if (apple.itsMe(point)) {
            return apple;
        }
        
        // получается я свой хвост немогу укусить, потому как я за ним двинусь а он отползет
        // но только если мы не растем сейчас (на старте или от съеденного яблока)
        // вроде логично
        if (snake().itsMyTail(point) && snake().getGrowBy() == 0) {
            return new EmptySpace(point);
        }        
        
        if (snake().itsMyBody(point)) {
            return snake();
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
    public void newGame(Player player) {
        this.player = player;
        player.newHero(this);
    }

    @Override
    public void clearScore() {
        newGame(player);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public void remove(Player player) {
        this.player = null;
    }

    @Override
    public Hero createSnake() {
        Hero snake = Hero.createHero(size, startLength);
        generateNewStone(snake);
        generateNewApple(snake);
        return snake;
    }

    private boolean isWall(Point point) {
        return walls.itsMe(point);
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
    public Hero snake() {
        return player.getHero();
    }

    @Override
    public Player player() {
        return player;
    }

    @Override
    public void tick() {
        if (!snake().isAlive()) return;

        snake().walk(this);
    }

    @Override
    public BoardReader<Player> reader() {
        return new BoardReader<>() {
            private int size = Snake.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
                processor.accept(walls);
                processor.accept(snake());
                processor.accept(Arrays.asList(apple, stone));
            }
        };
    }
}
