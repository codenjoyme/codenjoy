package com.codenjoy.dojo.icancode.model.items.perks;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Settingable;
import com.codenjoy.dojo.icancode.model.items.RenewableItem;
import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.services.Tickable;

public abstract class Perk extends RenewableItem implements Tickable, Settingable {

    protected GameSettings settings;

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

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }
}
