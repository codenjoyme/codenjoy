package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.FieldItem;
import com.codenjoy.dojo.icancode.model.Hero;
import com.codenjoy.dojo.icancode.model.Item;

public abstract class AbstractPerk extends FieldItem {

    public AbstractPerk(Elements element) {
        super(element);
    }

    @Override
    public void action(Item item) {
        HeroItem heroItem = getIf(item, HeroItem.class);
        if (heroItem != null) {
            toggle(heroItem.getHero());
        }
    }

    protected abstract void toggle(Hero hero);
}
