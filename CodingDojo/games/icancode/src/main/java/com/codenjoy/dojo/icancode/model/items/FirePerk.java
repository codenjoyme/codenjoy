package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.FieldItem;
import com.codenjoy.dojo.icancode.model.Hero;
import com.codenjoy.dojo.icancode.model.Item;

public class FirePerk extends AbstractPerk {

    public FirePerk() {
        super(Elements.FIRE_PERK);
    }

    // For reflection
    public FirePerk(Elements element) {
        super(element);
    }

    @Override
    protected void toggle(Hero hero) {
        if (!hero.isFlying()) {
            hero.setCanFire(true);
            this.removeFromCell();
        }
    }
}
