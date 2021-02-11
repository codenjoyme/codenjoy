package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.settings.CheckBox;
import com.codenjoy.dojo.services.settings.EditBox;
import com.codenjoy.dojo.services.settings.Parameter;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2019-10-13.
 */
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SemifinalSettings {

    public static final String SEMIFINAL = "Semifinal";

    public static final String ENABLED = SEMIFINAL + " enabled";
    public static final String TIMEOUT = SEMIFINAL + " timeout";
    public static final String PERCENTAGE = SEMIFINAL + " percentage";
    public static final String LIMIT = SEMIFINAL + " limit";
    public static final String RESET_BOARD = SEMIFINAL + " reset board";
    public static final String SHUFFLE_BOARD = SEMIFINAL + " shuffle board";

    @Value("${game.semifinal.enabled}")
    private boolean enabled;

    @Value("${game.semifinal.timeout}")
    private int timeout;

    @Value("${game.semifinal.limit.percentage}")
    private boolean percentage;

    @Value("${game.semifinal.limit.value}")
    private int limit;

    @Value("${game.semifinal.board.reset}")
    private boolean resetBoard;

    @Value("${game.semifinal.board.shuffle}")
    private boolean shuffleBoard;

    public SemifinalSettings clone() {
        return new SemifinalSettings(enabled, timeout, percentage, limit, resetBoard, shuffleBoard);
    }

    public void apply(SemifinalSettings input) {
        enabled = input.enabled;
        percentage = input.percentage;
        limit = input.limit;
        timeout = input.timeout;
        resetBoard = input.resetBoard;
        shuffleBoard = input.shuffleBoard;
    }

    // TODO test me
    public List<Parameter> parameters() {
        return new LinkedList<Parameter>(){{
            add(checkbox(ENABLED, enabled));
            add(editbox(TIMEOUT, timeout));
            add(checkbox(PERCENTAGE, percentage));
            add(editbox(LIMIT, limit));
            add(checkbox(RESET_BOARD, resetBoard));
            add(checkbox(SHUFFLE_BOARD, shuffleBoard));
        }};
    }

    private Parameter editbox(String name, int value) {
        return new EditBox(name).type(Integer.class).def(value);
    }

    private Parameter checkbox(String name, boolean value) {
        return new CheckBox(name).type(Boolean.class).def(value);
    }

    // TODO test me
    public void update(List<Parameter> parameters) {
        enabled      = (boolean)get(parameters, ENABLED);
        timeout      = (int)get(parameters, TIMEOUT);
        percentage   = (boolean)get(parameters, PERCENTAGE);
        limit        = (int)get(parameters, LIMIT);
        resetBoard   = (boolean)get(parameters, RESET_BOARD);
        shuffleBoard = (boolean)get(parameters, SHUFFLE_BOARD);
    }

    private Object get(List<Parameter> parameters, String name) {
        return parameters.stream()
                .filter(p -> p.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Parameter with name " + name + " not found"))
                .getValue();
    }
}
