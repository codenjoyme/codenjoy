package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.items.BaseItem;
import com.epam.dojo.icancode.model.items.Hero;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public interface ICell extends Point, Fieldable {

    void addItem(BaseItem item);

    BaseItem getItem(ItemLogicType type);

    BaseItem getItem(int layer);

    void removeItem(BaseItem item);

    boolean is(ItemLogicType type);

    boolean isPassable();

    void comeIn(BaseItem hero);
}
