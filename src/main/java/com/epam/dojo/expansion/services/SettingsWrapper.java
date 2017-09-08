package com.epam.dojo.expansion.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.epam.dojo.expansion.model.levels.Levels;

import java.util.LinkedList;
import java.util.List;

import static com.epam.dojo.expansion.model.levels.Levels.*;
import static java.util.stream.Collectors.toList;

/**
 * Created by Oleksandr_Baglai on 2017-09-08.
 */
public final class SettingsWrapper {

    public static SettingsWrapper data;

    private final Parameter<Integer> boardSize;
    private final Parameter<Boolean> waitingOthers;
    private final List<Parameter<String>> levels;
    private final int total;
    private final Parameter<Integer> leaveForceCount;

    public static SettingsWrapper setup(Settings settings) {
        return new SettingsWrapper(settings);
    }

    public static SettingsWrapper setup() {
        return setup(new SettingsImpl());
    }

    private SettingsWrapper(Settings settings) {
        data = this;

        levels = new LinkedList<>();
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(20);
        leaveForceCount = settings.addEditBox("Leave force count").type(Integer.class).def(0);
        waitingOthers = settings.addEditBox("Waiting others").type(Boolean.class).def(false);
        levels.add(settings.addEditBox("Multiple level 1").type(String.class).def(MULTI1));
        levels.add(settings.addEditBox("Multiple level 2").type(String.class).def(MULTI2));
        levels.add(settings.addEditBox("Multiple level 3").type(String.class).def(MULTI3));
        total = Levels.collectSingle(boardSize()).get().size();
    }

    public int boardSize() {
        return boardSize.getValue();
    }

    public List<String> levels() {
        return levels.stream().map(p -> p.getValue()).collect(toList());
    }

    public boolean waitingOthers() {
        return waitingOthers.getValue();
    }

    public int totalSingleLevels() {
        return total;
    }

    public int leaveForceCount() {
        return leaveForceCount.getValue();
    }

    // setters for testing

    public SettingsWrapper leaveForceCount(int count) {
        leaveForceCount.update(count);
        return this;
    }
}
