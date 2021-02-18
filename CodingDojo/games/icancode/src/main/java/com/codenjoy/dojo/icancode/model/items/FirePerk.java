package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Hero;

public class FirePerk extends AbstractPerk {

    public FirePerk() {
        super(Elements.FIRE_PERK);
    }

    // For reflection
    public FirePerk(Elements element) {
        super(element);
    }

    @Override
    protected void activate(Hero hero) {
        hero.setCanFire(true);
    }
}
