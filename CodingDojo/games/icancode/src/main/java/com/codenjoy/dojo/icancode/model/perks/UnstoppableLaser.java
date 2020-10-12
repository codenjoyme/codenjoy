package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.Elements;

public class UnstoppableLaser extends AbstractPerk {

    public UnstoppableLaser(Elements element) {
        super(element);
    }

    public UnstoppableLaser(Elements element, Timer availability, Timer activity) {
        super(element, availability, activity);
    }
}
