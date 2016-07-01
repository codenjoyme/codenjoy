package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.enums.FeatureItem;

/**
 * Created by oleksandr.baglai on 24.06.2016.
 */
public class Box extends BaseItem {

    public Box(Elements el) {
        super(el, new FeatureItem[]{FeatureItem.IMPASSABLE});
    }
}