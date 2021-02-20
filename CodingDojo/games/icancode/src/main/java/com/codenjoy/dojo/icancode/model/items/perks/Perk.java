package com.codenjoy.dojo.icancode.model.items.perks;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.items.RenewableItem;
import com.codenjoy.dojo.services.Tickable;

public abstract class Perk extends RenewableItem implements Tickable {

    public Perk(Elements element) {
        super(element);
    }

    @Override
    public void tick() {
        // do nothing
    }

    public boolean isAvailable() {
        return true;
    }

    public boolean isActive() {
        return true;
    }
}
