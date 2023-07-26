package com.codenjoy.dojo.expansion.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.expansion.model.Player;
import com.codenjoy.dojo.expansion.model.levels.items.FeatureItem;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.services.printer.state.State;

import java.util.List;

public interface Item extends State<Element, Player> {

    void action(Item item, boolean comeInOrLeave);

    Cell cell();

    List<Item> getItemsInSameCell();

    void joinCell(Cell cell);

    boolean has(FeatureItem feature);

    void leaveCell();
}
