package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.springframework.util.StringUtils;

/**
 * Когда пользователь зарегистрировался в игре создается новая игра в движке и джойстик игрока где-то там сохраняется во фреймворке.
 * Часто джойстик неразрывно связан с героем игрока, который бегает по полю. Так вот если этот герой помрет, и на его место появится новый
 * надо как-то вот тот изначально сохраненный в недрах фреймворка джойстик обновить. Приходится дергать постоянно game.
 * Кроме того из за ассинхронности ответов пришлось буфферизировать в этом джойстике ответ клиента, а по tick() передавать сохраненное
 * значение сервису.
 */
public class LazyJoystick implements Joystick, Tickable {

    enum Direction {
        DOWN, LEFT, RIGHT, UP;

    }
    private final Game game;

    private PlayerSpy player;
    private Direction direction;

    private String message;
    private int[] parameters;
    private boolean firstAct;

    public LazyJoystick(Game game, PlayerSpy player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
        firstAct = (parameters != null);
    }

    @Override
    public void up() {
        direction = Direction.UP;
        firstAct = (parameters != null);
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
        firstAct = (parameters != null);
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
        firstAct = (parameters != null);
    }

    @Override
    public void act(int... p) {
        parameters = p;
        firstAct = (direction == null);
    }

    @Override
    public void message(String command) {
        message = command;
    }

    @Override
    public void tick() {
        if (direction == null && parameters == null && message == null) return; // TODO test me

        if (!StringUtils.isEmpty(message)) {
            game.getJoystick().message(message);
        }

        if (parameters != null && firstAct) {
            game.getJoystick().act(parameters);
        }

        if (direction != null) {
            switch (direction) {
                case DOWN: game.getJoystick().down(); break;
                case LEFT: game.getJoystick().left(); break;
                case RIGHT: game.getJoystick().right(); break;
                case UP: game.getJoystick().up(); break;
            }
        }

        if (parameters != null && !firstAct) {
            game.getJoystick().act(parameters);
        }

        parameters = null;
        direction = null;
        message = null;
        player.act();
    }
}
