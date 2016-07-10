package net.tetris.services;

import com.codenjoy.dojo.services.Joystick;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergii_Zelenin on 7/10/2016.
 */
public class MockTetrisJoystik implements Joystick {
    protected List<String> calls = new ArrayList<>();

    public void down() {
        calls.add("drop");
    }

    @Override
    public void up() {

    }

    @Override
    public void left() {
        calls.add("left=" + 1);
    }

    @Override
    public void right() {
        calls.add("right=" + 1);
    }

    @Override
    public void act(int... p) {

    }

    public void act(int times) {
        calls.add("rotate=" + times);
    }

    @Override
    public String toString() {
        return StringUtils.join(calls, ",");
    }
}
