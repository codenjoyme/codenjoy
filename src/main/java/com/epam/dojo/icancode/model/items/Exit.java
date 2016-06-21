package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */

public class Exit extends BaseItem {

    public Exit(Elements el) {
        super(new ItemLogicType[]{ItemLogicType.PASSABLE, ItemLogicType.EXIT}, el);
    }

    @Override
    public void action(BaseItem item) {
        if (item instanceof Hero) {
            ((Hero)item).setWin();
        }
    }
}
