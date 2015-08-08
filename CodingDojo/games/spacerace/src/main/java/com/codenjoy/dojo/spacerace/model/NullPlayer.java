package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.spacerace.services.Events;

/**
 * Created by indigo on 08.08.2015.
 */
public class NullPlayer extends Player {
    public NullPlayer() {
        super(null);
    }

    public void event(Events event) {
        // do nothing
    }
}
