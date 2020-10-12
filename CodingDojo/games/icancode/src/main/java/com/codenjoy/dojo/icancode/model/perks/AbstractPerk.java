package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.BaseItem;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.services.Tickable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractPerk extends BaseItem implements Tickable {

    private String value;
    private final Timer availability;
    private final Timer activity;

    public AbstractPerk(Elements element) {
        this(element, StringUtils.EMPTY, Timer.empty(), Timer.empty());
    }

    public AbstractPerk(Elements element, Timer availability, Timer activity) {
        this(element, StringUtils.EMPTY, availability, activity);
    }

    public AbstractPerk(Elements element, String value, Timer availability, Timer activity) {
        super(element);
        this.value = value;
        this.availability = availability;
        this.activity = activity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
