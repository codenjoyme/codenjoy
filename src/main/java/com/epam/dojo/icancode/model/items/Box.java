package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;

/**
 * Created by oleksandr.baglai on 24.06.2016.
 */
public class Box extends BaseItem {

    public Box(Elements el) {
        super(new ItemLogicType[]{ItemLogicType.IMPASSABLE, ItemLogicType.BOX}, el);
    }
}