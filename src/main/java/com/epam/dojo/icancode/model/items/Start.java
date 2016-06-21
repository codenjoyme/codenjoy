package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */

public class Start extends BaseItem {

    public Start(Elements el) {
        super(new ItemLogicType[]{ItemLogicType.PASSABLE, ItemLogicType.START}, el);
    }
}
