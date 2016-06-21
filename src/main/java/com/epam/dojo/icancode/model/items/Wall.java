package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;

/**
 * Артефакт Стена на поле
 */
public class Wall extends BaseItem {

    public Wall(Elements el) {
        super(new ItemLogicType[] {ItemLogicType.IMPASSABLE}, el);
    }
}
