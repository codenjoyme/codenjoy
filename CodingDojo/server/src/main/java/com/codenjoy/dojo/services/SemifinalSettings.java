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

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Oleksandr_Baglai on 2019-10-13.
 */
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SemifinalSettings {

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
}
