package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;

/**
 * Created by Mikhail_Udalyi on 09.06.2016.
 */
public class Floor extends BaseItem {

    public Floor(Elements el) {
        super(new ItemLogicType[]{ItemLogicType.PASSABLE}, el);
    }

    @Override
    public void action(BaseItem item) {

    }
}
