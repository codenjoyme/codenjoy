package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.interfaces.IItem;

/**
 * Created by Mikhail_Udalyi on 09.06.2016.
 */
public class Hole extends BaseItem {

    public Hole(Elements el) {
        super(el);
    }

    @Override
    public void action(IItem item) {
        if (item instanceof Hero) {
            Hero hero = (Hero)item;
            if (!hero.isFlying()) {
                hero.dieOnHole();
            }
        }
    }
}
