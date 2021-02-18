package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.FieldItem;
import com.codenjoy.dojo.icancode.model.Hero;
import com.codenjoy.dojo.icancode.model.Item;

public class FirePerk extends FieldItem {

    public FirePerk() {
        super(Elements.FIRE_PERK, PASSABLE);
    }

    // For reflection
    public FirePerk(Elements element) {
        super(element);
    }

    @Override
    public void action(Item item) {
        HeroItem heroItem = getIf(item, HeroItem.class);
        if (heroItem == null) {
            return;
        }

        Hero hero = heroItem.getHero();
        if (!hero.isFlying()) {
            hero.setCanFire(true);
            this.removeFromCell();
        }
    }
}
