package com.epam.dojo.expansion.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.epam.dojo.expansion.model.levels.Level;
import com.epam.dojo.expansion.model.levels.Levels;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.dojo.expansion.model.levels.Levels.MULTI1;
import static com.epam.dojo.expansion.model.levels.Levels.MULTI2;
import static com.epam.dojo.expansion.model.levels.Levels.MULTI3;
import static java.util.stream.Collectors.toList;

/**
 * Created by Oleksandr_Baglai on 2017-09-08.
 */
public class SettingsWrapper {
    private final Parameter<Integer> boardSize;
    private final Parameter<Boolean> waitingOthers;
    private final List<Parameter<String>> levels;
    private final int total;

    public SettingsWrapper(Settings settings) {
        levels = new LinkedList<>();
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(20);
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
}
