package net.tetris.services;

import net.tetris.dom.TetrisJoystik;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* User: serhiy.zelenin
* Date: 10/27/12
* Time: 9:04 PM
*/
public class MockAdvancedTetrisJoystik extends MockTetrisJoystik implements TetrisJoystik {

    @Override
    public void left(int delta) {
        calls.add("left=" + delta);
    }

    @Override
    public void right(int delta) {
        calls.add("right=" + delta);
    }

}
