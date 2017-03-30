package com.codenjoy.dojo.kata.model.levels;

import java.util.Arrays;
import java.util.List;

/**
 * Created by indigo on 2017-03-30.
 */
public class WaitLevel extends NullLevel implements Level {
    
    @Override
    public String description() {
        return "Wait for next level. Please send 'act(1)' command.";
    }
}
