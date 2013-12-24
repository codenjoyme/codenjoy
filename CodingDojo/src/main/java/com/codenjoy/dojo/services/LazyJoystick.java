package com.codenjoy.dojo.services;

/**
 * Когда пользователь зарегистрировался в игре создается новая игра в движке и джойстик игрока где-то там сохраняется во фреймворке.
 * Часто джойстик неразрывно связан с героем игрока, который бегает по полю. Так вот если этот герой помрет, и на его место появится новый
 * надо как-то вот тот изначально сохраненный в недрах фреймворка джойстик обновить. Делаем это через сей декоратор.
 * TODO Когда-нибудь я это пофикшу
 * @see LazyJoystick#getJoystick()
 */
public class LazyJoystick implements Joystick {

    private Joystick joystick;

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public void down() {
        joystick.down();
    }

    @Override
    public void up() {
        joystick.up();
    }

    @Override
    public void left() {
        joystick.left();
    }

    @Override
    public void right() {
        joystick.right();
    }

    @Override
    public void act() {
        joystick.act();
    }

    public Joystick getJoystick() {
        return joystick;
    }
}
