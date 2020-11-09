package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.BaseItem;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Tickable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractPerk extends BaseItem implements Tickable {

    private final String value;
    private final Timer availability;
    private final Timer activity;

    public AbstractPerk(Elements element) {
        this(element, StringUtils.EMPTY);
    }

    public AbstractPerk(Elements element, String value) {
        super(element);
        this.value = value;
        this.availability = new Timer(SettingsWrapper.data.perkAvailability());
        this.activity = new Timer(SettingsWrapper.data.perkActivity());
    }

    public String getValue() {
        return value;
    }

    public boolean isAvailable() {
        return !availability.isTimeUp();
    }

    public boolean isActive() {
        return !activity.isTimeUp();
    }

    @Override
    public void tick() {
        availability.tick();
        activity.tick();
    }
}
