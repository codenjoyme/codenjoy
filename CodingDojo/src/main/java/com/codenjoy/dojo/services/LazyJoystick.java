package com.codenjoy.dojo.services;

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
    public void tick() {
        if (direction == null && parameters == null) return;

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
        player.act();
    }
}
