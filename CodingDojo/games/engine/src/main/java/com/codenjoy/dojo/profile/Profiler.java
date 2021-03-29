package com.codenjoy.dojo.profile;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Profiler {

    static class AverageTime {
        int count;
        long time;

        public void add(long delta) {
            time += delta;
            count++;
        }

        @Override
        public String toString() {
            return String.format("AverageTime{%s times, total %s ms, average %s ms}", count, time, ((double)time)/count);
        }
    }

    private Map<String, AverageTime> phasesAll = new ConcurrentHashMap<>();
    private Map<String, Long> phases = new ConcurrentHashMap<String, Long>();
    private long time;

    public synchronized void start() {
        time = now();
    }

    private long now() {
        return System.currentTimeMillis();
    }

    public synchronized void done(String phase) {
        if (!log.isDebugEnabled()) return;

        long delta = now() - time;

        phases.put(phase, delta);

        if (!phasesAll.containsKey(phase)) {
            phasesAll.put(phase, new AverageTime());
        }
        phasesAll.get(phase).add(delta);

        start();
    }

    @Override
    public String toString() {
        return phasesAll.toString();
    }

    public void print() {
        if (!log.isDebugEnabled()) return;

        log.debug(this.toString());
        log.debug("--------------------------------------------------");
    }

    public void print(String phase) {
        if (!log.isDebugEnabled()) return;

        log.debug("--------------------------------------------------");
        log.debug(phase + " = " + phases.get(phase));
        log.debug("--------------------------------------------------");
    }

    public String get(String phase) {
        if (!log.isDebugEnabled()) return StringUtils.EMPTY;

        if (!phasesAll.containsKey(phase)) {
            return "phase not found: " + phase;
        }
        return phasesAll.get(phase).toString();
    }
}
