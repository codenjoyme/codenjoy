package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.LazyJoystick;

/**
 * User: sanja
 * Date: 17.08.13
 * Time: 19:52
 */
public class SingleTanks implements Game {    // TODO test me

    private Player player;
    private Tanks tanks;
    private LazyJoystick joystick;

    public SingleTanks(Tanks tanks, EventListener listener) {
        this.tanks = tanks;
        this.player = new Player(listener);
        this.joystick = new LazyJoystick();
    }

    @Override
    public Joystick getJoystick() {
        return joystick;
    }

    @Override
    public int getMaxScore() {
        return player.getMaxScore();
    }

    @Override
    public int getCurrentScore() {
        return player.getScore();
    }

    @Override
    public boolean isGameOver() {
        return !player.getTank().isAlive();
    }

    @Override
    public void newGame() {
        player.newGame();
        joystick.setJoystick(player.getTank());
        tanks.add(player);
    }

    @Override
    public String getBoardAsString() {
        return tanks.getBoardAsString();
    }

    @Override
    public void destroy() {
        tanks.remove(player);
    }

    @Override
    public void clearScore() { // TODO test me
        player.clearScore();
    }

    @Override
    public void tick() {
       tanks.tick();
    }
}
