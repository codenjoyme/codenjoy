package com.codenjoy.dojo.profile;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import java.util.HashMap;
import java.util.Map;

public class Profiler {

    Map<String, Long> phasesAll = new HashMap<String, Long>();
    Map<String, Long> phases = new HashMap<String, Long>();
    private long time;

    public void start() {
        time = now();
    }

    private long now() {
        return System.currentTimeMillis();
    }

    public void done(String phase) {
        long delta = now() - time;

        phases.put(phase, delta);

        if (phasesAll.containsKey(phase)) {
            delta += phasesAll.get(phase);
        }
        phasesAll.put(phase, delta);

        start();
    }

    @Override
    public String toString() {
        return phasesAll.toString();
    }

    public void print() {
        System.out.println(this);
        System.out.println("--------------------------------------------------");
    }

    public long get(String phase) {
        return phasesAll.get(phase);
    }
}
