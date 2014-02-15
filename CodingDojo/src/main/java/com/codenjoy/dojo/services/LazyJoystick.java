package com.codenjoy.dojo.services;

/**
 * Когда пользователь зарегистрировался в игре создается новая игра в движке и джойстик игрока где-то там сохраняется во фреймворке.
 * Часто джойстик неразрывно связан с героем игрока, который бегает по полю. Так вот если этот герой помрет, и на его место появится новый
 * надо как-то вот тот изначально сохраненный в недрах фреймворка джойстик обновить. Приходится дергать постоянно game.
 * Кроме того из за ассинхронности ответов пришлось буфферизировать в этом джойстике ответ клиента, а по tick() передавать сохраненное
 * значение сервису.
 */
public class LazyJoystick implements Joystick, Tickable {

    enum Command {
        DOWN, LEFT, RIGHT, UP, ACT;
    }

    private final Game game;
    private PlayerSpy player;

    private Command command;
    private int[] parameters;

    public LazyJoystick(Game game, PlayerSpy player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void down() {
        command = Command.DOWN;
    }

    @Override
    public void up() {
        command = Command.UP;
    }

    @Override
    public void left() {
        command = Command.LEFT;
    }

    @Override
    public void right() {
        command = Command.RIGHT;
    }

    @Override
    public void act(int... p) {
        command = Command.ACT;
        parameters = p;
    }

    @Override
    public void tick() {
        if (command == null) return;

        switch (command) {
            case DOWN: game.getJoystick().down(); break;
            case LEFT: game.getJoystick().left(); break;
            case RIGHT: game.getJoystick().right(); break;
            case UP: game.getJoystick().up(); break;
            case ACT: game.getJoystick().act(parameters); break;
        }

        parameters = null;
        command = null;
        player.act();
    }
}
