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
import java.util.function.Consumer;

@Component
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

    private Parameter<Boolean> enabled;
    private Parameter<Integer> timeout;
    private Parameter<Boolean> percentage;
    private Parameter<Integer> limit;
    private Parameter<Boolean> resetBoard;
    private Parameter<Boolean> shuffleBoard;
    
    @Value("${game.semifinal.enabled}")
    public void setEnabled(boolean input) {
        if (enabled == null) {
            enabled = checkbox(ENABLED, input);
        } else {
            enabled.update(input);
        }
    }

    @Value("${game.semifinal.timeout}")
    public void setTimeout(int input) {
        if (timeout == null) {
            timeout = editbox(TIMEOUT, input);
        } else {
            timeout.update(input);
        }
    }

    @Value("${game.semifinal.limit.percentage}")
    public void setPercentage(boolean input) {
        if (percentage == null) {
            percentage = checkbox(PERCENTAGE, input);
        } else {
            percentage.update(input);
        }
    }

    @Value("${game.semifinal.limit.value}")
    public void setLimit(int input) {
        if (limit == null) {
            limit = editbox(LIMIT, input);
        } else {
            limit.update(input);
        }
    }

    @Value("${game.semifinal.board.reset}")
    public void setResetBoard(boolean input) {
        if (resetBoard == null) {
            resetBoard = checkbox(RESET_BOARD, input);
        } else {
            resetBoard.update(input);
        }
    }

    @Value("${game.semifinal.board.shuffle}")
    public void setShuffleBoard(boolean input) {
        if (shuffleBoard == null) {
            shuffleBoard = checkbox(SHUFFLE_BOARD, input);
        } else {
            shuffleBoard.update(input);
        }
    }
    
    public SemifinalSettings clone() {
        return new SemifinalSettings(
                enabled.clone(),
                timeout.clone(),
                percentage.clone(),
                limit.clone(),
                resetBoard.clone(),
                shuffleBoard.clone());
    }

    public void apply(SemifinalSettings input) {
        input = input.clone();

        enabled = input.enabled;
        percentage = input.percentage;
        limit = input.limit;
        timeout = input.timeout;
        resetBoard = input.resetBoard;
        shuffleBoard = input.shuffleBoard;
    }

    // TODO test me
    public List<Parameter> parameters() {
        return new LinkedList<>(){{
            add(enabled);
            add(timeout);
            add(percentage);
            add(limit);
            add(resetBoard);
            add(shuffleBoard);
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
        trySetFrom(parameters, ENABLED, enabled);
        trySetFrom(parameters, TIMEOUT, timeout);
        trySetFrom(parameters, PERCENTAGE, percentage);
        trySetFrom(parameters, LIMIT, limit);
        trySetFrom(parameters, RESET_BOARD, resetBoard);
        trySetFrom(parameters, SHUFFLE_BOARD, shuffleBoard);
    }

    private void trySetFrom(List<Parameter> parameters, String name, Parameter parameter) {
        Parameter result = parameters.stream()
                .filter(p -> p.getName().equals(name))
                .findAny()
                .orElse(null);

        if (result != null) {
            try {
                parameter.update(result.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }

    public int getTimeout() {
        return timeout.getValue();
    }

    public boolean isPercentage() {
        return percentage.getValue();
    }

    public int getLimit() {
        return limit.getValue();
    }

    public boolean isResetBoard() {
        return resetBoard.getValue();
    }

    public boolean isShuffleBoard() {
        return shuffleBoard.getValue();
    }
}
