package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;
import com.epam.dojo.icancode.model.Player;

/**
 * Created by Mikhail_Udalyi on 09.06.2016.
 */
public class Gold extends BaseItem {

    public boolean hidden;

    public Gold(Elements el) {
        super(new ItemLogicType[] {ItemLogicType.PASSABLE, ItemLogicType.GOLD}, el);
        hidden = false;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (hidden) {
            return Elements.FLOOR;
        } else {
            return super.state(player, alsoAtPoint);
        }
    }

    @Override
    public void action(BaseItem item) {
        if (item instanceof Hero) {
            Hero hero = (Hero) item;
            if (!hero.isFlying()) {
                hero.pickUpGold();
                hidden = true;
            }
        }
    }

    public void reset() {
        hidden = false;
    }
}
