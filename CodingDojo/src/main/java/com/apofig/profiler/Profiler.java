package com.apofig.profiler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sanja on 15.02.14.
 */
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

    public void phase(String phase) {
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
        return phasesAll + "\n" + phases;
    }

    public void print() {
        System.out.println(this);
        System.out.println("--------------------------------------------------");
    }
}
