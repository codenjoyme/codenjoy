package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;

/**
 * Created by Mikhail_Udalyi on 10.06.2016.
 */
public class None extends BaseItem {

    public None(Point point) {
        super(new ItemLogicType[] {}, Elements.EMPTY);
        move(point);
    }
}
