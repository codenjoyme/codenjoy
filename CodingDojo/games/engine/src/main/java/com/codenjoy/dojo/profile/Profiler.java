package com.codenjoy.dojo.profile;

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
