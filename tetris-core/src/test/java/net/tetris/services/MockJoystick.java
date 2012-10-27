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
    public void moveLeft(int delta) {
        calls.add("left=" + delta);
    }

    @Override
    public void moveRight(int delta) {
        calls.add("right=" + delta);
    }

    @Override
    public void drop() {
        calls.add("drop");
    }

    @Override
    public void rotate(int times) {
        calls.add("rotate=" + times);
    }

    @Override
    public String toString() {
        return StringUtils.join(calls, ",");
    }
}
