package com.epam.dojo.expansion.model.items;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.enums.FeatureItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by oleksandr.baglai on 24.06.2016.
 */
public class Box extends FieldItem {

    public Box(Elements el) {
        super(el, new FeatureItem[]{FeatureItem.IMPASSABLE});
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        List<Object> objects = Arrays.asList(alsoAtPoint);
        for (Object object : objects) {
            if (object == player.getHero()) {
                return Elements.ROBO_FLYING_ON_BOX;
            }
            if (object.getClass().equals(Hero.class)) {
                return Elements.ROBO_OTHER_FLYING_ON_BOX;
            }
        }
        return Elements.BOX;
    }
}