package com.epam.dojo.expansion.model.levels;

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


import com.codenjoy.dojo.services.State;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.levels.items.FeatureItem;

import java.util.List;

/**
 * Created by Mikhail_Udalyi on 01.07.2016.
 */
public interface Item extends State<Elements, Player> {
    void action(Item item, boolean comeInOrLeave);

    Cell getCell();

    List<Item> getItemsInSameCell();

    void setCell(Cell value);

    boolean hasFeature(FeatureItem feature);

    Cell removeFromCell();
}
