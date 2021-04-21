package com.codenjoy.dojo.services.semifinal;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.nullobj.NullParameter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SemifinalStatus {

    private Integer tick;
    private int count;
    private Boolean enabled;
    private Integer timeout;
    private Boolean percentage;
    private Integer limit;
    private Boolean resetBoard;
    private Boolean shuffleBoard;

    public SemifinalStatus(int tick, int count, SemifinalSettings settings) {
        this.count = count;
        if (settings.isEnabledValue() instanceof NullParameter) {
            this.enabled = false;
            return;
        }
        this.tick = tick;
        this.enabled = settings.isEnabled();
        this.timeout = settings.getTimeout();
        this.percentage = settings.isPercentage();
        this.limit = settings.getLimit();
        this.shuffleBoard = settings.isShuffleBoard();
        this.resetBoard = settings.isResetBoard();
    }
}
