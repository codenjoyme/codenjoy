package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Slf4j
@Component
@ToString
@RequiredArgsConstructor
@Getter
public class StatisticService {

    private static final SimpleDateFormat DAY_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private final TimeService time;

    private long tick;
    private String tickTime;
    private long tickDuration;
    private int screenUpdatesCount;
    private int requestControlsCount;
    private int dealsCount;

    public void tick() {
        tick = now();
        tickTime = DAY_TIME_FORMATTER.format(tick);
    }

    private long now() {
        return time.now();
    }

    public void screenUpdatesCount(int count) {
        log.debug("PSI.tick().sendScreenUpdates() {} endpoints", count);
        screenUpdatesCount = count;
    }

    public void requestControlsCount(int count) {
        log.debug("PSI.tick().requestControls() {} players", count);
        requestControlsCount = count;
    }

    public void dealsCount(int count) {
        dealsCount = count;
    }

    public void tickDuration(long mills) {
        tickDuration = mills;
    }

    public void log(Pair<Integer, Integer> tick) {
        screenUpdatesCount(tick.getLeft());
        requestControlsCount(tick.getRight());
    }
}
