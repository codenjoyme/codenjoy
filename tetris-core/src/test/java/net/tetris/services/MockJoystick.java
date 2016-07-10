package net.tetris.services;

import net.tetris.dom.Joystick;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* User: serhiy.zelenin
* Date: 10/27/12
* Time: 9:04 PM
*/
public class MockJoystick implements Joystick {
    private List<String> calls = new ArrayList<>();

    @Override
    public void left(int delta) {
        calls.add("left=" + delta);
    }

    @Override
    public void right(int delta) {
        calls.add("right=" + delta);
    }

    @Override
    public void down() {
        calls.add("drop");
    }

    @Override
    public void act(int times) {
        calls.add("rotate=" + times);
    }

    @Override
    public String toString() {
        return StringUtils.join(calls, ",");
    }
}
