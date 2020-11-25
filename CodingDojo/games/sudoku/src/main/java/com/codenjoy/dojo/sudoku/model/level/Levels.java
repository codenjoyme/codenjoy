package com.codenjoy.dojo.sudoku.model.level;

import com.codenjoy.dojo.sudoku.model.level.levels.*;
import com.codenjoy.dojo.sudoku.services.SettingsWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Levels {

    public static void setup() {
        AtomicInteger index = new AtomicInteger();
        all().forEach(level -> SettingsWrapper.data.addLevel(index.incrementAndGet(), level));
    }

    public static List<Level> all() {
        return Arrays.asList(
                new Level1(),
                new Level2()
        );
    }
}
